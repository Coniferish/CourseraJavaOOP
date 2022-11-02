package module1;

import processing.core.*;

public class Sun extends PApplet {

    PImage img;

    public static void main(String[] args) {
		Sun sun = new Sun();
		String[] processingArgs = { "Sun" };
		PApplet.runSketch(processingArgs, sun);
	  }


    public void setup() {
        size(320,400);
        img = loadImage("https://i.pinimg.com/736x/1a/18/48/1a1848560baf646057862c44b4c7bb12--hot-firefighters-hot-firemen.jpg");
    }

    public void draw() {
        image(img, 0, 0);
        fill(255,204,229);
        ellipse(50,50,50,50);
    }
}
