package quant.core;

/*
 * Part of the planning flow for events. Ideas -> Plans -> Events
 * Not to be implemented yet.
 */
public class Plan extends LifeObjective {
	
	private String[] pros;
	private String[] cons;
	private String implementation;
	private String approxTime;
	
	/* returns the pros value of the object */
	public String[] getPros() {
		return this.pros;
	}
	
	/* sets the pros value of the object */
	public void setPros(String[] inPros) {
		for(int i=0;i<inPros.length;i++)
			pros[i] = inPros [i];
	}
	
	/* returns the cons value of the object */
	public String[] getCons() {
		return this.cons;
	}
	
	/* sets the cons value of the object */
	public void setCons(String[] inCons) {
		for(int i=0;i<inCons.length;i++)
			cons[i] = inCons [i];
	}
	
	/* returns the implementation value of the object */
	public String getImplementation() {
		return this.implementation;
	}
	
	/* sets the implementation value of the object */
	public void setImplementation(String implementation) {
		this.implementation = implementation;
	}
	
	/* returns the approxTime value of the object */
	public String getApproxTime() {
		return this.approxTime;
	}
	
	/* sets the approxTime value of the object */
	public void setApproxTime(String approxTime) {
		this.approxTime = approxTime;
	}
	
}
