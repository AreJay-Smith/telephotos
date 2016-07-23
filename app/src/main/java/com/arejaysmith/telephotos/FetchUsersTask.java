package com.arejaysmith.telephotos;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by Urge_Smith on 7/20/16.
 */
public class FetchUsersTask extends AsyncTask<String, Void, ArrayList<User>> {

    private final String LOG_TAG = FetchUsersTask.class.getSimpleName();

    final MainActivity outer;

    //import class to pass list back to main activity
    FetchUsersTask(MainActivity outer) {

        this.outer = outer;
    }

    public ArrayList<User> parseUserObject(String userJsonString) throws JSONException {

        ArrayList<User> userList = new ArrayList<>();

        JSONArray userJsonArr = new JSONArray(userJsonString);

        for (int i = 0; i < userJsonArr.length(); i++){

            JSONObject currentUser = userJsonArr.getJSONObject(i);

            User user = new User();

            user.setId(currentUser.getInt("id"));
            user.setName(currentUser.getString("name"));

            userList.add(user);
            Log.v("My row : ", user.toString());

        }

        return userList;
    }

    @Override
    protected ArrayList<User> doInBackground(String... params) {

        //Holds the connection and buffer
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String jsonStr = null;

        try {

            final String BASE_URL = " https://jsonplaceholder.typicode.com/";
            final String DATA_TYPE = params[0];

            Uri builtUri = Uri.parse(BASE_URL). buildUpon()
                    .appendEncodedPath(DATA_TYPE)
                    .build();

            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG + " my url is: ", url.toString());

            // Create the request and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null){

                Log.e(LOG_TAG, "Input Stream is empty");
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                jsonStr = buffer.toString();

            }

        }catch (Exception e){

            Log.e(LOG_TAG, "JSON String: " + jsonStr);

        }finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }

        }

        try{

            return parseUserObject(jsonStr);

        }catch(JSONException e) {

            Log.e(LOG_TAG, e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<User> userData) {
        super.onPostExecute(userData);

        try{
            outer.getUserData(userData);
        }catch (Exception e){

            Log.e(LOG_TAG, "Can't pass data");
        }
    }
}
