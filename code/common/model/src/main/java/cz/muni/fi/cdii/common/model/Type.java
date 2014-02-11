package cz.muni.fi.cdii.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * Represents java type. For non-parametrized types it is equal to {@link Class}. For parametrized 
 * types there can be multiple {@link Type}s for one {@link Class}.
 * <p>
 * Equality is based on members {@link #clazz}, {@link #typeParameters} and {@link #wildcardType}.
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonAutoDetect(getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE, 
	isGetterVisibility=Visibility.NONE, fieldVisibility=Visibility.DEFAULT)
public class Type {
	
	/**
	 * null iff {@link #wildcardType} is {@link TypeWildcardEnum#QUESTIONMARK}
	 */

	@JsonProperty
	private Class clazz;
	/**
	 * null if 
	 * <ul>
	 * <li>{@link #wildcardType} is {@link TypeWildcardEnum#QUESTIONMARK}
	 * <li>type is non-parametrized
	 * </ul>
	 */

	@JsonProperty
	private List<Type> typeParameters;

	@JsonProperty
	private TypeWildcardEnum wildcardType;
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
		result = prime * result
				+ ((typeParameters == null) ? 0 : typeParameters.hashCode());
		result = prime * result
				+ ((wildcardType == null) ? 0 : wildcardType.hashCode());
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
		Type other = (Type) obj;
		if (clazz == null) {
			if (other.clazz != null)
				return false;
		} else if (!clazz.equals(other.clazz))
			return false;
		if (typeParameters == null) {
			if (other.typeParameters != null)
				return false;
		} else if (!typeParameters.equals(other.typeParameters))
			return false;
		if (wildcardType != other.wildcardType)
			return false;
		return true;
	}
	
	
}
