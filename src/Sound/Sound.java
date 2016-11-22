package Sound;

import java.io.*;
import javax.sound.sampled.*;

public class Sound extends Thread{

	String filename;
	
	public void play(String wav){
		filename = wav;
	}
	
	public void run(){
		while(true){
			File file = new File(filename);
			AudioInputStream a = null;
			try {
				a = AudioSystem.getAudioInputStream(file);
			} catch (UnsupportedAudioFileException e){
				e.printStackTrace();
				return;
			} catch (IOException e){
				e.printStackTrace();
				return;
			}
			AudioFormat format = a.getFormat();
			SourceDataLine auline = null;
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			try {
				auline = (SourceDataLine)AudioSystem.getLine(info);
				auline.open(format);
			} catch (Exception e){
				e.printStackTrace();
				return;
			}
			auline.start();
			System.out.println(auline.getLevel());
			int nBytesRead = 0;
			byte[] abData = new byte[512];
			try {
				while(nBytesRead != -1){
					nBytesRead = a.read(abData, 0, abData.length);
					if(nBytesRead >= 0){
						auline.write(abData, 0, nBytesRead);
					}
				}
			} catch (Exception e){
				e.printStackTrace();
				return ;
			} finally {
				auline.drain();
				auline.close();
			}
		}
	}
	
}
