package action;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.ocl.utilities.impl.Bag;
import org.eclipse.ocl.OCL;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.helper.OCLHelper;
import org.eclipse.ocl.util.Tuple;
import action.ValidatePrimitiveAction.ValidatePrimitiveRequest;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.UMLPackage;

public class PrimitiveAddition {

	String myPrimitiveType;
	Element myElement;

	public PrimitiveAddition(String primitiveType, Element element) {
		System.out.println("add a callback");
		myPrimitiveType = primitiveType;
		myElement=element;
		
		if (primitiveType=="Callback") {
			System.out.println("add a callback2");
			//Model model = (Model) myElement.getModel();
			//org.eclipse.uml2.uml.Class supplierClass = createClass(model,"Supplier", false);
			//model.createModelGen();
			//org.eclipse.uml2.uml.Component comp = (org.eclipse.uml2.uml.Component) UMLPackage.eINSTANCE.getNamedElement_Name();
			//System.out.println("comp = " + comp.getName());
		}
	}
	
	protected static org.eclipse.uml2.uml.Class createClass(
			org.eclipse.uml2.uml.Package package_, String name, boolean isAbstract) {
			org.eclipse.uml2.uml.Class class_ = package_.createOwnedClass(name,
            isAbstract);
			return class_;
}


	
}
