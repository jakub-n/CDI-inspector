package cz.muni.fi.cdii.common.model;

import java.util.Set;

public class Utils {
    
    private Utils() {}
    
    /**
     * Creates details entry for given set of qualifiers
     * @param qualifiers to provide details about
     * @return details entry
     */
    public static DetailsElement getQualifiersDetails(Set<Qualifier> qualifiers) {
        DetailsElement root = new DetailsElement("Qualifiers", "");
        for (Qualifier qualifier : qualifiers) {
            root.addSubElement(qualifier.getDetails());
        }
        return root;
    };

}
