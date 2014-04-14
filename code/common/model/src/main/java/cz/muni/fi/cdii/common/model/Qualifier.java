package cz.muni.fi.cdii.common.model;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonAutoDetect(getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE, 
	isGetterVisibility=Visibility.NONE, fieldVisibility=Visibility.DEFAULT)
public class Qualifier {

    private AnnotationType type;
    
    /**
     * Set is sorted in order to keep toString output static.
     */
    private SortedSet<AnnotationMemeber> members = new TreeSet<>(new AnnotationMemberComparator());

    public AnnotationType getType() {
        return type;
    }

    public void setType(AnnotationType type) {
        this.type = type;
    }

    public Set<AnnotationMemeber> getMembers() {
        return members;
    }

    public void setMembers(Set<AnnotationMemeber> members) {
        this.members = new TreeSet<>(new AnnotationMemberComparator());
        this.members.addAll(members);
    }
    
    public String toString(boolean qualified) {
        StringBuilder result = new StringBuilder();
        result.append(this.getType().toString(qualified));
        if (! this.members.isEmpty()) {
            result.append("(");
            for (AnnotationMemeber member : this.getMembers()) {
                result.append(member.toString());
                result.append(", ");
            }
            result.delete(result.length() - 2, result.length());
            result.append(")");
        }
        return result.toString();
    }
    
    @Override
    public String toString() {
        return toString(true);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        Qualifier other = (Qualifier) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
    
    /**
     * Alphabetical order besed on member names. 
     */
    private static class AnnotationMemberComparator implements Comparator<AnnotationMemeber> {

        @Override
        public int compare(AnnotationMemeber o1, AnnotationMemeber o2) {
            return o1.getName().compareTo(o2.getName());
        }
        
    }
    
}
