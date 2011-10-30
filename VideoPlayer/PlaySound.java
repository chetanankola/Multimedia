
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
 
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.DataLine.Info;
 

public class PlaySound implements Runnable {
 
    private InputStream waveStream;
    private String filename;
 
    private final int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb
 
    /**
     * CONSTRUCTOR
     */
    public PlaySound(/*FileInputStream waveStream*/String filename) {
       this.filename = filename;
	//this.waveStream = waveStream;
    }
    
       public PlaySound(FileInputStream waveStream/*String filename*/) {
       //this.filename = filename;
	this.waveStream = waveStream;
    }
 
    public void play() throws PlayWaveException{
 
        //FileInputStream inputStream;
	try {
	    waveStream = new FileInputStream(filename);
	} catch (FileNotFoundException e) {
	    return;
	}
      
        
	AudioInputStream audioInputStream = null;
	try {
	    audioInputStream = AudioSystem.getAudioInputStream(this.waveStream);
	} catch (UnsupportedAudioFileException e1) {
	    throw new PlayWaveException(e1);
	} catch (IOException e1) {
	    throw new PlayWaveException(e1);
	}
 
	// Obtain the information about the AudioInputStream
	AudioFormat audioFormat = audioInputStream.getFormat();
	System.out.println(audioFormat);
	Info info = new Info(SourceDataLine.class, audioFormat);
 
	// opens the audio channel
	SourceDataLine dataLine = null;
	try {
	    dataLine = (SourceDataLine) AudioSystem.getLine(info);
	    dataLine.open(audioFormat, this.EXTERNAL_BUFFER_SIZE);
	} catch (LineUnavailableException e1) {
	    throw new PlayWaveException(e1);
	}
 
	// Starts the music :P
	dataLine.start();
 
	int readBytes = 0;
	byte[] audioBuffer = new byte[this.EXTERNAL_BUFFER_SIZE];
 
	try {
	    while (readBytes != -1) {
		readBytes = audioInputStream.read(audioBuffer, 0,
			audioBuffer.length);
		if (readBytes >= 0){
		    dataLine.write(audioBuffer, 0, readBytes);
                    System.out.println("Soundbytes Read ="+readBytes);
		}
	    }
	} catch (IOException e1) {
	    throw new PlayWaveException(e1);
	} finally {
	    // plays what's left and and closes the audioChannel
	    dataLine.drain();
	    dataLine.close();
	}
 
    }

    @Override
    public void run() {
   
   try {
	 waveStream = new FileInputStream(filename);
	} catch (FileNotFoundException e) {
	    return;
	}
        
        
	AudioInputStream audioInputStream = null;
	try {
	    audioInputStream = AudioSystem.getAudioInputStream(this.waveStream);
	} catch (UnsupportedAudioFileException e1) {
            try {
                throw new PlayWaveException(e1);
            } catch (PlayWaveException ex) {
                Logger.getLogger(PlaySound.class.getName()).log(Level.SEVERE, null, ex);
            }
	} catch (IOException e1) {
            try {
                throw new PlayWaveException(e1);
            } catch (PlayWaveException ex) {
                Logger.getLogger(PlaySound.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
 
	// Obtain the information about the AudioInputStream
	AudioFormat audioFormat = audioInputStream.getFormat();
	System.out.println(audioFormat);
	Info info = new Info(SourceDataLine.class, audioFormat);
 
	// opens the audio channel
	SourceDataLine dataLine = null;
	try {
	    dataLine = (SourceDataLine) AudioSystem.getLine(info);
	    dataLine.open(audioFormat, this.EXTERNAL_BUFFER_SIZE);
	} catch (LineUnavailableException e1) {
            try {
                throw new PlayWaveException(e1);
            } catch (PlayWaveException ex) {
                Logger.getLogger(PlaySound.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
 
	// Starts the music :P
	dataLine.start();
 
	int readBytes = 0;
	byte[] audioBuffer = new byte[this.EXTERNAL_BUFFER_SIZE];
 
	try {
	    while (readBytes != -1) {
		readBytes = audioInputStream.read(audioBuffer, 0,
			audioBuffer.length);
		if (readBytes >= 0){
		    dataLine.write(audioBuffer, 0, readBytes);
		}
	    }
	} catch (IOException e1) {
            try {
                throw new PlayWaveException(e1);
            } catch (PlayWaveException ex) {
                Logger.getLogger(PlaySound.class.getName()).log(Level.SEVERE, null, ex);
            }
	} finally {
	    // plays what's left and and closes the audioChannel
	    dataLine.drain();
	    dataLine.close();
	}
 
    }
}