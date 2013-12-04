Created By: Akshat Sharma
	    Rahmanul Hoque
IDE: Eclipse

FOLLOW THESE STEPS TO RUN THE PROGRAM FROM ECLIPSE

1. After downloading the program, import it into Eclipse workspace to execute the program
	-Steps to import into eclipse
		a)Click File
		b)Click Import
		c)Open the General Folder
		d)Click Existing Project
		e)Browse to the Folder where the program is downloaded
		f)Select the program and hit import

2. Next to run the program, we need to modify our network settings
	-Steps to modify network setting
		a)Open Browser
		b)Open Browser Settings
		c)Browse to the network category, and click on change proxy settings
		d)Click on LAN settings
		e)Enter the port on which is server will be running. This project runs on Port:6768

3. Run the server from the program
4. Open any webpage on the browser. The response sent by the browser can be seen on the Eclipse Console.
5. If the URL was opened first time, you will see the response and at a message at the end saying the file is cached.
	-The next time this same URL loads, it will load from the cache file.
6. Proxy.log file will contain all the requests that were requested when the program was running

_________________________________________________________________________________________________________________________________

FOLLOW THESE STEPS TO RUN THE PROGRAM FROM THE EXECUTABLE JAR FILE

1. After downloading the program, locate the executable JAR file and run it using the command prompt
	-Steps to run the executable JAR file
		a)Click Start
		b)Open the Command Window
		c)Browse to the location of the JAR file using the Command Window
		d)Write java -jar Assignment2Exec.jar to execute the file
		e)The program is now executing on the Default Port 6768

2. Next to run the program, we need to modify our network settings
	-Steps to modify network setting
		a)Open Browser
		b)Open Browser Settings/Internet Properties
		c)Browse to the network category, and click on change proxy settings
		d)Click on LAN settings
		e)Enter the Port:6768 on which is server will be running

3. The server already started running when the JAR file was executed in step 1d.
4. Open any webpage on the browser. The response sent by the browser can be seen on the Command prompt
5. If the URL was opened first time, you will see the response and at a message at the end saying the file is cached.
	-The next time this same URL loads, it will load from the cache file.
6. Proxy.log file will contain all the requests that were requested when the program was running
