package com.example.ipctest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 另一个组件通过调用startService()方法，就可以启动一个特定的service，并且这将导致service中的onStartCommand()方法被调用。
 * 在调用startService()方法的时候，其他组件需要在方法中传递一个intent参数，然后service将会在onStartCommand()中接收这个intent，
 * 并获取一些数据。比如此时某个Activity要将一些数据存入数据库中，我就可以通过intent把数据传入service，然后让service去进行
 * 连接数据库，存储数据等操作，而此时用户可以执行其他的任何操作——甚至包括销毁那个Activity——这并不会影响service存储数据这件事。
 *
 * 当一个service通过这种方式启动之后，它的生命周期就已经不受启动它的组件影响了，它可以在后台无限期的运行下去，只要service自身
 * 没有调用stopSelf()并且其他的组件没有调用针对它的stopService()。
 *
 * 另外，如果确定了使用这种方式启动service并且不希望这个service被绑定的话，那么也许除了传统的创建一个类继承service之外我们有一个更好的选择——继承IntentService。
 *
 * 请查看ServiceDemo2
 */

public class ServiceDemo1 extends Service {

    private static final String TAG = "ServiceDemo1";
    public ServiceDemo1() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        //当其他组件调用bindService()方法时，此方法将会被调用
        //如果不想让这个service被绑定，在此返回null即可
        Log.d(TAG, "onBind: ");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        //只在service创建的时候调用一次，可以在此进行一些一次性的初始化操作
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        //当其他组件调用startService()方法时，此方法将会被调用
        //在这里进行这个service主要的操作
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        //service调用的最后一个方法
        //在此进行资源的回收
        super.onDestroy();
    }
}
