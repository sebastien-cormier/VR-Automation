package fr.cormier.vra.proxyserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.cormier.domain.db.Command;
import fr.cormier.vra.service.ICommandService;

/* 
 * The ProxyThread will take an HTTP request from the client
 * socket and send it to either the server that the client is
 * trying to contact, or another proxy server
 */
class ProxyThread extends Thread
{
	private Socket pSocket;
	private String fwdServer = "";
	private int fwdPort = 0;
	private int debugLevel = 0;
	private PrintStream debugOut = System.out;
	
	// the socketTimeout is used to time out the connection to
	// the remote server after a certain period of inactivity;
	// the value is in milliseconds -- use zero if you don't want 
	// a timeout
	public static final int DEFAULT_TIMEOUT = 20 * 1000;
	private int socketTimeout = DEFAULT_TIMEOUT;
	
	private ICommandService serviceCommand;

	
	public ProxyThread() {
		
	}
	
	public ProxyThread(Socket s)
	{
		pSocket = s;
	}

	public ProxyThread(Socket s, String proxy, int port)
	{
		pSocket = s;
		fwdServer = proxy;
		fwdPort = port;
	}
	
	
	
	public void setCommandService(ICommandService service) {
		serviceCommand = service;
	}
	
	public void setTimeout (int timeout)
	{
		// assume that the user will pass the timeout value
		// in seconds (because that's just more intuitive)
		socketTimeout = timeout * 1000;
	}


	public void setpSocket(Socket pSocket) {
		this.pSocket = pSocket;
	}

	public void setFwdServer(String fwdServer) {
		this.fwdServer = fwdServer;
	}

	public void setFwdPort(int fwdPort) {
		this.fwdPort = fwdPort;
	}

	public void setDebug (int level, PrintStream out)
	{
		debugLevel = level;
		debugOut = out;
	}


