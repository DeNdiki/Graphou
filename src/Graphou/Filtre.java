/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphou;

/**
 *
 * @author stephane
 */
import java.io.File;
import javax.swing.filechooser.FileFilter;

public class Filtre extends FileFilter 
{
 private String extension = ".kmk", description = "Fichier graphe dynamique";
 
 public Filtre(String ext, String descrip){
  this.extension = ext;
  this.description = descrip;
 }
 
    @Override
 public boolean accept(File file){
  return (file.isDirectory() || file.getName().endsWith(this.extension));
 }
 
    /**
     *
     * @return
     */
    @Override
 public String getDescription(){
  return this.extension + " - " + this.description;
 } 
}