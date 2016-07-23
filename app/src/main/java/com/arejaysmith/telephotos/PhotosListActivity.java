package com.arejaysmith.telephotos;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotosListActivity extends AppCompatActivity {

    private ArrayList<Photo> mPhotosArrayList = new ArrayList<>();
    private RecyclerView mPhotosRecyclerView;
    private Context mContext;
    private PhotoAdapter mPhotoAdapter;
    private int mSavedAlbumId;
    private String KEY_ALBUM_ID = "albumId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Album mAlbum = getIntent().getParcelableExtra("album");
        setTitle(mAlbum.getTitle());
        if(savedInstanceState != null) {

            mSavedAlbumId = savedInstanceState.getInt(KEY_ALBUM_ID, 0);
        }
            if (isNetworkAvailable()) {

                // Get new array
                FetchPhotosTask fetchPhotosTask = new FetchPhotosTask(this);
                fetchPhotosTask.execute("photos");
            } else {
                // TODO: create a broadcast receiver for when a connection is available
                Toast.makeText(getApplicationContext(), "No internet connection",
                        Toast.LENGTH_LONG).show();
        }

        mPhotosRecyclerView = (RecyclerView) findViewById(R.id.photos_recycler_view);
        mPhotosRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mPhotosRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, mPhotosRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        Context context = getApplicationContext();

                        Photo selectedPhoto = mPhotosArrayList.get(position);
                        Intent photoIntent = new Intent(context, PhotoActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putParcelable("photo", selectedPhoto);
                        photoIntent.putExtras(mBundle);
                        startActivity(photoIntent);
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // More options
                    }
                })
        );
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void getPhotoData(Photo[] photoData) {

        Album mAlbum = getIntent().getParcelableExtra("album");
        int albumIdSelected;

        if (mSavedAlbumId != 0) {

            albumIdSelected = mSavedAlbumId;
        }else{

            albumIdSelected = mAlbum.getId();
        }

        for (int i = 0; i < photoData.length; i++) {

            Photo currentPhoto = photoData[i];

            if (currentPhoto.getAlbumId() == albumIdSelected) {

                // have to fix the urls to use https:// so erase the http:// which isn't necessary
                String url = currentPhoto.getUrl();
                url = url.substring(7);

                currentPhoto.setUrl(url);
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

            Picasso.with(getApplicationContext()).load("https://" + photo.getUrl()).into(holder.mPhotoView);
            holder.mPhotoTitle.setText(photo.getTitle());
        }

        @Override
        public int getItemCount() {

            return mPhotos.size();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        Album mAlbum = getIntent().getParcelableExtra("album");
        outState.putInt(KEY_ALBUM_ID, mAlbum.getId());

        super.onSaveInstanceState(outState);
    }
}
