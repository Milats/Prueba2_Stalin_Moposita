package com.aperez.apps.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseDescription {
    public static final String AUTHORITY = "com.aperez.apps.data";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final class Contact implements BaseColumns{
        public static final String TABLE_NAME = "contacts";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PASSWD = "passwd";

        public static Uri buildContactUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}