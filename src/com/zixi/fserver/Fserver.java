package com.zixi.fserver;

import java.net.*;
import java.util.HashMap;
import java.io.*;

public class Fserver {
	
	static private final int 					PORT_NUMBER		=	4445;

    // Entry point.
	public static void main(String[] args) throws IOException {
		
		System.out.println("Hello I'm listening to: " + PORT_NUMBER);
       
		boolean listening = true;
        
        // Open a socket.
        try (ServerSocket serverSocket = new ServerSocket(PORT_NUMBER)) // The try-with-resources Statement. Java SE 7.
        {   
        	while (listening) 
            {
	            Socket 		 socket 		= 	serverSocket.accept(); // Blocking there.
	            WorkerTherad workerTherad 	= 	new WorkerTherad(socket);
	            workerTherad.start();
	        }
	    }
        catch (IOException e) {
            System.err.println("Could not listen on port " + PORT_NUMBER);
            System.exit(-1);
        }
    }
}
