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
import java.applet.Applet;
import java.awt.Graphics;
import java.net.*;
import java.io.*;
 
public class HelloFromNet extends Applet
{
  String texteLu;
  
  // Méthode appelée par le système à l'initialisation de l'applet
  public void init ()
  {
    try
    {
      // Création de l'URL http://www.eteks.com/classes/hello.txt
      URL urlFichierHello = new URL ("http",
                                     "www.eteks.com", 
                                     "/classes/hello.txt");
      // Ouverture d'une connexion et récupération d'un flux d'entrée                                     
      URLConnection connexion   = urlFichierHello.openConnection ();
      InputStream   fluxFichier = connexion.getInputStream ();
 
      // Lecture du contenu du flux d'entrée
      byte contenuFichier [ ] = new byte [connexion.getContentLength ()];
      int  octetsLus = fluxFichier.read (contenuFichier);
        
      texteLu = new String (contenuFichier, 0, 0, octetsLus);
 
      // Fermeture de la connexion
      fluxFichier.close ();
    }
    catch (Exception e)
    { 
      texteLu = "Probleme...";
    }
  }
 
  // Méthode appelée par le système pour mettre à jour le dessin de l'applet
  public void paint (Graphics gc)
  {
    if (texteLu != null)
      // Affichage du texte lu
      gc.drawString(texteLu, 10, 20);
  }
}
