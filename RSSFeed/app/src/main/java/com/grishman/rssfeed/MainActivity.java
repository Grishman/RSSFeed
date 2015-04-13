package com.grishman.rssfeed;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.grishman.rssfeed.data.RSSFeedContract;
import com.grishman.rssfeed.fragments.FeedFragment;
import com.grishman.rssfeed.service.FeedParserService;

import java.util.Calendar;
import java.util.TimeZone;


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
//        ContentValues initialValues = new ContentValues();
//        initialValues.put(RSSFeedContract.FeedsEntry.COLUMN_TITLE, "Test2 title2");
//        initialValues.put(RSSFeedContract.FeedsEntry.COLUMN_DESCRIPTION, "TDescription2222");
//        initialValues.put(RSSFeedContract.FeedsEntry.COLUMN_LINK, "http://abcnews.go.com/US/tiny-illinois-town-slammed-deadly-tornado/story?id=30217421");
        getApplicationContext().getContentResolver().delete(RSSFeedContract.FeedsEntry.CONTENT_URI, null, null);

    }

    private void setRecurringAlarm(Context context) {

        // let's grab new stuff at around 12:45 GMT, inexactly
        Calendar updateTime = Calendar.getInstance();
        updateTime.setTimeZone(TimeZone.getTimeZone("GMT"));
        updateTime.set(Calendar.HOUR_OF_DAY, 12);
        updateTime.set(Calendar.MINUTE, 45);

        Intent downloader = new Intent(context, FeedParserService.AlarmReceiver.class);
        PendingIntent recurringDownload = PendingIntent.getBroadcast(context,
                0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) getApplicationContext().getSystemService(
                Context.ALARM_SERVICE);
        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                updateTime.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, recurringDownload);
    }
    public boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        }
        return     cm.getActiveNetworkInfo().isConnectedOrConnecting();
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
        if (id == R.id.action_refresh) {
            // Starting the service
            Intent intent = new Intent(getApplicationContext(), FeedParserService.class);
            startService(intent);
            // Start schedule alarm to trigger once a day
            setRecurringAlarm(getApplicationContext());
        }
        if (id == R.id.action_delete) {
            fakeData();
        }

        return super.onOptionsItemSelected(item);
    }

}
