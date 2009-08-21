/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2002 - All Rights Reserved. 
 */

package action;

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
 * Wizard page shown when the user has chosen callback 
 * 
 */

public class MVCProfilePage extends WizardPage implements Listener 
{
	public static final String copyright = "(c) Copyright NICK Corporation 2002.";
	
	// widgets on this page
	Combo profileChoice;
	final static String[] variations ={ "standard", "MV Presenter"};
	org.eclipse.uml2.uml.Package myPackage;
		
	/**
	 * Constructor for callback page.
	 */
	protected MVCProfilePage(String arg0,org.eclipse.uml2.uml.Package myPackage) {
		super(arg0);
		setTitle("MVC available profiles");
		setDescription("Select MVC options");
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

		new Label (composite, SWT.NONE).setText("available profiles:");
		profileChoice = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		gd = new GridData();
		gd.horizontalAlignment = GridData.BEGINNING;
		profileChoice.setLayoutData(gd);
		profileChoice.setItems(variations);
		profileChoice.addListener(SWT.Selection, this);
		
		//callerChoice.setText(travelDate.getItem(dayOfMonth -1)); // 0 based indexes
		
		
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
		return true;
	}
	
	private void saveDataToModel()
	{
		
	}	

	void onEnterPage()
	{
	    // Gets the model
		AddPatternWizard wizard = (AddPatternWizard)getWizard();
		PrimitiveModel model = wizard.model;
	}
}
