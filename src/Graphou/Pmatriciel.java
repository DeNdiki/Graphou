/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphou;

/**
 *
 * @author stephane
 */
public final class Pmatriciel 
{   
    private int N,un,deux,expo;
    private int[][] resultat;
    String str="";
    
    public Pmatriciel(int[][] mat,int a,int som1,int som2) 
    {
      N = mat.length;
      expo = a;
      int[][] m =new int[N][N];
      m = initialise(mat);
      resultat = new int[N][N];
      un = som1;deux = som2;
      resultat = exposant(m, m, expo);
    }
    /**
     * @param args the command line arguments
     */
    
    public int nombre()
    {
        return resultat[un][deux];
    }
    
    public int get_trace()
    {
        int i=0;
        
        for(int j=0;j<resultat.length;j++) {
            i+= resultat[j][j];
        }
        return i;
    }
    
    public int[][] mat_id() 
    {
        int[][] m = new int[N][N];
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if ( i == j) {
                    m[i][j] = 1;
                }
                else {
                    m[i][j] = 0;
                }
            }
        }
        return m;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() 
    {
        String str="";
        for (int i = 0; i < N; i++)
        {
             for (int j = 0; j < N; j++) {
                str+=" "+resultat[i][j];
            }
               str+="\n";
       }
        return str;
    }

    public int[][] initialise(int[][] matrice) 
    {
        int[][] m = new int[N][N];
        for(int i = 0; i < matrice.length; i++)
        {
            for (int j = 0; j < matrice.length; j++)
            {
                if(matrice[i][j]!=Graphe.ALPHA_NOTDEF) {
                    m[i][j] = 1;
                }
                else {
                    m[i][j] = 0;
                }
            }
        }
        return m;
    }

    public int[][] produit(int[][] p, int[][] m) 
    {
        int[][] resultat = new int[p.length][p.length];
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                resultat[i][j] = 0;
                for (int k = 0; k < N; k++) {
                    resultat[i][j] += p[i][k] * m[k][j];
                }
            }
        }
        return resultat;
    }

    public final int[][] exposant(int[][] m,int[][] mat, int n)
    {
        int[][] resultat = m;
        if (n == 0) 
        {
                resultat = mat_id();
                String str="";
                System.out.println(" Ordre n='"+n+"'");
                for(int i=0;i<resultat.length;i++ )
                {
                    for(int j=0;j<resultat.length;j++){
                        str+=resultat[i][j];
                    }
                    str+="\n";
                }
                
                return mat_id();
        }
        else if ( n == 1)
        {  
                return resultat;
        }
        else
        {
            resultat = produit(resultat, mat);
            return exposant(resultat,mat, n-1);
        }
    }
    public final String exposant_str()
    {
                str+="Ordre n='"+expo+"'\n========\n";
                for(int i=0;i<resultat.length;i++ )
                {
                    for(int j=0;j<resultat.length;j++){
                        str+=resultat[i][j]+" ";
                    }
                    str+="\n";
                }
                str+="\n";
                return str;
    }
}
