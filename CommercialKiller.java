// Jesse Campbell
// 2015-04-07
// www.jbcse.com

// This program records commercials and attempts detect to detect repeated commercials.  Not currently working, stereo mix is noisy and doesn't repeat perfectly.
// It listens to audio via stereo mix.  Not all computers have stereo mix.
// Keyboard commands: "s" to save a commercial (saves last bufferSizeSeconds seconds, default is 5), "q" to close safely
// Requires: C:/Program Files/AutoHotkey/AutoHotkey.exe

import javax.sound.sampled.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
 
public class CommercialKiller {
    
	float sampleRate = 16000;
    int sampleSizeInBits = 16;
    int channels = 1;
    boolean signed = true;
    boolean bigEndian = true;
    TargetDataLine line; // the line from which audio data is captured
    AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,channels, signed, bigEndian);
    AudioInputStream ais;
    BufferedOutputStream pcmFile;
    ByteArrayQueue byteArrayQueue;
    int bufferSizeSeconds = 5;
    
    static final long RECORD_TIME = -1;//60*1000; // record duration, in milliseconds
    boolean shouldStop = false;
    boolean saveCommercial = false; // used get an exit signal from another thread
    ArrayList<Commercial> commercials;
    
    String logFile = "log.txt";
    FileWriter fileWriter;
    
    String autoHotkeyPath = "\"C:/Program Files/AutoHotkey/AutoHotkey.exe\"";
    String autoHotkeyScript = "C:/Users/Jesse/workspace/CommercialKiller/bin/killCommercialsAutomatically.ahk";
    String commercialAudioPath = "C:/Users/Jesse/workspace/CommercialKiller/bin/commercials/";
    
    public static void main(String[] args) throws IOException, InterruptedException {
    	new CommercialKiller().start();
    }
    
    void start() {
        try {
        	FileWriter fileWriterLogFile = new FileWriter(logFile,true); // append
        	
        	new KeyboardInterface(this).start();
        	commercials = new ArrayList<Commercial>();
        	commercials.add(new Commercial(commercialAudioPath+"progressive.pcm"));
        	commercials.add(new Commercial(commercialAudioPath+"progressive2.pcm"));
        	commercials.add(new Commercial(commercialAudioPath+"walgreens.pcm"));
        	commercials.add(new Commercial(commercialAudioPath+"auto.pcm"));
        	commercials.add(new Commercial(commercialAudioPath+"man-card-night.pcm"));
        	commercials.add(new Commercial(commercialAudioPath+"john.pcm"));
        	commercials.add(new Commercial(commercialAudioPath+"todays-the-day.pcm"));
        	
        	pcmFile = new BufferedOutputStream(new FileOutputStream("audio-with-commercials-"+System.currentTimeMillis()+".pcm"));
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
 
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }
            
            line = (TargetDataLine) AudioSystem.getLine(info);
            
            if (RECORD_TIME > 0) 
            	new Stopper(this, RECORD_TIME).start();
            
            line.open(format);
            line.start();   // start capturing
            
            System.out.println("Start capturing...");
 
            byteArrayQueue = new ByteArrayQueue((int) (bufferSizeSeconds*sampleSizeInBits/8*sampleRate)); // queue size
            ais = new AudioInputStream(line);
            
            byte[] buffer = new byte[(int)(0.5*sampleRate*sampleSizeInBits/8)]; // buffer size of half a second
            int bytesRead;
            long totalBytesRead = 0;
            
            System.out.println("Start recording/listening...");
            
            do{
            	bytesRead = ais.read(buffer, 0, buffer.length);
            	totalBytesRead += bytesRead;
            	
            	byteArrayQueue.add(Arrays.copyOfRange(buffer, 0, bytesRead));
            	pcmFile.write(buffer,0,bytesRead);         	
            	System.out.println(totalBytesRead);
            	
            	if (saveCommercial == true)
            		saveCommercial();
            	
            	for(int i=0; i<commercials.size(); i++){
            		if (foundSubArray(byteArrayQueue.queue,commercials.get(i).data)){
            			System.out.println("Commercial detected: #"+i);
            	    	Runtime.getRuntime().exec(autoHotkeyPath+" "+autoHotkeyScript);
            	    	saveCommercial();
            	    	fileWriterLogFile.write(System.currentTimeMillis()+", Commercial found! #"+i);
            	    	break;
            		}
            	}
            	
            }
            while(shouldStop == false);
            
            System.out.println("Stop recording...");
            
            line.stop();
    	    line.close();
    	    ais.close();
    	    
    	    pcmFile.flush();
    	    pcmFile.close();
    	    
    	    //playQueue();
    	    //printLastFewQueueBytes(10);    	    
    	    
    	    fileWriterLogFile.close();
    	    System.out.println("Exited");
    	    System.exit(0);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    void saveCommercial(){
    	
    	String path = ""+System.currentTimeMillis()+".pcm";
    	
    	try {
			FileOutputStream stream = new FileOutputStream(path);
			stream.write(byteArrayQueue.queue);
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	static boolean foundSubArray(byte[] array, byte[] subArray){

		if (subArray.length <= array.length){
			searching:
				for(int i=0; i<array.length; i++){
					if (subArray[0] == array[i]){
						
						if (subArray.length == 1)
							return true;

						for(int j=1; j<subArray.length; j++){
							if (i+j < array.length && array[i+j] == subArray[j]){
								
								if (j==subArray.length-1)
									return true;
							}
							else								
								continue searching;
							
						}
					}
				}
		}
		
		return false;
		
	}
	
	void playQueue(){
	    System.out.println("Start playing...");
	    
	    SourceDataLine lineOut;
		try {
			lineOut = AudioSystem.getSourceDataLine(format);
		    lineOut.open(format);
		    lineOut.start();
		    lineOut.write(byteArrayQueue.queue,0,byteArrayQueue.queue.length);
		    lineOut.drain();
		    lineOut.close();    
		} catch (Exception e) { 
			e.printStackTrace();
		}
	    
		System.out.println("Stopped playing...");	    
	    
	}
	
	void printLastFewQueueBytes(int bytes){
		byte[] lastFewBytes = new byte[bytes];
	    lastFewBytes = Arrays.copyOfRange(byteArrayQueue.queue, byteArrayQueue.queue.length-lastFewBytes.length, byteArrayQueue.queue.length);
	    
	    String lastFewBytesFormated = "[";
	    for(int i=0; i<lastFewBytes.length; i++)
	    	lastFewBytesFormated += String.format("%02X",lastFewBytes[i]) + ",";
	    
	    lastFewBytesFormated = "Last few bytes: "+lastFewBytesFormated.substring(0,lastFewBytesFormated.length()-1)+"]";
	    System.out.println(lastFewBytesFormated);
	}
}