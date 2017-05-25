package db;

/**
 * Created by Arturo on 24-05-2017.
 */
public class User {

    private String name;

    private int count = 0;

    public User(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
