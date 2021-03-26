package games.snake3001;

import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

import app.AppFont;
import app.AppGame;
import app.AppLoader;
import app.AppWorld;

public class World extends AppWorld {

	public int nbcasesh;
	public int nbcasesl;
	public int longueur;
	public int hauteur;

	private float widthBandeau;
	private ArrayList<Bonus> bonus;
	private ArrayList<Snake> snakes;
	private ArrayList<Snake> morts;

	public final static String DIRECTORY_SOUNDS="/sounds/snake3001/";
	public final static String DIRECTORY_MUSICS="/musics/snake3001/";
	public final static String DIRECTORY_IMAGES="/images/snake3001/";
	public static final TrueTypeFont Font = AppLoader.loadFont("/fonts/vt323.ttf", AppFont.BOLD, 20);

	private boolean jeuTermine;

	static Audio sonMouette;
	static Audio sonSncf;
	static Audio sonChute;
	static Audio sonCheval;
	static Audio sonEclair;
	static Audio sonMagic;
	static Audio sonMartien;
	static Audio sonPerdu;
	private static Audio soundMusicBackground;
	private float soundMusicBackgroundPos;

	static {
		sonMouette = AppLoader.loadAudio(DIRECTORY_SOUNDS+"seagulls-chatting.ogg");
		sonSncf = AppLoader.loadAudio(DIRECTORY_SOUNDS+"0564.ogg");
		sonChute = AppLoader.loadAudio(DIRECTORY_SOUNDS+"0477.ogg");
		sonCheval = AppLoader.loadAudio(DIRECTORY_SOUNDS+"horse-whinnies.ogg");
		sonEclair = AppLoader.loadAudio(DIRECTORY_SOUNDS+"ChargedLightningAttack8-Bit.ogg");
		sonMagic = AppLoader.loadAudio(DIRECTORY_SOUNDS+"FreezeMagic.ogg");
		sonMartien = AppLoader.loadAudio(DIRECTORY_SOUNDS+"martian-gun.ogg");
		sonPerdu = AppLoader.loadAudio(DIRECTORY_SOUNDS+"perdu.ogg");
		soundMusicBackground=AppLoader.loadAudio(DIRECTORY_MUSICS+"hymne_russe.ogg");
	}

	public World (int ID) {
		super (ID);
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g) {

		for(int i=0;i<bonus.size();i++){
			bonus.get(i).render(container, game, g);
		}

		for(int i=0;i<snakes.size();i++){
			snakes.get(i).render(container, game, g);
			g.setColor(Color.black);
		}

		g.setColor(new Color(150,150,150));
		g.fillRect(longueur-widthBandeau+2,0,widthBandeau,hauteur);
		g.setColor(new Color(170,170,170));
		g.fillRect(longueur-widthBandeau+4,0,widthBandeau,hauteur);
		g.setColor(new Color(200,200,200));
		g.fillRect(longueur-widthBandeau+6,0,widthBandeau,hauteur);

		g.setFont(Font);
		g.setColor(Color.black);
		g.drawString("SNAKE 3001 ! ",longueur-widthBandeau+20,20);

		g.setColor(new Color(150,150,150));
		g.fillRect(longueur-widthBandeau+6,60,widthBandeau,5);
		g.resetFont();

		if(jeuTermine){
			g.setColor(Color.black);
			g.fillRoundRect(longueur/2-75-widthBandeau/2,hauteur/2-50,
					150,100,20);
			g.setColor(Color.white);
			g.fillRoundRect(longueur/2-75+4-widthBandeau/2,hauteur/2-50+4,150-8,92,20);
			g.setColor(Color.black);
			g.setFont(Font);
			g.drawString("Fin du jeu", longueur/2-42-widthBandeau/2,hauteur/2-15);
			for(int i=morts.size()-1; i>=0 ;i--){
				g.setColor(morts.get(i).couleur);
				g.drawString(morts.get(i).nom+" : "+morts.get(i).score,longueur-widthBandeau+20,100+50*i+20);
			}
		} else {
			for(int i=0;i<snakes.size();i++){
				g.setColor(snakes.get(i).couleur);
				g.drawString(snakes.get(i).nom+" : "+snakes.get(i).score,longueur-widthBandeau+20,100+50*i+20);
			}
		}

	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		super.update (container, game, delta);
		if(!jeuTermine){
			jeuTermine = isFini();
			addBonus();
			for(int i=0;i<snakes.size();i++) {
				Snake snake = snakes.get(i);

				snake.GScore(1);
				snake.update(container, game, delta);

				for (int j = 0; j < bonus.size(); j++) {
					bonus.get(j).update(container, game, delta);
					if (!snakes.get(i).mort) {
						if (bonus.get(j).isInBonus(snakes.get(i).body.get(0))) {
							applyBonus(bonus.get(j), snakes.get(i));
							bonus.remove(j);
							j--;
						}
					}
				}

				for (int j = 0; j < snakes.size(); j++) {

					if (j != i) {
						if (!snakes.get(i).mort) {
							if (collide(snake.body.get(0), snakes.get(j),false)) {
								snake.meurt();
							}
						}
					}
				}
			}
		}
		if (jeuTermine && morts.size()==0) {
			for(int i=0;i<snakes.size();i++){
				if(!snakes.get(i).mort)snakes.get(i).GScore(200);
			}
			Snake tri[] = new Snake[snakes.size()];
			for (int i=0; i<snakes.size(); i++) {
				tri[i] = snakes.get(i);
			}
			for (int i=tri.length-1 ; i>0 ; i--) {
				for (int j=0; j<i ; j++) {
					if (tri[j+1].score<tri[j].score) {
						Snake tmp = tri[j+1];
						tri[j+1] = tri[j];
						tri[j]=tmp;
					}
				}
			}
			morts = new ArrayList<Snake>(Arrays.asList(tri));
		}
	}

