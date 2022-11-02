package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author John Jennings
 * Date: Oct 26, 2022
 * */
public class EarthquakeCityMap extends PApplet {

	private static final long serialVersionUID = 1L;
	public static final float THRESHOLD_MODERATE = 5;
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	private UnfoldingMap map;
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	public static void main(String[] args) {
		EarthquakeCityMap earthquakeCityMap = new EarthquakeCityMap();
		String[] processingArgs = { "EarthquakeCityMap" };
		PApplet.runSketch(processingArgs, earthquakeCityMap);
	  }

	public void setup() {
		size(950, 600, OPENGL);
		
		map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
		// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
		//earthquakesURL = "2.5_week.atom";
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List to populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
		for (PointFeature eq : earthquakes){
			// createMarker(eq);
			// markers.add(new SimplePointMarker(eq.getLocation(), eq.getProperties()));
			markers.add(createMarker(eq));
		}
	    
	    map.addMarkers(markers);
	}
		
	private SimplePointMarker createMarker(PointFeature feature)
	{  
		// To print all of the features in a PointFeature (so you can see what they are)
		// uncomment the line below.  Note this will only print if you call createMarker 
		// from setup
		//System.out.println(feature.getProperties());
		
		// Create a new SimplePointMarker at the location given by the PointFeature
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation(), feature.getProperties());
		
		Object magObj = feature.getProperty("magnitude");
		float mag = Float.parseFloat(magObj.toString());
	
	    int yellow = color(255, 255, 82);
		int orange = color(255, 190, 92);
		int red = color(255, 153, 102);
		
	    // Don't forget about the constants THRESHOLD_MODERATE and 
	    // THRESHOLD_LIGHT, which are declared above.
	    // Rather than comparing the magnitude to a number directly, compare 
	    // the magnitude to these variables (and change their value in the code 
	    // above if you want to change what you mean by "moderate" and "light")
	    
		marker.setRadius(mag*=1.5);
		if (mag > THRESHOLD_MODERATE){
			marker.setColor(red);
		} else if (mag > THRESHOLD_LIGHT){
			marker.setColor(orange);
		} else {
			marker.setColor(yellow);
		}
		
	    return marker;
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
	
	}
}
