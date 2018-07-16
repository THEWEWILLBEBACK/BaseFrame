package com.optimusprime.xframe.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.optimusprime.xframe.database.upgrade.BaseUpgrader;


/**
 * 数据库帮助类
 * <p>
 * Created by Xiejq on 2017/7/7.
 */

public class DbOpenHelper extends SQLiteOpenHelper {

    //类没有实例化,是不能用作父类构造器的参数,必须声明为静态
    private static String NAME; //数据库名称

    private static final int VERSION = 2; //数据库版本
    // 当前数据库新旧版本号数据库旧版本号 0：新 1：旧
    private int[] versions = new int[2];

    //数据库升级使用
    private BaseUpgrader mUpgrader;


    // 单例
    private static DbOpenHelper instance;

    public static DbOpenHelper getInstance() {
        return instance;
    }

    /**
     * 初始化Application调用
     *
     * @param context
     */
    public static void init(Context context, String dbName) {
        NAME = dbName;
        if (instance == null)
            synchronized (DbOpenHelper.class) {
                if (instance == null)
                    instance = new DbOpenHelper(context);
            }
    }

    private DbOpenHelper(Context context) {
        //第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        versions[0] = newVersion;
        versions[1] = oldVersion;
    }

    /**
     * 获取版本信息
     */
    public int[] getVersions() {
        return versions;
    }

    public BaseUpgrader getUpgrader() {
        return mUpgrader;
    }

    public void setUpgrader(BaseUpgrader upgrader) {
        mUpgrader = upgrader;
    }
}
