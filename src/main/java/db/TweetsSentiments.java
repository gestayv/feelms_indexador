package db;

import java.time.LocalDate;

/**
 * Created by Arturo on 02-06-2017.
 */
public class TweetsSentiments {

    private int film_id;
    private LocalDate date;
    private double pos;
    private double neg;

    public TweetsSentiments(int film_id, LocalDate date, double pos, double neg) {
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

    public double getPos() {
        return pos;
    }

    public void setPos(double pos) {
        this.pos = pos;
    }

    public double getNeg() {
        return neg;
    }

    public void setNeg(double neg) {
        this.neg = neg;
    }
}
