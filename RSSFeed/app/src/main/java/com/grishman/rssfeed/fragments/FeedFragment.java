package com.grishman.rssfeed.fragments;

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
import android.widget.ListView;

import com.grishman.rssfeed.FeedAdapter;
import com.grishman.rssfeed.R;
import com.grishman.rssfeed.data.RSSFeedContract;

/**
 * A Feed fragment containing list view for feed list.
 */
public class FeedFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FEED_LOADER_ID = 1;
    private FeedAdapter mFeedAdapter = null;
    private ListView mFeedList;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    public FeedFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
        mFeedList = (ListView) rootView.findViewById(R.id.list_feed);
        mFeedList.setAdapter(mFeedAdapter);
        mFeedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String url = cursor.getString(COL_LINK);
                ((Callback) getActivity())
                        .onItemSelected(url);
                mPosition = position;
            }
        });
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(FEED_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(FEED_LOADER_ID, null, this);
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
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mFeedList.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFeedAdapter.swapCursor(null);
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String url);
    }
}
