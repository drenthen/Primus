package action;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.ocl.utilities.impl.Bag;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import org.eclipse.ocl.OCL;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.helper.OCLHelper;
import org.eclipse.ocl.util.Tuple;
import action.ValidatePrimitiveAction.ValidatePrimitiveRequest;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Element;

public class OCLCheckAll {

	String myPrimitiveType;
	Element myElement;
	OCL myOcl;
	OCLHelper oclHelper;
	OCLExpression oclInv;
	OCLExpression oclInvNear;
	public String resultString="",resultStringNear="";
	public OCLCheckAll(String primitiveType, Element element,TransactionalEditingDomain domain) {
		myPrimitiveType = primitiveType;
		myElement=element;
		
		myOcl = org.eclipse.ocl.uml.OCL.newInstance(domain.getResourceSet());
		//myOcl = org.eclipse.ocl.uml.OCL.newInstance();
		oclHelper = myOcl.createOCLHelper();

		//ResourceSet resourceSet = element.getModel().get
		myOcl.setEvaluationTracingEnabled(true);
		myOcl.setParseTracingEnabled(true);
		
		oclHelper.setInstanceContext(element);

		oclInv=null;
		oclInvNear=null;
		
		oclHelper.setInstanceContext(element);
	}
	
	public boolean validatePrimitive(String primType) {
		PrimitiveRegister primitiveRegister = new PrimitiveRegister();
		String myQuery="";
		String nearQuery="";
		Object myObj,myObjNear;
		Bag myBag;
		Set mySet,mySetNear;
		org.eclipse.ocl.util.Tuple myTuple;
		org.eclipse.uml2.uml.Package myPackage;
		Component myComp;
		if(primType=="Callback") {
			myQuery=getCallbackQuery();
			nearQuery=getNearCallbackQuery();
		} else if(primType=="Indirection") {
			myQuery=getIndirectionQuery();
			nearQuery=getNearIndirectionQuery();
		} else if(primType=="Grouping") {
			myQuery=getGroupingQuery();
			nearQuery=getNearGroupingQuery();
		} else if(primType=="Layers") {
			myQuery=getLayersQuery();
			nearQuery=getNearLayersQuery();
		} else if (primType=="Aggregation Cascade") {
			myQuery=getAggregationCascadeQuery();
			nearQuery=getNearAggregationCascadeQuery();
		} else if (primType=="Composition Cascade") {
			myQuery=getCompositionCascadeQuery();
			nearQuery=getNearCompositionCascadeQuery();
		} else if (primType=="Shield") {
			myQuery=getShieldQuery();
			nearQuery=getNearShieldQuery();
		} else if (primType=="Typing") {
			myQuery=getTypingQuery();
			nearQuery=getNearTypingQuery();
		} else if (primType=="Super Typing") {
			myQuery=getSuperTypingQuery();
			nearQuery=getNearSuperTypingQuery();
		} else if (primType=="Virtual Connector") {
			myQuery=getVirtualConnectorQuery();
			nearQuery=getNearVirtualConnectorQuery();
		} else if (primType=="Callback detailed") {
			myQuery=getCallbackDetailedQuery();
		}
		boolean result=true;
		try {
			oclInv = oclHelper.createQuery(myQuery);
			oclInvNear = oclHelper.createQuery(nearQuery);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("in catch of OCL check - 1");
			result=false;
			e.printStackTrace();
		}
		try {
			if (primType=="Callback") {
				myObj = myOcl.evaluate(myElement, oclInv);
				mySet = (Set)myObj;
				
				myObjNear = myOcl.evaluate(myElement, oclInvNear);
				mySetNear = (Set)myObjNear;
				
				primitiveRegister.registerCallback(mySet,mySetNear);
			
			} else if(primType=="Indirection") {
				myObj = myOcl.evaluate(myElement, oclInv);
				mySet = (Set)myObj;

				myObjNear = myOcl.evaluate(myElement, oclInvNear);
				mySetNear = (Set)myObjNear;
				
				primitiveRegister.registerIndirection(mySet,mySetNear);				
			} else if(primType=="Grouping") {
				myObj = myOcl.evaluate(myElement, oclInv);
				mySet = (Set) myObj;
				
				myObjNear = myOcl.evaluate(myElement, oclInvNear);
				mySetNear = (Set)myObjNear;
				
				primitiveRegister.registerGrouping(mySet,mySetNear);
			} else if(primType=="Layers") {
				myObj = myOcl.evaluate(myElement, oclInv);
				mySet = (Set) myObj;
				
				myObjNear = myOcl.evaluate(myElement, oclInvNear);
				mySetNear = (Set)myObjNear;
				
				primitiveRegister.registerLayers(mySet,mySetNear);
			} else if(primType=="Aggregation Cascade") {
				myObj = myOcl.evaluate(myElement, oclInv);
				mySet = (Set)myObj;
				myObjNear = myOcl.evaluate(myElement, oclInvNear);
				mySetNear = (Set)myObjNear;
				primitiveRegister.registerAggregationCascade(mySet,mySetNear);
			} else if(primType=="Composition Cascade") {
				myObj = myOcl.evaluate(myElement, oclInv);
				mySet = (Set)myObj;
				
				myObjNear = myOcl.evaluate(myElement, oclInvNear);
				mySetNear = (Set)myObjNear;
				primitiveRegister.registerCompositionCascade(mySet,mySetNear);
			} else if(primType=="Shield") {
				myObj = myOcl.evaluate(myElement, oclInv);
				mySet = (Set)myObj;
				
				myObjNear = myOcl.evaluate(myElement, oclInvNear);
				mySetNear = (Set)myObjNear;
				primitiveRegister.registerShield(mySet,mySetNear);
			} else if(primType=="Typing") {
				myObj = myOcl.evaluate(myElement, oclInv);
				mySet = (Set)myObj;
				
				myObjNear = myOcl.evaluate(myElement, oclInvNear);
				mySetNear = (Set)myObjNear;
				primitiveRegister.registerTyping(mySet,mySetNear);
			} else if(primType=="Super Typing") {
				myObj = myOcl.evaluate(myElement, oclInv);
				mySet = (Set)myObj;
				
				myObjNear = myOcl.evaluate(myElement, oclInvNear);
				mySetNear = (Set)myObjNear;
				primitiveRegister.registerSuperTyping(mySet,mySetNear);
			} else if(primType=="Virtual Connector") {
				myObj = myOcl.evaluate(myElement, oclInv);
				mySet = (Set)myObj;
				
				myObjNear = myOcl.evaluate(myElement, oclInvNear);
				mySetNear = (Set)myObjNear;
				primitiveRegister.registerVirtualConnector(mySet,mySetNear);
			} else if(primType=="Callback detailed") {
				myObj = myOcl.evaluate(myElement, oclInv);
				mySet = (Set)myObj;
				
				primitiveRegister.registerCallbackDetailed(mySet);
			}
			resultString = resultString+primitiveRegister.resultString;
			resultStringNear = resultStringNear+primitiveRegister.resultStringNear;
		}
		catch (Exception e) {
			System.out.println("in catch of OCL check - 2");
			result=false;
		}
		return result;
	}
	public String getCallbackQuery() {
		String myQuery="Component.allInstances()->iterate(i;pairs : Set(Tuple(c1 : Component, s : Bag(Component))) = Set{} | "+ 
		"let comp : Component = i.oclAsType(Component), "+
		"stemp : Bag(Component) = comp.ownedConnector->select(j | "+
			"let callerPort : Port = j.oclAsType(Connector).end.partWithPort->any(owner=i).oclAsType(Port), "+
			"callbackPort : Port = j.oclAsType(Connector).end.partWithPort->any(owner<>i).oclAsType(Port) in "+
			"if "+
				"j.oclAsType(Connector).getAppliedStereotypes()->any(name='Callback')->notEmpty() and "+
				"j.oclAsType(Connector).end->size()=2 and "+
				"callerPort.getAppliedStereotypes()->any(name='EventPort')->notEmpty() and "+
				"callerPort.getProvideds().getAppliedStereotypes()->any(name='IEvent')->notEmpty() and "+
				"callerPort.getRequireds().getAppliedStereotypes()->any(name='ICallback')->notEmpty() and "+
				"callbackPort.getAppliedStereotypes()->any(name='CallbackPort')->notEmpty() and "+
				"callbackPort.getProvideds().getAppliedStereotypes()->any(name='ICallback')->notEmpty() and "+
				"callbackPort.getRequireds().getAppliedStereotypes()->any(name='IEvent')->notEmpty() and "+
				"callerPort.getProvideds() = callbackPort.getRequireds() and "+
				"callerPort.getRequireds() = callbackPort.getProvideds() "+
			"then "+
				"true "+
			"else "+
				"false "+
			"endif "+
			").oclAsType(Connector).end.role.type->select(c | c <> comp).oclAsType(Component) in "+
				"if "+
					"stemp->isEmpty() "+
				"then "+
					"pairs "+
				"else "+
					"pairs->including(Tuple{c1=comp, s = stemp })"+
				"endif"+
			")";
		return myQuery;
	}
	public String getNearCallbackQuery() {
		String myQuery="Component.allInstances()->iterate(i;pairs : Set(Tuple(c1 : Component, s : Bag(Component))) = Set{} | "+ 
		"let comp : Component = i.oclAsType(Component), "+
		"stemp : Bag(Component) = comp.ownedConnector->select(j | "+
			"if "+
				"j.oclAsType(Connector).getAppliedStereotypes()->any(name='Callback')->notEmpty() "+
			"then "+
				"true "+
			"else "+
				"false "+
			"endif "+
			").oclAsType(Connector).end.role.type->select(c | c <> comp).oclAsType(Component) in "+
				"if "+
					"stemp->isEmpty() "+
				"then "+
					"pairs "+
				"else "+
					"pairs->including(Tuple{c1=comp, s = stemp })"+
				"endif"+
			")";
		return myQuery;
	}
	public String getCallbackDetailedQuery() { 
		String myQuery="Component.allInstances()->collect(i | "+
							"i.ownedConnector->collect(conn | "+
					    	"let callerPortSter : Port = conn.end.partWithPort->any(owner=i).oclAsType(Port)->any(p | p.oclAsType(Port).getAppliedStereotypes()->any(name='EventPort')->notEmpty()), "+
							"callbackPortSter : Port = conn.end.partWithPort->any(owner<>i).oclAsType(Port)->any(p | p.oclAsType(Port).getAppliedStereotypes()->any(name='CallbackPort')->notEmpty()), "+
							"callerPort : Port = conn.end.partWithPort->any(owner=i).oclAsType(Port), "+
							"callbackPort : Port = conn.end.partWithPort->any(owner<>i).oclAsType(Port),"+	
							//"callbackPort2 : Port = conn.end.partWithPort->any(owner<>i).oclAsType(Port),"+
							"callbackConn : Connector = conn->any(p | p.oclAsType(Connector).getAppliedStereotypes()->any(name='Callback')->notEmpty()), "+
							"callbackConn2End : Connector = conn->any(p | p.end->size()=2), "+
							"otherComp : Component = if callbackPort->notEmpty() then callbackPort.owner.oclAsType(Component) else null endif, "+
							"callerProv : Interface = callerPort.getProvideds()->any(p | p.getAppliedStereotypes()->any(name='IEvent')->notEmpty()), "+
							"callerReq : Interface = callerPort.getRequireds()->any(p | p.getAppliedStereotypes()->any(name='ICallback')->notEmpty()), "+
							"callbackProv : Interface = callbackPort.getProvideds()->any(getAppliedStereotypes()->any(name='ICallback')->notEmpty()), "+
							"callbackReq : Interface = callbackPort.getRequireds()->any(getAppliedStereotypes()->any(name='IEvent')->notEmpty()), "+
							"observeEventMatch : Integer = if callerPort.getProvideds() = callbackPort.getRequireds() and callerPort.getProvideds()->notEmpty() then 1 else 0 endif, "+
							"updateMatch : Integer = if callerPort.getRequireds() = callbackPort.getProvideds() and callerPort.getRequireds()->notEmpty() then 1 else 0 endif in "+
					    	"Tuple{c1 = i, c2 = otherComp,callbackConn = callbackConn,callbackConn2End=callbackConn2End,callerPort=callerPortSter,callbackPort = callbackPortSter,callerProv = callerProv,callerReq = callerReq,callbackProv = callbackProv,callbackReq = callbackReq,observeEventMatch = observeEventMatch, updateMatch = updateMatch}))->asSet()";
							//"Tuple{a1 = i, a2 = otherComp,a3 = callbackConn,a4=callerPort,a5 = callbackPort,a6 = callerProv,a7 = callerReq,a8 = callbackProv,a9 = callbackReq,a10 = observeEventMatch, a11 = updateMatch}))->asSet()";
		return myQuery;
	}
	public String getIndirectionQuery() {
		String myQuery=
			"Component.allInstances()->iterate(i;pairs : Set(Tuple(c1 : Component, client : Set(Component), target : Bag(Component))) = Set{} | "+ 
				"let comp : Component = i.oclAsType(Component), "+
				"clienttemp : Set(Component) = Component.allInstances()->select(a | "+ 
					"a.oclAsType(Component).ownedConnector->any(b | "+
						"let clientPort : Port = b.end.partWithPort->any(owner=a).oclAsType(Port), "+
						"otherPort : Port = b.end.partWithPort->any(owner<>a).oclAsType(Port) in "+
							"clientPort.getRequireds().getAppliedStereotypes()->any(name='Indirector')->notEmpty() and "+
							"otherPort.getProvideds().getAppliedStereotypes()->any(name='Indirector')->notEmpty() and "+
							"clientPort.getRequireds() = otherPort.getProvideds() and "+
							"otherPort.owner.oclAsType(Component) = comp "+
				")->notEmpty() and a.oclAsType(Component) <> i), "+
				"stemp : Bag(Component) = comp.ownedConnector->select(c | "+
					"let indirectorPort : Port = c.oclAsType(Connector).end.partWithPort->any(owner=i).oclAsType(Port), "+ 
					"targetPort : Port = c.oclAsType(Connector).end.partWithPort->any(owner<>i).oclAsType(Port) in "+
						"c.oclAsType(Connector).getAppliedStereotypes()->any(name='Indirection')->notEmpty() and "+ 
						"c.oclAsType(Connector).end->size()=2 and "+
						"targetPort.getAppliedStereotypes()->any(name='IndirectionTargetPort')->notEmpty() and "+
						"targetPort.getProvideds().getAppliedStereotypes()->any(name='ITarget')->notEmpty() and "+ 
						"indirectorPort.getAppliedStereotypes()->any(name='IndirectionPort')->notEmpty() and "+
						"indirectorPort.getRequireds().getAppliedStereotypes()->any(name='ITarget')->notEmpty() and "+
						"indirectorPort.required = targetPort.provided "+
					").oclAsType(Connector).end.role.type->select(c | c <> comp).oclAsType(Component) in "+ 
					"if "+
						"stemp->isEmpty() "+ 
					"then "+
						"pairs "+
					"else "+
						"pairs->including(Tuple{c1=comp, client = clienttemp, target = stemp }) "+
					"endif "+
					")";
		return myQuery;
	}
	public String getNearIndirectionQuery() {
		String myQuery="Component.allInstances()->iterate(i;pairs : Set(Tuple(c1 : Component, client : Set(Component), target : Bag(Component))) = Set{} | "+ 
		"let comp : Component = i.oclAsType(Component), "+
		"clienttemp : Set(Component) = Component.allInstances()->select(a | "+ 
			"a.oclAsType(Component).ownedConnector->any(b | "+
				"let clientPort : Port = b.end.partWithPort->any(owner=a).oclAsType(Port), "+
				"otherPort : Port = b.end.partWithPort->any(owner<>a).oclAsType(Port) in "+
					"otherPort.owner.oclAsType(Component) = comp "+
		")->notEmpty() and a.oclAsType(Component) <> i), "+
		"stemp : Bag(Component) = comp.ownedConnector->select(c | "+
				"c.oclAsType(Connector).getAppliedStereotypes()->any(name='Indirection')->notEmpty() "+ 
			").oclAsType(Connector).end.role.type->select(c | c <> comp).oclAsType(Component) in "+ 
			"if "+
				"stemp->isEmpty() "+ 
			"then "+
				"pairs "+
			"else "+
				"pairs->including(Tuple{c1=comp, client = clienttemp, target = stemp }) "+
			"endif "+
			")";
		return myQuery;
		
	}
	public String getGroupingQuery() {
		String myQuery="Package.allInstances()->select(i | " +
		"i.oclIsKindOf(Package) and "+
		"i.oclAsType(Package).ownedMember->size()=0 and "+
		"i.oclAsType(Package).importedMember->forAll(c | c.oclIsTypeOf(Component)) and "+
		"i.oclAsType(Package).getAppliedStereotypes()->any(name='Group')->notEmpty() "+
		")";	
		return myQuery;
	}
	public String getNearGroupingQuery() {
		String myQuery="Package.allInstances()->select(i | " +
		"i.oclIsKindOf(Package) and "+
		"i.oclAsType(Package).getAppliedStereotypes()->any(name='Group')->notEmpty() "+
		")";	
		return myQuery;
	}
	public String getLayersQuery() {
		String myQuery="Package.allInstances()->select(z | "+
						"if "+
							"z.oclAsType(Package).getAppliedStereotypes()->any(name='Layer')->notEmpty() and "+ 
							"z.oclAsType(Package).ownedMember->size()=0 and "+
							"z.oclAsType(Package).importedMember->forAll (i | "+ 
								"i.oclIsKindOf(Component) and "+ 
								"Package.allInstances()->select(q | q.getAppliedStereotypes()->any(name='Layer')->notEmpty())->select(a | a.oclAsType(Package).importedMember->any(b | b=i)->notEmpty())->size()=1 and "+
									"i.oclAsType(Component).ownedConnector->forAll(d | "+
										"let connectingLayer : Package =  Package.allInstances()->select(q | q.getAppliedStereotypes()->any(name='Layer')->notEmpty())->any(p | p.importedMember->includes(i)).oclAsType(Package), "+
										"connectedLayer : Set(Package) = Package.allInstances()->select(q | q.getAppliedStereotypes()->any(name='Layer')->notEmpty())->select(t | t.importedMember->includes(d.oclAsType(Connector).end.role.type->any(c | c <> i))) in "+
										"if "+
											"connectedLayer->notEmpty() then "+ 
												"if "+
													"(connectingLayer.getValue(connectingLayer.getAppliedStereotypes()->any(name='Layer'),'LayerNumber').oclAsType(Integer))- "+
													"(connectedLayer->any(true).getValue(connectedLayer.getAppliedStereotypes()->any(name='Layer'),'LayerNumber').oclAsType(Integer))<=1 "+
												"then true else false endif "+
												"else true endif "+
								 	")" +
							") "+
							"then true else false endif "+
							")";
		return myQuery;
	}
	public String getNearLayersQuery() {
		String myQuery="Package.allInstances()->select(i | " +
		"i.oclIsKindOf(Package) and "+
		"i.oclAsType(Package).getAppliedStereotypes()->any(name='Layer')->notEmpty() "+
		")";
		return myQuery;
	}
	public String getAggregationCascadeQuery() {
		/*String myQuery="Component.allInstances()->iterate(i;sel : Set(Tuple(c1 : Component,c2 : Component,s : Set(Component))) = Set{} | "+
						"let stemp:Set(Component) = "+
						"i.oclAsType(Component)->closure(j | "+ 
							"if "+
								"j.oclAsType(Component).ownedConnector->select(type<>null).type.memberEnd->select(aggregation=AggregationKind::shared).opposite->forAll(c1,c2 | c1<>c2 implies c1.name=c2.name) "+
							"then "+
								"j.oclAsType(Component).ownedConnector->select(c | "+ 
									"j.oclAsType(Connector).getAppliedStereotypes()->any(name='AggregationCascade')->notEmpty() and "+
										"c.oclAsType(Connector).type->size()=1 and "+
										"c.oclAsType(Connector).type.ownedEnd->isEmpty() and "+
										"c.oclAsType(Connector)->any(d | d.type->any(memberEnd->exists(aggregation = AggregationKind :: shared))->notEmpty())->notEmpty() "+
									").oclAsType(Connector).end.role.type->select(c | c <> j).oclAsType(Component) "+
								"else "+
									"null "+
								"endif "+
							")->select(k | k.oclAsType(Component).general().oclAsType(Component) = i.general().oclAsType(Component)) "+
						"in "+
						"if "+ 
							"Component.allInstances()->any(a | "+
								"a.oclAsType(Component).ownedConnector->any(b | "+
								"b.oclAsType(Connector).getAppliedStereotypes()->any(name='AggregationCascade')->notEmpty() and "+
								"b.oclAsType(Connector).end.role.type->any(c | c=i and b.owner <> i)->notEmpty())->notEmpty())->isEmpty() and "+
								"i.oclAsType(Component).attribute->select(a | a.aggregation = AggregationKind :: none and a.association->notEmpty())->any(b | "+
									"b.oclAsType(Property).type.oclAsType(Component).attribute->any(c | c.aggregation = AggregationKind :: shared and c.type = i)->notEmpty() "+
								")->isEmpty() and "+
								"i.attribute->any(a | a.aggregation = AggregationKind :: shared)->notEmpty() and "+
							"stemp->notEmpty() "+ 
						"then "+
							"sel->including(Tuple{c1 = i.general().oclAsType(Component)->any(true),c2 = i.oclAsType(Component),s = stemp}) "+ 
						"else "+
							"sel "+
						"endif "+ 
						")";*/
		
		String myQuery="Component.allInstances()->iterate(i;sel : Set(Tuple(c1 : Component,c2 : Component,s : Set(Component))) = Set{} | "+ 
						"let stemp:Set(Component) = "+
							"i.oclAsType(Component)->closure(j | "+ 
								"if "+
									"j.oclAsType(Component).ownedConnector->select(type<>null).type.memberEnd->select(aggregation=AggregationKind::shared).opposite->forAll(c1,c2 | c1<>c2 implies c1.name=c2.name) "+
								"then "+
									"j.oclAsType(Component).ownedConnector->select(c | "+
										"c.oclAsType(Connector).getAppliedStereotypes()->any(name='AggregationCascade')->notEmpty() and "+
										"c.oclAsType(Connector).type->size()=1 and "+
										"c.oclAsType(Connector).type.ownedEnd->isEmpty() and "+
										"c.oclAsType(Connector)->any(d | d.type->any(memberEnd->exists(aggregation = AggregationKind :: shared))->notEmpty())->notEmpty() "+
									").oclAsType(Connector).end.role.type->select(c | c <> j).oclAsType(Component) "+ 
								"else "+
									"null "+
								"endif "+
							")->select(k | k.oclAsType(Component).general.oclAsType(Component) = i.general.oclAsType(Component)) "+
							"in "+
							"if "+
								"Component.allInstances()->any(a | "+ 
									"a.oclAsType(Component).ownedConnector->any(b | "+
										"b.oclAsType(Connector).getAppliedStereotypes()->any(name='AggregationCascade')->notEmpty() and "+
										"b.oclAsType(Connector).end.role.type->any(c | c=i and b.owner <> i)->notEmpty())->notEmpty())->isEmpty() and "+
										"i.oclAsType(Component).attribute->select(a | a.aggregation = AggregationKind :: none and a.association->notEmpty())->any(b | "+
										"b.oclAsType(Property).type.oclAsType(Component).attribute->any(c | c.aggregation = AggregationKind :: shared and c.type = i)->notEmpty() "+ 
										")->isEmpty() and "+
										"i.attribute->any(a | a.aggregation = AggregationKind :: shared)->notEmpty() and "+ 
									"stemp->notEmpty() "+ 
							"then "+
								"sel->including(Tuple{c1 = i.general.oclAsType(Component)->any(true),c2 = i.oclAsType(Component),s = stemp}) "+ 
							"else "+
								"sel "+
							"endif "+
							")";
		return myQuery;
	}
	public String getNearAggregationCascadeQuery() {
		String myQuery="Component.allInstances()->iterate(i;sel : Set(Tuple(c2 : Component,s : Set(Component))) = Set{} | "+
					"let stemp:Set(Component) = "+
						"i.oclAsType(Component)->closure(j | "+ 
							"if "+
								"j.oclAsType(Component).ownedConnector->any(c | c.oclAsType(Connector).getAppliedStereotypes()->any(name='AggregationCascade')->notEmpty())->notEmpty() "+
							"then "+  
								"j.oclAsType(Component).ownedConnector->select(c | c.oclAsType(Connector).getAppliedStereotypes()->any(name='AggregationCascade')->notEmpty()).oclAsType(Connector).end.role.type->select(c | c <> j).oclAsType(Component) "+ 
							"else "+
								"null "+
							"endif "+
						") "+
					"in "+
					"if "+
						"Component.allInstances()->any(a | "+
							"a.oclAsType(Component).ownedConnector->any(b | "+
								"b.oclAsType(Connector).getAppliedStereotypes()->any(name='AggregationCascade')->notEmpty() and "+
								"b.oclAsType(Connector).end.role.type->any(c | c=i and b.owner <> i)->notEmpty())->notEmpty())->isEmpty() and "+
								"stemp->notEmpty() "+
					"then "+
						"sel->including(Tuple{c2 = i.oclAsType(Component),s = stemp}) "+ 
					"else "+
						"sel "+
					"endif "+ 
					")";
		return myQuery;
	}
	public String getCompositionCascadeQuery() {
		String myQuery="Component.allInstances()->iterate(i;sel : Set(Tuple(c2 : Component,s : Set(Component))) = Set{} | "+ 
						"let stemp:Set(Component) = "+
						"i.oclAsType(Component)->closure(j | " + 
							"if "+
								"j.oclAsType(Component).ownedConnector->any(c | c.oclAsType(Connector)->any(d | d.type->any(memberEnd->exists(aggregation = AggregationKind :: composite))->notEmpty())->notEmpty() and c.oclAsType(Connector).getAppliedStereotypes()->any(name='CompositionCascade')->notEmpty())->notEmpty() and "+
								"j.general()->select(oclIsKindOf(Component))->includes(j.oclAsType(Component).ownedConnector->any(c | c.oclAsType(Connector).getAppliedStereotypes()->any(name='CompositionCascade')->notEmpty()).end.role.type->any(c | c <> j).oclAsType(Component)) and "+
								"j.attribute->select(aggregation = AggregationKind::composite).association->select(a | not(a.oclIsUndefined())).memberEnd.owner->includes(j.oclAsType(Component).ownedConnector->any(c | c.oclAsType(Connector).getAppliedStereotypes()->any(name='CompositionCascade')->notEmpty()).end.role.type->any(c | c <> j).oclAsType(Component)) "+
							"then "+
								"j.oclAsType(Component).ownedConnector->any(c | c.oclAsType(Connector).getAppliedStereotypes()->any(name='CompositionCascade')->notEmpty()).oclAsType(Connector).end.role.type->select(c | c <> j).oclAsType(Component)->any(true) "+ 
							"else "+
								"null "+ 
							"endif "+ 
							") "+
							"in "+
							"if "+
								"Component.allInstances()->any(a | "+
									"a.oclAsType(Component).ownedConnector->any(b | "+
									"b.oclAsType(Connector).getAppliedStereotypes()->any(name='CompositionCascade')->notEmpty() and "+
									"b.oclAsType(Connector).end.role.type->any(c | c=i and b.owner <> i)->notEmpty())->notEmpty())->isEmpty() and "+
									"i.oclAsType(Component).attribute->select(a | a.aggregation = AggregationKind :: none and a.association->notEmpty())->any(b | "+
									"b.oclAsType(Property).type.oclAsType(Component).attribute->any(c | c.aggregation = AggregationKind :: composite and c.type = i)->notEmpty() "+ 
								")->isEmpty() and "+
								"i.attribute->any(a | a.aggregation = AggregationKind :: composite)->notEmpty() and "+ 
								"stemp->notEmpty() "+ 
							"then "+
								"sel->including(Tuple{c2 = i.oclAsType(Component),s = stemp}) "+ 
							"else "+
								"sel "+
							"endif "+ 
							")";
		return myQuery;
	}
	public String getNearCompositionCascadeQuery() {
		String myQuery="Component.allInstances()->iterate(i;sel : Set(Tuple(c2 : Component,s : Set(Component))) = Set{} | "+ 
						"let stemp:Set(Component) = "+ 
						"i.oclAsType(Component)->closure(j | "+ 
							"if "+
								"j.oclAsType(Component).ownedConnector->any(c | c.oclAsType(Connector).getAppliedStereotypes()->any(name='CompositionCascade')->notEmpty())->notEmpty() "+
							"then "+
								"j.oclAsType(Component).ownedConnector->any(c | c.oclAsType(Connector).getAppliedStereotypes()->any(name='CompositionCascade')->notEmpty()).oclAsType(Connector).end.role.type->select(c | c <> j).oclAsType(Component)->any(true) "+ 
							"else "+
								"null "+
							"endif "+ 
							") "+
							"in "+
							"if "+
								"Component.allInstances()->any(a | "+
									"a.oclAsType(Component).ownedConnector->any(b | "+
										"b.oclAsType(Connector).getAppliedStereotypes()->any(name='CompositionCascade')->notEmpty() and "+ 
										"b.oclAsType(Connector).end.role.type->any(c | c=i and b.owner <> i)->notEmpty() "+
									")->notEmpty() "+
								")->isEmpty() and  "+
								"i.attribute->any(a | a.aggregation = AggregationKind :: composite)->notEmpty() and "+ 
								"stemp->notEmpty() "+ 
							"then "+
								"sel->including(Tuple{c2 = i.oclAsType(Component),s = stemp}) "+ 
							"else "+
								"sel "+
							"endif "+ 
							")";
		return myQuery;
	}
	public String getShieldQuery() {
		/*String myQuery="Component.allInstances()->iterate(i;sel : Set(Tuple(c : Component,group: Package,client : Set(Component))) = Set{} | "+
						"let comp : Component = i.oclAsType(Component), "+
						"clienttemp : Set(Component) =  Component.allInstances()->select(c | "+ 
							"c.ownedConnector->any(conn | "+
								"let thisPort : Port = conn.oclAsType(Connector).end.partWithPort->any(owner=comp).oclAsType(Port), "+
								"otherPort : Port = conn.oclAsType(Connector).end.partWithPort->any(owner<>comp).oclAsType(Port) in "+ 
								"thisPort.getAppliedStereotypes()->any(name='ShieldPort')->notEmpty() and "+ 
								"conn.getAppliedStereotypes()->any(name='Shield')->notEmpty() and "+
								"thisPort.getProvideds().getAppliedStereotypes()->any(name='IShield')->notEmpty() and "+
								"otherPort.getRequireds().getAppliedStereotypes()->any(name='IShield')->notEmpty() and "+ 
								"thisPort.getProvideds() = otherPort.getRequireds() "+ 
						")->notEmpty()), "+
						"shieldPortStereotype : Stereotype = comp.ownedPort->any(true).getApplicableStereotypes()->any(name='ShieldPort'), "+
						"shieldgrouptemp : Package = Package.allInstances()->any(name=(comp.ownedPort->any(p | p.getAppliedStereotypes()->any(name='ShieldPort')->notEmpty()).getValue(shieldPortStereotype,'shieldGroup').oclAsType(String))), "+
						"shieldedcomponents : Set(Component) = Component.allInstances()->select(s | shieldgrouptemp.importedMember->includes(s) and not( "+
						"Package.allInstances()->any(name=(s.ownedPort->any(p | p.getAppliedStereotypes()->any(name='ShieldPort')->notEmpty()).getValue(shieldPortStereotype,'shieldGroup').oclAsType(String)))=shieldgrouptemp)), "+
						"notinshieldgroup : Set(Component) = Component.allInstances()->select(s | shieldgrouptemp.importedMember->excludes(s))->select(s | clienttemp->excludes(s)) "+
						"in "+
						"if "+
							"clienttemp->notEmpty() and "+
							"notinshieldgroup->select(n | n.oclAsType(Component).ownedConnector.end.role.type.oclAsType(Component)->select(m | shieldedcomponents->includes(m))->notEmpty())->isEmpty() "+
						"then "+
							"sel->including(Tuple{c = comp, group = shieldgrouptemp,client = clienttemp}) "+ 
						"else "+ 
							"sel "+ 
						"endif "+ 
						")";
		*/
		String myQuery="Component.allInstances()->iterate(i;sel : Set(Tuple(c : Component,group: Package,client : Set(Component))) = Set{} | "+ 
						"let comp : Component = i.oclAsType(Component), "+
						"clienttemp : Set(Component) =  Component.allInstances()->select(c | "+ 
							"c.ownedConnector->any(conn | "+
								"let thisPort : Port = conn.oclAsType(Connector).end.partWithPort->any(owner=comp).oclAsType(Port), "+
								"otherPort : Port = conn.oclAsType(Connector).end.partWithPort->any(owner<>comp).oclAsType(Port) in "+ 
								"thisPort.getAppliedStereotypes()->any(name='ShieldPort')->notEmpty() and "+ 
								"conn.getAppliedStereotypes()->any(name='Shield')->notEmpty() and "+ 
								"thisPort.getProvideds()->any(k | "+
									"k.getAppliedStereotypes()->any(name='IShield')->notEmpty() and "+
									"k.feature->forAll(f | f.oclAsType(Feature).visibility = VisibilityKind :: public) and "+
									"Package.allInstances().importedMember.oclAsType(Component).provided->includes(k) "+
								")->notEmpty() and "+
								"otherPort.getRequireds().getAppliedStereotypes()->any(name='IShield')->notEmpty() and "+ 
								"thisPort.getProvideds() = otherPort.getRequireds() and "+
								"conn.end->size()=2 and "+
								"conn.type->size()=1 and "+
								"conn.type.ownedEnd->isEmpty() "+
							")->notEmpty()), "+
							"shieldPortStereotype : Stereotype = comp.ownedPort->any(true).getApplicableStereotypes()->any(name='ShieldPort'), "+
							"shieldgrouptemp : Package = Package.allInstances()->any(name=(comp.ownedPort->any(p | p.getAppliedStereotypes()->any(name='ShieldPort')->notEmpty()).getValue(shieldPortStereotype,'shieldGroup').oclAsType(String))), "+
							"shieldedcomponents : Set(Component) = Component.allInstances()->select(s | shieldgrouptemp.importedMember->includes(s) and not( "+
							"Package.allInstances()->any(name=(s.ownedPort->any(p | p.getAppliedStereotypes()->any(name='ShieldPort')->notEmpty()).getValue(shieldPortStereotype,'shieldGroup').oclAsType(String)))=shieldgrouptemp)), "+ 
							"notinshieldgroup : Set(Component) = Component.allInstances()->select(s | shieldgrouptemp.importedMember->excludes(s))->select(s | clienttemp->excludes(s)) "+ 
						"in "+
						"if "+
							"clienttemp->notEmpty() and "+
							"notinshieldgroup->select(n | n.oclAsType(Component).ownedConnector.end.role.type.oclAsType(Component)->select(m | shieldedcomponents->includes(m))->notEmpty())->isEmpty() "+ 
						"then "+
							"sel->including(Tuple{c = comp, group = shieldgrouptemp,client = clienttemp}) "+ 
						"else "+
							"sel "+
						"endif "+ 
						")";
		return myQuery;
	}
	public String getNearShieldQuery() {
		String myQuery="Component.allInstances()->iterate(i;sel : Set(Tuple(c : Component,group: Package,client : Set(Component))) = Set{} | "+
						"let comp : Component = i.oclAsType(Component), "+
							"clienttemp : Set(Component) =  Component.allInstances()->select(c | "+
								"c.ownedConnector->any(conn | "+
									"let thisPort : Port = conn.oclAsType(Connector).end.partWithPort->any(owner=comp).oclAsType(Port), "+
									"otherPort : Port = conn.oclAsType(Connector).end.partWithPort->any(owner<>comp).oclAsType(Port) in "+ 
									"thisPort.getAppliedStereotypes()->any(name='ShieldPort')->notEmpty() "+
								")->notEmpty()), "+
							"shieldPortStereotype : Stereotype = comp.ownedPort->any(true).getApplicableStereotypes()->any(name='ShieldPort'), "+
							"shieldgrouptemp : Package = Package.allInstances()->any(name=(comp.ownedPort->any(p | p.getAppliedStereotypes()->any(name='ShieldPort')->notEmpty()).getValue(shieldPortStereotype,'shieldGroup').oclAsType(String))) "+
							"in "+
							"if "+
								"clienttemp->notEmpty() "+
							"then "+
								"sel->including(Tuple{c = comp, group = shieldgrouptemp,client = clienttemp}) "+ 
							"else "+
								"sel "+
							"endif "+ 
						")";
		return myQuery;
	}
	public String getTypingQuery() {
		/*String myQuery="Component.allInstances()->iterate(i;sel : Set(Tuple(c1 : Component, s : Bag(Component))) = Set{} | " +
			"let comp : Component = i.oclAsType(Component), "+
			"c2temp : Bag(Component) = comp.ownedConnector->select(j | "+ 
					"if "+
					"j.oclAsType(Connector).getAppliedStereotypes()->any(name='TypeConnector')->notEmpty() "+
					"then true else false "+
					"endif "+
			").oclAsType(Connector).end.role.type->select(c | c <> comp).oclAsType(Component) in "+ 
			"if "+
				"c2temp->isEmpty() or "+
				"comp->closure(l | "+
					"l.oclAsType(Component).ownedConnector->select(conn | conn.getAppliedStereotypes()->any(name='TypeConnector')->notEmpty()).end.role.type->select(m | m <> l).oclAsType(Component) "+ 
				")->includes(comp) "+
				"then "+
				"sel "+
				"else  "+
					"sel->including(Tuple{c1=comp, s = c2temp }) "+
					"endif "+
			")";*/
		
		String myQuery="Component.allInstances()->iterate(i;sel : Set(Tuple(c1 : Component, s : Bag(Component), s1 : Bag(Component))) = Set{} | "+ 
							"let comp : Component = i.oclAsType(Component), "+
							"c2temp : Bag(Component) = comp.ownedConnector->select(j | "+ 
							"if "+
								"j.oclAsType(Connector).getAppliedStereotypes()->any(name='TypeConnector')->notEmpty() and "+
								"j.oclAsType(Connector).end->size()=2 "+
							"then true else false "+
							"endif "+
							").oclAsType(Connector).end.role.type->select(c | c <> comp).oclAsType(Component), "+
							"c3temp : Bag(Component) = comp.ownedConnector->select(j | "+ 
							"if "+
								"j.oclAsType(Connector).getAppliedStereotypes()->any(name='SuperTypeConnector')->notEmpty() and "+
								"j.oclAsType(Connector).end->size()=2 "+
							"then true else false "+
							"endif "+
							").oclAsType(Connector).end.role.type->select(c | c <> comp).oclAsType(Component) in "+
							"if "+
								"( "+
								"c2temp->notEmpty() and "+ 
								"comp->closure(l | "+
									"l.oclAsType(Component).ownedConnector->select(conn | conn.getAppliedStereotypes()->any(name='TypeConnector')->notEmpty()).end.role.type->select(m | m <> l).oclAsType(Component) "+ 
								")->excludes(comp) "+ 
								") or "+
								"( "+
								"c3temp->notEmpty() and "+ 
								"comp->closure(l | "+
									"l.oclAsType(Component).ownedConnector->select(conn | conn.getAppliedStereotypes()->any(name='SuperTypeConnector')->notEmpty()).end.role.type->select(m | m <> l).oclAsType(Component) "+ 
								")->excludes(comp) "+ 
								") "+
							"then "+
								"sel->including(Tuple{c1=comp, s = c2temp, s1 = c3temp }) "+  
							"else "+ 
								"sel "+
							"endif "+
							")";
		return myQuery;
	}
	public String getNearTypingQuery() {
		/*String myQuery="Component.allInstances()->iterate(i;sel : Set(Tuple(c1 : Component, s : Bag(Component))) = Set{} | " +
		"let comp : Component = i.oclAsType(Component), "+
		"c2temp : Bag(Component) = comp.ownedConnector->select(j | "+ 
				"if "+
				"j.oclAsType(Connector).getAppliedStereotypes()->any(name='TypeConnector')->notEmpty() "+
				"then true else false "+
				"endif "+
		").oclAsType(Connector).end.role.type->select(c | c <> comp).oclAsType(Component) in "+ 
		"if "+
			"c2temp->isEmpty()"+
			"then "+
			"sel "+
			"else  "+
				"sel->including(Tuple{c1=comp, s = c2temp }) "+
				"endif "+
		")";*/
		String myQuery="Component.allInstances()->iterate(i;sel : Set(Tuple(c1 : Component, s : Bag(Component), s1 : Bag(Component))) = Set{} | "+ 
							"let comp : Component = i.oclAsType(Component), "+
							"c2temp : Bag(Component) = comp.ownedConnector->select(j | "+ 
							"if "+
								"j.oclAsType(Connector).getAppliedStereotypes()->any(name='TypeConnector')->notEmpty() "+
							"then true else false "+
							"endif "+
							").oclAsType(Connector).end.role.type->select(c | c <> comp).oclAsType(Component), "+
							"c3temp : Bag(Component) = comp.ownedConnector->select(j | "+ 
							"if "+
								"j.oclAsType(Connector).getAppliedStereotypes()->any(name='SuperTypeConnector')->notEmpty() "+
							"then true else false "+
							"endif "+
							").oclAsType(Connector).end.role.type->select(c | c <> comp).oclAsType(Component) in "+
							"if "+
								"c2temp->notEmpty() or c3temp->notEmpty() "+ 
							"then "+
								"sel->including(Tuple{c1=comp, s = c2temp, s1 = c3temp }) "+  
							"else "+ 
								"sel "+
							"endif "+ 
							")";
		return myQuery;
	}
	public String getSuperTypingQuery() {
		String myQuery="Component.allInstances()->iterate(i;sel : Set(Tuple(c1 : Component, s : Bag(Component))) = Set{} | " +
			"let comp : Component = i.oclAsType(Component), "+
			"c2temp : Bag(Component) = comp.ownedConnector->select(j | "+ 
					"if "+
					"j.oclAsType(Connector).getAppliedStereotypes()->any(name='SuperTypeConnector')->notEmpty() "+
					"then true else false "+
					"endif "+
			").oclAsType(Connector).end.role.type->select(c | c <> comp).oclAsType(Component) in "+ 
			"if "+
			"Component.allInstances()->select(s | s<>comp and s.ownedConnector->select(u | u.getAppliedStereotypes()->any(name='TypeConnector')->notEmpty()).end.role.type->select(t | t=comp)->notEmpty() )->isEmpty() or "+
				"c2temp->isEmpty() or "+
				"comp->closure(l | "+
					"l.oclAsType(Component).ownedConnector->select(conn | conn.getAppliedStereotypes()->any(name='SuperTypeConnector')->notEmpty()).end.role.type->select(m | m <> l).oclAsType(Component) "+ 
				")->includes(comp) "+
				"then "+
				"sel "+
				"else  "+
					"sel->including(Tuple{c1=comp, s = c2temp }) "+
					"endif "+
			")";
		return myQuery;
	}
	public String getNearSuperTypingQuery() {
		String myQuery="Component.allInstances()->iterate(i;sel : Set(Tuple(c1 : Component, s : Bag(Component))) = Set{} | " +
		"let comp : Component = i.oclAsType(Component), "+
		"c2temp : Bag(Component) = comp.ownedConnector->select(j | "+ 
				"if "+
				"j.oclAsType(Connector).getAppliedStereotypes()->any(name='SuperTypeConnector')->notEmpty() "+
				"then true else false "+
				"endif "+
		").oclAsType(Connector).end.role.type->select(c | c <> comp).oclAsType(Component) in "+ 
		"if "+
			"c2temp->isEmpty()"+
			"then "+
			"sel "+
			"else  "+
				"sel->including(Tuple{c1=comp, s = c2temp }) "+
				"endif "+
		")";
		return myQuery;
	}
	public String getVirtualConnectorQuery() {
		String myQuery="Component.allInstances()->iterate(i;sel : Set(Tuple(c1 : Component, s : Bag(Component))) = Set{} | "+
			"let comp : Component = i.oclAsType(Component), "+
			"stemp : Bag(Component) = comp.ownedConnector->select(j | "+
					"let callerPort : Port = j.oclAsType(Connector).end.partWithPort->any(owner=i).oclAsType(Port), "+
					"receiverPort : Port = j.oclAsType(Connector).end.partWithPort->any(owner<>i).oclAsType(Port) in "+
					"j.oclAsType(Connector).getAppliedStereotypes()->any(name='VirtualConnector')->notEmpty() and "+
					"j.oclAsType(Connector).end->size()=2 and "+
					"callerPort.getRequireds().getAppliedStereotypes()->any(name='IVirtual')->notEmpty() and "+
					"receiverPort.getProvideds().getAppliedStereotypes()->any(name='IVirtual')->notEmpty() and "+
					"callerPort.getRequireds() = receiverPort.getProvideds() and "+
					"callerPort.owner->closure(l | "+
							"Component.allInstances()->select(s | (s<>l and s.ownedConnector->select(t | t.oclAsType(Connector).getAppliedStereotypes()->any(name='VirtualConnector')->isEmpty()).end.role.type->select(m | m=l)->notEmpty()) or l.oclAsType(Component).ownedConnector->select(t | t.oclAsType(Connector).getAppliedStereotypes()->any(name='VirtualConnector')->isEmpty()).end.role.type->select(t | t<>l)->includes(s))  "+
					")->includes(receiverPort.owner.oclAsType(Component)) "+
			").oclAsType(Connector).end.role.type->select(c | c <> comp).oclAsType(Component) in "+
			"if "+
			"stemp->isEmpty() "+
			"then "+
			"sel "+
			"else "+
				"sel->including(Tuple{c1=comp, s = stemp}) "+
				"endif "+
				")"; 
		return myQuery;
	}
	public String getNearVirtualConnectorQuery() {
		String myQuery="Component.allInstances()->iterate(i;sel : Set(Tuple(c1 : Component, s : Bag(Component))) = Set{} | "+
			"let comp : Component = i.oclAsType(Component), "+
			"stemp : Bag(Component) = comp.ownedConnector->select(j | "+
					"let callerPort : Port = j.oclAsType(Connector).end.partWithPort->any(owner=i).oclAsType(Port), "+
					"receiverPort : Port = j.oclAsType(Connector).end.partWithPort->any(owner<>i).oclAsType(Port) in "+
					"j.oclAsType(Connector).getAppliedStereotypes()->any(name='VirtualConnector')->notEmpty()"+
			").oclAsType(Connector).end.role.type->select(c | c <> comp).oclAsType(Component) in "+
			"if "+
			"stemp->isEmpty() "+
			"then "+
			"sel "+
			"else "+
				"sel->including(Tuple{c1=comp, s = stemp}) "+
				"endif "+
				")"; 
		return myQuery;
	}
}
