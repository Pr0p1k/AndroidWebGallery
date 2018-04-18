package itmo.pr0p1k.yandexschooltest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = new WebView(this);
        setContentView(webView);
        webView.loadUrl("https://oauth.yandex.ru/authorize?" +
                "response_type=token" +
                "&client_id=d1144b9228da41da8ba8c72322f9f595");
    }
}
