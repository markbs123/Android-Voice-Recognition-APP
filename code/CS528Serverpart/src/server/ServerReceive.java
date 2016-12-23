package server;

import java.io.BufferedReader;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import testdeploy.operation;

public class ServerReceive {
	

	
public static void main(String[] args) {
	
	    
		
		/**Build connection between server and App*/
		ServerSocket ss = null;
		Socket s = null;
		
		File file = null;
		FileOutputStream fos = null;
		
		InputStream is = null;
		
		byte[] buffer = new byte[4096 * 5];
		
		String comm = null;
		
     try {
     	   ss = new ServerSocket(4004);
    		System.out.println("Server: Waiting for Client!");
		

    while(true){
		/**Read the recognition message from the App to make sure connection has been built */
 

		s = ss.accept();
		
		
		InputStreamReader isr = new InputStreamReader(s.getInputStream());
		BufferedReader br = new BufferedReader(isr);
		comm = br.readLine();
		System.out.println("Finding Upload File!");
		System.out.println("************************************************");
		
		
		/**Starting phrase the request from the App*/
		int index = comm.indexOf("/#");
		
		/**Judge whether the receiving protocol is right or not */
		String xieyi = comm.substring(0, index);
		if(!xieyi.equals("111")){
			System.out.println("");
			return;
		}
		
		/**Phrasing the name and size of the upload file */
		comm = comm.substring(index + 2);
		index = comm.indexOf("/#");
		String filename = comm.substring(0, index).trim();
		String filesize = comm.substring(index + 2).trim();
		
		
		/**创建空文件，用来进行接收文件*/
		file = new File(filename);
		if(!file.exists()){
			System.out.println("Start receiving!");
			file.createNewFile();
		}
		else{
			System.out.println("The same file alread exists on this path, begin overwriting!");
		}
		
		/**
		 * The core part of server receiving file*/

				/**Pack the target file into stream*/
				fos = new FileOutputStream(file);
				long file_size = Long.parseLong(filesize);
				is = s.getInputStream();
				/**The file length of each receiving process*/
				int size = 0;
				/**count the length that the server has already received*/
				long count = 0;
				
				while(count < file_size){
					size = is.read(buffer);
					
					/**Write the receiving file to local file*/
					fos.write(buffer, 0, size);
					fos.flush();

					count += size;
					
					System.out.println("Server has already received the file，the size is " + size+" KB");

					
				}
			
				fos.close();
				
			   System.out.println("Server Receiving Success! Waiting for generate result file!");    
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				
				
				
				/**User Matlab JAR to calculate the feature value of receiving file**/
				Object result[] = null;   
			    operation myAdd = null; 
			    
				 try{
		             // Choose the famous singers'songs as the match target
		             String content[] ={ "Ne-YO --- So sick",
		            		             "Burno Mars --- Grenade",
		            		             "Taylor Swift --- Love Story",
		            		             "Barbra Streisand --- The Windmills Of Your Mind",
		            		             "Justin Bieber --- What Do You Mean",
		            		             "Adele --- Rolling In The Deep",
		            		             "Lana Del Rey --- Old Money",
		            		             "Adele --- Someone Like You",
		            		             "Eddie Vedder --- Black",
		            		             "Linkin Park --- Numb"};
		           
		             
		 	        myAdd = new operation();
		 	        
				       
			        String a = "C:/Mark/Interview/CS528Serverpart/speaker.wav";// Receiving file from APP
			        String b= "C:/Mark/Interview/CS528Serverpart/data.mat"; // Data includes codebook
			        
			        // According to match degree, generating a sequence which similarity from larg to small
			        
			        result = myAdd.operation(10,a,b);
		             
		             String path="C:/Mark/Interview/CS528Serverpart/result.txt";
		             File refile = new File(path);

		                // if file doesnt exists, then create it
		                if (!refile.exists()) {
		                    refile.createNewFile();
		                }
		                

		                FileWriter fw = new FileWriter(refile.getAbsoluteFile());
		                BufferedWriter bw = new BufferedWriter(fw);
		                
		                // Print the first 5 places singers
		                bw.write(content[Integer.parseInt(result[0].toString())]);
		                bw.write(System.getProperty("line.separator"));
		                bw.write(content[Integer.parseInt(result[1].toString())]);
		                bw.write(System.getProperty("line.separator"));
		                bw.write(content[Integer.parseInt(result[2].toString())]);
		                bw.write(System.getProperty("line.separator"));
		                bw.write(content[Integer.parseInt(result[3].toString())]);
		                bw.write(System.getProperty("line.separator"));
		                bw.write(content[Integer.parseInt(result[4].toString())]);

		                
		                // close connection
		                bw.close();
		                

		                
		                
		              System.out.println("Generate the phrasing result! Start sending it to App!");
		              
		              System.out.println("************************************************");
		                
		                
		                /**Definition the sending file stream**/
		            	File sendfile = new File(path);
		        	
		        		FileInputStream fis = null;

		        		byte[] buffer1 = new byte[4096 * 5];
		        		

		        		OutputStream os = null;
		        		
		        		
		        		if(!sendfile.exists()){
		        			System.out.println("Server:Result file does not exist");
		        			return;
		        		}
		        		
		        		fis = new FileInputStream(sendfile);
		        		
		        		/**Judge whether the sending protocol is right or not**/
		        		PrintStream ps1 = new PrintStream(s.getOutputStream());
		    			ps1.println("222/#" + sendfile.getName() + "/#" + fis.available());
		    			ps1.flush();
		    			
		    			try {
		    				Thread.sleep(2000);
		    			} catch (InterruptedException e1) {
		    				e1.printStackTrace();
		    			}
		        			    			
		    			
		    			os = s.getOutputStream();

		    			int sendsize = 0;
		    			

		    			while((sendsize = fis.read(buffer1)) != -1){
		    				System.out.println("Server send the result file to App, the size is " + sendsize+" KB");

		    				os.write(buffer1, 0, sendsize);
		    		
		    				os.flush();
		                
		    			}
		    			
		    			 System.out.println("Server has already finished the sending process");    
		    			
		    			fis.close();
		                
		          }catch(Exception e){
		              System.out.println(e);
		          }
		
			  } 
			
        

     } 
     catch (IOException e) {
			System.out.println("Server: Disconnection!");
			
     }
  
    
   } 

}


