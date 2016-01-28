package com.jcsoft.emsystem.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.jcsoft.emsystem.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 省市区数据读取
 *
 * @author longyanliang
 *         version 1.0
 */
public class ProvinceHelper {
    private final int BUFFER_SIZE = 1024;
    public static final String DB_NAME = "ebike_city.db";
    public static final String PACKAGE_NAME = "com.jcsoft.emsystem";

    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME;

    private SQLiteDatabase database;

    private Context context;
    private File file;

    public ProvinceHelper(Context context) {
        this.context = context;
    }

    public void openDatabase() {
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }

    public SQLiteDatabase getDatabase() {
        return this.database;
    }

    private SQLiteDatabase openDatabase(String dbfile) {
        try {
            file = new File(dbfile);
            if (!file.exists()) {
                InputStream is = context.getResources().openRawResource(R.raw.ebike_city);

                FileOutputStream fos = new FileOutputStream(dbfile);
                if (is != null) {
                } else {
                }
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                    fos.flush();
                }
                fos.close();
                is.close();
            }

            database = SQLiteDatabase.openOrCreateDatabase(dbfile, null);

            return database;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeDatabase() {
        if (this.database != null)
            this.database.close();
    }
}