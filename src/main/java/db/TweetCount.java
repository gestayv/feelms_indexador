package db;

import tweets.TweetLoader;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created by Arturo on 07-05-2017.
 */
public class TweetCount {

    private int film_id;
    private LocalDate date;
    private int count;

    private TweetCount() {
    }

    public TweetCount(int film_id, LocalDate date, int count) {
        this.film_id = film_id;
        this.date = date;
        this.count = count;
    }

    public int getFilm_id() {
        return film_id;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getCount() {
        return count;
    }
}
