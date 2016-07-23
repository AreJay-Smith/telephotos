package com.arejaysmith.telephotos;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {

    private ArrayList<Album> mAlbumList = new ArrayList<>();
    private RecyclerView mAlbumRecyclerView;
    private Context mContext;
    private AlbumAdapter mAlbumAdapter;
    private String KEY_USER_ID = "userId";
    private int mUserIdSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        User mUser = getIntent().getParcelableExtra("user");
        setTitle(mUser.getName() + "'s Albums");
        if (savedInstanceState != null) {

            mUserIdSelected = savedInstanceState.getInt(KEY_USER_ID, 0);
        }

        if(isNetworkAvailable()) {

            FetchAlbumsTask fetchAlbumsTask = new FetchAlbumsTask(this);
            fetchAlbumsTask.execute("albums");
        }else{
            // TODO: create a broadcast receiver for when a connection is available
            Toast.makeText(getApplicationContext(), "No internet connection",
                    Toast.LENGTH_LONG).show();
        }

        mAlbumRecyclerView = (RecyclerView) findViewById(R.id.album_recycler_view);
        mAlbumRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAlbumRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, mAlbumRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        Context context = getApplicationContext();

                        Album selectedAlbum = mAlbumList.get(position);
                        Intent photosListIntent = new Intent(context, PhotosListActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putParcelable("album", selectedAlbum);
                        photosListIntent.putExtras(mBundle);
                        startActivity(photosListIntent);
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void getAlbumData(ArrayList<Album> albumDataList){

        User mUser = getIntent().getParcelableExtra("user");
        int userIdSelected;

        if (mUserIdSelected != 0) {

            userIdSelected = mUserIdSelected;
        }else{
            userIdSelected = mUser.getId();
        }

        for (int i = 0; i < albumDataList.size(); i++) {

            Album currentAlbum = albumDataList.get(i);

            if (currentAlbum.getUserId() == userIdSelected) {

                mAlbumList.add(currentAlbum);
            }
        }

        setAlbumAdapter();
    }

    private void setAlbumAdapter() {

        mAlbumAdapter = new AlbumAdapter(mContext, mAlbumList);
        mAlbumRecyclerView.setAdapter(mAlbumAdapter);
    }

    private class AlbumHolder extends RecyclerView.ViewHolder {

        protected TextView mAlbumTitle;

        public AlbumHolder(View itemView) {
            super(itemView);

            mAlbumTitle = (TextView) itemView.findViewById(R.id.album_title);
        }
    }

    private class AlbumAdapter extends RecyclerView.Adapter<AlbumHolder> {

        private ArrayList<Album> mAlbums;
        private Context mContext;

        public AlbumAdapter(Context context, ArrayList<Album> albums) {

            mAlbums = albums;
            this.mContext = context;
        }

        @Override
        public AlbumHolder onCreateViewHolder(ViewGroup parent, int i) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_row, null);

            return new AlbumHolder(view);
        }

        @Override
        public void onBindViewHolder(AlbumHolder holder, int position) {

            Album album = mAlbumList.get(position);

            holder.mAlbumTitle.setText(album.getTitle());
        }

        @Override
        public int getItemCount() {

            return mAlbums.size();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        User mUser = getIntent().getParcelableExtra("user");
        outState.putInt(KEY_USER_ID, mUser.getId());

        super.onSaveInstanceState(outState);
    }
}
