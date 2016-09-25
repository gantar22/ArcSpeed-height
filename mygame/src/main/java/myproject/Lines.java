package myproject;

import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.input.*;
import static org.lwjgl.opengl.GL11.*;

public class Lines{
//width and height of the display area
private int width = 800;
private int height = 600;
//when space is pressed then when released and awaits command
private boolean spacePressed = false;
private boolean spaceReleased = false;
//test box size, just for testing the display
int bsize = 100;
//X and Y value of the hitbox
int bx = 200;
int by = 10;

//color values for our box
float red = .5f;
float green = .0f;
float blue = 1f;
//frames played this second
int fpsIt = 0;
//last time fps was taken
long lastfps = 0;
//Time since last frame in milliseconds
int delta = 0;
//Time of last frame
long lastFrame = 0;
//stable fps value
int fps = 0;
//time since last color change
long lastColor = 0;
//color of last rotation
boolean cRot = false;
//line starting point and ending point
int lAlphaX = -300;
int lOmegaX = 300;
//int lAlphaY = 300; Why would we need this?, just put an x-intercept in your f(x)
//line length
int lLength =lOmegaX - lAlphaX;
//temporary X and Y value of vertex
int tempX;
int tempY;
//steps in approximating the curve
int steps = 600;
//box horzontal speed pixels per millisecond
double hSpeed= 0;
//pixels squared per millisecond at the bottom
double c = .05;
//amount of steps to move each millisecond
int dx = 0;
//amount of time passed since last move in milliseconds
int timeSinceD = 0;
//step at which the box resides
int bi = 0;
//is the game over
boolean gameOver = false;
//Time that gets rounded off and carried over
double carryTime = 0;

/*
* initializes the window
*/
public void alpha(){ try{
	Display.setDisplayMode(new DisplayMode(width,height));
	Display.setInitialBackground(1f,1f,1f);
	Display.create();
	}catch(LWJGLException e){
		e.printStackTrace();
		System.exit(0);
	}
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(0,width,0,height,1,-1);
	glMatrixMode(GL_MODELVIEW);

	lastFrame = now();

}
/*
*does the main loop
*/
public void go(){
	while(!Display.isCloseRequested() && !gameOver){
		
		akrasia();
		godel();
		ross();
		Display.update();
		Display.sync(60);
	}
	Display.destroy();
}
/*
*does logic
*/
public void godel(){
	if(spaceReleased){
		spaceReleased = false;
		System.out.println("! " + fps);
		
	}
	
	delta = dTime();
	moveBox(delta);
	System.out.println(bx + " " + by + " " + dx + " " + timeSinceD + " " + hSpeed);
	//spectralRotation(delta,.5);
	updateFPS();
}
/*
*changes the color values
*/
public void spectra(int c){
	switch(c){
	case 1: red = 1f;
		green = .5f;
		blue = 0f;
		break;
	case 2: red = .5f;
		green = .0f;
		blue = 1f;
		break;
	case 3: red = .0f;
		green = 1f;
		blue = .0f;
		break;
	case 4: red = .0f;
		green =.5f;
		blue = .5f;
		break;
	}
}
/*
*changes the color twice a second
*param delta time that has passed
*/
public void spectralRotation(int delta, double cSpeed){
	lastColor += delta;
	int cNum;
	if(cRot){
		cNum = 0;
	}else {
		cNum = 1;
	}
	if(lastColor > 1000*cSpeed){
		spectra(3 + cNum);
		lastColor = 0;
		cRot = !cRot;
	}
}
/*
*returns the difference in time from last call
*/
public int dTime(){
	int delta = (int) (now() - lastFrame);
	lastFrame = now();
	
	return delta;
}
/*
*counts the frames per second
*/
public void updateFPS(){
	if(now() - lastfps > 1000){
		lastfps = now();
		fps = fpsIt;
		fpsIt = 0;
	}
	fpsIt++;
}
/*
* handles input
*
*/
public void akrasia(){
	if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
		spacePressed = true;
	} else{
		if(spacePressed){
			spacePressed = false;
			spaceReleased = true;
		}
	}
}
/*
*paints the board
*/
public void ross(){
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glColor3f(red,green,blue);
	glBegin(GL_LINE_STRIP);
	/*for(int i = -40; i<40;i++){
		glVertex2f((5 * i + 400),(400 - (int)Math.pow(i,2)/8));
	}*/
	for(int i = 0; i <= steps; i++){
		tempX = (lAlphaX + (i * ((lOmegaX - lAlphaX)/steps)));
		tempY = f(tempX);
		glVertex2f(tempX+ (width/2) ,tempY + (height/2));
		//System.out.println(tempX + " " + tempY);
	}
	glEnd();
	glColor3f(0f,0f,0f);
	glBegin(GL_QUADS);
		glVertex2f(bx + bsize/2,by + bsize/2);
		glVertex2f(bx - bsize/2,by + bsize/2);
		glVertex2f(bx - bsize/2,by - bsize/2);
		glVertex2f(bx + bsize/2,by - bsize/2);
	glEnd();
	
}
/*
*calculates the hieght of the curve
*/
public int f(int x){
double scale = .007;
return  (int)((-(int)(Math.pow(x,2)) * scale) + 290);
}
/*
*return the current time
*/
public long now(){
	return (Sys.getTime() * 1000) / Sys.getTimerResolution();
}
/*
*move the box
*/
public void moveBox(int delta){
setSpeed(delta);
bi += dx;
bx = (lAlphaX + (bi * ((lOmegaX - lAlphaX)/steps))) + (width/2);
by = f(bx - (width)/2) + (height/2);
if(bi > steps){
	gameOver = true;
}
}
/*
*set the hSpeed of the box by height of the box
*/
public void setSpeed(int delta){
timeSinceD += delta;
hSpeed = (c / (1 - ((double)by / (double)height) ));
dx = (int)( (steps/(lOmegaX - lAlphaX)) * hSpeed * timeSinceD);
carryTime = - dx * (lOmegaX - lAlphaX) * (1/(hSpeed * steps));

	timeSinceD -= dx * (lOmegaX - lAlphaX) * (1/(hSpeed *steps));

}


public static void main(String[] argv){
	Lines lines = new Lines();
	lines.alpha();
	lines.go();
}

}
