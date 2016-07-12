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
import java.awt.*;
import java.net.*;
import java.io.*;
 
public class EchoClient extends Applet
{
  String       texteLu = "";
  InputStream  fluxEntree;
  OutputStream fluxSortie;
  
  // Méthode appelée par le système à l'initialisation de l'applet
  public void init ()
  {
    try 
    {
      // Récupération de l'adresse de l'hôte local
      InetAddress adresse = InetAddress.getLocalHost ();
      // Ouverture d'une connexion sur le port 13267 de cet hôte
      Socket      socket  = new Socket (adresse, 13267);
      
      // Récupération des flux d'entrés et de sortie
      fluxEntree = socket.getInputStream ();
      fluxSortie = socket.getOutputStream ();
    }
    catch (IOException e)
    {
      texteLu = "Probleme de connexion";
    }
  }
 
  // Méthode appelée par le système pour mettre à jour le dessin de l'applet
  public void paint (Graphics gc)
  {
    gc.drawString (texteLu, 10, 20);
  }
  
  // Méthode appelée par le système quand une touche du clavier est enfoncée
  public boolean keyDown (Event evt, int key)
  {
    if (fluxSortie != null)
      try
      {
        // Envoie au serveur du caractère lu puis relecture
        fluxSortie.write(key);
        char caractereLu = (char)fluxEntree.read ();
        
        // Ajout du caractère et redessin de l'applet
        texteLu += caractereLu;          
        repaint();
      }
      catch (IOException e)
      { }
    return true;
  }
}
