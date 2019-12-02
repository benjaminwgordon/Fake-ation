package com.example.picturemashup;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String top= "https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=4ed60a6252f6a97f733595aa0f6a9875&tags=";
    private String bottom= "&extras=url_s&format=json&nojsoncallback=1";

    private String baseUrl;

    public ArrayList<String> pictures= new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void getJson( View v){

        pictures= new ArrayList<String>();
        EditText input= findViewById(R.id.input);
        String search= input.getText().toString();
        baseUrl= top + search + bottom;
        new pictures().execute();
    }




    private class pictures extends AsyncTask<Object, Void, JSONObject> {


        /**
         * This method receives a json line and adds the author, article name, article website, url
         * preview of the article into an Arraylist that contains a hashmap inside.
         * @param objects
         */
        @Override
        protected JSONObject doInBackground(Object[] objects) {
            try {

                String json = "";
                String line;

                URL url = new URL((baseUrl));


                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                while ((line = in.readLine()) != null) {
                    //System.out.println("JSON LINE " + line);
                    json += line;
                }
                in.close();

                JSONObject jsonObject = new JSONObject(json);

                JSONObject pics= jsonObject.getJSONObject("photos");

                JSONArray allPhotos= pics.getJSONArray("photo");

                for(int i=10; i< 30; i++){
                    String check= allPhotos.getJSONObject(i).getString("url_s");
                    pictures.add(check);
                    System.out.println(check);

                }
                System.out.println(pictures.size());


                return jsonObject;
            }

            catch (Exception e) { e.printStackTrace(); }

            return null;
        }


        @Override
        protected void onPostExecute(JSONObject json) {

            ListView allPictures= findViewById(R.id.all);
            ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(MainActivity.this, R.layout.activity_each_row, R.id.each_image, pictures);
            allPictures.setAdapter(arrayAdapter);
        }

        public Bitmap getBitmapFromURL(String src) {
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }



    }







}
