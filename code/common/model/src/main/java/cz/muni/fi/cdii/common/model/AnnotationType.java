package cz.muni.fi.cdii.common.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonAutoDetect(getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE, 
    isGetterVisibility=Visibility.NONE, fieldVisibility=Visibility.NONE)
public class AnnotationType implements Comparable<AnnotationType> {

    @JsonProperty
    private String package_;

    @JsonProperty
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((package_ == null) ? 0 : package_.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AnnotationType other = (AnnotationType) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (package_ == null) {
            if (other.package_ != null)
                return false;
        } else if (!package_.equals(other.package_))
            return false;
        return true;
    }

    @Override
    public int compareTo(AnnotationType o) {
        int packageComparison = this.getPackage().compareTo(o.getPackage());
        if (packageComparison != 0) {
            return packageComparison;
        }
        int nameComparison = this.getName().compareTo(o.getName());
        return nameComparison;
    }
    
    
}
