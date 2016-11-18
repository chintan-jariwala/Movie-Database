package chintanjariwala.com.moviedatabase.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import chintanjariwala.com.moviedatabase.R;
import chintanjariwala.com.moviedatabase.activity.MovieDetails;
import chintanjariwala.com.moviedatabase.network.VolleySingleton;
import chintanjariwala.com.moviedatabase.pojo.Movie;

/**
 * Created by chint on 11/15/2016.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolderTrending>{

    private LayoutInflater layoutInflater;
    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private static final String TAG = MovieListAdapter.class.getSimpleName();
    public Context context;

    public MovieListAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();
    }

    public void setMovieArrayList(ArrayList<Movie> movieArrayList) {
        this.movieArrayList = movieArrayList;
        notifyItemRangeChanged(0, movieArrayList.size());
    }

    @Override
    public ViewHolderTrending onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.movie_list_item, parent, false);
        ViewHolderTrending viewHolderTrending = new ViewHolderTrending(view);

        return viewHolderTrending;

    }

    @Override
    public void onBindViewHolder(final ViewHolderTrending holder, int position) {
        Movie currentMovie = movieArrayList.get(position);
        holder.setItem(currentMovie);
        holder.movietitle.setText(currentMovie.getTitle());
        holder.movieReleaseDate.setText(currentMovie.getRelease_date().toString());
        holder.audianceScore.setRating((float) (currentMovie.getVote_average()/2.0F));
        String thumbnail = currentMovie.getUrl_thumbnail();
        if(!thumbnail.equals("null")){
            thumbnail = VolleySingleton.base_image_url + thumbnail;
            Log.d(TAG, "onBindViewHolder: "+ thumbnail );
            if(thumbnail != null){
                imageLoader.get(thumbnail, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        holder.movieThumbnail.setImageBitmap(response.getBitmap());
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    public class ViewHolderTrending extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Movie movie;
        private ImageView movieThumbnail;
        private TextView movietitle;
        private TextView movieReleaseDate;
        private RatingBar audianceScore;

        public ViewHolderTrending(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            movieThumbnail = (ImageView) itemView.findViewById(R.id.movieThumbnail);
            movietitle = (TextView) itemView.findViewById(R.id.movieTitle);
            movieReleaseDate = (TextView) itemView.findViewById(R.id.movieReleaseDate);
            audianceScore = (RatingBar) itemView.findViewById(R.id.movieAudienceScore);
        }

        public void setItem(Movie movie){
            this.movie = movie;
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: " + getPosition() + " " + movie.getId());
            Intent i = new Intent(context, MovieDetails.class);
            i.putExtra("movieDetail", movie.getId() + "");
            context.startActivity(i);
        }
    }

}