	private void applyBonus(Bonus bonus, Snake snake ) {
		bonus.applyBonus(snake);

		if(bonus.type == Bonus.bonusType.bInverseBonus){
			for(int i= 0;i<snakes.size();i++){
				if(!snakes.get(i).equals(snake)){
					snakes.get(i).inverse = !snakes.get(i).inverse;
				}
			}
		}
	}

	private boolean collide(Point point, Snake snake, boolean exceptHead) {

		for(int i=exceptHead?3:0;i<snake.body.size();i++)
		{
			if(snake.body.get(i).x==point.x && snake.body.get(i).y==point.y){
				if(i==0)snakes.get(i).meurt();
				return true;
			}
		}
		return false;
	}

	public void addBonus(Bonus bonusLoc)
	{
		bonus.add(bonusLoc);
	}

	private void addBonus(){
		Random r =  new Random();
		if(r.nextFloat() >= 0.99){
			bonus.add(Bonus.RandomBonus(this, new Point(r.nextInt(nbcasesl)-28,r.nextInt(nbcasesh))));
		}
	}

	public boolean isFini() {

		int compt = 0;

		if(snakes.size()==1){
			if(snakes.get(0).mort)return true;
			else return false;
		}

		for(int i=0;i<snakes.size();i++){
			if(!snakes.get(i).mort)compt++;
		}

		if(compt<=1)return true;

		return false;
	}

	@Override
	public void play(GameContainer container, StateBasedGame game) {
		AppGame appGame = (AppGame) game;
		int nJoueur = appGame.appPlayers.size();
		longueur = container.getWidth();
		hauteur = container.getHeight();
		nbcasesh = hauteur/10;
		nbcasesl = longueur/10;
		widthBandeau = longueur-1000;
		this.snakes = new ArrayList<Snake>();
		this.morts = new ArrayList<Snake>();
		for (int i=0 ; i<nJoueur ; i++) {
			snakes.add(new Snake(this, (100-nJoueur)/(nJoueur+1) + i*((100-nJoueur)/(nJoueur+1)+1), appGame.appPlayers.get(i)));
		}
		this.bonus = new ArrayList <Bonus> ();
		this.jeuTermine=false;
		soundMusicBackground.playAsMusic(1f, .3f, true);
	}

	@Override
	public void pause(GameContainer container, StateBasedGame game) {
		this.soundMusicBackgroundPos = soundMusicBackground.getPosition();
		soundMusicBackground.stop();
	}

	@Override
	public void resume(GameContainer container, StateBasedGame game) {
		soundMusicBackground.playAsMusic(1, .3f, true);
		soundMusicBackground.setPosition(this.soundMusicBackgroundPos);
	}

	@Override
	public void stop(GameContainer container, StateBasedGame game) {
		soundMusicBackground.stop();
	}

}
