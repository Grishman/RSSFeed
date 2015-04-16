package com.grishman.rssfeed.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.grishman.rssfeed.R;

/**
 * A Detail View fragment containing a web view
 * for displaying URL from feed list.
 */
public class DetailWebViewFragment extends Fragment {

    public static final String DETAIL_URL = "URL";
    private String mUrl;

    public DetailWebViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        WebView myWebView = (WebView) rootView.findViewById(R.id.webview);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUrl = arguments.getString(DetailWebViewFragment.DETAIL_URL);
        }
        Log.d("URT TO DETAIL", "" + mUrl);
        // TODO Fix rotation
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new FeedWebViewClient());
        myWebView.loadUrl(mUrl);
        return rootView;
    }

    private class FeedWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
