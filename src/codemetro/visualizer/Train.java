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
	float x;
	float y;
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
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
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
		//System.out.println("waypoingNum is " + waypointNum);
		//System.out.println("loc.size is " + loc.size());
		if (waypointNum == 0) {
			Ani.setDefaultEasing(Ani.QUAD_IN);
		} else if (waypointNum == loc.size() - 2) {
			Ani.setDefaultEasing(Ani.QUAD_OUT);
		} else {
			Ani.setDefaultEasing(Ani.LINEAR);
		}
		waypointNum++;
		
		Ani.to(this, 4f, "x", loc.get(waypointNum).x);
		Ani.to(this, 4f, "y", loc.get(waypointNum).y);
	}
}
