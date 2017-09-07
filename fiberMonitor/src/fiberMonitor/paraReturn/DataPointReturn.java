package fiberMonitor.paraReturn;
/*数据点参数类*/
public class DataPointReturn {

	 private int endPos;
	 private float[] dataPoint=null;
	 private long curve_id=0;
	 /*设置结束位置*/
	 public void setEndPos(int pos){
		 this.endPos=pos;
	 }
	 /*存放数据点*/
	 public void setDataPoint(float[] dataPo){
		 this.dataPoint=dataPo;
	 }
	 public void setCurveId(long curve_id){
		 this.curve_id=curve_id;
	 }
	  /*获取结束位置*/
	 public int getEndPos(){
		  return this.endPos;
	 }
	 /*获取事件点数据*/
	 public float[] getDataPoint(){
		return this.dataPoint;
	 }
	 public long getCurveId(){
		return this.curve_id;
	 }
}
