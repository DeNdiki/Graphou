/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphou;

/**
 *
 * @author stephane
 */
public class Sarete 
{
   private Arete arete;
   private boolean statut;
   
   public Sarete(Arete ar,boolean boll)
   {
       arete = new Arete(ar);
       statut = boll;
   }

    public Sarete() {
       arete = new Arete();
       statut = false;
    }

    /**
     * @return the arete
     */
    public Arete getArete() {
        return arete;
    }

    /**
     * @param arete the arete to set
     */
    public void setArete(Arete arete) {
        this.arete = arete;
    }

    /**
     * @return the statut
     */
    public boolean isStatut() {
        return statut;
    }

    /**
     * @param statut the statut to set
     */
    public void setStatut(boolean statut) {
        this.statut = statut;
    }
}
