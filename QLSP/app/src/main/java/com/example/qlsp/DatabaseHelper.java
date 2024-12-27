package com.example.qlsp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QLSanpham.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_SANPHAM = "SanPham";
    public static final String COLUMN_ID = "MaSanPham";
    public static final String COLUMN_TEN = "TenSanPham";
    public static final String COLUMN_SOLUONG = "SoLuong";
    public static final String COLUMN_DONGIA = "DonGia";

    private static final String DATABASE_CREATE =
            "CREATE TABLE " + TABLE_SANPHAM + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TEN + " TEXT NOT NULL, " +
                    COLUMN_SOLUONG + " INTEGER NOT NULL, " +
                    COLUMN_DONGIA + " REAL NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SANPHAM);
        onCreate(db);
    }
}