/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.Arrays;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ichigo
 */
public class MongodbConnection {
    
    public MongoCollection<Document> mConnection()
    {
        //  Usuario
        String user = "admin";
        //  BD donde está el usuario
        String database = "admin";   
        //  Pass como arreglo de caracteres, ej: 
        //  si la contraseña es pass, se escribe como {'p', 'a', 's', 's'}
        char[] password = {'a', 'd', 'm', 'i', 'n', '1', '2', '3'};

        MongoCredential mcr = MongoCredential.createCredential(user, database, password);

        MongoClient mcl = new MongoClient(new ServerAddress("localhost", 27017),
                                                    Arrays.asList(mcr));

        MongoDatabase db = mcl.getDatabase("feelms");

        MongoCollection<Document> mColl = db.getCollection("tweets");
        
        return mColl;
    }
    
    public List<Tweet> getTweets()
    {
        MongoCollection<Document> coll = mConnection();
        List<Tweet> tweets = new ArrayList<Tweet>();
        MongoCursor<Document> cr = coll.find().iterator();
        while(cr.hasNext())
        {
           Document documento = cr.next();  
           String id_doc = documento.get("_id").toString();
           String id_tweet = documento.get("id").toString();
           String user = documento.get("user").toString();
           String name = documento.get("name").toString();
           String texto = documento.get("text").toString();
           String rt_count = documento.get("rt_count").toString();
           String fecha = documento.get("fecha").toString();
           String ht = documento.get("hashtag").toString();
           Tweet tw = new Tweet(id_doc, id_tweet, user, name, texto, rt_count, fecha, ht);
           
           tweets.add(tw);
        }
        return tweets;
    }
}