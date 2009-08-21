package action;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ocl.OCL;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.helper.OCLHelper;
import org.eclipse.ocl.util.Tuple;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import action.ValidatePrimitiveAction.ValidatePrimitiveRequest;
//import org.eclipse.uml2.diagram.clazz.part.CustomMessages;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Stereotype;

public class ValidatePrimitiveAction extends DiagramAction {
	private IWorkbenchWindow window;
	private static final String EMPTY_NAME = "emptyname";

	private Element myElement;
	private String myPrimitiveType;
	public ValidatePrimitiveAction(IWorkbenchPage workbenchPage, Element element, String primitiveType) {
		super(workbenchPage);
		myElement = element;
		myPrimitiveType = primitiveType;
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
		ValidatePrimitiveRequest request = new ValidatePrimitiveRequest(editingDomain);
		request.setElement(myElement);
		request.setPrimitiveType(myPrimitiveType);
		CompoundCommand command = new CompoundCommand();
		command.add(new ICommandProxy(new PrimitiveCommand(request)));
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

	private class PrimitiveCommand extends EditElementCommand {

		protected PrimitiveCommand(ValidatePrimitiveRequest request) {
			super("validate", request.getElement(), request);
		}

		@Override
		protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
			ValidatePrimitiveRequest request = (ValidatePrimitiveRequest) getRequest();
			Element element = request.getElement();
			String primitiveType = request.getPrimitiveType();
			OCLCheck check = new OCLCheck(primitiveType, element);
			boolean success;
			String message="Primitive not found";
			success = check.validatePrimitive(primitiveType);
			if (success) {
				message="Primitive found";
			}

			
			
			MessageDialog.openInformation(
					window.getShell(),
					"Primus Pop-up",
					message);
			
			return CommandResult.newOKCommandResult(request.getElement());
		}
	}
	public static class ValidatePrimitiveRequest extends AbstractEditCommandRequest {

		private Element myElement;
		private String myPrimitiveType;
		public ValidatePrimitiveRequest(TransactionalEditingDomain domain) {
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

