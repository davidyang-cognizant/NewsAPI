package com.example.newsapi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newsapi.adapter.NewsAdapter;
import com.example.newsapi.model.NewsArticle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Main Activity that will be run when the emulator is started.
 * In this application, it will retrieve the top 10 trending BBC News
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private List<NewsArticle> newsArticleList;
    private RecyclerView recyclerView;
    private NewsArticleDatabase db;
    private EditText filter_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsArticleList = new ArrayList<NewsArticle>();
        recyclerView = findViewById(R.id.recyclerView);
        filter_et = findViewById(R.id.filter_et);

        // Initialize database
        db = NewsArticleDatabase.getInstance(getApplicationContext());

        // **Be careful! This nuke method removes the table in Rooms Database**
        db.articleDao().nuke();

        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }
        searchNews();
    }


    /**
     * Connects to the NewsAPI to retrieve information about Top 10 BBC News
     */
    public void searchNews() {
        /*
        Checking to see if there is a connection to the web
         */
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        // If the network is connected then retrieve the news
        if (networkInfo != null && networkInfo.isConnected()) {
            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", "");
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        } else {
            Log.d("Outputs", "Network is null");
        }
    }

    /**
     * This method is called when the loader is to be created
     *
     * @param id   - ignore
     * @param args - any extra information needed
     * @return a string object containing JSON information returned from NewsLoader
     */
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";
        if (args != null) {
            queryString = args.getString("queryString");
        }

        return new NewsLoader(this, queryString);
    }

    /**
     * When onCreateLoader is completed, onLoadFinished will extract data from the parameters
     * and populate the recylcer view.
     *
     * @param loader - loader
     * @param data   - JSON
     */
    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray itemsArray = jsonObject.getJSONArray("articles");

            // Iterate through and create a list containing news articles
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject article = itemsArray.getJSONObject(i);
                String author = "";
                String title = "";
                String description = "";
                String urlToImage = "";
                String publishedAt = "";

                // All of these are necessary for checking is a key is available
                if (article.has("author")) {
                    author = article.get("author").toString();
                }
                if (article.has("title")) {
                    title = article.get("title").toString();
                }
                if (article.has("description")) {
                    description = article.get("description").toString();
                }
                if (article.has("urlToImage")) {
                    urlToImage = article.get("urlToImage").toString();
                }
                if (article.has("publishedAt")) {
                    String time = article.get("publishedAt").toString().substring(0, article.get("publishedAt").toString().indexOf('T'));
//                    SimpleDateFormat prevFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
//                    SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
//                    Date date = prevFormat.parse(article.get("publishedAt").toString());
//                    assert date != null;
                    publishedAt = time;
                }

                // Instantiate an object
                NewsArticle newsArticle = new NewsArticle(author, title, description, urlToImage, publishedAt);

                // Insert article into the database
                db.articleDao().insertArticle(newsArticle);

                // Add a new article into the array list.
//                newsArticleList.add(newsArticle); // Old way of adding to recycler view
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // If the array is not empty, set the adapter to the recycler view with data

//        if (!newsArticleList.isEmpty()) {
//            Log.d("Outputs", "Setting recycler view");
//            recyclerView.setAdapter(new NewsAdapter(this, newsArticleList));
//            recyclerView.setHasFixedSize(true);
//        }

        // Call to rooms database to populate the recycler view!
        populateRecyclerView();

    }

    /**
     * Populate recycler view by querying to the database to retrieving articles
     */
    private void populateRecyclerView() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
            final List<NewsArticle> articles = db.articleDao().getArticles();

            // Run on the UI thread rather than main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(new NewsAdapter(MainActivity.this, articles));
                        recyclerView.setHasFixedSize(true);
                    }
                });
            }
        });
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    /**
     * Handles filtering of titles specified by the user
     * @param view button
     */
    public void handleFilter(View view) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    String title = filter_et.getText().toString();
                    final List<NewsArticle> articles = db.articleDao().loadArticleByTitle(title);
                    // Run on the UI thread rather than main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(new NewsAdapter(MainActivity.this, articles));
                            recyclerView.setHasFixedSize(true);
                        }
                    });
                }
            });

    }
}