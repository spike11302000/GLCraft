package net.codepixl.GLCraft.util.data.saves;

import net.codepixl.GLCraft.util.Constants;
import net.codepixl.GLCraft.world.WeatherState;

import java.io.File;

public class Save implements Comparable<Save>{
	public String name,dispName,version,format;
	public long timestamp, worldTime, seed;
	public boolean isDedicated = false, hasSeed = false;
	public WeatherState weatherState;
	public Save(String name, String dispName, String version, String format, long seed){
		this.name = name;
		this.dispName = dispName;
		this.version = version;
		this.format = format;
		this.seed = seed;
		this.hasSeed = true;
	}
	
	public Save(String name, String dispName, String version, String format, long timestamp, long worldTime, WeatherState weatherState){
		this.name = name;
		this.dispName = dispName;
		this.version = version;
		this.format = format;
		this.timestamp = timestamp;
		this.worldTime = worldTime;
		this.weatherState = weatherState;
	}
	
	public Save(String name, String dispName, String version, String format, long timestamp, long worldTime, boolean dedicated, WeatherState weatherState){
		this.name = name;
		this.dispName = dispName;
		this.version = version;
		this.format = format;
		this.timestamp = timestamp;
		this.worldTime = worldTime;
		this.isDedicated = dedicated;
		this.weatherState = weatherState;
	}
	
	@Override
	public String toString(){
		return "|Name: "+name+"|Display Name: "+dispName+"|Version: "+version+"|Format: "+format+"|";
	}

	@Override
	public int compareTo(Save o) {
		return Long.compare(o.timestamp, this.timestamp);
	}
	
	public File getDirectory(){
		if(this.isDedicated)
			return new File(this.name);
		else
			return new File(Constants.GLCRAFTDIR+File.separator+"saves"+File.separator+this.name);
	}
}
