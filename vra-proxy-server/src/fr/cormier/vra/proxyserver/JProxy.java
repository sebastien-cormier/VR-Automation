package fr.cormier.vra.proxyserver;
/* <!-- in case someone opens this in a browser... --> <pre> */
/*
 * This is a simple multi-threaded Java proxy server
 * for HTTP requests (HTTPS doesn't seem to work, because
 * the CONNECT requests aren't always handled properly).
 * I implemented the class as a thread so you can call it 
 * from other programs and kill it, if necessary (by using 
 * the closeSocket() method).
 *
 * We'll call this the 1.1 version of this class. All I 
 * changed was to separate the HTTP header elements with
 * \r\n instead of just \n, to comply with the official
 * HTTP specification.
 *  
 * This can be used either as a direct proxy to other
 * servers, or as a forwarding proxy to another proxy
 * server. This makes it useful if you want to monitor
 * traffic going to and from a proxy server (for example,
 * you can run this on your local machine and set the
 * fwdServer and fwdPort to a real proxy server, and then
 * tell your browser to use "localhost" as the proxy, and
 * you can watch the browser traffic going in and out).
 *
 * One limitation of this implementation is that it doesn't 
 * close the ProxyThread socket if the client disconnects
 * or the server never responds, so you could end up with
 * a bunch of loose threads running amuck and waiting for
 * connections. As a band-aid, you can set the server socket
 * to timeout after a certain amount of time (use the
 * setTimeout() method in the ProxyThread class), although
 * this can cause false timeouts if a remote server is simply
 * slow to respond.
 *
 * Another thing is that it doesn't limit the number of
 * socket threads it will create, so if you use this on a
 * really busy machine that processed a bunch of requests,
 * you may have problems. You should use thread pools if
 * you're going to try something like this in a "real"
 * application.
 *
 * Note that if you're using the "main" method to run this
 * by itself and you don't need the debug output, it will
 * run a bit faster if you pipe the std output to 'nul'.
 *
 * You may use this code as you wish, just don't pretend 
 * that you wrote it yourself, and don't hold me liable for 
 * anything that it does or doesn't do. If you're feeling 
 * especially honest, please include a link to nsftools.com
 * along with the code. Thanks, and good luck.
 *
 * Julian Robichaux -- http://www.nsftools.com
 */
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.cormier.vra.service.ICommandService;

public class JProxy extends Thread
{
	public static final int DEFAULT_PORT = 8080;
	
	private ServerSocket server = null;
	private int thisPort = DEFAULT_PORT;
	private String fwdServer = "";
	private int fwdPort = 0;
	private int ptTimeout = ProxyThread.DEFAULT_TIMEOUT;
	private int debugLevel = 0;
	private PrintStream debugOut = System.out;

	private static AbstractApplicationContext context = new ClassPathXmlApplicationContext(
			"spring.xml");
	
