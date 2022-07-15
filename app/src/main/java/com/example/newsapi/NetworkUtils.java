package com.example.newsapi;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * NetworkUtils will call to NewsAPI and retrieve the top headlines from sources such as BBC News
 */
public class NetworkUtils {
    private static final String LOG_TAG =
            "Outputs";
    // Base URL for News API.
    private static final String API_KEY= "abfcbea2369c407b979303f7171b58dd";
    private static final String API_KEY_TEXT= "apiKey";
    private static final String NEWS_URL = "https://newsapi.org/v2/top-headlines?";
    private static final String SOURCES = "sources";

    /**
     * Calls to NewsAPI to retrieve a JSON object from the web service
     * @param queryString - for a specific source
     * @return - JSON object containing the response
     */
    static String getNewsInfo(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String newsJSONString = null;
        try {
            // Build the URI with request parameters
            Uri builtURI = Uri.parse(NEWS_URL).buildUpon()
                    .appendQueryParameter(SOURCES, "bbc-news")
                    .appendQueryParameter(API_KEY_TEXT, API_KEY)
                    .build();
            // Build the URL, ensure setRequestProperty is there or else it will fail
            URL requestURL = new URL(builtURI.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
            urlConnection.connect();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();
            // Create a buffered reader from that input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream));

            // Use a StringBuilder to hold the incoming response.
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                // Since it's JSON, adding a newline isn't necessary (it won't
                // affect parsing) but it does make debugging a *lot* easier
                // if you print out the completed buffer for debugging.
                builder.append("\n");
            }
            if (builder.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }
            newsJSONString = builder.toString();

        } catch (IOException e) {
            Log.d("Outputs", "Error: Network request is not handled correctly");
            e.printStackTrace();
        } finally {
            // Ensure to disconnect the connection when you are done
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            // Close the reader when done
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        // Output the JSON string
        Log.d("Outputs", "JSON response from API: " + newsJSONString);
        return newsJSONString;
    }
}

