import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server {
	public static void main(String[] args) {
		ServerSocket socketserver;
		try{
			socketserver = new ServerSocket(20);
			System.out.println("Le serveur est à l'écoute du port "+20);
			
			new Thread(new ThreadS(socketserver)).start();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static class ThreadS implements Runnable{
		Socket socket;
		ServerSocket socketserver;
		public ThreadS(ServerSocket socketserver){
			this.socketserver = socketserver;
		}
		public void run(){	
			try{
				while(true){
					socket = socketserver.accept();
					System.out.println("Un client veut se connecter");
					new Thread(new TraitementThread(socket)).start();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		public class TraitementThread implements Runnable{	
			String splitMessageCaratere[] = new String[20];
			String []splitMessage = new String[2];
			String user;
			String mot_passe;
			boolean authentifier = false;
			BufferedReader in = null;
			PrintWriter out = null;
			String msg;
			Socket serveur;
			float resultat;
			public TraitementThread(Socket serveur){
				this.serveur = serveur;
			}
			public void run(){	
				try{
					out = new PrintWriter(serveur.getOutputStream());
					in = new BufferedReader(new InputStreamReader(serveur.getInputStream()));
					
					
					while(!authentifier){
						out.println("Entrez votre login :");
						out.flush();
						user = in.readLine();
						out.println("Entrez votre mot de passe :");
						out.flush();
						mot_passe = in.readLine();
						if(verification(user, mot_passe)){
							out.println("auth");
							System.out.println(user +" vient de se connecter ");
							out.flush();
							authentifier = true;	
						}else {
							out.println("erreur"); out.flush();
						}
					}
					try{	
						while(true){
							msg = in.readLine();
							System.out.println(user+" : "+msg);
							splitMessage = msg.split("[+*-/]");
							splitMessageCaratere = msg.split("");
							for(int i=0; i<splitMessageCaratere.length ;i++){
									if(splitMessageCaratere[i].equals("+")){
										resultat = Integer.parseInt(splitMessage[0]) + Integer.parseInt(splitMessage[1]);
										msg = "Résultat est : "+resultat;
										out.println(msg);
									    out.flush();
									}else if(splitMessageCaratere[i].equals("*")){
										resultat = Integer.parseInt(splitMessage[0]) * Integer.parseInt(splitMessage[1]);
										msg = "Résultat est : "+resultat;
										out.println(msg);
									    out.flush();
									}else if(splitMessageCaratere[i].equals("-")){
										resultat = Integer.parseInt(splitMessage[0]) - Integer.parseInt(splitMessage[1]);
										msg = "Résultat est : "+resultat;
										out.println(msg);
									    out.flush();
									}else if(splitMessageCaratere[i].equals("/")){
										if(Integer.parseInt(splitMessage[1]) != 0){
											resultat = (float)Integer.parseInt(splitMessage[0]) / Integer.parseInt(splitMessage[1]);
											msg = "Résultat est : "+resultat;
											out.println(msg);
										    out.flush();
										}else{
											msg = "erreur division par zéro";
											out.println(msg);
										    out.flush();
										}
									}
								}
						}
						
					}catch(IOException e){
						e.printStackTrace();
					}
					
				}catch(IOException e){
					e.printStackTrace();
				}
			}
			private boolean verification(String login, String pass){
				boolean valide = false;
				try {
					Scanner sc = new Scanner(new File("C:\\Users\\chateau noir\\Desktop\\java\\PROJET\\src\\database.txt"));
					while(sc.hasNext()){
						if(sc.nextLine().equals(login+"->"+pass)){
							valide=true;
							break;
						}
					}
				}catch (FileNotFoundException e) {	
					e.printStackTrace();
				}
				return valide;
			}
		}
	}
}
