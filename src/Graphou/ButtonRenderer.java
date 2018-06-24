/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphou;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Kibongue
 */
public class ButtonRenderer extends JButton implements TableCellRenderer
{
 public Component getTableCellRendererComponent( JTable table,Object value,boolean isSelected , boolean isFocus,int row, int col) 
 {
  //On écrit dans le bouton ce que contient la cellule
  setText((value != null) ? value.toString() : "");
  //on retourne notre bouton
  return this;
 }
}