package itmo.pr0p1k.yandexschooltest;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Александр on 09.04.2018.
 */

public class PhotoGetter {
    private static final String TAG = "PhotoGetter";
    private String token;
    private String YandexAPIURL;
    private String downloadURL = "https://cloud-api.yandex.net/v1/disk/resources/download";

    PhotoGetter(String token, String YandexAPIURL) {
        this.token = token;
        this.YandexAPIURL = YandexAPIURL;
    }

    private String getJSON(String URL) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(URL).addHeader("authorization", "OAuth " + token).build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    private Drawable download(String URL) throws IOException { //Этот метод сразу скачивает фото. Использую для загрузки превью
        try {
            Log.i(TAG, URL);
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(URL).addHeader("authorization", "OAuth " + token).build();
            Response response = okHttpClient.newCall(request).execute();
            return Drawable.createFromStream(new ByteArrayInputStream(response.body().bytes()), "photo");
        } catch (Exception ex) {
            Log.e(TAG, "Exception occurred while downloading a photo", ex);
        }
        return null;
    }

    public String getDownloadLink(String URL) {
        try {
            Log.i(TAG, URL);
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(URL).addHeader("authorization", "OAuth " + token).build();
            Response response = okHttpClient.newCall(request).execute();
            JSONObject JSON = new JSONObject(response.body().string());
            return JSON.getString("href");
        } catch (Exception ex) {
            Log.e(TAG, "Exception occurred while getting a download link", ex);
        }
        return null;

    }

    public List<PhotoItem> getPhotos() {
        List<PhotoItem> itemsList = new ArrayList<>();
        String request = Uri.parse(YandexAPIURL).buildUpon()
                .appendQueryParameter("limit", "40")
                .appendQueryParameter("media_type", "image")
                .appendQueryParameter("preview_size", "M")
                .build().toString();
        try {
            String JSONString = getJSON(request);
            JSONObject JSONBody = new JSONObject(JSONString);
            itemsList = parse(JSONBody);
        } catch (IOException IOEx) {
            Log.e(TAG, "There's a problem with getting JSON", IOEx);
        } catch (JSONException JSONEx) {
            Log.e(TAG, "There's a problem with JSON object", JSONEx);
        }
        return itemsList;
    }

    private List<PhotoItem> parse(JSONObject body) throws JSONException, IOException {
        JSONArray JSONItems = body.getJSONArray("items");
        List<PhotoItem> list = new ArrayList<>();
        for (int i = 0; i < JSONItems.length(); i++) {
            if (!JSONItems.getJSONObject(i).has("preview")) continue;
            PhotoItem pi = new PhotoItem();
            pi.setTitle(JSONItems.getJSONObject(i).getString("name"));
            pi.setImage(download(JSONItems.getJSONObject(i).getString("preview")));
            pi.setFullURL(getDownloadLink(Uri.parse(downloadURL)
                    .buildUpon()
                    .appendQueryParameter("path", JSONItems.getJSONObject(i).getString("path"))
                    .build().toString()));
            list.add(pi);
        }
        return list;
    }
}
