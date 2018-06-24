/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphou;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Kibongue
 */
 
 public class TableComponent extends DefaultTableCellRenderer 
 {
     @Override
   public Component getTableCellRendererComponent(JTable table,
           Object value, boolean isSelected , boolean hasFocus, int row,
           int column) 
   {
     //Si la valeur de la cellule est un JButton, on transtype notre valeur
     if (value instanceof JButton)
     {
       return (JButton) value;
     }
     else {
           return this;
       }
 }
}
