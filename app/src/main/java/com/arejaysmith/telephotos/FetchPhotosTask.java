package com.arejaysmith.telephotos;

/**
 * Created by Urge_Smith on 7/21/16.
 */
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Urge_Smith on 7/20/16.
 */
public class FetchPhotosTask extends AsyncTask<String, Void, Photo[]> {

    private final String LOG_TAG = FetchPhotosTask.class.getSimpleName();

    final PhotosListActivity outer;

    //import class to pass list back to main activity
    FetchPhotosTask(PhotosListActivity outer) {

        this.outer = outer;
    }

    @Override
    protected Photo[] doInBackground(String... params) {

        //Holds the connection and buffer
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        Photo[] photoArray = new Photo[1];

        try {

            final String BASE_URL = " https://jsonplaceholder.typicode.com/";
            final String DATA_TYPE = params[0];

            Uri builtUri = Uri.parse(BASE_URL). buildUpon()
                    .appendEncodedPath(DATA_TYPE)
                    .build();

            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG + " my url is: ", url.toString());

            // Create the request open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = url.openStream();


            Reader jsonReader = new InputStreamReader(inputStream, "UTF-8");
            Type collectionType = new TypeToken<Collection<Photo>>(){}.getType();
            Collection<Photo> photo = new Gson().fromJson(jsonReader, collectionType);

            Log.v("The size is", (Integer.toString(photo.size())));
            photoArray = photo.toArray(new Photo[photo.size()]);


        }catch (Exception e){

            Log.e(LOG_TAG, "JSON String: " + e.toString());

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

            return photoArray;

        }catch(Exception e) {

            Log.e(LOG_TAG, e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Photo[] photoData) {
        super.onPostExecute(photoData);

        try{
            outer.getPhotoData(photoData);
        }catch (Exception e){

            Log.e(LOG_TAG, "Can't pass data");
        }
    }
}