	/* here's a main method, in case you want to run this by itself */
	public static void main (String args[])
	{
		
		JProxy proxyserver = context.getBean(JProxy.class);
		
		
		int port = 0;
		String fwdProxyServer = "";
		int fwdProxyPort = 0;
		
		if (args.length == 0)
		{
			System.err.println("USAGE: java jProxy <port number> [<fwd proxy> <fwd port>]");
			System.err.println("  <port number>   the port this service listens on");
			System.err.println("  <fwd proxy>     optional proxy server to forward requests to");
			System.err.println("  <fwd port>      the port that the optional proxy server is on");
			System.err.println("\nHINT: if you don't want to see all the debug information flying by,");
			System.err.println("you can pipe the output to a file or to 'nul' using \">\". For example:");
			System.err.println("  to send output to the file prox.txt: java jProxy 8080 > prox.txt");
			System.err.println("  to make the output go away: java jProxy 8080 > nul");
			return;
		}
		
		// get the command-line parameters
		port = Integer.parseInt(args[0]);
		if (args.length > 2)
		{
			fwdProxyServer = args[1];
			fwdProxyPort = Integer.parseInt(args[2]);
		}
		
		// create and start the jProxy thread, using a 20 second timeout
		// value to keep the threads from piling up too much
		System.err.println("  **  Starting jProxy on port " + port + ". Press CTRL-C to end.  **\n");
		proxyserver.setThisPort(port);
		proxyserver.setFwdServer(fwdProxyServer);
		proxyserver.setFwdPort(fwdProxyPort);
		proxyserver.setPtTimeout(20);
		
		proxyserver.setDebug(1, System.out);		// or set the debug level to 2 for tons of output
		proxyserver.start();
		
		// run forever; if you were calling this class from another
		// program and you wanted to stop the jProxy thread at some
		// point, you could write a loop that waits for a certain
		// condition and then calls jProxy.closeSocket() to kill
		// the running jProxy thread
		while (true)
		{
			try { Thread.sleep(3000); } catch (Exception e) {}
		}
		
		// if we ever had a condition that stopped the loop above,
		// we'd want to do this to kill the running thread
		//jp.closeSocket();
		//return;
	}
	
	public JProxy(){
		
	}
	
	/* the proxy server just listens for connections and creates
	 * a new thread for each connection attempt (the ProxyThread
	 * class really does all the work)
	 */
	public JProxy (int port)
	{
		thisPort = port;
	}
	
	public JProxy (int port, String proxyServer, int proxyPort)
	{
		thisPort = port;
		fwdServer = proxyServer;
		fwdPort = proxyPort;
	}
	
	public JProxy (int port, String proxyServer, int proxyPort, int timeout)
	{
		thisPort = port;
		fwdServer = proxyServer;
		fwdPort = proxyPort;
		ptTimeout = timeout;
	}
	
	public void setThisPort(int port) {
		this.thisPort = port;
	}
	
	public void setFwdServer(String fserver) {
		fwdServer = fserver;
	}
	
	public void setFwdPort(int fport) {
		fwdPort = fport;
	}
	
	public void setPtTimeout(int timeout) {
		ptTimeout = timeout;
	}
	
	/* allow the user to decide whether or not to send debug
	 * output to the console or some other PrintStream
	 */
	public void setDebug (int level, PrintStream out)
	{
		debugLevel = level;
		debugOut = out;
	}
	
	
	/* get the port that we're supposed to be listening on
	 */
	public int getPort ()
	{
		return thisPort;
	}
	 
	
	/* return whether or not the socket is currently open
	 */
	public boolean isRunning ()
	{
		if (server == null)
			return false;
		else
			return true;
	}
	 
	
	/* closeSocket will close the open ServerSocket; use this
	 * to halt a running jProxy thread
	 */
	public void closeSocket ()
	{
		try {
			// close the open server socket
			server.close();
			// send it a message to make it stop waiting immediately
			// (not really necessary)
			/*Socket s = new Socket("localhost", thisPort);
			OutputStream os = s.getOutputStream();
			os.write((byte)0);
			os.close();
			s.close();*/
		}  catch(Exception e)  { 
			if (debugLevel > 0)
				debugOut.println(e);
		}
		
		server = null;
	}
	
	
	public void run()
	{
		try {
			// create a server socket, and loop forever listening for
			// client connections
			server = new ServerSocket(thisPort);
			if (debugLevel > 0)
				debugOut.println("Started jProxy on port " + thisPort);
			
			while (true)
			{
				Socket client = server.accept();
				ProxyThread t = new ProxyThread(client, fwdServer, fwdPort);
				t.setDebug(debugLevel, debugOut);
				t.setTimeout(ptTimeout);
				t.setCommandService(context.getBean(ICommandService.class));
				
				t.start();
			}
		}  catch (Exception e)  {
			if (debugLevel > 0)
				debugOut.println("jProxy Thread error: " + e);
		}
		
		closeSocket();
	}
	
}

