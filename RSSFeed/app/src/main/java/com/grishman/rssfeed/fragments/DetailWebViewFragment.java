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
    private WebView detailWebView;
    private Bundle webViewBundle;

    public DetailWebViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        detailWebView.saveState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (webViewBundle == null) {
            detailWebView.restoreState(webViewBundle);
        }
//        detailWebView.restoreState(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        detailWebView = (WebView) rootView.findViewById(R.id.webview);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUrl = arguments.getString(DetailWebViewFragment.DETAIL_URL);
        }
        Log.d("URT TO DETAIL", "" + mUrl);
        // TODO Fix rotation
        detailWebView.getSettings().setJavaScriptEnabled(true);
        detailWebView.getSettings().setDomStorageEnabled(true);
        detailWebView.setWebViewClient(new FeedWebViewClient());
//        detailWebView.loadUrl(mUrl);

        if (webViewBundle == null) {
            detailWebView.loadUrl(mUrl);
        } else {
            detailWebView.restoreState(webViewBundle);
        }
        return rootView;
    }
    @Override
    public void onPause() {
        super.onPause();

        webViewBundle = new Bundle();
        detailWebView.saveState(webViewBundle);
    }

    private class FeedWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
