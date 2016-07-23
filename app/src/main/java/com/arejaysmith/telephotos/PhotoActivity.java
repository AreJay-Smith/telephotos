package com.arejaysmith.telephotos;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {

    private ImageView mPhotoDownload;
    private TextView mPhotoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        Photo mPhoto = getIntent().getParcelableExtra("photo");

        mPhotoTextView = (TextView) findViewById(R.id.photo_download_title);
        mPhotoTextView.setText(mPhoto.getTitle());

        mPhotoDownload = (ImageView) findViewById(R.id.photo_download);
        Picasso.with(getApplicationContext()).load("https://" + mPhoto.getUrl()).into(mPhotoDownload);
    }

}
