package com.example.newsapi;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

/**
 * This class is an AsyncTaskLoader that retrieves information in the background
 */
public class NewsLoader extends AsyncTaskLoader<String> {

    private String mQueryString;

    /**
     * Constructor and setting mQueryString although it is not needed here.
     * @param context - context of activity
     * @param queryString - string
     */
    public NewsLoader(@NonNull Context context, String queryString) {
        super(context);
        mQueryString = queryString;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    /**
     * This method runs in the background to get the information from NewsAPI
     * @return a JSON object containing data
     */
    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.getNewsInfo(mQueryString);
    }
}
