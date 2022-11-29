package module4;

import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;
// import java.awt.Color;

/** Implements a visual marker for earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 *
 */
public abstract class EarthquakeMarker extends SimplePointMarker
{
	
	// Did the earthquake occur on land?  This will be set by the subclasses.
	protected boolean isOnLand;	
	
	/** Greater than or equal to this threshold is a moderate earthquake */
	public static final float THRESHOLD_MODERATE = 5;
	public static final float THRESHOLD_LIGHT = 4;

	public static final float THRESHOLD_INTERMEDIATE = 70;
	public static final float THRESHOLD_DEEP = 300;

	// ADD constants for colors
	public static final int[] RED = {255,0,0};
	public static final int[] ORANGE = {255,160,122};
	public static final int[] YELLOW = {255,255,153};
	// static final Color YELLOW = new Color(255,255,153); 
	// The line above doesn't work because pg.fill() is expecting integers, not a color obj
	// It can work if you use the color.getRed (etc.) methods and import java.awt.Color, but that seems unnecessary...
	
	// abstract method implemented in derived classes
	public abstract void drawEarthquake(PGraphics pg, float x, float y);
		
	
	// constructor
	public EarthquakeMarker (PointFeature feature) 
	{
		super(feature.getLocation());
		// Add a radius property and then set the properties
		java.util.HashMap<String, Object> properties = feature.getProperties();
		float magnitude = Float.parseFloat(properties.get("magnitude").toString());
		properties.put("radius", 2*magnitude );
		setProperties(properties);
		this.radius = 1.75f*getMagnitude();
	}
	

	// calls abstract method drawEarthquake and then checks age and draws X if needed
	public void draw(PGraphics pg, float x, float y) {
		// save previous styling
		pg.pushStyle();
			
		// determine color of marker from depth
		colorDetermine(pg);
		
		// call abstract method implemented in child class to draw marker shape
		drawEarthquake(pg, x, y);
		
		// draw X over marker if within past day
		if (isPastDay()) {
			drawX(pg, x, y);
		}
		
		// reset to previous styling
		pg.popStyle();
		
	}


	private void drawX(PGraphics pg, float x, float y){
		float r = this.getRadius();
		pg.line(x-r, y-r, x+r, y+r);
		pg.line(x-r, y+r, x+r, y-r);
	}
	
	
	private void colorDetermine(PGraphics pg) {
		float depth = this.getDepth();
		if (depth >= THRESHOLD_DEEP) {
			pg.fill(RED[0], RED[1], RED[2]);
		} else if (depth >= THRESHOLD_INTERMEDIATE) {
			pg.fill(ORANGE[0], ORANGE[1], ORANGE[2]);
		} else {
			pg.fill(YELLOW[0], YELLOW[1], YELLOW[2]);
		}
	}
	
	
	/*
	 * getters for earthquake properties
	 */
	
	public float getMagnitude() {
		return Float.parseFloat(getProperty("magnitude").toString());
	}
	
	public float getDepth() {
		return Float.parseFloat(getProperty("depth").toString());	
	}
	
	public String getTitle() {
		return (String) getProperty("title");	
		
	}
	
	public float getRadius() {
		return Float.parseFloat(getProperty("radius").toString());
	}
	
	public boolean isOnLand()
	{
		return isOnLand;
	}

	public Boolean isPastDay() {
		Object age = getProperty("age");
		if (age.toString().equals("Past Day")){
			return true;
		}
		return false;
	}
	
	
}
