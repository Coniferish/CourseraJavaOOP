package module1;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;

/** HelloWorld
  * An application with two maps side-by-side zoomed in on different locations.
  * Author: UC San Diego Coursera Intermediate Programming team
  * @author Your name here
  * Date: July 17, 2015
  * */
public class HelloWorld extends PApplet
{
	/** Your goal: add code to display second map, zoom in, and customize the background.
	 * Feel free to copy and use this code, adding to it, modifying it, etc.  
	 * Don't forget the import lines above. */
	public static void main(String[] args) {
		HelloWorld hw = new HelloWorld();
		String[] processingArgs = { "HelloWorld" };
		PApplet.runSketch(processingArgs, hw);
	  }

	// This is to keep eclipse from reporting a warning
	private static final long serialVersionUID = 1L;
	
	UnfoldingMap map1;
	UnfoldingMap map2;

	public void setup() {
		size(800, 600, P2D);  // Set up the Applet window to be 800x600
		                      // The OPENGL argument indicates to use the 
		                      // Processing library's 2D drawing
		                      // You'll learn more about processing in Module 3

		// This sets the background color for the Applet.  
		// Play around with these numbers and see what happens!
		this.background(230,255,252);
		
		// Select a map provider
		AbstractMapProvider provider = new Google.GoogleTerrainProvider();
		// Set a zoom level
		int zoomLevel = 10;

		
		// Create a new UnfoldingMap to be displayed in this window.  
		// The 2nd-5th arguments give the map's x, y, width and height
		// When you create your map we want you to play around with these 
		// arguments to get your second map in the right place.
		// The 6th argument specifies the map provider.  
		// There are several providers built-in.
		// Note if you are working offline you must use the MBTilesMapProvider
		map1 = new UnfoldingMap(this, 50, 50, 350, 500, provider);

		// The next line zooms in and centers the map at 
	    // 32.9 (latitude) and -117.2 (longitude)
	    map1.zoomAndPanTo(zoomLevel, new Location(32.9f, -117.2f));
		
		map2 = new UnfoldingMap(this, 405, 50, 350, 500, provider);
		map2.zoomAndPanTo(zoomLevel, new Location(42.0f, -70.2f));

		// This line makes the map interactive
		MapUtils.createDefaultEventDispatcher(this, map1);
		// MapUtils.createDefaultEventDispatcher(this, map2);

		

	}

	/** Draw the Applet window.  */
	public void draw() {
		map1.draw();
		map2.draw();
	}

	
}
