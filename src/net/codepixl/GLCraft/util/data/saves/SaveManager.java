package net.codepixl.GLCraft.util.data.saves;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.evilco.mc.nbt.stream.NbtInputStream;
import com.evilco.mc.nbt.stream.NbtOutputStream;
import com.evilco.mc.nbt.tag.TagByte;
import com.evilco.mc.nbt.tag.TagCompound;
import com.evilco.mc.nbt.tag.TagFloat;
import com.evilco.mc.nbt.tag.TagInteger;
import com.evilco.mc.nbt.tag.TagList;
import com.evilco.mc.nbt.tag.TagLong;
import com.evilco.mc.nbt.tag.TagString;

import net.codepixl.GLCraft.util.Constants;
import net.codepixl.GLCraft.world.WorldManager;
import net.codepixl.GLCraft.world.entity.Entity;
import net.codepixl.GLCraft.world.entity.NBTUtil;
import net.codepixl.GLCraft.world.entity.mob.EntityPlayer;
import net.codepixl.GLCraft.world.item.Item;
import net.codepixl.GLCraft.world.item.ItemStack;
import net.codepixl.GLCraft.world.tile.Tile;

public class SaveManager {
	public static void saveWorld(WorldManager worldManager, String name){
		EntityPlayer p = worldManager.getEntityManager().getPlayer();
		TagCompound compound = new TagCompound("Player");
		TagList posList = new TagList("Pos");
		posList.addTag(new TagFloat("",p.getPos().x));
		posList.addTag(new TagFloat("",p.getPos().y));
		posList.addTag(new TagFloat("",p.getPos().z));
		TagList rotList = new TagList("Rot");
		rotList.addTag(new TagFloat("",p.getRot().x));
		rotList.addTag(new TagFloat("",p.getRot().y));
		rotList.addTag(new TagFloat("",p.getRot().z));
		TagList velList = new TagList("Vel");
		velList.addTag(new TagFloat("",p.getVel().x));
		velList.addTag(new TagFloat("",p.getVel().y));
		velList.addTag(new TagFloat("",p.getVel().z));
		TagLong timeTag = new TagLong("TimeAlive", p.timeAlive);
		TagString typeTag = new TagString("type", EntityPlayer.class.getSimpleName());
		compound.setTag(posList);
		compound.setTag(rotList);
		compound.setTag(velList);
		compound.setTag(timeTag);
		compound.setTag(typeTag);
		TagList inventory = new TagList("Inventory");
		for(int i = 0; i < p.getInventory().length; i++){
			ItemStack stack = p.getInventory(i);
			if(!stack.isNull()){
				TagCompound slot = new TagCompound("");
				slot.setTag(new TagInteger("slot",i));
				slot.setTag(new TagByte("isItem",(byte) (stack.isItem() ? 1 : 0 )));
				slot.setTag(new TagByte("id",stack.getId()));
				slot.setTag(new TagByte("count",(byte)stack.count));
				inventory.addTag(slot);
			}
		}
		compound.setTag (inventory);
		try{
			worldManager.saveChunks(name);
			File f = new File(Constants.GLCRAFTDIR+"saves/"+name+"/");
			f.mkdirs();
			FileOutputStream outputStream;
			outputStream = new FileOutputStream(new File(f,"player.nbt"));
			NbtOutputStream nbtOutputStream = new NbtOutputStream(outputStream);
			nbtOutputStream.write(compound);
			nbtOutputStream.close();
			worldManager.getEntityManager().save(name);
		}catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void loadWorld(WorldManager worldManager, String name) {
		try {
			EntityPlayer p = worldManager.getEntityManager().getPlayer();
			worldManager.loadChunks(name);
			worldManager.entityManager.removeAll();
			p.setInventory(new ItemStack[9]);
			for(int i = 0; i < 9; i++){
				p.setInventory(i, new ItemStack());
			}
			FileInputStream inputStream = new FileInputStream(Constants.GLCRAFTDIR+"/saves/"+name+"/player.nbt");
			NbtInputStream nbtInputStream = new NbtInputStream(inputStream);
			TagCompound tag = (TagCompound)nbtInputStream.readTag();
			if(tag != null){
				if(tag.getList("Inventory", TagCompound.class) != null){
					List<TagCompound> inventory = tag.getList("Inventory",TagCompound.class);
					Iterator<TagCompound> i = inventory.iterator();
					while(i.hasNext()){
						TagCompound t = i.next();
						int slot = t.getInteger("slot");
						ItemStack stack;
						if(t.getByte("isItem") == 0){
							stack = new ItemStack(Tile.getTile(t.getByte("id")));
						}else{
							stack = new ItemStack(Item.getItem(t.getByte("id")));
						}
						stack.count = t.getByte("count");
						p.setInventory(slot, stack);
					}
				}
				List<TagFloat> pos = tag.getList("Pos", TagFloat.class);
				p.setPos(new Vector3f(pos.get(0).getValue(),pos.get(1).getValue(),pos.get(2).getValue()));
				List<TagFloat> rot = tag.getList("Rot", TagFloat.class);
				p.setRot(new Vector3f(rot.get(0).getValue(),rot.get(1).getValue(),rot.get(2).getValue()));
				List<TagFloat> vel = tag.getList("Vel", TagFloat.class);
				p.setVel(new Vector3f(vel.get(0).getValue(),vel.get(1).getValue(),vel.get(2).getValue()));
			}
			inputStream = new FileInputStream(Constants.GLCRAFTDIR+"saves/"+name+"/entities.nbt");
			nbtInputStream = new NbtInputStream(inputStream);
			tag = (TagCompound)nbtInputStream.readTag ();
			List<TagCompound> l = tag.getList("Entities", TagCompound.class);
			Iterator<TagCompound> i = l.iterator();
			while(i.hasNext()){
				TagCompound t = i.next();
				Entity e = null;
				try {
					e = NBTUtil.readEntity(t, worldManager);
				} catch (NoSuchMethodException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(e != null){
					worldManager.spawnEntity(e);
				}else{
					//System.err.println("WARNING: ENTITY IN SAVE WAS NULL.");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
		}
	}
}