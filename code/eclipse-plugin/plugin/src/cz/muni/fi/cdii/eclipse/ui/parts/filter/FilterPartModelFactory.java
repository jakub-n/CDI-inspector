package cz.muni.fi.cdii.eclipse.ui.parts.filter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.StyledString;

import cz.muni.fi.cdii.common.model.Qualifier;
import cz.muni.fi.cdii.common.model.Type;
import cz.muni.fi.cdii.eclipse.inspection.GraphInspection;

public class FilterPartModelFactory {

    public static final String CATEGORY_TYPES = "Types";
    public static final String CATEGORY_EL_NAMES = "EL names";
    public static final String CATEGORY_QUALIFIERS = "Qualifiers";
    public static final String CATEGORY_PACKAGES = "Packages";

    private FilterPartModelFactory() {}
    
    /**
     * Creates filter part content provider input (CategoryFilterModel[]) based on 
     * {@link GraphInspection} instance
     * @param inspection collected data
     * @return array of following categories:
     * <ul>
     * <li> package names
     * <li> types
     * <li> EL names
     * <li> qualifiers
     * </ul>
     */
    public static CategoryFilterModel[] create(GraphInspection inspection) {
        List<CategoryFilterModel> result = new ArrayList<>();
        CategoryFilterModel packageCategory = getPackageCategory(inspection);
        result.add(packageCategory);
        CategoryFilterModel typesCategory = getTypesCategory(inspection);
        result.add(typesCategory);
        CategoryFilterModel elNamesCategory = getElNamesCategory(inspection);
        result.add(elNamesCategory);
        CategoryFilterModel qualifiersCategory = getQualifiersCategory(inspection);
        result.add(qualifiersCategory);
        return result.toArray(new CategoryFilterModel[0]);
    }

    private static CategoryFilterModel getQualifiersCategory(GraphInspection inspection) {
        CategoryFilterModel result = new CategoryFilterModel(CATEGORY_QUALIFIERS);
        for (Qualifier qualifier : inspection.getQualifiers()) {
            StyledString styledString = new StyledString();
            styledString.append(qualifier.toString(false));
            styledString.append(" ");
            styledString.append("[" + qualifier.getType().getPackage() + "]", 
                    StyledString.COUNTER_STYLER);
            ElementFilterModel item = new ElementFilterModel(styledString, 
                    qualifier.toString(true));
            result.addElement(item);
        }
        return result;
    }

    private static CategoryFilterModel getElNamesCategory(GraphInspection inspection) {
        CategoryFilterModel result = new CategoryFilterModel(CATEGORY_EL_NAMES);
        for (String elName : inspection.getElNames()) {
            ElementFilterModel item = new ElementFilterModel(elName, elName);
            result.addElement(item);
        }
        return result;
    }

    private static CategoryFilterModel getTypesCategory(GraphInspection inspection) {
        CategoryFilterModel result = new CategoryFilterModel(CATEGORY_TYPES);
        for (Type type : inspection.getTypes()) {
            StyledString styledString = new StyledString();
            styledString.append(type.toString(false, true));
            styledString.append(" ");
            styledString.append("[" + type.getPackage() + "]", StyledString.COUNTER_STYLER);
            ElementFilterModel item = new ElementFilterModel(styledString, 
                    type.toString(true, true));
            result.addElement(item);
        }
        return result;
    }

    private static CategoryFilterModel getPackageCategory(GraphInspection inspection) {
        CategoryFilterModel result = new CategoryFilterModel(CATEGORY_PACKAGES);
        for (String packageName : inspection.getPackageNames()) {
            String label = packageName.isEmpty() ? "<none>" : packageName;
            ElementFilterModel item = new ElementFilterModel(label, packageName);
            result.addElement(item);
        }
        return result;
    }
}
