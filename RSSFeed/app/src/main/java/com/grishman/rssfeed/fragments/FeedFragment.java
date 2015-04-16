package com.grishman.rssfeed.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.grishman.rssfeed.FeedAdapter;
import com.grishman.rssfeed.R;
import com.grishman.rssfeed.data.RSSFeedContract;
import com.grishman.rssfeed.sync.FeedParserService;

import java.util.Calendar;
import java.util.TimeZone;

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
        setRetainInstance(true);
    }

    private void setRecurringAlarm(Context context) {
        Log.d("Alarm", "in the alarm");

        // let's grab new stuff at around 12:45 GMT, inexactly
        Calendar updateTime = Calendar.getInstance();
        updateTime.setTimeZone(TimeZone.getTimeZone("GMT"));
        updateTime.set(Calendar.HOUR_OF_DAY, 10);
        updateTime.set(Calendar.MINUTE, 40);

        Intent downloader = new Intent(context, FeedParserService.AlarmReceiver.class);
        PendingIntent recurringDownload = PendingIntent.getBroadcast(context,
                0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) getActivity().getSystemService(
                Context.ALARM_SERVICE);
        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                updateTime.getTimeInMillis(),
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, recurringDownload);
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
        mFeedList.setAdapter(mFeedAdapter);
        mFeedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String url = cursor.getString(COL_LINK);
                ((Callback) getActivity())
                        .onItemSelected(url);
            }
        });
//        setRecurringAlarm(getActivity());
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(FEED_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
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
