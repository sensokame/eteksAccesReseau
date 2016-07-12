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
import java.util.*;
 
public class PaperBoardClient extends Applet implements Runnable
{
  private String          message;
  private Socket          socket;
  private DataInputStream fluxEntree;
  private PrintStream     fluxSortie;
  
  private Vector          listePolylines = new Vector (100);
  private Polygon         polylineCourante;
  
  private int             connexions;
  
  // Méthode appelée par le système à l'affichage de l'applet
  public void start ()
  {
    setBackground (Color.white);
    try 
    {
      // Récupération de l'adresse de l'hôte www.eteks.com
      InetAddress adresse = InetAddress.getByName ("localhost");
      // Ouverture d'une connexion sur le port 26197 de cet hôte
      socket = new Socket (adresse, PaperBoardServer.port);
      
      // Récupération des flux d'entrés et de sortie
      fluxEntree = new DataInputStream (
                     new BufferedInputStream (socket.getInputStream ()));
      fluxSortie = new PrintStream (
                     new BufferedOutputStream (socket.getOutputStream ()), true);
        
      // Lancement d'un thread qui interroge à intervalle régulier 
      // la liste des polylines enregistrées sur le serveur                
      new Thread (this).start ();
    }
    catch (IOException e)
    {
      message = "Probleme de connexion avec le serveur";
    }
  }
 
  // Méthode appelée par le système à la disparition de l'applet
  public void stop ()
  {
    try
    {
      // Envoi d'une requête FIN et fermeture des flux
      fluxSortie.println (PaperBoardServer.requeteFin);
      fluxSortie.close ();
      fluxEntree.close ();
      socket.close ();
    }
    catch (IOException e)
    { }
    
    fluxSortie = null;
  }
 
  public void run ()
  {
    try
    {
      while (fluxSortie != null)
      {
        // Envoi d'une requête CONNEXION pour récupérer le 
        // nombre de clients connectés au serveur
        fluxSortie.print (PaperBoardServer.requeteConnexion);
        fluxSortie.write ('\n');
 
        message = fluxEntree.readLine () + " connexions";
      
        // Envoi d'une requête LISTE pour récupérer 
        // la liste de toutes les polylines du serveur
        fluxSortie.print (PaperBoardServer.requeteListe);
        fluxSortie.write ('\n');
 
        String liste = fluxEntree.readLine ();
        
        // Vidange de la liste pour la remettre à jour
        listePolylines.removeAllElements ();
        StreamTokenizer tokens = new StreamTokenizer (
                                   new StringBufferInputStream (liste));
        tokens.parseNumbers ();
        tokens.ordinaryChar ('\t');
        tokens.whitespaceChars (' ', ' ');
    
        // Décodage de la liste de points 
        while (tokens.nextToken () != StreamTokenizer.TT_EOF)
        {
          Polygon polyline = new Polygon ();
          // Récupération des couples de valeurs (x,y)
          // d'une polyline jusqu'à la prochaine tabulation
          while (tokens.ttype != '\t')
          {
            int x = (int)tokens.nval;
            tokens.nextToken ();
            int y = (int)tokens.nval;
            tokens.nextToken ();
            polyline.addPoint (x, y);
          }
          // Ajout de la polyline à la liste
          listePolylines.addElement (polyline);
        }    
        
        repaint ();
        // Arrête le thread pendant 1 s avant de lancer 
        // une nouvelle demande de mise à jour
        Thread.sleep (1000);
      }
    }
    catch (InterruptedException e) 
    { }
    catch (IOException e)
    { }
  }
    
  // Méthode appelée par le système pour mettre à jour le dessin de l'applet
  public void paint (Graphics gc)
  {
    if (message != null)
      gc.drawString (message, 10, 20);
 
    // Dessin de toutes les polylines 
    // et de la polyline courante si elle existe
    for (Enumeration e = listePolylines.elements (); 
         e.hasMoreElements (); )
      drawPolyline (gc, (Polygon)e.nextElement ());
 
    if (polylineCourante != null)
      drawPolyline (gc, polylineCourante);
  }
  
  private void drawPolyline (Graphics gc, Polygon polyline)
  {
    for (int i = 1; i < polyline.npoints; i++)
      gc.drawLine (polyline.xpoints [i - 1], polyline.ypoints [i - 1],
                   polyline.xpoints [i],     polyline.ypoints [i]);
  }
  
  // Les méthodes mouseDown (), mouseDrag (), mouseUp () sont
  // appelées par le système à l'enfoncement du bouton de la souris,
  // au déplacement du pointeur, et au relâchement du bouton
  public boolean mouseDown (Event evt, int x, int y)
  {
    polylineCourante = new Polygon ();
    polylineCourante.addPoint (x, y);
    return true;
  }
 
  public boolean mouseDrag (Event evt, int x, int y)
  {
    polylineCourante.addPoint (x, y);
    paint (getGraphics ());
    return true;
  }
 
  public boolean mouseUp (Event evt, int x, int y)
  {
    polylineCourante.addPoint (x, y);
    
    // Construction d'une requête AJOUT avec la liste des points
    // de la nouvelle polyline
    fluxSortie.print (PaperBoardServer.requeteAjout);
    for (int i = 0; i < polylineCourante.npoints; i++)
      fluxSortie.print (String.valueOf (polylineCourante.xpoints [i])
                        + ' ' + polylineCourante.ypoints [i] + ' ');
         
    fluxSortie.write ('\n');
    listePolylines.addElement (polylineCourante);
    paint (getGraphics ());
    return true;
  }
}