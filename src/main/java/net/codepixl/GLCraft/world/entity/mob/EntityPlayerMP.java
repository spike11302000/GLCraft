package net.codepixl.GLCraft.world.entity.mob;

import com.nishu.utils.Color4f;
import net.codepixl.GLCraft.network.packet.PacketChat;
import net.codepixl.GLCraft.network.packet.PacketPlayerAction;
import net.codepixl.GLCraft.network.packet.PacketRespawn;
import net.codepixl.GLCraft.network.packet.PacketSetInventory;
import net.codepixl.GLCraft.render.Shape;
import net.codepixl.GLCraft.render.util.Spritesheet;
import net.codepixl.GLCraft.render.util.Tesselator;
import net.codepixl.GLCraft.util.Constants;
import net.codepixl.GLCraft.util.MathUtils;
import net.codepixl.GLCraft.util.Vector2i;
import net.codepixl.GLCraft.world.Chunk;
import net.codepixl.GLCraft.world.WorldManager;
import net.codepixl.GLCraft.world.entity.Entity;
import net.codepixl.GLCraft.world.entity.EntityItem;
import net.codepixl.GLCraft.world.item.Item;
import net.codepixl.GLCraft.world.item.ItemStack;
import net.codepixl.GLCraft.world.tile.Tile;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.TextureImpl;

import java.util.ArrayList;
import java.util.Iterator;

public class EntityPlayerMP extends EntityPlayer{

	public EntityPlayerMP(Vector3f pos, WorldManager w) {
		super(pos, w);
	}
	
	@Override
	public void render(){
		GL11.glPushMatrix();
		GL11.glTranslatef(this.pos.x, this.pos.y, this.pos.z);
		GL11.glRotatef(-rot.y, 0, 1, 0);
		GL11.glTranslatef(-(float)getAABB().r[0], 0, -(float)getAABB().r[2]);
		GL11.glBegin(GL11.GL_QUADS);
		Shape.createRect(0,0,0, new Color4f(light,light,light,1f), Tile.Bedrock.getTexCoords(), (float)getAABB().r[0]*2f, (float)getAABB().r[1]*2f, (float)getAABB().r[2]*2f);
		GL11.glEnd();
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glFrontFace(GL11.GL_CW);
		GL11.glTranslatef(pos.x, pos.y+(float)getAABB().r[0]*2f+1.75f, pos.z);
		GL11.glRotatef(180, 1, 0, 0);
		GL11.glRotatef(worldManager.getPlayer().getRot().y, 0, 1, 0);
		GL11.glTranslatef(-Tesselator.getFontWidth(getName())/2*0.02f, 0, 0);
		GL11.glScalef(0.02f, 0.02f, 0.02f);
		TextureImpl.bindNone();
		Tesselator.drawString(0, 0, getName());
		TextureImpl.unbind();
		GL11.glFrontFace(GL11.GL_CCW);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
		Spritesheet.atlas.bind();
	}
	
	@Override
	public void update(){
		if(!shouldUpdate)
			return;
		super.update();
		Iterator<Entity> i = (Iterator<Entity>) worldManager.getEntityManager().getEntitiesInRadiusOfEntityOfType(this, EntityItem.class, 1f).iterator();
		while(i.hasNext()){
			updatedInventory = true;
			EntityItem e = (EntityItem) i.next();
			if(e.getCount() > 0) {
				e.setCount(this.addToInventory(e.getItemStack()));
				if(e.getCount() <= 0) {
					e.setDead(true);
				}
			}
		}
		if(updatedInventory){
			worldManager.sendPacket(new PacketSetInventory(this));
			this.updatedInventory = false;
		}
		if(this.isDead()){
			this.health = 20f;
			this.respawn();
			this.dropAllItems();
			this.updatedInventory = true;
		}
	}
	
	@Override
	public void clientUpdate(){
		this.light = worldManager.getLightIntensity((int)this.pos.x, (int)this.pos.y, (int)this.pos.z);
	}
	
	@Override
	public void updateMouse(){}
	
	@Override
	public void respawn(){
		this.setDead(false);
		worldManager.sendPacket(new PacketRespawn(),this);
	}
	
	public void respawnServerSide(){
		int x = (int) (Constants.CHUNKSIZE*(Constants.worldLengthChunks/2f));
		int z = (int) (Constants.CHUNKSIZE*(Constants.worldLengthChunks/2f));
		int y = Constants.CHUNKSIZE*Constants.worldLengthChunks+1;
		while(Tile.getTile((byte) worldManager.getTileAtPos(x, y-1, z)).canPassThrough() || Tile.getTile((byte) worldManager.getTileAtPos(x, y-1, z)) == Tile.Void){y--;}
		this.setPos(new Vector3f(x,y,z));
		this.health = 20f;
		this.fallDistance = 0;
		this.onFire = 0;
	}
	
	@Override
	public void updateKeyboard(float a, float b){}

	public void dropHeldItem(boolean all) {
		ItemStack i = new ItemStack(this.getSelectedItemStack());
		if(!i.isNull()){
			if(!all)
				i.count = 1;
			EntityItem e = new EntityItem(i, pos.x, pos.y+this.eyeLevel, pos.z, worldManager);
			
			e.setVelocity(MathUtils.RotToVel(this.getRot(), 1f));
			worldManager.spawnEntity(e);
		}
		if(all)
			this.setSelectedItemStack(new ItemStack());
		else{
			this.getSelectedItemStack().count--;
			if(this.getSelectedItemStack().count <= 0)
				this.setSelectedItemStack(new ItemStack());
		}
		this.updatedInventory = true;
	}
	
	@Override
	public void sendMessage(String msg){
		worldManager.sendPacket(new PacketChat(msg), this);
	}

	public void dropItem(PacketPlayerAction p) {
		if(p.type == PacketPlayerAction.Type.DROPOTHERITEM && !p.isNull){
			ItemStack s;
			if(p.isTile)
				s = new ItemStack(Tile.getTile(p.id),p.count,p.meta);
			else
				s = new ItemStack(Item.getItem(p.id),p.count,p.meta);
			
			EntityItem e = new EntityItem(s, pos.x, pos.y+this.eyeLevel, pos.z, worldManager);
			e.setVelocity(MathUtils.RotToVel(this.getRot(), 1f));
			worldManager.spawnEntity(e);
		}
	}

}
