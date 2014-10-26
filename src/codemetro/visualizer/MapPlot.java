package codemetro.visualizer;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.*;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.*; //Map Styles
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.marker.*;

import java.util.*;

public class MapPlot extends PApplet{
	
	UnfoldingMap metro;
	
	public void setup() {
		size(1280, 720); //setup size of applet
		metro = new UnfoldingMap(this, new Google.GoogleSimplifiedProvider()); //TODO using GoogleSimplified2Provider for now...find a better map style
		
		// Add mouse and keyboard interactions
        MapUtils.createDefaultEventDispatcher(this, metro);
		
		//Show particular location in world map
		Location vancouver = new Location(49.27f,-123.1f);
		int zoomLv = 12; //the level of zoom for the map (0 = world view, bigger number = more zoom)
		metro.zoomAndPanTo(zoomLv, vancouver);
		
		List<Feature> loF = GeoJSONReader.loadData(this, "mockMarker.json");
		List<Marker> loM = MapUtils.createSimpleMarkers(loF);
		SimplePointMarker simpleMarker;
		for (int index = 0; index < loM.size(); index++) {
			simpleMarker = new SimplePointMarker(loM.get(index).getLocation());
			simpleMarker.setStrokeWeight(3);
			simpleMarker.setStrokeColor(color(0,0,0));
			simpleMarker.setColor(color(255,255,255));
			metro.addMarker(simpleMarker);
			System.out.println(loM.get(index).getLocation());
		}
	}
	
	public void draw() {
		metro.draw();
	}
}
