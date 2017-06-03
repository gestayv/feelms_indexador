package db;

import java.time.LocalDate;

/**
 * Created by Arturo on 02-06-2017.
 */
public class TweetsSentiments {

    private int film_id;
    private LocalDate date;
    private float pos;
    private float neg;

    public TweetsSentiments(int film_id, LocalDate date, float pos, float neg) {
        this.film_id = film_id;
        this.date = date;
        this.pos = pos;
        this.neg = neg;
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

    public float getPos() {
        return pos;
    }

    public void setPos(float pos) {
        this.pos = pos;
    }

    public float getNeg() {
        return neg;
    }

    public void setNeg(float neg) {
        this.neg = neg;
    }
}
