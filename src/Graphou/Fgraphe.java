/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphou;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author stephane
 */
public class Fgraphe extends javax.swing.JFrame {
    private ArrayList<Arete> copie; 
    private  DefaultTreeCellRenderer[] tCellRenderer = new DefaultTreeCellRenderer[1];

    /**
     * Creates new form Fgraphe
     */
    public Fgraphe() {
        initComponents();
	// specification de la fenêtre
	Toolkit theKit = getToolkit();
	Dimension wndSize = theKit.getScreenSize();
	setBounds(0,0,wndSize.width, wndSize.height);
        this.setLocationRelativeTo(null);
	sommetList = new Vector() ;
	areteList = new Vector() ;
        copieareteList = new Vector();
        areteListInv = new Vector();
        ar = new ArrayList();
        ACMarc = new ArrayList<>();
        Arbre = new ArrayList<>();
        recherche = new ArrayList();
        linge = new ArrayList();
	adjList = new Vector() ;
        zone.setToolTipText(null);
        //blocage du panneau qui affiche les résultats
        pcc.setEnabled(true);
        pcc1.setEnabled(true);
        mouseStatus = -5;
        filtre = new Filtre(".kmk","Fichier graphe dynamique");
        show.setSelected(true);
        show1.setSelected(true);
    }
    /** méthode pour le redessin de la fenêtre: sommets et arêtes */
    
  
  /** initialisation*/
  public void initialise(){
    System.out.println(" INITIALISATION");
    for( int i=0; i<sommetList.size(); i++)
    {
      Sommet init_g = (Sommet) sommetList.get(i);
      init_g.set_color(colorNonVisite);
    }
    graphe_sommet();
    pause(150);
  } 
  //visiter pp
  
  private DefaultMutableTreeNode constitu_arbre()
  {
      constitue_degre();
      DefaultMutableTreeNode racine = new DefaultMutableTreeNode("Graphe courant");
      DefaultMutableTreeNode sommets = new DefaultMutableTreeNode("Sommet(s)"); 
      for(int i=0;i<sommetList.size();i++){
         DefaultMutableTreeNode racine_sommet = new DefaultMutableTreeNode("Sommet N°"+((Sommet)sommetList.get(i)).get_numero());
         racine_sommet.add(new DefaultMutableTreeNode("Nom :"+((Sommet)sommetList.get(i)).get_nom()));
         racine_sommet.add(new DefaultMutableTreeNode("Dégré :"+((Sommet)sommetList.get(i)).get_Degre()));
         racine_sommet.add(new DefaultMutableTreeNode("Couleur :"+((Sommet)sommetList.get(i)).get_color().getRed()+";"+((Sommet)sommetList.get(i)).get_color().getGreen()+";"+((Sommet)sommetList.get(i)).get_color().getBlue()));
         sommets.add(racine_sommet);
      }
      DefaultMutableTreeNode Arcs = new DefaultMutableTreeNode("Arete(s)"); 
      for(int i=0;i<areteList.size();i++){
         DefaultMutableTreeNode racine_sommet = new DefaultMutableTreeNode("Arete N°"+i+"<>"+((Arete)areteList.get(i)).get_debut().get_nom()+","+((Arete)areteList.get(i)).get_fin().get_nom());
         racine_sommet.add(new DefaultMutableTreeNode("Départ :"+((Arete)areteList.get(i)).get_debut().get_nom()));
         racine_sommet.add(new DefaultMutableTreeNode("Arrivée :"+((Arete)areteList.get(i)).get_fin().get_nom()));
         racine_sommet.add(new DefaultMutableTreeNode("Coût :"+((Arete)areteList.get(i)).get_cout()));
         racine_sommet.add(new DefaultMutableTreeNode("Sens :"+((Arete)areteList.get(i)).get_debut().get_nom()+"-->"+((Arete)areteList.get(i)).get_fin().get_nom()));
         Arcs.add(racine_sommet);
      }
      racine.add(sommets);
      racine.add(Arcs);
      return racine;
  }
  public void Visiter_PP(Sommet sommet_pp)
  {
    tri=0;
    Sommet v;
    sommet_pp.set_color(colorEnCours);
    graphe_sommet();
    pause();
    d       = new int[sommetList.size()];
    p       = new int[sommetList.size()];
    Fd      = new int[sommetList.size()];
    //Ft      = new int[sommetList.size()];
    d[sommet_pp.get_numero()]=tps;
    tps++;
    int nbr_vois = nVoisins(sommet_pp);
    for (int i=0;i<nbr_vois;i++)
    {
      v = iemeVoisin(sommet_pp,i);
      if (v.get_color()==colorNonVisite)
      {
        p[v.get_numero()]=sommet_pp.get_numero();
        Visiter_PP(v);
      }
    }
    sommet_pp.set_color(colorDejaVisite);
    graphe_sommet();
    pause();
    Fd[sommet_pp.get_numero()]=tps;
    tps++;
    sommet_pp.set_dateZ(tps);
    Fd[sommet_pp.get_numero()]=tps;
    Ft[tri++]=sommet_pp.get_numero();

  }
 /** parcours en profondeur: tout le graphe */
    @Override
    public void repaint()
    {
        ((Fgraphe)monInstance).zone.repaint();
        graphe_sommet();
        graphe_arete();
    }
    public void PP()
    {
      Sommet vpp_som;
      
      ecran.setText("Parcours en profondeur\n==================");
      p = new int[sommetList.size()];
      Ft =new int[sommetList.size()];
      for (int i = 0; i < sommetList.size(); i++) {
        vpp_som = (Sommet) sommetList.get(i);
        if (vpp_som.equals(pointInitial) == false) {
          vpp_som.set_color(colorNonVisite);
          p[i] = Fgraphe.nil;
        }
      }
      graphe_sommet();pause();
      tps = 0;
      for (int i = 0; i < sommetList.size(); i++) {
        vpp_som = (Sommet) sommetList.get(i);
        if (vpp_som.get_color() == colorNonVisite) {
          Visiter_PP(vpp_som);
          if ( ( (nVoisins(vpp_som)) != 0) &&(vpp_som.get_pere()==null)) {
            vpp_som.set_color(Color.yellow);
            graphe_sommet();pause();
          }
	  if (boolcfctrue==true) {

	      vpp_som.set_color(Color.green); graphe_sommet(); pause();}
        }
      }
      ecran.setText(ecran.getText().concat("\nLes sommets en jaune sont soit : \n- Les points d'entrés des\n composants fortements connexes.\n- Des sources des composants\n fortements connexes."));
    }   
  /** effectue un parcours en largeur depuis un sommet, et l'anime */
  public void PL()
  {
    ecran.setText("Parcours en largeur\n===============\nSommet(s) en noir : \n--Sommet(s) accessible(s) \n  à partir du sommet\n  "+pointInitial.get_nom());
    initialise();
    String str="\n     ";
    System.out.println("parcours en largeur à partir du sommet " + indexPointInitial ) ;
    d       = new int[sommetList.size()];
    p       = new int[sommetList.size()];
    F       = new LinkedList();

    for( int i = 0; i < sommetList.size() ; i++ ) {
      Sommet g_som = (Sommet) sommetList.get(i);
      if( g_som.equals(pointInitial) == false ) {
        g_som.set_color( colorNonVisite ) ;
        d[i] = Fgraphe.iInfini ;
        p[i] = Fgraphe.nil ;
      }
    }
    int s = pointInitial.get_numero();
   pointInitial.set_color(colorEnCours) ;
   graphe_sommet();
   pause(150) ;
   d[s] = 0;
   p[s] = Fgraphe.nil;
   F.add((Object)pointInitial);// A decommenter apres avoir ecrit le code de la boucle while ci dessous
   Sommet u;
   while( F.size() != 0 ) {
     u =(Sommet) F.getFirst();
     for(int i=0;i<nVoisins(u);i++){
       Sommet v= iemeVoisin(u,i);
       if (v.get_color()==colorNonVisite){
         v.set_color(colorEnCours);
         graphe_sommet();
         pause(150);
         d[v.get_numero()]=d[u.get_numero()]+1;
         p[v.get_numero()]=u.get_numero();
         F.add((Object)v);
       }
     }
     F.removeFirst();
     u.set_color(colorDejaVisite);
     if(!pointInitial.get_nom().equals(u.get_nom())) {
           str+="\n     - ".concat(u.get_nom());
       }
     graphe_sommet();
	pause(150);
      }
     ecran.setText(ecran.getText().concat(str+"\n\nSommet(s) en bleu : \n--Sommet(s) inaccessibles\n  à partir du sommet\n  "+pointInitial.get_nom()));
    }
   
    
    /** setter pour showCout: si 'true' on affiche la distance à l'origine
	des sommets, sinon, on affiche le numéro du sommet */
    public static void set_showCout( boolean status )
    {
	showCout = status ;
    }
    /** getter pour showCout */
    public static boolean get_showCout()
    {
	return showCout ;
    }
    /** getter pour showCout2 */
    public static boolean get_showCout2()
    {
	return showCout2 ;
    }

    private Sommet Rplus_petit()
    {
        int val = iInfini;
        Sommet b = new Sommet();
        int k=0;
        for(int i=0;i<ACMarc.size();i++)
        {
                if(ACMarc.get(i).getArete().get_cout() <= val && ACMarc.get(i).isStatut() == false && est_dans_arbre(ACMarc.get(i).getArete().get_fin())==false)
                {
                    k = i;
                    val = (int) ACMarc.get(i).getArete().get_cout();
                }
        }
        if(val != iInfini){
            if(k<ACMarc.size()){
                ACMarc.get(k).setStatut(true);
                b = ACMarc.get(k).getArete().get_fin();
            }
        }
      if(Ajoute_a_arbre(b)) {
            return b;
        }
      else {
            return null;
        }
    }
    
    private boolean Ajoute_a_arbre(Sommet som)
    {
        if(som == null) {
            return false;
        }
        for(int i=0;i<Arbre.size();i++){
            if(Arbre.get(i) == som){
                return false;
            }
        }
            Arbre.add(som);
            return true;
    }
    
    private boolean est_dans_arbre(Sommet som)
    {
        if(som == null) {
            return true;
        }
        for(int i=0;i<Arbre.size();i++){
            if(Arbre.get(i) == som){
                return true;
            }
        }
            return false;
    }
    
    private void draw_witch_pause()
    {
          if(ar.size()>0)
          {
                for(int i=0;i<areteList.size();i++) {
                  effacer((Arete)areteList.get(i));
              }
              graphe_sommet();
                for(int i=0;i<ar.size();i++) {
                    Arete are = (Arete)ar.get(i); 
                    if(A_inverse(are) && type==1)
                    {
                        if(Ma_pos(are)<Pos_inverse(are)) {
                            are.draw3_moins((Graphics2D)getGraphics());
                        }
                        else {
                            are.draw3_plus((Graphics2D)getGraphics());
                        }
                    }
                    else {
                        are.draw3((Graphics2D)getGraphics());
                    }
                }
                graphe_sommet();
          }
    }
    
