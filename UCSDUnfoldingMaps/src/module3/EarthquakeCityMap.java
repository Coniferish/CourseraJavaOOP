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
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // List to populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
		for (PointFeature eq : earthquakes){
			markers.add(createMarker(eq));
		}
	    
	    map.addMarkers(markers);
	}
		
	private SimplePointMarker createMarker(PointFeature feature)
	{  		
		// Create a new SimplePointMarker at the location given by the PointFeature
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation(), feature.getProperties());
		
		Object magObj = feature.getProperty("magnitude");
		float mag = Float.parseFloat(magObj.toString());
	
	    int yellow = color(255, 205, 0);
		int orange = color(255, 120, 40);
		int red = color(255, 55, 110);
	    
		marker.setRadius(mag*=2.0);
		marker.setStrokeWeight(0);
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

	private void addKey() 
	{	
		fill(color(255, 204, 0)); 
		noStroke();
		rect(50, 50, 100, 500, 20); 

		fill(0, 0, 0);
		textSize(15);
		text("Legend", 75, 75, 100, 50);

	
	}
}
