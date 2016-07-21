package com.arejaysmith.telephotos;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<User> userDataList = new ArrayList<>();
    private RecyclerView mUserRecyclerView;
    private UserAdapter mUserAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Telephoto Users");

        FetchUsersTask fetchUsersTask = new FetchUsersTask(this);
        fetchUsersTask.execute("users");

        mUserRecyclerView = (RecyclerView) findViewById(R.id.user_recycler_view);
        mUserRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, mUserRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        Context context = getApplicationContext();

                        User selectedUser = userDataList.get(position);
                        Intent albumIntent = new Intent(context, AlbumActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putParcelable("user", selectedUser);
                        albumIntent.putExtras(mBundle);
                        startActivity(albumIntent);
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        mUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getUserData(ArrayList<User> userDataList){

        this.userDataList = userDataList;

        setAdapter();
    }

    private void setAdapter() {

        mUserAdapter = new UserAdapter(context, userDataList);
        mUserRecyclerView.setAdapter(mUserAdapter);
    }

    private class UserHolder extends RecyclerView.ViewHolder {

        protected TextView mUserName;
        protected ImageView mUserPic;

        public UserHolder(View itemView) {
            super(itemView);

            mUserName = (TextView) itemView.findViewById(R.id.user_name);
            mUserPic = (ImageView) itemView.findViewById(R.id.user_pic);
        }
    }

    private class UserAdapter extends RecyclerView.Adapter<UserHolder> {

        private ArrayList<User> mUsers;
        private Context mContext;

        public UserAdapter(Context context, ArrayList<User> users) {

            mUsers = users;
            this.mContext = context;
        }

        @Override
        public UserHolder onCreateViewHolder(ViewGroup parent, int i) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, null);

            return new UserHolder(view);
        }

        @Override
        public void onBindViewHolder(UserHolder holder, int position) {

            User user = mUsers.get(position);
            int imageId = getResources().getIdentifier("com.arejaysmith.telephotos:drawable/imageview" + user.getId(), null, null);

            holder.mUserName.setText(user.getName());
            holder.mUserPic.setImageResource(imageId);
        }

        @Override
        public int getItemCount() {

            return mUsers.size();
        }
    }
}
