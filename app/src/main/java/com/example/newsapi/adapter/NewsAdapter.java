package com.example.newsapi.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapi.R;
import com.example.newsapi.model.NewsArticle;

import java.io.InputStream;
import java.util.List;

/**
 * RecyclerView
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private Context context;
    private List<NewsArticle> dataset;

    /**
     * Constructor
     * @param context - activity
     * @param dataset - List of news
     */
    public NewsAdapter(Context context, List<NewsArticle> dataset){
        this.context = context;
        this.dataset = dataset;
    }

    /**
     * Inflates the view holder for views to be stored in
     * @param parent - parent of view grup
     * @param viewType - view type
     * @return - A view holder with inflated xml.
     */
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Holds only a reference to the list item view (from which we can later find child views)
        View adapterLayout = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new NewsViewHolder(adapterLayout);
    }

    /**
     * Setting the data to each view
     * Here I add a dialog to the image so when the user clicks, they can get more information
     * @param holder ViewHolder containing all views
     * @param position - Currently position of holder
     */
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        // Set the data for each textview and imageview
        holder.newsTitle.setText(dataset.get(position).getTitle());
        holder.newsAuthor.setText(dataset.get(position).getAuthor());
        new DownloadImageTask((ImageView) holder.newsImage)
                .execute(dataset.get(position).getUrlToTimage());
        holder.newsImage.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("InflateParams")
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Opening Dialog", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                View dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null, false);
                TextView dialogDescription = dialogLayout.findViewById(R.id.dialog_description);
                TextView dialogPublishedAt = dialogLayout.findViewById(R.id.dialog_date);
                dialogDescription.setText(dataset.get(holder.getAdapterPosition()).getDescription());
                dialogPublishedAt.setText(dataset.get(holder.getAdapterPosition()).getPublishedAt());
                alertDialog.setView(dialogLayout);
                alertDialog.show();
            }
        });
    }

    /**
     * Returns the count of dataset
     * @return count of dataset
     */
    @Override
    public int getItemCount() {
        return dataset.size();
    }

    /**
     * Binds a specific view to the passed in view such as text views.
     * Hold only ONE list item view
     */
    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView newsAuthor;
        TextView newsTitle;
        ImageView newsImage;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsAuthor= itemView.findViewById(R.id.item_author);
            newsImage = itemView.findViewById(R.id.item_image);
            newsTitle = itemView.findViewById(R.id.item_title);
        }
    }

    /**
     * Helper method to turn a URL image link into a Bitmap image
     * CREDIT: https://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
     */
    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
