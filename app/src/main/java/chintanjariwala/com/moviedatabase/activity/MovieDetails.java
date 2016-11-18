package chintanjariwala.com.moviedatabase.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import chintanjariwala.com.moviedatabase.R;
import chintanjariwala.com.moviedatabase.network.VolleySingleton;

public class MovieDetails extends AppCompatActivity {

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private final static String TAG = MovieDetails.class.getSimpleName();
    private ImageView movieBackdrop, moviePosterThumbnail;
    private TextView movieTitle, movieDescription;
    private String baseURL = VolleySingleton.base_request_url;
    private String imageURL = VolleySingleton.base_image_url_500;
    private ImageLoader imageLoader;
    private String movieID = "";
    public MovieDetails() {
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        imageLoader = volleySingleton.getImageLoader();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        getMovieDetails();
    }

    private void getMovieDetails() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, generateMovieDetailUrl(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                populateJSONData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.toString() );
            }
        });
        requestQueue.add(request);
    }

    private void populateJSONData(JSONObject response) {
        if(response == null && response.length() == 0){
            return;
        }

        if (response.has("backdrop_path")){
            try {
                String image = imageURL + response.getString("backdrop_path");
                imageLoader.get(image, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        movieBackdrop.setImageBitmap(response.getBitmap());
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private String generateMovieDetailUrl() {
        Intent intent = getIntent();
        movieID = intent.getStringExtra("movieDetail");
        Log.d(TAG, "generateMovieDetailUrl: " + movieID);
        Uri builtURI = Uri.parse(baseURL).buildUpon()
                .appendPath("movie").appendPath(movieID).appendQueryParameter("api_key",getString(R.string.TMDB_api_key))
                .appendQueryParameter("language","en-US").build();
        Log.d(TAG, "getTheTrendingData: "+ builtURI.toString() );
        return builtURI.toString();
    }

    private void init() {
        moviePosterThumbnail = (ImageView) findViewById(R.id.moviePosterThumbnail);
        movieBackdrop = (ImageView) findViewById(R.id.movieBackdrop);
        movieTitle = (TextView) findViewById(R.id.movieTitle);
        movieDescription = (TextView) findViewById(R.id.movieDescription);
    }

}
