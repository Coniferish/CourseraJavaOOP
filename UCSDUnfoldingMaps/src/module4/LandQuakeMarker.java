package module4;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;

/** Implements a visual marker for land earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 *
 */
public class LandQuakeMarker extends EarthquakeMarker {
	
	
	public LandQuakeMarker(PointFeature quake) {
		
		// calling EarthquakeMarker constructor
		super(quake);
		
		// setting field in earthquake marker
		isOnLand = true;
	}


	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {
		// Draw a centered circle for land quakes
		// Simply draw a centered circle, don't apply color

		float r = this.getRadius();
		pg.ellipse(x, y, r, r);

		// if (isPastDay()) {
		// 	drawX(pg, x, y);
		// }
	}


	// Get the country the earthquake is in
	public String getCountry() {
		return (String) getProperty("country");
	}

		
}