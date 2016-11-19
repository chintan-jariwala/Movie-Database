package chintanjariwala.com.moviedatabase.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import chintanjariwala.com.moviedatabase.R;
import chintanjariwala.com.moviedatabase.adapters.MovieListAdapter;
import chintanjariwala.com.moviedatabase.network.VolleySingleton;
import chintanjariwala.com.moviedatabase.pojo.Movie;

import static chintanjariwala.com.moviedatabase.utils.Keys.KEY_DESCRIPTION;
import static chintanjariwala.com.moviedatabase.utils.Keys.KEY_FIRST_AIR_DATE;
import static chintanjariwala.com.moviedatabase.utils.Keys.KEY_ID;
import static chintanjariwala.com.moviedatabase.utils.Keys.KEY_MOVIES;
import static chintanjariwala.com.moviedatabase.utils.Keys.KEY_POSTER;
import static chintanjariwala.com.moviedatabase.utils.Keys.KEY_RELEASE_DATE;
import static chintanjariwala.com.moviedatabase.utils.Keys.KEY_TITLE;
import static chintanjariwala.com.moviedatabase.utils.Keys.KEY_TITLE_TV;
import static chintanjariwala.com.moviedatabase.utils.Keys.KEY_VOTE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvAiringTodayFragment extends Fragment {

    private static final String TAG = TvAiringTodayFragment.class.getSimpleName();
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;

    private ArrayList<Movie> listMovies = new ArrayList<>();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private RecyclerView airingToday;
    private MovieListAdapter movieListAdapter;

    private String baseURL = VolleySingleton.base_request_url;

    public TvAiringTodayFragment() {
        Log.d(TAG, "onCreate: " + baseURL);
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    public void sendJSONRequest() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getTheTrendingData(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listMovies = parseJSONResponse(response);
                movieListAdapter.setMovieArrayList(listMovies);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.toString());
            }
        });
        requestQueue.add(request);
    }
    private ArrayList<Movie> parseJSONResponse(JSONObject response) {
        ArrayList<Movie> listMovies = new ArrayList<>();
        if(response == null && response.length() == 0){
            return listMovies;
        }
        try {
            if(response.has(KEY_MOVIES)){
                JSONArray arrayMovies = response.getJSONArray(KEY_MOVIES);
                for (int i = 0; i < arrayMovies.length() ; i++) {
                    JSONObject currentMovie = arrayMovies.getJSONObject(i);
                    long id = 0;
                    if(currentMovie.has(KEY_ID)){
                        id = currentMovie.getLong(KEY_ID);
                    }
                    String title = "Title not there";
                    if(currentMovie.has(KEY_TITLE_TV)){
                        title = currentMovie.getString(KEY_TITLE_TV);
                    }

                    String releaseDate = "0000-00-00";
                    if(currentMovie.has(KEY_FIRST_AIR_DATE)){
                        releaseDate = currentMovie.getString(KEY_FIRST_AIR_DATE);
                    }

                    double voteAverage = -1;
                    if(currentMovie.has(KEY_VOTE)){
                        voteAverage = currentMovie.getDouble(KEY_VOTE);
                    }
                    String url_thumbnail = "null";
                    if(currentMovie.has(KEY_POSTER)){
                        url_thumbnail = currentMovie.getString(KEY_POSTER);
                    }
                    Date curMovieDate = dateFormat.parse(releaseDate);
                    String overview = "Description not there";
                    if(currentMovie.has(KEY_DESCRIPTION)){
                        overview = currentMovie.getString(KEY_DESCRIPTION);
                    }

                    Movie movie = new Movie(id,title, curMovieDate, voteAverage, url_thumbnail,overview);
                    Log.d(TAG, "parseJSONOResponse: " + currentMovie.getString(KEY_ID) + " " + currentMovie.getString(KEY_TITLE_TV));
                    listMovies.add(movie);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return listMovies;

    }
    private String getTheTrendingData() {
        Uri builtURI = Uri.parse(baseURL).buildUpon()
                .appendPath("tv").appendPath("airing_today").appendQueryParameter("api_key",getString(R.string.TMDB_api_key))
                .appendQueryParameter("language","en-US").build();
        Log.d(TAG, "getTheTrendingData: "+ builtURI.toString() );
        return builtURI.toString();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv_airing_today, container, false);
        airingToday = (RecyclerView) view.findViewById(R.id.airingTodayTv);
        airingToday.setLayoutManager(new LinearLayoutManager(getActivity()));
        movieListAdapter = new MovieListAdapter(getActivity());
        airingToday.setAdapter(movieListAdapter);
        sendJSONRequest();
        return view;
    }

}
