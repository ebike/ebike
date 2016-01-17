package com.jcsoft.emsystem.db;

import android.os.Environment;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;

/**
 * 初始化数据库
 * 获得db
 * Created by jimmy on 16/1/17.
 */
public class XUtil {
    private static DbManager.DaoConfig daoConfig;
    public static DbManager db;

    public static DbManager.DaoConfig getDaoConfig() {
        File file = new File(Environment.getExternalStorageDirectory().getPath());
        if (daoConfig == null) {
            daoConfig = new DbManager.DaoConfig()
                    .setDbName("emsystem.db")
                    .setDbDir(file)
                    .setDbVersion(1)
                    .setAllowTransaction(true)
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                        }
                    });
        }
        return daoConfig;
    }

    //初始化数据库
    public static void initDB(){
        DbManager.DaoConfig daoConfig = XUtil.getDaoConfig();
        db = x.getDb(daoConfig);
    }

}
