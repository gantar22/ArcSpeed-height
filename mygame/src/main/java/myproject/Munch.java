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


class Munch{
public Munch(){
	
	
}

void init(){
glEnable(GL_BLEND);
	glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

}

public void paintEntities(ArrayList<Entity> eList){
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	for(Entity e : eList){
		Color.white.bind(); 
		
		e.getTexture().bind();
		
		GL11.glBegin(GL11.GL_QUADS);
		    GL11.glTexCoord2f((float)e.getSpriteBox().getX()/e.getTexture().getTextureWidth(),(float)e.getSpriteBox().getY()/e.getTexture().getTextureHeight());
		    GL11.glVertex2f((float)e.getPos().x,(float)e.getPos().y);
		    GL11.glTexCoord2f((float)((e.getSpriteBox().getX() + e.getSpriteBox().getWidth())/e.getTexture().getTextureWidth()),(float)(e.getSpriteBox().getY()/e.getTexture().getTextureHeight()));
		    GL11.glVertex2f((float)((float)e.getPos().x+e.getSpriteBox().getWidth()),(float)e.getPos().y);
		    GL11.glTexCoord2f((float)((e.getSpriteBox().getX() + e.getSpriteBox().getWidth())/e.getTexture().getTextureWidth()),(float)((e.getSpriteBox().getY() + e.getSpriteBox().getHeight())/e.getTexture().getTextureHeight()));
		    GL11.glVertex2f((float)((float)e.getPos().x+e.getSpriteBox().getWidth()),(float)((float)e.getPos().y+e.getSpriteBox().getHeight()));
		    GL11.glTexCoord2f((float)(e.getSpriteBox().getX()/e.getTexture().getTextureWidth()),(float)((e.getSpriteBox().getY() + e.getSpriteBox().getHeight())/e.getTexture().getTextureHeight()));
		    GL11.glVertex2f((float)e.getPos().x,(float)((float)e.getPos().y+e.getSpriteBox().getHeight()));
		GL11.glEnd();
	}
	
}


}
