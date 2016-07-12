/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accesaureseau;

/**
 *
 * @author houcem
 */
import java.net.*;
 
public class TestProtocole
{
  public static void main (String [] args)
  {      
    // Tableau avec 4 URL à tester
    String urlTest [] = {"http://www.eteks.com/index.html",
                         "ftp://www.eteks.com/index.html",
                         "file://C//java/bin/java.exe",
                         "mailto:puybaret@imaginet.fr"};
 
    for (int i = 0; i < urlTest.length; i++)
      try
      {
        // Création d'une URL
        URL url = new URL (urlTest [i]);
        System.out.println ("URL : " + url + " correctement form\u00e9e\n"
                            + " et protocole " + url.getProtocol () + " reconnu.");
      }
      catch (MalformedURLException e)
      { 
        // URL incorrect ou protocole inconnu
        System.out.println ("URL : " + urlTest [i] + " incorrect\n"
                            + " ou protocole non reconnu\n"
                            + "(Exception " + e + ").");
      }
  } 
}