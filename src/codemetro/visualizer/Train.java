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
	// Instant fields and constants
	float x;
	float y;
	Location location;
	SimplePointMarker marker;
	List<Location> loc = new ArrayList<Location>();
	List<Feature> loF;
	MultiFeature mf;
	int waypointNum = 0;
	ShapeFeature sf;
	int indexWayPoint;
	
	/**
	 * Constructor
	 * @param X x coordinate
	 * @param Y y coordinate
	 */
	public Train(float X, float Y) {
		this.x = X;
		this.y = Y;
		marker = new SimplePointMarker(new Location(x, y));
	}
	
	/**
	 * Constructor
	 * @param loc contains x and y coordinates
	 */
	public Train(Location loc) {
		this.x = loc.x;
		this.y = loc.y;
		marker = new SimplePointMarker(loc);
	}
	
	public Train(ShapeFeature feat){
		this.sf = feat;
		this.x = feat.getLocations().get(0).x;
		this.y = feat.getLocations().get(0).y;
		marker = new SimplePointMarker(new Location(x, y));
		loc = feat.locations;
	}
	
	public void setWayPoints(ShapeFeature sF){
		loc = sF.locations;
	}
	
	/**
	 * Set way points based on the list of locations
	 * @param feat list of locations
	 */
	public void setWayPoints(Feature feat) {
		ShapeFeature sF = (ShapeFeature) feat;
		for (int i = 0; i < sF.getLocations().size(); i++) {
			loc.add(sF.getLocations().get(i));
		}
	}
	/**
	 * @return List<Location> list of locations
	 */
	public List<Location> getLocations() {
		return loc;
	}
	
	/**
	 * @param i index of a way point
	 */ 
	public void setIndexWayPoint(int i) {
		this.indexWayPoint = i;
	}

	/**
	 * @return int index of a waypoint
	 */
	public int getIndexWayPoint() {
		return indexWayPoint;
	}
	/**
	 * @return Marker an interface to be drawn on to map
	 */
	public Marker getMarker() {
		return marker;
	}
	/**
	 * @param X x coodrinate
	 */
	public void setX(float X) {
		this.x = X;
	}
	/**
	 * @param Y y coordinate
	 */
	public void setY(float Y) {
		this.y = Y;
	}
	
	public Location getLocation() {
		return new Location(x, y);
	}
	
	/**
	 * @return float x coordinate
	 */
	public float getX() {
		return x;
	}
	/**
	 * @return float y coordinate
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * @return String "x"
	 */
	public String getXStr() {
		return "x";
	}
	/**
	 * @return String "y"
	 */
	public String getYStr() {
		return "y";
	}
	/**
	 * @return SimplePointMaker marker to be drawn to map
	 */
	public SimplePointMarker getTrain() {
		return marker;
	}
	
	public void setMultiFeat(MultiFeature mf) {
		this.mf = mf;
	}
	
	public MultiFeature getwayPoint() {
		return mf;
	}
	
	public void setloF(List<Feature> loF) {
		this.loF = loF;
	}
	
	public List<Feature> getloF() {
		return loF;
	}
	
	/**
	 * @param map coordinates
	 */
	public synchronized int moveTrain(MapPlot map){
		if (waypointNum == loc.size() - 1){ //reset back to beginning of the line if train is at the end
			waypointNum = 0;
			x = loc.get(waypointNum).x;
			y = loc.get(waypointNum).y;
		}
		
		float epsilon = 0.1f;
		if (Math.abs(x - loc.get(waypointNum).x) > epsilon || Math.abs(y - loc.get(waypointNum).y) > epsilon) { //bug fix...sometimes y or x axis isn't animated...just pop it back onto the line
			//System.out.println("ohoh!, loc has " + loc.get(waypointNum) + "train xy is (" + x + ", " + y + ")");
			x = loc.get(waypointNum).x;
			y = loc.get(waypointNum).y;
			return -1;
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
		return 1;
	}
}
