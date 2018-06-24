package Graphou;
import java.io.File;
import java.io.FileInputStream;


public class Son {

	private Thread audioPlayerThread;	
	private AudioPlayer player;
	private File audioFile;
        
	public Son(int i)
        {	
                player = new AudioPlayer();
                switch(i)
                {
                    case 0 :audioFile = new File("Graphou_Son/start.wav");
                    break;
                    case 1 :audioFile=new File("Graphou_Son/sommet.wav");
                    break;
                    case 2 :audioFile=new File("Graphou_Son/arete.wav");
                    break;
                    case 3:audioFile=new File("Graphou_Son/errorMsg.wav");
                    break;
                    case 4:audioFile=new File("Graphou_Son/arete.wav");
                    break;
                    case 5:audioFile=new File("Graphou_Son/suppression.wav");
                    break;
                }
                player.setFile(audioFile);
        }
	/**
	 * @param args
	 */
	public void player()
        {	
                player.init();
		audioPlayerThread = new Thread(player);
		audioPlayerThread.start();
                player.stop();
        }

}
