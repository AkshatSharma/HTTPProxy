package assignment2_proxy;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.imageio.ImageIO;

//Creating the class by extending the Thread class
public class ProxyImplementation extends Thread {
	
	public Socket clientSocket;
	
	public ProxyImplementation(Socket socket)
	{
		clientSocket = socket;
	}
	
	@Override
	//Run method for running the thread
	public void run()
	{
		try
		{
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			String sentence;
			sentence = inFromUser.readLine();
			
			//The connection will be closed if the input from user is null or if it uses any other method than "GET"
			if(sentence == null || !sentence.contains("GET"))
			{
				clientSocket.close();
			}
			else
			{
				//Gets and Stores the url by splitting the input received
				URL obj = new URL(sentence.split(" ")[1]);
				
				//Gets the host using the URL object
				String getHost = obj.getHost();
				
				//Creating the server socket
				Socket serverSocket = new Socket();
				
				//Initializing the server socket on the default port 80
				serverSocket.connect(new InetSocketAddress(getHost, 80));
				
				//Calling the getReponseMessage method to get and store the response
				getResponseMessage(obj, serverSocket);
				
				//Calling the logger method to log the requests in the proxy.log file
				logger(serverSocket, obj);
				
				//Close the server socket
				serverSocket.close();
				
				//close the client socket
				clientSocket.close();
			}
		}
		
		//Exception handling
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	//Method for getting the response messages
	private synchronized void getResponseMessage(URL obj, Socket s) throws Exception
	{
		//Declaring and initializing the required variables and Classes
	    String urlHost = obj.getHost();
	    String urlFile = obj.getFile();
	    PrintWriter outToServer = null;
	    PrintWriter c_out = null;
	    BufferedReader s_in = null;
	    String fileName = obj.toString().replace("/", "-").replace(":", "-").replace("+", "-").replace("?", "-").replace("=", "-").replace("&", "-").replace("%", "-").replace("~", "-");
	    String fileSubstring = fileName.substring(7);
	        try
	        {
	           	//writer for socket
	            outToServer = new PrintWriter(s.getOutputStream(), true);
	            
	            //writer for client
	            c_out = new PrintWriter(clientSocket.getOutputStream(),true);
	            
	            //reader for socket
	            s_in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	        }
	         
	        //Exception handling if the host is invalid
	        catch (UnknownHostException e) 
	        {
	        	//Prints the error message and exits
	            System.err.println("Invalid Host: " + urlHost);
	            System.exit(1);
	        }
	         
	    //Outputting the GET request to the server
	    String message = "GET "+ urlFile + " HTTP/1.0";
	    outToServer.println( message );
	    outToServer.println(String.format("Host: %s", urlHost));
	    outToServer.println("");
	         
	    //Getting the response from the server and loading into the StringBuffer
	    StringBuffer response = new StringBuffer();
	    String inputLine;
	    
	  //Checking for images in the file extracted from the URL
	   if(urlFile.contains(".jpg") || urlFile.contains(".png") || urlFile.contains(".gif") || urlFile.contains(".ttf") || urlFile.contains(".ico") 
			   || urlFile.contains(".GIF") || urlFile.contains(".swf") || urlFile.contains(".JPG") || urlFile.contains(".PNG") 
			   || urlFile.contains(".jpeg") || urlFile.contains(".JPEG"))
		{
		   //Calling the imageLoader method to load the images
		   imageLoader(s.getInputStream(), clientSocket.getOutputStream(), s);  
		}
	   else
	   {
		   if(fileSubstring.length() < 220)
   			{
   		   
			   File f = new File(fileSubstring + ".txt");
			   
			   //Checks if file doesn't exists. If it doesn't a new cache file is created
			   if(!f.exists())
			   {
				   f.createNewFile();
				   BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(f.getName(), true));
    			
				/*
				 * The response is first read into the StringBuffer and
				 * then cached using the BufferWriter into the file created. 
				 * After the cache file is created the user is informed that 
				 * the caching was successful
				 */
				   while ((inputLine = s_in.readLine()) != null) 
				   {
					   response.append(inputLine + "\n");
        			   c_out.println(inputLine);
	        		   bufferWriter.write(inputLine);
	        		   bufferWriter.newLine();
        			   
				   }
				   //Close bufferWriter
        		  bufferWriter.close();
        		  System.out.println(response.toString());
       		   System.out.println("Caching Successful");
			}
			   else
			   {
				   /*
				    * If the cache file is already present, the requested file is first called and stored
				    * into the BufferedReader, and then read out using the PrintWriter 
				    * to the browser line by line, and thus the browser does not have to
				    * communicate with the server to get the files again
				    */
    			   System.out.println("Loading URL: " + obj.toString() + " from cache." );
				   BufferedReader bufferReader = new BufferedReader(new FileReader(f.getName()));
    			   inputLine = null;
				   while ((inputLine = bufferReader.readLine()) != null) 
				   {
					   response.append(inputLine);
					   c_out.println(inputLine);
			   
				   }   
				   //Close bufferReader
    			   bufferReader.close();
			   }
   			}
		   
		   //Closing connections
		   outToServer.flush();
		   c_out.flush();
		   s_in.close();
	   }
	}
	
	//Method for loading images using the input stream and the output stream
	public synchronized void imageLoader(InputStream imageInFromServer, OutputStream imageOutToClient, Socket s) throws IOException
	{
		//Get the buffer size
		int getBufferSize = s.getReceiveBufferSize();
		
		//A byte array is created to load the images and initialized with the buffer size received
		byte [] imageBuffer = new byte[getBufferSize];
		
		//Variable for reading the bytes
		int readTheBytes;
		
		//The bytes are loaded in the imageBuffer and then written out to the client using the OutputStream
		while ((readTheBytes = imageInFromServer.read(imageBuffer)) != -1)
		{
			imageOutToClient.write(imageBuffer, 0, readTheBytes);
			imageOutToClient.flush();
		}
		//Closing the server input stream for taking images
		imageInFromServer.close();
	}
	
	//This method logs the requests in a proxy.log file
	public static synchronized void logger(Socket socket, URL url) throws Exception
	{
		//Gets the date
		Date d = new Date();
		
		//Formats the date and time into the MMM DD YYYY HH:MM:SS format
		SimpleDateFormat s = new SimpleDateFormat("MMM dd yyyy HH:mm:ss");
		
		/*
		 * Checks if the logger file exists. If it does, logs the requests in the file using the BufferedWriter
		 * If the logger file doesn't exist, the file is created and then the requests are logged
		 */
		try
		{
			File file = new File("proxy.log");
		
			if(!file.exists())
			{
				file.createNewFile();
			}
		
			BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(file.getName(), true));
			bufferWriter.append(s.format(d) + ":\t" + socket.getInetAddress().getHostAddress() + "\t\t"+ url.toString());
			bufferWriter.newLine();
			bufferWriter.close();
		}
		
		//Exception handling for invalid file created
		catch(Exception ex)
		{
			//Error Message printed to the console
			System.out.println("Unable to create file: " + ex.getMessage());
		}
	}
}
