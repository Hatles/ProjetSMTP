package com.polytech.smtp.utils;

import com.polytech.smtp.server.stockage.Header;
import com.polytech.smtp.server.stockage.Message;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class Utils
{
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	
//	public static Method getMethod(String line)
//	{
//		String[] args = line.split(" ");
//
//		switch(args[0])
//		{
//			case "GET":
//				return Method.GET;
//			case "HEAD":
//				return Method.HEAD;
//			default:
//				return Method.UNKOWN;
//		}
//	}
	
	public static File getFile(File root, String line)
	{
		String file = getArg(line, 1);
		String dir = root.getAbsolutePath() + file.replaceAll("%20", " ");
		return new File(dir);
	}
	
	public static String[] getArgs(String line)
	{
		return line.split(" ");
	}
	
	public static String getArg(String line, int i)
	{
		return line.split(" ")[i];
	}
	
	public static String getDate()
	{
		Calendar calendar = Calendar.getInstance();
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	        "EEE, dd MMM yyyy HH:mm:ss z");
	    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    return dateFormat.format(calendar.getTime());
	}
	
	public static String getContentType(File file)
	{
		String extension = "";

		int i = file.getName().lastIndexOf('.');
		if (i > 0) {
		    extension = file.getName().substring(i+1);
		}
		extension = extension.toLowerCase();
		
		switch(extension)
		{
		case "txt":
			return "text";
			
		case "png":
		case "jpg":
		case "jpeg":
		case "gif":
		case "bmp":
			return "image";
			
		case "mp3":
		case "wav":
			return "audio";
			
		case "html":
		case "php":
			return "text/html";
		
		default:
			return "application/octet-stream";
		}
	}

	public static boolean verifHTTP(String[] versions, String line)
	{
		String v = getArg(line, 2);
		String[] args = v.split("/");
		
		if(args.length == 2)
		{
			if(args[0].toLowerCase().equals("http") && arrayStringContains(versions, args[1]))
				return true;
		}
		return false;
	}
	
	public static boolean arrayStringContains(String[] versions, String v)
	{
		for(int i = 0; i < versions.length; i++)
		{
			if(versions[i].equals(v))
				return true;
		}
		return false;
	}
	
	public static String getPageError(int code, String title)
	{
		String page = "<html><head><title>"+code+" "+title+"</title>";
		page += "<style type=\"text/css\">";
		page += "#fof{display:block; position:relative; width:100%; height:529px; line-height:1.6em; text-align:center;}";
		page += "#fof .positioned{padding-top:80px;}";
		page += "#fof .hgroup{margin-bottom:20px; text-transform:uppercase;}";
		page += "#fof .hgroup h1{margin-bottom:0; font-size:160px; line-height:120px;}";
		page += "#fof .hgroup h2{margin-bottom:0; font-size:80px;line-height:80px;}";
		page += "#fof p{display:block; margin:0 0 25px 0; padding:0; font-size:16px;}";
		page += "</style></head><body><div class=\"wrapper row2\"><div id=\"container\" class=\"clear\">";
		page += "<section id=\"fof\" class=\"clear\"><div class=\"positioned\"><div class=\"hgroup\">";
		page += "<h1>!</h1><h2>"+code+"<br>"+title+"</h2></div></div></section></div></div></body></html>";
		return page;
	}

	public static boolean isForbiddenFile(String file)
	{
		return(file.contains(".."));
	}

	public static String getDateExpires(int i)
	{
		if(i <= 0)
			i = 1;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, i);
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	        "EEE, dd MMM yyyy HH:mm:ss z");
	    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    return dateFormat.format(calendar.getTime());
	}

    public static String getCommand(List<String> lines) {
		String[] words = lines.get(0).split(" ");
		return words[0].toUpperCase();
    }

    public static String buildMessage(Message message)
	{
	    String msg = "";
        for (Header header : message.getHeaders()) {
            msg += header.getTitle() + ": " + header.getValue() + "\r\n";
        }

        msg += "\r\n";
        msg += message.getMessage() + "\r\n";
        msg += ".\r\n";
        return msg;
	}
	
//	public static Message buildMessage(String data)
//	{
//	    Message message = new Message();
//	    String[] lines = data.split("\r\n");
//
//		for (int i = 0; i < ; i++) {
//
//		}
//		return
//	}

	public static String bytesToHex(byte[] bytes) {
		char[] hexArray = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static String createTimestamp(String serverName)
	{
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return "<"+ Double.toString(((int)(Math.random()*9999)))+"."+timestamp.getTime() + "@"+serverName+">";
	}
}
