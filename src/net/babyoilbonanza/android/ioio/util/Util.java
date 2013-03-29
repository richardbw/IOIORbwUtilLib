/**
 * 
 */
package net.babyoilbonanza.android.ioio.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author rbw
 *
 */
public class Util
{

   
    
    public static String readFile(URI url) throws IOException
    {
        String inputLine;
        
        StringBuilder fileCnts = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(url.getPath()));
        while ((inputLine = br.readLine()) != null) fileCnts.append(inputLine);
        br.close();
        return fileCnts.toString();
    }
    
    

    public static String readAsset(URI url, Context context) throws IOException
    {
        String inputLine;

        StringBuilder fileCnts = new StringBuilder();
        BufferedReader br = new BufferedReader( 
            new InputStreamReader(
                context.getAssets().open(
                        //strip off leading '/':
                    url.getSchemeSpecificPart().substring(url.getSchemeSpecificPart().indexOf('/')+1)
                )
            ) 
        );
        while ( (inputLine = br.readLine()) != null ) fileCnts.append(inputLine);
        br.close();
        return fileCnts.toString();
    }
    
    

    public static String sendRequest(HttpUriRequest request) throws IllegalStateException, IOException
    {
        StringBuilder responseBody = new StringBuilder();
        HttpClient httpclient = new DefaultHttpClient();
        //httpclient.getConnectionManager().getSchemeRegistry().register(setupSslScheme());
        
        HttpResponse response = null;
        response = httpclient.execute(request);
        HttpEntity resEntity = response.getEntity();
        
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        
        
        if (resEntity != null) {
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(resEntity.getContent()));
            
            String line;
            while ((line = reader.readLine()) != null) { responseBody.append(line); }
            
            resEntity.consumeContent();
        }
        httpclient.getConnectionManager().shutdown();
        
        return responseBody.toString();
    }
    
    

    public static Bitmap getViewImage(String urn, Context context) throws IOException
    {
        InputStream is = null;

        if ( urn.startsWith("asset") )
        {
            String file = urn.substring(urn.indexOf('/') + 1);
            is = context.getAssets().open(file);
        }
        else if ( urn.startsWith("/") )
        {
            urn = "file://"+urn;
            URLConnection conn = new URL(urn).openConnection();
            conn.connect();
            is = conn.getInputStream();
        }
        else
        {
            URLConnection conn = new URL(urn).openConnection();
            conn.connect();
            is = conn.getInputStream();
        }
        
        Bitmap b = BitmapFactory.decodeStream(is);
        try { is.close(); } catch (IOException e){}
        
        return b;
    }
    
    

}
