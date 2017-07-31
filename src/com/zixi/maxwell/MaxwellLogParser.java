package com.zixi.maxwell;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class MaxwellLogParser {	
	
	private String 			fileURL = null; // Path to log file.
	private BufferedReader 	logFileReader = null; // Read from log file 

	
	/**
	 * Returns total running time recorded it that Maxwell log file. 
	 * @param url Path to log file.
	 * @return
	 */
	public long getRunningTimeFromLogFile(String url) 
	{
		String line    = null;
		long startTiem = 0;
		long endTme    = 0;
		long totalTiem = -1;
		
		LinkedList<String> stack = new LinkedList<>();
		try {
			if (openLogFile(url))
			{
				while ((line = logFileReader.readLine()) != null) {
					stack.push(line);
					if(line.startsWith("==>S") && startTiem == 0)
					{
						startTiem = Long.parseLong(line.split(" ")[1].split("\\.")[0]);
					}
				 }
				
				while (true) {
					line = stack.pop();
					try {
						if(line.startsWith("==>S") && endTme == 0)
						{
							endTme = Long.parseLong(line.split(" ")[1].split("\\.")[0]);
							break; 
						} }catch(NoSuchElementException e) { break; }
				 }// end while
				if(startTiem != 0 && endTme !=0)
				{
					totalTiem = endTme - startTiem;
				}
				closeLogFile();
			} else System.out.println("The log file was not opened");
			}catch(Exception e) 
			{
				System.out.println(e.getMessage());
				closeLogFile();
			}
		return totalTiem;
	}
	
	public int getNumberOfDroptsFromLogFile(String url) throws Exception
	{
		LinkedList<String> 	stack 					= new LinkedList<>();
		String 				lineFromLog 			= null;
		int 				numberOfDroppedPackets 	= -1;
		
		if (openLogFile(url))
		{
			while ((lineFromLog = logFileReader.readLine()) != null) {
				stack.push(lineFromLog);
			 }
		
			if(stack.size() > 0)
			{
				while (true) {
					lineFromLog = stack.pop();
					try {
						if(lineFromLog.startsWith("==>S") )
						{
							String [] statisticLineFromLogfile = lineFromLog.split(" ");
							if(statisticLineFromLogfile.length > 10) //avoid accidentally abrupted file ending.
							{
								numberOfDroppedPackets = Integer.parseInt(statisticLineFromLogfile[10]);
								break;
							}
						} }catch(NoSuchElementException e) { break; }
				 }// end while
			} else throw new Exception("Log file too small - cannot be parsed");
			closeLogFile();
			return numberOfDroppedPackets;
		}else throw new Exception("Can't open log file");
	}
	
	private boolean openLogFile(String url) 
	{
		fileURL  = url;
		if(fileURL != null)
		{
			try { logFileReader = new BufferedReader(new FileReader(fileURL)) ;
			} catch (FileNotFoundException e) { e.printStackTrace(); return false;
			} catch (IOException e) { e.printStackTrace(); return false; } 
			return true;
		} else {System.out.println("no url is provided");}  
		return false;
	}
	
	private  void closeLogFile() {
		try { logFileReader.close();}  catch (IOException e) {e.printStackTrace(); }
	}
	
	public static void main(String[] args) throws Exception {
		MaxwellLogParser maxwellLogParser = new MaxwellLogParser();
		
		System.out.println( maxwellLogParser.getRunningTimeFromLogFile("C://Users//urasi//Desktop//log"));
		System.out.println(maxwellLogParser.getNumberOfDroptsFromLogFile("C://Users//urasi//Desktop//log"));
		
	}
}
