package Graphou;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.JButton;

/**
    Classe pour représenter les arêtes d'un graphe:
    Toutes variables privées:
         sommet de début,
	 sommet de fin,
	 coût.
*/
public class Arete
{
    private Sommet debut ; // sommet de début
    private Sommet fin ;   // sommet de fin
    private double cout ;      // coût
    private Color color ; // couleur de dessin
    private String nature;
    private String nature2;
    static final Color lineColor = Color.BLACK ; // arêtes dessinnées en noire
    static final Color arrowColor = Color.BLACK ; // flèches en blanc
    static final Color stringColor = Color.red ; // couleur du coût
    static final double arrowSize = 25.0 ; // taille d'une flèche

    /** constructeur par défaut: tout à zéro, poids à 1 couleur à bleu */
    public Arete()
    {
	debut = null ;
        nature = "";
        nature2 = "";
	fin = null ;
	cout = 1.0 ;
	color = lineColor ;
    }
    /** constructeur pour arête sans coût (=1), couleur à bleu */
    public Arete( Sommet debut , Sommet fin )
    {
	this() ;
	this.debut = debut ;
	this.fin = fin ;
	cout = 1 ;
    }
    /** constructeur avec toutes variables sauf couleur à bleu */
    public Arete( Sommet debut , Sommet fin , double cout,String natu,String natu2)
    {
	this( debut , fin ) ;
        this.nature = natu; 
        this.nature2 = natu2;
	this.cout = cout ;
    }
    
