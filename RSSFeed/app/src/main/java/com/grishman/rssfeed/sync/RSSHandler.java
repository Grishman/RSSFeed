package com.grishman.rssfeed.sync;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class RSSHandler extends DefaultHandler {
    // Feed and Article objects to use for temporary storage
    private RSSFeedItem currentArticle = new RSSFeedItem();

    public List<RSSFeedItem> getArticleList() {
        return articleList;
    }

    private List<RSSFeedItem> articleList = new ArrayList<>();
    //Current characters being accumulated
    StringBuffer chars;
    private int articlesAdded = 0;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        chars = new StringBuffer();
        if (localName.trim().equals("thumbnail")) {
//            Log.d("LOGGING RSS XML", "Setting img URL: " + chars.toString());
            String thumbnail = attributes.getValue("url");
            currentArticle.setImgLink(thumbnail);
            Log.d("LOGGING RSS XML", "Setting img URL: " + currentArticle.getImgLink());
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equalsIgnoreCase("title")) {
            Log.d("LOGGING RSS XML", "Setting article title: " + chars.toString());
            currentArticle.setTitle(chars.toString());

        } else if (localName.equalsIgnoreCase("description")) {
            Log.d("LOGGING RSS XML", "Setting article description: " + chars.toString());
            currentArticle.setDescription(chars.toString());
        } else if (localName.equalsIgnoreCase("pubDate")) {
            Log.d("LOGGING RSS XML", "Setting article published date: " + chars.toString());
            currentArticle.setPubDate(chars.toString());
        } else if (localName.equalsIgnoreCase("category")) {
            Log.d("LOGGING RSS XML", "Setting article category: " + chars.toString());
            currentArticle.setCategory(chars.toString());
        } else if (localName.equalsIgnoreCase("item")) {

        } else {
            if (localName.equalsIgnoreCase("link")) {
                Log.d("LOGGING RSS XML", "Setting article link url: " + chars.toString());
                currentArticle.setLink(chars.toString());

            }
        }

        // Check if looking for article, and if article is complete
        if (localName.equalsIgnoreCase("item")) {

            articleList.add(currentArticle);

            currentArticle = new RSSFeedItem();

//            // Lets check if we've hit our limit on number of articles
//            articlesAdded++;
//            if (articlesAdded >= ARTICLES_LIMIT)
//            {
//                throw new SAXException();
//            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        chars.append(new String(ch, start, length));
    }
//    Context mCont = new Activity().getApplication().getApplicationContext();
//    public void saveToDB(RSSFeedItem item){
//        ContentValues cv = new ContentValues();
////            Log.d(TAG, item.getImgLink());
//            cv.put("title",item.getTitle());
//            cv.put("description",item.getDescription());
//            cv.put("link",item.getLink());
//            cv.put("img_url",item.getImgLink());
//            cv.put("category",item.getCategory());
//            cv.put("pub_date",item.getPubDate());
//            mCont.getContentResolver().insert(RSSFeedContract.FeedsEntry.CONTENT_URI,cv);
//    }
}
