package com.polytech.smtp.server;


import com.polytech.smtp.utils.observer.Observable;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server extends Observable
{
	private int port;
	private ServerSocket socket;
	private boolean running;
	private File rootDir;

	private List<CommunicationRunnable> clients;
	
	private AcceptClient accept;
	private String name;

	private DateFormat dateFormat;

	final String[] enabledCipherSuites = {"SSL_DH_anon_WITH_RC4_128_MD5"};

	public Server(int port)
	{
		this.port = port;
		running = false;
		accept = new AcceptClient(this);
		clients = new ArrayList<CommunicationRunnable>();
		name = "POP3S_ToupieLicorne_v0.95";
		rootDir = new File(System.getProperty("user.home"));

		dateFormat = new SimpleDateFormat("[HH:mm:ss]");
//		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	}
	
	public void start()
	{
		try 
		{
			//socket = new ServerSocket(port);
			SSLServerSocketFactory fab = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			socket = fab.createServerSocket(port);
			((SSLServerSocket)socket).setEnabledCipherSuites(((SSLServerSocket)socket).getSupportedCipherSuites());
            ((SSLServerSocket)socket).setNeedClientAuth(false);
			Thread t = new Thread(accept);
			t.setDaemon(true);
			t.start();
			log("Serveur running on port : "+ port + " with name " + name);
			running = true;
		} catch (IOException e) {
			
			e.printStackTrace();
			log(e.getMessage());
		}
	}
	
	public void stop()
	{
		accept.stop();
		for (CommunicationRunnable client : clients)
		{
			client.stop();
		}

		try
		{
			socket.close();
			running = false;
			log("Server stopped");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			log(e.getMessage());
		}
	}
	
	public void log(String message)
	{
		if(message != null)
			this.notifyObservers(this.getDate()+" "+message+"\r\n");
	}

	public String getDate()
	{
		return this.dateFormat.format(new Date());
	}
	
	public ServerSocket getServerSocket()
	{
		return this.socket;
	}

	public File getRootDir()
	{
		return rootDir;
	}

	public void setRootDir(File rootDir)
	{
		this.rootDir = rootDir;
		this.log("Messages file dir set to : " + rootDir.getAbsolutePath());
	}
	
	public void addClient(CommunicationRunnable client)
	{
		this.clients.add(client);
		log("Nouveau client connecté : "+client.getID());
	}

	public void removeClient(CommunicationRunnable client)
	{
		this.clients.remove(client);
		log("Client " + client.getID() +" déconnecté.");
	}
	
	public void setPort(int port)
	{
		this.port = port;
		//this.notifyObservers(null);
	}

	public int getPort()
	{
		// TODO Auto-generated method stub
		return port;
	}
	
	public boolean isRunning()
	{
		return running;
	}

	public String getServerName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
		//this.notifyObservers(null);
	}

	public void close()
	{
		log("Deconection du serveur");
		this.accept.close();
		for (CommunicationRunnable client : clients) {
			client.close();
		}
		log("Tous les clients ont ete deconectés");
	}
}