    public boolean ACM2(Sommet som)
    {
	LinkedList sommetAdjList1 = (LinkedList)adjList.get(som.get_numero()) ;
        Sommet b = new Sommet();
        route_acm ="";
                 if(Arbre.size() == sommetList.size()){
                     ar.clear();
                     arbre_taille=0;
                     for(int i=0;i<ACMarc.size();i++){
                        if(ACMarc.get(i).isStatut()){
                            ar.add((Arete)new Arete((ACMarc.get(i).getArete())));
                            route_acm+="\n(".concat(((Arete)(ACMarc.get(i).getArete())).get_debut().get_nom()+","+((Arete)(ACMarc.get(i).getArete())).get_fin().get_nom()+")");
                            arbre_taille+= (int) ((Arete)ACMarc.get(i).getArete()).get_cout();
                        }
                    }
                    ecran.setText("Algo de L'A.C.M\n============\n- poids de l'arbre : ");
                    ecran.setText(ecran.getText().concat((new Integer(arbre_taille)).toString()));
                    ecran.setText(ecran.getText().concat("\n- Les arêtes sûres sont : "+route_acm));
                    ACMarc.clear();
                    draw_witch_pause();
                    return true;
                 }
                 
        Ajoute_a_arbre(som);
        if(sommetAdjList1.isEmpty())
        {
            b = Rplus_petit();
            if(b!=null){
                ACM2(b);
            }
            else{
                b = Rplus_petit();
                if(b!=null){
                 ar.clear();
                 if(Arbre.size() == sommetList.size()){
                    for(int i=0;i<ACMarc.size();i++){
                        if(ACMarc.get(i).isStatut()){
                            ar.add((Arete)(ACMarc.get(i).getArete()));
                        }
                    }
                    draw_witch_pause();
                    return true;
                 }
                 else{
                     ACM2(b);
                 }
                }
                 else{
                              JOptionPane.showMessageDialog(null,"Impossible de determiner l'ACM,\nVotre graphe n'est pas connexe.","Message d'avertissement",JOptionPane.ERROR_MESSAGE);
                              
                    for(int i=0;i<ACMarc.size();i++){
                        if(ACMarc.get(i).isStatut()){
                            ar.add((Arete)(ACMarc.get(i).getArete()));
                        }
                    }draw_witch_pause();
                              return false;
                }
            }
        }
        else{
            for(int i =0; i<areteList.size();i++){
                if(((Arete)areteList.get(i)).get_debut() == som ){
                    ACMarc.add(new Sarete((Arete)areteList.get(i), false));
                }
            }
            b = Rplus_petit();
            if(b!=null){
                ACM2(b);
            }
            else{
                b = Rplus_petit();
                if(b!=null){
                 ar.clear();
                 if(Arbre.size() == sommetList.size()){
                    for(int i=0;i<ACMarc.size();i++){
                        if(ACMarc.get(i).isStatut()){
                            ar.add((Arete)(ACMarc.get(i).getArete()));
                        }
                    }
                    draw_witch_pause();
                    return true;
                 }
                 else{
                     ACM2(b);
                 }
                }
                 else{
                              JOptionPane.showMessageDialog(null,"Impossible de determiner l'ACM,\nVotre graphe n'est pas connexe.","Message d'avertissement",JOptionPane.ERROR_MESSAGE);
                              
                    for(int i=0;i<ACMarc.size();i++){
                        if(ACMarc.get(i).isStatut()){
                            ar.add((Arete)(ACMarc.get(i).getArete()));
                        }
                    }
                              draw_witch_pause();
                              return false;
                }
            }
        }
        return true;
    }
    /** pause d'une demi-seconde */
    public void pause()
    {
	try {
	    Thread.sleep(500);
	}
	catch ( InterruptedException e ) {
	}
    }
    
//creer ensemble
  public void creerEnsemble(Sommet x){
    x.set_pere(x);
    x.set_rang(0);
  }
  //trouver ensemble
  public Sommet trouverEnsemble(Sommet x){
    if(x!=x.get_pere())
    {
      x.set_pere(trouverEnsemble(x.get_pere()));
    }
    return x.get_pere();
  }//union
  public void union(Sommet x,Sommet y){
    lier(trouverEnsemble(x),trouverEnsemble(y));
  }
  //lier
  public void lier(Sommet x, Sommet y){
    if (x.get_rang()>y.get_rang()){
      y.set_pere(x);
    }
    else {
      x.set_pere(y);
      if (x.get_rang()==y.get_rang()){
        y.set_rang((y.get_rang()+1));
      }
    }
  }
 //algo de l'arbre couvrant des poids minimum
public void ACM()
{
    Vector ListeArete =new Vector();
    Arete Ar,Ar1;
    E = new LinkedList();
    for(int i=0;i<sommetList.size();i++){
      Sommet v = (Sommet) sommetList.get(i);
      creerEnsemble(v);
    }
    /* trie des aretes*****/
    //on receuille les aretes
    for(int i =0;i<sommetList.size();i++)
    {
      Sommet u =(Sommet) sommetList.get(i);
      sommetAdjList= (LinkedList)adjList.get(u.get_numero());
      for(int j =0; j<sommetAdjList.size();j++)
      {
        ListeArete.add((Arete)sommetAdjList.get(j));
      } 
    }
    //on va trier la liste des arete par rapport au cout
    for(int i=0;i<ListeArete.size();i++)
    {
      for(int j=0;j<ListeArete.size();j++)
      {
        Ar1=(Arete)ListeArete.get(j);
        if (((Arete)ListeArete.get(i)).get_cout()>((Arete)ListeArete.get(j)).get_cout()){
          Arete temp = (Arete)ListeArete.get(i);
          ListeArete.get(i).equals(ListeArete.get(j));
          ListeArete.get(j).equals(temp);
        }
      }
    }
    ar.clear();
    graphe_arete();graphe_sommet();
    Sommet u,v;
    for(int i=0;i<ListeArete.size();i++)
    {
      u= ((Arete)ListeArete.get(i)).get_debut();
      v= ((Arete)ListeArete.get(i)).get_fin();
      if(trouverEnsemble(u)!=trouverEnsemble(v)){
        E.add(new Arete(u,v,((Arete)ListeArete.get(i)).get_cout(),((Arete)ListeArete.get(i)).get_nature(),((Arete)ListeArete.get(i)).get_nature2()));

        Arete acmArete = new Arete(u,v,((Arete)ListeArete.get(i)).get_cout(),((Arete)ListeArete.get(i)).get_nature(),((Arete)ListeArete.get(i)).get_nature2());
        ar.add((Arete)acmArete);
        union(u,v);
      }
    }
          if(ar.size()>0)
          {
                for(int i=0;i<areteList.size();i++) {
                  effacer((Arete)areteList.get(i));
              }
              graphe_sommet();
                for(int i=0;i<ar.size();i++) {
                    Arete are = (Arete)ar.get(i); 
                    if(A_inverse(are) && type==1)
                    {
                        if(Ma_pos(are)<Pos_inverse(are)) {
                            are.draw3_moins((Graphics2D)getGraphics());
                        }
                        else {
                            are.draw3_plus((Graphics2D)getGraphics());
                        }
                    }
                    else {
                        are.draw3((Graphics2D)getGraphics());
                    }
                    pause();
                }
          }
          graphe_sommet();
 }
    
    /** pause variable */
    public void pause(int milliSecondes)
    {
	try {
	    Thread.sleep(milliSecondes);
	}
	catch ( InterruptedException e ) {}
    }
    
    /** conversio	Fd       = new int[sommetList.size()]n en chaine de caractères de la liste d'adjacence */
    @Override
    public String toString()
    {
	String retStr ;
	LinkedList sadliqt ;
	Sommet cur ;
	retStr = "GRAPH'OU :: Structure du graphe\n" ;
	for ( int i=0 ; i<sommetList.size() ; i++ ) {
	    retStr += "Sommet numéro " + i + " : " ;
	    sadliqt = (LinkedList)adjList.get(i) ;
	    for (int j=0 ; j<sadliqt.size() ; j++ ) {
		cur = iemeVoisin( i , j ) ;
		retStr += " " + cur.get_numero() + "(" + W(i,cur) + ") "  ;
	    }
	    if ( i<sommetList.size()-1 ) {
                retStr += "\n" ;
            }
	}
	return retStr ;
    }
    
    /** méthode donnant le nombre de voisin d'un sommet u */
    public int nVoisins( Sommet u )
    {
	LinkedList sadlist = (LinkedList)adjList.get( u.get_numero() ) ;
	return sadlist.size() ;

    }
    /** méthode donnant le nombre de voisin du sommet numéro numSom, retour -1 si i trop grand */
    public int nVoisins( int numSom )
    {
	if ( numSom >= adjList.size() ) return -1 ;
	LinkedList sommetAdjList = (LinkedList)adjList.get( numSom ) ;
	return sommetAdjList.size() ;

    }
    /** méthode donnant le sommet adjacent numéro i d'un sommet u.
	Retour null si pas de ième voisin  */
    public Sommet iemeVoisin( Sommet u , int i )
    {
	LinkedList sommetAdjList = (LinkedList)adjList.get( u.get_numero() ) ;
	if ( i >= sommetAdjList.size() ) return null ;
	Arete ar = (Arete)sommetAdjList.get(i);
	return ar.get_fin() ;
    }
    /** méthode donnant le sommet adjacent numéro i d'un sommet numéro numSom.
	Retour null si pas de ième voisin ou si numSom trop grand   */
    public Sommet iemeVoisin( int numSom , int i )
    {
	if ( numSom >= adjList.size() ) {
            return null ;
        }
	LinkedList sommetAdjList = (LinkedList)adjList.get( numSom ) ;
	if ( i >= sommetAdjList.size() ) 
        {
            return null ;
        }
	Arete ar = (Arete)sommetAdjList.get(i);
	return ar.get_fin() ;
    }
    
    
    /** méthode donnant le poids de parcours d'un sommet u à un sommet adjacent v.
	Retour infini si sommet non adjacent, le poids de l'arête sinon.
	Equivalent de W( Sommet u , Sommet v) à utiliser si on connait déjà l'index de u */
    public double W( Sommet u , Sommet v )
    {
	Sommet som ;
	int numerodeU = u.get_numero() ;
	return W( numerodeU , v ) ;
    }
   /** méthode donnant le poids de parcours
       du sommet numéro numSom à un sommet adjacent v.
       Retour infini si sommet non adjacent ou si pas de sommet numéro numSom,
       le poids de l'arête sinon */
    public double W( int numSom , Sommet v )
    {
	Sommet som ;
	if ( numSom >= sommetList.size() ) {
            return dInfini ;
        }
	LinkedList sommetAdjList = (LinkedList)adjList.get( numSom ) ;
	for (int i=0 ; i<sommetAdjList.size() ; i++ ) {
	    som = iemeVoisin( numSom , i ) ;
	    if ( som.equals( v ) ) {
                return ( (Arete)sommetAdjList.get(i) ).get_cout() ;
            }
	}
	return dInfini ;

    }
    /** méthode donnant le poids de parcours depuis le sommet numéro numSom vers son
	ième voisin. Retour infini si pas de sommet numSom ou pas de ième voisin.
	Cette méthode est plus efficace que les autres implémentations de W
	car elle ne nécessite pas de parcourir les sommets pour trouver la bonne arête
	parmi la liste d'adjacence */
    public double W ( int numSom , int i )
    {
	if ( numSom >= sommetList.size() ) return dInfini ;
	LinkedList sommetAdjList = (LinkedList)adjList.get( numSom ) ;
	if ( i>= sommetAdjList.size() ) {
            return dInfini ;
        }
	return ((Arete)sommetAdjList.get(i)).get_cout() ;
    }
    /** méthode donnant le poids de parcours depuis le sommet u vers son
	ième voisin. Retour infini si pas de ième voisin.
	Equivalent de W( Sommet u, int i ), à utiliser si on connait déjà l'index de u */
    public double W ( Sommet u , int i )
    {
	int numSom = u.get_numero() ;
	return W( numSom , i ) ;
    }
    
    
    //*****************************************
     public void permut(){
       save =new Vector();
       save=areteList;
       areteList=areteListInv;
     }

