package com.example.aplikasikeuangan.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;
//    private static final int DATABASE_VERSION = 2; //upgraded

    // Database Name
    private static final String DATABASE_NAME = "FinansialBKN.db";

    // User table name
    private static final String TABLE_USER = "user";
    private static final String TABLE_CASH_FLOW = "cash_flow";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_FULL_NAME = "user_full_name";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    // Pemasukan Table Columns names
    private static final String COLUMN_CASH_FLOW_ID = "cash_flow_id";
    private static final String COLUMN_CASH_FLOW_TANGGAL = "cash_flow_tanggal";
    private static final String COLUMN_CASH_FLOW_BULAN = "cash_flow_bulan";
    private static final String COLUMN_CASH_FLOW_TAHUN = "cash_flow_tahun";
    private static final String COLUMN_CASH_FLOW_NOMINAL = "cash_flow_nominal";
    private static final String COLUMN_CASH_FLOW_KETERANGAN = "cash_flow_keterangan";
    private static final String COLUMN_CASH_FLOW_TIPE = "cash_flow_tipe";


    // create table user
    private String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_FULL_NAME + " TEXT,"
            + COLUMN_USER_NAME + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";

    // create table pemasukan
    private String CREATE_CASH_FLOW_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CASH_FLOW + "("
            + COLUMN_CASH_FLOW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CASH_FLOW_TANGGAL + " TEXT,"
            + COLUMN_CASH_FLOW_BULAN + " TEXT," + COLUMN_CASH_FLOW_TAHUN + " TEXT,"
            + COLUMN_CASH_FLOW_NOMINAL + " INTEGER," + COLUMN_CASH_FLOW_KETERANGAN + " TEXT,"
            + COLUMN_CASH_FLOW_TIPE + " TEXT" + ")";

    // drop table user
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    // drop table pemasukan
    private String DROP_CASH_FLOW_TABLE = "DROP TABLE IF EXISTS " + TABLE_CASH_FLOW;

    public int total = 0;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CASH_FLOW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_CASH_FLOW_TABLE);
        // Create tables again
        onCreate(db);
    }

    public User checkUser(User user){
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_FULL_NAME,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        // selection arguments
        String[] selectionArgs = {user.getUsername(),user.getPassword()};
        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();

        if(cursorCount > 0) {
            if (cursor.moveToFirst()) {
                user.setUserid(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ID))));
                user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)));
                user.setFullname(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_FULL_NAME)));

                cursor.close();
                return user;
            } else {
                return null;
            }

        }else{
            return null;
        }
    }

    public void addUser(User user) {
        SQLiteDatabase db;
        ContentValues values = new ContentValues();

        db = this.getWritableDatabase();

        values.put(COLUMN_USER_FULL_NAME, user.getFullname());
        values.put(COLUMN_USER_NAME, user.getUsername());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public boolean checkOldPassword(String oldPassword) {
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_FULL_NAME,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD,
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_PASSWORD + " = ?";
        // selection arguments
        String[] selectionArgs = {oldPassword};
        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;

    }

    public User updatePassword(User user, String newPassword) {

            user.setPassword(newPassword);

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            String[] selectionArgs = {String.valueOf(user.getUserid())};

            values.put(COLUMN_USER_PASSWORD, newPassword);
//            values.put(COLUMN_USER_NAME, user.getUsername());
//            values.put(COLUMN_USER_FULL_NAME, user.getFullname());
            // updating row
            db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?", selectionArgs);
            db.close();
            return user;
    }

    public void addCashFlow(DetailCashFlow dcf) {
        ContentValues values = new ContentValues();

        SQLiteDatabase db = this.getWritableDatabase();

        values.put(COLUMN_CASH_FLOW_NOMINAL, dcf.getNominal());
        values.put(COLUMN_CASH_FLOW_KETERANGAN, dcf.getKeterangan());
        values.put(COLUMN_CASH_FLOW_TANGGAL, dcf.getTanggal());
        values.put(COLUMN_CASH_FLOW_BULAN, dcf.getBulan());
        values.put(COLUMN_CASH_FLOW_TAHUN, dcf.getTahun());
        values.put(COLUMN_CASH_FLOW_TIPE, dcf.getTipe());
        // Inserting Row
        db.insert(TABLE_CASH_FLOW, null, values);
        db.close();
    }

    public List<DetailCashFlow> getNominalPemasukanByBulan(String bulan) {

        int bln = Integer.parseInt(bulan);

        String[] columns = {
//                COLUMN_CASH_FLOW_ID,
                COLUMN_CASH_FLOW_NOMINAL,
//                COLUMN_CASH_FLOW_BULAN,
//                COLUMN_CASH_FLOW_TIPE
        };
        // sorting orders
        String sortOrder =
                COLUMN_CASH_FLOW_ID + " ASC";
        List<DetailCashFlow> detailCashFlowList = new ArrayList<DetailCashFlow>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_CASH_FLOW_BULAN + " = ?" + " AND " + COLUMN_CASH_FLOW_TIPE + " = ?";
        String[] selectionArgs = {String.valueOf(bln), "pemasukan"};
        Cursor cursor = db.query(TABLE_CASH_FLOW, //Table to query
                columns,    //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    DetailCashFlow dcfPemasukan = new DetailCashFlow();
//                    dcfPemasukan.setIdCashFlow(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_ID))));
                    dcfPemasukan.setNominal(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_NOMINAL))));
