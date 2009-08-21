package action;

import java.util.*;
import java.awt.List;
import java.util.Iterator;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ocl.parser.ParserException;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.AbstractContributionItemProvider;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ocl.OCL;
import org.eclipse.ocl.ecore.*;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.helper.OCLHelper;
//import org.eclipse.ocl.uml.UMLEnvironmentFactory;
import org.eclipse.ui.IWorkbenchPage;
//import org.eclipse.uml2.diagram.component.action.PrimusAction1.MenuBuilder;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Stereotype;

public class PrimusActionAddPrimitiveToPackage extends AbstractContributionItemProvider implements IProvider {

	public static final String MENU_PRIMUSACTION1 = "primus_action_add_component_to_package"; //$NON-NLS-1$
	
	public PrimusActionAddPrimitiveToPackage() {
		// TODO Auto-generated constructor stub
	}
	
	public IMenuManager createMenuManager(String menuId, IWorkbenchPartDescriptor partDescriptor) {
		if (!MENU_PRIMUSACTION1.equals(menuId)) {
			return super.createMenuManager(menuId, partDescriptor);
		}
		MenuManager menuManager = new MenuManager("Add to Group/Layer");
		MenuBuilder builder = new MenuBuilder(partDescriptor);
		//XXX: build initial content -- otherwise menu is never shown
		builder.buildMenu(menuManager);

		menuManager.addMenuListener(builder);
		return menuManager;
	}

	public class MenuBuilder implements IMenuListener {
		private final IWorkbenchPartDescriptor myWorkbenchPart;

		public MenuBuilder(IWorkbenchPartDescriptor workbenchPart){
			myWorkbenchPart = workbenchPart;
		}
		
		public void menuAboutToShow(IMenuManager manager) {
			buildMenu(manager);
		}
		
		public void buildMenu(IMenuManager manager){
			manager.removeAll();
			IGraphicalEditPart selected = (IGraphicalEditPart) getSelectedObject(myWorkbenchPart);
			EObject selectedElement = selected.getNotationView().getElement();
			if (false == selectedElement instanceof Element) {
				return;
			}
			Element element = (Element)selectedElement;

			if( element instanceof Component) {
				org.eclipse.uml2.uml.Package mypackage;
				Element el = (org.eclipse.uml2.uml.Element) selected.getNotationView().getElement();
				mypackage = (org.eclipse.uml2.uml.Package) el.getNearestPackage();
				
				EList<org.eclipse.uml2.uml.Package> fulllist = (EList<org.eclipse.uml2.uml.Package>) mypackage.getNestedPackages();
				
				for (org.eclipse.uml2.uml.Package p : fulllist) {
					Stereotype s = p.getAppliedStereotype("Group");
					
					EList<Stereotype> stereotypes = p.getAppliedStereotypes();
					for (Stereotype stereotype: stereotypes) {
						if(stereotype.getName().equals("Group")) {
							AddComponentToPackageAction action = new AddComponentToPackageAction(getWorkbenchPage(),p, element,s);
							action.init();
							manager.add(action);
						} else if(stereotype.getName().equals("Layer")) {
							AddComponentToPackageAction action = new AddComponentToPackageAction(getWorkbenchPage(),p, element,s);
							action.init();
							manager.add(action);
						}
					}
				}
			}
		}

		private IWorkbenchPage getWorkbenchPage(){
			return myWorkbenchPart.getPartPage();
		}
	}

}
