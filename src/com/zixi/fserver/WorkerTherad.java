package com.zixi.fserver;

import java.net.*;
import java.io.*;
 
public class WorkerTherad extends Thread {
    private Socket 		socket 	= null;
    private Object 		arr[];
    public  WorkerTherad(Socket socket) { super("worker thread");  this.socket = socket; }
    
    // this function will be run on each a new request to the server.
    public void run() {
 
        try (
	            PrintWriter 	out 	= 	new PrintWriter(socket.getOutputStream(), true);
	            BufferedReader 	in 		= 	new BufferedReader( new InputStreamReader(socket.getInputStream()));
        	)
        {
        	ScriptExecutor 	scriptExecutor = new ScriptExecutor();
            String  		inputLine;
            out.println("connected");
            
            while ((inputLine = in.readLine()) != null) {
            	out.println("output");
            	
            	arr = scriptExecutor.runScript(inputLine);
            	out.println( (Integer) arr[0]);
            	out.println( (String)  arr[1]);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{ System.out.println("Client bye !!!"); }
    }
}