package net.codepixl.GLCraft.util;

import net.codepixl.GLCraft.render.util.Tesselator;
import net.codepixl.GLCraft.util.logging.GLogger;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import java.util.Arrays;
import java.util.Random;

public class Constants {
	public static Random rand = new Random();
	public static int renderDistance = 10;
	public static int worldLengthChunks = 10;
	public static int seaLevel = 62;
	public static final int CHUNKSIZE = 16;
	public static final float textSize = 0.5f;
	public static boolean doneGenerating = false;
	public static final int START_SCREEN = 0;
	public static final int GAME = 1;
	public static final int SERVER = 2;
	public static final String GLCRAFTDIR = getGamePath();
	public static int maxFPS;
	public static int GAME_STATE = START_SCREEN;
	public static int worldLength = CHUNKSIZE * worldLengthChunks;
	public static int dayLengthMS = 1200000;
	public static String[] SystemProperty = new String[54];
	public static String[] SystemPropertyName = new String[] { "java.runtime.name", "sun.boot.library.path",
			"java.vm.version", "java.vm.vendor", "java.vendor.url", "path.separator", "java.vm.name",
			"file.encoding.pkg", "user.country", "sun.java.launcher", "sun.os.patch.level",
			"java.vm.specification.name", "user.dir", "java.runtime.version", "java.awt.graphicsenv",
			"java.endorsed.dirs", "os.arch", "java.io.tmpdir", "java.vm.specification.vendor",
			"os.name", "sun.jnu.encoding", "java.library.path", "sun.awt.enableExtraMouseButtons",
			"java.specification.name", "java.class.version", "sun.management.compiler", "os.version", "user.home",
			"user.timezone", "java.awt.printerjob", "file.encoding", "java.specification.version", "user.name",
			"java.class.path", "java.vm.specification.version", "sun.arch.data.model", "java.home", "sun.java.command",
			"java.specification.vendor", "user.language", "awt.toolkit", "java.vm.info", "java.version",
			"java.ext.dirs", "sun.boot.class.path", "java.vendor", "file.separator", "java.vendor.url.bug",
			"sun.cpu.endian", "sun.io.unicode.encoding", "sun.font.fontmanager", "sun.desktop", "sun.cpu.isalist"};
	public static int FPS = 0; //FPS that updates every so often
	public static int QFPS = 0; //FPS that updates every frame
	public static Vector3f[] stars;
	public static final int LOAD_DISTANCE = 5; //The distance from the player at which chunks will load

	public static void gatherSystemInfo() {
		Arrays.sort(SystemPropertyName);
		GLogger.log("Gathering System Info...\n", LogSource.SILENT);
		for (int i = 0; i < SystemPropertyName.length; i++) {
			SystemProperty[i] = System.getProperty(SystemPropertyName[i]);
			GLogger.log("	" + SystemPropertyName[i] + " = " + SystemProperty[i], LogSource.SILENT);
		}
		GLogger.log("\n", LogSource.SILENT);
	}

	public static String getGamePath() {
               return System.getProperty("user.home") + System.getProperty("file.separator") + "GLCraft" + System.getProperty("file.separator");
        }

	public static void setDoneGenerating(boolean generating) {
		doneGenerating = generating;
	}

	public static void setState(int state) {
		GAME_STATE = state;
	}

	public static byte[] trim(byte[] bytes) {
		int i = bytes.length - 1;
		while (i >= 0 && bytes[i] == 0) {
			--i;
		}

		return Arrays.copyOf(bytes, i + 1);
	}

	public static int randInt(int min, int max) {
		return randInt(rand,min,max);
	}
	
	public static int randInt(Random rand, int min, int max) {
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	public static float randFloat(float min, float max) {
		return randFloat(rand,min,max);
	}
	
	public static float randFloat(Random rand, float min, float max) {
		float randomNum = rand.nextFloat() * (max - min) + min;
		return randomNum;
	}
	
	public static void initConstants(){
		
	}

	public static double randDouble(double max, double min) {
		return randDouble(rand,max,min);
	}
	
	public static double randDouble(Random rand, double max, double min) {
		double randomNum = rand.nextDouble() * (max - min) + min;
		return randomNum;
	}
	
	public static void generateStars(){
		Random r = new Random(10023L);
		stars = new Vector3f[3000];
		for(int i = 0; i < stars.length; i++){
			stars[i] = MathUtils.randomSpherePoint(r, 500);
		}
	}
	
	public static void initGL(){
		Tesselator.initFont();
	}
	
	public static void init(boolean dedicatedServer){
		if(!dedicatedServer)
			maxFPS = Integer.parseInt(SettingsManager.getSetting("max_fps"));
	}

	public static int getWidth(){
		return Display.getWidth();
	}
	
	public static int getHeight(){
		return Display.getHeight();
	}

	public static int getGUIScale(){
		return getWidth() > 2000 ? 2 : 1;
	}

}
