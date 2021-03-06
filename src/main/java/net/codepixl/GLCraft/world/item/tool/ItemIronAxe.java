package net.codepixl.GLCraft.world.item.tool;

public class ItemIronAxe extends Tool{
	@Override
	public ToolType getToolType(){
		return ToolType.AXE;
	}
	
	@Override
	public float getStrength(){
		return 4.5f;
	}
	
	@Override
	public String getName(){
		return "Iron Axe";
	}
	
	@Override
	public String getTextureName(){
		return "axe_iron";
	}
}
