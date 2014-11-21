package codemetro.visualizer.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.*;
import processing.core.PApplet;

import org.junit.Test;

import codemetro.visualizer.MapPlot;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Google;

public class MapPlotTest {
	MapPlot plotter = new MapPlot();
	PApplet p = new PApplet();
	UnfoldingMap uMap = new UnfoldingMap(p, new Google.GoogleSimplifiedProvider());
	
	@Test
	public void testRun() {
		PApplet.main("codemetro.visualizer.MapPlot");
		//PApplet.main(new String[] { "codemetro.visualizer.MapPlot" });
		//MapPlot m = new MapPlot();
		//m.setup();
	}
	
	/*
	@Test
	public void testGetLocationFromJSON() {
		List<Feature> loF = new ArrayList<Feature>();
		loF = GeoJSONReader.loadData(p, "./test/mockMarker.json");
		List<Marker> loM = plotter.plotPoints(uMap, loF);
		//System.out.println(loM.get(1).getLocation());
		float epsilon = 0.0001f;
		if (Math.abs(3.0f - loM.get(1).getLocation().x) < epsilon){ //test x value
			assertTrue(true);
		} else
			assertFalse(true);
		if (Math.abs(1.0f - loM.get(1).getLocation().y) < epsilon){ //test x value
			assertTrue(true);
		} else
			assertFalse(true);
		// epsilon is error value.
		// ProbABLY around 0.000001 or something
	}
	
	@Test
	public void testGetLinesFromJSON() {
		List<Feature> loF = new ArrayList<Feature>();
		loF = GeoJSONReader.loadData(p, "./test/mockLine.json");
		
		MultiFeature mF = new MultiFeature();
		mF = plotter.plotLines(uMap, loF);
		
		ShapeFeature sF1 = (ShapeFeature) mF.getFeatures().get(0);
		//ShapeFeature sF2 = (ShapeFeature) mF.getFeatures().get(1);
		
		System.out.println(mF.getFeatures().get(1));
		float epsilon = 0.0001f;
		if (Math.abs(0.0f - sF1.getLocations().get(0).x) < epsilon){ //test x value at index 0
			assertTrue(true);
		} else
			assertFalse(true);
		if (Math.abs(0.0f - sF1.getLocations().get(0).y) < epsilon){ //test y value at index 0
			assertTrue(true);
		} else
			assertFalse(true);
		if (Math.abs(2.0f - sF1.getLocations().get(1).x) < epsilon){ //test x value at index 1
			assertTrue(true);
		} else
			assertFalse(true);
		if (Math.abs(2.0f - sF1.getLocations().get(1).y) < epsilon){ //test y value at index 1
			assertTrue(true);
		} else
			assertFalse(true);
	}
	
	@Test
	public void testCheckMetroStyleLine() { //tests if a line that is not straight or a 45 degree slope will be changed to it (as per our line specifications)
		//TODO WARNING: THIS IS A REALLY BUGGY FEATURE RIGHT NOW. ONLY CERTAIN LINES WILL PRODUCE LINES OF OUR SPECIFICATIONS
		List<Feature> loF = new ArrayList<Feature>();
		loF = GeoJSONReader.loadData(p, "./test/mockLine.json");
		
		List<Location> loL = new ArrayList<Location>();
		
		for (int index = 0; index < loF.size(); index++) { //for each polyline set
			ShapeFeature linePoint = (ShapeFeature) loF.get(index);
			//System.out.println(loL.get(index).x);
			plotter.lineToSubwayLine(linePoint); //converts lines to Subway-style lines
			
			for (int i = 0; i < linePoint.getLocations().size(); i++) {
				loL.add(linePoint.getLocations().get(i));
			}
		}
		
		float epsilon = 0.0001f;
		if (Math.abs(1.0f - loL.get(3).x) < epsilon){ //test x value at index 3
			assertTrue(true);
		} else
			assertFalse(true);
		if (Math.abs(0.0f - loL.get(3).y) < epsilon){ //test y value at index 3
			assertTrue(true);
		} else
			assertFalse(true);
		if (Math.abs(2.0f - loL.get(4).x) < epsilon){ //test x value at index 4, if lineToSubwayLine had done nothing, index 4 would be the coordinates of index 5 (3.0)
			assertTrue(true);
		} else
			assertFalse(true);
		if (Math.abs(0.0f - loL.get(4).y) < epsilon){ //test y value at index 4, if lineToSubwayLine had done nothing, index 4 would be the coordinates of index 5 (1.0)
			assertTrue(true);
		} else
			assertFalse(true);
		if (Math.abs(3.0f - loL.get(5).x) < epsilon){ //test x value at index 5
			assertTrue(true);
		} else
			assertFalse(true);
		if (Math.abs(1.0f - loL.get(5).y) < epsilon){ //test y value at index 5
			assertTrue(true);
		} else
			assertFalse(true);
	}
	*/
}
