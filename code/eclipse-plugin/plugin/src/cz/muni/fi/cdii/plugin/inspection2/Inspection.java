package cz.muni.fi.cdii.plugin.inspection2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.cdi.core.IBean;
import org.jboss.tools.cdi.core.IBeanMember;
import org.jboss.tools.cdi.core.IBeanMethod;
import org.jboss.tools.cdi.core.IBuiltInBean;
import org.jboss.tools.cdi.core.ICDIProject;
import org.jboss.tools.cdi.core.IClassBean;
import org.jboss.tools.cdi.core.IInjectionPoint;
import org.jboss.tools.cdi.core.IInjectionPointField;
import org.jboss.tools.cdi.core.IInjectionPointParameter;
import org.jboss.tools.cdi.core.IParameter;
import org.jboss.tools.cdi.core.IProducer;
import org.jboss.tools.cdi.core.IProducerField;
import org.jboss.tools.cdi.core.IProducerMethod;
import org.jboss.tools.cdi.core.IQualifierDeclaration;
import org.jboss.tools.common.java.IParametedType;

import cz.muni.fi.cdii.common.model.AnnotationMemeber;
import cz.muni.fi.cdii.common.model.AnnotationType;
import cz.muni.fi.cdii.common.model.Bean;
import cz.muni.fi.cdii.common.model.Field;
import cz.muni.fi.cdii.common.model.InjectionPoint;
import cz.muni.fi.cdii.common.model.Member;
import cz.muni.fi.cdii.common.model.Method;
import cz.muni.fi.cdii.common.model.MethodParameter;
import cz.muni.fi.cdii.common.model.Model;
import cz.muni.fi.cdii.common.model.Qualifier;
import cz.muni.fi.cdii.common.model.Type;

public class Inspection {

    private Model model;
    private Set<Type> foundTypes = new HashSet<>();
    private Map<IBean,Bean> foundBeans = new HashMap<>();
    private final ICDIProject project;

    public Inspection(ICDIProject project) {
        this.project = project;
        this.model = new Model();
        IBean[] beans = project.getBeans();

        for (IBean bean : beans) {
            this.processBean(bean);
        }

        saveResultsToModel();
    }

    private void saveResultsToModel() {
        this.model.setBeans(new HashSet<>(this.foundBeans.values()));
        this.model.setTypes(this.foundTypes);
    }

    public Model getModel() {
        return this.model;
    }

    private void processBean(IBean bean) {
        if (bean instanceof IClassBean && !(bean instanceof IBuiltInBean)) {
            final IClassBean classBean = (IClassBean) bean;
              addBean(classBean, classBean.getBeanClass());
        }
        if (bean instanceof IProducer) {
            final IProducer producer = (IProducer) bean;
            final IType producedEclipseType = ((IBeanMember) bean).getMemberType().getType();
            IClassBean classBean = producer.getClassBean();
            Bean enclosingClassBean = addBean(classBean, classBean.getBeanClass());
            addProducer(enclosingClassBean, producer, producedEclipseType);
            return;
        }
    }

    /**
     * 
     * @param enclosingModelBean
     * @param producerJbossBean
     * @param producerEclipseType
     */
    private void addProducer(Bean enclosingModelBean, IProducer producerJbossBean, 
            IType producerEclipseType) {
        final Type enclosingBeanType = enclosingModelBean.getType();
        final String memberName = producerJbossBean.getElementName();
        Member member = enclosingBeanType.getMemberByName(memberName);
        if (member == null) {
            member = createMember(producerJbossBean, enclosingBeanType);
            enclosingBeanType.getMembers().add(member);
        }
        Bean producedBean = this.addBean(producerJbossBean, producerEclipseType);
        member.setProducedBean(producedBean);
    }
    
    private Member createMember(IProducer producerBean, Type type) {
        Member result = null;
        if (producerBean instanceof IProducerField) {
            result = new Field();
        }
        if (producerBean instanceof IProducerMethod) {
            final Method method = new Method();
            final IProducerMethod producerMethod = (IProducerMethod) producerBean;
            this.copyMethodParameters(method, producerMethod);
            result = method;
        }
        result.setName(producerBean.getElementName());
        result.setType(type);
        return result;
    }
    
