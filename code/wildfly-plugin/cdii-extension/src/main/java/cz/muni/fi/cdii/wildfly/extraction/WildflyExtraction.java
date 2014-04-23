package cz.muni.fi.cdii.wildfly.extraction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Named;

import org.jboss.weld.bean.AbstractProducerBean;
import org.jboss.weld.bean.ProducerField;
import org.jboss.weld.bean.ProducerMethod;
import org.jboss.weld.bean.RIBean;

import cz.muni.fi.cdii.common.model.AnnotationMemeber;
import cz.muni.fi.cdii.common.model.AnnotationType;
import cz.muni.fi.cdii.common.model.Field;
import cz.muni.fi.cdii.common.model.InjectionPoint;
import cz.muni.fi.cdii.common.model.Member;
import cz.muni.fi.cdii.common.model.Method;
import cz.muni.fi.cdii.common.model.MethodParameter;
import cz.muni.fi.cdii.common.model.Model;
import cz.muni.fi.cdii.common.model.Qualifier;
import cz.muni.fi.cdii.common.model.Scope;
import cz.muni.fi.cdii.common.model.Type;

public class WildflyExtraction {

    private final BeanManager beanManager;

    private Map<Bean<?>, cz.muni.fi.cdii.common.model.Bean> beansMap = new HashMap<>();
    private Map<java.lang.reflect.Type, Type> typesMap = new HashMap<>();
    private Set<javax.enterprise.inject.spi.InjectionPoint> injectionPoints = new HashSet<>();
    private Map<AbstractProducerBean<?, ?, ?>, cz.muni.fi.cdii.common.model.Bean> 
            deferredProducers = new HashMap<>();

    public WildflyExtraction(BeanManager beanManager) {
        this.beanManager = beanManager;
        processBeans();
        processInjectionPoints();
        processProducers();
    }

    private void processProducers() {
        for (Entry<AbstractProducerBean<?, ?, ?>, cz.muni.fi.cdii.common.model.Bean> entry : this.deferredProducers
                .entrySet()) {
            processProducer(entry.getKey(), entry.getValue());
        }

    }

    private void processProducer(AbstractProducerBean<?, ?, ?> weldProducer,
            cz.muni.fi.cdii.common.model.Bean cdiiBean) {
        if (weldProducer instanceof ProducerMethod) {
            ProducerMethod<?, ?> producerMethod = (ProducerMethod<?, ?>) weldProducer;
            java.lang.reflect.Method javaMethod = producerMethod.getAnnotated().getJavaMember();
            addProducingMember(javaMethod, cdiiBean);
            return;
        }
        if (weldProducer instanceof ProducerField) {
            ProducerField<?, ?> producerField = (ProducerField<?, ?>) weldProducer;
            java.lang.reflect.Field javaField = producerField.getAnnotated().getJavaMember();
            addProducingMember(javaField, cdiiBean);
            return;
        }
        throw new RuntimeException("Unexpected producer type: " 
        + weldProducer.getClass().getCanonicalName());
    }

    private void addProducingMember(java.lang.reflect.Member producingJavaMember,
            cz.muni.fi.cdii.common.model.Bean producedCdiiBean) {
        Class<?> javaParentClass = producingJavaMember.getDeclaringClass();
        Type cdiiParentType = addClass(javaParentClass);
        Member producingMember = getOrCreateCdiiMember(producingJavaMember, cdiiParentType);
        producingMember.setProducedBean(producedCdiiBean);
    }

    private void processInjectionPoints() {
        for (javax.enterprise.inject.spi.InjectionPoint javaInjectionPoint : this.injectionPoints) {
            addInjectionPoint(javaInjectionPoint);
        }
    }

