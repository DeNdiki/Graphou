package Graphou;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer implements Runnable {

	private SourceDataLine line;
	private File file;
	private AudioInputStream audioInputStream;	
    private AudioFormat audioFormat;
	private boolean stop=false;	

	public void stop() {
		stop=true;
	}

    public void init(){
        
        try {
            audioInputStream = AudioSystem.getAudioInputStream(file);
            
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        
        //Il est n�cessaire de conna�tre le format audio du fichier
        // d'entr�e
        // pour permettre � java de cr�er l'objet DataLine ad�quat
        audioFormat = audioInputStream.getFormat();
        //System.out.println(audioFormat);
        
        // En plus du format du flux audio d'entr�e il est n�cessaire de
        // sp�cifier le type de DataLine qu'on veut
        // ici le DataLine qu'on souhaite est un SourceDataLine qui permet
        // la
        // lecture (targetDataLine permet l'enregistrement).

        DataLine.Info info = new DataLine.Info(SourceDataLine.class,
                audioFormat);

        // On r�cup�re le DataLine ad�quat et on l'ouvre
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
           
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return;
        }
    } 
    
	public void run() {
        
        stop = false;
        // Avant toute chose il est n�cessaire d'ouvrir la ligne
        try {
            line.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return;
            // TODO Auto-generated catch block

        }
        // pour que le flux soit effectivement redirig� sur la carte son il
        // faut
        // demarrer la ligne
        line.start();

		// il faut maintenant �crire sur la ligne. Travail comme sur un
		// inputStream quelconque
		try {
			byte bytes[] = new byte[1024];
			int bytesRead=0;			
			while (((bytesRead = audioInputStream.read(bytes, 0, bytes.length)) != -1)
					&& !stop) {
				
				line.write(bytes, 0, bytesRead);
			}
		} catch (IOException io) {
			io.printStackTrace();
			return;
		}
		// on ferme la ligne � la fin
		line.close();
	}	

	public void setFile(File file) {
		this.file = file;
       
	}

	public SourceDataLine getLine() {
		return line;
	}	
	

}
