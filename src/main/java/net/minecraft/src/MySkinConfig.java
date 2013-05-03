package net.minecraft.src;
import java.io.*;

public class MySkinConfig {
	private static String skinsf = "http://skins.minecraft.net/MinecraftSkins/%s.png";
	private static String cloaksf = "http://skins.minecraft.net/MinecraftCloaks/%s.png";
	private static String loginf = "http://session.minecraft.net/game/joinserver.jsp?user=%s&sessionId=%s&serverId=%s";
	private static String checkf = "http://session.minecraft.net/game/checkserver.jsp?user=%s&serverId=%s";
	private static MySkinConfig m = new MySkinConfig();
	public MySkinConfig() {
		skinsf = "http://nekocraft.com/api/skin/%s";
		cloaksf = "http://nekocraft.com/api/cloak/%s";
		loginf = "http://nekocraft.com/api/login/joinserver/?user=%s&session=%s&serverId=%s";
		checkf = "http://nekocraft.com/api/login/checkserver/?user=%s&serverId=%s";

		if (true) {
			return;
		}

		System.out.println("[MySkinConfig] Loading skins.txt...");
		File file = new File("skins.txt");

		try {
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {}

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;

			for (int line = 0; (tempString = reader.readLine()) != null && line < 4; line++) {
				if (line == 0) {
					skinsf = tempString;
					System.out.println("[MySkinConfig] Skins URL Format set to " + tempString);
				} else if (line == 1) {
					cloaksf = tempString;
					System.out.println("[MySkinConfig] Cloaks URL Format set to " + tempString);
				} else if (line == 2) {
					loginf = tempString;
					System.out.println("[MySkinConfig] Login URL Format set to " + tempString);
				} else if (line == 3) {
					checkf = tempString;
					System.out.println("[MySkinConfig] Check URL Format set to " + tempString);
				}
			}
		} catch (IOException e) {}
		finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {}
			}
		}
	}
	public static String getSkinsUrl(String name) {
		return String.format(skinsf, name);
	}
	public static String getCloaksUrl(String name) {
		return String.format(cloaksf, name);
	}
	public static String getLoginUrl(String p1, String p2, String p3) {
		return String.format(loginf, p1, p2, p3);
	}
	public static String getCheckUrl(String p1, String p2) {
		return String.format(checkf, p1, p2);
	}
}