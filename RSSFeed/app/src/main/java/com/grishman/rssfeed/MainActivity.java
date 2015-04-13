package com.grishman.rssfeed;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.grishman.rssfeed.data.RSSFeedContract;
import com.grishman.rssfeed.fragments.FeedFragment;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        fakeData();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new FeedFragment())
                    .commit();
        }
    }

    private void fakeData() {
        ContentValues initialValues = new ContentValues();
        initialValues.put(RSSFeedContract.FeedsEntry.COLUMN_TITLE, "Test2 title2");
        initialValues.put(RSSFeedContract.FeedsEntry.COLUMN_DESCRIPTION, "TDescription2222");
        initialValues.put(RSSFeedContract.FeedsEntry.COLUMN_LINK, "http://abcnews.go.com/US/tiny-illinois-town-slammed-deadly-tornado/story?id=30217421");
        getApplicationContext().getContentResolver().insert(RSSFeedContract.FeedsEntry.CONTENT_URI, initialValues);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
