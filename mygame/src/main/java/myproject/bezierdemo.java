package myproject;

import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.input.*;
import static org.lwjgl.opengl.GL11.*;
import java.awt.Point;
import java.awt.Rectangle;

public class bezierdemo{
//width and height of the display area
private int width = 800;
private int height = 600;
//when space is pressed then when released and awaits command
private boolean spacePressed = false;
private boolean spaceReleased = false;
//test box size, just for testing the display
int bsize = 50;
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
double c = .5;
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
//arc length of each step in pixels
double stepP = 0;
//the amount of pixels that the box couldn't move but still needs to
double pixelDebt = 0;
//control points
Point cp0 = new Point(300,500);
Point cp1 = new Point(0,300);
Point cp2 = new Point(100,600);
Point cp3 = new Point(800,100);
//the coordinates of z
int zx = 400;
int zy = 300;
boolean up = true;
boolean down = false;
//boolean to set the box moving
boolean boxAlive = false;

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
		mort();
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
		boxAlive = true;
		
	}
	delta = dTime();
	if(boxAlive){
		moveBox(delta);
	} else{
		setBox();
	}
	moveZ(delta);
	
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
	
	for(int i = 0; i <= steps; i++){
		glVertex2f(bezier(cp0,cp1,cp2,cp3,(double)i / (double)steps).x,bezier(cp0,cp1,cp2,cp3,(double)i / (double)steps).y);
		
	}
	glEnd();
	glColor3f(0f,0f,0f);
	glBegin(GL_QUADS);
		glVertex2f(bx + bsize/2,by + bsize/2);
		glVertex2f(bx - bsize/2,by + bsize/2);
		glVertex2f(bx - bsize/2,by - bsize/2);
		glVertex2f(bx + bsize/2,by - bsize/2);
	glEnd();
	glBegin(GL_QUADS);
		glVertex2f(zx + 10/2,zy + 10/2);
		glVertex2f(zx - 10/2,zy + 10/2);
		glVertex2f(zx - 10/2,zy - 10/2);
		glVertex2f(zx + 10/2,zy - 10/2);
	glEnd();
	
}

/*
*return the current time
*/
public long now(){
	return (Sys.getTime() * 1000) / Sys.getTimerResolution();
}
/*
*set the box to its initial position
*/
public void setBox(){
bx = bezier(cp0,cp1,cp2,cp3,(double)0 / (double)steps).x;
by = bezier(cp0,cp1,cp2,cp3,(double)0 / (double)steps).y;
}
/*
*move the box
*/
public void moveBox(int delta){
setSpeed(delta);
//bi += dx;
bx = bezier(cp0,cp1,cp2,cp3,(double)bi / (double)steps).x;
by = bezier(cp0,cp1,cp2,cp3,(double)bi / (double)steps).y;
if(bi > steps){
	gameOver = true;
}
}
/*
*set the hSpeed of the box by height of the box
*/
public void setSpeed(int delta){
/*
timeSinceD += delta;
stepP = StepLength();
System.out.println(stepP);
hSpeed = (c / (1 - ((double)by / (double)height) ));
dx = (int)( (1.0/(double)stepP) * hSpeed * timeSinceD);
//carryTime = - dx * (lOmegaX - lAlphaX) * (1/(hSpeed * steps));

	timeSinceD -= dx * stepP * (1.0/((double) hSpeed));
*/
timeSinceD += delta;
//hSpeed = (c / (1 - ((double)by / (double)height) ));
hSpeed = c * ((double)by / (double)height);
stepP = StepLength();
pixelDebt += hSpeed * timeSinceD;
while(pixelDebt > stepP){
	pixelDebt -= stepP;
	bi++;
	stepP = StepLength();
}
timeSinceD = 0;
}
/*
*this calculates the amount pixels in the next step
*/
public double StepLength(){
return Math.pow((
Math.pow(bx - bezier(cp0,cp1,cp2,cp3,(double)(bi + 1.0) / (double)steps).x,2)
+ Math.pow(by - bezier(cp0,cp1,cp2,cp3,(double)(bi + 1.0) / (double)steps).y,2)
),.5);
}


/*
*this calculates the point on the bezier curve for the control points and t-value
*/
public Point bezier(Point a, Point b, Point c, Point d, double t){
        return new Point(
        (int)(a.x * Math.pow((1-t),3) + b.x * 3 * Math.pow((1-t),2) * t + c.x * 3 * Math.pow((1-t),1) * Math.pow(t,2) + d.x * Math.pow(t,3)),
        (int)(a.y * Math.pow((1-t),3) + b.y * 3 * Math.pow((1-t),2) * t + c.y * 3 * Math.pow((1-t),1) * Math.pow(t,2) + d.y * Math.pow(t,3)));
}
public void mort(){
if(new Rectangle(bx - bsize/2,by - bsize/2,bsize,bsize).intersects(new Rectangle(zx - 5,zy - 5,10,10))){
	boxAlive = false;
	gameOver = true;
}
}


/*
*moves the Z block
*/
public void moveZ(int delta){
	int zSpeed = (int)(.1 * delta);
	if(up){
		zy += zSpeed;
		if(zy > 400){up = false; down = true;}
	} else if(down){
		zy -= zSpeed;
		if(zy < 300){down = false; up = true;}
	}
	
}
public static void main(String[] argv){
	bezierdemo demo = new bezierdemo();
	demo.alpha();
	demo.go();
}

}
