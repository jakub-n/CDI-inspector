package cz.muni.fi.cdii.common.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * equality based on 'package' and 'name' fields
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonAutoDetect(getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE, 
	isGetterVisibility=Visibility.NONE, fieldVisibility=Visibility.DEFAULT)
public class Scope {

	@JsonProperty
	private boolean pseudo;
	
	@JsonProperty
	private String package_;
	
	@JsonProperty
	private String name;

    public boolean isPseudo() {
        return pseudo;
    }

    public void setPseudo(boolean pseudo) {
        this.pseudo = pseudo;
    }

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
	
	@Override
	public String toString() {
	    return "@" + this.getName();
	}  
    
    public String toQualifiedString() {
        return "@" + this.getPackage() + "." + this.getName();
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
        Scope other = (Scope) obj;
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
}
