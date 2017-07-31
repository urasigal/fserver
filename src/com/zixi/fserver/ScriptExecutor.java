package com.zixi.fserver;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.zixi.maxwell.MaxwellLogParser;

import static com.zixi.fserver.Fmacros.*; 

public class ScriptExecutor {
	
	private static final int 	STREAMISGOOD  = 1;
	static private final int 	STREAMISBAD   = 0 ; 
	private String 				ex 			  = "bug";
	private Process 			process;
	
	public Object[] runScript(String inputFromClient) 
	{
		StringBuffer 		sb 		= new StringBuffer();  
	    StringBuffer 		sberr 	= new StringBuffer(); 
		System.out.println("Trying to run a script ....");
		System.out.println("Input from client is :" + inputFromClient );
		try {
			// -i filename (input)
			// -y (global) Overwrite output files without asking.
			// -t duration (input/output) When used as an output option (before an output filename), stop writing the output after its duration reaches duration.
			// -ss position (input/output) When used as an output option (before an output filename), decodes but discards input until the timestamps reach position.
			if(inputFromClient.endsWith("m3u8"))
			{
				process = Runtime.getRuntime().exec("/root/ffmpeg_sources_new/ffmpeg/ffmpeg  -i " + inputFromClient  + " -y -f image2 -t 0.001 -ss 00:00:4 -s 640*480 /var/screen/testimg1.jpg");
			}
			else
				if(inputFromClient.endsWith("flv"))
				{
					process = Runtime.getRuntime().exec("/root/ffmpeg_sources_new/ffmpeg/ffmpeg  -i " + inputFromClient  + " -y -f image2 -t 0.001 -ss 00:00:4 -s 640*480 /var/screen/testimg1.jpg");
				}
			else
				if(inputFromClient.endsWith("analyze"))
				{
					process = Runtime.getRuntime().exec("/root/ffmpeg_sources_new/ffmpeg/ffprobe -i  udp://10.7.0.150:5554 -v quiet -print_format json -show_format -show_streams -hide_banner");
					
					InputStream 		is 		= 	process.getInputStream();
					InputStreamReader  	isr 	= 	new InputStreamReader(is);
					String line;
					BufferedReader 		br 		= 	new BufferedReader(isr);
					BufferedReader 		bre 	= 	new BufferedReader(new InputStreamReader(process.getErrorStream()));
					
					while ((line = br.readLine()) != null) 
					{
						sb.append(line);
						if(Thread.currentThread().isInterrupted())
						{
							throw new IOException("Interupted thread");
						}
						 System.out.println( "output is -------------------- " + line);
					}
					
					while ((line = bre.readLine()) != null) 
					{
						if(!line.startsWith("["))
						{
							
						}
						sb.append(line);
						if(Thread.currentThread().isInterrupted())
						{
							System.out.println("I'm dead ............................................");
							return new Object[] {0,""};
						}
					}
					return new Object[] {1,sb.toString()};
				}
				else
			if(inputFromClient.endsWith("maxwell"))
			{
				MaxwellLogParser maxwellLogParser = new MaxwellLogParser();
				
				try {
					return new Object [] {new Long(maxwellLogParser.getRunningTimeFromLogFile("//root//log")), 
					new Integer(maxwellLogParser.getNumberOfDroptsFromLogFile("//root//log"))};
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
			else
			{
				process = Runtime.getRuntime().exec("/root/ffmpeg_sources_new/ffmpeg/ffmpeg -i udp://10.7.0.150:5555 -y -f image2 -t 0.001 -ss 00:00:4 -s 640*480 /var/screen/testimg.jpg");
			}
			
			InputStream 		is 		= 	process.getInputStream();
			InputStreamReader  	isr 	= 	new InputStreamReader(is);
			String line;
			BufferedReader 		br 		= 	new BufferedReader(isr);	
			BufferedReader 		bre 	= 	new BufferedReader(new InputStreamReader(process.getErrorStream()));
			
			new ProcessKiller(process).start();
			while ((line = br.readLine()) != null) 
			{
				sb.append(line);
				if(Thread.currentThread().isInterrupted())
				{
					throw new IOException("Interupted thread");
				}
				 System.out.println( "output is -------------------- " + line);
			}
			
			while ((line = bre.readLine()) != null) 
			{
				sberr.append(line);
				if(Thread.currentThread().isInterrupted())
				{
					System.out.println("I'm dead ............................................");
					return new Object[] {0,""};
				}
				
				if (line.toLowerCase().contains("Missing reference picture, default".toLowerCase()))
				{
					System.out.println("Hello from server - cause is: " + line);
					return new Object[] {0,line};
				}
				if (line.toLowerCase().contains("left block unavailable for".toLowerCase()))
				{
					System.out.println("Hello from server - cause is: " + line);
					return new Object[] {0,line};
				}
				if (line.toLowerCase().contains("error while decoding".toLowerCase()))
				{
					System.out.println("Hello from server - cause is: " + line);
					return new Object[] {0,line};
				}
				if (line.toLowerCase().contains("Invalid data found when processing input".toLowerCase()))
				{
					System.out.println("Hello from server - cause is: " + line);
					return new Object[] {0,line};
				}
				if (line.toLowerCase().contains("HTTP error 404 Not Found".toLowerCase()))
				{
					System.out.println("Hello from server - cause is: " + line);
					return new Object[] {0,line};
				}	
				System.out.println("error output ---------------------- >" + line);
			}
			return new Object[] {1, "FFMPEG - no errors"};
			
		} catch (IOException e) { 
			// TODO Auto-generated catch block
			ex = e.getMessage();
			System.out.println("Exception output ---------------------- >" + ex);
			return new Object[] {0,ex};
		}
		finally
		{
			//return STREAMISBAD;
			
		}
	}
}
