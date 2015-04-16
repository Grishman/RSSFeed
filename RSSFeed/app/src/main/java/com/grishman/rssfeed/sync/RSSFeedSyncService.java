package com.grishman.rssfeed.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class RSSFeedSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static RSSFeedSyncAdapter sRSSFeedSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("RSSFeedSyncService", "onCreate - RSSFeedSyncService");
        synchronized (sSyncAdapterLock) {
            if (sRSSFeedSyncAdapter == null) {
                sRSSFeedSyncAdapter = new RSSFeedSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sRSSFeedSyncAdapter.getSyncAdapterBinder();
    }
}