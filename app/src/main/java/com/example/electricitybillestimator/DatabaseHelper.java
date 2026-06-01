package com.example.electricitybillestimator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ElectricityBill.db";
    public static final String TABLE_NAME = "bill";

    public static final String COL1 = "id";
    public static final String COL2 = "month";
    public static final String COL3 = "unit_used";
    public static final String COL4 = "rebate";
    public static final String COL5 = "total_charge";
    public static final String COL6 = "final_cost";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE " + TABLE_NAME + "(" +
                        COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COL2 + " TEXT," +
                        COL3 + " INTEGER," +
                        COL4 + " REAL," +
                        COL5 + " REAL," +
                        COL6 + " REAL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String month,
                              String unit,
                              String rebate,
                              String totalCharge,
                              String finalCost) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COL2, month);
        cv.put(COL3, unit);
        cv.put(COL4, rebate);
        cv.put(COL5, totalCharge);
        cv.put(COL6, finalCost);

        return db.insert(TABLE_NAME, null, cv) != -1;
    }

    public Cursor getAllData() {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_NAME,
                null
        );
    }

    public Cursor getDataById(String id) {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE id=?",
                new String[]{id}
        );
    }

    public boolean updateData(
            String id,
            String month,
            String unit,
            String rebate,
            String totalCharge,
            String finalCost) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COL1, id);
        cv.put(COL2, month);
        cv.put(COL3, unit);
        cv.put(COL4, rebate);
        cv.put(COL5, totalCharge);
        cv.put(COL6, finalCost);

        db.update(TABLE_NAME,
                cv,
                "id=?",
                new String[]{id});

        return true;
    }

    public Integer deleteData(String id) {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(
                TABLE_NAME,
                "id=?",
                new String[]{id}
        );
    }
}