package com.jcsoft.emsystem.client;

import java.io.*;
import java.net.*;

public class SocketClient {
   private Socket _client;
   private String _site;
   private int _port = 8111;
   final private int RESP_LENGTH = 640;
   private byte[] _receivedCmdRespBuffer = new byte[RESP_LENGTH];


   public SocketClient(String site, int port)
   {
       try
       {
           _site = site;
           _port = port;
           _client = new Socket(site, port);
       }
       catch (UnknownHostException e)
       {
           e.printStackTrace();
       }
       catch (IOException e)
       {
           e.printStackTrace();
       }
   }

   public int send(byte[] content, int length)
   {
       try
       {
           if (_client == null)
           {
               _client = new Socket(_site, _port);
               if (_client == null)
               {
                   return -1;
               }
           }

           _client.getOutputStream().write(content, 0, length);
           _client.getOutputStream().flush();
       }
       catch (IOException e)
       {
           e.printStackTrace();
       }
       return -1;
   }

   public int sendComand(String cmd)
   {
       try
       {
           byte[] cmdBytes = cmd.getBytes();
           send(cmdBytes, cmdBytes.length);

           if (_client == null)
           {
               return -1;
           }

           InputStream iStream = _client.getInputStream();
           while (true)
           {
               int len = iStream.read(_receivedCmdRespBuffer);
               if (len == -1)
               {
                   return -1;
               }

               String text = new String(_receivedCmdRespBuffer, 0, len);
               if (text.startsWith("OK"))
               {
                   return 0;
               }
           }
       }
       catch(IOException e)
       {
           e.printStackTrace();
       }
       return -1;
   }

   public void closeSocket()
   {
       if (_client == null)
       {
           return;
       }

       try
       {
           _client.close();
       }
       catch(IOException e)
       {
           e.printStackTrace();
       }
   }
}
