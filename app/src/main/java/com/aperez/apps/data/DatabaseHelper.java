package com.aperez.apps.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.aperez.apps.androidfunwithflags.MainActivity;
import com.aperez.apps.androidfunwithflags.MainActivityFragment;
import com.aperez.apps.data.DatabaseDescription.Contact;
import com.aperez.apps.eventhandlers.GuessButtonListener;
import com.aperez.apps.login.LoginSIMP;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AddressBook.db";
    private static final int DATABASE_VERSION = 1;
    public final String CREATE_CONTACTS_TABLE =
            "CREATE TABLE " + Contact.TABLE_NAME + "(" +
                    Contact._ID + " integer primary key, " +
                    Contact.COLUMN_NAME + " TEXT," +
                    Contact.COLUMN_PASSWD + " TEXT);";
    public final String CREATE_POINTS_TABLE =
            "CREATE TABLE POINTS (Player TEXT primary key, " +
                    "ActualLevel TEXT," +
                    "Level1Point TEXT," +
                    "Level2Point TEXT," +
                    "Level3Point TEXT," +
                    "Level4Point TEXT," +
                    "Total TEXT);";
    public final String insertPoints1 = "INSERT INTO POINTS VALUES('1', '1', '0', '0', '0', '0', '0');";
    public final String insertPoints2 = "INSERT INTO POINTS VALUES('2', '1', '0', '0', '0', '0', '0');";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(LoginSIMP context, String s, Object o, int i) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public DatabaseHelper(MainActivity context, String s, Object o, int i) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        //db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_POINTS_TABLE);
        db.execSQL(insertPoints1);
        db.execSQL(insertPoints2);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");

        db.execSQL(CREATE_CONTACTS_TABLE);
    }
}