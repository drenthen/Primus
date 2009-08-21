package action;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.AbstractEditCommandRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ocl.OCL;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.helper.OCLHelper;
import org.eclipse.ocl.util.Tuple;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
//import AddPatternAction.AddPatternRequest;
//import org.eclipse.uml2.diagram.clazz.part.CustomMessages;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Connector;
import org.eclipse.uml2.uml.ConnectorEnd;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Usage;

public class AddPatternAction extends DiagramAction {
	private IWorkbenchWindow window;
	private static final String EMPTY_NAME = "emptyname";

	private Element myElement;
	private String myPrimitiveType;
	private  org.eclipse.uml2.uml.Package myPackage;
	IWorkbenchPage workbenchPage;
	IWorkbenchPartDescriptor part;
	public AddPatternAction(IWorkbenchPartDescriptor part,IWorkbenchPage workbenchPage, org.eclipse.uml2.uml.Package package_, Element element, String primitiveType) {
		super(workbenchPage);
		myElement = element;
		myPackage = package_;
		myPrimitiveType = primitiveType;
		window = workbenchPage.getWorkbenchWindow();
		this.workbenchPage = workbenchPage;
		this.part = part;
	}

	@Override
	protected Request createTargetRequest() {
		return null;
	}

	@Override
	protected boolean isSelectionListener() {
		return true;
	}

	@Override
	protected Command getCommand() {
		final IGraphicalEditPart elementEditPart = getElementEditPart();
		if (elementEditPart == null) {
			return UnexecutableCommand.INSTANCE;
		}
		TransactionalEditingDomain editingDomain = elementEditPart.getEditingDomain();
		AddPrimitiveRequest request = new AddPrimitiveRequest(editingDomain);
		request.setElement(myElement);
		request.setPrimitiveType(myPrimitiveType);
		CompoundCommand command = new CompoundCommand();
		command.add(new ICommandProxy(new AddPrimitiveCommand(myPackage,request,myPrimitiveType,myElement)));
		command.add(new Command() {

			@Override
			public void execute() {
				// there are no clear ways for parser to locate this change and
				// update label
				for (Object nextChildEP : elementEditPart.getChildren()){
					if (nextChildEP instanceof ITextAwareEditPart){
						((ITextAwareEditPart)nextChildEP).refresh();
					}
				}
			}
		});
		return command;
	}

	@Override
	public boolean isEnabled() {
		return getElementEditPart() != null;
	}

	public void refresh() {
		super.refresh();
		setText(myPrimitiveType);
	}

	private IGraphicalEditPart getElementEditPart() {
		for (Object next : getSelectedObjects()) {
			if (next instanceof IGraphicalEditPart) {
				IGraphicalEditPart elementEditPart = (IGraphicalEditPart) next;
				return elementEditPart;
			}
		}
		return null;
	}

	private class AddPrimitiveCommand extends EditElementCommand {

		org.eclipse.uml2.uml.Package myPackage;
		org.eclipse.uml2.uml.Element currentElement;
		String myPrimitiveType;
		protected AddPrimitiveCommand(org.eclipse.uml2.uml.Package package_, IEditCommandRequest request,String primitiveType,Element element) {
			super("AddPrimitiveCommand",package_, request);
			myPackage = package_;
			myPrimitiveType = primitiveType;
			currentElement = element;
		}
		private String getUniqueNameinComponent(Component comp, String name, int type) {
			boolean carryOn=true;
			Component currComponent = comp;
			String compName1=name;
			int i=0;
			if(type>2 || type<1) {
				carryOn=false;
			}
			while(carryOn) {
				i++;
				String genCompName1=compName1+Integer.toString(i);
				if(type==1) {		
					boolean found=false;
					for (org.eclipse.uml2.uml.Property property : currComponent.getAttributes()){
						if (property.getName().equals(genCompName1)) {
							found=true;
						}
					}
					if(found==false) {
						carryOn=false;
					}
				} else if(type==2) {		
					boolean found=false;
					for (Port p : currComponent.getOwnedPorts()){
						if (p.getName().equals(genCompName1)) {
							found=true;
						}
					}
					if(found==false) {
						carryOn=false;
					}
				}
			}
			return name+Integer.toString(i);
		}
		private String getUniqueName(String name, int type) {
			//type int =1 if component =2 if class
			boolean carryOn=true;
			String compName1=name;
			int i=0;
			if(type>6 || type<1) {
				carryOn=false;
			}
			while(carryOn) {
				i++;
				String genCompName1=compName1+Integer.toString(i);
				if(type==1) {
					Component comp1 = (Component) myPackage.getMember(genCompName1);
					if(comp1==null) {
						carryOn=false;
					}
				} else if(type==2) {
					Class c1 = (Class) myPackage.getMember(genCompName1);
					if(c1==null) {
						carryOn=false;
					}
				} else if(type==3) {
					Interface i1 = (Interface) myPackage.getMember(genCompName1);
					if(i1==null) {
						carryOn=false;
					}
				} else if(type==4) {
					org.eclipse.uml2.uml.Package p1 = (org.eclipse.uml2.uml.Package) myPackage.getMember(genCompName1);
					if(p1==null) {
						carryOn=false;
					}
				} else if(type==5) {
					org.eclipse.uml2.uml.Port p1 = (org.eclipse.uml2.uml.Port) myPackage.getMember(genCompName1);
					if(p1==null) {
						carryOn=false;
					}
				} else if(type==6) {
					org.eclipse.uml2.uml.Association a1 = (org.eclipse.uml2.uml.Association) myPackage.getMember(genCompName1);
					if(a1==null) {
						carryOn=false;
					}
				}
			}
			return name+Integer.toString(i);
		}
		
		@Override
		protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
			
		 if(myPrimitiveType=="Wizard") {
			AddPatternWizard wizard = new AddPatternWizard(myPackage);
			ISelection selection;
			IWorkbench workbench=null;
			IStructuredSelection sel=null;
			//if ((selection instanceof IStructuredSelection) || (selection == null))
			wizard.init(workbench, (IStructuredSelection)sel);
				
			// Instantiates the wizard container with the wizard and opens it
			//WizardDialog dialog = new WizardDialog( part.getSite().getShell(), wizard);
			
			WizardDialog dialog = new WizardDialog( (Shell) workbench, wizard);
			dialog.create();
			dialog.open();
			
		}
			return CommandResult.newOKCommandResult();
		}
	}
	public static class AddPrimitiveRequest extends AbstractEditCommandRequest {

		private Element myElement;
		private String myPrimitiveType;
		public AddPrimitiveRequest(TransactionalEditingDomain domain) {
			super(domain);
		}

		public void setElement(Element element) {
			myElement = element;
		}
		public Element getElement() {
			return myElement;
		}

		public Object getEditHelperContext() {
			// TODO Auto-generated method stub
			return null;
		}
		public void setPrimitiveType(String primitiveType) {
			myPrimitiveType = primitiveType;
		}
		public String getPrimitiveType() {
			return myPrimitiveType;
		}
	}

}

