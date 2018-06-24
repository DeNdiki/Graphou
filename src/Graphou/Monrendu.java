/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphou;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Kibongue
 */
public class Monrendu extends DefaultTreeCellRenderer  
{
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,  
            boolean sel, boolean expanded, boolean leaf, int row, 
            boolean hasFocus) {
 
	super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,   
                row, hasFocus);
 
	DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
	//Condition pour laquelle tu veux changer l'icone
	if ("Graphe courant".equals(node.toString())) 
        {
	    //On affecte à la feuille une icone différente
	    setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/comp1.gif")));
	}
	if ("Sommet(s)".equals(node.toString())) 
        {
	    //On affecte à la feuille une icone différente
	    setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/globe.png")));
	}
	if ("Arete(s)".equals(node.toString())) 
        {
	    //On affecte à la feuille une icone différente
	    setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/sommet.png")));
	}
	if ((node.toString()).startsWith("Arete N°")) 
        {
	    //On affecte à la feuille une icone différente
	    setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/arete.png")));
	}
	if ((node.toString()).startsWith("Sommet N°")) 
        {
	    //On affecte à la feuille une icone différente
	    setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/sommet_arbre.png")));
	}
        
	if (!(node.toString()).startsWith("Arete N°") && !(node.toString()).startsWith("Sommet N°") && !"Arete(s)".equals(node.toString()) && !"Sommet(s)".equals(node.toString()) && !"Graphe courant".equals(node.toString())) 
        {
	    //On affecte à la feuille une icone différente
	    setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/bien.png")));
	}
 
	return this;
    }
}
