package cz.muni.fi.cdii.common.model;

public class AnnotationType {
    
    private String package_;
    
    private String name;

    public String getPackage() {
        return package_;
    }

    public void setPackage(String package_) {
        this.package_ = package_;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String toString(boolean qualified) {
        return (qualified ? this.getPackage() + "." : "") + this.getName();
    }
    
    @Override
    public String toString() {
        return this.toString(true);
    }
}
