package com.example.ipctest;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */

/**
 * 如果是扩建Service类的话，通常情况下我们需要新建一个用于执行工作的新线程，因为默认情况下service将工作于应用的主线程，
 * 而这将会降低所有正在运行的Activity的性能。而IntentService就不同了。它是Service的子类，它使用工作线程来注意的处理
 * 所有的startService请求。如果你不要求这个service要同时处理多个请求，那么继承这个类显然要比直接继承Service好到不知
 * 道哪里去了——IntentService已经做了这些事：
 * <p>
 * 创建默认的工作线程，用于在应用的主线程外执行传递给 onStartCommand() 的所有 Intent
 * 创建工作队列，用于将一个 Intent 逐一传递给 onHandleIntent() 实现，这样的话就永远不必担心多线程问题了
 * 在处理完所有启动请求后停止服务，从此妈妈再也不用担心我忘记调用 stopSelf() 了
 * 提供 onBind() 的默认实现（返回 null）
 * 提供 onStartCommand() 的默认实现，可将 Intent 依次发送到工作队列和 onHandleIntent() 实现
 * 因此我们只需要实现onHandleIntent()方法来完成具体的功能逻辑就可以了。
 */

public class ServiceDemo2 extends InterServiceDemo {

    private static final String TAG = "ServiceDemo2";

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.ipctest.action.FOO";
    private static final String ACTION_BAZ = "com.example.ipctest.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.ipctest.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.ipctest.extra.PARAM2";

    public ServiceDemo2() {
        super("ServiceDemo2");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, ServiceDemo2.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, ServiceDemo2.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 相比上面的继承service实现，这个确实要简单许多。但是要注意的是，如果需要重写其他的方法，比如onDestroy()方法，
     * 一定不要删掉它的超类实现！因为它的超类实现里面也许包括了对工作线程还有工作队列的初始化以及销毁等操作，如果没
     * 有了的话很容易出问题。
     */
    /*
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    */
}