    private InjectionPoint addInjectionPoint(
            javax.enterprise.inject.spi.InjectionPoint javaInjectionPoint) {
        cz.muni.fi.cdii.common.model.Bean resolvedBean = addBean(javaInjectionPoint.getBean());
        Type parentType = addClass(javaInjectionPoint.getMember().getDeclaringClass());
        Member cdiiMember = getOrCreateCdiiMember(javaInjectionPoint.getMember(), parentType);
        parentType.getMembers().add(cdiiMember);
        Set<Qualifier> cdiiQualifiers = toCdiiQualifiers(javaInjectionPoint.getQualifiers());
        Type cdiiType = addType(javaInjectionPoint.getType());
        InjectionPoint result = new InjectionPoint();
        result.setQualifiers(cdiiQualifiers);
        result.setResolvedBeans(Collections.singleton(resolvedBean));
        result.setType(cdiiType);
        if (cdiiMember instanceof Field) {
            Field field = (Field) cdiiMember;
            field.setInjectionPoint(result);
            Named namedAnnotation = ((java.lang.reflect.Field) javaInjectionPoint.getMember())
                    .getAnnotation(Named.class);
            result.setElName(toElName(namedAnnotation));
        } else {
            Method method = (Method) cdiiMember;
            int paramIndex = attachInjectionPointToMethod(method, result);
            String elName = getMethodLikeMemberParamNamedValue(javaInjectionPoint.getMember(),
                    paramIndex);
            result.setElName(elName);
        }
        return result;
    }

    /**
     * Returns existing member of the same name as param {@code member} or creates new one if 
     * member of specified name does not exist.
     * @param member
     * @param parentType
     * @return cdii member corresponding to input java {@code member} param
     */
    private Member getOrCreateCdiiMember(java.lang.reflect.Member member, Type parentType) {
        String memberName = member.getName();
        Member existingMember = parentType.getMemberByName(memberName);
        if (existingMember != null) {
            return existingMember;
        }
        return toCdiiMember(member, parentType);
    }

    private String getMethodLikeMemberParamNamedValue(java.lang.reflect.Member member,
            int paramIndex) {
        if (member instanceof java.lang.reflect.Constructor) {
            Constructor<?> constructor = (Constructor<?>) member;
            Annotation[] paramAnnotations = constructor.getParameterAnnotations()[paramIndex];
            return getElName(paramAnnotations);
        }
        if (member instanceof java.lang.reflect.Constructor) {
            Constructor<?> constructor = (Constructor<?>) member;
            Annotation[] paramAnnotations = constructor.getParameterAnnotations()[paramIndex];
            return getElName(paramAnnotations);
        }
        throw new RuntimeException("Unexpected member type.");
    }

