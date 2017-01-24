package com.zixi.fserver;

import java.net.*;
import java.io.*;
 
public class WorkerTherad extends Thread {
    private Socket 			socket 			= null;
    
    public WorkerTherad(Socket socket) { super("worker thread");  this.socket = socket; }
    
    // this function will be run on each a new request to the server.
    public void run() {
 
        try (
	            PrintWriter 	out 	= 	new PrintWriter(socket.getOutputStream(), true);
	            BufferedReader 	in 		= 	new BufferedReader( new InputStreamReader(socket.getInputStream()));
        	)
        {
        	ScriptExecutor 	scriptExecutor = new ScriptExecutor();
            String  		outputLine;
            String  		inputLine;
           
            outputLine = "connected"; // If returned then a connection was established.
            out.println(outputLine);
 
            while ((inputLine = in.readLine()) != null) {
            	out.println("output");
            	Object arr[] ;
            	arr = scriptExecutor.runScript(inputLine);
            	out.println( (Integer) arr[0]);
            	out.println( (String)  arr[1]);
            			 
//            	// In a case of HLS the "inputLine" equals to HLS URL.
//                out.println( (Integer)scriptExecutor.runScript(inputLine)[0] ); 
//                
//                out.println((String)scriptExecutor.runScript(inputLine)[1]);
               
                if (outputLine.equals("Bye"))
                    break;
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{ System.out.println("Client bye !!!"); }
    }
}