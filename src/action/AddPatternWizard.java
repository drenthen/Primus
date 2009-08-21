/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2002 - All Rights Reserved. 
 */
 
package action;
import java.util.Iterator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;

/**
 * Wizard class
 */
public class AddPatternWizard extends Wizard implements INewWizard
{
	public static final String copyright = "(c) Copyright Nick Corporation 2002.";	
	// wizard pages
	PatternMainPage patternmainPage;
	MVCProfilePage MVCprofilepage;
	
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
	public AddPatternWizard(org.eclipse.uml2.uml.Package myPackage) {
		super();
		this.myPackage=myPackage;
		model = new PrimitiveModel();
	}
	
	public void addPages()
	{
		patternmainPage = new PatternMainPage(myPackage);
		addPage(patternmainPage);
		MVCprofilepage = new MVCProfilePage("",myPackage);
		addPage(MVCprofilepage);
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
		return true;
	}
}
