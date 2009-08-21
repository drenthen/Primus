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

public class OCLCheck {

	String myPrimitiveType;
	Element myElement;
	OCL myOcl;
	OCLHelper oclHelper;
	OCLExpression oclInv;
	public OCLCheck(String primitiveType, Element element) {
		myPrimitiveType = primitiveType;
		myElement=element;
		
		myOcl = org.eclipse.ocl.uml.OCL.newInstance();
		oclHelper = myOcl.createOCLHelper();
		
		//ResourceSet resourceSet = element.getModel().get
		myOcl.setEvaluationTracingEnabled(true);
		myOcl.setParseTracingEnabled(true);
		
		oclHelper.setInstanceContext(element);

		oclInv=null;
		
		myOcl.setEvaluationTracingEnabled(true);
		myOcl.setParseTracingEnabled(true);
		
		oclHelper.setInstanceContext(element);
	}
	public boolean validatePrimitive(String primType) {
		System.out.println("boolean");
		String myQuery="";
		if(primType=="Callback") {
			System.out.println("Callback");
			
			myQuery="self.ownedPort.getAppliedStereotypes()->any(name='EventPort')->notEmpty() and " +
			"self.ownedPort.getProvideds().getAppliedStereotypes()->any(name='IEvent')->notEmpty() and " +
			"self.ownedPort.getRequireds().getAppliedStereotypes()->any(name='ICallback')->notEmpty() and " +
			"self.ownedConnector.getAppliedStereotypes()->any(name='Callback')->notEmpty() and " +
			"self.ownedConnector.end.role.type->any(c| c <> self).oclAsType(Component).ownedPort.getAppliedStereotypes()->any(name='CallbackPort')->notEmpty() and " +
			"self.ownedConnector.end.role.type->any(c| c <> self).oclAsType(Component).ownedPort.getProvideds().getAppliedStereotypes()->any(name='ICallback')->notEmpty() and " +
			"self.ownedConnector.end.role.type->any(c| c <> self).oclAsType(Component).ownedPort.getRequireds().getAppliedStereotypes()->any(name='IEvent')->notEmpty() and " +
			"self.ownedPort.required = self.ownedConnector.end.role.type->any(c| c <> self).oclAsType(Component).ownedPort.provided and " +
			"self.ownedPort.provided = self.ownedConnector.end.role.type->any(c| c <> self).oclAsType(Component).ownedPort.required and " +
			"self.ownedConnector->any(c : Connector | c.oclIsTypeOf(uml::Connector)).end->size()=2";

		} else if(primType=="Indirection") {
			System.out.println("Indirection");
			
			myQuery="self.ownedPort.getAppliedStereotypes()->any(name='IndirectionPort')->notEmpty() and " +
			"self.ownedPort.getProvideds().getAppliedStereotypes()->any(name='Indirector')->notEmpty() and " +
			"self.ownedPort.getRequireds().getAppliedStereotypes()->any(name='ITarget')->notEmpty() and " +
			"self.ownedConnector.getAppliedStereotypes()->any(name='Indirection')->notEmpty() and " +
			"self.ownedConnector.end.role.type->any(c| c <> self).oclAsType(Component).ownedPort.getAppliedStereotypes()->any(name='IndirectionTargetPort')->notEmpty() and " +
			"self.ownedConnector.end.role.type->any(c| c <> self).oclAsType(Component).ownedPort.getProvideds().getAppliedStereotypes()->any(name='ITarget')->notEmpty() and " +
			"self.ownedPort.required = self.ownedConnector.end.role.type->any(c| c <> self).oclAsType(Component).ownedPort.provided and " +
			"self.ownedConnector->any(c : Connector | c.oclIsTypeOf(uml::Connector)).end->size()=2";
		
		} else if(primType=="Grouping") {
			System.out.println("Grouping");
			
			myQuery="self.ownedMember->size()=0 and " +
					"self.importedMember()->forAll(c | c.oclIsTypeOf(Component)) and " +
					"self.getAppliedStereotypes()->any(name='Group')->notEmpty()";	
		} else if(primType=="Layers") {
			System.out.println("Layers");
			
			myQuery="self.nestedPackage.oclAsType(Package)->forAll(p1, p2 | p1<>p2 implies p1.importedMember->intersection(p2.importedMember)->isEmpty()) and " +
					"self.importedMember->forAll" +
					"(i | if i.oclAsType(Component).ownedConnector->isEmpty() then true else if" +
					"(Package.allInstances()->any(p | p.importedMember->includes(i)).oclAsType(Package).getValue(Package.allInstances()->select(p | p.importedMember->includes(i)).getAppliedStereotypes()->any(name='Layer'),'LayerNumber').oclAsType(Integer)) - " +
					"(Package.allInstances()->any(t | t.importedMember->includes(i.oclAsType(Component).ownedConnector.end.role.type->any(c | c <> i))).getValue(Package.allInstances()->select(t | t.importedMember->includes(i.oclAsType(Component).ownedConnector.end.role.type->any(c | c <> i))).getAppliedStereotypes()->any(name='Layer'),'LayerNumber').oclAsType(Integer))<=1 then true else false endif endif)";
		} else if (primType=="Aggregation Cascade") {
			System.out.println("Aggregation Cascade");
			
			//mistake in this code
			myQuery="self.ownedConnector.getApplicableStereotypes()->any(name='AggregationCascade')->notEmpty() and " +
					"self.general()->notEmpty() and " +
					"self.general() = self.ownedConnector.end.role->any(c | c <> self).type.oclAsType(Component).general() and " +
					"self.attribute->any(a | a.aggregation = AggregationKind :: shared)->notEmpty() and " +
					"self.attribute->any(a | a.aggregation = AggregationKind :: shared).type.oclAsType(Component).attribute->any(a | a.aggregation = AggregationKind :: shared)->notEmpty() and " +
					"self.attribute->any(a | a.aggregation = AggregationKind :: shared).type.oclAsType(Component).ownedConnector.getApplicableStereotypes()->any(name='AggregationCascade')->notEmpty() and " + //mistake in this line, checks for connector in the wrong component (should be top one)
					"self.general() = self.attribute->any(a | a.aggregation = AggregationKind :: shared).type.oclAsType(Component).general() and " +
					"self.attribute->any(a | a.aggregation = AggregationKind :: shared).lower = 1 and " +
					"self.attribute->any(a | a.aggregation = AggregationKind :: shared).upper = 1";
		} else if (primType=="Composition Cascade") {
			System.out.println("Composition Cascade");
			
			myQuery="";
		} else if (primType=="Shield") {
			System.out.println("Shield");
			
			myQuery="Package.allInstances()->any(importedMember->includes(self.ownedConnector.end.role.type->any(c | c <> self).oclAsType(Component))).getAppliedStereotypes()->any(name='Group')->notEmpty() and " +
					"self.ownedConnector.end->size()=2 and " +
					"self.ownedPort.required = self.ownedConnector.end.role.type->any(c| c <> self).oclAsType(Component).ownedPort.provided and " +
					"self.ownedConnector.end.role.type->any(c| c <> self).oclAsType(Component).ownedPort.getAppliedStereotypes()->any(name='ShieldPort')->notEmpty() and " +
					"self.ownedConnector.end.role.type->any(c| c <> self).oclAsType(Component).ownedPort.provided.getAppliedStereotypes()->any(name='IShield')->notEmpty() and " +
					"self.ownedPort.getRequireds().getAppliedStereotypes()->any(name='IShield')->notEmpty() and " +
					"self.ownedConnector.getAppliedStereotypes()->any(name='Shield')->notEmpty()";
		} else if (primType=="Typing") {
			System.out.println("Typing");
			
			myQuery="self.ownedConnector.end->size()=2 and " +
					"self.ownedConnector.getAppliedStereotypes()->any(name='TypeConnector')->notEmpty() and " +
					"self.ownedConnector.end.role.type->any(c| c <> self).oclAsType(Component).ownedConnector.getAppliedStereotypes()->any(name='SuperTypeConnector')->notEmpty()";
		} else if (primType=="Virtual Connector") {
			System.out.println("Virtual Connector");
			
			myQuery="self.ownedPort.required.getAppliedStereotypes()->any(name='IVirtual')->notEmpty() and " +
					"self.ownedConnector.getAppliedStereotypes()->any(name='VirtualConnector')->notEmpty()";
		} 
		boolean changed=false;
		boolean result=false;
		try {
			System.out.println("x");
			oclInv = oclHelper.createQuery(myQuery);
			System.out.println("xx");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("in catch of OCL check - 1");
			e.printStackTrace();
		}
		try {
			result = myOcl.check(myElement, oclInv);
			changed = true;
		}
		catch (Exception e) {
			System.out.println("in catch of OCL check - 2");
		}
		return result;
	}
}
