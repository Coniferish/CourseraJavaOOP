package module3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import processing.core.PApplet;

public class LifeExpectancy extends PApplet {
    UnfoldingMap worldMap;
    Map<String, Float> lifeExpectancyByCountry;
    List<Feature> countries;
    List<Marker> countryMarkers;

    // helper method because it's private?
    private Map<String, Float> loadExpectancyFromCSV (String filename){
        Map<String, Float> LifeExpectancy = new HashMap<String, Float>();
        String[] rows = loadStrings(filename);
        for (String row : rows){
            String[] columns = row.split(",");
            Float expectancy = Float.parseFloat(columns[5]);
            LifeExpectancy.put(columns[4], expectancy);
        }
        return LifeExpectancy;
    }

    public void setup(){
        size(800,600);
        worldMap = new UnfoldingMap(this);
        MapUtils.createDefaultEventDispatcher(this, worldMap);

        lifeExpectancyByCountry = loadExpectancyFromCSV("data/LifeExpectancyWorldBankModule3.csv");
        countries = GeoJSONReader.loadData(this, "data/countries.geo.json");
        countryMarkers = MapUtils.createSimpleMarkers(countries);

        worldMap.addMarkers(countryMarkers);
        shadeCountries();
    }

    private void shadeCountries() {
        for (Marker marker : countryMarkers){
            String countryId = marker.getId();

            if (lifeExpectancyByCountry.containsKey(countryId)){
                float lifeExp = lifeExpectancyByCountry.get(countryId);
                int colorLevel = (int) map(lifeExp, 40, 90, 10, 255);
                marker.setColor(color(255-colorLevel, 100, colorLevel));
            } else {
                marker.setColor(color(150,150,150));
            }
        }
    }

    public void draw(){
        worldMap.draw();
    }

    public static void main(String[] args) {
		LifeExpectancy lifeExpecancy = new LifeExpectancy();
		String[] processingArgs = { "LifeExpectancy" };
		PApplet.runSketch(processingArgs, lifeExpecancy);
	  }
}
