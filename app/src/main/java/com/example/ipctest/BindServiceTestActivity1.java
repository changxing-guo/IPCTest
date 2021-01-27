package com.example.ipctest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class BindServiceTestActivity1 extends Activity {
    private static final String TAG = "BindServiceTestActivity";

    private BindServieDemo1.BinderDemo1 mBinder;
    private TextView textView;
    private boolean isConn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_service_test1);

        registerBindService();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*while (true) {

            if (isConn) break;
            try{
                wait(1000);

            } catch (InterruptedException e) {

            }
        }

        if (mBinder != null){
            String a = mBinder.getName();
            textView = (TextView)findViewById(R.id.name);

            textView.setText(a);
        } else {

            Log.d(TAG, "mBinder is null ");
        }*/


    }

    private ServiceConnection mServiceConnection = new ServiceConnection ()
    {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            mBinder = (BindServieDemo1.BinderDemo1) service;
            isConn = true;
            if (mBinder != null){
                String a = mBinder.getName();
                textView = (TextView)findViewById(R.id.name);

                textView.setText(a);
            } else {

                Log.d(TAG, "mBinder is null ");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
            mBinder = null;
        }
    };


    private void registerBindService()
    {

        Intent mIntent = new Intent(this, BindServieDemo1.class);
        bindService(mIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void disRegisterBindService()
    {

        unbindService(mServiceConnection);
    }

    @Override
    protected void onDestroy() {
        disRegisterBindService();
        super.onDestroy();
    }
}
