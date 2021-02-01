// BookManager.aidl
package com.example.ipctest;

import com.example.ipctest.Book;

interface BookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */


    //void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
    //      double aDouble, String aString);

    List<Book> getBooks();
    void addBook(in Book book);
    int getBookCount();
    void setBookPrice(in Book book , int price);
    void setBookName(in Book book , String name);
    void addBookIn(in Book book);
    void addBookOut(out Book book);
    void addBookInout(inout Book book);
}
