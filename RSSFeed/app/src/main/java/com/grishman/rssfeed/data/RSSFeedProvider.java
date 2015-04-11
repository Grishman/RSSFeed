package com.grishman.rssfeed.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.grishman.rssfeed.data.RSSFeedContract.FeedsEntry;


public class RSSFeedProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String LOG_TAG = RSSFeedProvider.class.getSimpleName();
    private RSSFeedDbHelper mOpenHelper;

    static final int FEED = 100;
    static final int FEED_ID = 101;
    private SQLiteQueryBuilder sScanQueryBuilder;


    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root URI.
        final UriMatcher mURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = RSSFeedContract.CONTENT_AUTHORITY;
        mURIMatcher.addURI(authority, RSSFeedContract.PATH_FEEDS, FEED);
        mURIMatcher.addURI(authority, RSSFeedContract.PATH_FEEDS + "/*", FEED_ID);
        return mURIMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new RSSFeedDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query, " + uri.toString());
        Cursor retCursor;
        SQLiteDatabase database;
        switch (sUriMatcher.match(uri)) {
            case FEED:
                Log.d(LOG_TAG, "URI FEED");
                break;
            case FEED_ID:
                Log.d(LOG_TAG, "URI FEED_ID");
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = FeedsEntry._ID + " = " + id;
                } else {
                    selection = selection +
                            " AND " + FeedsEntry._ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
        database = mOpenHelper.getReadableDatabase();
        retCursor = database.query(FeedsEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FEED_ID:
                return FeedsEntry.CONTENT_ITEM_TYPE;
            case FEED:
                return FeedsEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Log.d(LOG_TAG, "insert, " + uri.toString());
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FEED: {
                long _id = db.insert(FeedsEntry.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = FeedsEntry.buildFeedsUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete, " + uri.toString());
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int delCount;
        switch (match) {
            case FEED:
                Log.d(LOG_TAG, "URI FEED");
                break;
            case FEED_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI FEED_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = FeedsEntry._ID + " = " + id;
                } else {
                    selection = selection +
                            " AND " + FeedsEntry._ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown uri:" + uri);
        }
        delCount = db.delete(FeedsEntry.TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return delCount;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int updateRows;
        switch (match) {
            case FEED: {
                updateRows = db.update(FeedsEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            }
            case FEED_ID: {
                updateRows = db.update(FeedsEntry.TABLE_NAME, contentValues,
                        "_id  = "
                                + uri.getLastPathSegment()
                                + (!TextUtils.isEmpty(selection) ? " AND ("
                                + selection + ')' : ""), selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (updateRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updateRows;
    }
}
