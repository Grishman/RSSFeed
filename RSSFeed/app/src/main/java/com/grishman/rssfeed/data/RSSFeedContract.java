package com.grishman.rssfeed.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class RSSFeedContract {
    // Authority
    public static final String CONTENT_AUTHORITY = "com.grishman.rssfeed";

    // The base of all URI's which apps will use to contact
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_FEEDS = "feed";

    /* Inner class that defines the table contents of the rssfeed table */
    public static final class FeedsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FEEDS).build();

        public static Uri buildFeedsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FEEDS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FEEDS;

        public static final String TABLE_NAME = "rssfeed";
        // Fields in DB
        // Title of news article. Stored as string.
        public static final String COLUMN_TITLE = "title";
        // Description of news article stored as string
        public static final String COLUMN_DESCRIPTION = "description";
        // URL of full news article. Stored as string.
        public static final String COLUMN_LINK = "link";
        // URL of image related to article. Stored as String.
        public static final String COLUMN_IMG = "img_url";
        // Category. Stored as String.
        public static final String COLUMN_CATEGORY = "category";
        // Publish date, stored as string
        public static final String COLUMN_PUBDATE = "pub_date";

    }
}
