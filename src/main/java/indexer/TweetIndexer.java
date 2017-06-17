package indexer;

import db.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.QueryBuilder;
import tweets.TweetLoader;

import org.apache.lucene.document.Document;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Arturo on 07-05-2017.
 */
public class TweetIndexer {

    private TweetLoader tweetLoader;
    private SqlConnection sqlConn;
    private Neo4jConnection neo4jConnection;
    private SentimentAnalyzer sentimentAnalyzer;
    //private ArrayList<String> countryCodes;

    private TweetIndexer() {

    }

    public TweetIndexer(TweetLoader tw, SqlConnection s, Neo4jConnection neo4jConnection, SentimentAnalyzer sentimentAnalyzer) {
        this.tweetLoader = tw;
        this.sqlConn = s;
        this.neo4jConnection = neo4jConnection;
        this.sentimentAnalyzer = sentimentAnalyzer;

    }

    public void run() {

        //List<Document> docs = tweetLoader.getTweets();
        DocumentList docList = tweetLoader.getTweets();
        List<Document> docs = docList.getDocuments();


        List<Tweet> tweetList = docList.getTweets();
        Double tweetMapCap = (tweetList.size() / 0.75) + 1;
        Map<String, String> tweetMap = new HashMap<String, String>(tweetMapCap.intValue());
        for (Tweet t : tweetList) {
            tweetMap.put(t.getId_doc(), t.getText());
        }

        System.out.print("\n\nTWEETS LEIDOS: " + docs.size() + "\n\n");

        Directory dir = new RAMDirectory();
        //Analyzer analyzer = new StandardAnalyzer();
        //Mientras, incluir stopwords
        Analyzer analyzer = new StandardAnalyzer(CharArraySet.EMPTY_SET);
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

        //Para cerrarlo en caso de exception
        IndexWriter indexWriter = null;
        IndexReader indexReader = null;



        try {


            //Obtener codigos de pais
            //countryCodes = sqlConn.getCountryCodes();

            //Tamaño para hashmaps adelante
            //Double auxCountryHashSize = countryCodes.size() / 0.75;
            //int countryHashSize = auxCountryHashSize.intValue() + 1;
            int countryHashSize = 100;

            indexWriter = new IndexWriter(dir, iwc);

            //Add documents
            indexWriter.addDocuments(docs);

            List<Film> films = sqlConn.getFilms();

            indexReader = DirectoryReader.open(indexWriter);

            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            //QueryBuilder queryBuilder = new QueryBuilder(analyzer);

            List<TweetCount> tweetCounts = new ArrayList<TweetCount>();

            //List<TweetsSentiments> tweetsSentiments = new ArrayList<TweetsSentiments>();

            //Por cada film, empieza a buscar tweets
            for (Film film: films) {

                //Si la ultima actualizacion fue ayer o antes, busca.
                if(film.getLastUpdate() == null || film.getLastUpdate().compareTo(LocalDate.now()) < 0) {

                    List<String> keyterms = film.getKeyterms();
                    BooleanQuery.Builder builder = new BooleanQuery.Builder();

                    for (String keyterm: keyterms) {
                        String[] arr = keyterm.toLowerCase().trim().split(" ");
                        if(arr.length == 1) {
                            BooleanClause bcText = new BooleanClause(new TermQuery(new Term("text", arr[0])), BooleanClause.Occur.SHOULD);
                            BooleanClause bcHash = new BooleanClause(new TermQuery(new Term("hashtags", arr[0])), BooleanClause.Occur.SHOULD);
                            builder.add(bcText);
                            builder.add(bcHash);
                        } else {

                            //Para terminos de busqueda de varias palabras, busca tanto con le frase con espacios como sin espacios
                            PhraseQuery.Builder pqBuilderText = new PhraseQuery.Builder();
                            PhraseQuery.Builder pqBuilderHash = new PhraseQuery.Builder();

                            for(String t: arr) {
                                pqBuilderText.add(new Term("text", t));
                                pqBuilderHash.add(new Term("hashtags", t));
                            }

                            BooleanClause bcText = new BooleanClause(pqBuilderText.build(), BooleanClause.Occur.SHOULD);
                            BooleanClause bcHash = new BooleanClause(pqBuilderHash.build(), BooleanClause.Occur.SHOULD);

                            String joinedString = String.join("", arr);
                            BooleanClause bcTextJoined = new BooleanClause(new TermQuery(new Term("text", joinedString)), BooleanClause.Occur.SHOULD);
                            BooleanClause bcHashJoined = new BooleanClause(new TermQuery(new Term("hashtags", joinedString)), BooleanClause.Occur.SHOULD);

                            builder.add(bcText);
                            builder.add(bcHash);
                            builder.add(bcTextJoined);
                            builder.add(bcHashJoined);
                        }
                    }


                    //Query Base
                    BooleanQuery query = builder.build();

                    //Empieza a calcular los tweets por dia
                    LocalDate beginPoint = null;
                    if(film.getLastUpdate() == null) {
                        beginPoint = LocalDate.now().minusDays(30);
                    } else {
                        beginPoint = film.getLastUpdate().plusDays(1);
                    }
                    LocalDate end = LocalDate.now().minusDays(1);

                    System.out.print("\nMovie Id: " + film.getId() + "\n");
                    System.out.print("Keyterms: " + film.getKeyterms() + "\n");
                    System.out.print("Begin: " + beginPoint.toString() + "\n");
                    System.out.print("End: " + end.toString() + "\n\n");


                    //Editado para guardar fechas en su lugar, como int
                    //Para guardar usuarios y su cantidad de tweets a esta pelicula
                    ArrayList<String> usuarios = new ArrayList<String>();
                    //ArrayList<Integer> conteos = new ArrayList<Integer>();
                    ArrayList<Integer> fechas = new ArrayList<Integer>();

                    while(beginPoint.compareTo(end) <= 0) {

                        //Crea una super boolean query
                        BooleanQuery.Builder auxQB = new BooleanQuery.Builder();
                        auxQB.add(query, BooleanClause.Occur.MUST);

                        //Agrega la clausula con el dia, transforma el formato YYYY-MM-DD a yyyyMMdd que acepta el indexador
                        String strDate = beginPoint.format(DateTimeFormatter.BASIC_ISO_DATE);

                        BooleanClause dateClause = new BooleanClause(new TermQuery(new Term("fecha", strDate)), BooleanClause.Occur.MUST);
                        auxQB.add(dateClause);

                        //Realizar query
                        BooleanQuery secondQuery = auxQB.build();
                        int count = indexSearcher.count(secondQuery);

                        if(count > 0) {
                            //tweetCounts.add(new TweetCount(film.getId(), beginPoint, count));

                            //Fecha a int
                            int intDate = Integer.parseInt(String.join("", strDate.split("-")));

                            ArrayList<String> ready = new ArrayList<String>();

                            int tweetPos = 0;
                            int tweetNeg = 0;
                            //int tweetNeut = 0;

                            Map<String, Integer> countryCountsMap = new HashMap<String, Integer>(countryHashSize);

                            //Para ver tweets de usuarios en particular, conteos por pais y analisis de sentimientos de tweets
                            TopDocs topDocs = indexSearcher.search(secondQuery, count);
                            for (ScoreDoc scoreDoc: topDocs.scoreDocs) {
                                Document doc = indexSearcher.doc(scoreDoc.doc);

                                //Parte para analisis de sentimientos de los Tweets
                                //int doc_id = Integer.valueOf(doc.get("doc_id"));
                                String doc_id = doc.get("doc_id");
                                String tweetText = tweetMap.get(doc_id);

                                int sentimentValue = sentimentAnalyzer.analyzeTweet(tweetText);
                                if(sentimentValue > 0) {
                                    tweetPos++;
                                } else if (sentimentValue < 0) {
                                    tweetNeg++;
                                }

                                //Parte conteo de tweets por usuario para grafos
                                String user = doc.get("user");

                                if(!ready.contains(user)) {
                                    /*

                                    BooleanQuery.Builder auxQB_user = new BooleanQuery.Builder();
                                    auxQB_user.add(secondQuery, BooleanClause.Occur.MUST);
                                    BooleanClause userClause = new BooleanClause(new TermQuery(new Term("user", user.toLowerCase())), BooleanClause.Occur.MUST);
                                    auxQB_user.add(userClause);
                                    int userCount = indexSearcher.count(auxQB_user.build());
                                    */
                                    int index = usuarios.indexOf(user);
                                    if(index == -1) {
                                        usuarios.add(user);
                                        fechas.add(intDate);
                                        //conteos.add(userCount);
                                    } else {
                                        fechas.set(index, intDate);
                                        //conteos.set(index, conteos.get(index) + userCount);
                                    }

                                    ready.add(user);
                                }

                                //Conteo por paises
                                String doc_country = doc.get("country_code");
                                if(!doc_country.equals("none")) {
                                    if(countryCountsMap.containsKey(doc_country)) {
                                        countryCountsMap.put(doc_country, countryCountsMap.get(doc_country) + 1);
                                    } else {
                                        countryCountsMap.put(doc_country, 1);
                                    }
                                }
                            }

                            //Crea los conteos de paises para ese dia
                            ArrayList<CountryCount> countryCounts = new ArrayList<CountryCount>();
                            for (Map.Entry<String, Integer> entry : countryCountsMap.entrySet()) {
                                countryCounts.add(new CountryCount(entry.getKey(), entry.getValue()));
                            }


                            //Agrega los porcentajes de tweets segun analisis de sentimientos y conteos

                            tweetCounts.add(new TweetCount(film.getId(), beginPoint, count, tweetPos, tweetNeg, countryCounts));

                            //tweetsSentiments.add(new TweetsSentiments(film.getId(), beginPoint, tweetPos / count, tweetNeg / count));


                        }

                        //Avanzar un dia
                        beginPoint = beginPoint.plusDays(1);

                    }


                    List<User> filmUsers = new ArrayList<User>();
                    int len = usuarios.size();
                    for(int i = 0; i < len; i++) {
                        //filmUsers.add(new User(usuarios.get(i), conteos.get(i)));
                        filmUsers.add(new User(usuarios.get(i), fechas.get(i)));
                    }

                    neo4jConnection.buildFilmUserGraph(filmUsers, film);


                }

            }

            //Escribir Conteo
            if(!tweetCounts.isEmpty()) {
                System.out.print("\nEscribiendo conteos\n");
                int filasAgregadas = sqlConn.writeData(tweetCounts);
                System.out.print("Filas Agregadas: " + filasAgregadas + "\n");
            } else {
                System.out.print("\nSin conteos que escribir\n");
            }

            /*
            if(!tweetsSentiments.isEmpty()) {
                System.out.print("\nEscribiendo porcentajes de pos/neg\n");
                int filasSent = sqlConn.writeSentiment(tweetsSentiments);
                System.out.print("Filas Agregadas: " + filasSent + "\n");
            } else {
                System.out.print("\nSin porcentajes que escribir\n");
            }
            */

        } catch (IOException | SQLException e) {
            System.out.print(e);
        } finally {
            if(indexWriter != null && indexWriter.isOpen()) {
                try {
                    indexWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(indexReader != null) try {
                indexReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
