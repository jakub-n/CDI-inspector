package cz.muni.fi.cdii.plugin.inspection2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.cdi.core.IBean;
import org.jboss.tools.cdi.core.IBuiltInBean;
import org.jboss.tools.cdi.core.ICDIProject;
import org.jboss.tools.cdi.core.IClassBean;
import org.jboss.tools.cdi.core.IInjectionPoint;
import org.jboss.tools.cdi.core.IProducer;
import org.jboss.tools.cdi.core.IProducerField;
import org.jboss.tools.cdi.core.IProducerMethod;
import org.jboss.tools.common.java.IParametedType;

import cz.muni.fi.cdii.common.model.Class;
import cz.muni.fi.cdii.common.model.Model;
import cz.muni.fi.cdii.common.model.Type;

public class Inspection {

    private Model model;
    private Set<IInjectionPoint> injectionPoints = new HashSet<>();
    private Set<Type> types = new HashSet<>();

    public Inspection(ICDIProject project) {
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

            System.out.println(">>> " + classBean.getBeanClass().getKey());
            try {
                System.out.println(">> "
                        + classBean.getBeanClass().getFullyQualifiedParameterizedName());
            } catch (JavaModelException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            for (IParametedType type : bean.getLegalTypes()) {
                System.out.println("> " + type.getSignature());
//                try {
//                    System.out.println(">j " + type.getType().getFullyQualifiedParameterizedName());
//                } catch (JavaModelException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
                System.out.println(">e " + extractType(type));
            }
            System.out.println("injectionPoints:");
            for (IInjectionPoint ip : classBean.getInjectionPoints()) {
                printInectionPointTypeDetail(ip);
            }

            System.out.println("producers");
            for (IProducer producer : classBean.getProducers()) {
                System.out.println("element name: " + producer.getElementName());
                try {
                    System.out.println(">> "
                            + producer.getBeanClass().getFullyQualifiedParameterizedName());
                } catch (JavaModelException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                for (IParametedType type : producer.getLegalTypes()) {
                    try {
                        System.out.println("> "
                                + type.getType().getFullyQualifiedParameterizedName());
                        System.out.println(">, " + type.getSignature());
                    } catch (JavaModelException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.out.println("reparsed: " + parseIType(type));
                }
            }

            addType(classBean);
            Collection<IInjectionPoint> beanInjectionPoints = bean.getInjectionPoints();
            this.injectionPoints.addAll(beanInjectionPoints);
            return;
        }
        if (bean instanceof IProducerField) {
            final IProducerField producerField = (IProducerField) bean;
            IClassBean classBean = producerField.getClassBean();
            addType(classBean);
            // addProducerField(bean);
            return;
        }
        if (bean instanceof IProducerMethod) {
            final IProducerMethod producerMethod = (IProducerMethod) bean;
            addType(producerMethod.getClassBean());
            // addProducerMethod(bean);
            return;
        }
        System.out.println("---------------------------------------------------------------------");
        System.out.println(bean + "; " + bean.getClass() + "; " + bean.getElementName() + "; "
                + bean.getName());
        System.out.println("type signature: "
                + bean.getAllTypes().toArray(new IParametedType[0])[0].getSignature());
        System.out.println("injection points: " + bean.getInjectionPoints());
        if (bean.getInjectionPoints().size() > 0) {
            System.out.println();
            for (IInjectionPoint ip : bean.getInjectionPoints()) {
                System.out.println(" > " + ip.getBeanName() + "; " + ip.getElementName() + "; "
                        + ip.getId() + "; " + ip.getMemberType() + "; " + ip.getType().getType());
            }
        }
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

    private String parseIType(IParametedType type) {
        String name = type.getSimpleName();
        String result = name;
        if (!type.getParameters().isEmpty()) {
            for (IParametedType param : type.getParameters()) {
            }
        }
        return result;
    }
    
    private Type extractType(IParametedType jbossType) {
        String fullyQualifiedName = jbossType.getType().getFullyQualifiedName();
        int lastDotIndex = fullyQualifiedName.lastIndexOf(".");
        String package_ = fullyQualifiedName.substring(0, lastDotIndex);
        String name = fullyQualifiedName.substring(lastDotIndex + 1);
        List<Type> typeParameters = new ArrayList<>();
        if (! jbossType.getParameters().isEmpty()) {
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

    private void addType(final IClassBean bean) {
        // TODO extract Type object creation into method

        // final Type type = new Type();
        // type.setSignature(bean.getBeanClass().getKey());
        // bean.getBeanClass().getFullyQualifiedParameterizedName();
        // type.get
        // this.beanTypeMap.put(bean, type);
    }

    private Class addClass(IType eclipseType) {
        // if (classes.get(key))
        String classKey = eclipseType.getKey();
        System.out.println(eclipseType + "\n\t" + classKey);

        printIType(eclipseType);

        try {
            // vypisuje i s typovymi parametry
            System.out.println(eclipseType.getFullyQualifiedParameterizedName());
        } catch (JavaModelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("=====");

        String javaName = eclipseType.getKey();
        Class result = new Class(javaName);
        result.setName(eclipseType.getElementName());
        // result.setPackage_(eclipseType.getPackageFragment());

        return null;
    }

    private void printIType(IType eclipseType) {
        try {
            System.out.println("element name " + eclipseType.getElementName());
            System.out.println("element type " + eclipseType.getElementType());
            System.out.println("flags " + eclipseType.getFlags());
            System.out.println("fq name" + eclipseType.getFullyQualifiedName());
            System.out.println("fully qualified parametrized name "
                    + eclipseType.getFullyQualifiedParameterizedName());
            System.out.println("fq parametrized name " + eclipseType.getHandleIdentifier());
            System.out.println("key " + eclipseType.getKey());
            // System.out.println("source " + eclipseType.getSource());
            System.out.println("super class name " + eclipseType.getSuperclassName());
            System.out.println("super class signature " + eclipseType.getSuperclassTypeSignature());
            System.out.println("type qualified name " + eclipseType.getTypeQualifiedName());
            System.out.println("declaring type " + eclipseType.getDeclaringType());
            System.out.println("fields " + Arrays.toString(eclipseType.getFields()));
            System.out.println("initializers " + Arrays.toString(eclipseType.getInitializers()));
            System.out.println("methods " + Arrays.toString(eclipseType.getMethods()));
            System.out.println("range name " + eclipseType.getNameRange().toString());
            // System.out.println("package fragment " +
            // eclipseType.getPackageFragment());
            System.out.println("parent " + eclipseType.getParent());
            System.out.println("path " + eclipseType.getPath());
            System.out.println("primary element " + eclipseType.getPrimaryElement());
            System.out.println("resource " + eclipseType.getResource());
            System.out.println("scheruling rule " + eclipseType.getSchedulingRule());
            System.out.println("superinterface name "
                    + Arrays.toString(eclipseType.getSuperInterfaceNames()));
            System.out.println("super interface type signatures"
                    + Arrays.toString(eclipseType.getSuperInterfaceTypeSignatures()));
            System.out
                    .println("type parameters" + Arrays.toString(eclipseType.getTypeParameters()));
            System.out.println("type parameter signatures"
                    + Arrays.toString(eclipseType.getTypeParameterSignatures()));
            System.out.println("type root " + eclipseType.getTypeRoot());
            System.out.println("is annotation " + eclipseType.isAnnotation());
            System.out.println("is anonymous " + eclipseType.isAnonymous());
            System.out.println("is binary " + eclipseType.isBinary());
            System.out.println("is class " + eclipseType.isClass());
            System.out.println("is enum " + eclipseType.isEnum());
            System.out.println("is interface " + eclipseType.isInterface());
            System.out.println("is member " + eclipseType.isMember());
            System.out.println("is read only " + eclipseType.isReadOnly());
            System.out.println("is resolved" + eclipseType.isResolved());
            System.out.println("is structure known " + eclipseType.isStructureKnown());
        } catch (JavaModelException ex) {
            ex.printStackTrace();
        }
    }
}
