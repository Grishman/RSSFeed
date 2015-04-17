package com.grishman.rssfeed;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.grishman.rssfeed.data.RSSFeedContract;
import com.grishman.rssfeed.fragments.DetailWebViewFragment;
import com.grishman.rssfeed.fragments.FeedFragment;
import com.grishman.rssfeed.sync.RSSFeedSyncAdapter;


public class MainActivity extends ActionBarActivity implements FeedFragment.Callback {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.feeds_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.feeds_detail_container, new DetailWebViewFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
        RSSFeedSyncAdapter.initializeSyncAdapter(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void deleteAll() {
        // Helper method for testing
        getApplicationContext().getContentResolver().delete(RSSFeedContract.FeedsEntry.CONTENT_URI, null, null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_refresh) {
            // Starting the sync
            RSSFeedSyncAdapter.syncImmediately(this);
        }
        if (id == R.id.action_delete) {
            deleteAll();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(String url) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putString(DetailWebViewFragment.DETAIL_URL, url);

            DetailWebViewFragment fragment = new DetailWebViewFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.feeds_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            // Open a detailActivity if we use phone
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(DetailWebViewFragment.DETAIL_URL, url);
            startActivity(intent);
        }
    }

}
