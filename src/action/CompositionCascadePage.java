/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2002 - All Rights Reserved. 
 */

package action;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Stereotype;

/**
 * Wizard page shown when the user has chosen indirection 
 * 
 */

public class CompositionCascadePage extends WizardPage implements Listener 
{
	public static final String copyright = "(c) Copyright NICK Corporation 2002.";
	
	// widgets on this page
	Combo mainChoice,leafChoice,compositeChoice;
	List componentList,componentList1,componentList2;
	final static String[] yesno ={ "Yes", "No" };
	org.eclipse.uml2.uml.Package myPackage;
		
	/**
	 * Constructor for indirection page.
	 */
	protected CompositionCascadePage(String arg0,org.eclipse.uml2.uml.Package myPackage) {
		super(arg0);
		setTitle("Composition Cascade");
		setDescription("Select composition cascade options");
		this.myPackage=myPackage;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {

	    // create the composite to hold the widgets
		GridData gd;
		Composite composite = new Composite(parent, SWT.NONE);

	    // create the desired layout for this wizard page
		GridLayout gl = new GridLayout();
		int ncol = 2;
		gl.numColumns = ncol;
		composite.setLayout(gl);

		new Label (composite, SWT.NONE).setText("New Main Composition Cascade Component?:");
		mainChoice = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		gd = new GridData();
		gd.horizontalAlignment = GridData.BEGINNING;
		mainChoice.setLayoutData(gd);
		mainChoice.setItems(yesno);
		mainChoice.addListener(SWT.Selection, this);
		
		//callerChoice.setText(travelDate.getItem(dayOfMonth -1)); // 0 based indexes
		
		new Label (composite, SWT.NONE).setText("Choose Main Composition component if required:");
		//new Label (composite, SWT.NONE).setText("Press shift or ctrl to select multiple components");
		componentList = new List(composite,  SWT.BORDER | SWT.READ_ONLY  | SWT.V_SCROLL | SWT.H_SCROLL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan =ncol;
		gd.heightHint = 70;
		componentList.setLayoutData(gd);
		componentList.addListener(SWT.Selection, this);
		EList<Element> elements = myPackage.getOwnedElements();
		
		for (Element el: elements) {
			if(el instanceof Component) {
				Component comp = (Component) el;
				componentList.add(comp.getName());
			}
		}
		
		new Label (composite, SWT.NONE).setText("New Leaf component?:");
		leafChoice = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		gd = new GridData();
		gd.horizontalAlignment = GridData.BEGINNING;
		leafChoice.setLayoutData(gd);
		leafChoice.setItems(yesno);
		leafChoice.addListener(SWT.Selection, this);
		
		//callerChoice.setText(travelDate.getItem(dayOfMonth -1)); // 0 based indexes
		
		new Label (composite, SWT.NONE).setText("Choose Leaf component if required:");
		componentList1 = new List(composite, SWT.BORDER | SWT.READ_ONLY  | SWT.V_SCROLL | SWT.H_SCROLL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan =ncol;
		gd.heightHint = 70;
		componentList1.setLayoutData(gd);
		componentList1.addListener(SWT.Selection, this);
		EList<Element> elements1 = myPackage.getOwnedElements();
		
		for (Element el: elements1) {
			if(el instanceof Component) {
				Component comp = (Component) el;
				componentList1.add(comp.getName());
			}
		}
		
		new Label (composite, SWT.NONE).setText("New Composite component?:");
		compositeChoice = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		gd = new GridData();
		gd.horizontalAlignment = GridData.BEGINNING;
		compositeChoice.setLayoutData(gd);
		compositeChoice.setItems(yesno);
		compositeChoice.addListener(SWT.Selection, this);

		//callerChoice.setText(travelDate.getItem(dayOfMonth -1)); // 0 based indexes
		
		new Label (composite, SWT.NONE).setText("Choose Composite component if required:");
		//new Label (composite, SWT.NONE).setText("Press shift or ctrl to select multiple components");
		componentList2 = new List(composite, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL );
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan =ncol;
		gd.heightHint = 70;
		componentList2.setLayoutData(gd);
		componentList2.addListener(SWT.Selection, this);
		EList<Element> elements2 = myPackage.getOwnedElements();
		
		for (Element el: elements2) {
			if(el instanceof Component) {
				Component comp = (Component) el;
				componentList2.add(comp.getName());
			}
		}
	    // set the composite as the control for this page
		setControl(composite);		
		//setPageComplete(true);
	}

	public boolean canFlipToNextPage()
	{
		// no next page for this path through the wizard
		return false;
	}
	
    /*
     * Process the events: 
     * when the user has entered all information
     * the wizard can be finished
     */
	public void handleEvent(Event e)
	{
		//MessageDialog.openInformation(this.getShell(),"", "Finished");
		getWizard().getContainer().updateButtons();
		setPageComplete(isPageComplete());
	}
	
	/*
	 * Sets the completed field on the wizard class when all the information 
	 * is entered and the wizard can be completed
	 */	 
	public boolean isPageComplete()
	{
		if((mainChoice.getText().equals("Yes") || (mainChoice.getText().equals("No") && componentList.getSelectionCount()>0)) && (leafChoice.getText().equals("Yes") || (leafChoice.getText().equals("No") && componentList1.getSelectionCount()>0)) && (compositeChoice.getText().equals("Yes") || (compositeChoice.getText().equals("No") && componentList2.getSelectionCount()>0))) {
			AddPrimitiveWizard wizard = (AddPrimitiveWizard)getWizard();
			wizard.canFinish=true;
			saveDataToModel();
			
			return true;
		} else {
			return false;
		}
	}
	
	private void saveDataToModel()
	{
		AddPrimitiveWizard wizard = (AddPrimitiveWizard)getWizard();
		PrimitiveModel model = wizard.model;
		if(mainChoice.getText().equals("Yes")) {
			model.mainCompNew=true;
		} else {
			model.mainCompNew=false;
			model.main = (Component) myPackage.getOwnedType(componentList.getSelection()[0]);
		}
		if(leafChoice.getText().equals("Yes")) {
			model.leafCompNew=true;
		} else {
			model.leafCompNew=false;
			model.leaf=(Component) myPackage.getOwnedType(componentList1.getSelection()[0]);
		}
		if(compositeChoice.getText().equals("Yes")) {
			model.compositeCompNew=true;
		} else {
			model.compositeCompNew=false;
			model.composite = (Component)myPackage.getOwnedType(componentList2.getSelection()[0]);
		}
	}	

	void onEnterPage()
	{
	    // Gets the model
		AddPrimitiveWizard wizard = (AddPrimitiveWizard)getWizard();
		PrimitiveModel model = wizard.model;
	}
}
