package com.grishman.rssfeed.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.grishman.rssfeed.DetailActivity;
import com.grishman.rssfeed.FeedAdapter;
import com.grishman.rssfeed.R;
import com.grishman.rssfeed.data.RSSFeedContract;
import com.grishman.rssfeed.service.FeedParserService;

/**
 * A Feed fragment containing list view for feed list.
 */
public class FeedFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FEED_LOADER_ID = 1;
    private FeedAdapter mFeedAdapter = null;

    public FeedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private static final String[] FEED_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            RSSFeedContract.FeedsEntry.TABLE_NAME + "." + RSSFeedContract.FeedsEntry._ID,
            RSSFeedContract.FeedsEntry.COLUMN_TITLE,
            RSSFeedContract.FeedsEntry.COLUMN_DESCRIPTION,
            RSSFeedContract.FeedsEntry.COLUMN_LINK,
            RSSFeedContract.FeedsEntry.COLUMN_IMG,
            RSSFeedContract.FeedsEntry.COLUMN_CATEGORY,
            RSSFeedContract.FeedsEntry.COLUMN_PUBDATE

    };

    // These indices are tied to FEED_COLUMNS.  If FEED_COLUMNS changes, these
    // must change.
    public final int COL_FEED_ID = 0;
    public static final int COL_TITLE = 1;
    public static final int COL_DESC = 2;
    public static final int COL_LINK = 3;
    public static final int COL_IMG = 4;
    public static final int COL_CATEGORY = 5;
    public static final int COL_PUBDATE = 6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFeedAdapter = new FeedAdapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        final ListView mFeedList = (ListView) rootView.findViewById(R.id.list_feed);
        final String[] values = new String[]{"http://abcnews.go.com/US/tiny-illinois-town-slammed-deadly-tornado/story?id=30217421", "http://abcnews.go.com/US/south-carolina-police-officers-mom-speaks-tearful-interview/story?id=30207558", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2"};
         ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        mFeedList.setAdapter(mFeedAdapter);
        mFeedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String url=cursor.getString(COL_LINK);
                Intent sendFeedURL = new Intent(getActivity(), DetailActivity.class);
                sendFeedURL.putExtra("URL", url);
                startActivity(sendFeedURL);
            }
        });
        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(FEED_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                RSSFeedContract.FeedsEntry.CONTENT_URI,
                FEED_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFeedAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFeedAdapter.swapCursor(null);
    }
}
