package db;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Arturo on 07-05-2017.
 */
public class Film {

    private int id;
    private List<String> keyterms;

    private LocalDate lastUpdate;

    private Film() {

    }

    public Film(int id, List<String> kw, LocalDate lastUpdate) {

        this.id = id;
        this.keyterms = kw;
        this.lastUpdate = lastUpdate;
    }

    public int getId() {
        return id;
    }

    public List<String> getKeyterms() {
        return keyterms;
    }

    public void addKeyterm(String term) { this.keyterms.add(term); }

    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
