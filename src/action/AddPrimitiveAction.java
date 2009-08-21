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
import action.AddPrimitiveAction.AddPrimitiveRequest;
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

public class AddPrimitiveAction extends DiagramAction {
	private IWorkbenchWindow window;
	private static final String EMPTY_NAME = "emptyname";

	private Element myElement;
	private String myPrimitiveType;
	private  org.eclipse.uml2.uml.Package myPackage;
	IWorkbenchPage workbenchPage;
	IWorkbenchPartDescriptor part;
	public AddPrimitiveAction(IWorkbenchPartDescriptor part,IWorkbenchPage workbenchPage, org.eclipse.uml2.uml.Package package_, Element element, String primitiveType) {
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
			if(myPrimitiveType=="Callback") {
				Component comp1,comp2;
				if( currentElement instanceof Component) {
					comp1 = (Component) currentElement;
					myPackage = (org.eclipse.uml2.uml.Package) comp1.getNearestPackage();
					comp2 = (Component) myPackage.createOwnedType(getUniqueName("Callback",1), UMLPackage.eINSTANCE.getComponent());
				} else {
				//create components
				
					comp1 = (Component) myPackage.createOwnedType(getUniqueName("Caller",1), UMLPackage.eINSTANCE.getComponent());
					comp2 = (Component) myPackage.createOwnedType(getUniqueName("Callback",1), UMLPackage.eINSTANCE.getComponent());
				}
				//create ports
				comp1.createOwnedPort("e", null);
				comp2.createOwnedPort("c", null);

				Class class1 = myPackage.createOwnedClass(getUniqueName("Caller_e",2), true); 
				Port p1 = (Port) comp1.getOwnedPort("e", null);			
				p1.setType(class1);
				p1.setIsComposite(true);

				EList<Stereotype> stereotypesPort1 = p1.getApplicableStereotypes();
				for (Stereotype stereotype: stereotypesPort1) {
					if(stereotype.getName().equals("EventPort")) {
						p1.applyStereotype(stereotype);
					}
				}
				
				Interface myInterface1 = (Interface) myPackage.createOwnedType(getUniqueName("ObserveEvent",3), UMLPackage.eINSTANCE.getInterface()); //create Provided Interface, //code taken from org.eclipse.uml2.diagram.clazz.edit.commands.InterfaceRealizationCreateCommand#doDefaultElementCreation
				InterfaceRealization newElement = UMLFactory.eINSTANCE.createInterfaceRealization();
				newElement.setImplementingClassifier(class1);
				newElement.setContract(myInterface1);
				class1.getInterfaceRealizations().add(newElement);

				Interface myInterface2 = (Interface) myPackage.createOwnedType(getUniqueName("Update",3), UMLPackage.eINSTANCE.getInterface());
				Usage newUsage = UMLFactory.eINSTANCE.createUsage();
				myPackage.getPackagedElements().add(newUsage);
				newUsage.getClients().add(class1);
				newUsage.getSuppliers().add(myInterface2);

				//Create provided/required on callback component(B)
				Class class2 = myPackage.createOwnedClass(getUniqueName("Callback_c",2), true); 
				Port p2 = (Port) comp2.getOwnedPort("c", null);			
				p2.setType(class2);
				p2.setIsComposite(true);

				EList<Stereotype> stereotypesPort2 = p2.getApplicableStereotypes();
				for (Stereotype stereotype: stereotypesPort2) {
					if(stereotype.getName().equals("CallbackPort")) {
						p2.applyStereotype(stereotype);
					}
				}

				InterfaceRealization newElement1 = UMLFactory.eINSTANCE.createInterfaceRealization();
				newElement1.setImplementingClassifier(class2);
				newElement1.setContract(myInterface2);
				class2.getInterfaceRealizations().add(newElement1);

				Usage newUsage1 = UMLFactory.eINSTANCE.createUsage();
				myPackage.getPackagedElements().add(newUsage1);
				newUsage1.getClients().add(class2);
				newUsage1.getSuppliers().add(myInterface1);

				//apply Stereotype interface ObserveEvent
				EList<Stereotype> stereotypes = myInterface1.getApplicableStereotypes();
				for (Stereotype stereotype: stereotypes) {
					if(stereotype.getName().equals("IEvent")) {
						myInterface1.applyStereotype(stereotype);
					}
				}

				//apply Stereotype interface ObserveEvent
				EList<Stereotype> stereotypes1 = myInterface2.getApplicableStereotypes();
				for (Stereotype stereotype: stereotypes1) {
					if(stereotype.getName().equals("ICallback")) {
						myInterface2.applyStereotype(stereotype);
					}
				}
				Connector connector;
				connector = comp1.createOwnedConnector(null);
				ConnectorEnd sourceEnd = UMLFactory.eINSTANCE.createConnectorEnd();
				ConnectorEnd targetEnd = UMLFactory.eINSTANCE.createConnectorEnd();
				sourceEnd.setPartWithPort(p1);
				targetEnd.setPartWithPort(p2);
				//see ConnectorEndsConvention -- source first, than target
				connector.getEnds().add(sourceEnd);
				connector.getEnds().add(targetEnd);

				org.eclipse.uml2.uml.Property a1 = comp1.createOwnedAttribute("foo", comp1);
				org.eclipse.uml2.uml.Property a2 = comp1.createOwnedAttribute("bar", comp2);
				sourceEnd.setRole(a1);
				targetEnd.setRole(a2);
				
				EList<Stereotype> stereotypesConn = connector.getApplicableStereotypes();
				for (Stereotype stereotype: stereotypesConn) {
					if(stereotype.getName().equals("Callback")) {
						connector.applyStereotype(stereotype);
					}
				}
			} else if(myPrimitiveType=="Indirection") {
				//create components
				Component comp1 = (Component)myPackage.createOwnedType(getUniqueName("Indirector",1), UMLPackage.eINSTANCE.getComponent());
				Component comp2 = (Component)myPackage.createOwnedType(getUniqueName("IndirectionTarget",1), UMLPackage.eINSTANCE.getComponent());
				Component comp3 = (Component)myPackage.createOwnedType(getUniqueName("IndirectionClient",1), UMLPackage.eINSTANCE.getComponent());
				//create ports
				comp1.createOwnedPort("Indirector_ip", null); 
				comp2.createOwnedPort("IndirectionTarget_itp", null);
				comp3.createOwnedPort("clientport", null);

				//Create provided/required on component(A)
				Class class1 = myPackage.createOwnedClass(getUniqueName("Indirector_ip",2), true);
				Port p1 = (Port) comp1.getOwnedPort("Indirector_ip", null);	
				p1.setType(class1);
				p1.setIsComposite(true);
				EList<Stereotype> stereotypesPort1 = p1.getApplicableStereotypes();
				for (Stereotype stereotype: stereotypesPort1) {
					if(stereotype.getName().equals("IndirectionPort")) {
						p1.applyStereotype(stereotype);
					}
				}
				Interface myInterface1 = (Interface) myPackage.createOwnedType(getUniqueName("IndirectedOp",3), UMLPackage.eINSTANCE.getInterface()); //create Provided Interface, //code taken from org.eclipse.uml2.diagram.clazz.edit.commands.InterfaceRealizationCreateCommand#doDefaultElementCreation
				InterfaceRealization newElement = UMLFactory.eINSTANCE.createInterfaceRealization();
				newElement.setImplementingClassifier(class1);
				newElement.setContract(myInterface1);
				class1.getInterfaceRealizations().add(newElement);

				Interface myInterface2 = (Interface) myPackage.createOwnedType(getUniqueName("TargetOp",3), UMLPackage.eINSTANCE.getInterface());
				Usage newUsage = UMLFactory.eINSTANCE.createUsage();
				myPackage.getPackagedElements().add(newUsage);
				newUsage.getClients().add(class1);
				newUsage.getSuppliers().add(myInterface2);

				//Create provided/required on component(B)
				Class class2 = myPackage.createOwnedClass(getUniqueName("Class",2), true); 
				Port p2 = (Port) comp2.getOwnedPort("IndirectionTarget_itp", null);			
				p2.setType(class2);
				p2.setIsComposite(true);

				EList<Stereotype> stereotypesPort2 = p2.getApplicableStereotypes();
				for (Stereotype stereotype: stereotypesPort2) {
					if(stereotype.getName().equals("IndirectionTargetPort")) {
						p2.applyStereotype(stereotype);
					}
				}

				InterfaceRealization newElement1 = UMLFactory.eINSTANCE.createInterfaceRealization();
				newElement1.setImplementingClassifier(class2);
				newElement1.setContract(myInterface2);
				class2.getInterfaceRealizations().add(newElement1);

				//Create provided/required on component(Client)
				Class class3 = myPackage.createOwnedClass(getUniqueName("Client_clientport",2), true); 
				Port p3 = (Port) comp3.getOwnedPort("clientport", null);			
				p3.setType(class3);
				p3.setIsComposite(true);
				Usage newUsage1 = UMLFactory.eINSTANCE.createUsage();
				myPackage.getPackagedElements().add(newUsage1);
				newUsage1.getClients().add(class3);
				newUsage1.getSuppliers().add(myInterface1);

				//apply Stereotype interface IndirectedOp
				EList<Stereotype> stereotypesInterface1 = myInterface1.getApplicableStereotypes();
				for (Stereotype stereotype: stereotypesInterface1) {
					if(stereotype.getName().equals("Indirector")) {
						myInterface1.applyStereotype(stereotype);
					}
				}
				//apply Stereotype interface Target
				EList<Stereotype> stereotypesInterface2 = myInterface1.getApplicableStereotypes();
				for (Stereotype stereotype: stereotypesInterface2) {
					if(stereotype.getName().equals("ITarget")) {
						myInterface2.applyStereotype(stereotype);
					}
				}
				Connector connector;
				connector = comp1.createOwnedConnector(null);
				ConnectorEnd sourceEnd = UMLFactory.eINSTANCE.createConnectorEnd();
				ConnectorEnd targetEnd = UMLFactory.eINSTANCE.createConnectorEnd();

				//see ConnectorEndsConvention -- source first, than target
				connector.getEnds().add(sourceEnd);
				connector.getEnds().add(targetEnd);

				sourceEnd.setPartWithPort(p1);
				targetEnd.setPartWithPort(p2);
				
				org.eclipse.uml2.uml.Property a1 = comp1.createOwnedAttribute("foo", comp1);
				org.eclipse.uml2.uml.Property a2 = comp1.createOwnedAttribute("bar", comp2);
				sourceEnd.setRole(a1);
				targetEnd.setRole(a2);
				
				EList<Stereotype> stereotypesConn = connector.getApplicableStereotypes();
				for (Stereotype stereotype: stereotypesConn) {
					if(stereotype.getName().equals("Indirection")) {
						connector.applyStereotype(stereotype);
					}
				}
				
				Connector connector1;
				connector1 = comp3.createOwnedConnector(null);
				ConnectorEnd sourceEnd1 = UMLFactory.eINSTANCE.createConnectorEnd();
				ConnectorEnd targetEnd1 = UMLFactory.eINSTANCE.createConnectorEnd();

				//see ConnectorEndsConvention -- source first, than target
				connector1.getEnds().add(sourceEnd1);
				connector1.getEnds().add(targetEnd1);

				sourceEnd1.setPartWithPort(p3);
				targetEnd1.setPartWithPort(p1);
				
				org.eclipse.uml2.uml.Property a3 = comp3.createOwnedAttribute("foo", comp3);
				org.eclipse.uml2.uml.Property a4 = comp3.createOwnedAttribute("bar", comp1);
				sourceEnd1.setRole(a3);
				targetEnd1.setRole(a4);
			} else if(myPrimitiveType=="Grouping") {
				Element element;
				element = (org.eclipse.uml2.uml.Package) myPackage.createNestedPackage(getUniqueName("Group",4));
				EList<Stereotype> stereotypes = element.getApplicableStereotypes();
				for (Stereotype stereotype: stereotypes) {
					if(stereotype.getName().equals("Group")) {
						element.applyStereotype(stereotype);
					}
				}
			} else if(myPrimitiveType=="Layers") {
				Element element;
				element = (org.eclipse.uml2.uml.Package) myPackage.createNestedPackage(getUniqueName("Layer",4));
				EList<Stereotype> stereotypes = element.getApplicableStereotypes();
				for (Stereotype stereotype: stereotypes) {
					if(stereotype.getName().equals("Layer")) {
						element.applyStereotype(stereotype);
					}
				}
			} else if(myPrimitiveType=="Aggregation Cascade") {
				if( currentElement instanceof Component) {
					Component comp = (Component) currentElement;
					myPackage = (org.eclipse.uml2.uml.Package) comp.getNearestPackage();
					Component comp1 = (Component) myPackage.createOwnedType(getUniqueName("Agg",1), UMLPackage.eINSTANCE.getComponent());
					Port p1 = (Port) comp.createOwnedPort(getUniqueName("PortAgg",5), null);
					Port p2 = (Port) comp1.createOwnedPort("PortAgg", null);
					Class class1 = myPackage.createOwnedClass(getUniqueName("Class",2), true);	
					
					p1.setType(class1);
					p1.setIsComposite(true);
					Class class2 = myPackage.createOwnedClass(getUniqueName("Agg_PortAgg",2), true); 		
					p2.setType(class2);
					p2.setIsComposite(true);

					EList<Generalization> gen = comp.getGeneralizations();
					Generalization generalization = gen.get(0);
					Component genComp = (Component)generalization.getGeneral();
					comp1.createGeneralization(genComp);

					Connector connector;
					connector = comp.createOwnedConnector(null);
					ConnectorEnd sourceEnd = UMLFactory.eINSTANCE.createConnectorEnd();
					ConnectorEnd targetEnd = UMLFactory.eINSTANCE.createConnectorEnd();

					//see ConnectorEndsConvention -- source first, than target
					connector.getEnds().add(sourceEnd);
					connector.getEnds().add(targetEnd);
	
					org.eclipse.uml2.uml.Property a1 = comp.createOwnedAttribute("foo", comp);
					org.eclipse.uml2.uml.Property a2 = comp.createOwnedAttribute("bar", comp1);
					sourceEnd.setRole(a1);
					targetEnd.setRole(a2);

					sourceEnd.setPartWithPort(p1);
					targetEnd.setPartWithPort(p2);
					
					Interface myInterface1 = (Interface) myPackage.createOwnedType(getUniqueName("AggInt",3), UMLPackage.eINSTANCE.getInterface());
					Usage newUsage = UMLFactory.eINSTANCE.createUsage();
					myPackage.getPackagedElements().add(newUsage);
					newUsage.getClients().add(class1);
					newUsage.getSuppliers().add(myInterface1);

					InterfaceRealization newElement = UMLFactory.eINSTANCE.createInterfaceRealization();
					newElement.setImplementingClassifier(class2);
					newElement.setContract(myInterface1);
					class2.getInterfaceRealizations().add(newElement);

					EList<Stereotype> stereotypes = connector.getApplicableStereotypes();
					for (Stereotype stereotype: stereotypes) {
						if(stereotype.getName().equals("AggregationCascade")) {
							connector.applyStereotype(stereotype);
						}
					}
					org.eclipse.uml2.uml.Association association = (org.eclipse.uml2.uml.Association) myPackage.createOwnedType(getUniqueName("aggbetweencomp",6),UMLPackage.eINSTANCE.getAssociation());
					org.eclipse.uml2.uml.Property prop1 = comp.createOwnedAttribute("prop1", comp1);
					org.eclipse.uml2.uml.Property prop2 = comp1.createOwnedAttribute("prop", comp);
					prop1.setAssociation(association);
					prop1.setAggregation(AggregationKind.SHARED_LITERAL);
					
					prop2.setAssociation(association);
					
				} else {
					//create components
					Component comp0 = (Component) myPackage.createOwnedType(getUniqueName("MainAgg",1), UMLPackage.eINSTANCE.getComponent());
					Component comp1 = (Component) myPackage.createOwnedType(getUniqueName("Agg",1), UMLPackage.eINSTANCE.getComponent());
					Component comp2 = (Component) myPackage.createOwnedType(getUniqueName("Agg",1), UMLPackage.eINSTANCE.getComponent());
					
					//create ports
					Port p1 = (Port) comp1.createOwnedPort("PortAgg", null); 
					Port p2 = (Port) comp2.createOwnedPort("PortAgg", null); 
					
					Class class1 = myPackage.createOwnedClass(getUniqueName("Agg_PortAgg",2), true);	
					p1.setType(class1);
					p1.setIsComposite(true);
					
					Class class2 = myPackage.createOwnedClass(getUniqueName("Agg_PortAgg",2), true); 		
					p2.setType(class2);
					p2.setIsComposite(true);
					
					Interface myInterface1 = (Interface) myPackage.createOwnedType(getUniqueName("AggInt",3), UMLPackage.eINSTANCE.getInterface());
					Usage newUsage = UMLFactory.eINSTANCE.createUsage();
					myPackage.getPackagedElements().add(newUsage);
					newUsage.getClients().add(class1);
					newUsage.getSuppliers().add(myInterface1);
					
					InterfaceRealization newElement = UMLFactory.eINSTANCE.createInterfaceRealization();
					newElement.setImplementingClassifier(class2);
					newElement.setContract(myInterface1);
					class2.getInterfaceRealizations().add(newElement);
					
					Connector connector;
					connector = comp1.createOwnedConnector(null);
					ConnectorEnd sourceEnd = UMLFactory.eINSTANCE.createConnectorEnd();
					ConnectorEnd targetEnd = UMLFactory.eINSTANCE.createConnectorEnd();
	
					//see ConnectorEndsConvention -- source first, than target
					connector.getEnds().add(sourceEnd);
					connector.getEnds().add(targetEnd);
	
					org.eclipse.uml2.uml.Property a1 = comp1.createOwnedAttribute("foo", comp1);
					org.eclipse.uml2.uml.Property a2 = comp1.createOwnedAttribute("bar", comp2);
					sourceEnd.setRole(a1);
					targetEnd.setRole(a2);
					sourceEnd.setPartWithPort(p1);
					targetEnd.setPartWithPort(p2);
					
					EList<Stereotype> stereotypes = connector.getApplicableStereotypes();
					for (Stereotype stereotype: stereotypes) {
						if(stereotype.getName().equals("AggregationCascade")) {
							connector.applyStereotype(stereotype);
						}
					}
					comp1.createGeneralization(comp0);
					comp2.createGeneralization(comp0);
					
					org.eclipse.uml2.uml.Association association = (org.eclipse.uml2.uml.Association) myPackage.createOwnedType(getUniqueName("aggbetweencomp",6),UMLPackage.eINSTANCE.getAssociation());
					org.eclipse.uml2.uml.Property prop1 = comp1.createOwnedAttribute("prop", comp2);
					org.eclipse.uml2.uml.Property prop2 = comp2.createOwnedAttribute("prop", comp1);
					prop1.setAssociation(association);
					prop1.setAggregation(AggregationKind.SHARED_LITERAL);
					
					prop2.setAssociation(association);
				}
			} else if(myPrimitiveType=="Composition Cascade") {
				if( currentElement instanceof Component) {

					Component comp = (Component) currentElement;
					myPackage = (org.eclipse.uml2.uml.Package) comp.getNearestPackage();

					Component comp2 = (Component) myPackage.createOwnedType(getUniqueName("Leaf",1), UMLPackage.eINSTANCE.getComponent());
					Component comp3 = (Component)myPackage.createOwnedType(getUniqueName("Composite",1), UMLPackage.eINSTANCE.getComponent());

					Port p1 = (Port) comp.createOwnedPort("PortComp1", null);
					Port p2 = (Port) comp3.createOwnedPort("PortComp", null);

					Class class1 = myPackage.createOwnedClass(getUniqueName("Main_PortComp",2), true);	
					p1.setType(class1);
					p1.setIsComposite(true);
					
					Class class2 = myPackage.createOwnedClass(getUniqueName("Composite_PortComp",2), true); 		
					p2.setType(class2);
					p2.setIsComposite(true);
					
					comp2.createGeneralization(comp);
					comp3.createGeneralization(comp);

					Interface myInterface1 = (Interface) myPackage.createOwnedType(getUniqueName("CompInt",3), UMLPackage.eINSTANCE.getInterface());
					InterfaceRealization newElement = UMLFactory.eINSTANCE.createInterfaceRealization();
					newElement.setImplementingClassifier(class1);
					newElement.setContract(myInterface1);
					class1.getInterfaceRealizations().add(newElement);
					
					Usage newUsage = UMLFactory.eINSTANCE.createUsage();
					myPackage.getPackagedElements().add(newUsage);
					newUsage.getClients().add(class2);
					newUsage.getSuppliers().add(myInterface1);
					
					Connector connector;
					connector = comp3.createOwnedConnector(null);
					ConnectorEnd sourceEnd = UMLFactory.eINSTANCE.createConnectorEnd();
					ConnectorEnd targetEnd = UMLFactory.eINSTANCE.createConnectorEnd();

					//see ConnectorEndsConvention -- source first, than target
					connector.getEnds().add(sourceEnd);
					connector.getEnds().add(targetEnd);
	
					sourceEnd.setPartWithPort(p1);
					targetEnd.setPartWithPort(p2);
					
					org.eclipse.uml2.uml.Property a1 = comp3.createOwnedAttribute("foo", comp3);
					org.eclipse.uml2.uml.Property a2 = comp3.createOwnedAttribute("bar", comp);
					sourceEnd.setRole(a1);
					targetEnd.setRole(a2);

					EList<Stereotype> stereotypes = connector.getApplicableStereotypes();
					for (Stereotype stereotype: stereotypes) {
						if(stereotype.getName().equals("CompositionCascade")) {
							connector.applyStereotype(stereotype);
						}
					}
					org.eclipse.uml2.uml.Association association = (org.eclipse.uml2.uml.Association) myPackage.createOwnedType(getUniqueName("compositionbetweencomp",6),UMLPackage.eINSTANCE.getAssociation());
					org.eclipse.uml2.uml.Property prop3 = comp3.createOwnedAttribute("prop", comp);
					org.eclipse.uml2.uml.Property prop1 = comp.createOwnedAttribute("prop1", comp3);
					prop3.setAssociation(association);
					prop3.setIsComposite(true);

					prop1.setAssociation(association);
					
				} else {
					Component comp1 = (Component)myPackage.createOwnedType(getUniqueName("Main",1), UMLPackage.eINSTANCE.getComponent());
					Component comp2 = (Component)myPackage.createOwnedType(getUniqueName("Leaf",1), UMLPackage.eINSTANCE.getComponent());
					Component comp3 = (Component)myPackage.createOwnedType(getUniqueName("Composite",1), UMLPackage.eINSTANCE.getComponent());
					//create ports
					
					Port p1 = (Port) comp1.createOwnedPort("PortComp", null); 
					Port p2 = (Port) comp3.createOwnedPort("PortComp", null);

					comp2.createGeneralization(comp1);
					comp3.createGeneralization(comp1);

					Class class1 = myPackage.createOwnedClass(getUniqueName("Main_PortComp",2), true);	
					p1.setType(class1);
					p1.setIsComposite(true);
					
					Class class2 = myPackage.createOwnedClass(getUniqueName("Composite_PortComp",2), true); 		
					p2.setType(class2);
					p2.setIsComposite(true);
					
					Interface myInterface1 = (Interface) myPackage.createOwnedType(getUniqueName("CompInt",3), UMLPackage.eINSTANCE.getInterface());
					InterfaceRealization newElement = UMLFactory.eINSTANCE.createInterfaceRealization();
					newElement.setImplementingClassifier(class1);
					newElement.setContract(myInterface1);
					class1.getInterfaceRealizations().add(newElement);
					
					Usage newUsage = UMLFactory.eINSTANCE.createUsage();
					myPackage.getPackagedElements().add(newUsage);
					newUsage.getClients().add(class2);
					newUsage.getSuppliers().add(myInterface1);
					
					Connector connector;
					connector = comp3.createOwnedConnector(null);
					ConnectorEnd sourceEnd = UMLFactory.eINSTANCE.createConnectorEnd();
					ConnectorEnd targetEnd = UMLFactory.eINSTANCE.createConnectorEnd();

					//see ConnectorEndsConvention -- source first, than target
					connector.getEnds().add(sourceEnd);
					connector.getEnds().add(targetEnd);
	
					org.eclipse.uml2.uml.Property a1 = comp3.createOwnedAttribute("foo", comp3);
					org.eclipse.uml2.uml.Property a2 = comp3.createOwnedAttribute("bar", comp1);
					sourceEnd.setRole(a1);
					targetEnd.setRole(a2);

					sourceEnd.setPartWithPort(p1);
					targetEnd.setPartWithPort(p2);
					
					EList<Stereotype> stereotypes = connector.getApplicableStereotypes();
					for (Stereotype stereotype: stereotypes) {
						if(stereotype.getName().equals("CompositionCascade")) {
							connector.applyStereotype(stereotype);
						}
					}

					org.eclipse.uml2.uml.Association association = (org.eclipse.uml2.uml.Association)myPackage.createOwnedType(getUniqueName("compositebetweencomp",6),UMLPackage.eINSTANCE.getAssociation());
					org.eclipse.uml2.uml.Property prop3 = comp3.createOwnedAttribute("prop", comp1);
					org.eclipse.uml2.uml.Property prop1 = comp1.createOwnedAttribute("prop", comp3);
					prop3.setAssociation(association);
					prop3.setIsComposite(true);

					prop1.setAssociation(association);					
				}
			} else if(myPrimitiveType=="Shield") {
				Component comp1,comp2,comp3;
				System.out.println("show selection");
				if( currentElement instanceof Component) {
					comp1 = (Component) currentElement;
					myPackage = (org.eclipse.uml2.uml.Package) comp1.getNearestPackage();
					comp2 = (Component)myPackage.createOwnedType(getUniqueName("AccessPoint",1), UMLPackage.eINSTANCE.getComponent());
					comp3 = (Component)myPackage.createOwnedType(getUniqueName("Protected",1), UMLPackage.eINSTANCE.getComponent());
					
				} else {
					
				
					comp1 = (Component)myPackage.createOwnedType(getUniqueName("Client",1), UMLPackage.eINSTANCE.getComponent());
					comp2 = (Component)myPackage.createOwnedType(getUniqueName("AccessPoint",1), UMLPackage.eINSTANCE.getComponent());
					comp3 = (Component)myPackage.createOwnedType(getUniqueName("Protected",1), UMLPackage.eINSTANCE.getComponent());
				}
				org.eclipse.uml2.uml.Package pack1 = (org.eclipse.uml2.uml.Package) myPackage.createNestedPackage(getUniqueName("ShieldGroup",4));
				
				Port p1 = (Port) comp1.createOwnedPort(getUniqueName("PortClient",5), null); 

				Class class1 = myPackage.createOwnedClass(getUniqueName("Client_PortClient",2), true);	
				p1.setType(class1);
				p1.setIsComposite(true);
				
				Interface myInterface1 = (Interface) myPackage.createOwnedType(getUniqueName("ShieldInt",3), UMLPackage.eINSTANCE.getInterface());			
				Usage newUsage = UMLFactory.eINSTANCE.createUsage();
				myPackage.getPackagedElements().add(newUsage);
				newUsage.getClients().add(class1);
				newUsage.getSuppliers().add(myInterface1);
				
				Port p2 = (Port) comp2.createOwnedPort("PortAccessPoint", null); 

				Class class2 = myPackage.createOwnedClass(getUniqueName("AccessPoint_PortAccessPoint",2), true);	
				p2.setType(class2);
				p2.setIsComposite(true);
				
				InterfaceRealization newElement = UMLFactory.eINSTANCE.createInterfaceRealization();
				newElement.setImplementingClassifier(class2);
				newElement.setContract(myInterface1);
				class2.getInterfaceRealizations().add(newElement);
				
				EList<Stereotype> stereotypes = myInterface1.getApplicableStereotypes();
				for (Stereotype stereotype: stereotypes) {
					if(stereotype.getName().equals("IShield")) {
						myInterface1.applyStereotype(stereotype);
					}
				}
				EList<Stereotype> stereotypesPort = p2.getApplicableStereotypes();
				for (Stereotype stereotype: stereotypesPort) {
					if(stereotype.getName().equals("ShieldPort")) {
						p2.applyStereotype(stereotype);
					}
				}
				pack1.createElementImport(comp2);
				pack1.createElementImport(comp3);
				EList<Stereotype> stereotypesPackage = pack1.getApplicableStereotypes();
				for (Stereotype stereotype: stereotypesPackage) {
					if(stereotype.getName().equals("Group")) {
						pack1.applyStereotype(stereotype);
					}
				}
				Connector connector;
				connector = comp1.createOwnedConnector(null);
				ConnectorEnd sourceEnd = UMLFactory.eINSTANCE.createConnectorEnd();
				ConnectorEnd targetEnd = UMLFactory.eINSTANCE.createConnectorEnd();

				//see ConnectorEndsConvention -- source first, than target
				connector.getEnds().add(sourceEnd);
				connector.getEnds().add(targetEnd);

				sourceEnd.setPartWithPort(p1);
				targetEnd.setPartWithPort(p2);
				
				org.eclipse.uml2.uml.Property a1 = comp1.createOwnedAttribute(getUniqueNameinComponent(comp1,"foo",1), comp1);
				org.eclipse.uml2.uml.Property a2 = comp1.createOwnedAttribute(getUniqueNameinComponent(comp1,"foo",1), comp2);
				
				sourceEnd.setRole(a1);
				targetEnd.setRole(a2);

				EList<Stereotype> stereotypesConn = connector.getApplicableStereotypes();
				for (Stereotype stereotype: stereotypesConn) {
					if(stereotype.getName().equals("Shield")) {
						connector.applyStereotype(stereotype);
					}
				}
			} else if(myPrimitiveType=="ShieldExt") {
				if( currentElement instanceof Port) {
					Port p1 = (Port) currentElement;
					Component comp1 = (Component) p1.getOwner();
					
					Connector relevantConn;
					EList<Connector> conns = comp1.getOwnedConnectors();
					for (Connector conn: conns) {
						EList<ConnectorEnd> connsends = conn.getEnds();
						for (ConnectorEnd connend: connsends) {
							if(connend.getPartWithPort()==p1) {
								relevantConn=conn;
							}
						}
					}	
					
				}
			} else if(myPrimitiveType=="Typing") {
				if( currentElement instanceof Component) {
					Component comp1 = (Component) currentElement;
					myPackage = (org.eclipse.uml2.uml.Package) comp1.getNearestPackage();

					Component comp2 = (Component)myPackage.createOwnedType(getUniqueName("AType",1), UMLPackage.eINSTANCE.getComponent());
					Port p1 = (Port) comp1.createOwnedPort(getUniqueNameinComponent(comp1,"PortA",2), null); 
					
					Class class1 = myPackage.createOwnedClass(getUniqueName("A_PortA",2), true);	
					p1.setType(class1);
					p1.setIsComposite(true);
					
					Port p2 = (Port) comp2.createOwnedPort("PortAType", null); 
	
					Class class2 = myPackage.createOwnedClass(getUniqueName("AType_PortAType",2), true);	
					p2.setType(class2);
					p2.setIsComposite(true);
					
					Interface myInterface1 = (Interface) myPackage.createOwnedType(getUniqueName("TypeInt",3), UMLPackage.eINSTANCE.getInterface());			
					Usage newUsage = UMLFactory.eINSTANCE.createUsage();
					myPackage.getPackagedElements().add(newUsage);
					newUsage.getClients().add(class1);
					newUsage.getSuppliers().add(myInterface1);
					
					InterfaceRealization newElement = UMLFactory.eINSTANCE.createInterfaceRealization();
					newElement.setImplementingClassifier(class2);
					newElement.setContract(myInterface1);
					class2.getInterfaceRealizations().add(newElement);
					
					Connector connector;
					connector = comp1.createOwnedConnector(null);
					ConnectorEnd sourceEnd = UMLFactory.eINSTANCE.createConnectorEnd();
					ConnectorEnd targetEnd = UMLFactory.eINSTANCE.createConnectorEnd();
	
					//see ConnectorEndsConvention -- source first, than target
					connector.getEnds().add(sourceEnd);
					connector.getEnds().add(targetEnd);
	
					org.eclipse.uml2.uml.Property a1 = comp1.createOwnedAttribute(getUniqueNameinComponent(comp1,"foo",1), comp1);
					org.eclipse.uml2.uml.Property a2 = comp1.createOwnedAttribute(getUniqueNameinComponent(comp1,"bar",1), comp2);
					sourceEnd.setRole(a1);
					targetEnd.setRole(a2);
	
					sourceEnd.setPartWithPort(p1);
					targetEnd.setPartWithPort(p2);
					
					EList<Stereotype> stereotypesConn = connector.getApplicableStereotypes();
					for (Stereotype stereotype: stereotypesConn) {
						if(stereotype.getName().equals("TypeConnector")) {
							connector.applyStereotype(stereotype);
						}
					}	
				} else {
					Component comp1 = (Component)myPackage.createOwnedType(getUniqueName("A",1), UMLPackage.eINSTANCE.getComponent());
					Component comp2 = (Component)myPackage.createOwnedType(getUniqueName("AType",1), UMLPackage.eINSTANCE.getComponent());
					
					Port p1 = (Port) comp1.createOwnedPort("PortA", null); 
	
					Class class1 = myPackage.createOwnedClass(getUniqueName("A_PortA",2), true);	
					p1.setType(class1);
					p1.setIsComposite(true);
					
					Port p2 = (Port) comp2.createOwnedPort("PortAType", null); 
	
					Class class2 = myPackage.createOwnedClass(getUniqueName("AType_PortAType",2), true);	
					p2.setType(class2);
					p2.setIsComposite(true);
					
					Interface myInterface1 = (Interface) myPackage.createOwnedType(getUniqueName("TypeInt",3), UMLPackage.eINSTANCE.getInterface());			
					Usage newUsage = UMLFactory.eINSTANCE.createUsage();
					myPackage.getPackagedElements().add(newUsage);
					newUsage.getClients().add(class1);
					newUsage.getSuppliers().add(myInterface1);
					
					InterfaceRealization newElement = UMLFactory.eINSTANCE.createInterfaceRealization();
					newElement.setImplementingClassifier(class2);
					newElement.setContract(myInterface1);
					class2.getInterfaceRealizations().add(newElement);
					
					Connector connector;
					connector = comp1.createOwnedConnector(null);
					ConnectorEnd sourceEnd = UMLFactory.eINSTANCE.createConnectorEnd();
					ConnectorEnd targetEnd = UMLFactory.eINSTANCE.createConnectorEnd();
	
					//see ConnectorEndsConvention -- source first, than target
					connector.getEnds().add(sourceEnd);
					connector.getEnds().add(targetEnd);
	
					sourceEnd.setPartWithPort(p1);
					targetEnd.setPartWithPort(p2);
					
					org.eclipse.uml2.uml.Property a1 = comp1.createOwnedAttribute("foo", comp1);
					org.eclipse.uml2.uml.Property a2 = comp1.createOwnedAttribute("bar", comp2);
					sourceEnd.setRole(a1);
					targetEnd.setRole(a2);
	
					EList<Stereotype> stereotypesConn = connector.getApplicableStereotypes();
					for (Stereotype stereotype: stereotypesConn) {
						if(stereotype.getName().equals("TypeConnector")) {
							connector.applyStereotype(stereotype);
						}
					}
				}
			} else if(myPrimitiveType=="Super Typing") {
				if( currentElement instanceof Component) {
					Component comp2 = (Component) currentElement;
					myPackage = (org.eclipse.uml2.uml.Package) comp2.getNearestPackage();
					Component comp3 = (Component)myPackage.createOwnedType(getUniqueName("BSuperType",1), UMLPackage.eINSTANCE.getComponent());
					
					Port p2 = (Port) comp2.createOwnedPort(getUniqueNameinComponent(comp2,"PortAType",2), null); 
	
					Class class2 = myPackage.createOwnedClass(getUniqueName("AType_PortAType",2), true);	
					p2.setType(class2);
					p2.setIsComposite(true);
					
					Port p3 = (Port) comp3.createOwnedPort("PortBSuperType", null); 
	
					Class class3 = myPackage.createOwnedClass(getUniqueName("BSuperType_PortBSuperType",2), true);	
					p3.setType(class3);
					p3.setIsComposite(true);
					
					Interface myInterface2 = (Interface) myPackage.createOwnedType(getUniqueName("SuperTypeInt",3), UMLPackage.eINSTANCE.getInterface());			
					Usage newUsage1 = UMLFactory.eINSTANCE.createUsage();
					myPackage.getPackagedElements().add(newUsage1);
					newUsage1.getClients().add(class2);
					newUsage1.getSuppliers().add(myInterface2);
					
					InterfaceRealization newElement1 = UMLFactory.eINSTANCE.createInterfaceRealization();
					newElement1.setImplementingClassifier(class3);
					newElement1.setContract(myInterface2);
					class3.getInterfaceRealizations().add(newElement1);
					
					Connector connector1;
					connector1 = comp2.createOwnedConnector(null);
					ConnectorEnd sourceEnd1 = UMLFactory.eINSTANCE.createConnectorEnd();
					ConnectorEnd targetEnd1 = UMLFactory.eINSTANCE.createConnectorEnd();
	
					//see ConnectorEndsConvention -- source first, than target
					connector1.getEnds().add(sourceEnd1);
					connector1.getEnds().add(targetEnd1);
	
					sourceEnd1.setPartWithPort(p2);
					targetEnd1.setPartWithPort(p3);
					
					org.eclipse.uml2.uml.Property a11 = comp2.createOwnedAttribute(getUniqueNameinComponent(comp2,"foo",1), comp2);
					org.eclipse.uml2.uml.Property a21 = comp2.createOwnedAttribute(getUniqueNameinComponent(comp2,"bar",1), comp3);
					sourceEnd1.setRole(a11);
					targetEnd1.setRole(a21);
	
					EList<Stereotype> stereotypesConn1 = connector1.getApplicableStereotypes();
					for (Stereotype stereotype: stereotypesConn1) {
						if(stereotype.getName().equals("SuperTypeConnector")) {
							connector1.applyStereotype(stereotype);
						}
					}
				} else {
					Component comp1 = (Component)myPackage.createOwnedType(getUniqueName("A",1), UMLPackage.eINSTANCE.getComponent());
					Component comp2 = (Component)myPackage.createOwnedType(getUniqueName("AType",1), UMLPackage.eINSTANCE.getComponent());
					Component comp3 = (Component)myPackage.createOwnedType(getUniqueName("BSuperType",1), UMLPackage.eINSTANCE.getComponent());
					
					Port p1 = (Port) comp1.createOwnedPort("PortA", null); 
	
					Class class1 = myPackage.createOwnedClass(getUniqueName("A_PortA",2), true);	
					p1.setType(class1);
					p1.setIsComposite(true);
					
					Port p2 = (Port) comp2.createOwnedPort("PortAType", null); 
	
					Class class2 = myPackage.createOwnedClass(getUniqueName("AType_PortAType",2), true);	
					p2.setType(class2);
					p2.setIsComposite(true);
					
					Port p22 = (Port) comp2.createOwnedPort("PortAType", null); 
					
					Class class22 = myPackage.createOwnedClass(getUniqueName("Class",2), true);	
					p22.setType(class22);
					p22.setIsComposite(true);
					
					Port p3 = (Port) comp3.createOwnedPort("PortBSuperType", null); 
	
					Class class3 = myPackage.createOwnedClass(getUniqueName("BSuperType_PortBSuperType",2), true);	
					p3.setType(class3);
					p3.setIsComposite(true);
					
					Interface myInterface1 = (Interface) myPackage.createOwnedType(getUniqueName("TypeInt",3), UMLPackage.eINSTANCE.getInterface());			
					Usage newUsage = UMLFactory.eINSTANCE.createUsage();
					myPackage.getPackagedElements().add(newUsage);
					newUsage.getClients().add(class1);
					newUsage.getSuppliers().add(myInterface1);
					
					InterfaceRealization newElement = UMLFactory.eINSTANCE.createInterfaceRealization();
					newElement.setImplementingClassifier(class2);
					newElement.setContract(myInterface1);
					class2.getInterfaceRealizations().add(newElement);
					
					Connector connector;
					connector = comp1.createOwnedConnector(null);
					ConnectorEnd sourceEnd = UMLFactory.eINSTANCE.createConnectorEnd();
					ConnectorEnd targetEnd = UMLFactory.eINSTANCE.createConnectorEnd();
	
					//see ConnectorEndsConvention -- source first, than target
					connector.getEnds().add(sourceEnd);
					connector.getEnds().add(targetEnd);
	
					org.eclipse.uml2.uml.Property a1 = comp1.createOwnedAttribute("foo", comp1);
					org.eclipse.uml2.uml.Property a2 = comp1.createOwnedAttribute("bar", comp2);
					sourceEnd.setRole(a1);
					targetEnd.setRole(a2);
	
					sourceEnd.setPartWithPort(p1);
					targetEnd.setPartWithPort(p2);
					
					EList<Stereotype> stereotypesConn = connector.getApplicableStereotypes();
					for (Stereotype stereotype: stereotypesConn) {
						if(stereotype.getName().equals("TypeConnector")) {
							connector.applyStereotype(stereotype);
						}
					}
					Interface myInterface2 = (Interface) myPackage.createOwnedType(getUniqueName("SuperTypeInt",3), UMLPackage.eINSTANCE.getInterface());			
					Usage newUsage1 = UMLFactory.eINSTANCE.createUsage();
					myPackage.getPackagedElements().add(newUsage1);
					newUsage1.getClients().add(class2);
					newUsage1.getSuppliers().add(myInterface2);
					
					InterfaceRealization newElement1 = UMLFactory.eINSTANCE.createInterfaceRealization();
					newElement1.setImplementingClassifier(class3);
					newElement1.setContract(myInterface2);
					class3.getInterfaceRealizations().add(newElement1);
					
					Connector connector1;
					connector1 = comp2.createOwnedConnector(null);
					ConnectorEnd sourceEnd1 = UMLFactory.eINSTANCE.createConnectorEnd();
					ConnectorEnd targetEnd1 = UMLFactory.eINSTANCE.createConnectorEnd();
	
					//see ConnectorEndsConvention -- source first, than target
					connector1.getEnds().add(sourceEnd1);
					connector1.getEnds().add(targetEnd1);
	
					sourceEnd.setPartWithPort(p22);
					targetEnd.setPartWithPort(p3);
					
					org.eclipse.uml2.uml.Property a11 = comp2.createOwnedAttribute("foo", comp2);
					org.eclipse.uml2.uml.Property a21 = comp2.createOwnedAttribute("bar", comp3);
					sourceEnd1.setRole(a11);
					targetEnd1.setRole(a21);
	
					EList<Stereotype> stereotypesConn1 = connector1.getApplicableStereotypes();
					for (Stereotype stereotype: stereotypesConn1) {
						if(stereotype.getName().equals("SuperTypeConnector")) {
							connector1.applyStereotype(stereotype);
						}
					}
				}
				
			} else if(myPrimitiveType=="Virtual Connector") {
				Component comp1,comp2;
				if( currentElement instanceof Component) {
					comp1 = (Component) currentElement;
					myPackage = (org.eclipse.uml2.uml.Package) comp1.getNearestPackage();
					comp2 = (Component)myPackage.createOwnedType(getUniqueName("B",1), UMLPackage.eINSTANCE.getComponent());
					
				} else {
					comp1 = (Component)myPackage.createOwnedType(getUniqueName("A",1), UMLPackage.eINSTANCE.getComponent());
					comp2 = (Component)myPackage.createOwnedType(getUniqueName("B",1), UMLPackage.eINSTANCE.getComponent());
				}
				Port p1 = (Port) comp1.createOwnedPort("PortA", null); 
				
				Class class1 = myPackage.createOwnedClass(getUniqueName("A_PortA",2), true);	
				p1.setType(class1);
				p1.setIsComposite(true);
				
				Port p2 = (Port) comp2.createOwnedPort("PortB", null); 

				Class class2 = myPackage.createOwnedClass(getUniqueName("B_PortB",2), true);	
				p2.setType(class2);
				p2.setIsComposite(true);
				
				Interface myInterface1 = (Interface) myPackage.createOwnedType(getUniqueName("VirtualConnectorInt",3), UMLPackage.eINSTANCE.getInterface());			
				Usage newUsage = UMLFactory.eINSTANCE.createUsage();
				myPackage.getPackagedElements().add(newUsage);
				newUsage.getClients().add(class1);
				newUsage.getSuppliers().add(myInterface1);
				
				InterfaceRealization newElement = UMLFactory.eINSTANCE.createInterfaceRealization();
				newElement.setImplementingClassifier(class2);
				newElement.setContract(myInterface1);
				class2.getInterfaceRealizations().add(newElement);
				
				Connector connector;
				connector = comp1.createOwnedConnector(null);
				ConnectorEnd sourceEnd = UMLFactory.eINSTANCE.createConnectorEnd();
				ConnectorEnd targetEnd = UMLFactory.eINSTANCE.createConnectorEnd();

				//see ConnectorEndsConvention -- source first, than target
				connector.getEnds().add(sourceEnd);
				connector.getEnds().add(targetEnd);

				org.eclipse.uml2.uml.Property a1 = comp1.createOwnedAttribute("foo", comp1);
				org.eclipse.uml2.uml.Property a2 = comp1.createOwnedAttribute("bar", comp2);
				sourceEnd.setRole(a1);
				targetEnd.setRole(a2);

				sourceEnd.setPartWithPort(p1);
				targetEnd.setPartWithPort(p2);
				
				EList<Stereotype> stereotypesInt = myInterface1.getApplicableStereotypes();
				for (Stereotype stereotype: stereotypesInt) {
					if(stereotype.getName().equals("IVirtual")) {
						myInterface1.applyStereotype(stereotype);
					}
				}
				
				EList<Stereotype> stereotypesConn = connector.getApplicableStereotypes();
				for (Stereotype stereotype: stereotypesConn) {
					if(stereotype.getName().equals("VirtualConnector")) {
						connector.applyStereotype(stereotype);
					}
				}
			} else if(myPrimitiveType=="Wizard") {
				AddPrimitiveWizard wizard = new AddPrimitiveWizard(myPackage);
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
		 else if(myPrimitiveType=="Client-server" || myPrimitiveType=="Pipe-filter" || myPrimitiveType=="Layers") {
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

