/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphou;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Kibongue
 */ 
 class GTodel extends AbstractTableModel
 {
        private Object[][] data;
        private String[] title;
        /**
      * Constructeur
      * @param data
      * @param title
      */
        public GTodel(Object[][] data, String[] title){
         this.data = data;
         this.title = title;
        }

        /**
      * Retourne le nombre de colonnes
      */
     @Override
        public int getColumnCount() {
         return this.title.length;
        }

        /**
      * Retourne le nombre de lignes
      */
     @Override
        public int getRowCount() {
         return this.data.length;
        }

        /**
      * Retourne la valeur à l'emplacement spécifié
      */
     @Override
        public Object getValueAt(int row, int col) {
         return this.data[row][col];
        }
     /**
      * Retourne le titre de la colonne à l'indice spécifé
     */
     @Override
    public String getColumnName(int col) {
      return this.title[col];
    }
        /**
    * Retourne la classe de la donnée de la colonne
    * @param col
    */
     @Override
    public Class getColumnClass(int col){
     //On retourne le type de la cellule à la colonne demandée
     //On se moque de la ligne puisque les données sur chaque ligne sont les mêmes
     //On choisit donc la première ligne
     return this.data[0][col].getClass();
    }
 }