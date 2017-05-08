package indexer;

import db.Film;
import db.SqlConnection;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;

/**
 * Created by Arturo on 07-05-2017.
 */
public class TweetIndexer {

    private TweetLoader tweetLoader;
    private SqlConnection sqlConn;

    private TweetIndexer() {

    }

    public TweetIndexer(TweetLoader tw, SqlConnection s) {
        this.tweetLoader = tw;
        this.sqlConn = s;
    }

    public void run() {

        List<Document> docs = tweetLoader.getTweets();

        Directory dir = new RAMDirectory();
        //Analyzer analyzer = new StandardAnalyzer();
        //Mientras, incluir stopwords
        Analyzer analyzer = new StandardAnalyzer(CharArraySet.EMPTY_SET);
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        try (IndexWriter indexWriter = new IndexWriter(dir, iwc)) {

            //Add documents
            indexWriter.addDocuments(docs);

            List<Film> films = sqlConn.getFilms();

            IndexReader indexReader = DirectoryReader.open(indexWriter);

            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            //QueryBuilder builder = new QueryBuilder(analyzer);

            //Por cada film, empieza a buscar tweets
            for (Film film: films) {

                //Si la ultima actualizacion fue ayer o antes, busca.
                if(film.getLastUpdate() == null || film.getLastUpdate().compareTo(LocalDate.now()) < 0) {
                    List<String> keyterms = film.getKeyterms();
                    BooleanQuery.Builder builder = new BooleanQuery.Builder();

                    for (String keyterm: keyterms) {
                        String[] arr = keyterm.trim().split(" ");
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
                        beginPoint = LocalDate.now().minusDays(10);
                    } else {
                        beginPoint = film.getLastUpdate();
                    }
                    LocalDate end = LocalDate.now().minusDays(1);;

                    System.out.print("Begin: " + beginPoint.toString() + "\n");
                    System.out.print("End: " + end.toString() + "\n");

                    while(beginPoint.compareTo(end) <= 0) {
                        //Copiar la query y agregarle fecha
                        BooleanQuery.Builder queryCopy = new BooleanQuery.Builder();
                        List<BooleanClause> clauses = query.clauses(); //Copiar clausulas para agregar la de fecha
                        for(BooleanClause clause: clauses) {
                            queryCopy.add(clause);
                        }
                        //Agrega la clausula con el dia, transforma el formato YYYY-MM-DD a yyyyMMdd que acepta el indexador
                        BooleanClause dateClause = new BooleanClause(new TermQuery(new Term("fecha", beginPoint.format(DateTimeFormatter.BASIC_ISO_DATE))), BooleanClause.Occur.MUST);
                        queryCopy.add(dateClause);

                        //Realizar query
                        int count = indexSearcher.count(queryCopy.build());

                        //Test
                        System.out.print(beginPoint.toString() + " " + count + "\n");

                        //Avanzar un dia
                        beginPoint = beginPoint.plusDays(1);

                    }

                    /* Codigo basura, borrar cuando no lo necesite
                    int count = indexSearcher.count(query); //Obtiene conteo de hits

                    //SOLO PRUEBAS
                    System.out.print("For id: " + film.getId() + " Count: " + count + "\n");

                    TopDocs top = indexSearcher.search(query, 10);
                    ScoreDoc[] sd = top.scoreDocs;

                    for(int i = 0; i < sd.length; i++) {
                        Document d = indexSearcher.doc(sd[i].doc);
                        System.out.print("Id: " + d.get("doc_id") + " \n");
                    }

                    //Fin pruebas
                    */
                }

            }

            indexReader.close();
            indexWriter.close();


        } catch (IOException e) {
            System.out.print(e);
        }

    }

}
