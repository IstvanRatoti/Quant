package quant.core;

/*
 * Abstract class for all the core classes, might not be necessary
 */
public abstract class LifeObjective {
	
	String name;
	String description;
	enum Type {	OBLIGATORY, HEALTH, SELFIMPROVEMENT,
				RECREATIONAL, CHARITY, CREATIVE};
	Type type;
	
	/* returns the name value of the object */
	public String getName() {
		return this.name;
	}
	
	/* sets the name value of the object */
	public void setName(String name) {
		this.name = name;
	}
	
	/* returns the description value of the object */
	public String getDescription() {
		return this.description;
	}
	
	/* sets the description value of the object */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/* returns the Type value of the object */
	public Type getType() {
		return this.type;
	}
	
	/* sets the Type value of the object */
	public void setType(int type) {
		switch (type)
		{
			case 0: this.type = Type.OBLIGATORY;
					break;
			case 1: this.type = Type.HEALTH;
					break;
			case 2: this.type = Type.SELFIMPROVEMENT;
					break;
			case 3: this.type = Type.RECREATIONAL;
					break;
			case 4: this.type = Type.CHARITY;
					break;
			case 5: this.type = Type.CREATIVE;
					break;
		}
	}
}
