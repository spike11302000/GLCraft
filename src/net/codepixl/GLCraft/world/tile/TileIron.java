package net.codepixl.GLCraft.world.tile;

import com.nishu.utils.Color4f;

import net.codepixl.GLCraft.world.tile.material.Material;

public class TileIron extends Tile{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Iron Ore";
	}
	
	@Override
	public Material getMaterial(){
		return Material.STONE;
	}

	@Override
	public byte getId() {
		// TODO Auto-generated method stub
		return 6;
	}
	
	@Override
	public String getTextureName(){
		return "iron";
	}

	@Override
	public Color4f getColor() {
		// TODO Auto-generated method stub
		return Color4f.WHITE;
	}
	
	@Override
	public float getHardness(){
		return 5.5f;
	}

	@Override
	public boolean isTransparent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canPassThrough() {
		// TODO Auto-generated method stub
		return false;
	}

}
