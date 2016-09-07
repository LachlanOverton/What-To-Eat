package com.boken_edge.lachlan.whattoeat;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.w3c.dom.Document;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Lachlan on 14/06/2016.
 */
public class RetrieveDoc extends AsyncTask<String , Integer, Document> {

//    private boolean isRunning;
//    private Context context;

//    public RetrieveDoc(String url) {}

    @Override
    protected Document doInBackground(String... urls) {

        Document rdoc = null;
        try {
            String url = urls[0].toString();
            URL e = new URL(url);
            HttpsURLConnection urlConnection = (HttpsURLConnection) e.openConnection();
            try {
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.connect();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                rdoc = db.parse(urlConnection.getInputStream());
                return rdoc;
            }catch (Exception var8) {
                var8.printStackTrace();
            }

        } catch (Exception var8) {
            var8.printStackTrace();
        }

            return rdoc;
    }

//
//    @Override
//    protected void onProgressUpdate(Integer... progress)
//    {
////        setProgressPercent(progress[0]);
//    }
//
//    @Override
//    protected void onPostExecute(Document doc) {
//
//    }


}
