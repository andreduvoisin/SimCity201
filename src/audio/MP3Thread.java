package audio;

import java.awt.FlowLayout;
import javax.media.Format;
import javax.media.PlugInManager;
import javax.media.format.AudioFormat;
import javax.swing.JApplet;
import javax.swing.JLabel;

public class MP3Thread extends JApplet {

	JLabel title = new JLabel("MP3 Threaded");
	JLabel pathto = new JLabel("File Path: ");
	public static JLabel status = new JLabel("Null");
	
	public void init(){
		setLayout(new FlowLayout());
		getContentPane().add(title);
		getContentPane().add(pathto);
		getContentPane().add(status);
		
			Format INPUT_MP3 = new AudioFormat(AudioFormat.MPEGLAYER3);
			Format INPUT_MPEG = new AudioFormat(AudioFormat.MPEG);
			Format OUTPUT = new AudioFormat(AudioFormat.LINEAR);
			
			PlugInManager.addPlugIn("com.sun.media.codec.audo.mp3.JavaDecoder", 
					new Format[]{INPUT_MP3, INPUT_MPEG}, new Format[]{OUTPUT}, PlugInManager.CODEC);
			
		String SoundSubDirectory = "/audio/";
		String SoundFile = "1-welcome.mp3";
		SoundFile = SoundSubDirectory + SoundFile;
		
		mp3 ThemeMusic = new mp3(SoundFile);
		ThemeMusic.start();
		
	}
	
	public MP3Thread() {
		// TODO Auto-generated constructor stub
	}

}
