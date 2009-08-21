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
import org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.AbstractEditCommandRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ocl.OCL;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.helper.OCLHelper;
import org.eclipse.ocl.util.Tuple;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import action.AddComponentToPackageAction.AddPrimitiveRequest;
//import org.eclipse.uml2.diagram.clazz.part.CustomMessages;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Connector;
import org.eclipse.uml2.uml.ConnectorEnd;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Usage;

public class AddComponentToPackageAction extends DiagramAction {
	private IWorkbenchWindow window;
	private static final String EMPTY_NAME = "emptyname";

	private Element myElement;
	private  org.eclipse.uml2.uml.Package myPackage;
	public org.eclipse.uml2.uml.Stereotype myStereotype;
	public AddComponentToPackageAction(IWorkbenchPage workbenchPage, org.eclipse.uml2.uml.Package package_, Element element, Stereotype s) {
		super(workbenchPage);
		myElement = element;
		myPackage = package_;
		myStereotype = s;
		window = workbenchPage.getWorkbenchWindow();
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
		CompoundCommand command = new CompoundCommand();
		command.add(new ICommandProxy(new AddPrimitiveCommand(myPackage,request,myStereotype)));
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
		setText(myPackage.getName());
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
		org.eclipse.uml2.uml.Stereotype myStereotype;
		protected AddPrimitiveCommand(org.eclipse.uml2.uml.Package package_, IEditCommandRequest request, Stereotype s) {
			super("AddPrimitiveCommand",package_, request);
			myPackage = package_;
			myStereotype = s;
		}

		@Override
		protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
			PackageableElement pe= (PackageableElement) myElement;
			myPackage.createElementImport(pe);
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

