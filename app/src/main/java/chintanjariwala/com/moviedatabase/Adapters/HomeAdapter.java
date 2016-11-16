package chintanjariwala.com.moviedatabase.Adapters;

import android.content.Context;
import android.provider.ContactsContract;
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
import java.util.PriorityQueue;

import chintanjariwala.com.moviedatabase.Network.VolleySingleton;
import chintanjariwala.com.moviedatabase.Pojo.Movie;
import chintanjariwala.com.moviedatabase.R;

/**
 * Created by chint on 11/15/2016.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolderHome>{

    private LayoutInflater layoutInflater;
    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private static final String TAG = HomeAdapter.class.getSimpleName();


    public HomeAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();
    }

    public void setMovieArrayList(ArrayList<Movie> movieArrayList) {
        this.movieArrayList = movieArrayList;
        notifyItemRangeChanged(0, movieArrayList.size());
    }

    @Override
    public ViewHolderHome onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.movie_list_item, parent, false);

        ViewHolderHome viewHolderHome = new ViewHolderHome(view);

        return viewHolderHome;
    }

    @Override
    public void onBindViewHolder(final ViewHolderHome holder, int position) {
        Movie currentMovie = movieArrayList.get(position);
        holder.movietitle.setText(currentMovie.getTitle());
        holder.movieReleaseDate.setText(currentMovie.getRelease_date().toString());
        holder.audianceScore.setRating((float) (currentMovie.getVote_average()/20.0F));
        String thumbnail = currentMovie.getUrl_thumbnail();
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

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    static class ViewHolderHome extends RecyclerView.ViewHolder{

        private ImageView movieThumbnail;
        private TextView movietitle;
        private TextView movieReleaseDate;
        private RatingBar audianceScore;

        public ViewHolderHome(View itemView) {
            super(itemView);
            movieThumbnail = (ImageView) itemView.findViewById(R.id.movieThumbnail);
            movietitle = (TextView) itemView.findViewById(R.id.movieTitle);
            movieReleaseDate = (TextView) itemView.findViewById(R.id.movieReleaseDate);
            audianceScore = (RatingBar) itemView.findViewById(R.id.movieAudienceScore);
        }
    }
}
