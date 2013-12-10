package audio;

import java.io.*;
import javax.media.*;

public class mp3 extends Thread {
	
	//global variables
	private Player MP3;
	String FilePath = "";
	
	
	public mp3(String fp) {
		FilePath = fp;
	}
	
	public void run(){
		try{
			MP3 = Manager.createPlayer(this.getClass().getResource(FilePath));
		}
		catch(java.io.IOException e)
		{ System.out.println(e.getMessage());}
		
		catch(javax.media.NoPlayerException e)
		{ System.out.println(e.getMessage());}
		
		MP3.addControllerListener(new ControllerListener(){
			public void controllerUpdate(ControllerEvent e){
				if(e instanceof EndOfMediaEvent){
					MP3.stop();
					MP3.close();
				}
			}
		});
		MP3.realize();
		MP3.start();
	}
		

}
