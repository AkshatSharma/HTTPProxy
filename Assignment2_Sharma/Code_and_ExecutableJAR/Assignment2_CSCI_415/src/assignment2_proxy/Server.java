/*
 * Name: HTTP Proxy Implementation project
 * Course: CSCI 415 - Networking and Parallel Computation
 * Created By: Akshat Sharma
 */

package assignment2_proxy;

import java.io.*;
import java.net.*;
import assignment2_proxy.ProxyImplementation;


public class Server {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// This creates a server socket at the desired port. The port here is 6768
		ServerSocket welcomeSocket = new ServerSocket(6768);
		try
		{
			//While the server socket is running, create an instance of the ProxyImplementation class
			//accept the socket connection and start running the threads
			while(true)
			{
				new ProxyImplementation(welcomeSocket.accept()).start();
			}
		}
		finally
		{
			//When all the requests are processed, close the server socket
			welcomeSocket.close();
		}

	}

}
