package cz.muni.fi.cdii.plugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.jdt.core.IMethod;
import org.jboss.tools.cdi.core.IInjectionPoint;
import org.jboss.tools.cdi.internal.core.impl.ClassBean;
import org.jboss.tools.cdi.internal.core.impl.ProducerMethod;

import cz.muni.fi.cdii.plugin.model.IBean;

public class LocalBean implements IBean {

	private org.jboss.tools.cdi.core.IBean jbossBean;

	public LocalBean(org.jboss.tools.cdi.core.IBean jbossBean) {
		this.jbossBean = jbossBean;
	}

	@Override
	public String getQalifiedName() {
		if (this.jbossBean instanceof ProducerMethod) {
			final ProducerMethod producerMethod = (ProducerMethod) this.jbossBean;
			final IMethod method = producerMethod.getDefinition().getMethod();
			return //producerMethod.getBeanClass().getFullyQualifiedName()
					"node type: " + this.jbossBean.getClass().getSimpleName()
					+ "\nreturn type: " + producerMethod.getMemberType().getSignature()
					+ "\nmethod name: " + producerMethod.getDefinition().getTypeDefinition().getQualifiedName()
						+ " " + method.getElementName()
					+ "\nparams: " + Arrays.toString(method.getParameterTypes());
//					+ " " + producerMethod.getElementName() 
//					+ "\n" + producerMethod.getDefinition().getTypeDefinition().getQualifiedName()
//					+ " " + method.getElementName()
//					+ "(" + Arrays.toString(method.getParameterTypes()) + ")";
		}
		if (this.jbossBean.getClass().equals(ClassBean.class)) {
			return "node type: " + this.jbossBean.getClass().getSimpleName()
					+ "\nclass: " + this.jbossBean.getBeanClass().getFullyQualifiedName()
					+ "\ninjection points:" + injectionPointsToString(this.jbossBean);
		}
		return this.jbossBean.getBeanClass().getFullyQualifiedName() + "\n" 
				+ this.jbossBean.getClass().getSimpleName();
	}

	private String injectionPointsToString(org.jboss.tools.cdi.core.IBean bean) {
		StringBuilder resultBuilder = new StringBuilder();
		for (IInjectionPoint point : bean.getInjectionPoints()) {
			resultBuilder.append("\n - ")
				.append(point.getType().getSignature())
				.append(" ").append(point.getElementName());
		}
		return resultBuilder.toString();
	}

	@Override
	public Collection<IBean> getInjectedIntoNodes() {
		return Collections.emptySet();
	}

	@Override
	public Collection<IBean> getInjectedNodes() {
		return Collections.emptySet();
	}

	@Override
	public boolean isOpenable() {
		return true;
	}

	@Override
	public void open() {
		this.jbossBean.open();
	}

	@Override
	public String toString() {
		return "{" + this.getQalifiedName() + "}";
	}

}
