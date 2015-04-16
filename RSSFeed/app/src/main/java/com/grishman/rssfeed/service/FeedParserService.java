package com.grishman.rssfeed.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.grishman.rssfeed.MainActivity;
import com.grishman.rssfeed.R;
import com.grishman.rssfeed.data.RSSFeedContract;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class FeedParserService extends IntentService {

    private static final String TAG = FeedParserService.class.getSimpleName();
    RSSHandler rssHandler;
    List<RSSFeedItem> articleList2;
    private String feedUrl = "http://feeds.abcnews.com/abcnews/topstories";

    public FeedParserService() {
        super("FeedService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Service started");
        URL url = null;
        rssHandler = new RSSHandler();

        try {

            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();

            url = new URL(feedUrl);

            xr.setContentHandler(rssHandler);
            xr.parse(new InputSource(url.openStream()));


        } catch (IOException e) {
            Log.e("RSS Handler IO", e.getMessage() + " >> " + e.toString());
        } catch (SAXException e) {
            Log.e("RSS Handler SAX", e.toString());
        } catch (ParserConfigurationException e) {
            Log.e("RSS Handler Parser Config", e.toString());
        }
        // Delete feeds
        getApplicationContext().getContentResolver().delete(RSSFeedContract.FeedsEntry.CONTENT_URI, null, null);
// TODO refactor this sht
        // Insert new feeds
        ContentValues cv = new ContentValues();
        for (RSSFeedItem item : articleList2 = rssHandler.getArticleList()) {
            Log.d(TAG, item.getImgLink());
            cv.put("title", item.getTitle());
            cv.put("description", item.getDescription());
            cv.put("link", item.getLink());
            cv.put("img_url", item.getImgLink());
            cv.put("category", item.getCategory());
            cv.put("pub_date", item.getPubDate());
            getApplicationContext().getContentResolver().insert(RSSFeedContract.FeedsEntry.CONTENT_URI, cv);
        }

    }

    public static class AlarmReceiver extends BroadcastReceiver {

        private int numMessages = 0;
        private int notificationID = 100;
        private NotificationManager mNotificationManager;

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent sendIntent = new Intent(context, FeedParserService.class);
            context.startService(sendIntent);
//            sendUpdateNotify();
            /* Invoking the default notification service */
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context);

            mBuilder.setContentTitle("Get your fresh RSS!");
            mBuilder.setContentText("Service complete update feed.");
            mBuilder.setTicker("RSS feed is up to date");
            mBuilder.setSmallIcon(R.drawable.ic_noti_rss);
            mBuilder.setAutoCancel(true);

      /* Increase notification number every time a new notification arrives */
            mBuilder.setNumber(++numMessages);

      /* Creates an explicit intent for an Activity in your app */
            Intent resultIntent = new Intent(context, MainActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);

      /* Adds the Intent that starts the Activity to the top of the stack */
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);

            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

      /* notificationID allows you to update the notification later on. */
            mNotificationManager.notify(notificationID, mBuilder.build());

        }


    }

}
