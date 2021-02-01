//Book.aidl
//第一类AIDL文件
//这个文件的作用是引入了一个序列化对象 Book 供其他的AIDL文件使用
//注意：Book.aidl与Book.java的包名应当是一样的

// BookManager.aidl
//第二类AIDL文件
//作用是定义方法接口

//所有的返回值前都不需要加任何东西，不管是什么数据类型
List<Book> getBooks();

//传参时除了Java基本类型以及String，CharSequence之外的类型
//都需要在前面加上定向tag，具体加什么量需而定
void addBook(in Book book);

################  注意    #############
1、带中文注释得aidl文件生成得Java文件为空，解决就是,把注释去掉或者添加英文注释