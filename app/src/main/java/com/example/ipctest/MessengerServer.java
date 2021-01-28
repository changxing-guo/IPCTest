package com.example.ipctest;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * Messenger 双向通讯实例
 * <p>
 * 1，在服务端，我们要构造一个messenger用来接收信息，而这个messenger发送消息的方向是固定的，只能从client发送到service端
 * 2，在客户端，我们要想接收到服务端传过来的消息，同样也要构造一个messenger来接收消息
 */
public class MessengerServer extends Service {

    private static final String TAG = "MessengerServer";

    //客户端消息标志
    private static final int MSG_FROM_CLIENT = 0x10001;
    //服务端消息标志
    private static final int MSG_TO_CLIENT = 0x10002;
    //传递消息的参数标志
    private static final String NICK_NAME = "nickName";


    private class MessagerHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FROM_CLIENT:
                    // 根据msg.what接收客户端的信息
                    Log.d(TAG, "MSG_FROM_CLIENT");
                    Bundle b1 = msg.getData();
                    Log.d(TAG, b1.getString(NICK_NAME));

                    //解析信息后再回调给客户端
                    //构造传回客户端的数据bundle
                    Message msgToClient = Message.obtain(msg);
                    Bundle toClicentDate = new Bundle();
                    toClicentDate.putString(NICK_NAME, "这是服务端发出的消息");
                    msgToClient.setData(toClicentDate);
                    msgToClient.what = MSG_TO_CLIENT;

                    //传回Client
                    try {
                        //msg.replyTo在客户端有定义，其实这就是第二个messenger，在messenger中，
                        // 发送消息和接收消息都必须要有一个messenger，而在这个从客户端获取的msg中，其实在客户端已经通过msg.reply=xxx，设置了接收消息的messenger了
                        msg.replyTo.send(msgToClient);
                    } catch (RemoteException e) {
                        Log.d(TAG, "handleMessage: to client error");

                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }

        }
    }

    // 使用实现的Handler创建Messenger对象
    final Messenger messenger = new Messenger(new MessagerHandler());

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        //返回给客户端一个IBinder实例
        Log.d(TAG, "onBind");
        return messenger.getBinder();
    }
}
