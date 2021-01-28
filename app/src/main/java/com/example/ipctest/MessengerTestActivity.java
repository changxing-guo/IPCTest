package com.example.ipctest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MessengerTestActivity extends Activity {

    private static final String TAG = "MessengerTestActivity";

    //客户端消息标志
    private static final int MSG_FROM_CLIENT = 0x10001;
    //服务端消息标志
    private static final int MSG_TO_CLIENT = 0x10002;
    //传递消息的参数标志
    private static final String NICK_NAME = "nickName";
    //判断binder是否已连接
    boolean isConn = false;
    //服务端Messenger
    private Messenger mService = null;

    // 服务端的action和package_name
    private static final String server_action = "com.example.ipctest.MessengerServer";
    private static final String server_packagename = "com.example.ipctest";

    private Button bt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_test);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBindService();
    }

    @Override
    protected void onStop() {
        unRegisterBindService();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            //接收onBind()传回来的IBinder，并用它构造Messenger
            mService = new Messenger(service);
            isConn = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
            isConn = false;
        }
    };


    private void registerBindService() {
        if (isConn) return;
        Intent mIntent = new Intent();
        //绑定服务端的服务，此处的action是service在Manifests文件里面声明的
        mIntent.setAction(server_action);
        //不要忘记了包名，不写会报错
        mIntent.setPackage(server_packagename);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    }

    private void unRegisterBindService() {
        if (!isConn) return;
        isConn = false;
        unbindService(mConnection);
    }

    private void sendMesage(String str) {
        Message msgFromClient = new Message();
        Bundle toServiceDate = new Bundle();
        toServiceDate.putString(NICK_NAME, str);
        msgFromClient.what = MSG_FROM_CLIENT;
        msgFromClient.setData(toServiceDate);
        //将自己定义的messenger设置在要发送出去的msg里面，在服务器那边才能通过这个messenger将消息发送回来客户端
        msgFromClient.replyTo = mClient;
        if (isConn) {
            try {
                mService.send(msgFromClient);
            } catch (RemoteException e) {
                Log.d(TAG, "sendMesage: error");
                e.printStackTrace();
            }
        }
    }

    /**
     * 注册一个messenger，监听系统消息
     * mClient ，在前面说过了，messenger的通讯都是这样的，要想发送消息，必须在接收端定义一个messenger，用来接收数据，然后将这个
     * messenger的实例传回给发送端，让发送端调用这个实例的messenger.send方法来发送消息
     */

    static class ClientHaner extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TO_CLIENT:
                    Bundle data = msg.getData();
                    Log.d(TAG, "handleMessage: " + data.getString(NICK_NAME));
                    break;
                case 2:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private Messenger mClient = new Messenger(new ClientHaner());

    void initView() {
        bt1 = (Button) findViewById(R.id.messenger1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMesage("这里是客户端");
            }
        });
    }

}
