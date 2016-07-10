package net.codepixl.GLCraft.GUI;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import java.awt.Desktop;
import java.net.URI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Callable;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.codepixl.GLCraft.GLCraft;
import net.codepixl.GLCraft.GUI.Elements.GUIButton;
import net.codepixl.GLCraft.GUI.Inventory.Elements.GUISlot;
import net.codepixl.GLCraft.plugin.PluginManager;
import net.codepixl.GLCraft.plugin.PluginManagerWindow;
import net.codepixl.GLCraft.render.TextureManager;
import net.codepixl.GLCraft.util.Constants;
import net.codepixl.GLCraft.util.Spritesheet;
import net.codepixl.GLCraft.util.logging.CrashHandler;
import net.codepixl.GLCraft.world.tile.Tile;
import net.lingala.zip4j.core.ZipFile;

public class GUIStartScreen extends GUIScreen{
	
	private static final int MIDDLE = Constants.WIDTH/2;
	private static final int SINGLEPLAYERY = (int) (Constants.HEIGHT*0.2);
	private static final int PLUGINMANAGERY = (int) (Constants.HEIGHT*0.4);
	private static final int TEXTUREY = (int) (Constants.HEIGHT * 0.6);
	private static final int BUGY = (int) (Constants.HEIGHT * 0.8);
	
	private GUIButton startButton, pluginManagerButton, quitButton, bugButton,texturepackButton;

	public GUIStartScreen() {
		startButton = new GUIButton("Singleplayer", MIDDLE, SINGLEPLAYERY, new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				Constants.setState(Constants.GAME);
				Constants.world.getWorldManager().createWorld();
				glDisable(GL_TEXTURE_2D);
				GUIManager.getMainManager().closeGUI();
				return null;
			}
		});
		pluginManagerButton = new GUIButton("Plugin Manager", MIDDLE, PLUGINMANAGERY, new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				new PluginManagerWindow(GLCraft.getGLCraft().getPluginManager()).setVisible(true);
				return null;
			}
		});
		
		texturepackButton = new GUIButton("Texturepacks", MIDDLE, TEXTUREY, new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				JFileChooser jf = new JFileChooser();
				jf.setCurrentDirectory(new File(System.getProperty("user.home") + "/GLCraft/Texturepacks"));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Compressed GLCraft Texturepack", "zip");
				jf.setFileFilter(filter);
				int returnVal = jf.showOpenDialog(null);
				File TP = null;
			
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					TP = jf.getSelectedFile();
					File tempFolder = new File(System.getProperty("user.home") + "/GLCraft/Texturepacks/tmp/textures");
					tempFolder.delete();
					tempFolder.mkdirs();
					ZipFile zip = new ZipFile(TP);
					zip.extractAll(tempFolder.getAbsolutePath());
				}

				File texturepackInfo = new File(System.getProperty("user.home") + "/GLCraft/Texturepacks/currentTP.txt");
				try {
					texturepackInfo.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				PrintWriter writer = null;
				try {
					writer = new PrintWriter(texturepackInfo.getAbsolutePath(), "UTF-8");
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				writer.println(TP.getName().substring(0, TP.getName().length()-4));
				writer.close();
				
				JOptionPane.showMessageDialog(null, "You must restart GLCraft for changes to take effect.");

				
				return null;
			}
		});
		quitButton = new GUIButton("Quit", (Constants.FONT.getWidth("quit") + 40) / 2, Constants.BTNHEIGHT / 2 + 10, new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				System.exit(0);
				return null;
			}
		});
		bugButton = new GUIButton("Report a bug", MIDDLE, BUGY, new Callable<Void>(){
			@Override
			public Void call() throws Exception {
				Desktop.getDesktop().browse(new URI("https://gitreports.com/issue/Codepixl/GLCraft"));
				return null;
			}
		});
		this.addElement(quitButton);
		this.addElement(startButton);
		this.addElement(pluginManagerButton);
		this.addElement(bugButton);
		this.addElement(texturepackButton);
	}

	@Override
	public void drawBG() {
		Spritesheet.atlas.bind();
		int howManyWide = (Constants.WIDTH/64)+1;
		int howManyTall = (Constants.HEIGHT/64)+1;
		float[] texCoords = TextureManager.tile(Tile.Stone);
		for(int x = 0; x < howManyWide*64; x+=64){
			for(int y = 0; y < howManyTall*64; y+=64){
				glBegin(GL_QUADS);
				glTexCoord2f(texCoords[0], texCoords[1]);
				glVertex2f(x, y);
				glTexCoord2f(texCoords[0] + Spritesheet.atlas.uniformSize(), texCoords[1]);
				glVertex2f(x, y+64);
				glTexCoord2f(texCoords[0] + Spritesheet.atlas.uniformSize(), texCoords[1] + Spritesheet.atlas.uniformSize());
				glVertex2f(x+64, y+64);
				glTexCoord2f(texCoords[0], texCoords[1] + Spritesheet.atlas.uniformSize());
				glVertex2f(x+64, y);
				glEnd();
			}
		}
	}
}
