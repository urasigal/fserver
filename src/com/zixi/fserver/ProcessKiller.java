package com.zixi.fserver;

public class ProcessKiller extends Thread {

	private Process process;
	public ProcessKiller (Process process) 
	{
		this.process = process;
	}
	
	public void run()
	{
		try {
			Thread.currentThread().sleep(40000);
			process.destroy();
			return;
		} 
		catch (InterruptedException e) {
			return;
		}
	}
	
}
