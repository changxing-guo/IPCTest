package com.example.ipctest;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

public class ServiceDemo3 extends Service {

    private static final String TAG = "ServiceDemo3";
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

            //执行一些操作
            stopSelf(msg.arg1);
        }
    }


    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);
        /**
         * - START_NOT_STICKY : 如果系统在 onStartCommand() 返回后终止服务，则除非有挂起 Intent 要传递，否则系统不会重建服务。
         *      这是最安全的选项，可以避免在不必要时以及应用能够轻松重启所有未完成的作业时运行服务。
         *
         * - START_STICKY : 如果系统在 onStartCommand() 返回后终止服务，则会重建服务并调用 onStartCommand()，但绝对不会重新传
         *      递最后一个 Intent。相反，除非有挂起 Intent 要启动服务（在这种情况下，将传递这些 Intent ），否则系统会通过空 Intent
         *      调用 onStartCommand()。这适用于不执行命令、但无限期运行并等待作业的媒体播放器（或类似服务）。
         *
         * - START_REDELIVER_INTENT : 如果系统在 onStartCommand() 返回后终止服务，则会重建服务，并通过传递给服务的最后一个 Intent
         *      调用 onStartCommand()。任何挂起 Intent 均依次传递。这适用于主动执行应该立即恢复的作业（例如下载文件）的服务。
         *
         *
         * - 除此之外，继承service来实现的service，一定要记得，停止这个service。因为除非系统必须回收内存资源，否则系统不会停止或销毁service。
         *      事实上，如果这个service是与可见组件绑定，其几乎永远不会被停止或销毁。在这种情况下，如果你忘记了在其工作完成之后将其停止，势必会
         *      造成系统资源的浪费以及电池电量的消耗，而这应当是我们要极力避免的。
         */

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onDestroy() {
        //super.onDestroy();
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}
