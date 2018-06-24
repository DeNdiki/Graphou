package Graphou;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.JButton;

/**
   Classe pour représenter les sommets d'un graphe.
   Toutes variables privées:
       numéro,
       forme,
       dates de début et fin,
       couleur,
       pere.
*/
public class Sommet
{
    private int numero ;             // numéro du sommet dans le graphe
    private Ellipse2D.Double forme ; // forme géométrique de dessin
    private int dateA ;              // date de début de parcours (PP)
    private int dateZ ;              // date de fin de parcours (PP)
    private Color color ;            // couleur du sommet
    private Sommet pere ;            // père du sommet dans le graphe
    private double cout ;            // cout depuis l'origine
    private String nom;
    private int rang;
    static final double diametre=20.0 ;
    private int degre;//dégré du sommet
    ArrayList Adjacent,Adjacent_copie;
    private boolean est_colorie;

    /**
       Constructeur par défaut:
       initialisation de la couleur à non visité,
       du père et forme à null,
       de toutes les autres variables à 0
       sauf cout à infini
    */
    public Sommet()
    {
	pere = null ;
        est_colorie = false;
	color = Fgraphe.colorNonVisite ;
	dateA = dateZ = numero = 0 ;
	forme = null ;
	cout = Fgraphe.dInfini ;
        rang=0;
        degre=0;
        nom=Integer.toString(0);
        Adjacent=new ArrayList();
        Adjacent_copie=new ArrayList();
    }
    /**
	Constructeur à partir de coordonnées et numéro: crée la forme géométrique
    */
    public Sommet( double x, double y, int numero )
    {
	this() ;
	this.numero = numero ;
        this.nom = Integer.toString(numero);
	forme = new Ellipse2D.Double( x,y,diametre,diametre) ;

    }
    
    public void copie_adjacent()
    {
      Adjacent_copie.clear(); 
      for(int i=0;i<Adjacent.size();i++) {
            Adjacent_copie.add(Adjacent.get(i));
        }    
    }
    
    public void init_adjacent()
    {
      Adjacent.clear();
      for(int i=0;i<Adjacent.size();i++) {
            Adjacent.add(Adjacent_copie.get(i)) ;
        }    
    }
    
    /**
     * 
     * Constructeur de copie 
     */
    public Sommet(Sommet som)
    {
        this(som.get_forme().getX(),som.get_forme().getY(),som.get_numero());
        this.set_nom2(som.get_nom());
    }
    
    public void set_nom(String name)
    {
        this.nom =Integer.toString(numero).concat(" : "+name.toUpperCase());
    }
    
    public void set_nom2(String name)
    {
        this.nom =name.toUpperCase();
    }
    
    public String get_nom()
    {
        return this.nom;
    }
    
    //ajout d'un sommet adjacent
    public void AddAdjacent(int a)
    {
       if(!Adjacent.contains(a)) {
            Adjacent.add(a);
        }
    }
    
    //vérifie si un sommet est contenu dans la liste d'adjacence
    public boolean est_contenu(int a)
    {
        return (Adjacent.contains(a));
    }
    
    public Object[][] get_properties()
    {
        String str = this.get_color().getRed()+";"+this.get_color().getGreen()+";"+this.get_color().getBlue();
        JButton btn = new JButton(str);
        btn.setBackground(this.get_color());
        JButton btn_num = new JButton(num_s()),btn_nom = new JButton(this.get_nom()),
                btn_deg = new JButton(deg_s());
        btn_num.setBackground(Color.DARK_GRAY);
        btn_num.setForeground(Color.WHITE);
        btn_nom.setBackground(Color.DARK_GRAY);
        btn_nom.setForeground(Color.WHITE);
        btn_deg.setBackground(Color.DARK_GRAY);
        btn_deg.setForeground(Color.WHITE);
        btn.setForeground(Color.WHITE);
        Object [][] obj = {{"Numéro",btn_num},{"Nom",btn_nom},{"Dégré",btn_deg},{"Couleur",btn}};
        return obj;
    }
    
