package com.grishman.rssfeed.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.grishman.rssfeed.R;

/**
 * A Detail View fragment containing a web view
 * for displaying URL from feed list.
 */
public class DetailWebViewFragment extends Fragment {

    public DetailWebViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        WebView myWebView = (WebView) rootView.findViewById(R.id.webview);
        Bundle extras = getActivity().getIntent().getExtras();
        String urlFromIntent="http://google.com";
        if (extras != null) {
            urlFromIntent = extras.getString("URL");
        }
        myWebView.loadUrl(urlFromIntent);
        return rootView;
    }
}
