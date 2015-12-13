package com.zixi.fserver;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static com.zixi.fserver.Fmacros.*; 

public class ScriptExecutor {
	
	private static final int STREAMISGOOD  = 1;
	static private final int STREAMISBAD   = 0 ; 
	
	private String ex  = "bug";
    private BufferedReader bre;
	private Process process;
	public int runScript(String inputFromClient) 
	{
		StringBuffer sb = new StringBuffer();  
	    StringBuffer sberr = new StringBuffer(); 
		System.out.println("Trying to run a script ....");
		System.out.println("input from client is :" + inputFromClient );
		try {
			// -i filename (input)
			// -y (global) Overwrite output files without asking.
			// -t duration (input/output) When used as an output option (before an output filename), stop writing the output after its duration reaches duration.
			// -ss position (input/output) When used as an output option (before an output filename), decodes but discards input until the timestamps reach position.
			if(inputFromClient.equals(HLS))
			{
				process = Runtime.getRuntime().exec("/root/ffmpeg_sources/ffmpeg/ffmpeg  -i http://10.7.0.91:7777/feederout.m3u8 -y -f image2 -t 0.001 -ss 00:00:4 -s 640*480 /var/screen/testimg1.jpg");
			}
			else
				if(inputFromClient.equals(FLV))
				{
					process = Runtime.getRuntime().exec("/root/ffmpeg_sources/ffmpeg/ffmpeg -i http://10.7.0.91:7777/test.flv -y -f image2 -t 0.001 -ss 00:00:4 -s 640*480 /var/screen/testimg.jpg");
				}
				else
			{
				process = Runtime.getRuntime().exec("/root/ffmpeg_sources/ffmpeg/ffmpeg -i udp://10.7.0.150:5555 -y -f image2 -t 0.001 -ss 00:00:4 -s 640*480 /var/screen/testimg.jpg");
			}
			InputStream is = process.getInputStream();
			InputStreamReader  isr = new InputStreamReader(is);
			String line;
			BufferedReader br = new BufferedReader(isr);	
			BufferedReader bre = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			new ProcessKiller(process).start();
			while ((line = br.readLine()) != null) 
			{
				sb.append(line);
				if(Thread.currentThread().isInterrupted())
				{
					return STREAMISBAD;
				}
				 System.out.println( "output is -------------------- " +line);
			}
			
			while ((line = bre.readLine()) != null) 
			{
				sberr.append(line);
				if(Thread.currentThread().isInterrupted())
				{
					System.out.println("I'm dead ...");
					return STREAMISBAD;
				}
				
				if (line.toLowerCase().contains("Missing reference picture, default".toLowerCase()))
					return STREAMISBAD;
				if (line.toLowerCase().contains("left block unavailable for".toLowerCase()))
					return STREAMISBAD;
				if (line.toLowerCase().contains("error while decoding".toLowerCase()))
					return STREAMISBAD;
				if (line.toLowerCase().contains("Invalid data found when processing input".toLowerCase()))
					return STREAMISBAD;
					
				System.out.println("error output ---------------------- >" + line);
			}
			return STREAMISGOOD;
			
		} catch (IOException e) { 
			// TODO Auto-generated catch block
			ex = e.getMessage();
			System.out.println("Exception output ---------------------- >" + ex);
			return STREAMISBAD;
		}
		finally
		{
			//return STREAMISBAD;
			
		}
	}
}
