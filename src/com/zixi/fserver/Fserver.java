package com.zixi.fserver;

import java.net.*;
import java.util.HashMap;
import java.io.*;

public class Fserver {
	
	static private HashMap<WorkerTherad, Long> timedThreads = new HashMap<>();

	
    public static HashMap<WorkerTherad, Long> getTimedThreads() {
		return timedThreads;
	}

	public static void main(String[] args) throws IOException {

        int portNumber = Integer.parseInt("4445");
        boolean listening = true;
        ThreadHunter threadHunter = new ThreadHunter(timedThreads);
        try (ServerSocket serverSocket = new ServerSocket(portNumber))
        { 
        	threadHunter.start();
            while (listening) 
            {
            	
	            Socket socket = serverSocket.accept();
	            WorkerTherad workerTherad = new WorkerTherad(socket);
	            Long time = System.currentTimeMillis();
	            System.out.println("Sys time " + time);
	            Fserver.getTimedThreads().put(workerTherad, System.currentTimeMillis());
	            workerTherad.start();
            
	        }
	    } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}
