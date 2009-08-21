package action;

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

public class PrimusActionPrimitivesOfType extends AbstractContributionItemProvider implements IProvider {

	public static final String MENU_PRIMUSACTION1 = "primus_action_primitives_of_type"; //$NON-NLS-1$
	
	public PrimusActionPrimitivesOfType() {
		// TODO Auto-generated constructor stub
	}
	
	public IMenuManager createMenuManager(String menuId, IWorkbenchPartDescriptor partDescriptor) {
		if (!MENU_PRIMUSACTION1.equals(menuId)) {
			return super.createMenuManager(menuId, partDescriptor);
		}
		MenuManager menuManager = new MenuManager("Validate Primitives of Type");
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

			String[] primitives;
			primitives = new String[11];
			primitives[0]="Callback";
			primitives[1]="Indirection";
			primitives[2]="Grouping";
			primitives[3]="Layers";
			primitives[4]="Aggregation Cascade";
			primitives[5]="Composition Cascade";
			primitives[6]="Shield";
			primitives[7]="Typing";
			primitives[8]="Super Typing";
			primitives[9]="Virtual Connector";
			primitives[10]="Callback detailed";
			int i=0;
			for(i=0;i<11;i++) {
				ValidatePrimitivesOfTypeAction action = new ValidatePrimitivesOfTypeAction(getWorkbenchPage(), element, primitives[i]);
				action.init();
				manager.add(action);
			}
		}

		private IWorkbenchPage getWorkbenchPage(){
			return myWorkbenchPart.getPartPage();
		}
	}

}
