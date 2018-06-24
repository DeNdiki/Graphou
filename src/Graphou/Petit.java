/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphou;

import java.util.Vector;

/**
 *
 * @author stephane
 */
public class Petit 
{
    private Vector sommlist,arelist;
    private int type_graphe,nombre;
    
    public Petit()
    {
      sommlist = new Vector();
      arelist  = new Vector();
      sommlist = null;
      arelist = null;
      type_graphe = 0;
      nombre =0;
    }
    
    public Petit(Vector sommet,Vector arete, int typ,int nbe)
    {
      this();
      sommlist = sommet;
      arelist = arete;
      type_graphe = typ;
      nombre = nbe;
    }

    /**
     * @return the sommlist
     */
    public Vector getSommlist() {
        return sommlist;
    }

    /**
     * @param sommlist the sommlist to set
     */
    public void setSommlist(Vector sommlist) {
        this.sommlist = sommlist;
    }

    /**
     * @return the arelist
     */
    public Vector getArelist() {
        return arelist;
    }

    /**
     * @param arelist the arelist to set
     */
    public void setArelist(Vector arelist) {
        this.arelist = arelist;
    }

    /**
     * @return the type_graphe
     */
    public int getType_graphe() {
        return type_graphe;
    }

    /**
     * @param type_graphe the type_graphe to set
     */
    public void setType_graphe(int type_graphe) {
        this.type_graphe = type_graphe;
    }

    /**
     * @return the nombre
     */
    public int getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(int nombre) {
        this.nombre = nombre;
    }
}
