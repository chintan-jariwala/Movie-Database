package chintanjariwala.com.moviedatabase.Pojo;

import java.util.Date;

/**
 * Created by chint on 11/15/2016.
 */

public class Movie {

    long id;
    String title;
    Date release_date;
    double vote_average;
    String url_thumbnail;
    String overview;

    public Date getRelease_date() {
        return release_date;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

    public Movie(long id, String title, Date release_date, double vote_average, String url_thumbnail, String overview) {

        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.url_thumbnail = url_thumbnail;
        this.overview = overview;
    }

    public Movie() {
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getUrl_thumbnail() {
        return url_thumbnail;
    }

    public void setUrl_thumbnail(String url_thumbnail) {
        this.url_thumbnail = url_thumbnail;
    }
}
