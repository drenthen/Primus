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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ocl.OCL;
import org.eclipse.ocl.ecore.*;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.helper.OCLHelper;
//import org.eclipse.ocl.uml.UMLEnvironmentFactory;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
//import org.eclipse.uml2.diagram.component.action.PrimusAction1.MenuBuilder;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Stereotype;

public class PrimusActionAddPattern extends AbstractContributionItemProvider implements IProvider {

	public static final String MENU_PRIMUSACTION1 = "primus_action_add_pattern"; //$NON-NLS-1$
	
	public PrimusActionAddPattern() {
		// TODO Auto-generated constructor stub
	}
	
	public IMenuManager createMenuManager(String menuId, IWorkbenchPartDescriptor partDescriptor) {
		if (!MENU_PRIMUSACTION1.equals(menuId)) {
			return super.createMenuManager(menuId, partDescriptor);
		}
		MenuManager menuManager = new MenuManager("Add Pattern");
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
			String[] patterns;
			patterns = new String[11];
			patterns[0]="Wizard";
			/*patterns[3]="Layers";
			patterns[4]="Aggregation Cascade";
			patterns[5]="Composition Cascade";
			patterns[6]="Shield";
			patterns[7]="Typing";
			patterns[8]="Super Typing";
			patterns[9]="Virtual Connector";
			patterns[10]="Wizard";*/
			int i=0;
			for(i=0;i<1;i++) {
				org.eclipse.uml2.uml.Package package_;
				if(selected.getNotationView().getElement() instanceof org.eclipse.uml2.uml.Package) {
					package_ = (org.eclipse.uml2.uml.Package) selected.getNotationView().getElement();
				} else {
					package_=null;
				}
				AddPatternAction action = new AddPatternAction(myWorkbenchPart,getWorkbenchPage(),package_, element, patterns[i]);
				action.init();
				manager.add(action);
			}
		}

		private IWorkbenchPage getWorkbenchPage(){
			return myWorkbenchPart.getPartPage();
		}
	}

}
