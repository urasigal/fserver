package com.zixi.fserver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ThreadHunter extends Thread{
	
	private HashMap<WorkerTherad, Long> timedThreads = new HashMap<>(); 
	
	public ThreadHunter(HashMap<WorkerTherad, Long> timedThreads)
	{
		this.timedThreads = timedThreads;
	}
	
	
	 public HashMap<WorkerTherad, Long> getTimedThreads() {
		return timedThreads;
	}

	public void setTimedThreads(HashMap<WorkerTherad, Long> timedThreads) {
		this.timedThreads = timedThreads;
	}

	public void run() 
	{
		while(true)
		{
			try 
			{
				Thread.currentThread().sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Iterator it = timedThreads.entrySet().iterator();
			System.out.println("hunt");
			System.out.println("Map size " + timedThreads.size());

			while (it.hasNext()) 
			{
		        Map.Entry pair = (Map.Entry)it.next();
		        System.out.println(pair.getKey() + " = " + pair.getValue());
		        
		        System.out.println("Old time is " + ((Long)pair.getValue()) + 40000);
		        Long curr = System.currentTimeMillis(); 
		        System.out.println("Current time is "  + curr);
		        Long diff = ((Long)pair.getValue()) + 40000 - curr;
		        System.out.println("Difference is  " + diff);
		        if(diff < 0)
		        {
		        	WorkerTherad thrd = (WorkerTherad)pair.getKey();
		        	System.out.println("The thread is" + thrd.getName());
		        	//((WorkerTherad)pair.getKey()).interrupt();
		        	it.remove(); 
		        }
		    }
		}
	}
}
