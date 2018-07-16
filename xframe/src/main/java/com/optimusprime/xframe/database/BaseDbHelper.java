package com.optimusprime.xframe.database;

import android.database.sqlite.SQLiteDatabase;


import com.optimusprime.xframe.database.dao.Property;
import com.optimusprime.xframe.database.upgrade.BaseUpgrader;
import com.optimusprime.xframe.database.upgrade.DefaultUpgrader;
import com.optimusprime.xframe.database.upgrade.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据表创建需要在onCreate中
 * <p>
 * Created by Xiejq on 2017/7/7.
 */

public abstract class BaseDbHelper {

    /**
     * 全局数据库锁对象
     */
    protected static final Object LOCK_OBJECT = new Object();

    protected BaseDbHelper() {
    }


    /**
     * 初始化
     */
    public void init() {
        synchronized (LOCK_OBJECT) {
            int[] versions = DbOpenHelper.getInstance().getVersions();
            SQLiteDatabase db = DbOpenHelper.getInstance().getWritableDatabase();
            if (versions[0] != versions[1]) {
                onUpdate(db, versions[1], versions[0]);
            }
            if (!db.isOpen()) { // onUpdate 可能被关闭
                db = DbOpenHelper.getInstance().getWritableDatabase();
            }
            onCreate(db);
        }
    }

    public SQLiteDatabase getWritableDatabase() {
        return DbOpenHelper.getInstance().getWritableDatabase();
    }

    public SQLiteDatabase getReadableDatabase() {
        return DbOpenHelper.getInstance().getReadableDatabase();
    }

    protected abstract void onCreate(SQLiteDatabase db);

    protected void onUpdate(SQLiteDatabase db, int oldVersion, int newVersion) {
        BaseUpgrader upgrader = DbOpenHelper.getInstance().getUpgrader();
        if (upgrader == null) {
            upgrader = new DefaultUpgrader();
        }
        upgrader.setDatabase(db);
//        List<AbstractDao> list = getTables();
//        if (list == null) {
//            return;
//        }
//        int size = list.size();
//        AbstractDao dao;
//        ArrayList<Table> tableList = new ArrayList<>(size);
//
//
//        for (int i = 0; i < size; i++) {
//            dao = list.get(i);
//            tableList.add(new Table(dao.getTableName(), dao.getCreateSql()));
//        }

        ArrayList<Table> tableList = new ArrayList<>();
        tableList.add(new Table(getTableName(), Property.createSQL(getProperList(), getTableName())));
        try {
            //Catch unexpected exceptions.
            upgrader.upgrade(oldVersion, newVersion, tableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    protected abstract String getTableName();

    protected abstract List<Property> getProperList();
}
