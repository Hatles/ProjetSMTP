package com.polytech.smtp.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kifkif on 15/02/2017.
 */
abstract class CommunicationRunnable extends ServerRunnable implements Communication
{
    private static int count = 0;

    private Socket socket;

    private BufferedReader in;
    private OutputStreamWriter out;

    private boolean firstLoop;

    int id;

    public CommunicationRunnable(Server server, Socket socket)
    {
        super(server);
        this.socket = socket;
        id = count++;
        this.initBuffers();
        firstLoop = true;
    }

    public void initBuffers()
    {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new OutputStreamWriter(new BufferedOutputStream(socket.getOutputStream()), "UTF-8");
        } catch (IOException e)
        {
            e.printStackTrace();
            log(e.getMessage());
            close();
        }
    }

    @Override
    protected void loop()
    {
        if(firstLoop)
        {
            onStart();
            firstLoop = false;
        }

        try
        {
            String line;
            List<String> lines = new ArrayList<String>();

            log("waiting message");
            boolean reading = true;
            int nullLine = 0;
            boolean head = true;
            while(reading)
            {
                line = in.readLine();

                if(line == null)
                {
                    reading = false;
                    this.stop();
                }
                else if(line.length() > 0)
                {
                    lines.add(line);
                    nullLine = 0;
                    head = false;
                }
                else if(!head)
                {
                    nullLine++;
                    if(nullLine >= 1)
                        reading = false;
                    else lines.add(line);
                }
            }

            if(lines.size() > 0)
            {
                log("message received : "+lines.get(0)+"...");
                onClientCommunication(lines);
            }

        } catch (IOException e)
        {
            //e.printStackTrace();
            log(e.getMessage());
            this.stop();
        }
    }

    protected abstract void onStart();

    protected abstract void onClientCommunication(List<String> lines);

    public void log(String m)
    {
        server.log(this.getTag() +" : "+ m);
    }

    public int getID()
    {
        return id;
    }

    @Override
    protected String getTag()
    {
        return "[Client-"+id+"]";
    }

    public void close()
    {
        log("closing");
        this.onClose();
        try
        {
            socket.close();
        } catch (IOException e)
        {
            log("Error during closing : "+e.getMessage());
            e.printStackTrace();
        }
        server.removeClient(this);
        log("closed");
    }

    protected abstract void onClose();

    public void writeLine(String message) throws IOException
    {
        write(message+"\r\n");//CR LF
    }

    public void write(String message) throws IOException
    {
        out.write(message);
    }

    public void send(String message) throws IOException
    {
        if(message != null && !message.equals(""))
            writeLine(message);
        writeLine("");
        writeLine("");
        out.flush();
        log("sending message");
    }

    public void send() throws IOException
    {
        send("");
    }
}
