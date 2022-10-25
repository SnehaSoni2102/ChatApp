package javaProject;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Server extends JFrame{
	
	ServerSocket server;
	Socket socket;
	
	BufferedReader br;
	PrintWriter out;
	
	public JLabel heading =new JLabel("Server Area");
	private JTextArea messageArea=new JTextArea();
	private JTextField messageInput=new JTextField();
	private Font font=new Font("Roboto",Font.PLAIN,20);
	
    public Server() {
    	
    	try {
    		server=new ServerSocket(7777);
    		System.out.println("Server is ready to accept connection");
    		System.out.println("waiting...");
    		socket=server.accept();
    		
    		br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
    		out=new PrintWriter(socket.getOutputStream());
    		createGUI();
    		handleEvents();
    		
    		startReading();
//    		startWriting();
//    		
    	}catch(Exception e){
    		  e.printStackTrace();
    	}
    	
    }
   
	

    private void handleEvents() {
		messageInput.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
//				System.out.println("key Released");
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println("key Released"+e.getKeyCode());
				if(e.getKeyCode()==10) {
				//	System.out.println("you have press the button");
					String contentToSend =messageInput.getText();
					messageArea.append("Me:"+contentToSend+"\n");
					out.println(contentToSend);
					out.flush();
					messageInput.setText("");
					messageInput.requestFocus();
				}
			}
			
		})
		;
		
	}
	private void createGUI() {
		// gui code
		this.setTitle("Client Messager[END]");
		this.setSize(600,600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//coding for componenet
		
		heading.setFont(font);
		messageArea.setFont(font);
		messageInput.setFont(font);
		//heading.setIcon(new ImageIcon("clogo.png"));
		heading.setHorizontalTextPosition(SwingConstants.CENTER);
		heading.setVerticalTextPosition(SwingConstants.BOTTOM);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		messageArea.setEditable(false);
		messageInput.setHorizontalAlignment(SwingConstants.CENTER);
		
		//Set frame layout
		this.setLayout(new BorderLayout());
		
		//adding the component to frmae
		this.add(heading,BorderLayout.NORTH);
		JScrollPane jScrollPane=new JScrollPane(messageArea);
		this.add(jScrollPane,BorderLayout.CENTER);
		this.add(messageInput,BorderLayout.SOUTH);
		
		this.setVisible(true);
		
	}



	public void startReading() {
		// thread read krke deta rhega
		
		Runnable r1=()->{
			System.out.println("reader started..");
			try {
			while(true) {
				String msg;
				
					msg = br.readLine();
					if(msg.equals("exit")) {
						System.out.println("Client terminated the chat");
						JOptionPane.showInputDialog( this,"Server Terminated the chat" );
						messageInput.setEnabled(false);
						socket.close();
						break;
					}
//					System.out.println("Client : "+msg);
					messageArea.append("Client:"+msg+"\n");
			}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.out.println("Connection is closed");
				}
				
			
		};
		new Thread(r1).start();
		
	}
	
	public void startWriting() {
		// thread - data user lega and the send karega client tak
		
		Runnable r2=()->{
			System.out.println("writer started...");
			try {
			while(!socket.isClosed()) {
				
					
					BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
					String content =br1.readLine();
					out.println(content);
					out.flush();
					if(content.equals("exit")) {
						socket.close();
						break;
					}
			}	
				}catch(Exception e) {
					//e.printStackTrace();
					System.out.println("Connection is closed");
				}
			
		};
		new Thread(r2).start();
		
	}

	public static void main(String[] args) {
		System.out.println("this is server ..going to start server");
		new Server();

	}

}
