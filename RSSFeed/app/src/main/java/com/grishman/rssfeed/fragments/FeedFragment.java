package com.grishman.rssfeed.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.grishman.rssfeed.DetailActivity;
import com.grishman.rssfeed.R;

/**
 * A Feed fragment containing list view for feed list.
 */
public class FeedFragment extends Fragment {

    public FeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        final ListView mFeedList = (ListView) rootView.findViewById(R.id.list_feed);
        final String[] values = new String[]{"http://abcnews.go.com/US/tiny-illinois-town-slammed-deadly-tornado/story?id=30217421", "http://abcnews.go.com/US/south-carolina-police-officers-mom-speaks-tearful-interview/story?id=30207558", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2"};
         ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        mFeedList.setAdapter(adapter);
        mFeedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent sendFeedURL = new Intent(getActivity(), DetailActivity.class);
                sendFeedURL.putExtra("URL", values[position]);
                startActivity(sendFeedURL);
            }
        });
        return rootView;
    }
}
