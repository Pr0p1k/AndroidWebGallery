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

    PhotoGetter(String token, String YandexAPIURL) {
        this.token = token;
        this.YandexAPIURL = YandexAPIURL;
    }

    private String getJSON(String URL) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(URL).addHeader("authorization", token).build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    private InputStream download(String URL) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(URL).build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().byteStream();
    }

    public Drawable getPhoto(String URL) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        String DownloadURL = Uri.parse("https://cloud-api.yandex.net/v1/disk/resources/download").buildUpon()
                .appendQueryParameter("path", URL).build().toString();
        Request request = new Request.Builder().url(DownloadURL).addHeader("authorization", token).build();
        Response response = okHttpClient.newCall(request).execute();
        JSONObject JSON = new JSONObject(response.body().string());
        Log.i(TAG, JSON.getString("href"));

        return Drawable.createFromStream(download(JSON.getString("href")), "Received picture");
    }

    public List<PhotoItem> getPhotos() {
        List<PhotoItem> itemsList = new ArrayList<>();
        String request = Uri.parse(YandexAPIURL).buildUpon()
                .appendQueryParameter("limit", "40")
                .appendQueryParameter("media_type", "image")
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
            pi.setShortcutURL(JSONItems.getJSONObject(i).getString("preview"));
            pi.setFullURL(JSONItems.getJSONObject(i).getString("path"));
            pi.setImage(getPhoto(pi.getFullURL()));
            list.add(pi);
        }
        return list;
    }

//    public List<PhotoItem> getItems() {
//        List<PhotoItem> itemsList = new ArrayList<>();
//        String URL = Uri.parse(APIServerURL).buildUpon()
//                .appendQueryParameter("method", "flickr.photos.getRecent")
//                .appendQueryParameter("api_key", TOKEN)
//                .appendQueryParameter("format", "json")
//                .appendQueryParameter("nojsoncallback", "1")
//                .appendQueryParameter("extras", "url_s")
//                .build().toString();
//        try {
//            String JSONString = getJSON(URL);
//            JSONObject JSONBody = new JSONObject(JSONString);
//            itemsList = parse(JSONBody);
//        } catch (IOException IOEx) {
//            Log.e(TAG, "IOEXCEPTION БЛЯТЬ!", IOEx);
//        } catch (JSONException JSONEx) {
//            Log.e(TAG, "JSONEXCEPTION БЛЯТЬ!", JSONEx);
//        }
//        return itemsList;
//    }
//
//    private List<PhotoItem> parse(JSONObject body) throws IOException, JSONException {
//        JSONObject photos = body.getJSONObject("photos");
//        JSONArray photo = photos.getJSONArray("photo");
//        List<PhotoItem> list = new ArrayList<>();
//        for (int i = 0; i < photo.length(); i++) {
//            PhotoItem pi = new PhotoItem();
//            pi.setID(photo.getJSONObject(i).getString("id"));
//            pi.setTitle(photo.getJSONObject(i).getString("title"));
//
//            if (!photo.getJSONObject(i).has("url_s")) {
//                continue;
//            }
//            pi.setShortcutURL(photo.getJSONObject(i).getString("url_s"));
//            pi.setFullURL(photo.getJSONObject(i).getString("url_s").substring(0, photo.getJSONObject(i).getString("url_s").length() - 5) + "c.jpg");
//            list.add(pi);
//        }
//        return list;
//    }
}
