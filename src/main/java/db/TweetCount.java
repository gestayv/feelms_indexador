package db;

import tweets.TweetLoader;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Arturo on 07-05-2017.
 */
public class TweetCount {

    private int film_id;
    private LocalDate date;
    private int count;
    private int pos;
    private int neg;
    private ArrayList<CountryCount> countryCounts;

    private TweetCount() {

    }

    /*
    public TweetCount(int film_id, LocalDate date, int count, int pos, int neg) {
        this.film_id = film_id;
        this.date = date;
        this.count = count;
        this.pos = pos;
        this.neg = neg;
    }
    */

    public TweetCount(int film_id, LocalDate date, int count, int pos, int neg, ArrayList<CountryCount> countryCounts) {
        this.film_id = film_id;
        this.date = date;
        this.count = count;
        this.pos = pos;
        this.neg = neg;
        this.countryCounts = countryCounts;
    }

    public int getFilm_id() {
        return film_id;
    }

    public void setFilm_id(int film_id) {
        this.film_id = film_id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getNeg() {
        return neg;
    }

    public void setNeg(int neg) {
        this.neg = neg;
    }

    public ArrayList<CountryCount> getCountryCounts() {
        return countryCounts;
    }

    public void setCountryCounts(ArrayList<CountryCount> countryCounts) {
        this.countryCounts = countryCounts;
    }
}
