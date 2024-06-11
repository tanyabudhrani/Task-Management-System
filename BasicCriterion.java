package hk.edu.polyu.comp.comp2021.tms.model;

import java.io.Serializable;

class BasicCriterion implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String property;
    private String op;
    private String value;

    public BasicCriterion(String name, String property, String op, String value) {
        this.name = name;
        this.property = property;
        this.op = op;
        this.value = value;
    }

	public String getName() {
		return name;
	}
	
	public String getProperty() {
        return property;
    }
    
    public String getOp() {
    	return op;
    }
    
    public String getValue() {
        return value;
    }
}
