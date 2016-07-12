/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accesaureseau;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author houcem
 */
public class AccesAuReseau {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            URL url = new URL("http://google.com");
            System.out.println("printing URL Properties");
            System.out.println("printing URL Protocole : " + url.getProtocol());
            System.out.println("printing URL Host : " + url.getHost());
            System.out.println("printing URL Port : " + url.getPort());
            System.out.println("printing URL File : " + url.getFile());
            System.out.println("prnting URL refrence : " + url.getRef());
            System.out.println("just testing the samefile method : " + url.sameFile(url));
            System.out.println("printing the external form : " + url.toExternalForm());
            InputStream inputstream = url.openStream();
            System.out.println("trying input : " + inputstream.available());
            Object content = url.getContent();
            System.out.println("printing the class of the content we get" + content.getClass());
            URLConnection connection = url.openConnection();
            System.out.println("getting urlconnection url : " + connection.getURL());
            inputstream = connection.getInputStream();
            System.out.println("printing the input stream quantity of the connection : " + inputstream.available());
            System.out.println("getting content length : " + connection.getContentLength());
            System.out.println("getting content type : " + connection.getContentType());
            System.out.println("getting content encoding : " + connection.getContentEncoding());
            System.out.println("getting content : " + connection.getContent().getClass());
            
            
            System.out.println("printing connection date : " + connection.getDate());
            System.out.println("printing connection last modification time : " + connection.getLastModified());
            System.out.println("printing connection expiration date : " + connection.getExpiration());
            System.out.println("printing connection header : " + connection.getHeaderField(""));
            
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(AccesAuReseau.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AccesAuReseau.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("tests over for some URL methods");
        //socket and datagram are tested seperatly
        //will get more in depth with them in future courses
    }
    
}
