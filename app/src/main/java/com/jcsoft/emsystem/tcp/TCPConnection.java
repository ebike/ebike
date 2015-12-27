package com.jcsoft.emsystem.tcp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.ConditionVariable;

import com.jcsoft.emsystem.client.JCLog;
import com.jcsoft.emsystem.tcp.IJCProtocolScanner.ScanResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class TCPConnection {
    private final static String TAG = "JCSoft";
    private Socket _socket;
    private RecvThread _recvThread;
    private SendThread _sendThread;
    private Context _context = null;
    private String _site = "www.gnets.cn";
    private int _port = 7777;
    private Object _streamLock = new Object();
    private OutputStream _os = null;
    private InputStream _is = null;
    private ITCPConnectionListener _listener = null;
    private IJCProtocolScanner _scanner = null;
    private boolean _isFirstNotifyConnectFailure = true;
    private final int ONE_PROTOCOL_BUFFER_LENGTH = 2048;
    private byte[] _oneProtocolBuffer = new byte[ONE_PROTOCOL_BUFFER_LENGTH];
    private static TCPConnection _instance = null;
    private boolean _isInited = false;

    private TCPConnection() {
    }

    public static TCPConnection instance() {
        if (_instance == null) {
            _instance = new TCPConnection();
        }
        return _instance;
    }

    public void init(Context context, String site, int port) {
        JCLog.d(TAG, "init()!site = " + site + "! port = " + String.valueOf(port));
        if (!_isInited) {
            _context = context;
            _site = site;
            _port = port;
            _sendThread = new SendThread();
            _recvThread = new RecvThread();

            _sendThread.start();
            _recvThread.start();
            _isInited = true;
        }
    }

    public void uninit() {
        JCLog.d(TAG, "uninit()");
        _isInited = false;
        if (_recvThread != null) {
            disconnect();
            _recvThread.setExitFlag(true);
            _recvThread.interrupt();
            _sendThread.sendToList(new SendDataModel(true));
            _recvThread = null;
            _sendThread = null;
        }
    }

    private int connect() {
        try {
            synchronized (_streamLock) {
                if (_socket != null) {
                    _socket.close();
                    _socket = null;
                }
                _socket = new Socket(_site, _port);

                if (_socket != null) {
                    _os = _socket.getOutputStream();
                    _is = _socket.getInputStream();
                    return 0;
                }
                onConnectFailed();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        onConnectFailed();
        return -1;
    }

    public void disconnect() {
        synchronized (_streamLock) {
            if (_socket != null) {
                try {
                    _os = null;
                    _is = null;
                    _socket.close();
                    //释放资源
                    _socket = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void send(SendDataModel data) {
        try {
            _sendThread.sendToList(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onConnected() {
        if (_listener != null) {
            disconnect(); //先断开现有的连接，然后重连，防止发生错误提示的情况
            _listener.onConnected();
        }
    }

    private void onConnectFailed() {
        JCLog.d(TAG, "onConnectFailed()!");
        if (_isFirstNotifyConnectFailure) {
            if (_listener != null) {
                _listener.onConnectFailed();
            }
            _isFirstNotifyConnectFailure = false;
        }
    }

    private void onSendFailed(SendDataModel data) {
        if (_listener != null) {
            _listener.onSendFailed(data);
        }
    }

    private void onRecvData(byte[] data, int offset, int length) {
        if (!_isFirstNotifyConnectFailure) {
            _isFirstNotifyConnectFailure = true;
        }

        if (_listener != null) {
            if (length > ONE_PROTOCOL_BUFFER_LENGTH) {
                //收到的数据有错，不做其它处理
                JCLog.e(TAG, "The data is too long!");
                return;
            }
            System.arraycopy(data, offset, _oneProtocolBuffer, 0, length);
            _listener.onRecvData(_oneProtocolBuffer, length);
        }
    }

    public ITCPConnectionListener getListener() {
        return _listener;
    }

    public void setListener(ITCPConnectionListener listener) {
        _listener = listener;
    }

    public IJCProtocolScanner getScanner() {
        return _scanner;
    }

    public void setScanner(IJCProtocolScanner scanner) {
        _scanner = scanner;
    }

    public class SendThread extends Thread {
        private List<SendDataModel> _list = new ArrayList<SendDataModel>();
        private ConditionVariable _condV = new ConditionVariable();
        private NetState _receiver = new NetState();
        private IntentFilter _filter = new IntentFilter();

        public SendThread() {
            JCLog.d(TAG, "SendThread()!");
            try {
                _filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                _context.registerReceiver(_receiver, _filter);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void sendToList(SendDataModel data) {
            synchronized (_list) {
                _list.add(data);
                _condV.open();
            }
        }

        @Override
        public void run() {
            while (this.isAlive()) {
                try {
                    SendDataModel data = null;
                    synchronized (_list) {
                        if (_list.size() != 0) {
                            //检查队列里面有没有数据，如果有则发送
                            data = _list.get(0);
                        }
                    }

                    if (data == null) {
                        _condV.close();
                        _condV.block(60 * 60 * 1000); //60分钟后强制唤醒一次
                        continue;
                    }

                    if (data.isExit()) {
                        break;
                    }

                    try {
                        boolean isSent = false;
                        synchronized (_streamLock) {
                            if (_os != null) {
                                byte[] buffer = data.getProtocol().encode();
                                JCLog.d(TAG, "Send Data! Buffer length: " + String.valueOf(buffer.length));
                                _os.write(buffer);

                                _list.remove(data);
                                isSent = true;
                            }
                        }

                        if (!isSent) {
                            int ret = connect();
                            if (ret == 0) {
                                continue;
                            }

                            data.setDisconnected(true);
                            _list.remove(data);
                            onSendFailed(data);
                        }
                    } catch (Exception e) {
                        //通知上层发送失败
                        _list.remove(0);
                        e.printStackTrace();
                        onSendFailed(data);
                    }
                } catch (Exception ex) {
                    JCLog.e(TAG, ex.getMessage());
                }
            }
            try {
                _context.unregisterReceiver(_receiver);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            JCLog.d(TAG, "SendThread Exit!");
        }
    }

    public static void printByteArray(String prefix, byte[] data, int bufferPos, int length) {
        if (data == null) {
            return;
        }

        String str = prefix + ":";
        for (int i = bufferPos; i < bufferPos + length; i++) {
            String hex = String.format("0x%02X, ", data[i] & 0xFF);
            str += hex;
        }

        if (str.lastIndexOf(", ") != -1) {
            str = str.substring(0, str.length() - 2);
        }
        JCLog.d(TAG, str);
    }

    public class RecvThread extends Thread {
        private boolean _isExit = false;
        private final int BUFFER_LENGTH = 2 * 1024;         //设置缓存为2KB，能够满足大部分需要
        private byte[] _buffer = new byte[BUFFER_LENGTH];
        private int _bufferPos = 0;
        private int _newProtocolStartPos = 0;

        public RecvThread() {
        }

        @Override
        public void run() {
            while (this.isAlive()) {
                try {
                    if (_is != null) {
                        //此处有可能收到的数据是整个数据包的一部分，所以设置_bufferPos
                        if (_bufferPos < 0) {
                            _bufferPos = 0;
                            _newProtocolStartPos = 0;
                        }

                        JCLog.d(TAG, "Before read! offset = " + String.valueOf(_bufferPos) + "! length = "
                                + String.valueOf(_buffer.length - _bufferPos));
                        int readLength = 0;
                        readLength = _is.read(_buffer, _bufferPos, _buffer.length - _bufferPos);
                        if (_isExit) {
                            break;
                        }
                        JCLog.d(TAG, "Recv Data! length = " + String.valueOf(readLength));
                        if (readLength == -1) {
                            //连接断开
                            disconnect();
                            onConnectFailed();
                            sleep(100);
                            continue;
                        }
                        printByteArray("Recv Buffer", _buffer, _bufferPos, readLength);

                        //将收到的所有数据分解成单个的JCProtocol，交由上层处理
                        if (_scanner == null) {
                            onRecvData(_buffer, 0, readLength);
                            continue;
                        }

                        int totalLength = _bufferPos + readLength;
                        _bufferPos = 0;
                        preparseProcess(totalLength, totalLength);
                    } else {
                        sleep(100);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    if (_isExit) {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            JCLog.d(TAG, "RecvThread Exit!");
        }

        //totalLength表示整个数据包的长度，length表示还没有处理的长度, _bufferPos表示当前解析到的位置，_newProtocolStartPos表示新的解析位置
        private ScanResult preparseProcess(int totalLength, int length) {
            JCLog.d(TAG, "preparseProcess : length = " + String.valueOf(length)
                    + "! _bufferPos = " + String.valueOf(_bufferPos)
                    + "! _newProtocolStartPos = " + String.valueOf(_newProtocolStartPos));

            ScanResult result = _scanner.scanBuffer(_buffer, _newProtocolStartPos, _bufferPos + length - _newProtocolStartPos);
            JCLog.d(TAG, "Scan result: " + result.getEnumScanResult().toString() + "! unparsedPos = " + String.valueOf(result.getUnprocessedLocation()));
            switch (result.getEnumScanResult()) {
                case RESULT_End:
                    onRecvData(_buffer, _newProtocolStartPos, result.getUnprocessedLocation() - _newProtocolStartPos);
                    if (result.getUnprocessedLocation() < _bufferPos + length) {
                        //如果还有后续的数据需要处理，则继续递归处理
                        _bufferPos = result.getUnprocessedLocation();
                        _newProtocolStartPos = _bufferPos;

                        if (_bufferPos > BUFFER_LENGTH - 100)  //如果_bufferPos已经快到buffer的结尾了，则将剩余的数据拷贝到缓存开始处，并设置_bufferPos为0
                        {
                            int newTotalLength = length - _bufferPos;
                            System.arraycopy(_buffer, _bufferPos, _buffer, 0, newTotalLength);
                            _bufferPos = 0;
                            _newProtocolStartPos = 0;
                            preparseProcess(newTotalLength, newTotalLength);
                        } else {
                            preparseProcess(totalLength, totalLength - _bufferPos);
                        }
                        break;
                    }
                    _bufferPos = 0;
                    _newProtocolStartPos = 0;
                    break;
                case RESULT_NeedNextBuffer:
                    _bufferPos = result.getUnprocessedLocation();
                    break;
                case RESULT_Error:
                    //直接滤掉
                    _bufferPos = 0;
                    break;
                default:
                    break;
            }
            return result;
        }


        public void setExitFlag(boolean isExit) {
            _isExit = isExit;
        }
    }

    public class NetState extends BroadcastReceiver {
        private boolean _isFirstTime = true; //过滤掉第一次

        @Override
        public void onReceive(Context con, Intent arg1) {
            ConnectivityManager manager = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo gprs = manager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi = manager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (!gprs.isConnected() && !wifi.isConnected()) {
                // 连接断开或者未连接
                //disconnect();
            } else {
                //此处是主线程不能调用connect,通知上层，上层可以决定是否重连
                if (_isFirstTime) {
                    _isFirstTime = false;
                    return;
                }
                onConnected();
            }
        }
    }
}