	public void run()
	{
		try
		{
			long startTime = System.currentTimeMillis();
			
			// client streams (make sure you're using streams that use
			// byte arrays, so things like GIF and JPEG files and file
			// downloads will transfer properly)
			BufferedInputStream clientIn = new BufferedInputStream(pSocket.getInputStream());
			BufferedOutputStream clientOut = new BufferedOutputStream(pSocket.getOutputStream());
			
			// the socket to the remote server
			Socket server = null;
			
			// other variables
			byte[] request = null;
			byte[] response = null;
			int requestLength = 0;
			int responseLength = 0;
			int pos = -1;
			StringBuffer host = new StringBuffer("");
			String hostName = "";
			int hostPort = 80;
			StringBuffer queryString = new StringBuffer("");
			
			// get the header info (the web browser won't disconnect after
			// it's sent a request, so make sure the waitForDisconnect
			// parameter is false)
			request = getHTTPData(clientIn, host, queryString, false);
			requestLength = Array.getLength(request);
			
			// separate the host name from the host port, if necessary
			// (like if it's "servername:8000")
			hostName = host.toString();
			pos = hostName.indexOf(":");
			if (pos > 0)
			{
				try { hostPort = Integer.parseInt(hostName.substring(pos + 1)); 
					}  catch (Exception e)  { }
				hostName = hostName.substring(0, pos);
			}
			
			// either forward this request to another proxy server or
			// send it straight to the Host
			try
			{
				if ((fwdServer.length() > 0) && (fwdPort > 0))
				{
					server = new Socket(fwdServer, fwdPort);
				}  else  {
					server = new Socket(hostName, hostPort);
				}
			}  catch (Exception e)  {
				// tell the client there was an error
				String errMsg = "HTTP/1.0 500\nContent Type: text/plain\n\n" + 
								"Error connecting to the server:\n" + e + "\n";
				clientOut.write(errMsg.getBytes(), 0, errMsg.length());
			}
			
			if (server != null)
			{
				server.setSoTimeout(socketTimeout);
				BufferedInputStream serverIn = new BufferedInputStream(server.getInputStream());
				BufferedOutputStream serverOut = new BufferedOutputStream(server.getOutputStream());
				
				// send the request out
				serverOut.write(request, 0, requestLength);
				serverOut.flush();
				
				// and get the response; if we're not at a debug level that
				// requires us to return the data in the response, just stream
				// it back to the client to save ourselves from having to
				// create and destroy an unnecessary byte array. Also, we
				// should set the waitForDisconnect parameter to 'true',
				// because some servers (like Google) don't always set the
				// Content-Length header field, so we have to listen until
				// they decide to disconnect (or the connection times out).
				if (debugLevel > 1)
				{
					response = getHTTPData(serverIn, true);
					responseLength = Array.getLength(response);
				}  else  {
					responseLength = streamHTTPData(serverIn, clientOut, true);
				}
				
				serverIn.close();
				serverOut.close();
			}
			
			// send the response back to the client, if we haven't already
			if (debugLevel > 1)
				clientOut.write(response, 0, responseLength);
			
			// if the user wants debug info, send them debug info; however,
			// keep in mind that because we're using threads, the output won't
			// necessarily be synchronous
			if (debugLevel > 0)
			{
				long endTime = System.currentTimeMillis();
				String hostAdress = pSocket.getInetAddress().getHostAddress();
				
				if( hostName.contains("virtualregatta.com") ) {
					debugOut.println("Request from " + hostAdress + 
									" on Port " + pSocket.getLocalPort() + 
									" to host " + hostName + ":" + hostPort + 
									"\n  (" + requestLength + " bytes sent, " + 
									responseLength + " bytes returned, " + 
									Long.toString(endTime - startTime) + " ms elapsed)");
				
					int index = hostName.indexOf("virtualregatta.com");
					String raceName = hostName.substring(0, index-1);
					System.out.println("-------------------------");
					System.out.println("< RACE : "+raceName);
					System.out.println("< CMD : "+queryString);
					System.out.println("-------------------------");
					
					Command command = serviceCommand.parseUrl(queryString.toString());
					if(command.getCommandType()!=null)
						serviceCommand.addCommand(command);
					
				}
				
				debugOut.flush();
			}
			if (debugLevel > 1)
			{
				debugOut.println("REQUEST:\n" + (new String(request)));
				debugOut.println("RESPONSE:\n" + (new String(response)));
				debugOut.flush();
			}
			
			// close all the client streams so we can listen again
			clientOut.close();
			clientIn.close();
			pSocket.close();
		}  catch (Exception e)  {
			if (debugLevel > 0)
				debugOut.println("Error in ProxyThread: " + e);
			e.printStackTrace();
		}

	}
	
	
	private byte[] getHTTPData (InputStream in, boolean waitForDisconnect)
	{
		// get the HTTP data from an InputStream, and return it as
		// a byte array
		// the waitForDisconnect parameter tells us what to do in case
		// the HTTP header doesn't specify the Content-Length of the
		// transmission
		StringBuffer foo = new StringBuffer("");
		StringBuffer bar = new StringBuffer("");
		return getHTTPData(in, foo, bar, waitForDisconnect);
	}
	
	
	private byte[] getHTTPData (InputStream in, StringBuffer host, StringBuffer queryString, boolean waitForDisconnect)
	{
		// get the HTTP data from an InputStream, and return it as
		// a byte array, and also return the Host entry in the header,
		// if it's specified -- note that we have to use a StringBuffer
		// for the 'host' variable, because a String won't return any
		// information when it's used as a parameter like that
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		streamHTTPData(in, bs, host, queryString, waitForDisconnect);
		return bs.toByteArray();
	}
	

	private int streamHTTPData (InputStream in, OutputStream out, boolean waitForDisconnect)
	{
		StringBuffer foo = new StringBuffer("");
		StringBuffer bar = new StringBuffer("");
		return streamHTTPData(in, out, foo, bar, waitForDisconnect);
	}
	