    /**
     * @param annotations
     * @return value of {@link Named} annotation if it is contained in {@code annotations} param and
     *         the value extraction will success; null otherwise
     */
    private String getElName(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Named) {
                Named namedAnnotation = (Named) annotation;
                return toElName(namedAnnotation);
            }
        }
        return null;
    }

    /**
     * Should be used with injection points only. For empty 'value' member returns {@code null}.
     * 
     * @param namedAnnotation
     *            annotation to extract the value
     * @return value of annotation
     */
    private String toElName(Named namedAnnotation) {
        if (namedAnnotation == null) {
            return null;
        }
        java.lang.reflect.Method valueMethod;
        try {
            valueMethod = namedAnnotation.annotationType().getMethod("value");
        } catch (NoSuchMethodException | SecurityException e) {
            return null;
        }
        String result = (String) getAnnotationMemberValue(valueMethod, namedAnnotation);
        return result;
    }

    /**
     * @return zero based parameter index
     */
    private int attachInjectionPointToMethod(Method method, InjectionPoint injectionPoint) {
        for (MethodParameter parameter : method.getParameters()) {
            if (parameter.getType().equals(injectionPoint.getType())) {
                parameter.setInjectionPoint(injectionPoint);
                return method.getParameters().indexOf(parameter);
            }
        }
        throw new RuntimeException("Method argument of type " + injectionPoint.getType()
                + " of method " + method + " was not found.");
    }

    private Member toCdiiMember(java.lang.reflect.Member javaMember, Type cdiiParentType) {
        if (javaMember instanceof java.lang.reflect.Field) {
            java.lang.reflect.Field javaField = (java.lang.reflect.Field) javaMember;
            Field result = new Field();
            result.setName(javaField.getName());
            Type fieldCdiiType = addClass(javaField.getType());
            result.setSurroundingType(fieldCdiiType);
            Type fieldCdiiParentClass = addClass(javaField.getDeclaringClass());
            result.setSurroundingType(fieldCdiiParentClass);
            cdiiParentType.getMembers().add(result);
            return result;
        }
        if (javaMember instanceof java.lang.reflect.Method) {
            java.lang.reflect.Method javaMethod = (java.lang.reflect.Method) javaMember;
            Method result = new Method();
            result.setConstructor(false);
            result.setName(javaMember.getName());
            List<MethodParameter> parameters = toCdiiTypes(javaMethod.getParameterTypes());
            result.setParameters(parameters);
            Type methodCdiiParentType = addClass(javaMethod.getDeclaringClass());
            result.setSurroundingType(methodCdiiParentType);
            Type methodCdiiReturnType = addClass(javaMethod.getReturnType());
            result.setType(methodCdiiReturnType);
            cdiiParentType.getMembers().add(result);
            return result;
        }
        if (javaMember instanceof java.lang.reflect.Constructor) {
            java.lang.reflect.Constructor<?> constructor = (java.lang.reflect.Constructor<?>) javaMember;
            Method result = new Method();
            result.setConstructor(true);
            result.setName(constructor.getName());
            List<MethodParameter> parameters = toCdiiTypes(constructor.getParameterTypes());
            result.setParameters(parameters);
            Type constructorCdiiParentType = addClass(constructor.getDeclaringClass());
            result.setSurroundingType(constructorCdiiParentType);
            result.setType(null);
            cdiiParentType.getMembers().add(result);
            return result;
        }
        throw new RuntimeException("Unknown member type.");
    }

    private List<MethodParameter> toCdiiTypes(Class<?>[] parameterTypes) {
        List<MethodParameter> result = new ArrayList<>();
        for (Class<?> parameterJavaClass : parameterTypes) {
            MethodParameter methodParameter = new MethodParameter();
            Type cdiiParamType = addClass(parameterJavaClass);
            methodParameter.setType(cdiiParamType);
            result.add(methodParameter);
        }
        return result;
    }

    public static Model extract(BeanManager beanManager) {
        WildflyExtraction extractor = new WildflyExtraction(beanManager);
        Model model = extractor.getModel();
        return model;
    }

    public Model getModel() {
        Model model = new Model();
        Collection<cz.muni.fi.cdii.common.model.Bean> beans = this.beansMap.values();
        model.setBeans(new HashSet<cz.muni.fi.cdii.common.model.Bean>(beans));
        return model;
    }

    private void processBeans() {
        @SuppressWarnings("serial")
        Set<Bean<?>> allBeans = this.beanManager.getBeans(Object.class,
                new AnnotationLiteral<Any>() {
                });
        Set<Bean<?>> applicationBeans = removeInternalBeans(allBeans);
        for (Bean<?> bean : applicationBeans) {
            addBean(bean);
        }
    }

    private cz.muni.fi.cdii.common.model.Bean addBean(Bean<?> bean) {
        if (this.beansMap.containsKey(bean)) {
            return this.beansMap.get(bean);
        }
        Class<?> javaBeanType = bean.getBeanClass();
        if (bean instanceof RIBean) {
            javaBeanType = ((RIBean<?>) bean).getType();
        }
        cz.muni.fi.cdii.common.model.Bean result = new cz.muni.fi.cdii.common.model.Bean();
        Type type = addClass(javaBeanType);
        result.setType(type);
        result.setElName(bean.getName());
        Set<Qualifier> cdiiQualifiers = toCdiiQualifiers(bean.getQualifiers());
        result.setQualifiers(cdiiQualifiers);
        Scope scope = toScope(bean.getScope());
        result.setScope(scope);
        Set<Type> typeSet = toTypeSet(bean.getTypes());
        result.setTypeSet(typeSet);
        if (bean instanceof AbstractProducerBean) {
            AbstractProducerBean<?, ?, ?> producerBean = (AbstractProducerBean<?, ?, ?>) bean;
            this.deferredProducers.put(producerBean, result);
        }
        this.beansMap.put(bean, result);
        return result;
    }

    private Set<Type> toTypeSet(Set<java.lang.reflect.Type> javaTypes) {
        HashSet<Type> result = new HashSet<>();
        for (java.lang.reflect.Type javaType : javaTypes) {
            Type cdiiType = addType(javaType);
            result.add(cdiiType);
        }
        return result;
    }

    private Type addType(java.lang.reflect.Type javaType) {
        if (javaType instanceof Class) {
            Class<?> javaClass = (Class<?>) javaType;
            return addClass(javaClass);
        }
        if (javaType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) javaType;
            return addParameterizedType(parameterizedType);
        }
        if (javaType instanceof TypeVariable) {
            TypeVariable<?> typeVariable = (TypeVariable<?>) javaType;
            return addTypeVariable(typeVariable);
        }
        throw new RuntimeException("Unknown implementation of " + Type.class.getCanonicalName()
                + ": " + javaType.getClass().getCanonicalName());
    }

    private Type addTypeVariable(TypeVariable<?> typeVariable) {
        if (this.typesMap.containsKey(typeVariable)) {
            return this.typesMap.get(typeVariable);
        }
        List<Type> cdiiUpperBounds = toCdiiTypeList(typeVariable.getBounds());
        String boundsString = upperBoundsToString(cdiiUpperBounds);
        Type result = new Type();
        result.setArray(false);
        result.setPackage("");
        result.setName(typeVariable.getName() + boundsString);
        this.typesMap.put(typeVariable, result);
        return result;
    }

    /**
     * This is workaround of poorly designed {@link Type} model class, TODO FIX
     * 
     * @param cdiiUpperBounds
     *            list of upper bounds
     * @return "" for empty input list; " extends " followed by ", " separated list of qualified
     *         type names
     */
    private static String upperBoundsToString(List<Type> cdiiUpperBounds) {
        if (cdiiUpperBounds.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (Type type : cdiiUpperBounds) {
            result.append(type.toString(true, true)).append(", ");
        }
        result.delete(result.length() - 2, result.length());
        return result.toString();
    }

    private Type addParameterizedType(ParameterizedType parameterizedType) {
        if (this.typesMap.containsKey(parameterizedType)) {
            return this.typesMap.get(parameterizedType);
        }
        Type cdiiRawType = addType(parameterizedType.getRawType());
        List<Type> cdiiTypeParameters = toCdiiTypeList(parameterizedType.getActualTypeArguments());
        Type result = new Type();
        result.setPackage(cdiiRawType.getPackage());
        result.setName(cdiiRawType.getName());
        result.setArray(cdiiRawType.isArray());
        result.setTypeParameters(cdiiTypeParameters);
        result.setMembers(cdiiRawType.getMembers());
        this.typesMap.put(parameterizedType, result);
        return result;
    }

    private List<Type> toCdiiTypeList(java.lang.reflect.Type[] javaTypesArray) {
        List<Type> result = new ArrayList<>();
        for (java.lang.reflect.Type javaType : javaTypesArray) {
            Type cdiiParam = addType(javaType);
            result.add(cdiiParam);
        }
        return result;
    }

    private Scope toScope(Class<? extends Annotation> scopeClass) {
        Scope scope = new Scope();
        String packageName = scopeClass.getPackage().getName();
        scope.setPackage(packageName);
        String name = getClassName(packageName, scopeClass.getCanonicalName());
        scope.setName(name);
        boolean isNormal = this.beanManager.isNormalScope(scopeClass);
        scope.setPseudo(!isNormal);
        return scope;
    }

    private Set<Qualifier> toCdiiQualifiers(Set<Annotation> javaQualifiers) {
        Set<Qualifier> result = new HashSet<>();
        for (Annotation javaQualifier : javaQualifiers) {
            Qualifier cdiiQualifier = toCdiiQualifier(javaQualifier);
            result.add(cdiiQualifier);
        }
        return result;
    }

    private Qualifier toCdiiQualifier(Annotation javaQualifier) {
        Qualifier result = new Qualifier();
        AnnotationType qualifierType = toAnnotationType(javaQualifier.annotationType());
        result.setType(qualifierType);
        Set<AnnotationMemeber> members = toAnnotationMembers(javaQualifier);
        result.setMembers(members);
        return result;
    }

    private Set<AnnotationMemeber> toAnnotationMembers(Annotation javaAnnotation) {
        HashSet<AnnotationMemeber> result = new HashSet<>();
        for (java.lang.reflect.Method memberMethod : javaAnnotation.annotationType()
                .getDeclaredMethods()) {
            AnnotationMemeber member = toAnnotationMember(memberMethod, javaAnnotation);
            result.add(member);
        }
        return result;
    }

    private AnnotationMemeber toAnnotationMember(java.lang.reflect.Method annotationMemberMethod,
            Annotation javaAnnotation) {
        AnnotationMemeber result = new AnnotationMemeber();
        result.setName(annotationMemberMethod.getName());
        result.setType(annotationMemberMethod.getReturnType().getCanonicalName());
        Object value = getAnnotationMemberValue(annotationMemberMethod, javaAnnotation);
        result.setValue(arrayAwareToString(value));
        return result;
    }

    private static Object getAnnotationMemberValue(java.lang.reflect.Method annotationMemberMethod,
            Annotation javaAnnotation) {
        try {
            return annotationMemberMethod.invoke(javaAnnotation);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            return "<unknown>";
        }
    }

    /**
     * calls object.toString() of Arrays.toString((proper cast) object);
     * 
     * @param object
     *            to get a string representation of
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

    private AnnotationType toAnnotationType(Class<? extends Annotation> javaAnnotationType) {
        AnnotationType result = new AnnotationType();
        String packageName = javaAnnotationType.getPackage().getName();
        result.setPackage(packageName);
        String annotationName = getClassName(packageName, javaAnnotationType.getCanonicalName());
        result.setName(annotationName);
        return result;
    }

    private Type addClass(Class<?> beanClass) {
        if (this.typesMap.containsKey(beanClass)) {
            return this.typesMap.get(beanClass);
        }
        Type result = new Type();
        result.setArray(beanClass.isArray());
        Class<?> realClass = beanClass.isArray() ? beanClass.getComponentType() : beanClass;
        String packageName = realClass.getPackage() == null ? "" : realClass.getPackage().getName();
        result.setPackage(packageName);
        result.setName(getClassName(packageName, realClass.getCanonicalName()));
        List<Type> typeParameters = Collections.emptyList();
        result.setTypeParameters(typeParameters);
        this.typesMap.put(beanClass, result);
        return result;
    }

    private static String getClassName(String packageName, String qualifiedName) {
        if (packageName.isEmpty()) {
            return qualifiedName;
        }
        String result = qualifiedName.substring(packageName.length() + 1);
        return result;
    }

    /*
     *  TODO filtering of nodes could be added to remove isolated graph components created purely 
     *  by application server
     *  
     *  hints:
     *  import org.jboss.as.server.deployment.Attachments;
     *  deploymentUnit.getAttachment(Attachments.CLASS_INDEX);
     *  deploymentUnit.getAttachment(Attachments.CLASS_PATH_ENTRIES);
     *  deploymentUnit.getAttachment(Attachments.CLASS_PATH_RESOURCE_ROOTS);
     *  deploymentUnit.getAttachment(Attachments.DEPLOYMENT_ROOT);
     *  deploymentUnit.getAttachment(Attachments.RESOURCE_ROOTS);
     */
    private Set<Bean<?>> removeInternalBeans(Set<Bean<?>> beans) {
        HashSet<Bean<?>> result = new HashSet<>();
        for (Bean<?> bean : beans) {
            result.add(bean);
        }
        return result;
    }
}
