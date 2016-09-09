package net.codepixl.GLCraft.GUI.Elements;

import java.util.concurrent.Callable;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.TextureImpl;

import com.nishu.utils.Color4f;

import net.codepixl.GLCraft.GUI.GUIScreen;
import net.codepixl.GLCraft.render.Shape;
import net.codepixl.GLCraft.util.Constants;

public class GUISlider extends GUIScreen{
	
	private float val;
	private int max;
	private int min;
	private boolean grabbed = false;
	private Callable<Void> callback;
	public String maxlbl = "";
	public String lbl;
	
	public GUISlider(String lbl, int x, int y, int length, int min, int max, Callable<Void> callback){
		this.x = x;
		this.y = y;
		this.height = 20;
		this.width = length;
		this.min = min;
		this.max = max;
		this.val = min;
		this.callback = callback;
		this.lbl = lbl;
	}
	
	@Override
	public void render(){
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glBegin(GL11.GL_QUADS);
		Shape.createTexturelessRect(0, 0, width, height, Color4f.BLACK);
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_LINE_LOOP);
		Shape.createTexturelessRect(0, 0, width, height, Color4f.GRAY);
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_QUADS);
		Shape.createTexturelessRect(((float)(val-min)/(float)(max-min))*(width-10), 0, 10f, height, Color4f.WHITE);
		GL11.glEnd();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		String lblend = (!maxlbl.equals("") && val == max) ? maxlbl : Integer.toString((int) val);
		String fulllbl = lbl+":"+lblend;
		Constants.FONT.drawString(width/2-Constants.FONT.getWidth(fulllbl)/2, height/2-Constants.FONT.getHeight()/2, fulllbl, Color.gray);
		TextureImpl.unbind();
	}
	
	public int getVal(){
		return (int) val;
	}
	
	public void setVal(int val){
		this.val = val;
	}
	
	private void sliderUpdate(int xof, int yof){
		int mouseY = Mouse.getY()+yof;
		int mouseX = Mouse.getX()-xof;
		mouseY = -mouseY+Constants.HEIGHT;
		float barX = ((float)val/(float)max)*(width-height);
		if(mouseY <= height && mouseY >= 0){
			if(mouseX <= barX+20 && mouseX >= barX){
				if(Mouse.isButtonDown(0)){
			    	grabbed = true;
				}
			}
		}
		
		if(grabbed){
			float dx = Mouse.getDX();
			val+=dx/(float)(width-height)*max;
			if(val<min)
	    		val=min;
	    	else if(val>max)
	    		val=max;
			if(dx != 0){
				try {
					callback.call();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(!Mouse.isButtonDown(0))
				grabbed = false;
		}
	}
	
	@Override
	public void input(int xof, int yof){
		sliderUpdate(xof+x, yof+y);
	}
	
	public float getMax() {
		return max;
	}
	
	public void setMax(int max) {
		this.max = max;
	}
	
	public float getMin() {
		return min;
	}
	
	public void setMin(int min) {
		this.min = min;
	}
}