    //constructeur de copie
    public Arete(Arete ar)
    {
        this(ar.get_debut(),ar.get_fin(),ar.get_cout(),ar.get_nature(),ar.get_nature2());
    }
    /** setter pour debut */
    public void set_debut( Sommet debut )
    {
	this.debut = debut ;
    }
    /** getter pour debut */
    public Sommet get_debut()
    {
	return debut ;
    }
    /** setter pour fin */
    public void set_fin( Sommet fin )
    {
	this.fin = fin ;
    }
    /** getter pour fin */
    public Sommet get_fin()
    {
	return fin ;
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
    
    public String get_nature()
    {
        return this.nature;
    }
    
    public String get_nature2()
    {
        return this.nature2;
    }
    /** setter pour cout */
    public void set_cout( double cout )
    {
	this.cout = cout ;
    }
    /** getter pour cout */
    public double get_cout()
    {
	return cout ;
    }
    /** methode de dessin, à appeler depuis paint */
    public void draw( Graphics2D g2D )
    {
	// calcul des points de départ et d'arrivée de l'arête
	Point2D start,end ;
	start = new Point2D.Double() ;
	end = new Point2D.Double() ;
	start.setLocation( (debut.get_forme()).getX() + (debut.get_forme()).getWidth()/2.0 ,
			   (debut.get_forme().getY()) + (debut.get_forme()).getHeight()/2.0 ) ;
	end.setLocation( (fin.get_forme()).getX() + (fin.get_forme()).getWidth()/2.0 ,
			   (fin.get_forme()).getY() + (fin.get_forme()).getHeight()/2.0 ) ;
	// on la dessine
	g2D.setPaint( color ) ;
        g2D.setStroke(new BasicStroke(1f));
	if(((debut.get_forme()).getX()==(fin.get_forme()).getX())&&((debut.get_forme()).getY()==(fin.get_forme().getY()))){
		   Ellipse2D.Double elle=new Ellipse2D.Double((debut.get_forme()).getX()+15,(debut.get_forme()).getY()+15,((debut.get_forme()).getWidth())/1.2,((debut.get_forme()).getWidth())/1.2);
                   g2D.draw(elle);
                   if(!Fgraphe.get_showCout2()){
		   g2D.setPaint(new Color(204,204,204)) ;}
                   else{
                       g2D.setPaint( stringColor ) ;
                   }
                   g2D.drawString( Double.toString( cout ) ,(int)(debut.get_forme()).getX() +40,(int)(debut.get_forme()).getY()+20) ;
                   if(!"Nor".equals(nature))
                   {
                     g2D.setStroke(new BasicStroke(1.5f));
                     Point2D.Double mm = new Point2D.Double((debut.get_forme()).getX() + 20 + ((((debut.get_forme()).getWidth())/1.2)/2),(debut.get_forme()).getY() + 15) ;
                     Point2D.Double f1 = new Point2D.Double((debut.get_forme()).getX() + 20+ ((((debut.get_forme()).getWidth())/1.2)/2),(debut.get_forme()).getY() + 25) ;
                     Point2D.Double f2 = new Point2D.Double((debut.get_forme()).getX() + 20 + ((((debut.get_forme()).getWidth())/1.2)/2) + 7 ,(debut.get_forme()).getY() + 15) ;
                     g2D.setPaint( arrowColor ) ;
                     int []  x = {(int)mm.getX(),(int)f1.getX(),(int)f2.getX()};
                     int []  y = {(int)mm.getY(),(int)f1.getY(),(int)f2.getY()};
                     g2D.fillPolygon(x, y, 3);
                   }
	}
	else {
		   g2D.draw( new Line2D.Double( start , end ) ) ;
                   g2D.setStroke(new BasicStroke(1.5f));
		   // on calcule l'endroit où l'on va dessiner la flèche: au 3/5 vers la fin
		   Point2D.Double mm = new Point2D.Double() ;
		   mm.setLocation( (2.0 * start.getX() + 3.0 * end.getX()) / 5.0 ,
			(2.0 * start.getY() + 3.0 * end.getY()) / 5.0 ) ;
		   // vecteur normal a  origine-->extremite
		   double a = ( double ) end.getX() - start.getX();
		   double b = ( double ) end.getY() - start.getY();
		   Point2D.Double nn = new Point2D.Double();
		   nn.setLocation( b/(Math.sqrt(a*a+b*b)),-a/(Math.sqrt(a*a+b*b)));
		   // calcul des points pour la geometrie de la fleche
		   Point2D.Double pp = new Point2D.Double(),temp = new Point2D.Double();
		   double x1 = start.getX() + arrowSize*nn.getX();
		   double y1 =  start.getY() + arrowSize*nn.getY();
		   double x2 = mm.getX();
		   double y2 = mm.getY();
		   pp.setLocation(0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2);
                   temp.setLocation(0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2);
		   g2D.setPaint( arrowColor );
		   x1 = start.getX() - arrowSize*nn.getX();
		   y1 = start.getY() - arrowSize*nn.getY();
		   x2 = mm.getX();
		   y2 = mm.getY();
		   pp.setLocation(0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2);
		   g2D.setPaint( arrowColor );
                   if(!"Nor".equals(nature))
                    {
                     int []  x = {(int)mm.getX(),(int)temp.getX(),(int)pp.getX()};
                     int []  y = {(int)mm.getY(),(int)temp.getY(),(int)pp.getY()};
                     g2D.drawPolygon(x, y, 3);
                     g2D.fillPolygon(x, y, 3);
                    }
                   if(!Fgraphe.get_showCout2()){
		   g2D.setPaint(new Color(204,204,204)) ;}
                   else{
                       g2D.setPaint( stringColor ) ;
                   }
                   if(!"Nor".equals(nature))
                    {g2D.drawString( Double.toString( cout ) , (int)mm.getX() + 10 , (int)mm.getY() + 10 ) ;}
                   else
                   {
                       if(!"second".equals(nature2)) {
                           g2D.drawString( Double.toString( cout ) , (int)mm.getX() + 10 , (int)mm.getY() + 10 ) ;
                       }
                   }
	}

    }
    /*
     * méthode permettant de renvoyer les propriétés de l'arête courante
     */
    public Object[][] get_properties()
    {
        JButton b1 = new JButton(this.get_debut().get_nom()),
                b2 = new JButton(this.get_fin().get_nom()),
                b3 = new JButton(""+this.get_cout()),
                b4 = new JButton(this.get_debut().get_nom()+" ---> "+this.get_fin().get_nom());
                b1.setBackground(new Color(0,204,255));
                b1.setForeground(Color.BLACK);
                b2.setBackground(new Color(0,204,255));
                b2.setForeground(Color.BLACK);
                b3.setBackground(new Color(0,204,255));
                b3.setForeground(Color.BLACK);
                b4.setBackground(new Color(0,204,255));
                b4.setForeground(Color.BLACK);
        Object [][] obj = {{"Départ",b1},{"Arrivée",b2},{"Coût",b3},{"Sens",b4}};
        return obj;
    }
    
    /** methode de dessin, à appeler depuis paint */
    public void draw2(Graphics2D g2D)
    {
	// calcul des points de départ et d'arrivée de l'arête
	Point2D start,end ;
	start = new Point2D.Double() ;
	end = new Point2D.Double() ;
	start.setLocation( (debut.get_forme()).getX() + (debut.get_forme()).getWidth()/2.0 ,
			   (debut.get_forme().getY()) + (debut.get_forme()).getHeight()/2.0 ) ;
	end.setLocation( (fin.get_forme()).getX() + (fin.get_forme()).getWidth()/2.0 ,
			   (fin.get_forme()).getY() + (fin.get_forme()).getHeight()/2.0 ) ;
	// on la dessine
	g2D.setPaint( new Color(204,204,204) ) ;
        g2D.setStroke(new BasicStroke(1f));
	if(((debut.get_forme()).getX()==(fin.get_forme()).getX())&&((debut.get_forme()).getY()==(fin.get_forme().getY()))){
		   Ellipse2D.Double elle=new Ellipse2D.Double((debut.get_forme()).getX()+15,(debut.get_forme()).getY()+15,((debut.get_forme()).getWidth())/1.2,((debut.get_forme()).getWidth())/1.2);
                   g2D.draw(elle);
                   //on calcul l'endroit où l'on va dessiner la flèche pour les elipse
		    g2D.setPaint(new Color(204,204,204)) ;
		    g2D.drawString( Double.toString( cout ) ,(int)(debut.get_forme()).getX() +40,(int)(debut.get_forme()).getY()+20) ;
                   if(!"Nor".equals(nature))
                   {
                     Point2D.Double mm = new Point2D.Double((debut.get_forme()).getX() + 20 + ((((debut.get_forme()).getWidth())/1.2)/2),(debut.get_forme()).getY() + 15) ;
                     Point2D.Double f1 = new Point2D.Double((debut.get_forme()).getX() + 20+ ((((debut.get_forme()).getWidth())/1.2)/2),(debut.get_forme()).getY() + 25) ;
                     Point2D.Double f2 = new Point2D.Double((debut.get_forme()).getX() + 20 + ((((debut.get_forme()).getWidth())/1.2)/2) + 7 ,(debut.get_forme()).getY() + 15) ;
                     g2D.setPaint(new Color(204,204,204)) ;
                     int []  x = {(int)mm.getX(),(int)f1.getX(),(int)f2.getX()};
                     int []  y = {(int)mm.getY(),(int)f1.getY(),(int)f2.getY()};
                     g2D.fillPolygon(x, y, 3);
                   }
	}
	else {
		   g2D.draw( new Line2D.Double( start , end ) ) ;
                   g2D.setStroke(new BasicStroke(1.5f));
		   // on calcule l'endroit où l'on va dessiner la flèche: au 3/5 vers la fin
		   Point2D.Double mm = new Point2D.Double() ;
		   mm.setLocation( (2.0 * start.getX() + 3.0 * end.getX()) / 5.0 ,
			(2.0 * start.getY() + 3.0 * end.getY()) / 5.0 ) ;
		   // vecteur normal a  origine-->extremite
		   double a = ( double ) end.getX() - start.getX();
		   double b = ( double ) end.getY() - start.getY();
		   Point2D.Double nn = new Point2D.Double();
		   nn.setLocation( b/(Math.sqrt(a*a+b*b)) ,
			-a/(Math.sqrt(a*a+b*b)));
		   // calcul des points pour la geometrie de la fleche
		   Point2D.Double pp = new Point2D.Double(),temp = new Point2D.Double();
		   double x1 = start.getX() + arrowSize*nn.getX();
		   double y1 =  start.getY() + arrowSize*nn.getY();
		   double x2 = mm.getX();
		   double y2 = mm.getY();
		   pp.setLocation(  0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2 );
		   temp.setLocation(  0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2 );
		   g2D.setPaint(new Color(204,204,204));
		    x1 = start.getX() - arrowSize*nn.getX();
		   y1 = start.getY() - arrowSize*nn.getY();
		   x2 = mm.getX();
		   y2 = mm.getY();
		   pp.setLocation( 0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2 );
		   g2D.setPaint(new Color(204,204,204));
                   if(!"Nor".equals(nature))
                   {
                     int []  x = {(int)mm.getX(),(int)temp.getX(),(int)pp.getX()};
                     int []  y = {(int)mm.getY(),(int)temp.getY(),(int)pp.getY()};
                     g2D.drawPolygon(x, y, 3);
                     g2D.fillPolygon(x, y, 3);
                   }
                   g2D.setPaint(new Color(204,204,204)) ;
                   if(!"Nor".equals(nature))
                   {
		     g2D.drawString( Double.toString( cout ) , (int)mm.getX() + 10 , (int)mm.getY() + 10 ) ;}
                   else
                   {
                       if(!"second".equals(nature2)) {
                           g2D.drawString( Double.toString( cout ) , (int)mm.getX() + 10 , (int)mm.getY() + 10 ) ;
                       }
                   }
	}

    }
    
    /** methode de dessin, à appeler depuis paint */
    public void draw2_moins(Graphics2D g2D)
    {
	// calcul des points de départ et d'arrivée de l'arête
	Point2D start,end ;
	start = new Point2D.Double() ;
	end = new Point2D.Double() ;
	start.setLocation( (debut.get_forme()).getX() + (debut.get_forme()).getWidth()/2.0 - (Sommet.diametre/2.2) ,
			   (debut.get_forme().getY()) + (debut.get_forme()).getHeight()/2.0 ) ;
	end.setLocation( (fin.get_forme()).getX() + (fin.get_forme()).getWidth()/2.0 - (Sommet.diametre/2.2) ,
			   (fin.get_forme()).getY() + (fin.get_forme()).getHeight()/2.0 ) ;
	// on la dessine
	g2D.setPaint( new Color(204,204,204) ) ;
        g2D.setStroke(new BasicStroke(1f));
	if(((debut.get_forme()).getX()==(fin.get_forme()).getX())&&((debut.get_forme()).getY()==(fin.get_forme().getY()))){
		   Ellipse2D.Double elle=new Ellipse2D.Double((debut.get_forme()).getX()+15,(debut.get_forme()).getY()+15,((debut.get_forme()).getWidth())/1.2,((debut.get_forme()).getWidth())/1.2);
                   g2D.draw(elle);
                   //on calcul l'endroit où l'on va dessiner la flèche pour les elipse
		    g2D.setPaint(new Color(204,204,204)) ;
		    g2D.drawString( Double.toString( cout ) ,(int)(debut.get_forme()).getX() +40,(int)(debut.get_forme()).getY()+20) ;
                   if(!"Nor".equals(nature))
                   {
                     Point2D.Double mm = new Point2D.Double((debut.get_forme()).getX() + 20 + ((((debut.get_forme()).getWidth())/1.2)/2),(debut.get_forme()).getY() + 15) ;
                     Point2D.Double f1 = new Point2D.Double((debut.get_forme()).getX() + 20+ ((((debut.get_forme()).getWidth())/1.2)/2),(debut.get_forme()).getY() + 25) ;
                     Point2D.Double f2 = new Point2D.Double((debut.get_forme()).getX() + 20 + ((((debut.get_forme()).getWidth())/1.2)/2) + 7 ,(debut.get_forme()).getY() + 15) ;
                     g2D.setPaint(new Color(204,204,204)) ;
                     int []  x = {(int)mm.getX(),(int)f1.getX(),(int)f2.getX()};
                     int []  y = {(int)mm.getY(),(int)f1.getY(),(int)f2.getY()};
                     g2D.fillPolygon(x, y, 3);
                   }
	}
	else {
		   g2D.draw( new Line2D.Double( start , end ) ) ;
                   g2D.setStroke(new BasicStroke(1.5f));
		   // on calcule l'endroit où l'on va dessiner la flèche: au 3/5 vers la fin
		   Point2D.Double mm = new Point2D.Double() ;
		   mm.setLocation( (2.0 * start.getX() + 3.0 * end.getX()) / 5.0 ,
			(2.0 * start.getY() + 3.0 * end.getY()) / 5.0 ) ;
		   // vecteur normal a  origine-->extremite
		   double a = ( double ) end.getX() - start.getX();
		   double b = ( double ) end.getY() - start.getY();
		   Point2D.Double nn = new Point2D.Double();
		   nn.setLocation( b/(Math.sqrt(a*a+b*b)) ,
			-a/(Math.sqrt(a*a+b*b)));
		   // calcul des points pour la geometrie de la fleche
		   Point2D.Double pp = new Point2D.Double(),temp = new Point2D.Double();
		   double x1 = start.getX() + arrowSize*nn.getX();
		   double y1 =  start.getY() + arrowSize*nn.getY();
		   double x2 = mm.getX();
		   double y2 = mm.getY();
		   pp.setLocation(  0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2 );
		   temp.setLocation(  0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2 );
		   g2D.setPaint(new Color(204,204,204));
		    x1 = start.getX() - arrowSize*nn.getX();
		   y1 = start.getY() - arrowSize*nn.getY();
		   x2 = mm.getX();
		   y2 = mm.getY();
		   pp.setLocation( 0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2 );
		   g2D.setPaint(new Color(204,204,204));
                   if(!"Nor".equals(nature))
                   {
                     int []  x = {(int)mm.getX(),(int)temp.getX(),(int)pp.getX()};
                     int []  y = {(int)mm.getY(),(int)temp.getY(),(int)pp.getY()};
                     g2D.drawPolygon(x, y, 3);
                     g2D.fillPolygon(x, y, 3);
                   }
                   g2D.setPaint(new Color(204,204,204)) ;
                   if(!"Nor".equals(nature))
                   {
		     g2D.drawString( Double.toString( cout ) , (int)mm.getX() + 10 , (int)mm.getY() + 10 ) ;}
                   else
                   {
                       if(!"second".equals(nature2)) {
                           g2D.drawString( Double.toString( cout ) , (int)mm.getX() + 10 , (int)mm.getY() + 10 ) ;
                       }
                   }
	}

    }
    /** methode de dessin, à appeler depuis paint */
    public void draw2_plus(Graphics2D g2D)
    {
	// calcul des points de départ et d'arrivée de l'arête
	Point2D start,end ;
	start = new Point2D.Double() ;
	end = new Point2D.Double() ;
	start.setLocation( (debut.get_forme()).getX() + (debut.get_forme()).getWidth()/2.0 + (Sommet.diametre/2.2) ,
			   (debut.get_forme().getY()) + (debut.get_forme()).getHeight()/2.0 ) ;
	end.setLocation( (fin.get_forme()).getX() + (fin.get_forme()).getWidth()/2.0 + (Sommet.diametre/2.2) ,
			   (fin.get_forme()).getY() + (fin.get_forme()).getHeight()/2.0 ) ;
	// on la dessine
	g2D.setPaint( new Color(204,204,204) ) ;
        g2D.setStroke(new BasicStroke(1f));
	if(((debut.get_forme()).getX()==(fin.get_forme()).getX())&&((debut.get_forme()).getY()==(fin.get_forme().getY()))){
		   Ellipse2D.Double elle=new Ellipse2D.Double((debut.get_forme()).getX()+15,(debut.get_forme()).getY()+15,((debut.get_forme()).getWidth())/1.2,((debut.get_forme()).getWidth())/1.2);
                   g2D.draw(elle);
                   //on calcul l'endroit où l'on va dessiner la flèche pour les elipse
		    g2D.setPaint(new Color(204,204,204)) ;
		    g2D.drawString( Double.toString( cout ) ,(int)(debut.get_forme()).getX() +40,(int)(debut.get_forme()).getY()+20) ;
                   if(!"Nor".equals(nature))
                   {
                     Point2D.Double mm = new Point2D.Double((debut.get_forme()).getX() + 20 + ((((debut.get_forme()).getWidth())/1.2)/2),(debut.get_forme()).getY() + 15) ;
                     Point2D.Double f1 = new Point2D.Double((debut.get_forme()).getX() + 20+ ((((debut.get_forme()).getWidth())/1.2)/2),(debut.get_forme()).getY() + 25) ;
                     Point2D.Double f2 = new Point2D.Double((debut.get_forme()).getX() + 20 + ((((debut.get_forme()).getWidth())/1.2)/2) + 7 ,(debut.get_forme()).getY() + 15) ;
                     g2D.setPaint(new Color(204,204,204)) ;
                     int []  x = {(int)mm.getX(),(int)f1.getX(),(int)f2.getX()};
                     int []  y = {(int)mm.getY(),(int)f1.getY(),(int)f2.getY()};
                     g2D.fillPolygon(x, y, 3);
                   }
	}
	else {
		   g2D.draw( new Line2D.Double( start , end ) ) ;
                   g2D.setStroke(new BasicStroke(1.5f));
		   // on calcule l'endroit où l'on va dessiner la flèche: au 3/5 vers la fin
		   Point2D.Double mm = new Point2D.Double() ;
		   mm.setLocation( (2.0 * start.getX() + 3.0 * end.getX()) / 5.0 ,
			(2.0 * start.getY() + 3.0 * end.getY()) / 5.0 ) ;
		   // vecteur normal a  origine-->extremite
		   double a = ( double ) end.getX() - start.getX();
		   double b = ( double ) end.getY() - start.getY();
		   Point2D.Double nn = new Point2D.Double();
		   nn.setLocation( b/(Math.sqrt(a*a+b*b)) ,
			-a/(Math.sqrt(a*a+b*b)));
		   // calcul des points pour la geometrie de la fleche
		   Point2D.Double pp = new Point2D.Double(),temp = new Point2D.Double();
		   double x1 = start.getX() + arrowSize*nn.getX();
		   double y1 =  start.getY() + arrowSize*nn.getY();
		   double x2 = mm.getX();
		   double y2 = mm.getY();
		   pp.setLocation(  0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2 );
		   temp.setLocation(  0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2 );
		   g2D.setPaint(new Color(204,204,204));
		    x1 = start.getX() - arrowSize*nn.getX();
		   y1 = start.getY() - arrowSize*nn.getY();
		   x2 = mm.getX();
		   y2 = mm.getY();
		   pp.setLocation( 0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2 );
		   g2D.setPaint(new Color(204,204,204));
                   if(!"Nor".equals(nature))
                   {
                     int []  x = {(int)mm.getX(),(int)temp.getX(),(int)pp.getX()};
                     int []  y = {(int)mm.getY(),(int)temp.getY(),(int)pp.getY()};
                     g2D.drawPolygon(x, y, 3);
                     g2D.fillPolygon(x, y, 3);
                   }
                   g2D.setPaint(new Color(204,204,204)) ;
                   if(!"Nor".equals(nature))
                   {
		     g2D.drawString( Double.toString( cout ) , (int)mm.getX() + 10 , (int)mm.getY() + 10 ) ;}
                   else
                   {
                       if(!"second".equals(nature2)) {
                           g2D.drawString( Double.toString( cout ) , (int)mm.getX() + 10 , (int)mm.getY() + 10 ) ;
                       }
                   }
	}

    }
    
    /** methode de dessin, à appeler depuis paint */
    public void draw3( Graphics2D g2D )
    {
	// calcul des points de départ et d'arrivée de l'arête
	Point2D start,end ;
	start = new Point2D.Double() ;
	end = new Point2D.Double() ;
	start.setLocation( (debut.get_forme()).getX() + (debut.get_forme()).getWidth()/2.0 ,
			   (debut.get_forme().getY()) + (debut.get_forme()).getHeight()/2.0 ) ;
	end.setLocation( (fin.get_forme()).getX() + (fin.get_forme()).getWidth()/2.0 ,
			   (fin.get_forme()).getY() + (fin.get_forme()).getHeight()/2.0 ) ;
	// on la dessine
	g2D.setPaint( Color.orange ) ;
        g2D.setStroke(new BasicStroke(1f));
	if(((debut.get_forme()).getX()==(fin.get_forme()).getX())&&((debut.get_forme()).getY()==(fin.get_forme().getY()))){
		   Ellipse2D.Double elle=new Ellipse2D.Double((debut.get_forme()).getX()+15,(debut.get_forme()).getY()+15,((debut.get_forme()).getWidth())/1.2,((debut.get_forme()).getWidth())/1.2);
                   g2D.draw(elle);
                   if(!Fgraphe.get_showCout2()){
		   g2D.setPaint(new Color(204,204,204)) ;}
                   else{
                       g2D.setPaint( stringColor ) ;
                   }
                   if(!"Nor".equals(nature))
                   {
                    g2D.setStroke(new BasicStroke(1.5f));
                    Point2D.Double mm = new Point2D.Double((debut.get_forme()).getX() + 20 + ((((debut.get_forme()).getWidth())/1.2)/2),(debut.get_forme()).getY() + 15) ;
                    Point2D.Double f1 = new Point2D.Double((debut.get_forme()).getX() + 20+ ((((debut.get_forme()).getWidth())/1.2)/2),(debut.get_forme()).getY() + 25) ;
                    Point2D.Double f2 = new Point2D.Double((debut.get_forme()).getX() + 20 + ((((debut.get_forme()).getWidth())/1.2)/2) + 7 ,(debut.get_forme()).getY() + 15) ;
                    g2D.setPaint(Color.RED) ;
                     int []  x = {(int)mm.getX(),(int)f1.getX(),(int)f2.getX()};
                     int []  y = {(int)mm.getY(),(int)f1.getY(),(int)f2.getY()};
                     g2D.fillPolygon(x, y, 3);
                   }
	}
	else {
		   g2D.draw( new Line2D.Double( start , end ) ) ;
                   g2D.setStroke(new BasicStroke(1.5f));
		   // on calcule l'endroit où l'on va dessiner la flèche: au 3/5 vers la fin
		   Point2D.Double mm = new Point2D.Double() ;
		   mm.setLocation( (2.0 * start.getX() + 3.0 * end.getX()) / 5.0 ,
			(2.0 * start.getY() + 3.0 * end.getY()) / 5.0 ) ;
		   // vecteur normal a  origine-->extremite
		   double a = ( double ) end.getX() - start.getX();
		   double b = ( double ) end.getY() - start.getY();
		   Point2D.Double nn = new Point2D.Double();
		   nn.setLocation( b/(Math.sqrt(a*a+b*b)) ,
			-a/(Math.sqrt(a*a+b*b)));
		   // calcul des points pour la geometrie de la fleche
		   Point2D.Double pp = new Point2D.Double(),temp = new Point2D.Double();
		   double x1 = start.getX() + arrowSize*nn.getX();
		   double y1 =  start.getY() + arrowSize*nn.getY();
		   double x2 = mm.getX();
		   double y2 = mm.getY();
		   pp.setLocation(  0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2 );
		   temp.setLocation(  0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2 );
		   g2D.setPaint(Color.RED);
		   x1 = start.getX() - arrowSize*nn.getX();
		   y1 = start.getY() - arrowSize*nn.getY();
		   x2 = mm.getX();
		   y2 = mm.getY();
		   pp.setLocation( 0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2 );
                   if(!"Nor".equals(nature))
                   {int []  x = {(int)mm.getX(),(int)temp.getX(),(int)pp.getX()};
                     int []  y = {(int)mm.getY(),(int)temp.getY(),(int)pp.getY()};
                     g2D.drawPolygon(x, y, 3);
                     g2D.fillPolygon(x, y, 3);
                   }
                   if(Fgraphe.get_showCout2()){
		   g2D.setPaint(Color.blue) ;
                   if(!"Nor".equals(nature))
                   {
		     g2D.drawString( Double.toString( cout ) , (int)mm.getX() + 10 , (int)mm.getY() + 10 ) ;}
                   else
                   {
                       if(!"second".equals(nature2)) {
                           g2D.drawString( Double.toString( cout ) , (int)mm.getX() + 10 , (int)mm.getY() + 10 ) ;
                       }
                   }}
	}

    }
    
    /** methode de dessin, à appeler depuis paint */
    public void draw3_plus( Graphics2D g2D )
    {
	// calcul des points de départ et d'arrivée de l'arête
	Point2D start,end ;
	start = new Point2D.Double() ;
	end = new Point2D.Double() ;
	start.setLocation( (debut.get_forme()).getX() + (debut.get_forme()).getWidth()/2.0 + (Sommet.diametre/2.2)  ,
			   (debut.get_forme().getY()) + (debut.get_forme()).getHeight()/2.0 ) ;
	end.setLocation( (fin.get_forme()).getX() + (fin.get_forme()).getWidth()/2.0 + (Sommet.diametre/2.2)  ,
			   (fin.get_forme()).getY() + (fin.get_forme()).getHeight()/2.0 ) ;
	// on la dessine
	g2D.setPaint( Color.orange ) ;
        g2D.setStroke(new BasicStroke(1f));
	if(((debut.get_forme()).getX()==(fin.get_forme()).getX())&&((debut.get_forme()).getY()==(fin.get_forme().getY()))){
		   Ellipse2D.Double elle=new Ellipse2D.Double((debut.get_forme()).getX()+15,(debut.get_forme()).getY()+15,((debut.get_forme()).getWidth())/1.2,((debut.get_forme()).getWidth())/1.2);
                   g2D.draw(elle);
                   if(!"Nor".equals(nature))
                   {
                    g2D.setStroke(new BasicStroke(1.5f));
                    Point2D.Double mm = new Point2D.Double((debut.get_forme()).getX() + 20 + ((((debut.get_forme()).getWidth())/1.2)/2),(debut.get_forme()).getY() + 15) ;
                    Point2D.Double f1 = new Point2D.Double((debut.get_forme()).getX() + 20+ ((((debut.get_forme()).getWidth())/1.2)/2),(debut.get_forme()).getY() + 25) ;
                    Point2D.Double f2 = new Point2D.Double((debut.get_forme()).getX() + 20 + ((((debut.get_forme()).getWidth())/1.2)/2) + 7 ,(debut.get_forme()).getY() + 15) ;
                    g2D.setPaint(Color.RED) ;
                     int []  x = {(int)mm.getX(),(int)f1.getX(),(int)f2.getX()};
                     int []  y = {(int)mm.getY(),(int)f1.getY(),(int)f2.getY()};
                     g2D.fillPolygon(x, y, 3);
                   }
	}
	else {
		   g2D.draw( new Line2D.Double( start , end ) ) ;
                   g2D.setStroke(new BasicStroke(1.5f));
		   // on calcule l'endroit où l'on va dessiner la flèche: au 3/5 vers la fin
		   Point2D.Double mm = new Point2D.Double() ;
		   mm.setLocation( (2.0 * start.getX() + 3.0 * end.getX()) / 5.0 ,
			(2.0 * start.getY() + 3.0 * end.getY()) / 5.0 ) ;
		   // vecteur normal a  origine-->extremite
		   double a = ( double ) end.getX() - start.getX();
		   double b = ( double ) end.getY() - start.getY();
		   Point2D.Double nn = new Point2D.Double();
		   nn.setLocation( b/(Math.sqrt(a*a+b*b)) ,
			-a/(Math.sqrt(a*a+b*b)));
		   // calcul des points pour la geometrie de la fleche
		   Point2D.Double pp = new Point2D.Double(),temp = new Point2D.Double();
		   double x1 = start.getX() + arrowSize*nn.getX();
		   double y1 =  start.getY() + arrowSize*nn.getY();
		   double x2 = mm.getX();
		   double y2 = mm.getY();
		   pp.setLocation(  0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2 );
		   temp.setLocation(  0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2 );
		   g2D.setPaint(Color.RED);
		   x1 = start.getX() - arrowSize*nn.getX();
		   y1 = start.getY() - arrowSize*nn.getY();
		   x2 = mm.getX();
		   y2 = mm.getY();
		   pp.setLocation( 0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2 );
                   if(!"Nor".equals(nature))
                   {int []  x = {(int)mm.getX(),(int)temp.getX(),(int)pp.getX()};
                     int []  y = {(int)mm.getY(),(int)temp.getY(),(int)pp.getY()};
                     g2D.drawPolygon(x, y, 3);
                     g2D.fillPolygon(x, y, 3);
                   }
                   if(Fgraphe.get_showCout2()){
		   g2D.setPaint(Color.blue) ;
                   if(!"Nor".equals(nature))
                   {
		     g2D.drawString( Double.toString( cout ) , (int)mm.getX() + 10 , (int)mm.getY() + 10 ) ;}
                   else
                   {
                       if(!"second".equals(nature2)) {
                           g2D.drawString( Double.toString( cout ) , (int)mm.getX() + 10 , (int)mm.getY() + 10 ) ;
                       }
                   }}
	}

    }
    
    /** methode de dessin, à appeler depuis paint */
    public void draw3_moins( Graphics2D g2D )
    {
	// calcul des points de départ et d'arrivée de l'arête
	Point2D start,end ;
	start = new Point2D.Double() ;
	end = new Point2D.Double() ;
	start.setLocation( (debut.get_forme()).getX() + (debut.get_forme()).getWidth()/2.0 - (Sommet.diametre/2.2)  ,
			   (debut.get_forme().getY()) + (debut.get_forme()).getHeight()/2.0 ) ;
	end.setLocation( (fin.get_forme()).getX() + (fin.get_forme()).getWidth()/2.0 - (Sommet.diametre/2.2)  ,
			   (fin.get_forme()).getY() + (fin.get_forme()).getHeight()/2.0 ) ;
	// on la dessine
	g2D.setPaint( Color.orange ) ;
        g2D.setStroke(new BasicStroke(1f));
	if(((debut.get_forme()).getX()==(fin.get_forme()).getX())&&((debut.get_forme()).getY()==(fin.get_forme().getY()))){
		   Ellipse2D.Double elle=new Ellipse2D.Double((debut.get_forme()).getX()+15,(debut.get_forme()).getY()+15,((debut.get_forme()).getWidth())/1.2,((debut.get_forme()).getWidth())/1.2);
                   g2D.draw(elle);
                   if(!"Nor".equals(nature))
                   {
                    g2D.setStroke(new BasicStroke(1.5f));
                    Point2D.Double mm = new Point2D.Double((debut.get_forme()).getX() + 20 + ((((debut.get_forme()).getWidth())/1.2)/2),(debut.get_forme()).getY() + 15) ;
                    Point2D.Double f1 = new Point2D.Double((debut.get_forme()).getX() + 20+ ((((debut.get_forme()).getWidth())/1.2)/2),(debut.get_forme()).getY() + 25) ;
                    Point2D.Double f2 = new Point2D.Double((debut.get_forme()).getX() + 20 + ((((debut.get_forme()).getWidth())/1.2)/2) + 7 ,(debut.get_forme()).getY() + 15) ;
                    g2D.setPaint(Color.RED) ;
                     int []  x = {(int)mm.getX(),(int)f1.getX(),(int)f2.getX()};
                     int []  y = {(int)mm.getY(),(int)f1.getY(),(int)f2.getY()};
                     g2D.fillPolygon(x, y, 3);
                   }
	}
	else {
		   g2D.draw( new Line2D.Double( start , end ) ) ;
                   g2D.setStroke(new BasicStroke(1.5f));
		   // on calcule l'endroit où l'on va dessiner la flèche: au 3/5 vers la fin
		   Point2D.Double mm = new Point2D.Double() ;
		   mm.setLocation( (2.0 * start.getX() + 3.0 * end.getX()) / 5.0 ,
			(2.0 * start.getY() + 3.0 * end.getY()) / 5.0 ) ;
		   // vecteur normal a  origine-->extremite
		   double a = ( double ) end.getX() - start.getX();
		   double b = ( double ) end.getY() - start.getY();
		   Point2D.Double nn = new Point2D.Double();
		   nn.setLocation( b/(Math.sqrt(a*a+b*b)) ,
			-a/(Math.sqrt(a*a+b*b)));
		   // calcul des points pour la geometrie de la fleche
		   Point2D.Double pp = new Point2D.Double(),temp = new Point2D.Double();
		   double x1 = start.getX() + arrowSize*nn.getX();
		   double y1 =  start.getY() + arrowSize*nn.getY();
		   double x2 = mm.getX();
		   double y2 = mm.getY();
		   pp.setLocation(  0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2 );
		   temp.setLocation(  0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2 );
		   g2D.setPaint(Color.RED);
		   x1 = start.getX() - arrowSize*nn.getX();
		   y1 = start.getY() - arrowSize*nn.getY();
		   x2 = mm.getX();
		   y2 = mm.getY();
		   pp.setLocation( 0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2 );
                   if(!"Nor".equals(nature))
                   {int []  x = {(int)mm.getX(),(int)temp.getX(),(int)pp.getX()};
                     int []  y = {(int)mm.getY(),(int)temp.getY(),(int)pp.getY()};
                     g2D.drawPolygon(x, y, 3);
                     g2D.fillPolygon(x, y, 3);
                   }
                   if(Fgraphe.get_showCout2()){
		   g2D.setPaint(Color.blue) ;
                   if(!"Nor".equals(nature))
                   {
		     g2D.drawString( Double.toString( cout ) , (int)mm.getX() + 10 , (int)mm.getY() + 10 ) ;}
                   else
                   {
                       if(!"second".equals(nature2)) {
                           g2D.drawString( Double.toString( cout ) , (int)mm.getX() + 10 , (int)mm.getY() + 10 ) ;
                       }
                   }}
	}

    }
    /** methode de dessin, à appeler depuis paint */
    public void draw_moins( Graphics2D g2D )
    {
	// calcul des points de départ et d'arrivée de l'arête
	Point2D start,end ;
	start = new Point2D.Double() ;
	end = new Point2D.Double() ;
	start.setLocation( (debut.get_forme()).getX() + (debut.get_forme()).getWidth()/2.0 - (Sommet.diametre/2.2) ,
			   (debut.get_forme().getY()) + (debut.get_forme()).getHeight()/2.0) ;
	end.setLocation( (fin.get_forme()).getX() + (fin.get_forme()).getWidth()/2.0  - (Sommet.diametre/2.2) ,
			   (fin.get_forme()).getY() + (fin.get_forme()).getHeight()/2.0) ;
	// on la dessine
	g2D.setPaint( color ) ;
        g2D.setStroke(new BasicStroke(1f));
	if(((debut.get_forme()).getX()==(fin.get_forme()).getX())&&((debut.get_forme()).getY()==(fin.get_forme().getY()))){
		   Ellipse2D.Double elle=new Ellipse2D.Double((debut.get_forme()).getX()+15,(debut.get_forme()).getY()+15,((debut.get_forme()).getWidth())/1.2,((debut.get_forme()).getWidth())/1.2);
                   g2D.draw(elle);
                   //on calcul l'endroit où l'on va dessiner la flèche pour les elipse
                   g2D.setPaint( stringColor ) ;
		   g2D.drawString( Double.toString( cout ) ,(int)(debut.get_forme()).getX() +40,(int)(debut.get_forme()).getY()+20) ;
                   if(!"Nor".equals(nature))
                   {
                     g2D.setStroke(new BasicStroke(1.5f));
                     Point2D.Double mm = new Point2D.Double((debut.get_forme()).getX() + 20 + ((((debut.get_forme()).getWidth())/1.2)/2),(debut.get_forme()).getY() + 15) ;
                     Point2D.Double f1 = new Point2D.Double((debut.get_forme()).getX() + 20+ ((((debut.get_forme()).getWidth())/1.2)/2),(debut.get_forme()).getY() + 25) ;
                     Point2D.Double f2 = new Point2D.Double((debut.get_forme()).getX() + 20 + ((((debut.get_forme()).getWidth())/1.2)/2) + 7 ,(debut.get_forme()).getY() + 15) ;
                     g2D.setPaint( arrowColor ) ;
                     int []  x = {(int)mm.getX(),(int)f1.getX(),(int)f2.getX()};
                     int []  y = {(int)mm.getY(),(int)f1.getY(),(int)f2.getY()};
                     g2D.fillPolygon(x, y, 3);
                   }
	}
	else {
		   g2D.draw( new Line2D.Double( start , end ) ) ;
                   g2D.setStroke(new BasicStroke(1.5f));
		   // on calcule l'endroit où l'on va dessiner la flèche: au 3/5 vers la fin
		   Point2D.Double mm = new Point2D.Double() ;
		   mm.setLocation( (2.0 * start.getX() + 3.0 * end.getX()) / 5.0 ,
			(2.0 * start.getY() + 3.0 * end.getY()) / 5.0 ) ;
		   // vecteur normal a  origine-->extremite
		   double a = ( double ) end.getX() - start.getX();
		   double b = ( double ) end.getY() - start.getY();
		   Point2D.Double nn = new Point2D.Double();
		   nn.setLocation( b/(Math.sqrt(a*a+b*b)),-a/(Math.sqrt(a*a+b*b)));
		   // calcul des points pour la geometrie de la fleche
		   Point2D.Double pp = new Point2D.Double(),temp = new Point2D.Double();
		   double x1 = start.getX() + arrowSize*nn.getX();
		   double y1 =  start.getY() + arrowSize*nn.getY();
		   double x2 = mm.getX();
		   double y2 = mm.getY();
		   pp.setLocation(0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2);
                   temp.setLocation(0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2);
		   g2D.setPaint( arrowColor );
		   x1 = start.getX() - arrowSize*nn.getX();
		   y1 = start.getY() - arrowSize*nn.getY();
		   x2 = mm.getX();
		   y2 = mm.getY();
		   pp.setLocation(0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2);
		   g2D.setPaint( arrowColor );
                   if(!"Nor".equals(nature))
                    {
                     int []  x = {(int)mm.getX(),(int)temp.getX(),(int)pp.getX()};
                     int []  y = {(int)mm.getY(),(int)temp.getY(),(int)pp.getY()};
                     g2D.drawPolygon(x, y, 3);
                     g2D.fillPolygon(x, y, 3);
                    }
                   if(!Fgraphe.get_showCout2()){
		   g2D.setPaint(new Color(204,204,204)) ;}
                   else{
                       g2D.setPaint( stringColor ) ;
                   }
                   if(!"Nor".equals(nature))
                    {g2D.drawString( Double.toString( cout ) , (int)mm.getX() + 10 , (int)mm.getY() + 10 ) ;}
                   else
                   {
                       if(!"second".equals(nature2)) {
                           g2D.drawString( Double.toString( cout ) , (int)mm.getX() + 10 , (int)mm.getY() + 10 ) ;
                       }
                   }
	}

    }
    /** methode de dessin, à appeler depuis paint */
    public void draw_plus( Graphics2D g2D )
    {
	// calcul des points de départ et d'arrivée de l'arête
	Point2D start,end ;
	start = new Point2D.Double() ;
	end = new Point2D.Double() ;
	start.setLocation( (debut.get_forme()).getX() + (debut.get_forme()).getWidth()/2.0 + (Sommet.diametre/2.2) ,
			   (debut.get_forme().getY()) + (debut.get_forme()).getHeight()/2.0) ;
	end.setLocation( (fin.get_forme()).getX() + (fin.get_forme()).getWidth()/2.0  + (Sommet.diametre/2.2) ,
			   (fin.get_forme()).getY() + (fin.get_forme()).getHeight()/2.0 ) ;
	// on la dessine
	g2D.setPaint( color ) ;
        g2D.setStroke(new BasicStroke(1f));
	if(((debut.get_forme()).getX()==(fin.get_forme()).getX())&&((debut.get_forme()).getY()==(fin.get_forme().getY()))){
		   Ellipse2D.Double elle=new Ellipse2D.Double((debut.get_forme()).getX()+15,(debut.get_forme()).getY()+15,((debut.get_forme()).getWidth())/1.2,((debut.get_forme()).getWidth())/1.2);
                   g2D.draw(elle);
                   if(!Fgraphe.get_showCout2()){
		   g2D.setPaint(new Color(204,204,204)) ;}
                   else{
                       g2D.setPaint( stringColor ) ;
                   }
                   //on calcul l'endroit où l'on va dessiner la flèche pour les elipse
		   g2D.drawString( Double.toString( cout ) ,(int)(debut.get_forme()).getX() +40,(int)(debut.get_forme()).getY()+20) ;
                   if(!"Nor".equals(nature))
                   {
                     g2D.setStroke(new BasicStroke(1.5f));
                     Point2D.Double mm = new Point2D.Double((debut.get_forme()).getX() + 20 + ((((debut.get_forme()).getWidth())/1.2)/2),(debut.get_forme()).getY() + 15) ;
                     Point2D.Double f1 = new Point2D.Double((debut.get_forme()).getX() + 20+ ((((debut.get_forme()).getWidth())/1.2)/2),(debut.get_forme()).getY() + 25) ;
                     Point2D.Double f2 = new Point2D.Double((debut.get_forme()).getX() + 20 + ((((debut.get_forme()).getWidth())/1.2)/2) + 7 ,(debut.get_forme()).getY() + 15) ;
                     g2D.setPaint( arrowColor ) ;
                     int []  x = {(int)mm.getX(),(int)f1.getX(),(int)f2.getX()};
                     int []  y = {(int)mm.getY(),(int)f1.getY(),(int)f2.getY()};
                     g2D.fillPolygon(x, y, 3);
                   }
	}
	else {
		   g2D.draw( new Line2D.Double( start , end ) ) ;
                   g2D.setStroke(new BasicStroke(1.5f));
		   // on calcule l'endroit où l'on va dessiner la flèche: au 3/5 vers la fin
		   Point2D.Double mm = new Point2D.Double() ;
		   mm.setLocation( (2.0 * start.getX() + 3.0 * end.getX()) / 5.0 ,
			(2.0 * start.getY() + 3.0 * end.getY()) / 5.0 ) ;
		   // vecteur normal a  origine-->extremite
		   double a = ( double ) end.getX() - start.getX();
		   double b = ( double ) end.getY() - start.getY();
		   Point2D.Double nn = new Point2D.Double();
		   nn.setLocation( b/(Math.sqrt(a*a+b*b)),-a/(Math.sqrt(a*a+b*b)));
		   // calcul des points pour la geometrie de la fleche
		   Point2D.Double pp = new Point2D.Double(),temp = new Point2D.Double();
		   double x1 = start.getX() + arrowSize*nn.getX();
		   double y1 =  start.getY() + arrowSize*nn.getY();
		   double x2 = mm.getX();
		   double y2 = mm.getY();
		   pp.setLocation(0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2);
                   temp.setLocation(0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2);
		   g2D.setPaint( arrowColor );
		   x1 = start.getX() - arrowSize*nn.getX();
		   y1 = start.getY() - arrowSize*nn.getY();
		   x2 = mm.getX();
		   y2 = mm.getY();
		   pp.setLocation(0.1*x1 + 0.9*x2, 0.1*y1 + 0.9*y2);
		   g2D.setPaint( arrowColor );
                   if(!"Nor".equals(nature))
                    {
                     int []  x = {(int)mm.getX(),(int)temp.getX(),(int)pp.getX()};
                     int []  y = {(int)mm.getY(),(int)temp.getY(),(int)pp.getY()};
                     g2D.drawPolygon(x, y, 3);
                     g2D.fillPolygon(x, y, 3);
                    }
                   if(!Fgraphe.get_showCout2()){
		   g2D.setPaint(new Color(204,204,204)) ;}
                   else{
                       g2D.setPaint( stringColor ) ;
                   }
                   if(!"Nor".equals(nature))
                    {g2D.drawString( Double.toString( cout ) , (int)mm.getX() + 10 , (int)mm.getY() + 10 ) ;}
                   else
                   {
                       if(!"second".equals(nature2)) {
                           g2D.drawString( Double.toString( cout ) , (int)mm.getX() + 10 , (int)mm.getY() + 10 ) ;
                       }
                   }
	}

    }
}

