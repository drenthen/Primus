/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2002 - All Rights Reserved. 
 */
 
package action;
import java.util.Iterator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.uml2.uml.Component;

/**
 * Wizard class
 */
public class AddPrimitiveWizard extends Wizard implements INewWizard
{
	public static final String copyright = "(c) Copyright Nick Corporation 2002.";	
	// wizard pages
	PrimitiveMainPage primitivemainPage;
	CallbackPage callbackPage;
	IndirectionPage indirectionPage;
	GroupingPage groupingPage;
	LayersPage layersPage;
	AggregationCascadePage aggregationCascadePage;
	CompositionCascadePage compositionCascadePage;
	ShieldPage shieldPage;
	TypingPage typingPage;
	VirtualConnectorPage virtualConnectorPage;
	ConnectorPage connectorPage;
	
	org.eclipse.uml2.uml.Package myPackage;
	boolean canFinish=false;

	// the model
	PrimitiveModel model;
	
	// workbench selection when the wizard was started
	protected IStructuredSelection selection;
	
	// flag indicated whether the wizard can be completed or not 
	// if the user has selected plane as type of transport
	protected boolean primitiveCompleted = false;
	
	// the workbench instance
	protected IWorkbench workbench;

	/**
	 * Constructor for HolidayMainWizard.
	 */
	public AddPrimitiveWizard(org.eclipse.uml2.uml.Package myPackage) {
		super();
		this.myPackage=myPackage;
		model = new PrimitiveModel();
	}
	
	public void addPages()
	{
		primitivemainPage = new PrimitiveMainPage();
		addPage(primitivemainPage);
		callbackPage = new CallbackPage("",myPackage);
		addPage(callbackPage);
		indirectionPage = new IndirectionPage("",myPackage);
		addPage(indirectionPage);
		groupingPage = new GroupingPage("",myPackage);
		addPage(groupingPage);
		layersPage = new LayersPage("",myPackage);
		addPage(layersPage);
		aggregationCascadePage = new AggregationCascadePage("",myPackage);
		addPage(aggregationCascadePage);
		compositionCascadePage = new CompositionCascadePage("",myPackage);
		addPage(compositionCascadePage);
		shieldPage = new ShieldPage("",myPackage);
		addPage(shieldPage);
		typingPage = new TypingPage("",myPackage);
		addPage(typingPage);
		virtualConnectorPage = new VirtualConnectorPage("",myPackage);
		addPage(virtualConnectorPage);
		connectorPage = new ConnectorPage("",myPackage);
		addPage(connectorPage);
	}

	/**
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection)
	{
		this.workbench = workbench;
		this.selection = selection;
		
	}

	public boolean canFinish()
	{
		return canFinish;
	}
	
	public boolean performFinish() 
	{	
		CreatePrimitive createPrimitive = new CreatePrimitive(myPackage);
		if(model.primitiveChoice.equals("Callback")){
			createPrimitive.createCallback(model.callerNew, model.callbackNew, model.caller, model.callback);			
		} else if(model.primitiveChoice.equals("Indirection")){
			createPrimitive.createIndirection(model.clientActive,model.clientNew,model.client, model.indirectorNew,model.indirector,null,model.targetNew,model.target,null,null,null,null,null,null);
		} else if(model.primitiveChoice.equals("Grouping")){
			createPrimitive.createGroup(model.groupPackageNew, model.groupPackage);
		} else if(model.primitiveChoice.equals("Layers")){
			createPrimitive.createLayer(model.layerPackageNew, model.layerPackage,model.layerNumber);
		} else if(model.primitiveChoice.equals("Aggregation Cascade")){
			createPrimitive.createAggregationCascade(model.mainAggActive,model.mainAggNew, model.agg1New,model.agg2New,model.mainAgg,model.agg1,model.agg2,model.aggregationType);
		} else if(model.primitiveChoice.equals("Composition Cascade")){
			createPrimitive.createCompositionCascade(model.mainCompNew, model.leafCompNew,model.compositeCompNew,model.main,model.leaf,model.composite);
		} else if(model.primitiveChoice.equals("Shield")){
			createPrimitive.createShield(model.clietShieldNew, model.accesspointShieldNew,model.protectedActive, model.protectedShieldNew,model.groupShieldNew,model.clientShield,model.accessPointShield,model.protectedShield,model.groupShield);
		} else if(model.primitiveChoice.equals("Typing")){
			createPrimitive.createTyping(model.baseNew, model.typeNew,model.supertypeNew,model.firstActive,model.thirdActive,model.base,model.type,model.supertype);
		} else if(model.primitiveChoice.equals("Virtual Connector")){
			createPrimitive.createVirtualConnector(model.callerVCNew, model.receiverVCNew,model.callerVC,model.receiverVC);
		} else if(model.primitiveChoice.equals("Connector")){
			createPrimitive.createConnector(model.callerConnectorNew, model.receiverConnectorNew,model.callerConnector,model.receiverConnector);
		}
		return true;
	}
}
