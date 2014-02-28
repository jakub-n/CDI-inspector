package cz.muni.fi.cdii.plugin.inspection2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.eclipse.jdt.core.IJavaProject;
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
import org.jboss.tools.common.java.IParametedType;

import cz.muni.fi.cdii.common.model.Bean;
import cz.muni.fi.cdii.common.model.Field;
import cz.muni.fi.cdii.common.model.InjectionPoint;
import cz.muni.fi.cdii.common.model.Member;
import cz.muni.fi.cdii.common.model.Method;
import cz.muni.fi.cdii.common.model.MethodParameter;
import cz.muni.fi.cdii.common.model.Model;
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

    }

    public Model getModel() {
        return this.model;
    }

    private void processBean(IBean bean) {
        if (bean instanceof IClassBean && !(bean instanceof IBuiltInBean)) {
            final IClassBean classBean = (IClassBean) bean;

            // System.out.println(">>> " + classBean.getBeanClass().getKey());
            // try {
            // System.out.println(">> "
            // + classBean.getBeanClass().getFullyQualifiedParameterizedName());
            // } catch (JavaModelException e) {
            // e.printStackTrace();
            // }
            // for (IParametedType type : bean.getLegalTypes()) {
            // System.out.println("> " + type.getSignature());
            // System.out.println(">e " + extractType(type));
            // }
            // System.out.println("injectionPoints:");
            // for (IInjectionPoint ip : classBean.getInjectionPoints()) {
            // printInectionPointTypeDetail(ip);
            // }
            //
            // System.out.println("producers");
            // for (IProducer producer : classBean.getProducers()) {
            // System.out.println("element name: " + producer.getElementName());
            // try {
            // System.out.println(">> "
            // + producer.getBeanClass().getFullyQualifiedParameterizedName());
            // } catch (JavaModelException e) {
            // e.printStackTrace();
            // }
            // System.out.println("legal types count: " +
            // producer.getLegalTypes().size());
            // for (IParametedType type : producer.getLegalTypes()) {
            // try {
            // System.out.println("> "
            // + type.getType().getFullyQualifiedParameterizedName());
            // System.out.println(">, " + type.getSignature());
            // System.out.println("superInterfaceTypeSignatures: " +
            // Arrays.toString(type.getType().getSuperInterfaceNames()));
            // System.out.println("superClassTypeSignature: " +
            // type.getType().getSuperclassName());
            // } catch (JavaModelException e) {
            // e.printStackTrace();
            // }
            // }
            // }

            addBean(classBean);
            
        }
        if (bean instanceof IProducer) {
            final IProducer producer = (IProducer) bean;
            final IType producerType = ((IBeanMember) bean).getMemberType().getType();
            IClassBean classBean = producer.getClassBean();
            Bean modelBean = addBean(classBean);
            addProducer(modelBean, producer, producerType);
            return;
        }
//        System.out.println("---------------------------------------------------------------------");
//        System.out.println(bean + "; " + bean.getClass() + "; " + bean.getElementName() + "; "
//                + bean.getName());
//        System.out.println("type signature: "
//                + bean.getAllTypes().toArray(new IParametedType[0])[0].getSignature());
//        System.out.println("injection points: " + bean.getInjectionPoints());
//        if (bean.getInjectionPoints().size() > 0) {
//            System.out.println();
//            for (IInjectionPoint ip : bean.getInjectionPoints()) {
//                System.out.println(" > " + ip.getBeanName() + "; " + ip.getElementName() + "; "
//                        + ip.getId() + "; " + ip.getMemberType() + "; " + ip.getType().getType());
//            }
//        }
        // TODO overit, ze se na BuiltInBeany je pripadne mozne dostat jinak -
        // napr pres injection pointy, pokud jsou injectovany, ...
        // if (bean instanceof IBuiltInBean) {
        // return;
        // }
        // IClassBean, IDecorator, IGenericBean, IInterceptor, IProducer,
        // IProducerField, IProducerMethod, ISessionBean
        //
        // printIType(bean.getBeanClass());
        // this.addClass(bean.getBeanClass());
        // this.addType(bean);
        // this.addBean(bean);
    }

    private void addProducer(Bean modelBean, IProducer producerBean, IType producerType) {
        final Type type = modelBean.getType();
        final String memberName = producerBean.getElementName();
        Member member = type.getMemberByName(memberName);
        if (member == null) {
            member = createMember(producerBean, modelBean.getType());
            type.getMembers().add(member);
        }
        Bean producedBean = this.addBean(producerBean, producerType);
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
    
    private Bean addBean(IBean jbossBean) {
        return this.addBean(jbossBean, jbossBean.getBeanClass());
    }
    
    private Bean addBean(IBean jbossBean, IType mainType) {
        final Bean alreadyCreatedBean = this.foundBeans.get(jbossBean);
        if (alreadyCreatedBean != null) {
            return alreadyCreatedBean;
        }
        
        Collection<IParametedType> jbossTypes = jbossBean.getLegalTypes();
        Set<Type> types = addTypes(jbossTypes);
        Type declaredType = selectTypeByEclipseType(mainType, types);
        jbossBean.getBeanClass().getFullyQualifiedName();
        Bean bean = new Bean();
        bean.setType(declaredType);
        bean.setTypeSet(types);
        copyInjectionPointsToType(bean, jbossBean);
        // TODO add other bean properties
        this.foundBeans.put(jbossBean, bean);
        return bean;
    }

    private void copyInjectionPointsToType(Bean outputBean, IBean inputBean) {
        Collection<IInjectionPoint> injectionPoints = inputBean.getInjectionPoints();
        for (IInjectionPoint injectionPoint : injectionPoints) {
            InjectionPoint modelIP = new InjectionPoint();
            modelIP.setElName(injectionPoint.getBeanName());
            Set<Bean> beans = addBeans(this.project.getBeans(true, injectionPoint));
            modelIP.setResolvedBeans(beans);
            // TODO add qualifiers
            addInjectionPointToType(outputBean.getType(), modelIP, injectionPoint);
        }
    }

    private void addInjectionPointToType(Type type, InjectionPoint modelIP, 
            IInjectionPoint jbossIP) {
        if (jbossIP instanceof IInjectionPointField) {
            final IInjectionPointField ipField = (IInjectionPointField) jbossIP;
            addFieldIPToType(type, modelIP, jbossIP, ipField);
        }
        if (jbossIP instanceof IInjectionPointParameter) {
            final IInjectionPointParameter ipParameter = (IInjectionPointParameter) jbossIP;
            addMethodParameterIPToType(type, modelIP, ipParameter);
        }
    }

    private void addMethodParameterIPToType(Type type, InjectionPoint modelIP,
            final IInjectionPointParameter ipParameter) {
        String methodName = ipParameter.getBeanMethod().getElementName();
        Method method = (Method) type.getMemberByName(methodName);
        if (method == null) {
            Type methodType = addType(ipParameter.getBeanMethod().getMemberType());
            method = new Method();
            method.setName(methodName);
            method.setType(methodType);
            this.copyMethodParameters(method, ipParameter.getBeanMethod());
            type.getMembers().add(method);
        }
        final int parameterIndex = getParameterIndex(ipParameter);
        method.getParameters().get(parameterIndex).setInjectionPoint(modelIP);
    }

    private void addFieldIPToType(Type type, InjectionPoint modelIP, IInjectionPoint jbossIP,
            final IInjectionPointField ipField) {
        String fieldName = ipField.getElementName();
        Field field = (Field) type.getMemberByName(fieldName);
        if (field == null) {
            Type ipType = addType(jbossIP.getType());
            field = new Field();
            field.setType(ipType);
            field.setName(fieldName);
            type.getMembers().add(field);
        }
        field.setInjectionPoint(modelIP);
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

    private Set<Bean> addBeans(Collection<IBean> beans) {
        Set<Bean> result = new HashSet<>();
        for (IBean jbossBean: beans) {
            Bean modelBean = addBean(jbossBean);
            result.add(modelBean);
        }
        return result;
    }

    private static Type selectTypeByEclipseType(IType eclipseType, Set<Type> types) {
        String fullyQualifiedName = eclipseType.getFullyQualifiedName();
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
    private static Type extractType(IParametedType jbossType) {
        String fullyQualifiedName = jbossType.getType().getFullyQualifiedName();
        int lastDotIndex = fullyQualifiedName.lastIndexOf(".");
        String package_ = fullyQualifiedName.substring(0, lastDotIndex);
        String name = fullyQualifiedName.substring(lastDotIndex + 1);
        List<Type> typeParameters = new ArrayList<>();
        if (!jbossType.getParameters().isEmpty()) {
            for (IParametedType jbossParam : jbossType.getParameters()) {
                Type typeParam = extractType(jbossParam);
                typeParameters.add(typeParam);
            }
        }
        Type result = new Type();
        result.setPackage(package_);
        result.setName(name);
        result.setTypeParameters(typeParameters);
        return result;
    }

    // TODO delete
    void printInectionPointTypeDetail(IInjectionPoint ip) {
        System.out.println("signature: " + ip.getType().getSignature());
        try {
            System.out.println("fqt: "
                    + ip.getType().getType().getFullyQualifiedParameterizedName());
        } catch (JavaModelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // TODO mozna smazat
    private static IParametedType selectMostSpecific(Set<IParametedType> types) {
        Map<IParametedType, Set<IType>> parentTypesMap = new HashMap<>();
        for (IParametedType type : types) {
            Set<IType> parentTypes = getParentTypes(type);
            parentTypesMap.put(type, parentTypes);
        }
        int maxCount = 0;
        IParametedType maxCountType = null;
        for (Map.Entry<IParametedType, Set<IType>> entry : parentTypesMap.entrySet()) {
            if (entry.getValue().size() > maxCount) {
                maxCount = entry.getValue().size();
                maxCountType = entry.getKey();
            }
        }
        return maxCountType;
    }

    private static Set<IType> getParentTypes(IParametedType type) {
        Set<IType> result = new HashSet<>();
        Queue<IType> toProcess = new LinkedList<>();
        toProcess.add(type.getType());
        while (toProcess.peek() != null) {
            IType currentType = toProcess.poll();
            result.add(currentType);
            Set<IType> superClassAndInterfaces = getSuperClassAndInterfaces(currentType);
            toProcess.addAll(superClassAndInterfaces);
        }
        return null;
    }

    private static Set<IType> getSuperClassAndInterfaces(final IType type) {
        try {
            return getSuperClassAndInterfacesUnchecked(type);
        } catch (JavaModelException e) {
            // TODO log
            return Collections.emptySet();
        }
    }

    private static Set<IType> getSuperClassAndInterfacesUnchecked(final IType type)
            throws JavaModelException {
        Set<String> typeNames = new HashSet<>();
        String superclassName = type.getSuperclassName();
        typeNames.add(superclassName);
        String[] superInterfaceNames = type.getSuperInterfaceNames();
        typeNames.addAll(Arrays.asList(superInterfaceNames));
        Set<IType> result = new HashSet<>();
        IJavaProject javaProject = type.getJavaProject();
        for (String typeName : typeNames) {
            IType resolvedType = javaProject.findType(typeName);
            // TODO log if resolved type is null
            if (resolvedType != null) {
                result.add(resolvedType);
            }
        }
        return result;
    }

    // TODO delete
//    private void printIType(IType eclipseType) {
//        try {
//            System.out.println("element name " + eclipseType.getElementName());
//            System.out.println("element type " + eclipseType.getElementType());
//            System.out.println("flags " + eclipseType.getFlags());
//            System.out.println("fq name" + eclipseType.getFullyQualifiedName());
//            System.out.println("fully qualified parametrized name "
//                    + eclipseType.getFullyQualifiedParameterizedName());
//            System.out.println("fq parametrized name " + eclipseType.getHandleIdentifier());
//            System.out.println("key " + eclipseType.getKey());
//            // System.out.println("source " + eclipseType.getSource());
//            System.out.println("super class name " + eclipseType.getSuperclassName());
//            System.out.println("super class signature " + eclipseType.getSuperclassTypeSignature());
//            System.out.println("type qualified name " + eclipseType.getTypeQualifiedName());
//            System.out.println("declaring type " + eclipseType.getDeclaringType());
//            System.out.println("fields " + Arrays.toString(eclipseType.getFields()));
//            System.out.println("initializers " + Arrays.toString(eclipseType.getInitializers()));
//            System.out.println("methods " + Arrays.toString(eclipseType.getMethods()));
//            System.out.println("range name " + eclipseType.getNameRange().toString());
//            // System.out.println("package fragment " +
//            // eclipseType.getPackageFragment());
//            System.out.println("parent " + eclipseType.getParent());
//            System.out.println("path " + eclipseType.getPath());
//            System.out.println("primary element " + eclipseType.getPrimaryElement());
//            System.out.println("resource " + eclipseType.getResource());
//            System.out.println("scheruling rule " + eclipseType.getSchedulingRule());
//            System.out.println("superinterface name "
//                    + Arrays.toString(eclipseType.getSuperInterfaceNames()));
//            System.out.println("super interface type signatures"
//                    + Arrays.toString(eclipseType.getSuperInterfaceTypeSignatures()));
//            System.out
//                    .println("type parameters" + Arrays.toString(eclipseType.getTypeParameters()));
//            System.out.println("type parameter signatures"
//                    + Arrays.toString(eclipseType.getTypeParameterSignatures()));
//            System.out.println("type root " + eclipseType.getTypeRoot());
//            System.out.println("is annotation " + eclipseType.isAnnotation());
//            System.out.println("is anonymous " + eclipseType.isAnonymous());
//            System.out.println("is binary " + eclipseType.isBinary());
//            System.out.println("is class " + eclipseType.isClass());
//            System.out.println("is enum " + eclipseType.isEnum());
//            System.out.println("is interface " + eclipseType.isInterface());
//            System.out.println("is member " + eclipseType.isMember());
//            System.out.println("is read only " + eclipseType.isReadOnly());
//            System.out.println("is resolved" + eclipseType.isResolved());
//            System.out.println("is structure known " + eclipseType.isStructureKnown());
//        } catch (JavaModelException ex) {
//            ex.printStackTrace();
//        }
//    }
}
