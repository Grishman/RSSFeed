package com.grishman.rssfeed.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.grishman.rssfeed.data.RSSFeedContract;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
        rssHandler=new RSSHandler();

        try {

            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();

            url = new URL(feedUrl);

            xr.setContentHandler((org.xml.sax.ContentHandler) rssHandler);
            xr.parse(new InputSource(url.openStream()));


        } catch (IOException e) {
            Log.e("RSS Handler IO", e.getMessage() + " >> " + e.toString());
        } catch (SAXException e) {
            Log.e("RSS Handler SAX", e.toString());
        } catch (ParserConfigurationException e) {
            Log.e("RSS Handler Parser Config", e.toString());
        }
// TODO refactor this sht
        ContentValues cv = new ContentValues();
        for (RSSFeedItem item : articleList2 = rssHandler.getArticleList()) {
            Log.d(TAG, item.getImgLink());
            cv.put("title",item.getTitle());
            cv.put("description",item.getDescription());
            cv.put("link",item.getLink());
            cv.put("img_url",item.getImgLink());
            cv.put("category",item.getCategory());
            cv.put("pub_date",item.getPubDate());
            getApplicationContext().getContentResolver().insert(RSSFeedContract.FeedsEntry.CONTENT_URI,cv);
        }

    }
    public static class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent sendIntent = new Intent(context, FeedParserService.class);
            context.startService(sendIntent);

        }
    }

}
