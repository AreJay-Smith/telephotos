package com.arejaysmith.telephotos;

/**
 * Created by Urge_Smith on 7/21/16.
 */
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
import java.util.List;

/**
 * Created by Urge_Smith on 7/20/16.
 */
public class FetchPhotosTask extends AsyncTask<String, Void, ArrayList<Photo>> {

    private final String LOG_TAG = FetchPhotosTask.class.getSimpleName();

    final PhotosListActivity outer;

    //import class to pass list back to main activity
    FetchPhotosTask(PhotosListActivity outer) {

        this.outer = outer;
    }

    public ArrayList<Photo> parsePhotosObject(String jsonString) throws JSONException {

        ArrayList<Photo> photoList = new ArrayList<>();

        JSONArray photoJsonArr = new JSONArray(jsonString);

        for (int i = 0; i < photoJsonArr.length(); i++){

            JSONObject currentPhoto = photoJsonArr.getJSONObject(i);

            Photo photo = new Photo();

            photo.setAlbumId(currentPhoto.getInt("albumId"));
            photo.setId(currentPhoto.getInt("id"));
            photo.setTitle(currentPhoto.getString("title"));
            photo.setUrl(currentPhoto.getString("url"));
            photo.setThumbnailUrl(currentPhoto.getString("thumbnailUrl"));

            photoList.add(photo);
        }

        return photoList;
    }

    @Override
    protected ArrayList<Photo> doInBackground(String... params) {

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

            // Create the request to MovieDatabase.org and open the connection
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

            return parsePhotosObject(jsonStr);

        }catch(JSONException e) {

            Log.e(LOG_TAG, e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Photo> photoData) {
        super.onPostExecute(photoData);

        try{
            outer.getPhotoData(photoData);
        }catch (Exception e){

            Log.e(LOG_TAG, "Can't pass data");
        }
    }
}