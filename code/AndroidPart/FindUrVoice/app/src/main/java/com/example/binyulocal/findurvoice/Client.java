package com.example.binyulocal.findurvoice;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Client extends AsyncTask<Void, Void, Void> {

	private String dstAddress;
	private int dstPort;
	private String response = "";
	private TextView textResponse;
	private FileInputStream fis;
	private MainActivity m_Activity;
	private int Flag = 0;
	private FileOutputStream fOut;
	private OutputStreamWriter osw;
	private InputStream is = null;

	Client(String addr, int port,TextView textResponse, FileInputStream fIn, MainActivity m_Activity) {
		dstAddress = addr;
		dstPort = port;
		this.textResponse=textResponse;
		fis = fIn;
		this.m_Activity = m_Activity;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		Socket socket = null;
		Flag = 0;
		//buffer for input
		byte[] buffer = new byte[4096 * 5];
		//buffer for output
		byte[] buffer2 = new byte[4096 * 5];
		try {
			//setup socket
			socket = new Socket(dstAddress, dstPort);

			try {
				PrintStream ps = new PrintStream(socket.getOutputStream());
				//111 for handshaking
				ps.println("111/#" + "speaker.wav" + "/#" + fis.available());
				ps.flush();

			} catch (IOException e) {
				System.out.println("Server Disconnected");
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			try {
				//get OutputStream
				OutputStream os = socket.getOutputStream();
				//file size
				int size = 0;
				while ((size = fis.read(buffer)) != -1) {
					System.out.println("Sending，File Size" + size);
					os.write(buffer, 0, size);
					os.flush();
				}
				//socket.close();
				fis.close();
			} catch (FileNotFoundException e) {
				System.out.println("Read File Error");
			} catch (IOException e) {
				System.out.println("IOException Output");
			} finally {
				try {
					if (fis != null)
						fis.close();
				} catch (IOException e) {
					System.out.println("IOException Close File");
				}//catch (IOException e)
			}//finally

     		launchRingDialog();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			//read handshaking code
			String comm = null;
			try {
				InputStreamReader isr = new InputStreamReader(socket.getInputStream());
				BufferedReader br = new BufferedReader(isr);
				comm = br.readLine();
				System.out.println(comm);
			} catch (IOException e) {
				System.out.println("Server Disconnected");
			}

			int index = comm.indexOf("/#");

			String handshaking = comm.substring(0, index);
			if (!handshaking.equals("222")) {
				System.out.println("Wrong Code");
			}
			else{
				//file name and size
				comm = comm.substring(index + 2);
				index = comm.indexOf("/#");
				String filename = comm.substring(0, index).trim();
				String filesize = comm.substring(index + 2).trim();

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				//Recieve result txt file
				try {
					try {
						fOut = m_Activity.openFileOutput("result.txt", Context.MODE_PRIVATE);
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
					osw = new OutputStreamWriter(fOut);
					long file_size = Long.parseLong(filesize);
					System.out.println("file_size"+file_size);
					is = socket.getInputStream();
					InputStreamReader isr = new InputStreamReader(is);
					int size = 0;
					long count = 0;

					while (count < file_size) {
						size = is.read(buffer2);
						//System.out.println(new String(buffer2));
						osw.write((new String(buffer2)).toCharArray(), 0, size);
						osw.flush();
						count += size;
						System.out.println("Receiving，File Size" + size);
					}
					Flag =1;
					socket.close();
					socket = null;

				} catch (FileNotFoundException e) {
					System.out.println("Write File Error");
				} catch (IOException e) {
					System.out.println("IOException Disconnect");
				} finally {

					try {
						if (osw != null)
							osw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}


		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = "UnknownHostException: " + e.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = "IOException: " + e.toString();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public void launchRingDialog() {
		m_Activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				final ProgressDialog ringProgressDialog = ProgressDialog.show(m_Activity, "Please wait ...", "Processing ...", true);
				ringProgressDialog.setCancelable(true);

				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							//check if the processing is over
							while(Flag == 0)
							Thread.sleep(50);
							//switch to result layout
							Intent i = new Intent(m_Activity.getApplicationContext(),
									result.class);
							m_Activity.startActivity(i);
							m_Activity.finish();
						} catch (Exception e) {
						}
						ringProgressDialog.dismiss();
					}
				}).start();




			}
		});
	}

	@Override
	protected void onPostExecute(Void result) {
		textResponse.setText(response);
		super.onPostExecute(result);
	}

}
