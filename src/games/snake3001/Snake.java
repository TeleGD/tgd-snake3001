package games.snake3001;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import app.AppInput;
import app.AppPlayer;

public class Snake {
	private World w;
	private int horizontal;
	private int vertical;
	private int compteur = 0;
	ArrayList<Point> body;
	Color couleur;
	private boolean moveLeft,moveRight=false;
	String nom;
	private int speed;
	private int dir;
	int score;
	boolean mort ;
	boolean inverse;
	int invincible;
	private int controllerID;

	public Snake(World world, int x_init, AppPlayer appPlayer) {
		this.couleur = AppPlayer.FILL_COLORS[appPlayer.getColorID()];
		this.controllerID = appPlayer.getControllerID();
		this.w=world;
		this.dir = 0;
		this.body = new ArrayList<Point>();
		this.nom = appPlayer.getName();
		this.score = 0;
		this.speed = 10;
		this.mort = false;
		this.inverse = false;
		this.invincible = 0;
		this.horizontal=w.longueur/w.nbcasesl;
		this.vertical=w.hauteur/w.nbcasesh;
		for (int i = 10;i>0;i--){
			body.add(new Point(x_init,(w.nbcasesh-i)));
		}
	}

	public void GScore(int x) {
		if (mort==false){
			score += x;
		}
	}

	public void meurt(){
		if (invincible==0){
			mort = true;
		}
	}

	public void move() {
		if (body.size()!=0){
			if ((moveRight && !inverse) || (moveLeft && inverse)) {
				dir = (dir+1) % 4;
			} else if ((moveRight && inverse) || (moveLeft && !inverse)) {
				dir = (dir+3) % 4;
			}

			Point ajout = null;
			if (dir == 0) { //haut
				if (body.get(0).y > 0) {
					ajout = new Point((body.get(0).x) , (body.get(0).y - 1));
				}
				else {
					ajout = new Point((body.get(0).x) , w.nbcasesh-1);
				}
			}
			if (dir == 1) { //droite
				if (body.get(0).x < w.nbcasesl-29 ) {
					ajout = new Point((body.get(0).x + 1) , (body.get(0).y));
				} else {
					ajout = new Point(0 , body.get(0).y) ;
				}
			}
			if (dir == 2) { //bas
				if (body.get(0).y < w.nbcasesh-1) {
					ajout = new Point((body.get(0).x) , (body.get(0).y + 1));
				} else {
					ajout = new Point((body.get(0).x) , 0);
				}
			}
			if (dir == 3) { //gauche
				if (body.get(0).x > 0) {
					ajout = new Point((body.get(0).x-1) , (body.get(0).y));
				} else {
					ajout = new Point(w.nbcasesl-29 , (body.get(0).y));
				}
			}

			body.remove((body.size()-1));
			if (invincible!=0 && body.size()==0 ){
				body.add(0,ajout);
			}
			if (mort == false){
				body.add(0,ajout);
			}
		}
	}

	public void grandir(){
		Point ajout = null;
		if (dir == 0) {
			if (body.get(0).y != 0) {
				ajout = new Point((body.get(0).x) , (body.get(0).y - 1));
				}
			else {
				ajout = new Point((body.get(0).x) , w.nbcasesh);}
		}
		if (dir == 1) {
			if (body.get(0).x != w.nbcasesl-28 ) {
				ajout = new Point((body.get(0).x + 1) , (body.get(0).y));
			}else {
				ajout = new Point(0 , body.get(0).y) ;
				}
		}
		if (dir == 2) {
			if (body.get(0).y != w.nbcasesh) {
				ajout = new Point((body.get(0).x) , (body.get(0).y + 1));}
			else {
				ajout = new Point((body.get(0).x) , 0);
				}
		}
		if (dir == 3) {
			if (body.get(0).x != 0) {
				ajout = new Point((body.get(0).x-1) , (body.get(0).y));
				}
			else {
				ajout = new Point(w.nbcasesl-28 , (body.get(0).y));}
		}
		body.add(0,ajout);
	}

	public void retrecir(){
		if((body.size() == 1))
			this.meurt();
		body.remove((body.size()-1));
	}

	public void plusRapide(){
		speed += 7;
	}

	public void plusLent(){
		if (speed > 4){
		speed -= 4;
		}
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		for  (int i = 0 ; i<body.size(); i++) {
			g.setColor(couleur);
			g.fillRect(body.get(i).x*horizontal,body.get(i).y*vertical,horizontal,vertical);
		}
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		AppInput input = (AppInput) container.getInput();
		if (input.isControlPressed(AppInput.BUTTON_LEFT,controllerID)) {
			moveLeft=true;
			moveRight=false;
		}
		if (input.isControlPressed(AppInput.BUTTON_RIGHT,controllerID)) {
			moveLeft=false;
			moveRight=true;
		}

		if (invincible >0) {
			if(invincible % 40 > 20) {
				couleur= new Color(couleur.r,couleur.g,couleur.b,0.5f);
			} else {
				couleur= new Color(couleur.r,couleur.g,couleur.b,1);
			}
			invincible = invincible - 1;
		}

		compteur += speed;
		while(compteur >= 15){
			move();
			moveLeft=false;
			moveRight=false;
			compteur -=15;
		}
	}

}
