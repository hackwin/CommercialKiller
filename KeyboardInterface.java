import java.io.BufferedReader;
import java.io.InputStreamReader;

class KeyboardInterface extends Thread{
	
	CommercialKiller parent;
	BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
	String keyboardLine = "";
	
	KeyboardInterface(CommercialKiller parent){
		this.parent = parent;
	}
	
	public void run(){
		try {
			
			System.out.println("Listening for keyboard commands...");
			
			while(parent.shouldStop == false){
				keyboardLine = keyboard.readLine();
				System.out.println(">"+keyboardLine);
				
				if (keyboardLine.equals("s"))
					parent.saveCommercial();				
				else if (keyboardLine.equals("q")){
					parent.shouldStop = true;
				}
				else{
					System.out.println("Invalid command");
				}
				sleep(50);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}