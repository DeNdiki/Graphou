/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphou;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.output.*;

public class Gfile
{
   //Nous allons commencer notre arborescence en créant la racine XML
   //qui sera ici "personnes".
   private Element racine = new Element("Graphe");
   private Vector sommetlist;
   private Vector aretelist;
   private int type;
   private String name;

   //On crée un nouveau Document JDOM basé sur la racine que l'on vient de créer
   private org.jdom2.Document document;
   
   public Gfile()
   {
       sommetlist = new Vector();
       aretelist = new Vector();
       document = new Document();
       sommetlist.clear();
       aretelist.clear();
   }
   
   public Gfile(String nom)
   {
       this();
       name = nom;
   }
   public Gfile(int typ,Vector noeuds,Vector arete,String nom)
   {   
       this();
       document = new Document(racine);
       for(int i=0;i<noeuds.size();i++) {
           sommetlist.add((Sommet)noeuds.get(i));
       }
       for(int i=0;i<arete.size();i++) {
           aretelist.add((Arete)arete.get(i));
       }
       type = typ; 
       name = nom;
   }
   
   public void construct()
   {
      //On crée un nouvel Element etudiant et on l'ajoute
      //en tant qu'Element de racine
      if(type == 1) {
           racine.setAttribute("Nature","orienté");
       }
      else {
           racine.setAttribute("Nature","Non orienté");
       }
      ////////////////////////////////////////////
      ////////////////////////////////////////////
      for(int i=0;i<sommetlist.size();i++)
      {
        Element sommet = new Element("Sommet");
        racine.addContent(sommet);
        Sommet cur_som;
        cur_som = ((Sommet)sommetlist.get(i));
        sommet.setAttribute("X",Double.toString(cur_som.get_forme().getX()));
        sommet.setAttribute("Y",Double.toString(cur_som.get_forme().getY()));
        sommet.setAttribute("NUM",Integer.toString(cur_som.get_numero()));
        sommet.setAttribute("Name",cur_som.get_nom());
        
        //On crée un nouvel Element arete,
        //et on l'ajoute en tant qu'Element de sommet
        for(int j=0;j<aretelist.size();j++)
        {
            if(((Arete)aretelist.get(j)).get_debut() == cur_som)
            {
                 Element arete = new Element("Arc");
                 arete.setAttribute("Origine",Integer.toString(cur_som.get_numero()));
                 arete.setAttribute("Fin",Integer.toString(((Arete)aretelist.get(j)).get_fin().get_numero()));
                 arete.setAttribute("Cout",Double.toString(((Arete)aretelist.get(j)).get_cout()));
                 arete.setAttribute("Nature1",((Arete)aretelist.get(j)).get_nature());
                 arete.setAttribute("Nature2",((Arete)aretelist.get(j)).get_nature2());
                 sommet.addContent(arete);
            }
        }
      }
      enregistre(name);
   }
   
   //Ajouter ces deux méthodes à notre classe Gfile
    /**
     *
     */
    public void affiche()
    {
       try
       {
          //On utilise ici un affichage classique avec getPrettyFormat()
          XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
          sortie.output(document, System.out);
       }
       catch (java.io.IOException e){}
    }

    /**
     *
     * @param fichier
     * @return
     */
    public boolean enregistre(String fichier)
    {
       try
       {
          //On utilise ici un affichage classique avec getPrettyFormat()
          XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
          //Remarquez qu'il suffit simplement de créer une instance de FileOutputStream
          //avec en argument le nom du fichier pour effectuer la sérialisation.
          sortie.output(document, new FileOutputStream(fichier));
          return true;
       }
       catch (java.io.IOException e){return false;}
    }
    
    public Petit restor_graph()
    {
        int i=0,i1=0;
        sommetlist = new Vector();
        aretelist = new Vector();
        document = new Document();
        sommetlist.clear();
        aretelist.clear();    //On crée une instance de SAXBuilder
        SAXBuilder sxb = new SAXBuilder();
        try
        {
         //On crée un nouveau document JDOM avec en argument le fichier XML
         //Le parsing est terminé ;)
         document = sxb.build(new File(name));
        }
        catch(Exception e){}

        //On initialise un nouvel élément racine avec l'élément racine du document.
        racine = document.getRootElement();
        //on récupère la nature de l'élément qui se trouve à la racine du graphe (Graphe)
        switch (racine.getAttributeValue("Nature"))
        {
            case "orienté" : i=1;break;
            case "Non orienté" : i=2;break;
        }
        //on récupère la liste de tous les fils de premier niveau (Sommet)
        List listSommet = racine.getChildren("Sommet");
        Iterator j = listSommet.iterator();
        while(j.hasNext())
        {
            Element cur_el = (Element)j.next();
            Sommet cur_som = new Sommet(Double.parseDouble(cur_el.getAttributeValue("X")),Double.parseDouble(cur_el.getAttributeValue("Y")),Integer.parseInt(cur_el.getAttributeValue("NUM")));
            cur_som.set_nom2(cur_el.getAttributeValue("Name"));
            sommetlist.add((Sommet)cur_som);
        }   
        //on récupère la liste de tous les fils de second niveau (Arc)
        List listarc = racine.getChildren("Sommet");
        if(!listarc.isEmpty())
        {
          Iterator k = listarc.iterator();
          while(k.hasNext())
          {
            Sommet deb = new Sommet(),fin = new Sommet();
            Element cur = (Element)k.next();
            List cur_tab = cur.getChildren("Arc");
            if(!cur_tab.isEmpty())
            {
              Iterator ko = cur_tab.iterator();
              while(ko.hasNext())
              {
                Element cur_el = (Element)ko.next();
                i1 = Integer.parseInt(cur_el.getAttributeValue("Origine"));
                for(int y=0;y<sommetlist.size();y++)
                {
                  if(((Sommet)sommetlist.get(y)).get_numero() == i1) {
                      deb = (Sommet)sommetlist.get(y);
                      break;
                  }
                } 
                i1 = Integer.parseInt(cur_el.getAttributeValue("Fin"));
                for(int y=0;y<sommetlist.size();y++)
                {
                  if(((Sommet)sommetlist.get(y)).get_numero() == i1) {
                      fin = (Sommet)sommetlist.get(y);
                      break;
                  }
                }
                Arete ar = new Arete(deb, fin,Double.parseDouble(cur_el.getAttributeValue("Cout")),cur_el.getAttributeValue("Nature1"),cur_el.getAttributeValue("Nature2"));
                aretelist.add((Arete)ar);
              }
            }
        }
        }
        else {
            System.out.println("liste des arcs vide!!!!!!");
        }
        return (new Petit(sommetlist, aretelist,i,listarc.size()));
    }
}

