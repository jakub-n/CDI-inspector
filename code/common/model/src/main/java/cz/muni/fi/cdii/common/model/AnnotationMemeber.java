package cz.muni.fi.cdii.common.model;

public class AnnotationMemeber {
    
    private static final String STRING_TYPE_NAME = "String";
    
    private String name;
    private String type;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return this.getName() + "="
                + (STRING_TYPE_NAME.equals(this.type) ? "\"" : "")
                + this.getValue()
                + (STRING_TYPE_NAME.equals(this.type) ? "\"" : "");
    }
}