    private void copyMethodParameters(Method outputMethod, IBeanMethod inputMethod) {
        for (IParameter parameter : inputMethod.getParameters()) {
            Type paramType = this.addType(parameter.getType());
            MethodParameter methodParameter = new MethodParameter();
            methodParameter.setType(paramType);
            outputMethod.getParameters().add(methodParameter);
        }
    }
    
    /**
     * 
     * @param jbossBean been to add; could be producer or class-bean
     * @param mainEclipseType eclipse type of jbossBean
     * @return cdii bean corresponding to {@code jbossBean}
     */
    private Bean addBean(IBean jbossBean, IType mainEclipseType) {
        final Bean alreadyCreatedBean = this.foundBeans.get(jbossBean);
        if (alreadyCreatedBean != null) {
            return alreadyCreatedBean;
        }
        
        Collection<IParametedType> jbossTypes = jbossBean.getLegalTypes();
        Set<Type> cdiiTypes = addTypes(jbossTypes);
        Type declaredType = selectMostSpecificType(mainEclipseType, cdiiTypes);
        Bean bean = new Bean();
        bean.setType(declaredType);
        bean.setTypeSet(cdiiTypes);
        copyInjectionPointsToType(bean, jbossBean);
        // TODO add other bean properties
        this.foundBeans.put(jbossBean, bean);
        return bean;
    }

    private void copyInjectionPointsToType(Bean outputBean, IBean inputBean) {
        Collection<IInjectionPoint> injectionPoints = inputBean.getInjectionPoints();
        for (IInjectionPoint jbossInjectionPoint : injectionPoints) {
            // TODO refactor to separate method
            IParametedType jbossInjectedType = jbossInjectionPoint.getType();
            Type cdiiInjectedType = addType(jbossInjectedType);
            InjectionPoint cdiiInjectionPoint = new InjectionPoint();
            cdiiInjectionPoint.setType(cdiiInjectedType);
            cdiiInjectionPoint.setElName(jbossInjectionPoint.getBeanName());
            Collection<IBean> jbossBeansEligibleForInjection = this.project.getBeans(true, jbossInjectionPoint);
            Set<Bean> beans = addBeans(jbossBeansEligibleForInjection, jbossInjectedType.getType());
            cdiiInjectionPoint.setResolvedBeans(beans);
            // TODO add qualifiers
            
            Collection<IQualifierDeclaration> qualifierDeclarations = jbossInjectionPoint.getQualifierDeclarations();
            for (IQualifierDeclaration jbossQualifierDeclaration : qualifierDeclarations) {
                Qualifier cdiiQualifier = createQualifier(jbossQualifierDeclaration);
                jbossQualifierDeclaration.getTypeName();
                //AnnotationType cdiiAnnotationType = 
                //cdiiQualifier.setType(type);
                cdiiInjectionPoint.getQualifiers().add(cdiiQualifier);
            }
            
            addInjectionPointToType(outputBean.getType(), cdiiInjectionPoint, jbossInjectionPoint);
        }
    }

    private static Qualifier createQualifier(IQualifierDeclaration jbossQualifierDeclaration) {
        Qualifier cdiiQualifier = new Qualifier();
        AnnotationType type = annotationTypeFromQualifiedName(
                jbossQualifierDeclaration.getTypeName());
        Set<AnnotationMemeber> members = createAnnotationMemebers(
                jbossQualifierDeclaration.getJavaAnnotation());
        cdiiQualifier.setType(type);
        cdiiQualifier.setMembers(members);
        return cdiiQualifier;
    }
    
