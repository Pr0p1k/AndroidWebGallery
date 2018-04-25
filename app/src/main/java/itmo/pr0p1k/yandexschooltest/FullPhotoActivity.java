package itmo.pr0p1k.yandexschooltest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

public class FullPhotoActivity extends AppCompatActivity {
    private ImageView image;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full_photo);

        image = findViewById(R.id.photo_full);
        Picasso.with(this).load(getIntent().getStringExtra("Photo")).into(image);
    }

}