package games.stairsToHeaven;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Player {
	private int floor;
	
	private int value;
	private int compteur;
	private int T1;
	private int T3;
	private int T5;
	private boolean onePress;
	private boolean threePress;
	private boolean fivePress;
	private boolean onePressEn;
	private boolean threePressEn;
	private boolean fivePressEn;
	
	public Player(int T1,int T3, int T5) {
		
		this.T1 = T1;
		this.T3 = T3;
		this.T5 = T5;
		floor = 0;
		onePress = false;
		threePress = false;
		fivePress = false;
		value=0;
		compteur = 0;
		
	}	
	
	private Player tabPlay[] = {new Player(Input.KEY_LEFT,Input.KEY_UP,Input.KEY_RIGHT),new Player(Input.KEY_A,Input.KEY_Z,Input.KEY_E)};
	
	public void update(GameContainer arg0, StateBasedGame arg1, int delta) throws SlickException {
		compteur += 1;
		if (compteur >= 300) {
			valueChange();
			afterChange();
			for (int i=0;i<=tabPlay.length;i++){
				isEqual(tabPlay[i],this);
			}
			climb();
		}
		
		
	}

	public void valueChange() {
		if (onePress == true){
			this.value = 1;
		}
		if (threePress == true){
			this.value = 3;
		}
		if (fivePress == true){
			this.value = 5;
		}
	}
	
	
	
	public void afterChange() {
		onePress = false;
		threePress = false;
		fivePress = false;
		}
	
	public int getValue() {
		return this.value;
	}
	
	public void isEqual(Player P1,Player P2) {
		if (P1.getValue() == P2.getValue()){
			P1.value=0;
			P2.value=0;
		}
	}
	
	public void climb(){
		this.floor = this.floor + this.value;
	}
	
	public void keyReleased(int key, char c) {

			if (key == T5) {
				fivePressEn = true;
			}
			if (key == T1) {
				onePressEn = true;
			}
			if (key == T3) {
				threePressEn = true;
			}
	} 
		
		public void keyPressed(int key, char c) {
		 
		if (fivePressEn == true && threePressEn == true && onePressEn == true){	
			if (key == T5){
				fivePress = true;
				fivePressEn = false;
			}
			else if (key == T1) {
				onePress = true;
				onePressEn = false;
			}
			else if (key == T3) {
				threePress = true;
				threePressEn = false;
			}
		}
			
			
			
	} 
	
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
