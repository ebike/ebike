package com.jcsoft.emsystem.processor;


import com.jcsoft.emsystem.client.IJCClientListener;
import com.jcsoft.emsystem.protocol.JCProtocol;

import java.util.ArrayList;
import java.util.List;

public class ProcessorManager {
    private static ProcessorManager _instance = null;
    //注意此处没有加锁，所以在使用时，需要在启动前进行初始化
    private List<IProcessor> _processors = new ArrayList<IProcessor>();

    public static ProcessorManager instance() {
        if (_instance == null) {
            _instance = new ProcessorManager();
        }
        return _instance;
    }

    private ProcessorManager() {
        init();
    }

    private void init() {
        registerProcessor(LoginRespProcessor.instance());
        registerProcessor(CommonRespFromServerProcessor.instance());
        registerProcessor(GetAllDevInfosRespProcessor.instance());
        registerProcessor(GetPositionRespProcessor.instance());
        registerProcessor(GetTrackRespProcessor.instance());
        registerProcessor(NotifyReqFromServerProcessor.instance());
        registerProcessor(QueryLocationReqFromServerProcessor.instance());
        registerProcessor(QueryParamsReqFromServerProcessor.instance());
        registerProcessor(RemoteCtrlRespProcessor.instance());
        registerProcessor(SetParamsReqFromServerProcessor.instance());
        registerProcessor(TextMessageReqFromServerProcessor.instance());
        registerProcessor(KickUserReqFromServerProcessor.instance());
    }

    public void process(JCProtocol protocol, IJCClientListener listener) {
        if (protocol == null || listener == null) {
            //如果为空，则对接收的消息不做处理
            return;
        }

        try {
            //调用处理器进行处理，各个处理器查看是否是自己需要处理的数据
            //，如果是则处理，不是则不处理，交由其它处理器处理
            for (IProcessor processor : _processors) {
                if (processor.process(protocol, listener) == 0) {
                    return;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void registerProcessor(IProcessor processor) {
        _processors.add(processor);
    }

    public void unregisterProcessor(IProcessor processor) {
        _processors.remove(processor);
    }

}