    private static Set<AnnotationMemeber> createAnnotationMemebers (IAnnotation javaAnnotation) {
        try {
            return createAnnotationMemebersUnchecked(javaAnnotation);
        } catch (JavaModelException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static Set<AnnotationMemeber> createAnnotationMemebersUnchecked
            (IAnnotation javaAnnotation) throws JavaModelException {
        Set<AnnotationMemeber> result = new HashSet<>();
        for (IMemberValuePair memberValuePair : javaAnnotation.getMemberValuePairs()) {
            AnnotationMemeber annotationMemeber = createAnnotationMemebers(memberValuePair);
            result.add(annotationMemeber);
        }
        return result;
    }

    private static AnnotationMemeber createAnnotationMemebers(IMemberValuePair eclipseMember) {
        AnnotationMemeber result = new AnnotationMemeber();
        result.setName(eclipseMember.getMemberName());
        result.setValue(arrayAwareToString(eclipseMember.getValue()));
        result.setType(annotationMemberTypeToString(eclipseMember.getValueKind()));
        return result;
    }
    
    private static String annotationMemberTypeToString(int typeCode) {
        switch (typeCode) {
        case (IMemberValuePair.K_ANNOTATION):
            return "annotation";
        case (IMemberValuePair.K_BOOLEAN):
            return "boolean";
        case (IMemberValuePair.K_BYTE):
            return "byte";
        case (IMemberValuePair.K_CHAR):
            return "char";
        case (IMemberValuePair.K_CLASS):
            return "class";
        case (IMemberValuePair.K_DOUBLE):
            return "double";
        case (IMemberValuePair.K_FLOAT):
            return "float";
        case (IMemberValuePair.K_INT):
            return "int";
        case (IMemberValuePair.K_LONG):
            return "long";
        case (IMemberValuePair.K_QUALIFIED_NAME):
            return "qualified name";
        case (IMemberValuePair.K_SHORT):
            return "short";
        case (IMemberValuePair.K_SIMPLE_NAME):
            return "simple name";
        case (IMemberValuePair.K_STRING):
            return "string";
        default:
            return "unknown";
        }
    }
    
    /**
     * calls object.toString() of Arrays.toString((proper cast) object);
     * @param object to get a string representation of
     */
    private static String arrayAwareToString(Object object) {
        Class<? extends Object> objectClass = object.getClass();
        if (objectClass.isArray()) {
            if (objectClass.equals(byte[].class)) {
                return Arrays.toString((byte[]) object);
            }
            if (objectClass.equals(short[].class)) {
                return Arrays.toString((short[]) object);
            }
            if (objectClass.equals(int[].class)) {
                return Arrays.toString((int[]) object);
            }
            if (objectClass.equals(long[].class)) {
                return Arrays.toString((long[]) object);
            }
            if (objectClass.equals(char[].class)) {
                return Arrays.toString((char[]) object);
            }
            if (objectClass.equals(float[].class)) {
                return Arrays.toString((float[]) object);
            }
            if (objectClass.equals(double[].class)) {
                return Arrays.toString((double[]) object);
            }
            if (objectClass.equals(boolean[].class)) {
                return Arrays.toString((boolean[]) object);
            }
            return Arrays.toString((Object[]) object);
        }
        return object.toString();
    }

    private static AnnotationType annotationTypeFromQualifiedName(String qualifiedName) {
        int lastDotIndex = qualifiedName.lastIndexOf(".");
        String packageName = qualifiedName.substring(0, lastDotIndex);
        String simpleName = qualifiedName.substring(lastDotIndex + 1);
        AnnotationType result = new AnnotationType();
        result.setName(simpleName);
        result.setPackage(packageName);
        return result;
    }

    private void addInjectionPointToType(Type cdiiType, InjectionPoint cdiiInjectionPoint, 
            IInjectionPoint jbossInjectionPoint) {
        if (jbossInjectionPoint instanceof IInjectionPointField) {
            final IInjectionPointField ipField = (IInjectionPointField) jbossInjectionPoint;
            addFieldIPToType(cdiiType, cdiiInjectionPoint, ipField);
        }
        if (jbossInjectionPoint instanceof IInjectionPointParameter) {
            final IInjectionPointParameter jbossIpParameter = 
                    (IInjectionPointParameter) jbossInjectionPoint;
            addMethodParameterIPToType(cdiiType, cdiiInjectionPoint, jbossIpParameter);
        }
    }

    private void addMethodParameterIPToType(Type cdiiType, InjectionPoint cdiiInjectionPoint,
            final IInjectionPointParameter jbossIpParameter) {
        String methodName = jbossIpParameter.getBeanMethod().getElementName();
        Method method = (Method) cdiiType.getMemberByName(methodName);
        if (method == null) {
            Type methodType = addType(jbossIpParameter.getBeanMethod().getMemberType());
            method = new Method();
            method.setName(methodName);
            method.setType(methodType);
            this.copyMethodParameters(method, jbossIpParameter.getBeanMethod());
            cdiiType.getMembers().add(method);
        }
        final int parameterIndex = getParameterIndex(jbossIpParameter);
        method.getParameters().get(parameterIndex).setInjectionPoint(cdiiInjectionPoint);
    }

    private void addFieldIPToType(Type type, InjectionPoint modelIP,
            final IInjectionPointField jbossIPField) {
        String fieldName = jbossIPField.getElementName();
        Field cdiiField = (Field) type.getMemberByName(fieldName);
        if (cdiiField == null) {
            Type cdiiIpType = addType(jbossIPField.getType());
            cdiiField = new Field();
            cdiiField.setType(cdiiIpType);
            cdiiField.setName(fieldName);
            type.getMembers().add(cdiiField);
        }
        cdiiField.setInjectionPoint(modelIP);
    }

    private static int getParameterIndex(IInjectionPointParameter ipParameter) {
        String parameterName = ipParameter.getName();
        List<IParameter> jbossParameters = ipParameter.getBeanMethod().getParameters();
        for (int parameterIndex = 0; parameterIndex < jbossParameters.size(); parameterIndex++) {
            String currentParamName = jbossParameters.get(parameterIndex).getName();
            if (parameterName.equals(currentParamName)) {
                return parameterIndex;
            }
        }
        throw new RuntimeException("Argument of name '" + parameterName + "' not found in method '" 
                + ipParameter.getBeanMethod() + "'.");
    }

    private Set<Bean> addBeans(Collection<IBean> beans,  IType eclipseTypeHint) {
        Set<Bean> result = new HashSet<>();
        for (IBean jbossBean: beans) {
            Bean modelBean = addBean(jbossBean, eclipseTypeHint);
            result.add(modelBean);
        }
        return result;
    }
    
    private static Type selectMostSpecificType(IType eclipseTypeHint, Set<Type> types) {
        String fullyQualifiedName = eclipseTypeHint.getFullyQualifiedName();
        for (Type item : types) {
            String itemQualifiedName = item.getFullyQualifiedName();
            if (fullyQualifiedName.equals(itemQualifiedName)) {
                return item;
            }
        }
        throw new RuntimeException("Failed to find proper bean type for " + fullyQualifiedName);
    }

    private Set<Type> addTypes(Collection<IParametedType> types) {
        Set<Type> result = new HashSet<>();
        for (IParametedType jbossType : types) {
            Type type = addType(jbossType);
            result.add(type);
        }
        return result;
    }

    private Type addType(IParametedType jbossType) {
        Type type = extractType(jbossType);
        boolean typeAdded = this.foundTypes.add(type);
        if (typeAdded) {
            return type;
        }
        return getEqual(type, this.foundTypes);
    }

    /**
     * 
     * @param element
     *            determining the equality; must not be null
     * @param set
     *            to search in
     * @return item `e` of `set` such that element.equals(e) or `null` if set
     *         does not contain item equal to element
     */
    private static <T> T getEqual(final T element, final Set<T> set) {
        for (T setItem : set) {
            if (element.equals(setItem)) {
                return setItem;
            }
        }
        return null;
    }

    /**
     * note: {@link Type} does not support nested classes, type variables and
     * type bounds. Method provide undefined results for such inputs.
     */
    private Type extractType(IParametedType jbossType) {
        String simpleName = jbossType.getSimpleName();
        boolean isArray = simpleName.endsWith("[]");
        String fullyQualifiedName = jbossType.getType().getFullyQualifiedName();
        int lastDotIndex = fullyQualifiedName.lastIndexOf(".");
        String package_ = fullyQualifiedName.substring(0, lastDotIndex);
        String name = fullyQualifiedName.substring(lastDotIndex + 1);
        List<Type> typeParameters = new ArrayList<>();
        if (!jbossType.getParameters().isEmpty()) {
            for (IParametedType jbossParam : jbossType.getParameters()) {
                Type typeParam = addType(jbossParam);
                typeParameters.add(typeParam);
            }
        }
        Type result = new Type();
        result.setPackage(package_);
        result.setName(name);
        result.setTypeParameters(typeParameters);
        result.setArray(isArray);
        return result;
    }

}
