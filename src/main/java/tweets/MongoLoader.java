/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweets;

import db.DocumentList;
import db.MongodbConnection;
import db.Tweet;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *
 * @author ichigo
 */
public class MongoLoader implements TweetLoader{
    
    
    
    public boolean connectionStatus()
    {
        MongodbConnection mc = new MongodbConnection();
        if(mc.mConnection() == null) return false;
        return true;
    }
    
    //  Obtiene todos los tweets como un listado de tweets creados previamente
    //  los que se añaden a un listado de documentos que serán indexados.
    @Override   
    public DocumentList getTweets() {
            
        //  Se obtiene el listado de tweets.
        MongodbConnection mdc = new MongodbConnection();
        
        List<Tweet> lt = mdc.getTweets(mdc.mConnection());
        
        //  Se crea el listado de documentos
        List<Document> docs = new ArrayList<Document>();
        
        //  Se recorre el listado de tweets y se agregan a un documento
        for (Tweet tw: lt) {
            Document tweet = new Document();
            
            tweet.add(new StoredField("doc_id", tw.getId_doc()));
            tweet.add(new StoredField("tweet_id", tw.getId_tweet()));
            tweet.add(new TextField("user", tw.getUser(), Store.YES));
            tweet.add(new StoredField("name", tw.getName()));
            tweet.add(new TextField("text", tw.getText(), Store.NO));
            tweet.add(new StoredField("rt_count", tw.getRt_count()));
            tweet.add(new TextField("fecha", LocalDate.parse(tw.getFecha()).format(DateTimeFormatter.BASIC_ISO_DATE), Store.YES));
            tweet.add(new TextField("hashtags", tw.getHashtag(), Store.NO));
            tweet.add(new TextField("country_code", tw.getPais(), Store.NO));
            
            //  Se agrega el documento a un listado
            docs.add(tweet);
        }
        
        //return docs;
        return new DocumentList(lt, docs);
    }
    
    @Override
    public List<Document> getTweets(Date date) {
        return null;
    }

    @Override
    public List<Document> getTweets(Date start, Date end) {
        return null;
    }
}
