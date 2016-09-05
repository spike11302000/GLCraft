package net.codepixl.GLCraft.util;

import org.lwjgl.util.vector.Vector3f;

public class Vector3i {
	public int x,y,z;
	public Vector3i(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3i(float x, float y, float z){
		this.x = (int) x;
		this.y = (int) y;
		this.z = (int) z;
	}
	
	public Vector3i(Vector3f f){
		this.x = (int)f.x;
		this.y = (int)f.y;
		this.z = (int)f.z;
	}
	
	@Override
	public String toString(){
		return "["+x+","+y+","+z+"]";
	}
	
	@Override
	public int hashCode() {
	    int hash = (x << 2)+(y << 1)+(z);
	    return hash;
	}
	
	@Override
	public boolean equals(Object o){
		return (o instanceof Vector3i) && (((Vector3i)o).x == x) && (((Vector3i)o).y == y) && (((Vector3i)o).z == z);
	}
   
}