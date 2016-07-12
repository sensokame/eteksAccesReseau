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
 
public class EchoServer
{
  public static void main (String args [])
  {
    try
    {
      // Création d'un serveur sur le port 13267
      ServerSocket server = new ServerSocket (13267);
      // Attente de la connexion d'un client
      Socket       socket = server.accept ();
 
      System.out.println ("Connexion sur le socket : " + socket);
 
      // Récupération des flux d'entrés et de sortie
      InputStream  fluxEntree = socket.getInputStream ();
      OutputStream fluxSortie = socket.getOutputStream ();
 
      char caractereLu;
      // Renvoie de chaque caractère lu au client
      // jusqu'à ce que ce caractère soit un retour chariot
      while ((caractereLu = (char)fluxEntree.read ()) != '\n')
        fluxSortie.write (caractereLu);
        
      server.close ();
    }
    catch (IOException e)
    {
      System.out.println (e);
    }  
  }
}
