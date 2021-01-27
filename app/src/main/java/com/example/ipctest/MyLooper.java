package com.example.ipctest;

import android.os.Message;
import android.os.MessageQueue;
import android.util.Config;
import android.util.Printer;

public class MyLooper {

    //static变量，判断是否打印调试信息。
    private static final boolean DEBUG = false;
    private static final boolean localLOGV = DEBUG ? Config.LOGD : Config.LOGV;

    /**
     * sThreadLocal.get() will return null unless you've called prepare().
     *
     * 线程本地存储功能的封装，TLS，thread local storage,什么意思呢？因为存储要么在栈上，
     * 例如函数内定义的内部变量。要么在堆上，例如new或者malloc出来的东西
     *
     * 但是现在的系统比如Linux和windows都提供了线程本地存储空间，也就是这个存储空间是和线程相关的，一个线程内有一个内部存储空间，这样的话我把线程相关的东西就存储到
     * 这个线程的TLS中，就不用放在堆上而进行同步操作了。
     */

    private static final ThreadLocal sThreadLocal = new ThreadLocal();

    //消息队列，MessageQueue，看名字就知道是个queue..
    //final MessageQueue mQueue;
    volatile boolean mRun;

    //和本looper相关的那个线程，初始化为null
    Thread mThread;
    private Printer mLogging = null;

    //static变量，代表一个UI Process（也可能是service吧，这里默认就是UI）的主线程
    private static MyLooper mMainLooper = null;

    /** Initialize the current thread as a looper.
     * This gives you a chance to create handlers that then reference
     * this looper, before actually starting the loop. Be sure to call
     * {@link #loop()} after calling this method, and end it by calling
     * {@link #quit()}.
     */

    /**
     *  往TLS中设上这个Looper对象的，如果这个线程已经设过了looper的话就会报错
     *  这说明，一个线程只能设一个looper
     */
    public static final void prepare() {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new MyLooper());
    }

    /** Initialize the current thread as a looper, marking it as an application's main
     * looper. The main looper for your application is created by the Android environment,
     * so you should never need to call this function yourself.
     * {@link #prepare()}
     */

    //由framework设置的UI程序的主消息循环，注意，这个主消息循环是不会主动退出的
    /*public static final void prepareMainLooper() {
        prepare();
        setMainLooper(myLooper());
        //判断主消息循环是否能退出....
        //通过quit函数向looper发出退出申请
        //if (Process.supportsProcesses()) {
            myLooper().mQueue.mQuitAllowed = false;
        //}
    }*/

    private synchronized static void setMainLooper(MyLooper looper) {
        mMainLooper = looper;
    }

    /** Returns the application's main looper, which lives in the main thread of the application.
     */
    public synchronized static final MyLooper getMainLooper() {
        return mMainLooper;
    }

    /**
     * Run the message queue in this thread. Be sure to call
     * {@link #quit()} to end the loop.
     */
    //消息循环，整个程序就在这里while了。
    //这个是static函数喔！
    /*public static final void loop() {
        MyLooper me = myLooper();//从该线程中取出对应的looper对象
        MessageQueue queue = me.mQueue;//取消息队列对象...
        while (true) {
            Message msg = queue.next(); // might block取消息队列中的一个待处理消息..
            //if (!me.mRun) {//是否需要退出？mRun是个volatile变量，跨线程同步的，应该是有地方设置它。
            //  break;
            //}
            if (msg != null) {
                if (msg.target == null) {
                    // No target is a magic identifier for the quit message.
                    return;
                }
                if (me.mLogging!= null) me.mLogging.println(
                        ">>>>> Dispatching to " + msg.target + " "
                                + msg.callback + ": " + msg.what
                );
                msg.target.dispatchMessage(msg);
                if (me.mLogging!= null) me.mLogging.println(
                        "<<<<< Finished to  " + msg.target + " "
                                + msg.callback);
                msg.recycle();
            }
        }
    }*/

    /**
     * Return the Looper object associated with the current thread. Returns
     * null if the calling thread is not associated with a Looper.
     */
    //返回和线程相关的looper
    public static final MyLooper myLooper() {
        return (MyLooper)sThreadLocal.get();
    }

    /**
     * Control logging of messages as they are processed by this Looper. If
     * enabled, a log message will be written to <var>printer</var>
     * at the beginning and ending of each message dispatch, identifying the
     * target Handler and message contents.
     *
     * @param printer A Printer object that will receive log messages, or
     * null to disable message logging.
     */
    //设置调试输出对象，looper循环的时候会打印相关信息，用来调试用最好了。
    public void setMessageLogging(Printer printer) {
        mLogging = printer;
    }

    /**
     * Return the {@link MessageQueue} object associated with the current
     * thread. This must be called from a thread running a Looper, or a
     * NullPointerException will be thrown.
     */
    /*public static final MessageQueue myQueue() {
        return myLooper().mQueue;
    }*/

    //创建一个新的looper对象，
    //内部分配一个消息队列，设置mRun为true
    /*private MyLooper() {
        mQueue = new MessageQueue();
        mRun = true;
        mThread = Thread.currentThread();
    }*/

    /*public void quit() {
        Message msg = Message.obtain();
        // NOTE: By enqueueing directly into the message queue, the
        // message is left with a null target. This is how we know it is
        // a quit message.
        mQueue.enqueueMessage(msg, 0);
    }*/

    /**
     * Return the Thread associated with this Looper.
     */
    public Thread getThread() {
        return mThread;
    }

    //后面就简单了，打印，异常定义等。
    /*public void dump(Printer pw, String prefix) {
        pw.println(prefix + this);
        pw.println(prefix + "mRun=" + mRun);
        pw.println(prefix + "mThread=" + mThread);
        pw.println(prefix + "mQueue=" + ((mQueue != null) ? mQueue : "(null"));
        if (mQueue != null) {
            synchronized (mQueue) {
                Message msg = mQueue.mMessages;
                int n = 0;
                while (msg != null) {
                    pw.println(prefix + " Message " + n + ": " + msg);
                    n++;
                    msg = msg.next;
                }
                pw.println(prefix + "(Total messages: " + n + ")");
            }
        }
    }*/

    public String toString() {
        return "Looper{"
                + Integer.toHexString(System.identityHashCode(this))
                + "}";
    }

    static class HandlerException extends Exception {

        HandlerException(Message message, Throwable cause) {
            super(createMessage(cause), cause);
        }

        static String createMessage(Throwable cause) {
            String causeMsg = cause.getMessage();
            if (causeMsg == null) {
                causeMsg = cause.toString();
            }
            return causeMsg;
        }
    }
}


