package com.noplayer.rumahsakit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {
    String TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "sqlite";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "tb_pendaftaran";

    public static final String COLUMN_ID = "id_pendaftaran";
    public static final String COLUMN_NAMA_USER = "nama_user";
    public static final String COLUMN_TANGGAL_PERIKSA = "tanggal_periksa";
    public static final String COLUMN_NO_ANTRIAN = "no_antrian";
    public static final String COLUMN_POLI_TUJUAN = "poli_tujuan";
    public static final String COLUMN_ID_USER = "id_user";
    public static final String COLUMN_STATUS_PENDAFTARAN = "status_pendaftaran";

    private Context context;

    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

//        SQLiteDatabase db = this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY autoincrement, " +
                COLUMN_NAMA_USER + " TEXT NOT NULL, " +
                COLUMN_TANGGAL_PERIKSA + " TEXT NOT NULL," +
                COLUMN_POLI_TUJUAN + " TEXT NOT NULL," +
                COLUMN_NO_ANTRIAN + " TEXT NOT NULL," +
                COLUMN_ID_USER + " TEXT NOT NULL," +
                COLUMN_STATUS_PENDAFTARAN + " TEXT NOT NULL" +
                " )";

        db.execSQL(SQL_CREATE_TABLE);

        getTableAsString(db, TABLE_NAME);

//        db.execSQL("create table tb_pendaftaran(id_pendaftaran integer primary key autoincrement," +
//                "nama_user text," +
//                "tanggal_periksa text," +
//                "poli_tujuan text," +
//                "no_antrian text);"
//        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);

        onCreate(db);
    }

    public ArrayList<HashMap<String, String>> getAllData(String id_user) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID_USER + "=" + "'" + id_user + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_ID, cursor.getString(0));
                map.put(COLUMN_NAMA_USER, cursor.getString(1));
                map.put(COLUMN_TANGGAL_PERIKSA, cursor.getString(2));
                map.put(COLUMN_POLI_TUJUAN, cursor.getString(3));
                map.put(COLUMN_NO_ANTRIAN, cursor.getString(4));
                map.put(COLUMN_STATUS_PENDAFTARAN, cursor.getString(6));
                wordList.add(map);
            } while (cursor.moveToNext());
        }

        Log.e("select sqlite ", "" + wordList);

        database.close();
        return wordList;
    }

    public void insert(String nama_user, String tanggal_periksa, String poli_tujuan, String no_antrian, String id_user, String status_pendaftaran) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO " + TABLE_NAME + " (nama_user, tanggal_periksa, poli_tujuan, no_antrian, id_user, status_pendaftaran) " +
                "VALUES ('" + nama_user + "', '" + tanggal_periksa + "', '" +poli_tujuan+ "', '" + no_antrian + "', '" + id_user + "', '" + status_pendaftaran + "')";
        Log.e("insert sqlite ", "" + queryValues);
        database.execSQL(queryValues);
        database.close();
    }
//    public void insert(String nama_user, String tanggal_periksa, String poli_tujuan, String no_antrian, String id_user, String id_pendaftaran) {
//        SQLiteDatabase database = this.getWritableDatabase();
//        String queryValues = "INSERT INTO " + TABLE_NAME + " (nama_user, tanggal_periksa, poli_tujuan, no_antrian, id_user, id_pendaftaran) " +
//                "VALUES ('" + nama_user + "', '" + tanggal_periksa + "', '" +poli_tujuan+ "', '" + no_antrian + "', '" + id_user + "', '" + id_pendaftaran + "')";
//        Log.e("insert sqlite ", "" + queryValues);
//        database.execSQL(queryValues);
//        database.close();
//    }



    public String getTableAsString(SQLiteDatabase db, String tableName) {
        Log.d(TAG, "getTableAsString called");
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }

        return tableString;
    }

//    public void update(int id, String name, String address) {
//        SQLiteDatabase database = this.getWritableDatabase();
//
//        String updateQuery = "UPDATE " + TABLE_SQLite + " SET "
//                + COLUMN_NAME + "='" + name + "', "
//                + COLUMN_ADDRESS + "='" + address + "'"
//                + " WHERE " + COLUMN_ID + "=" + "'" + id + "'";
//        Log.e("update sqlite ", updateQuery);
//        database.execSQL(updateQuery);
//        database.close();
//    }
//
    public void select(String id_user){
        SQLiteDatabase database = this.getWritableDatabase();

        String seletQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID_USER + "=" + "'" + id_user + "'";
        Log.e("update sqlite ", seletQuery);
        database.execSQL(seletQuery);
        database.close();
    }

    public void delete(String id) {
        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID_USER + "=" + "'" + id + "'";
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();


    }

//    public void delete_all() {
//        SQLiteDatabase database = this.getWritableDatabase();
//
//        String updateQuery = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + "=" + "'" + id + "'";
//        Log.e("delete sqlite ", updateQuery);
//        database.execSQL(updateQuery);
//        database.close();
//    }

}
