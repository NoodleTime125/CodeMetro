package codemetro.visualizer;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.MultiFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.looksgood.ani.Ani;
import de.looksgood.ani.easing.Easing;

public class Train {
	float x;
	float y;
	Location location;
	int indexLocation;
	int indexWayPoint;
	SimplePointMarker marker;
	List<Location> loc = new ArrayList<Location>();
	int waypointNum = 0;
	MultiFeature wayPoint;

	public Train(float X, float Y) {
		this.x = X;
		this.y = Y;
		marker = new SimplePointMarker(new Location(x, y));
	}
	
	public Train (Location loc) {
		this.x = loc.x;
		this.y = loc.y;
		marker = new SimplePointMarker(loc);
	}
	
	public void setWayPoints(Feature feat) {
		ShapeFeature sF = (ShapeFeature) feat;
		for (int i = 0; i < sF.getLocations().size(); i++) {
			loc.add(sF.getLocations().get(i));
		}
	}

	public List<Location> getLocations() {
		return loc;
	}
	
	public void setIndexLocation(int i) {
		this.indexLocation = i;
	}
	
	public void setIndexWayPoint(int i) {
		this.indexWayPoint = i;
	}
	
	public int getIndexLocation() {
		return indexLocation;
	}
	
	public int getIndexWayPoint() {
		return indexWayPoint;
	}
	
	public Marker getMarker() {
		return marker;
	}
	
	public void setX(float X) {
		this.x = X;
	}
	
	public void setY(float Y) {
		this.y = Y;
	}
	
	public Location getLocation() {
		location.set(x, y);
		return location;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public Location getLoc() {
		Location loc = new Location(x, y);
		return loc;
	}
	
	public String getXStr() {
		return "x";
	}
	
	public String getYStr() {
		return "y";
	}
	
	public SimplePointMarker getTrain() {
		return marker;
	}
	
	public void moveTrain(MapPlot map){
		if (waypointNum == loc.size() - 1){
			waypointNum = 0;
			x = loc.get(waypointNum).x;
			y = loc.get(waypointNum).y;
		}
		
		if (x != loc.get(waypointNum).x && y != loc.get(waypointNum).y) { //bug fix...sometimes y or x axis isn't animated...just pop it back onto the line
			x = loc.get(waypointNum).x;
			y = loc.get(waypointNum).y;
		}
		
		Easing currentEasing;
		if (loc.size() == 2){ //two points in the line only
			currentEasing = Ani.SINE_IN_OUT;
		} else if (waypointNum == 0) { //the beginning of the line
			currentEasing = Ani.SINE_IN;
		} else if (waypointNum == loc.size() - 2) { //the last waypoint to the final point in the line
			currentEasing = Ani.SINE_OUT;
		} else { //anything in between
			currentEasing = Ani.LINEAR;
		}
		waypointNum++;
		
		Ani.to(this, 4f, "x", loc.get(waypointNum).x, currentEasing);
		Ani.to(this, 4f, "y", loc.get(waypointNum).y, currentEasing);
		
	}
}
