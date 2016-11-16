package chintanjariwala.com.moviedatabase.Fragment;

import android.content.Context;
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

import chintanjariwala.com.moviedatabase.Adapters.HomeAdapter;
import chintanjariwala.com.moviedatabase.Network.VolleySingleton;
import chintanjariwala.com.moviedatabase.Pojo.Movie;
import chintanjariwala.com.moviedatabase.R;
import static chintanjariwala.com.moviedatabase.Utils.Keys.*;


public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = HomeFragment.class.getSimpleName();

    private String mParam1;
    private String mParam2;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;

    private String baseURL = VolleySingleton.base_request_url;

    private OnFragmentInteractionListener mListener;
    private ArrayList<Movie> listMovies = new ArrayList<>();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private RecyclerView nowPlaying;
    private HomeAdapter homeAdapter;
    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.d(TAG, "onCreate: " + baseURL);
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    private void sendJSONrequest() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getTheTrendingData(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: "+response.toString());
                listMovies = parseJSONOResponse(response);
                homeAdapter.setMovieArrayList(listMovies);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: "+error.toString() );
            }
        });
        requestQueue.add(request);
    }

    private ArrayList<Movie> parseJSONOResponse(JSONObject response) {
        ArrayList<Movie> listMovies = new ArrayList<>();
        if(response == null && response.length() == 0){
            return listMovies;
        }

        try {
            if(response.has(KEY_MOVIES)){
                JSONArray arrayMovies = response.getJSONArray(KEY_MOVIES);
                for (int i = 0; i < arrayMovies.length() ; i++) {
                    JSONObject currentMovie = arrayMovies.getJSONObject(i);
                    long id = currentMovie.getLong(KEY_ID);
                    String title = currentMovie.getString(KEY_TITLE);
                    String releaseDate = currentMovie.getString(KEY_RELEASE_DATE);
                    double voteAverage = currentMovie.getDouble(KEY_VOTE);
                    String url_thumbnail = currentMovie.getString(KEY_POSTER);
                    Date curMovieDate = dateFormat.parse(releaseDate);
                    String overview = currentMovie.getString(KEY_DESCRIPTION);

                    Movie movie = new Movie(id,title, curMovieDate, voteAverage, url_thumbnail,overview);
                    Log.d(TAG, "parseJSONOResponse: " + currentMovie.getString(KEY_ID) + " " + currentMovie.getString(KEY_TITLE));
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
                .appendPath("movie").appendPath("now_playing").appendQueryParameter("api_key",getString(R.string.TMDB_api_key))
                .appendQueryParameter("language","en-US").build();
        Log.d(TAG, "getTheTrendingData: "+ builtURI.toString() );
        return builtURI.toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        nowPlaying = (RecyclerView) view.findViewById(R.id.nowPlayingMovies);
        nowPlaying.setLayoutManager(new LinearLayoutManager(getActivity()));
        homeAdapter = new HomeAdapter(getActivity());
        nowPlaying.setAdapter(homeAdapter);
        sendJSONrequest();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
