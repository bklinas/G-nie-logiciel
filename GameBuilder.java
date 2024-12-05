package apprentissage;

import apprentissage.GameConfiguration;
import apprentissage.Block;
import apprentissage.Map;
import apprentissage.Chat;

/**
 * 
 * 
 * @author karinemoussaoui9@gmail.com
 *
 */
class GameBuilder {

	public static Map buildMap() {
		return new Map(GameConfiguration.LINE_COUNT, GameConfiguration.COLUMN_COUNT);
	}

	public static MobileElementManager buildInitMobile(Map map) {
		MobileElementManager manager = new MobileElementManager(map);
		
		initializeChat(map, manager);
		
		return manager;
	}

	private static void initializeChat(Map map, MobileElementManager manager) {
		Block block = map.getBlock(GameConfiguration.LINE_COUNT - 1, (GameConfiguration.COLUMN_COUNT - 1) / 2);
		Chat chat = new Chat(block);
		manager.set(chat);
	}

}


