package com.arejaysmith.telephotos;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotosListActivity extends AppCompatActivity {

    private ArrayList<Photo> mPhotosArrayList = new ArrayList<>();
    private RecyclerView mPhotosRecyclerView;
    private Context mContext;
    private PhotoAdapter mPhotoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FetchPhotosTask fetchPhotosTask = new FetchPhotosTask(this);
        fetchPhotosTask.execute("photos");

        mPhotosRecyclerView = (RecyclerView) findViewById(R.id.photos_recycler_view);
        mPhotosRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
    }

    public void getPhotoData(Photo[] photoData) {

        Album mAlbum = getIntent().getParcelableExtra("album");

        for (int i = 0; i < photoData.length; i++) {

            Photo currentPhoto = photoData[i];

            if (currentPhoto.getAlbumId() == mAlbum.getId()) {

                mPhotosArrayList.add(currentPhoto);
            }
        }

        Log.v("SizePhotosArray: ", Integer.toString(mPhotosArrayList.size()));


        setPhotoAdapter();
    }

    private void setPhotoAdapter() {

        mPhotoAdapter = new PhotoAdapter(mPhotosArrayList);
        mPhotosRecyclerView.setAdapter(mPhotoAdapter);
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {

        private ImageView mPhotoView;
        private TextView mPhotoTitle;

        public PhotoHolder(View itemView) {
            super(itemView);

            mPhotoView = (ImageView) itemView.findViewById(R.id.photo_view);
            mPhotoTitle = (TextView) itemView.findViewById(R.id.photo_title);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private ArrayList<Photo> mPhotos;

        public PhotoAdapter(ArrayList<Photo> photoItems) {

            mPhotos = photoItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder (ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_holder, null);

                return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {

            Photo photo = mPhotosArrayList.get(position);

            // have to fix the urls to use https://
            String url = photo.getUrl();
            url = url.substring(7);

            Picasso.with(getApplicationContext()).load("https://" + url).into(holder.mPhotoView);
            holder.mPhotoTitle.setText(photo.getTitle());
        }

        @Override
        public int getItemCount() {

            return mPhotos.size();
        }
    }
}
