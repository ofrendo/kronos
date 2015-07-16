package kronos.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import kronos.util.Log;


@XmlRootElement
public class SAData {

	private double em1;
	
	private double em2;
	
	private double a1;
	
	private double a2;
	
    private double b1;
	
	private double b2;
	
	private long ts_start;
	
	private long ts_stop;
	
	private String overallStatus;
	
	public SAData(){
		
	}
	
	public void print (){
		Log.info("#####################");
		Log.info("Status: "+overallStatus);
		Log.info("Start: " + ts_start);
		Log.info("Stop: " + ts_stop);
	}

	public double getEm1() {
		return em1;
	}

	public void setEm1(double em1) {
		this.em1 = em1;
	}

	public double getEm2() {
		return em2;
	}

	public void setEm2(double em2) {
		this.em2 = em2;
	}

	public double getB1() {
		return b1;
	}

	public void setB1(double b1) {
		this.b1 = b1;
	}

	public double getB2() {
		return b2;
	}

	public void setB2(double b2) {
		this.b2 = b2;
	}


	public String getOverallStatus() {
		return overallStatus;
	}

	public void setOverallStatus(String overallStatus) {
		this.overallStatus = overallStatus;
	}

	public double getA1() {
		return a1;
	}

	public void setA1(double a1) {
		this.a1 = a1;
	}

	public double getA2() {
		return a2;
	}

	public void setA2(double a2) {
		this.a2 = a2;
	}

	public void setTs_start(long ts_start) {
		this.ts_start = ts_start;
	}

	public void setTs_stop(long ts_stop) {
		this.ts_stop = ts_stop;
	}
}
