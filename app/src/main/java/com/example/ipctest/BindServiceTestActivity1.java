package com.example.ipctest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BindServiceTestActivity1 extends Activity implements View.OnClickListener {
    private static final String TAG = "BindServiceTestActivity";

    private BindServieDemo1.BinderDemo1 mBinder;
    private TextView textView;

    private Button bt_register;
    private Button bt_unregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_service_test1);

        //registerBindService();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        //系统会调用该方法以传递服务的　onBind() 方法返回的 IBinder。
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            //当系统调用 onServiceConnected() 回调方法时，我们可以使用接口定义的方法开始调用服务。
            mBinder = (BindServieDemo1.BinderDemo1) service;
        }

        // Android系统会在与服务的连接意外中断时（例如当服务崩溃或被终止时）调用该方法。当客户端取消绑定时，系统
        // “绝对不会”调用该方法。
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
        }
    };

    /**
     * bindService()方法需要三个参数，第一个是一个intent，我们都很熟悉——它和startService()里面那个intent是一样的，
     * 用来指定启动哪一个service以及传递一些数据过去。第二个参数可能就有点陌生了，这是个啥？这是实现客户端与服务端通
     * 信的一个关键类。要想实现它，就必须重写两个回调方法：onServiceConnected()以及onServiceDisconnected()，而我
     * 们可以通过这两个回调方法得到服务端里面的IBinder对象
     *
     * bindService()方法的第三个参数是一个int值，还叫flag(这flag立的)，它是用来做什么的呢？它是一个指示绑定选项的
     * 标志，通常应该是 BIND_AUTO_CREATE，以便创建尚未激活的服务。 其他可能的值为 BIND_DEBUG_UNBIND 和
     * BIND_NOT_FOREGROUND，或 0（表示无）。
     */

    private void registerBindService() {
        if(mBinder!=null) return;
        Intent mIntent = new Intent(this, BindServieDemo1.class);
        bindService(mIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void unRegisterBindService() {
        if (mBinder == null) return;
        unbindService(mServiceConnection);
    }

    private String getName() {
        if (mBinder!=null) return mBinder.getName();

        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                registerBindService();
                break;
            case R.id.unregister:
                unRegisterBindService();
                break;

        }
    }

    private void initView()
    {
        bt_register = (Button) findViewById(R.id.register);
        bt_register.setOnClickListener(this);

        bt_unregister = (Button) findViewById(R.id.unregister);
        bt_unregister.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        unRegisterBindService();
        super.onDestroy();
    }
}
