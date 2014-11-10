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

public class Train {
	// Instant fields and constants
	float x;
	float y;
	int indexLocation;
	int indexWayPoint;
	SimplePointMarker marker;
	List<Location> loc = new ArrayList<Location>();
	int waypointNum = 0;
	MultiFeature wayPoint;
	
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
	 * @param i index of a location
	 */
	public void setIndexLocation(int i) {
		this.indexLocation = i;
	}
	/**
	 * @param i index of a way point
	 */ 
	public void setIndexWayPoint(int i) {
		this.indexWayPoint = i;
	}
	/**
	 * @return int index of a location
	 */
	public int getIndexLocation() {
		return indexLocation;
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
	/**
	 * @param map coordinates
	 */
	public void moveTrain(MapPlot map){
		if (waypointNum == loc.size() - 1){
			waypointNum = 0;
			x = loc.get(waypointNum).x;
			y = loc.get(waypointNum).y;
		}
		//System.out.println("waypoingNum is " + waypointNum);
		//System.out.println("loc.size is " + loc.size());
		
		if (x != loc.get(waypointNum).x && y != loc.get(waypointNum).y) { //bug fix...sometimes y or x axis isn't animated...just pop it back onto the line
			x = loc.get(waypointNum).x;
			y = loc.get(waypointNum).y;
		}
		
		if (loc.size() == 2){ 						//two points in the line only
			Ani.setDefaultEasing(Ani.QUAD_IN_OUT);
		} else if (waypointNum == 0) { 				//the beginning of the line
			Ani.setDefaultEasing(Ani.QUAD_IN);
		} else if (waypointNum == loc.size() - 2) { //the last waypoint to the final point in the line
			Ani.setDefaultEasing(Ani.QUAD_OUT);
		} else { 									//anything in between
			Ani.setDefaultEasing(Ani.LINEAR);
		}
		waypointNum++;
		
		Ani.to(this, 4f, "x", loc.get(waypointNum).x);
		Ani.to(this, 4f, "y", loc.get(waypointNum).y);
	}
}
