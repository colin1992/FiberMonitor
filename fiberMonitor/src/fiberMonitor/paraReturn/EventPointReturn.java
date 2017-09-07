package fiberMonitor.paraReturn;

import java.util.LinkedHashMap;

/*事件点参数类*/
public class EventPointReturn {
	private int ednPos=0;
	@SuppressWarnings("rawtypes")
	private LinkedHashMap[] eventPointMap;
	public void setEndPos(int endpos){
		this.ednPos=endpos;
	}
	
	@SuppressWarnings("rawtypes")
	public void setEventPoint(LinkedHashMap[] eventP){
		this.eventPointMap=eventP;
	}
	
	
	public int getEndPos(){
		return this.ednPos;
	}
	@SuppressWarnings("rawtypes")
	public LinkedHashMap[]  getEventPoint(){
		return this.eventPointMap;
	}

}
