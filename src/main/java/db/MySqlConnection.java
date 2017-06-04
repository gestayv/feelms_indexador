package db;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arturo on 07-05-2017.
 */
public class MySqlConnection implements SqlConnection {

    DataSource dataSource = null;

    private MySqlConnection() {

    }

    public MySqlConnection(String user, String pass, String host, String port, String db_name) {

        boolean valid = true;

        //Intenta obtener el datasource de glassfish (experimental)
        try {
            Context context = new InitialContext();
            dataSource = (DataSource) context.lookup("java:comp/env/jdbc/myDB");
        } catch (NamingException e) {
            System.out.print(e.toString() + "\n");

            valid = false;
        }

        //Si no pudo, se conecta usando los datos entregados
        if(!valid) {
            System.out.print("\nCreando conexion manualmente\n\n");
            MysqlDataSource ds = new MysqlDataSource();
            ds.setUser(user);
            ds.setPassword(pass);
            //ds.setDatabaseName(db_name);
            //ds.setPort(Integer.parseInt(port));
            ds.setUrl("jdbc:mysql://" + host + ":" + port + "/" + db_name);

            this.dataSource = ds;
        } else {
            System.out.print("\nConectado al DataSource existente\n\n");
        }


    }

    //Probar conexion
    public boolean test() {
        Connection conn = null;
        Boolean valid = true;

        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            System.out.print(e.toString() + "\n");
            valid = false;
        } finally {
            if (conn != null) {
                try {
                    if (!conn.isClosed()) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    System.out.print(e.toString() + "\n");
                    valid = false;

                }
            }
        }

