package myproject;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.input.*;
import static org.lwjgl.opengl.GL11.*;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import java.io.*;

class Entity{

Rectangle hitBox = new Rectangle();
Texture texture;
Rectangle spriteBox = new Rectangle();
public Entity(Rectangle hitBox, String textDir, Rectangle spriteBox){ try{
this.hitBox = hitBox;
texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(textDir));
this.spriteBox = spriteBox;
}catch(IOException e){System.out.println("!!!!!!");}
}

public Texture getTexture(){
	return texture;
}
public Rectangle getSpriteBox(){
	return spriteBox;
}
public Point getPos(){
	return new Point((int)hitBox.getX(),(int)hitBox.getY());
}
}