	private int streamHTTPData (InputStream in, OutputStream out, 
									StringBuffer host, StringBuffer queryString, boolean waitForDisconnect)
	{
		// get the HTTP data from an InputStream, and send it to
		// the designated OutputStream
		StringBuffer header = new StringBuffer("");
		String data = "";
		int responseCode = 200;
		int contentLength = 0;
		int pos = -1;
		int byteCount = 0;

		try
		{
			// get the first line of the header, so we know the response code
			data = readLine(in);
			if (data != null)
			{
				
				header.append(data + "\r\n");
				pos = data.indexOf(" ");
				if ((data.toLowerCase().startsWith("http")) && 
					(pos >= 0) && (data.indexOf(" ", pos+1) >= 0))
				{
					String rcString = data.substring(pos+1, data.indexOf(" ", pos+1));
					try
					{
						responseCode = Integer.parseInt(rcString);
					}  catch (Exception e)  {
						if (debugLevel > 0)
							debugOut.println("Error parsing response code " + rcString);
					}
				}
				
				if (data.contains("virtual")) {
					int start = data.indexOf(' ');
					int end = data.indexOf(' ', start+1);
					queryString.append(data.substring(start+1,end));
				}
			}
			
			// get the rest of the header info
			while ((data = readLine(in)) != null)
			{
				// the header ends at the first blank line
				if (data.length() == 0)
					break;
				header.append(data + "\r\n");
				
				// check for the Host header
				pos = data.toLowerCase().indexOf("host:");
				if (pos >= 0)
				{
					host.setLength(0);
					host.append(data.substring(pos + 5).trim());
				}
				
				// check for the Content-Length header
				pos = data.toLowerCase().indexOf("content-length:");
				if (pos >= 0)
					contentLength = Integer.parseInt(data.substring(pos + 15).trim());
			}
			
			// add a blank line to terminate the header info
			header.append("\r\n");
			
			// convert the header to a byte array, and write it to our stream
			out.write(header.toString().getBytes(), 0, header.length());
			
			// if the header indicated that this was not a 200 response,
			// just return what we've got if there is no Content-Length,
			// because we may not be getting anything else
			if ((responseCode != 200) && (contentLength == 0))
			{
				out.flush();
				return header.length();
			}

			// get the body, if any; we try to use the Content-Length header to
			// determine how much data we're supposed to be getting, because 
			// sometimes the client/server won't disconnect after sending us
			// information...
			if (contentLength > 0)
				waitForDisconnect = false;
			
			if ((contentLength > 0) || (waitForDisconnect))
			{
				try {
					byte[] buf = new byte[4096];
					int bytesIn = 0;
					while ( ((byteCount < contentLength) || (waitForDisconnect)) 
							&& ((bytesIn = in.read(buf)) >= 0) )
					{
						out.write(buf, 0, bytesIn);
						byteCount += bytesIn;
					}
				}  catch (Exception e)  {
					String errMsg = "Error getting HTTP body: " + e;
					if (debugLevel > 0)
						debugOut.println(errMsg);
					//bs.write(errMsg.getBytes(), 0, errMsg.length());
				}
			}
		}  catch (Exception e)  {
			if (debugLevel > 0)
				debugOut.println("Error getting HTTP data: " + e);
		}
		
		//flush the OutputStream and return
		try  {  out.flush();  }  catch (Exception e)  {}
		return (header.length() + byteCount);
	}
	
	
	private String readLine (InputStream in)
	{
		// reads a line of text from an InputStream
		StringBuffer data = new StringBuffer("");
		int c;
		
		try
		{
			// if we have nothing to read, just return null
			in.mark(1);
			if (in.read() == -1)
				return null;
			else
				in.reset();
			
			while ((c = in.read()) >= 0)
			{
				// check for an end-of-line character
				if ((c == 0) || (c == 10) || (c == 13))
					break;
				else
					data.append((char)c);
			}
		
			// deal with the case where the end-of-line terminator is \r\n
			if (c == 13)
			{
				in.mark(1);
				if (in.read() != 10)
					in.reset();
			}
		}  catch (Exception e)  {
			if (debugLevel > 0)
				debugOut.println("Error getting header: " + e);
		}
		
		// and return what we have
		return data.toString();
	}
	
}
