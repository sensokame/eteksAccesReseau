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
import java.io.*;
import java.util.*;
 
public class PaperBoardServer implements Runnable
{
  public static int     port = 26197;
  public static String  requeteFin   = "FIN";
  public static String  requeteListe = "LISTE";
  public static String  requeteAjout = "AJOUT";
  public static String  requeteConnexion = "CONNEXION";
 
  private static ServerSocket serveur;
  
  public static void main (String args [])
  {
    if (lancerServeur () == null)
      System.exit (-1);
  }
  
  // lancerServeur () peut être appelée par une servlet
  // pour éviter de lancer une Machine Virtuelle supplémentaire
  // sur le serveur où est hébergé le site
  public static ServerSocket lancerServeur ()
  {
    if (serveur == null)
      try
      {
        // Création d'un serveur sur le port 26197
        serveur = new ServerSocket (port); 
        // Lancement d'un thread d'attente de connexion
        new Thread (new PaperBoardServer ()).start ();
      }
      catch (IOException e)
      {
        System.out.println (e);
        return null;
      }
      
    return serveur;
  }
 
  public void run ()
  {    
    try
    {
      // Boucle sans fin d'attente de connexion
      while (true)
      {
        // Attente de la connexion d'un client
        Socket socket = serveur.accept ();
        // Démarrage d'un nouveau thread pour gérer la connexion
        new ThreadSocket (socket).start ();
      }  
    }
    catch (IOException e)
    {
      System.out.println (e);
    }  
    finally
    {
      try
      {
        // Fermeture du serveur
        serveur.close ();
        serveur = null;
      }
      catch (IOException e)
      {
        System.out.println (e);
      }  
    }
  }
}
 
class ThreadSocket extends Thread
{
  static private Vector listePolylines = new Vector (100);
  static private int    connexions;
  private        Socket socket;
 
  public ThreadSocket (Socket socket)
  {
    this.socket = socket;
  }
 
  public void run ()
  {
    try
    {
      // Récupération des flux d'entrés et de sortie
      DataInputStream fluxEntree = new DataInputStream (
                        new BufferedInputStream (socket.getInputStream ()));
      PrintStream fluxSortie = new PrintStream (
                        new BufferedOutputStream (socket.getOutputStream ()), true);
   
      // Augmentation du nombre de connexions au serveur
      connexions++;
      
      String chaineLue;
      // Lecture sur le flux d'entrée tant que la requête FIN n'est pas arrivée
      while (   (chaineLue = fluxEntree.readLine ()) != null
             && !chaineLue.equals (PaperBoardServer.requeteFin))
      {
        if (chaineLue.startsWith (PaperBoardServer.requeteListe))
        {
          // Si la liste des polylines est demandée, elles sont
          // renvoyées séparées par des tabulations
          for (Enumeration e = listePolylines.elements (); 
               e.hasMoreElements (); )
            fluxSortie.print (e.nextElement ().toString () + '\t');
          fluxSortie.write ('\n');
        }
        else if (chaineLue.equals (PaperBoardServer.requeteConnexion))
        {
          // Renvoi du nombre de connexions au serveur
          fluxSortie.print (String.valueOf (connexions));
          fluxSortie.write ('\n');
        }
        else if (chaineLue.startsWith (PaperBoardServer.requeteAjout))
        {
          // Si le vecteur arrive à saturation, il est vidée
          // (ceci pour éviter que le dessin ne devienne trop dense)
          if (listePolylines.size () == listePolylines.capacity ())
            listePolylines.removeAllElements ();
          // La nouvelle polyline reçue est ajoutée à la liste
          listePolylines.addElement (
            chaineLue.substring (PaperBoardServer.requeteAjout.length ()));
        }
      }
      
      fluxEntree.close ();
      fluxSortie.close ();
      socket.close ();
    }
    catch (IOException e)
    {
      System.out.println (e);
    }  
    
    connexions--;
  }
}
