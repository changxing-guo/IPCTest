package com.example.ipctest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * 这是一种比startService更复杂的启动方式，同时使用这种方式启动的service也能完成更多的事情，比如其他组件可向其发送请求，
 * 接受来自它的响应，甚至通过它来进行IPC等等。我们通常将绑定它的组件称为客户端，而称它为服务端。
 *
 * 如果要创建一个支持绑定的service，我们必须要重写它的onBind()方法。这个方法会返回一个IBinder对象，它是客户端用来和服务
 * 器进行交互的接口。而要得到IBinder接口，我们通常有三种方式：继承Binder类，使用Messenger类，使用AIDL。
 *
 * 要完成客户端与服务端的绑定，有两件事要做。一是在客户端完成bindService的调用以及相关配置，二是在服务端里面实现onBind()方法
 * 的重写，返回一个用做信息交互的IBinder接口。接下来我们就一块一块的来看它的实现方法。
 */

public class BindServieDemo1 extends Service {

    private static final String TAG = "BindServieDemo1";

    private BinderDemo1 mBinder = new BinderDemo1();

    class BinderDemo1 extends Binder {

        public void sayHello() {
            Log.d(TAG, "hello");
        }

        public String getName() {
            return "jack";
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }
}
