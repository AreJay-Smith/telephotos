package com.arejaysmith.telephotos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {

    private ImageView mPhotoDownload;
    private TextView mPhotoTextView;
    private static final String ALBUM_INDEX = "album";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle("Photo");
        Photo mPhoto = getIntent().getParcelableExtra("photo");

        mPhotoTextView = (TextView) findViewById(R.id.photo_download_title);
        mPhotoTextView.setText(mPhoto.getTitle());

        mPhotoDownload = (ImageView) findViewById(R.id.photo_download);
        Picasso.with(getApplicationContext()).load("https://" + mPhoto.getUrl()).into(mPhotoDownload);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Photo mPhoto = getIntent().getParcelableExtra("photo");

        savedInstanceState.putInt(ALBUM_INDEX, mPhoto.getAlbumId());
    }
}
