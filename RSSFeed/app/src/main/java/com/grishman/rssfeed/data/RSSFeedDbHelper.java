package com.grishman.rssfeed.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.grishman.rssfeed.data.RSSFeedContract.FeedsEntry;

public class RSSFeedDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "rssfeed.db";

    public RSSFeedDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_RSSFEED_TABLE = new StringBuilder().append("CREATE TABLE ").append(FeedsEntry.TABLE_NAME)
                .append(" (").append(FeedsEntry._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(FeedsEntry.COLUMN_TITLE).append(" TEXT NOT NULL, ")
                .append(FeedsEntry.COLUMN_DESCRIPTION).append(" TEXT NOT NULL, ")
                .append(FeedsEntry.COLUMN_LINK).append(" TEXT NOT NULL,")
                .append(FeedsEntry.COLUMN_IMG).append(" TEXT NOT NULL, ")
                .append(FeedsEntry.COLUMN_CATEGORY).append(" TEXT, ")
                .append(FeedsEntry.COLUMN_PUBDATE).append(" TEXT ")
                .append(");").toString();

        sqLiteDatabase.execSQL(SQL_CREATE_RSSFEED_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FeedsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
