/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphou;
import java.awt.*;

/**
 *
 * @author stephane
 */
public class ligne 
{
   private int aa,ba,ca,da;
   
   public ligne(int a,int b,int c,int d)
   {
       aa =a;ba=b;ca=c;da=d;
   }

   
   public void draw(Graphics2D g2D)
   {
       g2D.setPaint(Color.BLACK);
       g2D.setStroke(new BasicStroke(1f));
       g2D.drawLine(aa,ba,ca,da);
   }
   
    /**
     *
     * @param g2D
     */
    public void draw2(Graphics2D g2D)
   {
       g2D.setPaint(new Color(204,204,204));
       g2D.setStroke(new BasicStroke(1f));
       g2D.drawLine(aa,ba,ca,da);
   }
}
