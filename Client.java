import java.net.*;
import java.util.Scanner;
import java.io.*;
public class Client {
	public static void main(String[] args) {
		Socket client=null;
		try{
			System.out.println("Demande de connexion");
			client = new Socket("127.0.0.1",20);
			System.out.println("Connexion établie avec le serveur, authentification : ");
			Thread t = new Thread(new ThreadC(client));
			t.start();
		}catch(UnknownHostException  e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static class ThreadC implements Runnable{
		String user;
		BufferedReader in;
		boolean connexion = false;
		PrintWriter out;
		String msg;
		Socket socketclient;
		Scanner scan;
		String mot_passe;
		String message_distant;
		public ThreadC(Socket mon_socket){
			socketclient = mon_socket;
		}
		public void run(){
			try{
				out = new PrintWriter(socketclient.getOutputStream());
				in = new BufferedReader(new InputStreamReader(socketclient.getInputStream()));	
				scan = new Scanner(System.in);
				while(!connexion){
					message_distant = in.readLine();
					System.out.println(message_distant);
					user = scan.nextLine();
					out.println(user);
					out.flush();
					message_distant = in.readLine();
					System.out.println(message_distant);
					mot_passe = scan.nextLine();
					out.println(mot_passe);
					out.flush();
					
					message_distant=in.readLine();
					
					if(message_distant.equals("authentic")){
						System.out.println("Je suis connecté "); 
						connexion = true;
					}else{
						System.out.println("Connexion impossible, user ID ou mot de passe est incorrect"+message_distant); 
					}
				}
				out = new PrintWriter(socketclient.getOutputStream());
				in = new BufferedReader(new InputStreamReader(socketclient.getInputStream()));
				while(true){
					System.out.println("Votre message : ");
					msg = scan.nextLine();
					out.println(msg);
				    out.flush();
				    msg = in.readLine();
					System.out.println("Le serveur vous dit : " + msg);
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}
