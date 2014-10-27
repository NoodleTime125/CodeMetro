package codemetro.visualizer;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.*;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.*; //Map Styles
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.marker.*;

import java.util.Timer;
import java.util.TimerTask;
import java.math.*;

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
		Location testMap = new Location(0f,0f);
		int zoomLv = 8; //the level of zoom for the map (0 = world view, bigger number = more zoom) use 12
		metro.zoomAndPanTo(zoomLv, testMap);
		
		//Plot Lines
		List<Feature> loFL = GeoJSONReader.loadData(this, "mockLine.json");
		List<Marker> loML = new ArrayList<Marker>();
		
		for (int index = 0; index < loFL.size(); index++) { //for each polyline set
			ShapeFeature linePoint = (ShapeFeature) loFL.get(index);
			
			/*
			//turn lines into subway-like paths
			int nextLocation = 1;
			for (int i = 0; i < linePoint.getLocations().size() - nextLocation; i++){ //we compare the current latlon with next latlon, so we minus nextLocation to handle array out of bound
				float currentLat = linePoint.getLocations().get(i).getLat();
				float currentLon = linePoint.getLocations().get(i).getLon();

				System.out.println(linePoint.getLocations().size()); //TODO remove
				float nextLat = linePoint.getLocations().get(i+1).getLat();
				float nextLon = linePoint.getLocations().get(i+1).getLon();
				if ((currentLat != nextLat)&&(currentLon != nextLon)){ //Lines will be diagonal, force lines to 45-degree angles
					if((Math.abs(currentLat - nextLat)) != (Math.abs(currentLon - nextLon))){ //checks if lines are not 45-degrees by nature
						System.out.println("hey, not 45 degrees!\n");
						//float tempLatDist = Math.abs(currentLat - nextLat);
						float tempLat;
						float tempLon = currentLon;
						tempLat = Math.abs(nextLon - currentLon);
						Location tempLatLon = null;
						
						System.out.println("\ntempLat " + tempLat);
						System.out.println("CLat " + currentLat + " Lon " + currentLon); //TODO remove
						System.out.println("NLat " + nextLat + " NLon " + nextLon);
						if (Math.abs(currentLat) - Math.abs(nextLat) < Math.abs(currentLon) - Math.abs(nextLon)) { //if latitude is less than longitude
							if (nextLon > currentLon) {
								tempLatLon = new Location(currentLat + tempLat, tempLon); //this is fine
								System.out.println("if else 1");
							} else {
								tempLatLon = new Location(tempLat, tempLon - nextLon); //this is fine
								System.out.println("if else 2");
							}
						} else
							if (nextLon > currentLon) {
								System.out.println("if else 3");
								tempLatLon = new Location(nextLat, Math.abs(currentLat - nextLat));
							}
							else { 
								tempLatLon = new Location(tempLat, tempLon);
								System.out.println("if else 4");
							}

						//now we insert this tempLatLon into our linePoint and recursively move all linePoints + 1
						linePoint.getLocations().add(i+1, tempLatLon);
					}
				}
				
			}*/
			
			System.out.println("Still in for loop\n");
			SimpleLinesMarker lineMarker = new SimpleLinesMarker(linePoint.getLocations());
		
			//add characteristics to lines
			lineMarker.setStrokeWeight(5);
			lineMarker.setColor(color(255,0,0)); //RED
			
			loML.add(lineMarker);
			
			metro.addMarker(lineMarker);
		}
		
		//Plot Points
		List<Feature> loF = GeoJSONReader.loadData(this, "mockMarker.json");
		List<Marker> loM = MapUtils.createSimpleMarkers(loF);
		SimplePointMarker simpleMarker;
		for (int index = 0; index < loM.size(); index++) {
			simpleMarker = new SimplePointMarker(loM.get(index).getLocation());
			simpleMarker.setStrokeWeight(3);
			simpleMarker.setStrokeColor(color(0,0,0)); //Black
			simpleMarker.setColor(color(255,255,255)); //White
			metro.addMarker(simpleMarker);
			//System.out.println(loM.get(index).getLocation());
		}
	}
	
	public void trains() {
		
	}
	
	public void draw() {
		metro.draw();
	}
}
