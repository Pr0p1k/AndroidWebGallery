package itmo.pr0p1k.yandexschooltest;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Александр on 09.04.2018.
 */

public class PhotoGetter {
    private static final String TAG = "PhotoGetter", TOKEN = "31ef209585608ac876b6bfacc29644e7";
    private String APIServerURL = "https://api.flickr.com/services/rest/";

    private String getJSON(String URL) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(URL).build();
        Response response = okHttpClient.newCall(request).execute();
        //Log.e(TAG, response.body().string());
        return response.body().string();
    }

    public List<PhotoItem> getItems() {
        List<PhotoItem> itemsList = new ArrayList<>();
        String URL = Uri.parse(APIServerURL).buildUpon()
                .appendQueryParameter("method", "flickr.photos.getRecent")
                .appendQueryParameter("api_key", TOKEN)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .appendQueryParameter("extras", "url_s")
                .build().toString();
        try {
            String JSONString = getJSON(URL);
            JSONObject JSONBody = new JSONObject(JSONString);
            itemsList = parse(JSONBody);
        } catch (IOException IOEx) {
            Log.e(TAG, "IOEXCEPTION БЛЯТЬ!", IOEx);
        } catch (JSONException JSONEx) {
            Log.e(TAG, "JSONEXCEPTION БЛЯТЬ!", JSONEx);
        }
        return itemsList;
    }

    private List<PhotoItem> parse(JSONObject body) throws IOException, JSONException {
        JSONObject photos = body.getJSONObject("photos");
        JSONArray photo = photos.getJSONArray("photo");
        List<PhotoItem> list = new ArrayList<>();
        for (int i = 0; i < photo.length(); i++) {
            PhotoItem pi = new PhotoItem();
            pi.setID(photo.getJSONObject(i).getString("id"));
            pi.setTitle(photo.getJSONObject(i).getString("title"));

            if (!photo.getJSONObject(i).has("url_s")) {
                continue;
            }
            pi.setShortcutURL(photo.getJSONObject(i).getString("url_s"));
            pi.setFullURL(photo.getJSONObject(i).getString("url_s").substring(0,photo.getJSONObject(i).getString("url_s").length()-5)+"c.jpg");
            list.add(pi);
        }
        return list;
    }
}
