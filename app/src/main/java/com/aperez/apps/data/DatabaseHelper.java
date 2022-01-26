package com.aperez.apps.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.aperez.apps.data.DatabaseDescription.Contact;
import com.aperez.apps.login.SIMPLogin;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AddressBook.db";
    private static final int DATABASE_VERSION = 1;
    public final String CREATE_CONTACTS_TABLE =
            "CREATE TABLE " + Contact.TABLE_NAME + "(" +
                    Contact._ID + " integer primary key, " +
                    Contact.COLUMN_NAME + " TEXT," +
                    Contact.COLUMN_PASSWD + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(SIMPLogin context, String s, Object o, int i) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");

        db.execSQL(CREATE_CONTACTS_TABLE);
    }
}