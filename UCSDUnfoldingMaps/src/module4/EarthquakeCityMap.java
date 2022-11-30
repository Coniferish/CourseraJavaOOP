package module4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {
	
	// We will use member variables, instead of local variables, to store the data
	// that the setUp and draw methods will need to access (as well as other methods)
	// You will use many of these variables, but the only one you should need to add
	// code to modify is countryQuakes, where you will store the number of earthquakes
	// per country.
	
	// You can ignore this.  It's to get rid of eclipse warnings
	private static final long serialVersionUID = 1L;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";

	private UnfoldingMap map;
	private List<Marker> cityMarkers;
	private List<Marker> quakeMarkers;
	private List<Marker> countryMarkers;
	private HashMap<String,Integer> quakeCount;
	
	public static void main(String[] args) {
		EarthquakeCityMap earthquakeCityMap = new EarthquakeCityMap();
		String[] processingArgs = { "EarthquakeCityMap" };
		PApplet.runSketch(processingArgs, earthquakeCityMap);
	  }

	  
	public void setup() {		
		size(900, 700, OPENGL);
		map = new UnfoldingMap(this, 200, 50, 650, 600, new Google.GoogleMapProvider());
		// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
		earthquakesURL = "2.5_week.atom";
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// FOR TESTING: Set earthquakesURL to be one of the testing files by uncommenting
		// one of the lines below.  This will work whether you are online or offline
		//earthquakesURL = "test1.atom";
		//earthquakesURL = "test2.atom";
		
		// WHEN TAKING THIS QUIZ: Uncomment the next line
		//earthquakesURL = "quiz1.atom";
		
		// (2) read in earthquake data and geometric properties
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for(Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}

		quakeCount = new HashMap<String,Integer>();
	    
		//     STEP 3: read in earthquake RSS feed
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    quakeMarkers = new ArrayList<Marker>();
	    
	    for(PointFeature feature : earthquakes) {
		  if(isLand(feature)) {
		    quakeMarkers.add(new LandQuakeMarker(feature));
		  }
		  else {
		    quakeMarkers.add(new OceanQuakeMarker(feature));
		  }
	    }

	    // could be used for debugging
	    printQuakes();
	 		
	    // (3) Add markers to map
	    //     NOTE: Country markers are not added to the map.  They are used
	    //           for their geometric properties
	    map.addMarkers(quakeMarkers);
	    map.addMarkers(cityMarkers);
	    
	}  // End setup
	
	
	public void draw() {
		background(0);
		map.draw();
		addKey();
	}
	
	private void addKey() {	
		// Processing's graphics methods
		fill(255, 250, 240);
		rect(25, 50, 150, 300);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Earthquake Key", 50, 75);
		
		fill(color(255, 0, 0));
		ellipse(50, 125, 15, 15);
		fill(color(255,160,122));
		ellipse(50, 150, 15, 15);
		fill(color(255,255,153));
		ellipse(50, 175, 15, 15);

		fill(color(138,43,226));
		int x = 50;
		int y = 220;
		int TRI_SIZE = 7;
		triangle(x, y-TRI_SIZE, x-TRI_SIZE, y+TRI_SIZE, x+TRI_SIZE, y+TRI_SIZE);

		fill(255, 250, 240);
		ellipse(50, 270, 15, 15);
		// rect(75, 50, 50, 100);
		// rect(x-r/2, y-r/2, r, r);
		int r = 7;
		rect(50-r, 245-r, 14, 14);
		int lineX = 50;
		int lineY = 315;
		line(lineX-7, lineY-7, lineX+7, lineY+7);
		line(lineX-7, lineY+7, lineX+7, lineY-7);
		// pg.line(x-r, y-r, x+r, y+r);
		// pg.line(x-r, y+r, x+r, y-r);

		fill(0);
		text("5.0+ Magnitude", 75, 125);
		text("4.0+ Magnitude", 75, 150);
		text("<4.0 Magnitude", 75, 175);
		text("City Marker", 75, 220);
		text("Ocean", 75, 245);
		text("Land", 75, 270);
		text("Within 24 hrs", 75, 315);
	}

	
	// Checks whether this quake occurred on land.  If it did, it sets the 
	// "country" property of its PointFeature to the country where it occurred
	// and returns true.
	private boolean isLand(PointFeature earthquake) {
		HashMap<String, Object> properties = earthquake.getProperties();
	
		for (Marker country : countryMarkers) {
			String name = (String)country.getProperty("name");
			if (isInCountry(earthquake, country)){
				properties.put("country", name);
				earthquake.setProperties(properties);
				countQuake(earthquake);
				return true;
			}
		}
		properties.put("country", "ocean");
		earthquake.setProperties(properties);
		countQuake(earthquake);
		return false;
	}

	private void countQuake(PointFeature earthquake) {
		String country = (String)earthquake.getProperty("country");

		if (quakeCount.containsKey(country)) {
			quakeCount.put(country, quakeCount.get(country) + 1);
		}
		else {
			quakeCount.put(country, 1);
		}
	}
	
	private void printQuakes() 
	{
		quakeCount.forEach(
			(key, value)
				-> System.out.println(key + ": " + value)
		);

		// Here is some code you will find useful:
		// 
		//  * To get the name of a country from a country marker in variable cm, use:
		//     String name = (String)cm.getProperty("name");
		//  * If you have a reference to a Marker m, but you know the underlying object
		//    is an EarthquakeMarker, you can cast it:
		//       EarthquakeMarker em = (EarthquakeMarker)m;
		//    Then em can access the methods of the EarthquakeMarker class 
		//       (e.g. isOnLand)
		//  * If you know your Marker, m, is a LandQuakeMarker, then it has a "country" 
		//      property set.  You can get the country with:
		//        String country = (String)m.getProperty("country");
		
	}
	
	
	
	// helper method to test whether a given earthquake is in a given country
	// This will also add the country property to the properties of the earthquake 
	// feature if it's in one of the countries.
	// You should not have to modify this code
	private boolean isInCountry(PointFeature earthquake, Marker country) {
		// getting location of feature
		Location checkLoc = earthquake.getLocation();

		// some countries represented it as MultiMarker
		// looping over SimplePolygonMarkers which make them up to use isInsideByLoc
		if(country.getClass() == MultiMarker.class) {
				
			// looping over markers making up MultiMarker
			for(Marker marker : ((MultiMarker)country).getMarkers()) {
					
				// checking if inside
				if(((AbstractShapeMarker)marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));
						
					// return if is inside one
					return true;
				}
			}
		}
			
		// check if inside country represented by SimplePolygonMarker
		else if(((AbstractShapeMarker)country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));
			
			return true;
		}
		return false;
	}

}