//                    dcfPemasukan.setKeterangan(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_KETERANGAN)));
//                    dcfPemasukan.setTanggal(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_TANGGAL)));
//                    dcfPemasukan.setBulan(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_BULAN)));
//                    dcfPemasukan.setTahun(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_TAHUN)));
//                    dcfPemasukan.setTipe(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_TIPE)));

                    // Adding detail cash flow record to list
                    detailCashFlowList.add(dcfPemasukan);
                    //                total += Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_NOMINAL)));
                } while (cursor.moveToNext());
                cursor.close();
                db.close();
//                return detailCashFlowList;
            }
        }
        // return detailCashFlow list
        return detailCashFlowList;
    }
    public List<DetailCashFlow> getNominalPengeluaranByBulan(String bulan) {

        int bln = Integer.parseInt(bulan);

        String[] columns = {
//                COLUMN_CASH_FLOW_ID,
                COLUMN_CASH_FLOW_NOMINAL,
//                COLUMN_CASH_FLOW_BULAN,
//                COLUMN_CASH_FLOW_TIPE
        };
        // sorting orders
        String sortOrder =
                COLUMN_CASH_FLOW_ID + " ASC";
        List<DetailCashFlow> detailCashFlowList = new ArrayList<DetailCashFlow>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_CASH_FLOW_BULAN + " = ?" + " AND " + COLUMN_CASH_FLOW_TIPE + " = ?";
        String[] selectionArgs = {String.valueOf(bln), "pengeluaran"};
        Cursor cursor = db.query(TABLE_CASH_FLOW, //Table to query
                columns,    //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    DetailCashFlow dcfPengeluaran = new DetailCashFlow();

//                    dcfPengeluaran.setIdCashFlow(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_ID))));
                    dcfPengeluaran.setNominal(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_NOMINAL))));
//                    dcfPengeluaran.setKeterangan(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_KETERANGAN)));
//                    dcfPengeluaran.setTanggal(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_TANGGAL)));
//                    dcfPengeluaran.setBulan(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_BULAN)));
//                    dcfPengeluaran.setTahun(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_TAHUN)));
//                    dcfPengeluaran.setTipe(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_TIPE)));

                    // Adding detail cash flow record to list
                    detailCashFlowList.add(dcfPengeluaran);
                    //                total += Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_NOMINAL)));
                } while (cursor.moveToNext());
                cursor.close();
                db.close();
//                return detailCashFlowList;
            }
        }
        // return detailCashFlow list
        return detailCashFlowList;
    }

    public List<DetailCashFlow> getAllCashFlow(){

        String[] columns = {
                COLUMN_CASH_FLOW_ID,
                COLUMN_CASH_FLOW_NOMINAL,
                COLUMN_CASH_FLOW_KETERANGAN,
                COLUMN_CASH_FLOW_TANGGAL,
                COLUMN_CASH_FLOW_BULAN,
                COLUMN_CASH_FLOW_TAHUN,
                COLUMN_CASH_FLOW_TIPE
        };
        // sorting orders
        String sortOrder =
                COLUMN_CASH_FLOW_ID + " ASC";
        List<DetailCashFlow> detailCashFlowList = new ArrayList<DetailCashFlow>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CASH_FLOW, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DetailCashFlow dcf = new DetailCashFlow();

                dcf.setIdCashFlow(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_ID))));
                dcf.setNominal(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_NOMINAL))));
                dcf.setKeterangan(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_KETERANGAN)));
                dcf.setTanggal(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_TANGGAL)));
                dcf.setBulan(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_BULAN)));
                dcf.setTahun(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_TAHUN)));
                dcf.setTipe(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CASH_FLOW_TIPE)));

                // Adding detail cash flow record to list
                detailCashFlowList.add(dcf);
                Log.d("List detail "+String.valueOf(dcf.getIdCashFlow()), String.valueOf(dcf.getNominal()));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return detailCashFlowList;
    }

}