        return valid;
    }


    @Override
    public List<Film> getFilms() throws SQLException {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        List<Film> films = null;

        System.out.print("Obteniendo Films \n\n");

        try {
            conn = dataSource.getConnection();

            stm = conn.prepareStatement("SELECT f.id, f.title, sub.last_update, k.term\n" +
                    "FROM films f\n" +
                    "INNER JOIN key_terms k\n" +
                    "ON f.id = k.film_id\n" +
                    "LEFT JOIN (SELECT film_id, MAX(date) as last_update FROM tweet_counts GROUP BY film_id) sub\n" +
                    "ON sub.film_id = k.film_id\n" +
                    "ORDER BY f.id");
            rs = stm.executeQuery();

            List<Film> filmsAux = new ArrayList<Film>();
            Film lastFilm = null;

            //Empieza a iterar cada elemento de la query
            while(rs.next()) {
                int auxId = rs.getInt("f.id");
                String auxTitle = rs.getString("f.title");

                if(lastFilm == null || lastFilm.getId() != auxId) {
                    LocalDate last_update = null;
                    String dateString = rs.getString("sub.last_update");
                    if(dateString != null) {
                        last_update = LocalDate.parse(dateString);
                    }

                    lastFilm = new Film(auxId, new ArrayList<String>(), last_update, auxTitle);
                    filmsAux.add(lastFilm);
                }

                lastFilm.addKeyterm(rs.getString("k.term"));
            }

            rs.close();
            stm.close();

            //Retorna null si no habian peliculas con keywords para utilizarse
            if(filmsAux.isEmpty()) return null;

            films = filmsAux;

            /*
            //Solo para pruebas
            for(Film film: films) {
                System.out.print("Id film: " + film.getId() + "\n");
                if(film.getLastUpdate() == null) {
                    System.out.print("No Last Update\n");
                } else {
                    System.out.print("Last Update: " + film.getLastUpdate().toString() + "\n");
                }
                for(String term: film.getKeyterms()) {
                    System.out.print(term + "\n");
                }
            }

            */



        } catch (SQLException e) {
            throw e;
        } finally {
            if(rs != null && !rs.isClosed()) rs.close();

            if(stm != null && !stm.isClosed()) stm.close();

            if(conn != null && !conn.isClosed()) conn.close();
        }

        return films;
    }

    @Override
    public int writeData(List<TweetCount> data) throws SQLException {

        //La query debe ser algo como
        // INSERT INTO tweet_counts (date, count, film_id)
        // VALUES ('YYYY-MM-DD', 100, 1), ('YYYY-MM-DD', 200, 1), (otra fila), (otra fila)
        // Así para meter todos los valores de una

        if(!data.isEmpty()) {
            Connection conn = null;
            PreparedStatement stm = null;
            ResultSet rs = null;

            try {

                conn = dataSource.getConnection();

                String queryBase = "INSERT INTO tweet_counts (date, count, film_id) VALUES";
                StringBuilder queryValues = new StringBuilder();
                queryValues.ensureCapacity(1200);

                int updatedRows = 0;

                for(TweetCount tw: data) {

                    if(queryValues.length() == 0) {
                        queryValues.append(" (\'").append(tw.getDate().toString()).append("\', ").append(tw.getCount()).append(", ").append(tw.getFilm_id()).append(")");
                    } else if (queryValues.length() < 1000) {
                        queryValues.append(", (\'").append(tw.getDate().toString()).append("\', ").append(tw.getCount()).append(", ").append(tw.getFilm_id()).append(")");
                    } else {
                        //Manda a la BD los datos que tiene por ahora
                        stm = conn.prepareStatement(queryBase + queryValues.toString());
                        updatedRows += stm.executeUpdate();

                        if(rs != null && !rs.isClosed()) rs.close();
                        if(stm != null && !stm.isClosed()) stm.close();

                        //Vacia los valores de la query
                        queryValues = new StringBuilder();
                    }
                }

                //Ingresa a la BD lo que falta
                if(queryValues.length() > 0) {
                    stm = conn.prepareStatement(queryBase + queryValues.toString());
                    updatedRows += stm.executeUpdate();
                }

                return updatedRows;

            } catch (SQLException e) {
                throw e;
            } finally {
                if(rs != null && !rs.isClosed()) rs.close();

                if(stm != null && !stm.isClosed()) stm.close();

                if(conn != null && !conn.isClosed()) conn.close();
            }




        }

        return 0;

    }

    @Override
    public int writeSentiment(List<TweetsSentiments> data) throws SQLException {
        if(!data.isEmpty()) {
            Connection conn = null;
            PreparedStatement stm = null;
            ResultSet rs = null;

            try {

                conn = dataSource.getConnection();

                String queryBase = "INSERT INTO tweet_sentiments (date, pos, neg, film_id) VALUES";
                StringBuilder queryValues = new StringBuilder();
                queryValues.ensureCapacity(1500);

                int updatedRows = 0;

                for(TweetsSentiments tw: data) {

                    if(queryValues.length() == 0) {
                        queryValues.append(" (\'").append(tw.getDate().toString()).append("\', ").append(Double.toString(tw.getPos())).append(", ").append(Double.toString(tw.getNeg())).append(", ").append(tw.getFilm_id()).append(")");

                    } else if (queryValues.length() < 1000) {
                        queryValues.append(", (\'").append(tw.getDate().toString()).append("\', ").append(Double.toString(tw.getPos())).append(", ").append(Double.toString(tw.getNeg())).append(", ").append(tw.getFilm_id()).append(")");

                    } else {
                        //Manda a la BD los datos que tiene por ahora
                        stm = conn.prepareStatement(queryBase + queryValues.toString());
                        updatedRows += stm.executeUpdate();

                        if(rs != null && !rs.isClosed()) rs.close();
                        if(stm != null && !stm.isClosed()) stm.close();

                        //Vacia los valores de la query
                        queryValues = new StringBuilder();
                    }
                }

                //Ingresa a la BD lo que falta
                if(queryValues.length() > 0) {
                    stm = conn.prepareStatement(queryBase + queryValues.toString());
                    updatedRows += stm.executeUpdate();
                }

                return updatedRows;

            } catch (SQLException e) {
                throw e;
            } finally {
                if(rs != null && !rs.isClosed()) rs.close();

                if(stm != null && !stm.isClosed()) stm.close();

                if(conn != null && !conn.isClosed()) conn.close();
            }

        }


        return 0;
    }


}
