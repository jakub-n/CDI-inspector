package cz.muni.fi.cdii.plugin.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.cdi.core.CDICoreNature;
import org.jboss.tools.cdi.core.CDIUtil;
import org.jboss.tools.cdi.core.IBean;
import org.jboss.tools.cdi.core.ICDIProject;
import org.jboss.tools.cdi.core.IClassBean;

public class IncpectCdiHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("Inspect CDI command");
		
		ISelection selection = HandlerUtil.getActiveMenuSelection(event);
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structSelection = (IStructuredSelection) selection;
			Object firstElement = structSelection.getFirstElement();
			System.out.println("first element type: " + firstElement.getClass().getName());
			if (firstElement instanceof IJavaElement) {
				final IJavaElement javaElement = (IJavaElement) firstElement;
				IProject project = javaElement.getResource().getProject();
				CDICoreNature nature = CDIUtil.getCDINatureWithProgress(project);
				ICDIProject cdiProject = nature.getDelegate();
				IBean[] beans = cdiProject.getBeans();
				printBeansDetais(beans);
				if (javaElement instanceof ICompilationUnit) {
					IClassBean beanClass = cdiProject.getBeanClass(((ICompilationUnit)javaElement).findPrimaryType());
					if (beanClass != null) {
						beanClass.open();
					}
				}
			} else {
				System.out.println("Selected element is of unexpected type: " + firstElement.getClass().getName());
			}
		} else {
			System.out.println("Selection is of IStructuredSelection type: " 
					+ selection.getClass().getName());
		}
		
		return null;
	}
	
	private void printBeansDetais(IBean[] beans) {
		for (IBean bean : beans) {
			System.out.println("----------");
			System.out.println("name: " + bean.getName());
			System.out.println("element name: " + bean.getElementName());
			System.out.println("sourcePath: " + bean.getSourcePath());
			System.out.println("types: " + bean.getAllTypes());
			System.out.println("annotations: " + bean.getAnnotations());
			System.out.println("scope: " + bean.getScope());
		}
	}
}
