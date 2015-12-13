package com.zixi.fserver;

import java.net.*;
import java.io.*;
 
public class WorkerTherad extends Thread {
    private Socket socket = null;
    private BufferedReader externalBuffer;
    
    public WorkerTherad(Socket socket) {
        super("worker thread");
        this.socket = socket;
    }
     
    public void run() {
 
        try (
	            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	            BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
        	)
        {
        	ScriptExecutor scriptExecutor = new ScriptExecutor();
            String  outputLine;
            String  inputLine;
            outputLine = "connected"; // If returned then a connection was established.
            out.println(outputLine);
 
            while ((inputLine = in.readLine()) != null) {
            	out.println("output");
                out.println(scriptExecutor.runScript(inputLine));
                out.println("accepted");
                if (outputLine.equals("Bye"))
                    break;
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
        	System.out.println("Client bye !!!");
        }
    }
}