    private String num_s()
    {
        String s="";
        return (s+=this.get_numero());
    }
    private String deg_s()
    {
        String s="";
        return (s+=this.get_Degre());
    }
    
    public String liste_Ad()
    {
        String str="-";
        for(int i=0;i<Adjacent.size();i++) {
            str+=Adjacent.get(i)+"-";
        }
        return str;
    }
    /** setter pour estcolorié */
    public void set_estcolorier( boolean numero )
    {
	this.est_colorie = numero ;
    }
    /** getter pour dégré */
    public boolean get_statut()
    {
	return est_colorie ;
    }
    /** setter pour numero */
    public void set_numero( int numero )
    {
	this.numero = numero ;
    }
    /** getter pour dégré */
    public int get_Degre()
    {
	return degre ;
    }
    /** getter pour numero */
    public int get_numero()
    {
	return numero ;
    }
    /** setter pour le dégré */
    public void set_Degre(int deg )
    {
	this.degre =deg ;
    }
    /** setter pour forme */
    public void set_forme( Ellipse2D.Double forme )
    {
	this.forme = forme ;
    }
    /** getter pour forme */
    public Ellipse2D.Double get_forme()
    {
	return forme ;
    }
    /** getter rang*/
    public int get_rang()
    {
      return rang;
    }
    /** setter rang*/
    public void set_rang(int r){
      this.rang=r;
    }
    /** setter pour color */
    public void set_color( Color color )
    {
	this.color = color ;
    }
    /** getter pour color */
    public Color get_color()
    {
	return color ;
    }
    /** setter pour dateA */
    public void set_dateA( int dateA )
    {
	this.dateA = dateA ;
    }
    /** getter pour dateA */
    public int get_dateA()
    {
	return dateA ;
    }
    /** setter pour dateZ */
    public void set_dateZ( int dateZ )
    {
	this.dateZ = dateZ ;
    }
    /** getter pour dateA */
    public int get_dateZ()
    {
	return dateZ ;
    }
    /** setter pour pere */
    public void set_pere( Sommet pere )
    {
	this.pere = pere ;
    }
    /** getter pour pere */
    public Sommet get_pere()
    {
	return pere ;
    }
    /** setter pour distance à l'origine */
    public void set_cout( double cout )
    {
	this.cout = cout ;
    }
    /** getter pour distance à l'origine */
    public double get_cout()
    {
	return cout ;
    }
    /** méthode vérifiant qu'un point x,y est dans un sommet */
    public boolean contains( double x, double y )
    {        
        return (( Math.abs(this.get_forme().x - x) <=(diametre/2) && Math.abs(this.get_forme().y - y) <= (diametre/2) )?true:false);
    }
    /**
     *
     * @return
     */
    @Override
    public String toString()
    {
        String str="";
        str+=" Sommet N° : "+numero+"\n";
        str+="      Nom  : '"+nom+"'\n";
        str+="    Dégré  : "+degre+"\n";
        str+="  couleur  : "+color.toString()+"\n";
        return str;
    }
    public void draw_search( Graphics2D g2D )
    {
	String str ;
        ////////////////////////////////
        Ellipse2D.Double eller = new Ellipse2D.Double(forme.getX()+3,forme.getY()+15,18,7);
	g2D.setPaint(new  Color(180,180,180)) ;
	g2D.fill( eller ) ;
        ////////////////////////////////
	Ellipse2D.Double ellipse = get_forme();
	g2D.setPaint(Color.MAGENTA) ;
        g2D.setStroke(new BasicStroke(1f));
	g2D.fill( ellipse ) ;
	g2D.setPaint(new Color(230,230,230,230)) ;
        g2D.drawOval((int)forme.getX(),(int)forme.getY(),(int)diametre,(int)diametre);
	g2D.setPaint( new Color(204,204,204)  ) ;
        Ellipse2D.Double elle = new Ellipse2D.Double(forme.getX()+15,forme.getY()+15,4,4);
	g2D.setPaint(new  Color(0b11110101, 0b11110101, 0b11110101)) ;
	g2D.fill( elle ) ;
	g2D.setPaint( Color.white) ;
        g2D.setFont(new Font("Helvetica",Font.BOLD,10)); //Font("t",1,12));
	if (Fgraphe.get_showCout() == false ) {
            str = nom ;
        }
	else {
	    if ( cout == Fgraphe.dInfini ) {
                str = "x";
            }
	    else {
                str = Double.toString(cout) ;
            }
	}
	g2D.drawString( str ,
			( int ) ellipse.getX() + 6,
			( int ) ellipse.getY() + 11);
    }
    /** méthode de dessin, à appeler depuis paint.
	Si la variable showCout du graphe est vraie,
	on affiche les couts, sinon, le numéro */
    public void draw( Graphics2D g2D )
    {
	String str ;
        ////////////////////////////////
        Ellipse2D.Double eller = new Ellipse2D.Double(forme.getX()+3,forme.getY()+15,18,7);
	g2D.setPaint(new  Color(180,180,180)) ;
	g2D.fill( eller ) ;
        ////////////////////////////////
	Ellipse2D.Double ellipse = get_forme();
	g2D.setPaint( color ) ;
        g2D.setStroke(new BasicStroke(1f));
	g2D.fill( ellipse ) ;
	g2D.setPaint(new Color(230,230,230,230)) ;
        g2D.drawOval((int)forme.getX(),(int)forme.getY(),(int)diametre,(int)diametre);
	g2D.setPaint( new Color(204,204,204)  ) ;
        Ellipse2D.Double elle = new Ellipse2D.Double(forme.getX()+12,forme.getY()+12,4,4);
	g2D.setPaint(new  Color(0b11110101, 0b11110101, 0b11110101)) ;
	g2D.fill( elle ) ;
	g2D.setPaint( Color.white) ;
        g2D.setFont(new Font("Times New Roman",Font.BOLD,10)); //Font("t",1,12));
	if (Fgraphe.get_showCout() == false ) {
            str = nom ;
        }
	else {
	    if ( cout == Fgraphe.dInfini ) {
                str = "";
            }
	    else {
                str = Double.toString(cout) ;
            }
	}
	g2D.drawString( str ,
			( int ) ellipse.getX() + 6,
			( int ) ellipse.getY() + 11);
    }
    
    
    public void draw3( Graphics2D g2D )
    {
        g2D.setStroke(new BasicStroke(1f));
	g2D.setPaint(Color.red) ;
        g2D.drawOval((int)forme.getX(),(int)forme.getY(),(int)diametre,(int)diametre);
    }
    
    public void draw2( Graphics2D g2D )
    {
	String str ;
        ///////////  ombre du sommet
        Ellipse2D.Double eller = new Ellipse2D.Double(forme.getX()+3,forme.getY()+15,18,7);
	g2D.setPaint(new Color(204,204,204)) ;
	g2D.fill( eller ) ;
        ///////////  fin de l'ombre
	Ellipse2D.Double ellipse = get_forme();
	g2D.setPaint( new Color(204,204,204)) ;
        g2D.setStroke(new BasicStroke(1f));
	g2D.fill( ellipse ) ;
	g2D.setPaint(new Color(204,204,204)) ;
        g2D.drawOval((int)forme.getX(),(int)forme.getY(),(int)diametre,(int)diametre);
        g2D.setFont(new Font("Times New Roman",Font.BOLD,10)); //Font("t",1,12));
	    str = nom ;
        
	g2D.drawString( str ,
			( int ) ellipse.getX() + 6,
			( int ) ellipse.getY() + 11);
    }

}