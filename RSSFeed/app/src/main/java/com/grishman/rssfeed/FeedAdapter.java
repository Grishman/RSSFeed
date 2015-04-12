package com.grishman.rssfeed;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grishman.rssfeed.fragments.FeedFragment;

public class FeedAdapter extends CursorAdapter {

    public FeedAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }
    public static class ViewHolder {
//        public final TextView iconView;
        public final TextView titleView;
        public final TextView descriptionView;


        public ViewHolder(View view) {
//            iconView = (TextView) view.findViewById(R.id.list_item_icon);
            titleView = (TextView) view.findViewById(R.id.list_item_title_textview);
            descriptionView = (TextView) view.findViewById(R.id.list_item_desc_textview);
        }
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId=-1;
        layoutId=R.layout.item_feeds_listview;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String title = cursor.getString(FeedFragment.COL_TITLE);
        viewHolder.titleView.setText(title);
        // Read weather forecast from cursor
        String description = cursor.getString(FeedFragment.COL_DESC);
        viewHolder.descriptionView.setText(description);

    }
}
