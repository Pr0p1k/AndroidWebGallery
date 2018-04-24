package itmo.pr0p1k.yandexschooltest;

import android.annotation.SuppressLint;
import android.gesture.GestureOverlayView;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class FullPhotoActivity extends AppCompatActivity {
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full_photo);

        image = findViewById(R.id.photo_full);
        Picasso.with(this).load(getIntent().getStringExtra("Photo")).into(image);
    }

}