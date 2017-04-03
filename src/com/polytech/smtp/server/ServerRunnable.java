package com.polytech.smtp.server;
public abstract class ServerRunnable implements Runnable {

	protected Server server;
	private boolean running;
	
	public ServerRunnable(Server server)
	{
		this.server = server;
	}
	
	protected void log(String message)
	{
		server.log(this.getTag() +" : "+ message);
	}
	
	public void run() 
	{
		running = true;
        while(running){
        	loop();
        }
	}
	
	protected abstract void loop();
	
	protected Server getServer()
	{
		return server;
	}
	
	public synchronized void stop()
	{
        this.close();
		this.running = false;
	}
	
	protected abstract String getTag();

	public abstract void close();
}
