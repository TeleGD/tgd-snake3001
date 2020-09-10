package pages;

import java.util.Arrays;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import app.AppGame;
import app.AppMenu;
import app.elements.MenuItem;

public class Games extends AppMenu {

	public Games(int ID) {
		super(ID);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) {
		super.initSize(container, game, 600, 400);
		super.init(container, game);
		this.setTitle("Menu des jeux");
		this.setSubtitle("Sans sous-titre");
		this.setMenu(Arrays.asList(new MenuItem[] {
			new MenuItem(AppGame.TITLES[AppGame.GAMES_SNAKE3001_WORLD]) {
				public void itemSelected() {
					Players players = (Players) game.getState(AppGame.PAGES_PLAYERS);
					Pause pause = (Pause) game.getState(AppGame.PAGES_PAUSE);
					players.setPreviousID(AppGame.PAGES_GAMES);
					players.setNextID(AppGame.GAMES_SNAKE3001_WORLD);
					pause.setPreviousID(AppGame.GAMES_SNAKE3001_WORLD);
					pause.setNextID(AppGame.PAGES_GAMES);
					game.enterState(AppGame.PAGES_PLAYERS);
				}
			},
			new MenuItem("Retour") {
				public void itemSelected() {
					AppGame appGame = (AppGame) game;
					for (int i = appGame.appPlayers.size() - 1; i >= 0; i--) {
						appGame.availableColorIDs.add(0, appGame.appPlayers.remove(i).getColorID());
					}
					appGame.enterState(AppGame.PAGES_WELCOME, new FadeOutTransition(), new FadeInTransition());
				}
			}
		}));
		this.setHint("SELECT A GAME");
	}

}