    public void retablissement(){
      permut();
      areteList=save;
    }



    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jSplitPane2 = new javax.swing.JSplitPane();
        scroll_arbre = new javax.swing.JScrollPane();
        arbre2 = new javax.swing.JTree();
        scroll_tableau = new javax.swing.JScrollPane();
        pte = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        choixnature = new javax.swing.JComboBox();
        validnature = new javax.swing.JButton();
        init_nature = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        Vsommet = new javax.swing.JButton();
        Varc = new javax.swing.JButton();
        Vpointeur = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        show = new javax.swing.JRadioButton();
        show1 = new javax.swing.JRadioButton();
        jSeparator2 = new javax.swing.JSeparator();
        jButton4 = new javax.swing.JButton();
        Varc1 = new javax.swing.JButton();
        Varc2 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        reboot = new javax.swing.JButton();
        conteneur1 = new javax.swing.JToolBar();
        pcc = new javax.swing.JButton();
        pcc1 = new javax.swing.JButton();
        jToolBar5 = new javax.swing.JToolBar();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        X = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        Y = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jToolBar6 = new javax.swing.JToolBar();
        jButton7 = new javax.swing.JButton();
        PPihm = new javax.swing.JButton();
        PLihm = new javax.swing.JButton();
        Varc4 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        nature = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        cout_visible = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        Szone = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        ecran = new javax.swing.JTextPane();
        zone = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        changementnat = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("GRAPHOU :: untitled1");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });

        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        arbre2.setForeground(new java.awt.Color(0, 153, 255));
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Graphe");
        arbre2.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        arbre2.setAutoscrolls(true);
        scroll_arbre.setViewportView(arbre2);

        jSplitPane2.setTopComponent(scroll_arbre);

        pte.setBackground(new java.awt.Color(153, 204, 255));
        pte.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pte.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Propriété", "Valeur"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        pte.setToolTipText("Propriétés du l'objet sélectionné");
        pte.setGridColor(new java.awt.Color(153, 204, 255));
        pte.setSelectionBackground(new java.awt.Color(255, 255, 255));
        scroll_tableau.setViewportView(pte);

        jSplitPane2.setRightComponent(scroll_tableau);

        jToolBar1.setRollover(true);

        choixnature.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        choixnature.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Orienté", "Non orienté" }));
        jToolBar1.add(choixnature);

        validnature.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        validnature.setText("     Ok     ");
        validnature.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        validnature.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                validnatureMouseClicked(evt);
            }
        });
        jToolBar1.add(validnature);

        init_nature.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        init_nature.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/cool (2).png"))); // NOI18N
        init_nature.setToolTipText("Remettre le graphe initial");
        init_nature.setEnabled(false);
        init_nature.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                init_natureMouseClicked(evt);
            }
        });
        jToolBar1.add(init_nature);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator3.setToolTipText("");
        jToolBar1.add(jSeparator3);

        Vsommet.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Vsommet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/globe.png"))); // NOI18N
        Vsommet.setToolTipText("Sommet");
        Vsommet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                VsommetMouseClicked(evt);
            }
        });
        Vsommet.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                VsommetFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                VsommetFocusLost(evt);
            }
        });
        jToolBar1.add(Vsommet);

        Varc.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Varc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/sommet.png"))); // NOI18N
        Varc.setToolTipText("Arc");
        Varc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                VarcMouseClicked(evt);
            }
        });
        Varc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                VarcFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                VarcFocusLost(evt);
            }
        });
        jToolBar1.add(Varc);

        Vpointeur.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Vpointeur.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/cur.png"))); // NOI18N
        Vpointeur.setToolTipText("mettre le curseur à sa forme initiale");
        Vpointeur.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                VpointeurMouseClicked(evt);
            }
        });
        jToolBar1.add(Vpointeur);

        cancel.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        cancel.setIcon(new javax.swing.ImageIcon("I:\\projet_c++_duree\\graphe2\\src\\graphe2\\b_firstpage.png")); // NOI18N
        cancel.setText("Annuler");
        cancel.setToolTipText("Annuler");
        cancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cancelMouseEntered(evt);
            }
        });
        jToolBar1.add(cancel);

        show.setFont(new java.awt.Font("Times New Roman", 1, 10)); // NOI18N
        show.setText("Afficher les coûts");
        show.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        show.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showMouseClicked(evt);
            }
        });
        jToolBar1.add(show);

        show1.setFont(new java.awt.Font("Times New Roman", 1, 10)); // NOI18N
        show1.setText("Afficher les N° des  sommets");
        show1.setToolTipText("Afficher les numéros des sommets");
        show1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        show1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                show1MouseClicked(evt);
            }
        });
        jToolBar1.add(show1);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jToolBar1.add(jSeparator2);

        jButton4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/b_edit.png"))); // NOI18N
        jButton4.setToolTipText("Renommer le sommet");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });
        jToolBar1.add(jButton4);

        Varc1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Varc1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/b_save.png"))); // NOI18N
        Varc1.setToolTipText("Sauvegarder le graphe");
        Varc1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Varc1MouseClicked(evt);
            }
        });
        jToolBar1.add(Varc1);

        Varc2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Varc2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/open~1.png"))); // NOI18N
        Varc2.setToolTipText("Restaurer un graphe");
        Varc2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Varc2MouseClicked(evt);
            }
        });
        jToolBar1.add(Varc2);

        jButton6.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/b_help.png"))); // NOI18N
        jButton6.setToolTipText("Aide");
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton6MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton6MouseEntered(evt);
            }
        });
        jToolBar1.add(jButton6);

        reboot.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        reboot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/s_reload.png"))); // NOI18N
        reboot.setToolTipText("Nouveau Graphe");
        reboot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rebootMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                rebootMouseEntered(evt);
            }
        });
        reboot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rebootActionPerformed(evt);
            }
        });
        jToolBar1.add(reboot);

        conteneur1.setRollover(true);

        pcc.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        pcc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/new_data.png"))); // NOI18N
        pcc.setText("Djisktra");
        pcc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pccMouseClicked(evt);
            }
        });
        conteneur1.add(pcc);

        pcc1.setFont(new java.awt.Font("Times New Roman", 1, 10)); // NOI18N
        pcc1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/s_theme.png"))); // NOI18N
        pcc1.setText("Colorier");
        pcc1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pcc1MouseClicked(evt);
            }
        });
        conteneur1.add(pcc1);

        jToolBar5.setRollover(true);

        jLabel6.setText("Moore de X à Y <>  ");
        jToolBar5.add(jLabel6);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 10)); // NOI18N
        jLabel4.setText("X : ");
        jToolBar5.add(jLabel4);

        X.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jToolBar5.add(X);

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 10)); // NOI18N
        jLabel5.setText("Y : ");
        jToolBar5.add(jLabel5);

        Y.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jToolBar5.add(Y);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/s_okay.png"))); // NOI18N
        jButton2.setText("Ok");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jToolBar5.add(jButton2);

        jToolBar6.setRollover(true);

        jButton7.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/new_struct.png"))); // NOI18N
        jButton7.setText("Sollin");
        jButton7.setToolTipText("Arbre couvrant de poids minimum");
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton7MouseClicked(evt);
            }
        });
        jToolBar6.add(jButton7);

        PPihm.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        PPihm.setText("    P.P    ");
        PPihm.setToolTipText("Parcours en profondeur du graphe");
        PPihm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PPihmMouseClicked(evt);
            }
        });
        jToolBar6.add(PPihm);

        PLihm.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        PLihm.setText("    P.L    ");
        PLihm.setToolTipText("Parcours en largeur du graphe");
        PLihm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PLihmMouseClicked(evt);
            }
        });
        jToolBar6.add(PLihm);

        Varc4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Varc4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/s_error2.png"))); // NOI18N
        Varc4.setToolTipText("Quitter Graph'ou");
        Varc4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Varc4MouseClicked(evt);
            }
        });
        jToolBar6.add(Varc4);

        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jToolBar6.add(jSeparator5);

        jLabel2.setText("   Nature du graphe : ");
        jToolBar6.add(jLabel2);

        nature.setBackground(new java.awt.Color(0, 153, 255));
        nature.setFont(new java.awt.Font("Times New Roman", 1, 10)); // NOI18N
        nature.setText("---");
        jToolBar6.add(nature);

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jToolBar6.add(jSeparator4);

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton1.setText("   +   ");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton3.setText("   -   ");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        cout_visible.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cout_visible.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        cout_visible.setText("1.0");
        cout_visible.setEnabled(false);
        jScrollPane1.setViewportView(cout_visible);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/b_search.png"))); // NOI18N

        Szone.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Szone.setToolTipText("Rechercher un sommet en entrant son nom ici");
        Szone.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                SzoneFocusLost(evt);
            }
        });
        Szone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SzoneKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                SzoneKeyReleased(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/s_reload.png"))); // NOI18N
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Sortie graphique", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        ecran.setEditable(false);
        ecran.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        ecran.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jScrollPane3.setViewportView(ecran);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1117, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );

        zone.setBackground(new java.awt.Color(204, 204, 204));
        zone.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        zone.setToolTipText("");
        zone.setAutoscrolls(true);
        zone.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        zone.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                zoneMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                zoneMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                zoneMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                zoneMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                zoneMouseReleased(evt);
            }
        });
        zone.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                zoneMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                zoneMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout zoneLayout = new javax.swing.GroupLayout(zone);
        zone.setLayout(zoneLayout);
        zoneLayout.setHorizontalGroup(
            zoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        zoneLayout.setVerticalGroup(
            zoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 504, Short.MAX_VALUE)
        );

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/globe.png"))); // NOI18N
        jMenu1.setText("Graphe");
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenu1MousePressed(evt);
            }
        });

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/b_tipp.png"))); // NOI18N
        jMenuItem1.setMnemonic('m');
        jMenuItem1.setText("Moore");
        jMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem1MousePressed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/s_theme.png"))); // NOI18N
        jMenuItem2.setMnemonic('p');
        jMenuItem2.setText("Coloriage");
        jMenuItem2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem2MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem2MousePressed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/s_info.png"))); // NOI18N
        jMenu3.setText("Sommet");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setBackground(new java.awt.Color(153, 204, 255));
        jMenuItem3.setText("Renommer");
        jMenuItem3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem3MousePressed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem8.setText("Nombre de chemin de longueur X");
        jMenuItem8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem8MousePressed(evt);
            }
        });
        jMenu3.add(jMenuItem8);

        jMenu1.add(jMenu3);

        jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem11.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jMenuItem11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/b_relations.png"))); // NOI18N
        jMenuItem11.setText("Distance");
        jMenuItem11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem11MousePressed(evt);
            }
        });
        jMenu1.add(jMenuItem11);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/b_save.png"))); // NOI18N
        jMenuItem6.setMnemonic('s');
        jMenuItem6.setText("Sauvegarder");
        jMenuItem6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem6MousePressed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem7.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/open~1.png"))); // NOI18N
        jMenuItem7.setMnemonic('o');
        jMenuItem7.setText("Restaurer");
        jMenuItem7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem7MousePressed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        changementnat.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        changementnat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/cool (2).png"))); // NOI18N
        changementnat.setText("Changer Nature");
        changementnat.setToolTipText("Changer la nature du graphe");
        changementnat.setEnabled(false);
        changementnat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                changementnatMousePressed(evt);
            }
        });
        jMenu1.add(changementnat);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/s_reload.png"))); // NOI18N
        jMenuItem5.setMnemonic('n');
        jMenuItem5.setText("Nouveau");
        jMenuItem5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem5MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem5MousePressed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem9.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/s_error2.png"))); // NOI18N
        jMenuItem9.setText("Quiter");
        jMenuItem9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem9MousePressed(evt);
            }
        });
        jMenu1.add(jMenuItem9);

        jMenuBar1.add(jMenu1);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/b_help.png"))); // NOI18N
        jMenu2.setText("Aide");

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuItem10.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/s_info.png"))); // NOI18N
        jMenuItem10.setText("Aide");
        jMenuItem10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem10MousePressed(evt);
            }
        });
        jMenu2.add(jMenuItem10);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(conteneur1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToolBar5, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jToolBar6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Szone, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3))))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(zone, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane1)))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(conteneur1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToolBar5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToolBar6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Szone, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(zone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rebootMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rebootMouseClicked
      try
      {
        String[] action = {"Réinitialiser la couleur des sommets", "Réinitialiser la couleur des arêtes","Remettre le graphe initial", "Relancer Graph'ou"};
           String nom = (String)JOptionPane.showInputDialog(null, 
          "Veuillez selectionner l'action à réaliser","Graph'ou reboot",
          JOptionPane.QUESTION_MESSAGE,null,action,action[0]);
          switch(nom)
            {
              case "Réinitialiser la couleur des sommets":
                     ;recherche.clear();init_sommet();graphe_arete();graphe_sommet();
                  break;
              case "Réinitialiser la couleur des arêtes":
                     ar.clear();graphe_arete();graphe_sommet();
                  break;
              case "Relancer Graph'ou":
                     setVisible(false);
                     String[] args = null;
                     (new Fgraphe()).main(args); 
                  break;    
              case "Remettre le graphe initial":
                           try
                            {
                                  if(!copieareteList.isEmpty() && copieareteList.size() == areteList.size()){
                                    for(int i=0;i<copieareteList.size();i++){
                                        effacer((Arete)areteList.get(i));
                                        ((Arete)areteList.get(i)).set_cout(((Arete)copieareteList.get(i)).get_cout());
                                        }
                                  }
                                  else{
                                      JOptionPane.showMessageDialog(null,"Impossible de restaurer les coûts précedent, le graphe a été modifié","Message d'avertissement",JOptionPane.ERROR_MESSAGE);
                                  }
                                      
                                graphe_arete();graphe_sommet();
                            }
                            catch(Exception e)
                            {
                              JOptionPane.showMessageDialog(null,e.toString(),"Message d'avertissement",JOptionPane.ERROR_MESSAGE);
                            }
                  break;
            }
      }
      catch (Exception ex) {
            
        }
    }//GEN-LAST:event_rebootMouseClicked

    private void zoneMouseClicked(java.awt.event.MouseEvent evt) 
	{//GEN-FIRST:event_zoneMouseClicked

	        coord_x = ( double ) evt.getX()-6;
	        coord_y = ( double ) evt.getY()+conteneur1.getHeight()*(4.2);

	    switch ( mouseStatus ) {
	    case waitForSommet : // on attend un sommet
		if(zone.getCursor()!=Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
                {
                 // création du sommet
		 cur_som = new Sommet( coord_x , coord_y , curIndexSommet++ ) ;
		 // ajout dans le vecteur de sommets
		 sommetList.add( (Object)cur_som ) ;
		 // création de la liste d'adjacence du sommet
		 sommetAdjList = new LinkedList() ;
		 // ajout à la liste d'adjacence du graphe
		 adjList.add( (Object)sommetAdjList ) ;
                 X.addItem(cur_som.get_numero());
                 Y.addItem(cur_som.get_numero());
                }
		break ;

	    case waitForDebutArete : // on attend un début d'arête
		if(zone.getCursor()!=Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
                {
		 found = false ; // indique si l'on a bien cliqué un sommet
		 // recherche du sommet cliqué s'il existe
		 for ( int i=0 ; i< sommetList.size() ; i++ ) {
		    cur_som = (Sommet)sommetList.get( i ) ;
		    if ( cur_som.contains( coord_x, coord_y ) ) {
			found = true ;
			indexStartSommet = i ;
			break ;
		    }
		}
		if ( found ) { // on a bien cliqué un sommet
		    mouseStatus = waitForFinArete ;
		    startSommet = cur_som ;
		}
                }
                break ;

	    case waitForFinArete : // on attend une fin d'arête
		if(zone.getCursor()!=Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
                {
		 found = false ; // indique si l'on a bien cliqué un sommet
		 // recherche du sommet cliqué s'il existe
		 for ( int i=0 ; i< sommetList.size() ; i++ ) {
		    cur_som = (Sommet)sommetList.get( i ) ;
		    if ( cur_som.contains( coord_x, coord_y ) ) {
			found = true ;
			indexEndSommet = i ;
			break ;
		    }
		}
		if ( found ) { // on a bien cliqué un sommet
		    mouseStatus = waitForDebutArete ;
		    endSommet = cur_som ;
		    // création de l'arête
		    cur_arete = new Arete( startSommet , endSommet , poids,"Or","first") ;
		    // ajout de l'arête à la liste d'adjacence
                    //------------------------------------------------------------------------------------
                    if(Existe(cur_arete) == -2)
                    {
		      sommetAdjList = (LinkedList)adjList.get( indexStartSommet ) ;
		      sommetAdjList.addLast( (Object) cur_arete ) ;
		      // ajout de l'arête à la liste des arêtes
		      areteList.add( (Object)cur_arete ) ;
                      // ajout de l'arete à la liste des aretes inversées
                      areteListInv.add( (Object)new Arete( endSommet, startSommet, poids,"Or","first"));
                    }
                    else
                    {  
                        if(JOptionPane.showConfirmDialog(null, "cet Arc existe déjà,"+"|"+cur_arete.get_debut().get_nom()+"| ---"+Existe(cur_arete)+"---> |"+cur_arete.get_fin().get_nom()+"|"+"\nvoulez vous remplacer l'ancien coût par : "+poids+"\n"
                                , "Graph'ou :: Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)== JOptionPane.OK_OPTION)
                         {
                           for(int y=0;y<areteList.size();y++)
                           {
                               Arete reza = (Arete)areteList.get(y);
                               if(reza.get_debut() == cur_arete.get_debut() && reza.get_fin() == cur_arete.get_fin())
                               {
                                   effacer(((Arete)areteList.get(y)));
                                   ((Arete)areteList.get(y)).set_cout(poids);
                                   JOptionPane.showMessageDialog(null,"Coût de l'arête modifié avec succès","Graph'ou :: succès",JOptionPane.INFORMATION_MESSAGE);
                                   break;
                               }
                           }
                           for(int y=0;y<areteListInv.size();y++)
                           {
                               Arete reza = (Arete)areteList.get(y);
                               if(reza.get_debut() == cur_arete.get_fin() && reza.get_fin() == cur_arete.get_debut())
                               {
                                   ((Arete)areteListInv.get(y)).set_cout(poids);
                                   break;
                               }
                           }
                         } 
                    }
                    //------------------------------------------------------------------------------------
                    try
                    {
                      if(!startSommet.get_forme().contains(endSommet.get_forme().getX(),endSommet.get_forme().getY())) {
                        ((ligne)linge.get(linge.size()-1)).draw2((Graphics2D)getGraphics());}
                    }
                    catch(Exception e)
                    {
                    }
                    linge.clear();
		}
		else {
                    ((ligne)linge.get(linge.size()-1)).draw2((Graphics2D)getGraphics());
                    linge.clear();     
		    mouseStatus = waitForDebutArete ;}
                }
            break ;
	    //***************************************************************************************************
	    case waitForDebutNoSens : // on attend un début d'arête pour le non orienté
		if(zone.getCursor()!=Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
                {
	         found = false ; // indique si l'on a bien cliqué un sommet
		 // recherche du sommet cliqué s'il existe
		 for ( int i=0 ; i< sommetList.size() ; i++ ) {
		    cur_som = (Sommet)sommetList.get( i ) ;
		    if ( cur_som.contains( coord_x, coord_y ) ) {
			found = true ;
			indexStartSommet = i ;
			break ;
		    }
		}
		if ( found ) { // on a bien cliqué un sommet
		    mouseStatus = waitForFinNoSens ;
		    startSommet = cur_som ;
		}
		else {
                    }
                }
                break ;
            //***************************************************************************
	    case waitForFinNoSens : // on attend une fin d'arête non oriené(double sens)
		if(zone.getCursor()!=Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
                {
		 found = false ; // indique si l'on a bien cliqué un sommet
		 // recherche du sommet cliqué s'il existe
		 for ( int i=0 ; i< sommetList.size() ; i++ ) {
		    cur_som = (Sommet)sommetList.get( i ) ;
		    if ( cur_som.contains( coord_x, coord_y ) ) {
			found = true ;
			indexEndSommet = i ;
			break ;
		    }
		}
		if ( found )
                { // on a bien cliqué un sommet
                    mouseStatus = waitForDebutNoSens ;
		    endSommet = cur_som ;
		    // création de l'arête
		    cur_arete = new Arete( startSommet , endSommet , poids,"Nor","first") ;
		    cur2_arete = new Arete(endSommet, startSommet, poids,"Nor","second");
		    // ajout de l'arête à la liste d'adjacence
                    //------------------------------------------------------------------------------------
                    if(Existe(cur_arete) == -2)
                    {
		      sommetAdjList = (LinkedList)adjList.get( indexStartSommet ) ;
		      sommetAdjList.addLast( (Object) cur_arete ) ;
		      // ajout de l'arête à la liste des arêtes
		      areteList.add( (Object)cur_arete ) ;
		      sommetAdjList = (LinkedList) adjList.get(indexEndSommet);
		      sommetAdjList.addLast((Object) cur2_arete);
		      areteList.add((Object)cur2_arete);
                    }
                    else
                    {
                        if(JOptionPane.showConfirmDialog(null, "cet Arc existe déjà,"+"|"+cur_arete.get_debut().get_nom()+"| ---"+Existe(cur_arete)+"---> |"+cur_arete.get_fin().get_nom()+"|"+"\nvoulez vous remplacer l'ancien coût par : "+poids+"\n"
                                , "Graph'ou :: Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)== JOptionPane.OK_OPTION)
                         {
                           for(int y=0;y<areteList.size();y++)
                           {
                               Arete reza = (Arete)areteList.get(y);
                               if((reza.get_debut() == cur_arete.get_debut() && reza.get_fin() == cur_arete.get_fin()) ||(reza.get_fin() == cur_arete.get_debut() && reza.get_debut() == cur_arete.get_fin()))
                               {
                                   effacer(((Arete)areteList.get(y)));
                                   ((Arete)areteList.get(y)).set_cout(poids);
                               }
                           }    
                           JOptionPane.showMessageDialog(null,"Coût de l'arête modifié avec succès","Graph'ou :: succès",JOptionPane.INFORMATION_MESSAGE);
                         } 
                    }
                    try
                    {
                      if(!startSommet.get_forme().contains(endSommet.get_forme().getX(),endSommet.get_forme().getY())) {
                        ((ligne)linge.get(linge.size()-1)).draw2((Graphics2D)getGraphics());}
                    }
                    catch(Exception e)
                    {
                    }
                    linge.clear();
		}
		else {
                    ((ligne)linge.get(linge.size()-1)).draw2((Graphics2D)getGraphics());
                    linge.clear();     
		    mouseStatus = waitForDebutNoSens ;
                }
                }
              break ;
		/*********************************************************/
            /**************************************************************/
            case -5 :
                if(zone.getCursor()== Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
                {
                    constitue_degre();
                    found = false ; // indique si l'on a bien cliqué un sommet
                    // recherche du sommet cliqué s'il existe
                    for ( int i=0 ; i< sommetList.size() ; i++ ) {
                       cur_som = (Sommet)sommetList.get( i ) ;
                       if ( cur_som.contains( coord_x, coord_y ) ) {
                           found = true ;
                           pointInitial = new Sommet(cur_som);
                           indexPointInitial = i;
                           indexEndSommet = i ;
                           String [] ti ={"Propriétés","Valeurs"};
                           GTodel model = new GTodel(cur_som.get_properties(), ti);
                           pte = new JTable(model);
                           scroll_tableau.setViewportView(pte);
                           pte.getColumn("Valeurs").setCellRenderer(new TableComponent());
                           break ;
                       }
                    }
                }
                renommage =found;
                break;
            /**************************************************************/

	    } // switch mouseStatus// dessin des arêtes

            graphe_arete();
            graphe_sommet();
            construit_l_arbre();
            if(renommage == true) {cur_som.draw3((Graphics2D)getGraphics());}
    }//GEN-LAST:event_zoneMouseClicked

    public void graphe_sommet()
    {
	// dessin des sommets
	if ( sommetList != null )
        {
	    for ( int i = 0 ; i<sommetList.size() ; i++ ) {
		Sommet som = (Sommet)sommetList.get(i) ;
                    som.draw( (Graphics2D)getGraphics()) ;
	    }
        }
        if(!recherche.isEmpty())
        {for ( int i = 0 ; i<recherche.size() ; i++ ) {
                ((Sommet)recherche.get(i)).draw_search( (Graphics2D)getGraphics()) ;
                
        }
        }
    }

    public void efface()
    {
        if ( areteList != null )
        {
	    for ( int i=0 ; i<areteList.size() ; i++ ) 
            {
		Arete arete = (Arete)areteList.get(i) ;
                if(type == 1)
                {
                  arete.draw2( (Graphics2D)getGraphics()) ;
                  if(A_inverse(arete))
                  {
                   if(Pos_inverse(arete)>i) {
                        arete.draw2_moins( (Graphics2D)getGraphics()) ;
                    }
                   else {
                        arete.draw2_plus( (Graphics2D)getGraphics()) ;
                    }
                  }
                  else {
                    arete.draw2( (Graphics2D)getGraphics()) ;
                  }
                }
                else {
                    arete.draw2( (Graphics2D)getGraphics()) ;
                }
	    }
        }
    }
    
    public void graphe_arete()
    {
	if ( areteList != null )
        {
	    for ( int i=0 ; i<areteList.size() ; i++ ) 
            {
		Arete arete = (Arete)areteList.get(i) ;
                if(type == 1)
                {
                  arete.draw2( (Graphics2D)getGraphics()) ;
                  if(A_inverse(arete))
                  {
                   if(Pos_inverse(arete)>i) {
                        arete.draw_moins( (Graphics2D)getGraphics()) ;
                    }
                   else {
                        arete.draw_plus( (Graphics2D)getGraphics()) ;
                    }
                  }
                  else {
                    arete.draw( (Graphics2D)getGraphics()) ;
                  }
                }
                else {
                    arete.draw( (Graphics2D)getGraphics()) ;
                }
	    }
            draw_road();
        }
    }
    
    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        poids++;
        cout_visible.setText(""+poids);
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
       if(poids>1) {
            poids--;
        } 
       else {
            poids=1;
        } 
       
       cout_visible.setText(""+poids);
    }//GEN-LAST:event_jButton3MouseClicked

    
    //renvoie de la matrice d'adjacence
    public int [][] Madjacence()
    {
        int[][] MAT = new int [sommetList.size()][sommetList.size()];
        int i,j;
        LinkedList sommetAdj;
        Sommet cur;
        
        for(int a=0;a<sommetList.size();a++)
        {
            for(int b=0;b<sommetList.size();b++) {
                MAT[a][b] = Graphe.ALPHA_NOTDEF;
            }  
        }
        
        for(i=0;i<sommetList.size();i++)
        {
             sommetAdj = (LinkedList)adjList.get(i);
             for(j=0;j<sommetAdj.size();j++)
             {
                 cur = iemeVoisin(i,j);
                 if(cur.get_numero() == j) {
                     MAT[i][j]=(int)W(i,cur);
                 }
                 else
                 {
                     if(cur.get_numero()<sommetList.size()) {
                         MAT[i][cur.get_numero()]=(int)W(i,cur);
                     }    
                 }
             }
        }
        return MAT;
    }
    
    public String matrice()
    {
        int [][] mate=Madjacence();
        String str="";
        for(int a=0;a<mate.length;a++)
        {
            for(int b=0;b<mate.length;b++)
            {
               if((mate[a][b])!=Graphe.ALPHA_NOTDEF) {
                    str +=" " + (mate[a][b]);
                }
               else {
                    str +=" " + "X";
                }
            }
            str+="\n";
        }
        return str;
    }
    
    private void validnatureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_validnatureMouseClicked
      if(validnature.isEnabled())
      {
        switch(choixnature.getSelectedIndex())
        {
            case 0 :
              typekandor = 1;
	      type = waitForDebutArete ;break;
            case 1 :
              typekandor = 2;
	      type = waitForDebutNoSens;break;
                
        }
          nature.setText((choixnature.getSelectedItem()).toString());
          validnature.setEnabled(false);
      }
    }//GEN-LAST:event_validnatureMouseClicked

    private void jButton6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseClicked
           jMenuItem10MousePressed(evt);
    }//GEN-LAST:event_jButton6MouseClicked

    private void jButton6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseEntered
 
    }//GEN-LAST:event_jButton6MouseEntered

    private void cancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelMouseClicked
      if(cancel.isEnabled())
      {
       ar.clear();
       if(areteList.size()>0)
       {
            if(type == waitForDebutNoSens)
            {
                
                for(int i=0;i<2;i++)
                {
                    try
                    {
                        Arete ar;
                        ar = (Arete) areteList.get(areteList.size()-1);
                        ar.draw2((Graphics2D)getGraphics());
                        Sommet k;
                        k=ar.get_debut();
                        sommetAdjList = (LinkedList)adjList.get(k.get_numero()) ;
                        sommetAdjList.remove(sommetAdjList.size() - 1);
                        try
                        {
                          areteList.remove((Arete)(areteList.get(areteList.size()-1)));
                          areteListInv.remove((Arete)areteListInv.get(areteListInv.size()-1));
                        }
                        catch (Exception e)
                        {}
                    }
                    catch (Exception e)
                    {
                        JOptionPane.showMessageDialog(null,e.toString(),"Message d'avertissement",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
           else
            {
             try
             {
              Arete ar;
              ar = (Arete) areteList.get(areteList.size()-1);
              effacer(ar);
              Sommet k;
              k=ar.get_debut();
              sommetAdjList = (LinkedList)adjList.get(k.get_numero()) ;
              sommetAdjList.remove(sommetAdjList.size() - 1);
              try
              {
                areteList.remove((Arete)(areteList.get(areteList.size()-1)));
                areteListInv.remove((Arete)areteListInv.get(areteListInv.size()-1));
              }
              catch (Exception e)
              {}
             }
             catch (Exception e)
             {
                 JOptionPane.showMessageDialog(null,e.toString(),"Message d'avertissement",JOptionPane.ERROR_MESSAGE);
             }
            }
       }
       else
       {
           if(sommetList.size()>0)
           {
            try
             {
               Sommet som;
               som = (Sommet)(sommetList.get(sommetList.size()-1));
               som.draw2((Graphics2D)getGraphics());
               X.removeItemAt(som.get_numero());
               Y.removeItemAt(som.get_numero());
               sommetList.remove((Sommet)(sommetList.get(sommetList.size()-1)));
               curIndexSommet--;
             }
            catch (Exception e)
            {
              JOptionPane.showMessageDialog(null,e.toString(),"Message d'avertissement",JOptionPane.ERROR_MESSAGE);
            }
           }
       }
       construit_l_arbre();
      }
       graphe_arete();
       graphe_sommet();
    }//GEN-LAST:event_cancelMouseClicked

    private void cancelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_cancelMouseEntered

    private void pccMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pccMouseClicked
       if(sommetList.size()>1 && areteList.size()>0)
       {
        try
        {
          Dijkstra Moore= new Dijkstra(0,new Graphe(Madjacence()));
          ecran.setText("Algo de Moore\n");
          ecran.setText(ecran.getText().concat("=============\n"));
          ecran.setText(ecran.getText().concat("Lg Mini de 0 à " + (sommetList.size() - 1) + " : "+ Moore.longueurChemin(sommetList.size() - 1)));
          ecran.setText(ecran.getText().concat("\n"+Moore.afficheChemin2(sommetList.size() - 1)));
          ArrayList P = new ArrayList(Moore.contenu_chemin(sommetList.size() - 1));
          ar.clear();
          graphe_arete();graphe_sommet();
          Arete cur;
          for(int j=0;j<P.size()-1;j++)
          {
              for(int i=0;i<areteList.size();i++)
              {
                  cur=(Arete)areteList.get(i);
                  if(cur.get_debut().get_numero()==(int)P.get(j) && cur.get_fin().get_numero()==(int)P.get(j+1))
                  {
                      ar.add(cur);
                      break;
                  }
              }
          }
          if(ar.size()>0)
          {
                for(int i=0;i<areteList.size();i++) {
                  effacer((Arete)areteList.get(i));
              }
              graphe_sommet();
                for(int i=0;i<ar.size();i++) {
                    Arete are = (Arete)ar.get(i); 
                    if(A_inverse(are) && type==1)
                    {
                        if(Ma_pos(are)<Pos_inverse(are)) {
                            are.draw3_moins((Graphics2D)getGraphics());
                        }
                        else {
                            are.draw3_plus((Graphics2D)getGraphics());
                        }
                    }
                    else {
                        are.draw3((Graphics2D)getGraphics());
                    }
                }
          }
          graphe_sommet();
          zoneMouseEntered(evt);
       }
       catch (Exception e){ecran.setText("Longueur Minimale\n de 0 à " + (sommetList.size() - 1) + " : Infini");}
       }
       else
        {
           JOptionPane.showMessageDialog(null,"Le graphe doit contenir au moins\n- 2 sommets.\n- 1 arête. !","GRAPH'OU::Message d'erreur",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_pccMouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        
       if(sommetList.size()>1 && areteList.size()>0)
        {
         try
         {
          Dijkstra Moore= new Dijkstra(X.getSelectedIndex(),new Graphe(Madjacence()));
          ecran.setText("Algo de Moore\n");
          ecran.setText(ecran.getText().concat("=============\n"));
          ecran.setText(ecran.getText().concat("Lg Mini de "+X.getSelectedIndex()+" à " + Y.getSelectedIndex() + " : "+ Moore.longueurChemin(Y.getSelectedIndex())));
          ecran.setText(ecran.getText().concat("\n"+Moore.afficheChemin2(Y.getSelectedIndex())));
          ArrayList P = new ArrayList(Moore.contenu_chemin(Y.getSelectedIndex()));
          ar.clear();
          graphe_arete();graphe_sommet();
          Arete cur;
          for(int j=0;j<P.size()-1;j++)
          {
              for(int i=0;i<areteList.size();i++)
              {
                  cur=(Arete)areteList.get(i);
                  if(cur.get_debut().get_numero()==(int)P.get(j) && cur.get_fin().get_numero()==(int)P.get(j+1))
                  {
                      ar.add(cur);
                      break;
                  }
              }
          }
          if(ar.size()>0)
          {
                for(int i=0;i<areteList.size();i++) {
                  effacer((Arete)areteList.get(i));
              }
              graphe_sommet();
                for(int i=0;i<ar.size();i++) {
                    Arete are = (Arete)ar.get(i); 
                    if(A_inverse(are) && type == 1)
                    {
                        if(Ma_pos(are)<Pos_inverse(are)) {
                            are.draw3_moins((Graphics2D)getGraphics());
                        }
                        else {
                            are.draw3_plus((Graphics2D)getGraphics());
                        }
                    }
                    else {
                        are.draw3((Graphics2D)getGraphics());
                    }
                }
          }
          graphe_sommet();
          zoneMouseEntered(evt);
         }
          catch (Exception e){ecran.setText("Longueur Minimale\n de "+X.getSelectedIndex()+" à " + Y.getSelectedIndex() + " : Infini");}
        }
       else
          {
             JOptionPane.showMessageDialog(null,"Le graphe doit contenir au moins\n- 2 sommets.\n- 1 arête. !","GRAPH'OU::Message d'erreur",JOptionPane.ERROR_MESSAGE);
          }
    }//GEN-LAST:event_jButton2MouseClicked

    private void pcc1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pcc1MouseClicked
       try
       {
         if(sommetList.size()>1 && areteList.size()>0)
         {
            monInstance.Coloriage();
            pcc1.setText("Changer de couleurs");
            ecran.setText("Algo du Coloriage\n==============\n"+"\nNombre chromatique\ndu graphe : "+chromatique);
            zoneMouseEntered(evt);
         }
         else
         {
            JOptionPane.showMessageDialog(null,"Le graphe doit contenir au moins\n- 2 sommets.\n- 1 arête. !","GRAPH'OU::Message d'erreur",JOptionPane.ERROR_MESSAGE);
         }
       }
       catch (Exception e)
       {
           JOptionPane.showMessageDialog(null,e.toString(),"GRAPH'OU::Erreur",JOptionPane.ERROR_MESSAGE);
       }
    }//GEN-LAST:event_pcc1MouseClicked

    private void zoneMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_zoneMouseEntered
        if(zone.getCursor() == Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)) {
            VsommetFocusGained(null);
        }
        if(zone.getCursor() == Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)) {
            VarcFocusGained(null);
        }
        if(zone.getCursor() == Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)) {
            VpointeurMouseClicked(evt);
        }   
          graphe_arete();graphe_sommet();
    }//GEN-LAST:event_zoneMouseEntered

    private void zoneMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_zoneMouseMoved
       if(mouseStatus == waitForFinArete || mouseStatus == waitForFinNoSens)
       {
	        coord_x = ( double ) evt.getX()-6;
	        coord_y = ( double ) evt.getY()+conteneur1.getHeight()*(4.2);
          /*
           debut.get_forme()).getX() + (debut.get_forme()).getWidth()/2.0 ,
			   (debut.get_forme().getY()) + (debut.get_forme()).getHeight()/2.0
           */
          ligne l1 = new ligne((int)(cur_som.get_forme().getX()+(cur_som.get_forme()).getWidth()/2.0),(int)(cur_som.get_forme().getY()+(cur_som.get_forme()).getHeight()/2.0),(int)coord_x,(int)coord_y);
          linge.add((Object)l1);
          for(int i=0;i<linge.size()-1;i++) 
          {
               ((ligne)linge.get(i)).draw2((Graphics2D)getGraphics());graphe_sommet();
          }
          ((ligne)linge.get(linge.size()-1)).draw((Graphics2D)getGraphics());
       } 
    }//GEN-LAST:event_zoneMouseMoved

    
    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        
        
    }//GEN-LAST:event_formKeyPressed

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped
       switch(evt.getKeyChar())
        {
            case '+':                                      
                      poids++;
                      cout_visible.setText(""+poids);break;
            case '-':
                      if(poids>1) {
            poids--;
        } 
                      else {
            poids=1;
        }break;
        } 
       cout_visible.setText(""+poids); 
    }//GEN-LAST:event_formKeyTyped

    private void VsommetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_VsommetMouseClicked
     if(Vsommet.isEnabled()){
        if(type!=-5)
       {
        mouseStatus = waitForSommet;
        zone.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Varc.setBackground(new Color(240,240,240));
       }
       else {
            JOptionPane.showMessageDialog(null,"veuillez choisir la nature du graphe","GRAPH'OU::Message d'avertissement",JOptionPane.ERROR_MESSAGE);
        }}
    }//GEN-LAST:event_VsommetMouseClicked

    private void VarcMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_VarcMouseClicked
     if(Varc.isEnabled()){
        if(type!=-5)
       {
         if(type == waitForDebutArete) {
               mouseStatus = waitForDebutArete;
           }
         else
         {
             if(type == waitForDebutNoSens) {
                 mouseStatus = waitForDebutNoSens;
             }
         }
         zone.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
         Vsommet.setBackground(new Color(240,240,240));
       }
       else {
            JOptionPane.showMessageDialog(null,"Veuillez choisir la nature du graphe !","GRAPH'OU::Message d'avertissement",JOptionPane.ERROR_MESSAGE);
        }
     }
    }//GEN-LAST:event_VarcMouseClicked

    private void VpointeurMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_VpointeurMouseClicked
     if(Vpointeur.isEnabled()){
        zone.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        mouseStatus = -5;
        VsommetFocusLost(null);
        VarcFocusLost(null);}
    }//GEN-LAST:event_VpointeurMouseClicked

    @SuppressWarnings("empty-statement")
    private void Varc1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Varc1MouseClicked
       Sauvegarde_graphe_dynamique();
       zoneMouseEntered(evt);
    }//GEN-LAST:event_Varc1MouseClicked
    
    private void Sauvegarde_graphe_dynamique()
    {if(sommetList.size()>1 && areteList.size()>0)
     { //sauvegarde du fichier
       JFileChooser choix = new JFileChooser();
       choix.setDialogTitle("GRAP'OU :: Sauvegarde du Graphe");
       Date dateFormat = new Date();
       choix.setSelectedFile(new File("Graphe"+dateFormat.getHours()+""+dateFormat.getMinutes()+""+dateFormat.getSeconds()+(".kmk")));
       choix.addChoosableFileFilter(filtre);
        if(choix.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
        {
          File fichier = choix.getSelectedFile(),fichier2;
          
          if(!fichier.getName().endsWith(".kmk"))
          {
              fichier2 = new File(fichier.getPath().concat(".kmk"));
          }
          else
          {
              fichier2 = fichier;
          }
          if(choix.getFileFilter().accept(fichier2))
          {
              Gfile Gxml = new Gfile(type,sommetList,areteList,fichier2.toString());
              Gxml.construct();
              (new resto(this,true,"Sauvegarde de "+fichier2.getName()+" en cours...")).ShowDialog();
              JOptionPane.showMessageDialog(null, "Graphe sauvegardé avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
              this.setTitle("Graph'ou :: "+fichier2.getName());
          }
          else
          {
           JOptionPane.showMessageDialog(null, "Erreur d'extension de fichier !\nVotre sauvegarde a échoué !", "Erreur", JOptionPane.ERROR_MESSAGE);
          }
        }
     }
     else {
            JOptionPane.showMessageDialog(null, "Le graphe ne possède pas assez de sommets et d'arcs !\nVotre sauvegarde a échoué !", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void Varc2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Varc2MouseClicked
             restauration_graphe_dynamique();
             zoneMouseEntered(evt);
             VpointeurMouseClicked(evt);
             typekandor = type;
             construit_l_arbre();
    }//GEN-LAST:event_Varc2MouseClicked

 private void restauration_graphe_dynamique()
 {    //ouverture du fichier de graphe
   if(sommetList.size()>1 && areteList.size()>0)
   {
       if(JOptionPane.showConfirmDialog(null, "Voulez-vous sauvegarder le graphe courant?", "Sauvegarde du graphe",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)!= JOptionPane.OK_OPTION)
       {
       try
       {
        JFileChooser choix = new JFileChooser();
        File fichier;
        choix.setDialogTitle("GRAP'OU :: Restauration du Graphe");
        choix.setMultiSelectionEnabled(false);
        choix.addChoosableFileFilter(filtre);
        if(choix.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
          fichier = choix.getSelectedFile();
          if(fichier.getName().endsWith(".kmk"))
          {
            Petit Mince = ((new Gfile(fichier.toString())).restor_graph());
            for(int ii=0;ii<sommetList.size();ii++) {
                  ((Sommet)sommetList.get(ii)).draw2((Graphics2D)getGraphics());
              }
            for(int ii=0;ii<areteList.size();ii++) {
                  effacer((Arete)areteList.get(ii));
              }
            sommetList.clear();
            adjList.clear();
            areteList.clear();
            areteListInv.clear();
            X.removeAllItems();
            Y.removeAllItems();
            curIndexSommet = Mince.getNombre();
            ar.clear();
            recherche.clear();
            changementnat.setEnabled(false);
            switch(Mince.getType_graphe())
            {
                case 1 :
                {
                    choixnature.setSelectedIndex(1);
                    choixnature.setSelectedItem("Orienté");
                    nature.setText("Orienté");
                    pcc1.setText("Colorier");
                    type = 1;
                    validnature.setEnabled(false);
                    ecran.setText("");
                    sommetList = Mince.getSommlist();
                    areteList = Mince.getArelist();
                    changementnat.setEnabled(true);
                    for(int i=0;i<sommetList.size();i++)
                    {
		      sommetAdjList = new LinkedList();
		      adjList.add( (Object)sommetAdjList ); 
                      X.addItem(i);
                      Y.addItem(i);
                    }
                    for(int i=0;i<areteList.size();i++)
                    {
                      cur_arete = (Arete)areteList.get(i);
		      sommetAdjList = (LinkedList)adjList.get(cur_arete.get_debut().get_numero());
		      sommetAdjList.addLast( (Object) cur_arete ) ;
                      // ajout de l'arete à la liste des aretes inversées
                      areteListInv.add( (Object)new Arete(cur_arete.get_fin(),cur_arete.get_debut(),cur_arete.get_cout(),"Or","first"));
                    }
                }break;
                case 2 :
                {
                    choixnature.setSelectedIndex(1);
                    choixnature.setSelectedItem("Non orienté");
                    nature.setText("Non orienté");
                    ecran.setText("");
                    pcc1.setText("Colorier");
                    type = 3;
                    validnature.setEnabled(false);
                    sommetList = Mince.getSommlist();
                    areteList = Mince.getArelist();
                    for(int i=0;i<sommetList.size();i++)
                    {
		      sommetAdjList = new LinkedList();
		      adjList.add( (Object)sommetAdjList ); 
                      X.addItem(i);
                      Y.addItem(i);  
                    }
                    for(int i=0;i<areteList.size();i++)
                    {
                      cur_arete = (Arete)areteList.get(i);
		      sommetAdjList = (LinkedList)adjList.get(cur_arete.get_debut().get_numero());
		      sommetAdjList.addLast( (Object) cur_arete ) ;
                      // ajout de l'arete à la liste des aretes inversées
                      areteListInv.add( (Object)new Arete(cur_arete.get_fin(),cur_arete.get_debut(),cur_arete.get_cout(),"Or","first"));
                    }  
                }break;
            }
            (new resto(this,true,"Restauration de "+fichier.getName()+" en cours...")).ShowDialog();
            this.setTitle("Graph'ou :: "+fichier.getName());
            graphe_arete();
            graphe_sommet();
          }
          else
          {
           JOptionPane.showMessageDialog(null, "Erreur d'extension de fichier !\nVotre restauration a échoué !", "Erreur", JOptionPane.ERROR_MESSAGE);
          }
        }
       }
      catch(Exception e)
        {
          JOptionPane.showMessageDialog(null, "Erreur d'extension de fichier !\nVotre restauration a échoué !", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
       }
      else
       {
           Sauvegarde_graphe_dynamique();//on sauvegarde le graphe courant
           restor_cool();//on restaure le graphe choisit
       }
   }
   else
   {
       try
       {
        JFileChooser choix = new JFileChooser();
        File fichier;
        choix.setDialogTitle("GRAP'OU :: Restauration du Graphe");
        choix.setMultiSelectionEnabled(false);
        choix.addChoosableFileFilter(filtre);
        if(choix.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
          fichier = choix.getSelectedFile();
          if(fichier.getName().endsWith(".kmk"))
          {
            Petit Mince = ((new Gfile(fichier.toString())).restor_graph());
            for(int ii=0;ii<sommetList.size();ii++) {
                  ((Sommet)sommetList.get(ii)).draw2((Graphics2D)getGraphics());
              }
            for(int ii=0;ii<areteList.size();ii++) {
                  effacer((Arete)areteList.get(ii));
              }
            sommetList.clear();
            adjList.clear();
            areteList.clear();
            areteListInv.clear();
            curIndexSommet = Mince.getNombre();
            X.removeAllItems();
            Y.removeAllItems();
            ar.clear();
            recherche.clear();
            changementnat.setEnabled(false);
            switch(Mince.getType_graphe())
            {
                case 1 :
                {
                    choixnature.setSelectedIndex(0);
                    choixnature.setSelectedItem("Orienté");
                    nature.setText("Orienté");
                    ecran.setText("");
                    pcc1.setText("Colorier");
                    type = 1;
                    validnature.setEnabled(false);
                    sommetList = Mince.getSommlist();
                    areteList = Mince.getArelist();
                    changementnat.setEnabled(true);
                    for(int i=0;i<sommetList.size();i++)
                    {
		      sommetAdjList = new LinkedList();
		      adjList.add( (Object)sommetAdjList ); 
                      X.addItem(i);
                      Y.addItem(i);  
                    }
                    for(int i=0;i<areteList.size();i++)
                    {
                      cur_arete = (Arete)areteList.get(i);
		      sommetAdjList = (LinkedList)adjList.get(cur_arete.get_debut().get_numero());
		      sommetAdjList.addLast( (Object) cur_arete ) ;
                      // ajout de l'arete à la liste des aretes inversées
                      areteListInv.add( (Object)new Arete(cur_arete.get_fin(),cur_arete.get_debut(),cur_arete.get_cout(),"Or","first"));
                    }
                }break;
                case 2 :
                {
                    choixnature.setSelectedIndex(1);
                    choixnature.setSelectedItem("Non orienté");
                    nature.setText("Non orienté");
                    ecran.setText("");
                    pcc1.setText("Colorier");
                    type = 0b11;
                    validnature.setEnabled(false);
                    sommetList = Mince.getSommlist();
                    areteList = Mince.getArelist();
                    for(int i=0;i<sommetList.size();i++)
                    {
		      sommetAdjList = new LinkedList();
		      adjList.add( (Object)sommetAdjList ); 
                      X.addItem(i);
                      Y.addItem(i);  
                    }
                    for(int i=0;i<areteList.size();i++)
                    {
                      cur_arete = (Arete)areteList.get(i);
		      sommetAdjList = (LinkedList)adjList.get(cur_arete.get_debut().get_numero());
		      sommetAdjList.addLast( (Object) cur_arete ) ;
                      // ajout de l'arete à la liste des aretes inversées
                      areteListInv.add( (Object)new Arete(cur_arete.get_fin(),cur_arete.get_debut(),cur_arete.get_cout(),"Or","first"));
                    }   
                }break;
            }
            (new resto(this,true,"Restauration de "+fichier.getName()+" en cours...")).ShowDialog();
            this.setTitle("Graph'ou :: "+fichier.getName());
            graphe_arete();
            graphe_sommet();
          }
          else
          {
           JOptionPane.showMessageDialog(null, "Erreur d'extension de fichier !\nVotre restauration a échoué !", "Erreur", JOptionPane.ERROR_MESSAGE);
          }
        }    
      }
      catch(Exception e)
      {
          JOptionPane.showMessageDialog(null, "Erreur d'extension de fichier !\nVotre restauration a échoué !", "Erreur", JOptionPane.ERROR_MESSAGE);
      }
   }
 }
 
 private void restor_cool()
 {
       try
       {
        JFileChooser choix = new JFileChooser();
        File fichier;
        choix.setDialogTitle("GRAP'OU :: Restauration du Graphe");
        choix.setMultiSelectionEnabled(false);
        choix.addChoosableFileFilter(filtre);
        if(choix.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
          fichier = choix.getSelectedFile();
          if(fichier.getName().endsWith(".kmk"))
          {
            Petit Mince = ((new Gfile(fichier.toString())).restor_graph());
            for(int ii=0;ii<sommetList.size();ii++) {
                  ((Sommet)sommetList.get(ii)).draw2((Graphics2D)getGraphics());
              }
            for(int ii=0;ii<areteList.size();ii++) {
                  effacer((Arete)areteList.get(ii));
              }
            sommetList.clear();
            adjList.clear();
            areteList.clear();
            areteListInv.clear();
            X.removeAllItems();
            Y.removeAllItems();
            ecran.setText("");
            ar.clear();
            recherche.clear();
            curIndexSommet = Mince.getNombre();
            changementnat.setEnabled(false);
            switch(Mince.getType_graphe())
            {
                case 1 :
                {
                    choixnature.setSelectedIndex(1);
                    choixnature.setSelectedItem("Orienté");
                    nature.setText("Orienté");
                    ecran.setText("");
                    type = 1;
                    validnature.setEnabled(false);
                    sommetList = Mince.getSommlist();
                    areteList = Mince.getArelist();
                    changementnat.setEnabled(true);
                    for(int i=0;i<sommetList.size();i++)
                    {
		      sommetAdjList = new LinkedList();
		      adjList.add( (Object)sommetAdjList ); 
                      X.addItem(i);
                      Y.addItem(i);
                    }
                    for(int i=0;i<areteList.size();i++)
                    {
                      cur_arete = (Arete)areteList.get(i);
		      sommetAdjList = (LinkedList)adjList.get(cur_arete.get_debut().get_numero());
		      sommetAdjList.addLast( (Object) cur_arete ) ;
                      // ajout de l'arete à la liste des aretes inversées
                      areteListInv.add( (Object)new Arete(cur_arete.get_fin(),cur_arete.get_debut(),cur_arete.get_cout(),"Or","first"));
                    }
                }break;
                case 2 :
                {
                    choixnature.setSelectedIndex(1);
                    choixnature.setSelectedItem("Non orienté");
                    nature.setText("Non orienté");
                    ecran.setText("");
                    pcc1.setText("Colorier");
                    type = 3;
                    validnature.setEnabled(false);
                    sommetList = Mince.getSommlist();
                    areteList = Mince.getArelist();
                    for(int i=0;i<sommetList.size();i++)
                    {
		      sommetAdjList = new LinkedList();
		      adjList.add( (Object)sommetAdjList ); 
                      X.addItem(i);
                      Y.addItem(i);  
                    }
                    for(int i=0;i<areteList.size();i++)
                    {
                      cur_arete = (Arete)areteList.get(i);
		      sommetAdjList = (LinkedList)adjList.get(cur_arete.get_debut().get_numero());
		      sommetAdjList.addLast( (Object) cur_arete ) ;
                      // ajout de l'arete à la liste des aretes inversées
                      areteListInv.add( (Object)new Arete(cur_arete.get_fin(),cur_arete.get_debut(),cur_arete.get_cout(),"Or","first"));
                    }  
                }break;
            }
            (new resto(this,true,"Restauration de "+fichier.getName()+" en cours...")).ShowDialog();
            this.setTitle("Graph'ou :: "+fichier.getName());
            graphe_arete();
            graphe_sommet();
          }
          else
          {
           JOptionPane.showMessageDialog(null, "Erreur d'extension de fichier !\nVotre restauration a échoué !", "Erreur", JOptionPane.ERROR_MESSAGE);
          }
        }
       }
      catch(Exception e)
      {
          JOptionPane.showMessageDialog(null, "Erreur d'extension de fichier !\nVotre restauration a échoué !", "Erreur", JOptionPane.ERROR_MESSAGE);
      }
 }
 
 private boolean A_inverse(Arete ar)
 {
     Arete re = new Arete(ar.get_fin(),ar.get_debut(),ar.get_cout(), ar.get_nature(),ar.get_nature2());
     Arete ro;
     for(int i=0;i<areteList.size();i++)
     {
         ro = (Arete)areteList.get(i);
         if( (ro.get_debut() == re.get_debut()) && (ro.get_fin() == re.get_fin())) {
             return true;
         }
     }
     return false;
 }
 private boolean A_inverse2(Arete ar)
 {
     Arete re = new Arete(ar.get_fin(),ar.get_debut(),ar.get_cout(), ar.get_nature(),ar.get_nature2());
     Arete ro;
     for(int i=0;i<copie.size();i++)
     {
         ro = (Arete)copie.get(i);
         if( (ro.get_debut() == re.get_debut()) && (ro.get_fin() == re.get_fin())) {
             return true;
         }
     }
     return false;
 }
 
 private Double Poids_inverse(Arete ar)
 {
     Arete re = new Arete(ar.get_fin(),ar.get_debut(),ar.get_cout(), ar.get_nature(),ar.get_nature2());
     Arete ro;
     for(int i=0;i<copie.size();i++)
     {
         ro = (Arete)copie.get(i);
         if( (ro.get_debut() == re.get_debut()) && (ro.get_fin() == re.get_fin())) {
             return ro.get_cout();
         }
     }
     return -5.0;
 }
 
 private double Existe(Arete ar)
 {
     Arete re = new Arete(ar.get_debut(),ar.get_fin(),ar.get_cout(), ar.get_nature(),ar.get_nature2());
     Arete ro;
     for(int i=0;i<areteList.size();i++)
     {
         ro = (Arete)areteList.get(i);
         if( (ro.get_debut() == re.get_debut()) && (ro.get_fin() == re.get_fin())) {
             return ro.get_cout();
         }
     }
     return -2;
 }
 
 private int Pos_inverse(Arete ar)
 {
     int k=-2;
     Arete cur = new Arete(ar.get_fin(),ar.get_debut(),ar.get_cout(), ar.get_nature(),ar.get_nature2());
     for(int i=0;i<areteList.size();i++)
     {
         Arete cura = (Arete)areteList.get(i);
         if((cura.get_debut() == cur.get_debut()) && (cura.get_fin() == cur.get_fin()))
         {
           k=i;break;
         }
     }
     return k;
 }
 private int Pos_inverse2(Arete ar)
 {
     int k=-2;
     Arete cur = new Arete(ar.get_fin(),ar.get_debut(),ar.get_cout(), ar.get_nature(),ar.get_nature2());
     for(int i=0;i<copie.size();i++)
     {
         Arete cura = (Arete)copie.get(i);
         if((cura.get_debut() == cur.get_debut()) && (cura.get_fin() == cur.get_fin()))
         {
           k=i;break;
         }
     }
     return k;
 }
 
    private void VarcFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_VarcFocusGained
        Varc.setBackground(Color.YELLOW);
    }//GEN-LAST:event_VarcFocusGained

    private void VarcFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_VarcFocusLost
        Varc.setBackground(new Color(240,240,240));
    }//GEN-LAST:event_VarcFocusLost

    private void VsommetFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_VsommetFocusGained
        // TODO add your handling code here:
        Vsommet.setBackground(Color.YELLOW);
    }//GEN-LAST:event_VsommetFocusGained

    private void VsommetFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_VsommetFocusLost
        // TODO add your handling code here:
        Vsommet.setBackground(new Color(240,240,240));
    }//GEN-LAST:event_VsommetFocusLost

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        if(renommage == true)
        {
            String mince = JOptionPane.showInputDialog (this, "Entrez le nouveau nom du sommet <<"+cur_som.get_nom()+">>") ;
            if(mince != null)
            {
                cur_som.draw2((Graphics2D)getGraphics());
                cur_som.set_nom(mince);
                graphe_arete();graphe_sommet();
            }   
        }
        else
        {     JOptionPane.showMessageDialog(null,"Veuillez cliquez dans un sommet pour le renommer !","GRAPH'OU::Message d'avertissement",JOptionPane.ERROR_MESSAGE);   
        
                graphe_arete();graphe_sommet();}
                renommage = false;
    }//GEN-LAST:event_jButton4MouseClicked

    
    public Object[][] get_properties_sommets()
    {
        int deg=0;
        constitue_degre();
        for(int $i=0;$i<sommetList.size();$i++){
        deg+=((Sommet)sommetList.get($i)).get_Degre();
    }
        JButton b1 = new JButton("Sommet"),
                b2 = new JButton(""+sommetList.size()),
                b3 = new JButton(""+deg);
                b1.setBackground(new Color(0,204,255));
                b1.setForeground(Color.BLACK);
                b2.setBackground(new Color(0,204,255));
                b2.setForeground(Color.BLACK);
                b3.setBackground(new Color(0,204,255));
                b3.setForeground(Color.BLACK);
        Object[][] Op = {{"Nature",b1},{"Total",b2},{"Somme dégré",b3}};
        return Op;
    }
    
    
    public Object[][] get_properties_graphe()
    {
        Double deg=0.0;
        int deg2=0;
        constitue_degre();
        for(int $i=0;$i<areteList.size();$i++){
        deg+=((Arete)areteList.get($i)).get_cout();
    }
        for(int $i=0;$i<sommetList.size();$i++){
        deg2+=((Sommet)sommetList.get($i)).get_Degre();
    }
        JButton b1,
                b2 = new JButton(""+areteList.size()),
                b3 = new JButton(""+deg),
                b5 = new JButton(""+deg2),
                b4 = new JButton(""+sommetList.size()),
                b0 = new JButton("Graphe");
                if(typekandor == 1){
                    b1 = new JButton("Orienté");
                }
                else{
                    b1 = new JButton("Non-rienté");
                }
                b1.setBackground(new Color(0,204,255));
                b1.setForeground(Color.BLACK);
                b5.setBackground(new Color(0,204,255));
                b5.setForeground(Color.BLACK);
                b4.setBackground(new Color(0,204,255));
                b4.setForeground(Color.BLACK);
                b0.setBackground(new Color(0,204,255));
                b0.setForeground(Color.BLACK);
                b2.setBackground(new Color(0,204,255));
                b2.setForeground(Color.BLACK);
                b3.setBackground(new Color(0,204,255));
                b3.setForeground(Color.BLACK);
        Object[][] Op = {{"Objet",b0},{"Nature",b1},{"Ordre",b4},{"Total-Arête",b2},{"Coût total",b3},{"Dégré",b5}};
        return Op;
    }
    public Object[][] get_properties_arcs()
    {
        Double deg=0.0;
        constitue_degre();
        for(int $i=0;$i<areteList.size();$i++){
        deg+=((Arete)areteList.get($i)).get_cout();
    }
        JButton b1 = new JButton("Arete"),
                b2 = new JButton(""+areteList.size()),
                b3 = new JButton(""+deg);
                b1.setBackground(new Color(0,204,255));
                b1.setForeground(Color.BLACK);
                b2.setBackground(new Color(0,204,255));
                b2.setForeground(Color.BLACK);
                b3.setBackground(new Color(0,204,255));
                b3.setForeground(Color.BLACK);
        Object[][] Op = {{"Nature",b1},{"Total",b2},{"Coût total",b3}};
        return Op;
    }
    
    private void zoneMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_zoneMousePressed
            if(zone.getCursor()== Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
                {
	        coord_x = ( double ) evt.getX()-6;
	        coord_y = ( double ) evt.getY()+conteneur1.getHeight()*(4.2);
                    found = false ; // indique si l'on a bien cliqué un sommet
                    // recherche du sommet cliqué s'il existe
                    for ( int i=0 ; i< sommetList.size() ; i++ ) {
                       cur_som = (Sommet)sommetList.get( i ) ;
                       if ( cur_som.contains( coord_x, coord_y ) ) {
                           found = true ;
                           indexEndSommet = i ;
                           break ;
                       }
                    }
                }
                deplacement =found;
    }//GEN-LAST:event_zoneMousePressed

   
 /**
* Méthode qui permet de créer des modèles d'affichage
*/
 private void  initRenderer()
{
      //Instanciation
      this.tCellRenderer[0] = new  DefaultTreeCellRenderer();
      //Initialisation des images pour les actions fermer, ouvrir et pour les feuilles
      this.tCellRenderer[0].setClosedIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/comp1.gif")));
      this.tCellRenderer[0].setOpenIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/comp2.gif")));
      this.tCellRenderer[0].setLeafIcon(new javax.swing.ImageIcon(getClass().getResource("/Graphou/comp3.gif")));
 }
    
    private void zoneMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_zoneMouseDragged
        if((zone.getCursor()).equals(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)) && deplacement == true)
        {
            if(zone.contains(new Point((int)(evt.getX()),(int)(evt.getY()))))
            {
                if(type != 1)
                {
                 for(int i=0;i<areteList.size();i++)
                 {
                    if(((Arete)areteList.get(i)).get_debut() == cur_som || ((Arete)areteList.get(i)).get_fin() == cur_som) {
                        ((Arete)areteList.get(i)).draw2((Graphics2D)getGraphics());
                    }
                 }
                }
               else
                {
                 for(int i=0;i<areteList.size();i++)
                 {
                    if(((Arete)areteList.get(i)).get_debut() == cur_som || ((Arete)areteList.get(i)).get_fin() == cur_som) {
                        {
                            if(A_inverse((Arete)areteList.get(i)))
                            {
                                ((Arete)areteList.get(i)).draw2_moins((Graphics2D)getGraphics());
                                ((Arete)areteList.get(i)).draw2_plus((Graphics2D)getGraphics());
                            }
                            else{
                                ((Arete)areteList.get(i)).draw2((Graphics2D)getGraphics());
                            }
                        }
                    }
                 }
                }
	        coord_x = ( double ) evt.getX()-6;
	        coord_y = ( double ) evt.getY()+conteneur1.getHeight()*(4.2);
                cur_som.draw2((Graphics2D)getGraphics());
                cur_som.set_forme(new Ellipse2D.Double(coord_x, coord_y,Sommet.diametre,Sommet.diametre));
                cur_som.draw((Graphics2D)getGraphics());
                if(deplacement == true) {cur_som.draw3((Graphics2D)getGraphics());}
                graphe_arete();graphe_sommet();
            }
        }
    }//GEN-LAST:event_zoneMouseDragged

    private Arete Get_inverse(Arete ar)
    {
            return ((Arete)areteList.get(Pos_inverse(ar)));
    }
    
    private void effacer(Arete ar)
    {
        if(type == 1)
        {
                            if(A_inverse(ar))
                            {
                                ar.draw2_moins((Graphics2D)getGraphics());
                                ar.draw2_plus((Graphics2D)getGraphics());
                                Get_inverse(ar).draw2_moins((Graphics2D)getGraphics());
                                Get_inverse(ar).draw2_plus((Graphics2D)getGraphics());
                            }
                            else{
                                ar.draw2((Graphics2D)getGraphics());
                            }
        }
        else
        {
            ar.draw2((Graphics2D)getGraphics());
        }
    }
    
    private void zoneMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_zoneMouseReleased
        if(deplacement == true)
        {
            deplacement = false;
            cur_som.draw((Graphics2D)getGraphics());
        }
    }//GEN-LAST:event_zoneMouseReleased

    private void zoneMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_zoneMouseExited
        if(mouseStatus == waitForFinArete ) {
            mouseStatus = waitForDebutArete;
        }
        if(mouseStatus == waitForFinNoSens) {
            mouseStatus = waitForDebutNoSens;
        }
        if(!linge.isEmpty()) {
            ((ligne)linge.get(linge.size()-1)).draw2((Graphics2D)getGraphics());
            linge.clear();
        }
          graphe_arete();graphe_sommet();
    }//GEN-LAST:event_zoneMouseExited

    private void jMenuItem1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MouseClicked
       pccMouseClicked(evt);
    }//GEN-LAST:event_jMenuItem1MouseClicked

    private void jMenuItem2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem2MouseClicked
        pcc1MouseClicked(evt);
    }//GEN-LAST:event_jMenuItem2MouseClicked

    private void jMenuItem5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem5MouseClicked
         rebootMouseClicked(evt);
    }//GEN-LAST:event_jMenuItem5MouseClicked

    private void jMenuItem1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MousePressed
         pccMouseClicked(evt);
    }//GEN-LAST:event_jMenuItem1MousePressed

    private void jMenuItem2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem2MousePressed
        pcc1MouseClicked(evt);
    }//GEN-LAST:event_jMenuItem2MousePressed

    private void jMenuItem5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem5MousePressed
        rebootMouseClicked(evt);
    }//GEN-LAST:event_jMenuItem5MousePressed

    private void jMenuItem3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem3MousePressed
        jButton4MouseClicked(evt);
    }//GEN-LAST:event_jMenuItem3MousePressed

    @SuppressWarnings("empty-statement")
    private void jMenuItem8MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem8MousePressed
       if(!sommetList.isEmpty())
       {    
         try
         {
             Distance ds = new Distance(this,false,Madjacence(),sommetList);
             ds.showDDialog();
             graphe_arete();
             graphe_sommet();
         }
         catch (Exception e)
         {
             JOptionPane.showMessageDialog(null, e.toString());
         }
       }
       else
       {
         JOptionPane.showMessageDialog(null,"Le graphe ne possède aucun sommet !","GRAPH'OU::Message d'avertissement",JOptionPane.ERROR_MESSAGE);
       }
    }//GEN-LAST:event_jMenuItem8MousePressed

    private void jMenuItem6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem6MousePressed
        Varc1MouseClicked(evt);
    }//GEN-LAST:event_jMenuItem6MousePressed

    private void jMenuItem7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem7MousePressed
        Varc2MouseClicked(evt);
    }//GEN-LAST:event_jMenuItem7MousePressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
      
    }//GEN-LAST:event_formWindowClosed

    private void jMenuItem9MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem9MousePressed
       if(JOptionPane.showConfirmDialog(null, "Voulez-vous Vraiment Quitter GRAPH'OU?", "Fermeture de Graph'ou",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)== JOptionPane.OK_OPTION)
       {
           System.exit(0);
       }
    }//GEN-LAST:event_jMenuItem9MousePressed

    private void jMenuItem10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem10MousePressed
        Aide ad = new Aide(this,true);
        ad.setVisible(true);
    }//GEN-LAST:event_jMenuItem10MousePressed

    private void rebootActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rebootActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rebootActionPerformed

    private void SzoneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SzoneKeyPressed
       
    }//GEN-LAST:event_SzoneKeyPressed

    private void SzoneKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SzoneKeyReleased
       String chance = Szone.getText().toUpperCase();
       if(!chance.isEmpty() || "".equals(chance))
       {
        recherche.clear();
        for(int i=0;i<sommetList.size();i++)
        {
           if(((Sommet)sommetList.get(i)).get_nom().indexOf(chance)>= 0) {
               Sommet cur = (Sommet)sommetList.get(i);
               recherche.add(cur);
           }
        }
       }
       else
       {
           init_sommet();
       }
       graphe_sommet();
    }//GEN-LAST:event_SzoneKeyReleased

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        init_sommet();
        recherche.clear();
        graphe_sommet();
        Szone.setText("");
    }//GEN-LAST:event_jLabel3MouseClicked

    private void rebootMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rebootMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_rebootMouseEntered

    private void SzoneFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SzoneFocusLost
       recherche.clear();
    }//GEN-LAST:event_SzoneFocusLost

    private void jButton7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseClicked
       if(sommetList.size()>1 && areteList.size()>0)
       {
        try
        {
          int i;
          ecran.setText("");
          ACMarc.clear();Arbre.clear();
          if(type != 1){
          Random a = new Random();
          i= a.nextInt() % sommetList.size();}
          else {
              i = 0;
          }
          ACM2((Sommet)sommetList.get(Math.abs(i)));
          zoneMouseEntered(evt);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e.toString(),"Message d'avertissement",JOptionPane.ERROR_MESSAGE);
        }
       }
    }//GEN-LAST:event_jButton7MouseClicked

    private void Varc4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Varc4MouseClicked
        jMenuItem9MousePressed(evt);
    }//GEN-LAST:event_Varc4MouseClicked

    private void jMenuItem11MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem11MousePressed
       if(sommetList.size()>1 && areteList.size()>0)
       {
        try
        {
            if(type == 1){
                copieareteList.clear();
                for(int i=0;i<areteList.size();i++){
                    Arete ar = new Arete(((Arete)areteList.get(i)).get_debut(),((Arete)areteList.get(i)).get_fin(),((Arete)areteList.get(i)).get_cout(),((Arete)areteList.get(i)).get_nature(),((Arete)areteList.get(i)).get_nature2());
                    copieareteList.add((Arete)ar);
                    effacer((Arete)areteList.get(i));
                    ((Arete)areteList.get(i)).set_cout(1);
                    }
            }
            else{
                for(int i=0;i<areteList.size();i++){
                    copieareteList.add((Arete)areteList.get(i));
                    effacer((Arete)areteList.get(i));
                    ((Arete)areteList.get(i)).set_cout(1);
                    }
            }
            graphe_arete();graphe_sommet();
        }
        catch(Exception e)
        {
            
        }
       }
    }//GEN-LAST:event_jMenuItem11MousePressed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
                construit_l_arbre();
    }//GEN-LAST:event_formWindowActivated

    private void construit_l_arbre()
    {
               try{
                arbre2 = new JTree(constitu_arbre());
                arbre2.addTreeSelectionListener(new TreeSelectionListener() {

                       @Override
                       public void valueChanged(TreeSelectionEvent tse) 
                       {
                               if(arbre2.getLastSelectedPathComponent() != null)
                               {
                                   String str = arbre2.getLastSelectedPathComponent().toString();
                                   if(str.startsWith("Arete N°") || str.startsWith("Sommet N°"))
                                   {
                                       String [] str2 = str.split("N°");
                                       if(str.startsWith("Sommet N°")){
                                           int i=Integer.parseInt(str2[1]);
                                           String [] ti ={"Propriétés","Valeurs"};
                                           GTodel model = new GTodel(((Sommet)sommetList.get(i)).get_properties(), ti);
                                           pte = new JTable(model);
                                           scroll_tableau.setViewportView(pte);
                                           pte.getColumn("Valeurs").setCellRenderer(new TableComponent());
                                       }
                                       else{
                                           String [] str3 = str2[1].split("<");
                                           int i=Integer.parseInt(str3[0]);
                                           String [] ti ={"Propriétés","Valeurs"};
                                           GTodel model = new GTodel(((Arete)areteList.get(i)).get_properties(), ti);
                                           pte = new JTable(model);
                                           scroll_tableau.setViewportView(pte);
                                           pte.getColumn("Valeurs").setCellRenderer(new TableComponent());
                                       }
                                   }
                                  else
                                   {
                                       switch (str) {
                                           case "Sommet(s)":
                                               String [] ti ={"Propriétés","Valeurs"};
                                               GTodel model = new GTodel(get_properties_sommets(), ti);
                                               pte = new JTable(model);
                                               scroll_tableau.setViewportView(pte);
                                               pte.getColumn("Valeurs").setCellRenderer(new TableComponent());
                                               break;
                                           case "Arete(s)":
                                               String [] tir ={"Propriétés","Valeurs"};
                                               GTodel modelr = new GTodel(get_properties_arcs(), tir);
                                               pte = new JTable(modelr);
                                               scroll_tableau.setViewportView(pte);
                                               pte.getColumn("Valeurs").setCellRenderer(new TableComponent());
                                               break;
                                           case "Graphe courant":
                                               String [] tire ={"Propriétés","Valeurs"};
                                               GTodel modelre = new GTodel(get_properties_graphe(), tire);
                                               pte = new JTable(modelre);
                                               scroll_tableau.setViewportView(pte);
                                               pte.getColumn("Valeurs").setCellRenderer(new TableComponent());
                                               break;
                                           
                                       }
                                   }
                               }
                               
                       }
                });
                initRenderer();
                arbre2.setCellRenderer(new Monrendu());
                scroll_arbre.setViewportView(arbre2);
               }
               catch(Exception e){
                   JOptionPane.showMessageDialog(monInstance, e.toString());
               }
    }
    
    private void PLihmMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PLihmMouseClicked
       if(PLihm.isEnabled()){ 
        if(renommage == true)
        {
            PL();
        }
        else
        {     JOptionPane.showMessageDialog(null,"Veuillez cliquez dans un sommet pour le désigner comme origine\ndu parcours !","GRAPH'OU::Message d'avertissement",JOptionPane.ERROR_MESSAGE);   
        
                graphe_arete();graphe_sommet();}
       }
    }//GEN-LAST:event_PLihmMouseClicked

    private void PPihmMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PPihmMouseClicked
       if(PPihm.isEnabled()){
        PP();}
    }//GEN-LAST:event_PPihmMouseClicked

    private void showMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showMouseClicked
        if(show.isSelected()) {
            showCout2 = true;
        }
        else {
            showCout2 = false;
        }
        graphe_arete();graphe_sommet();
    }//GEN-LAST:event_showMouseClicked

    private void show1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_show1MouseClicked
        if(show1.isSelected()) {
            showCout = false;
        }
        else {
            showCout = true;
        }
        graphe_arete();graphe_sommet();
    }//GEN-LAST:event_show1MouseClicked

    private void jMenu1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MousePressed
        if(type == 1){
            changementnat.setEnabled(true);
        }
    }//GEN-LAST:event_jMenu1MousePressed

    private void changementnatMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_changementnatMousePressed
       if(changementnat.isEnabled())
       {
        if(sommetList.size()>1 && areteList.size()>0){
         if(JOptionPane.showConfirmDialog(null, "Voulez-vous Vraiment Changer la nature de votre graphe?\nil passera de graphe orienté à graphe non orienté.", "Graph'ou::Changement de nature",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)== JOptionPane.OK_OPTION)
          {
              ar.clear();
              changement_nature();
              init_nature.setEnabled(true);
              PPihm.setEnabled(false);
              PLihm.setEnabled(false);
              Vsommet.setEnabled(false);
              Varc.setEnabled(false);
              Vpointeur.setEnabled(false);
              cancel.setEnabled(false);
              changementnat.setEnabled(false);
              typekandor = type;
          }
        }
       else{
              JOptionPane.showMessageDialog(null,"Votre graphe ne comporte pas assez de sommet\net d'arc pour solliciter le changement de nature!","GRAPH'OU::Message d'erreur",JOptionPane.ERROR_MESSAGE);
       }
       }
    }//GEN-LAST:event_changementnatMousePressed

    private void init_graphe()
    {
        for(int i=0;i<areteList.size();i++){
            effacer((Arete)areteList.get(i));
        }
        areteList.clear();
        ar.clear();
        for(int i=0;i<copie.size();i++){
            areteList.add(copie.get(i));
        }
        copie.clear();
        type=1;
        graphe_arete();graphe_sommet();
    }
    
    private void init_natureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_init_natureMouseClicked
        if(init_nature.isEnabled())
        {
           init_graphe();
           init_nature.setEnabled(false);
           PPihm.setEnabled(true);
           PLihm.setEnabled(true);
           Vsommet.setEnabled(true);
           Varc.setEnabled(true);
           Vpointeur.setEnabled(true);
           cancel.setEnabled(true);
           changementnat.setEnabled(true );
           typekandor = type;
        }
    }//GEN-LAST:event_init_natureMouseClicked
    
    private void init_sommet()
    {
        for(int i=0;i<sommetList.size();i++)
        {
            ((Sommet)sommetList.get(i)).set_color(new Color(0,0,255));
        }
    }
    
 //fonction qui renvoie la liste des sommets ainsi que leur dégré.   
    public void constitue_degre()
    {
        int g=0,j,k,i;
        int[][] liste = Madjacence();
        
        for(i=0;i<liste.length;i++)
        {
            g=0;
            for(j=0;j<liste.length;j++)
            {
                if(liste[i][j]!=Dijkstra.ALPHA_NOTDEF)
                {
                    g++;
                    if(j!=i && !(((Sommet)sommetList.get(i)).est_contenu(j))) {
                        ((Sommet)sommetList.get(i)).AddAdjacent(j);
                    }
                }
            }
            j=i;
            for(k=0;k<liste.length;k++)
            {
                if(liste[k][j]!=Dijkstra.ALPHA_NOTDEF)
                {
                    g++;
                    if(k!=i && !(((Sommet)sommetList.get(i)).est_contenu(k))) {
                        ((Sommet)sommetList.get(i)).AddAdjacent(k);
                    }
                }
            }
         ((Sommet)sommetList.get(i)).set_Degre(g);
        }
    }
    
    public String show_degre()
    {
        String str="";
        constitue_degre();
        for(int i=0;i<sommetList.size();i++)
        {
            str+=((Sommet)sommetList.get(i)).get_numero()+" ---> "+((Sommet)sommetList.get(i)).get_Degre()+"\n";
            str+="Sommet(s) Adjacent(s) à "+i+"\n";
            str+=((Sommet)sommetList.get(i)).liste_Ad()+"\n ------------- \n";
        }
        return str;
    }
    //fonction permettant de trier les sommets par ordre décroissant de dégré

    public void Coloriage()
    {
        Sommet [] tableau = new Sommet [sommetList.size()];
        Sommet som;
        int k,nbe=1;
        Random a = new Random();
        Color c = new Color(Math.abs(a.nextInt())% 256,Math.abs(a.nextInt())% 256,Math.abs(a.nextInt())% 256);
        
        constitue_degre();
        for(int i=0;i<sommetList.size();i++) {
            tableau[i]=(Sommet)sommetList.get(i);
        }
        
        for(int i=1;i<tableau.length;i++)
        {
            som=tableau[i];
            k=i-1;
            while((k>=0) && (tableau[k].get_Degre()<som.get_Degre()))
            {
                tableau[k+1]=tableau[k];
                k--;
            }
            tableau[k+1]=som;
        }
        
        for(int i=0;i<tableau.length;i++)
        {
            tableau[i].copie_adjacent();
        }
        
        for(int i=0;i<tableau.length;i++)
        {
            if(used_color(tableau, c)==true)
            {c = new_color(tableau);}
            
            recur_sommet(tableau[i], c);
        }
        
        for(int i=0;i<tableau.length;i++)
        {
            pause(75);
            tableau[i].draw((Graphics2D)getGraphics());
            tableau[i].set_estcolorier(false);
            tableau[i].init_adjacent();
        }
        c=tableau[0].get_color();
        ArrayList lite=new ArrayList();
        lite.add(c);
        for(int i=1;i<tableau.length;i++)
        {
            if(tableau[i].get_color()!=c && !lite.contains(tableau[i].get_color()))
            { nbe++;lite.add(c);c=tableau[i].get_color();}
        }
        lite.clear();
          chromatique=nbe;
    }
    
    public void recur_sommet(Sommet s,Color c)
    {
        Sommet cur;
        
        if(s.get_statut()==false)
        {
            s.set_estcolorier(true);
            s.set_color(c);
            for(int i=0;i<sommetList.size();i++)
            {
                cur = (Sommet)sommetList.get(i);
                if((s.est_contenu(cur.get_numero())==false) && (s.get_numero()!=cur.get_numero()))
                {
                    for(int ji=0;ji<s.Adjacent.size();ji++) {
                        cur.AddAdjacent((int)s.Adjacent.get(ji));
                    } 
                    recur_sommet(cur, c);
                }
            }
        }
    }
    //dit si une couleur a déjà été utilisée ou pas dans la coloration du graphe
    public boolean used_color(Sommet [] tableau,Color c)
    {
        for(int i=0;i<tableau.length;i++)
        {
            if(tableau[i].get_color()==c)
            {return true;}
        }
        return false;
    }
    //renvoie une couleur non encore utiliséé
    public Color new_color(Sommet [] tableau)
    {
        Color c;
        boolean trouve = false;
        
        do
        {
            Random a = new Random();
            c = new Color(Math.abs(a.nextInt())% 256,Math.abs(a.nextInt())% 256,Math.abs(a.nextInt())% 256);  
            for(int i=0;i<tableau.length;i++)
            {
                if(tableau[i].get_color() == c)
                {trouve=true;}
            }
        }
        while(trouve==true);
        return c;
    }
    
    private int Ma_pos(Arete ar)
    {
        for(int $i=0;$i<areteList.size();$i++)
        {
            if(((Arete)areteList.get($i)).get_debut() == ar.get_debut() && ((Arete)areteList.get($i)).get_fin() == ar.get_fin() && ((Arete)areteList.get($i)).get_cout() == ar.get_cout()) 
            {
                return $i;
            }
        }
        
        return -1;
    }
    
    private int Ma_pos2(Arete ar)
    {
        for(int $i=0;$i<copie.size();$i++)
        {
            if(((Arete)copie.get($i)).get_debut() == ar.get_debut() && ((Arete)copie.get($i)).get_fin() == ar.get_fin() && ((Arete)copie.get($i)).get_cout() == ar.get_cout()) 
            {
                return $i;
            }
        }
        
        return -1;
    }
    
    public void draw_road()
    {
        boolean trouve = true;
        if(!ar.isEmpty())
        {
            /*for(int i=0;i<ar.size();i++)
            {
                if(!areteList.contains((Arete)ar.get(i)))
                {trouve=false;break;}
            }*/
            if(trouve == true)
            {
              if(type==1)
              {
                for(int i=0;i<ar.size();i++) {
                    Arete are = (Arete)ar.get(i); 
                    if(A_inverse(are))
                    {
                        if(Ma_pos(are)<Pos_inverse(are)) {
                            are.draw3_moins((Graphics2D)getGraphics());
                        }
                        else {
                            are.draw3_plus((Graphics2D)getGraphics());
                        }
                    }
                    else {
                        are.draw3((Graphics2D)getGraphics());
                    }
                }
            }
           else
            {
                for(int i=0;i<ar.size();i++) {
                    ((Arete)ar.get(i)).draw3((Graphics2D)getGraphics());
                }
            }
           }
        }
    }
   
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Fgraphe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Fgraphe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Fgraphe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Fgraphe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Fgraphe().setVisible(true);
            }
        });
    }
    
    
    private Filtre filtre;
    private LinkedList E ;
    private ArrayList<Sarete> ACMarc;
    private ArrayList<Sommet> Arbre;
    //nombre chromatique du graphe
    private int chromatique,arbre_taille;
        // différentes constantes
    static final int waitForSommet = 0 ;
    static final int waitForDebutArete = 1 ;
    static final int waitForFinArete = 2 ;
    static final int waitForDebutNoSens =3;
    static final int waitForFinNoSens = 4;
    static final float mon_stroke=(float)2.0  ;         // taille du pinceau de dessin
    private ArrayList ar;
    /** couleur d'un sommet non visité */
    public static final Color colorNonVisite =new Color(0,0, 0xff);
    /** couleur d'un sommet en cours de visite */
    public static final Color colorEnCours = Color.gray ;
    /** couleur d'un sommet déjà visité */
    public static final Color colorDejaVisite = Color.black ;
    /** booléen indiquant le graphe est fini et prêt pour les algos */
    protected boolean ready = false ;
    /** infini (int) */
    protected static final int iInfini = 100000 ;
    /** infini (double) */
    protected static final double dInfini = 100000000.0 ;
    /** nil */
    protected static final int nil = -1 ;
    // flag indiquant si on affiche les distances ou les numéros
    private static boolean showCout = false ;
    private static boolean showCout2 = true ;
    /** liste des sommets */
    protected static Vector sommetList ; // liste des sommets
    /** liste d'adjacence: vecteur indexé par les sommets, chaque élément est une LinkedList
     contenant la liste des arêtes partant de ce sommet */
    protected Vector adjList ;
    private Vector areteList,copieareteList; // liste des aretes
    private Vector areteListInv;//listes des aretes inversé
    private Vector save;
    private ArrayList recherche;
    private int curIndexSommet = 0 ; // indice du sommet courant
    private int mouseStatus = waitForSommet ; // ce qu'on fait à la souris
    private Sommet startSommet , endSommet ; // sommets de début et fin d'arête
    private int indexStartSommet , indexEndSommet ; // leur numéros
    private double poids = 1.0 ; // poids initial d'une arete
    private Fgraphe monInstance = this ;
    private int type=-5,typekandor;
    double coord_x,xglob;
    double coord_y,yglob;
    //dessin
    //fin historique
    private Sommet cur_som;
    private LinkedList sommetAdjList ;
    private Arete cur_arete,cur2_arete ;
    private boolean found ;
    private boolean renommage;
    private boolean deplacement;
    private ArrayList linge;
    private String route_acm;
    // variables des parcours
    private int d[] ;
    private int p[] ;
    private int Fd[];
    private int Ft[];
    private Vector nouveausommet;
    private int Fu[];
    private boolean boolcfctrue;
    private LinkedList F ;
    private Sommet pointInitial ;
    private int indexPointInitial ;
    private int tps ;
    private int tri ;
    //fin
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton PLihm;
    private javax.swing.JButton PPihm;
    private javax.swing.JTextField Szone;
    private javax.swing.JButton Varc;
    private javax.swing.JButton Varc1;
    private javax.swing.JButton Varc2;
    private javax.swing.JButton Varc4;
    private javax.swing.JButton Vpointeur;
    private javax.swing.JButton Vsommet;
    private javax.swing.JComboBox X;
    private javax.swing.JComboBox Y;
    private javax.swing.JTree arbre2;
    private javax.swing.JButton cancel;
    private javax.swing.JMenuItem changementnat;
    private javax.swing.JComboBox choixnature;
    private javax.swing.JToolBar conteneur1;
    private javax.swing.JTextPane cout_visible;
    private javax.swing.JTextPane ecran;
    private javax.swing.JButton init_nature;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar5;
    private javax.swing.JToolBar jToolBar6;
    private javax.swing.JLabel nature;
    private javax.swing.JButton pcc;
    private javax.swing.JButton pcc1;
    private javax.swing.JTable pte;
    private javax.swing.JButton reboot;
    private javax.swing.JScrollPane scroll_arbre;
    private javax.swing.JScrollPane scroll_tableau;
    private javax.swing.JRadioButton show;
    private javax.swing.JRadioButton show1;
    private javax.swing.JButton validnature;
    private javax.swing.JPanel zone;
    // End of variables declaration//GEN-END:variables

    private void changement_nature() {
        try
              {
                  copie = new ArrayList();
                  copie.clear();
                  for(int i=0;i<areteList.size();i++){
                      copie.add((Arete)areteList.get(i));
                      effacer((Arete)areteList.get(i));
                  }
                  type = waitForDebutNoSens;
                  nature.setText("Non orienté");
                  areteList.clear();
                  for(int i=0;i<copie.size();i++){
                      if(A_inverse2(copie.get(i)))
                      {
                         if(Ma_pos2(copie.get(i))>Pos_inverse2(copie.get(i)))
                         {
                          Double quinze = Math.min(copie.get(i).get_cout(),Poids_inverse(copie.get(i)));
                          areteList.add(new Arete(copie.get(i).get_debut(),copie.get(i).get_fin(),quinze,"Nor","first"));
                          areteList.add(new Arete(copie.get(i).get_fin(),copie.get(i).get_debut(),quinze,"Nor","second"));
                         }
                      }
                      else
                      {
                       areteList.add(new Arete(copie.get(i).get_debut(),copie.get(i).get_fin(),copie.get(i).get_cout(),"Nor","first"));
                       areteList.add(new Arete(copie.get(i).get_fin(),copie.get(i).get_debut(),copie.get(i).get_cout(),"Nor","second"));
                      }
                  }
                  graphe_arete();graphe_sommet();
              }
              catch(Exception e){
                 JOptionPane.showMessageDialog(null,e.toString(),"GRAPH'OU::Message d'erreur",JOptionPane.ERROR_MESSAGE);
              }
    }
}
