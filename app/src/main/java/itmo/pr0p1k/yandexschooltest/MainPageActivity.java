package itmo.pr0p1k.yandexschooltest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {
    private RecyclerView photoContent;
    private final String TAG = "MainPageActivity";
    private List<PhotoItem> itemsList = new ArrayList<>();
    private int amountOfRows;
    private String token;
    private final String YandexAPIURL = "https://cloud-api.yandex.net/v1/disk/resources/files";
    private PhotoGetter photoGetter;
    // private PhotoItem currentPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        photoContent = findViewById(R.id.photo_list);
        amountOfRows = 3;
        token = getIntent().toUri(Intent.URI_ALLOW_UNSAFE);
        token = token.substring(token.indexOf("access_token=") + 13, token.indexOf("&"));
        photoGetter = new PhotoGetter(token, YandexAPIURL);
        photoContent.setLayoutManager(new GridLayoutManager(this, amountOfRows));
        new GetItemTask().execute();
        setAdapter();
    }

    private class PhotoItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView itemImageView;

        public PhotoItemHolder(View itemView) {
            super(itemView);
            itemImageView = itemView.findViewById(R.id.photo_object);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(MainPageActivity.this, FullPhotoActivity.class);
                intent.putExtra("Photo", itemsList.get(position).getFullURL());
                startActivity(intent);
            }
        }
    }

    private void setAdapter() {
        photoContent.setAdapter(new PhotoItemAdapter(itemsList));
    }

    private class PhotoItemAdapter extends RecyclerView.Adapter<PhotoItemHolder> {
        List<PhotoItem> collection;

        PhotoItemAdapter(List<PhotoItem> collection) {
            this.collection = collection;
        }

        @Override
        public void onBindViewHolder(final PhotoItemHolder holder, int position) {
            holder.itemImageView.setImageDrawable(collection.get(position).getImage());
        }

        @Override
        public int getItemCount() {
            return collection.size();
        }

        @Override
        public PhotoItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(MainPageActivity.this);
            View view = inflater.inflate(R.layout.item_photo, parent, false);
            return new PhotoItemHolder(view);
        }

    }

    private class GetItemTask extends AsyncTask<Void, Void, List<PhotoItem>> {
        @Override
        protected List<PhotoItem> doInBackground(Void... voids) {
            return photoGetter.getPhotos();
        }

        @Override
        protected void onPostExecute(List<PhotoItem> photoItems) {
            itemsList = photoItems;
            setAdapter();
        }
    }
}