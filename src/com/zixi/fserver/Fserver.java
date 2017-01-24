package com.zixi.fserver;

import java.net.*;
import java.util.HashMap;
import java.io.*;

public class Fserver {
	
	static private final int 					PORT_NUMBER		=	4445;
	static private HashMap<WorkerTherad, Long> 	timedThreads 	= 	new HashMap<>();

    public static HashMap<WorkerTherad, Long> getTimedThreads() { return timedThreads; }

    // Entry point.
	public static void main(String[] args) throws IOException {
		
		System.out.println("Hello I'm listening to: " + 4445);
       
		boolean listening = true;
        ThreadHunter threadHunter = new ThreadHunter(timedThreads);
        
        // Open a socket.
        try (ServerSocket serverSocket = new ServerSocket(PORT_NUMBER)) // The try-with-resources Statement. Java SE 7.
        { 
        	threadHunter.start();
            
        	while (listening) 
            {
	            Socket socket 				= 	serverSocket.accept(); // Blocking there.
	            WorkerTherad workerTherad 	= 	new WorkerTherad(socket);
	            Long time 					= 	System.currentTimeMillis();
	            System.out.println("Sys time " + time);
	            Fserver.getTimedThreads().put(workerTherad, System.currentTimeMillis());
	            workerTherad.start();
	        }
	    }
        catch (IOException e) {
            System.err.println("Could not listen on port " + PORT_NUMBER);
            System.exit(-1);
        }
    }
}
