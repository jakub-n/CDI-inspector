package cz.muni.fi.cdii.common.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * Bean oriented model. It is shared among Wildfly and Eclipse
 *
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonAutoDetect(getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE, 
	isGetterVisibility=Visibility.NONE, fieldVisibility=Visibility.DEFAULT)
public class Model {

	@JsonProperty
	private Set<? extends Bean> beans = new HashSet<>();

    public Set<? extends Bean> getBeans() {
        return beans;
    }

    public void setBeans(Set<? extends Bean> beans) {
        this.beans = beans;
    }
	
	@Override
	public String toString() {
	    return "cdii model; beans: " + this.beans.toString();
	}

}
