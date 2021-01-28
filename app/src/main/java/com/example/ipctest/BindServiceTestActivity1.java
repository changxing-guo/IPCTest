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
    private BindServieDemo1 mService;
    private boolean bindIsConn = false;

    private TextView text_display;
    private Button bt_register;
    private Button bt_unregister;
    private Button bt_maxValue;

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

    /**
     * 可以看到，在使用这种方法进行客户端与服务端之间的交互是需要有一个强制类型转换的——在onServiceDisconnected()
     * 中获得一个经过转换的IBinder对象，我们必须将其转换为service类中的Binder实例的类型才能正确的调用其方法。而这
     * 强制类型转换其实就隐含了一个使用这种方法的条件：客户端和服务端应当在同一个进程中！不然在类型转换的时候也许会
     * 出现问题——在另一个进程中一定有这个Binder实例么？没有的话就不能完成强制类型转换。
     */
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        //系统会调用该方法以传递服务的　onBind() 方法返回的 IBinder。
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            //当系统调用 onServiceConnected() 回调方法时，我们可以使用接口定义的方法开始调用服务。
            mBinder = (BindServieDemo1.BinderDemo1) service;
            mService = mBinder.getServer();
            bindIsConn = true;
        }

        // Android系统会在与服务的连接意外中断时（例如当服务崩溃或被终止时）调用该方法。当客户端取消绑定时，系统
        // “绝对不会”调用该方法。
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
            bindIsConn = false;
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
        if(bindIsConn) return;
        Intent mIntent = new Intent(this, BindServieDemo1.class);
        bindService(mIntent, mServiceConnection, BIND_AUTO_CREATE);
        bindIsConn = true;
    }

    private void unRegisterBindService() {
        if (!bindIsConn) return;
        bindIsConn = false;
        unbindService(mServiceConnection);
    }

    private String getName() {
        if (mBinder!=null) return mService.getName();

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
            case R.id.bt_display:
                text_display.setText(Integer.toString(mService.maxValue(1,2)));
                break;

        }
    }

    private void initView()
    {


        text_display = (TextView) findViewById(R.id.display);
        //text_display.setOnClickListener(this);

        bt_register = (Button) findViewById(R.id.register);
        bt_register.setOnClickListener(this);

        bt_unregister = (Button) findViewById(R.id.unregister);
        bt_unregister.setOnClickListener(this);

        bt_maxValue = (Button) findViewById(R.id.bt_display);
        bt_maxValue.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        unRegisterBindService();
        super.onDestroy();
    }
}
