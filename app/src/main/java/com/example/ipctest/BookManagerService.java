package com.example.ipctest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 整体的代码结构很清晰，大致可以分为三块：
 * 第一块是初始化。在 onCreate() 方法里面我进行了一些数据的初始化操作。
 * 第二块是重写 BookManager.Stub 中的方法。在这里面提供AIDL里面定义的方法接口的具体实现逻辑。
 * 第三块是重写 onBind() 方法。在里面返回写好的 BookManager.Stub 。
 */

public class BookManagerService extends Service {

    private static final String TAG = "BookManagerService";
    //包含Book对象的list
    private List<Book> mBooks = new ArrayList<>();

    public BookManagerService() {
    }

    /**
     * 第二块是重写 BookManager.Stub 中的方法。在这里面提供AIDL里面定义的方法接口的具体实现逻辑。
     */
    private final BookManager.Stub mBookManager = new BookManager.Stub()
    {

        @Override
        public List<Book> getBooks() throws RemoteException {
            synchronized (this) {
                Log.e(TAG, "invoking getBooks() method , now the list is : " + mBooks.toString());
                if (mBooks != null) {
                    return mBooks;
                }
                return new ArrayList<>();
            }
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            synchronized (this) {
                if (mBooks == null) {
                    mBooks = new ArrayList<>();
                }
                if (book == null) {
                    Log.e(TAG, "Book is null in In");
                    book = new Book();
                }
                //尝试修改book的参数，主要是为了观察其到客户端的反馈
                book.setPrice(2333);
                if (!mBooks.contains(book)) {
                    mBooks.add(book);
                }
                //打印mBooks列表，观察客户端传过来的值
                Log.e(TAG, "invoking addBooks() method , now the list is : " + mBooks.toString());
            }
        }

        @Override
        public int getBookCount() throws RemoteException {
            return 0;
        }

        @Override
        public void setBookPrice(Book book, int price) throws RemoteException {

        }

        @Override
        public void setBookName(Book book, String name) throws RemoteException {

        }

        @Override
        public void addBookIn(Book book) throws RemoteException {

        }

        @Override
        public void addBookOut(Book book) throws RemoteException {

        }

        @Override
        public void addBookInout(Book book) throws RemoteException {

        }

    };

    /**
     * 第一块是初始化。在 onCreate() 方法里面我进行了一些数据的初始化操作。
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    /**
     * 第三块是重写 onBind() 方法。在里面返回写好的 BookManager.Stub.
     */
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        Log.d(TAG, "onBind");
        return mBookManager;
    }
}
