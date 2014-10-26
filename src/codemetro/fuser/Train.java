package codemetro.fuser;

public class Train {
	public Train(Object objectType){
		this.objectType = objectType;
	}
	public Object getType(){
		return objectType;
	}
	
	private Object objectType;
}
