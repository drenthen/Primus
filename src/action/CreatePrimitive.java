package action;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Connector;
import org.eclipse.uml2.uml.ConnectorEnd;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Usage;


public class CreatePrimitive {
	org.eclipse.uml2.uml.Package myPackage;
	public CreatePrimitive(org.eclipse.uml2.uml.Package myPackage) {this.myPackage=myPackage;}
	
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
	
	public void createCallback(boolean newCaller, boolean newCallback, Component caller, Component callback) {
		Component comp1,comp2;
		if(newCaller==true) {
			comp1 = (Component) myPackage.createOwnedType(getUniqueName("Caller",1), UMLPackage.eINSTANCE.getComponent());
		} else {
			comp1 = (Component) myPackage.getOwnedType(caller.getName());
		}
		if(newCallback==true) {
			comp2 = (Component) myPackage.createOwnedType(getUniqueName("Callback",1), UMLPackage.eINSTANCE.getComponent());
		} else {
			comp2 = (Component) myPackage.getOwnedType(callback.getName());
		}
		//create ports
		Port p1 = (Port) comp1.createOwnedPort(getUniqueNameinComponent(comp1,"e",2), null);
		Port p2 = (Port) comp2.createOwnedPort(getUniqueNameinComponent(comp2,"c",2), null);
		Class class1 = myPackage.createOwnedClass(getUniqueName("Caller_e",2), true); 
	
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

		org.eclipse.uml2.uml.Property a1 = comp1.createOwnedAttribute(getUniqueNameinComponent(comp1,"foo",1), comp1);
		org.eclipse.uml2.uml.Property a2 = comp1.createOwnedAttribute(getUniqueNameinComponent(comp1,"bar",1), comp2);
		sourceEnd.setRole(a1);
		targetEnd.setRole(a2);
		
		EList<Stereotype> stereotypesConn = connector.getApplicableStereotypes();
		for (Stereotype stereotype: stereotypesConn) {
			if(stereotype.getName().equals("Callback")) {
				connector.applyStereotype(stereotype);
			}
		}
	}
	//public void createIndirection(boolean clientEnabled, boolean newClient, boolean newIndirector,boolean newIndirectorPort, boolean newTarget, boolean newTargetPort, Component client,Component indirector,Port indirectorPort, Component target, Port targetPort) {
	public void createIndirection(boolean clientEnabled,boolean newClient,Component client,boolean newIndirector,Component indirector,Port indirectorPort, boolean newTarget,Component target, Port targetPort,Interface op, Interface tar,Connector conn,Class class_ip,Class class_itp) {
		Component comp1,comp2,comp3;

		if(indirector==null) {
			comp1 = (Component) myPackage.createOwnedType(getUniqueName("Indirector",1), UMLPackage.eINSTANCE.getComponent());
		} else {
			comp1 = (Component) myPackage.getOwnedType(indirector.getName());
		}

		if(target==null) {
			comp2 = (Component) myPackage.createOwnedType(getUniqueName("Target",1), UMLPackage.eINSTANCE.getComponent());
		} else {
			comp2 = (Component) myPackage.getOwnedType(target.getName());
		}
		Port p1,p2;
		//create ports
		if(indirectorPort==null) {
			p1 = (Port) comp1.createOwnedPort(getUniqueNameinComponent(comp1,"Indirector_ip",2), null);
		} else {
			p1 = indirectorPort;
		}
		if(targetPort==null) {
			p2 = (Port) comp2.createOwnedPort(getUniqueNameinComponent(comp1,"IndirectionTarget_itp",2), null);
		} else {
			p2 = targetPort;
		}
		Class class1;
		//Create provided/required on component(A)
		if(class_ip==null) {
			class1 = myPackage.createOwnedClass(getUniqueName("Indirector_ip",2), true);
		} else {
			class1 = class_ip;
		}
			
		p1.setType(class1);
		p1.setIsComposite(true);

		EList<Stereotype> stereotypesPort1 = p1.getApplicableStereotypes();
		for (Stereotype stereotype: stereotypesPort1) {
			if(stereotype.getName().equals("IndirectionPort")) {
				p1.applyStereotype(stereotype);
			}
		}
		Interface myInterface1; 
		if(op==null) {
			myInterface1 = (Interface) myPackage.createOwnedType(getUniqueName("IndirectedOp",3), UMLPackage.eINSTANCE.getInterface()); //create Provided Interface, //code taken from org.eclipse.uml2.diagram.clazz.edit.commands.InterfaceRealizationCreateCommand#doDefaultElementCreation
		} else {
			myInterface1 = op;
		}
		InterfaceRealization newElement = UMLFactory.eINSTANCE.createInterfaceRealization();
		newElement.setImplementingClassifier(class1);
		newElement.setContract(myInterface1);
		class1.getInterfaceRealizations().add(newElement);
		
		Interface myInterface2;
		if(tar==null) {
			myInterface2 = (Interface) myPackage.createOwnedType(getUniqueName("TargetOp",3), UMLPackage.eINSTANCE.getInterface());
		} else {
			myInterface2=tar;
		}
		Usage newUsage = UMLFactory.eINSTANCE.createUsage();
		myPackage.getPackagedElements().add(newUsage);
		newUsage.getClients().add(class1);
		newUsage.getSuppliers().add(myInterface2);

		//Create provided/required on component(B)
		Class class2;
		if(class_itp==null) {
			class2 = myPackage.createOwnedClass(getUniqueName("Target_targetport",2), true); 
		} else {
			class2 = class_itp;
		}
		//Port p2 = (Port) comp2.getOwnedPort("IndirectionTarget_itp", null);			
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
		if(conn==null) {
			connector = comp1.createOwnedConnector(null);
			ConnectorEnd sourceEnd = UMLFactory.eINSTANCE.createConnectorEnd();
			ConnectorEnd targetEnd = UMLFactory.eINSTANCE.createConnectorEnd();

			//see ConnectorEndsConvention -- source first, than target
			connector.getEnds().add(sourceEnd);
			connector.getEnds().add(targetEnd);

			sourceEnd.setPartWithPort(p1);
			targetEnd.setPartWithPort(p2);
			
			org.eclipse.uml2.uml.Property a1 = comp1.createOwnedAttribute(getUniqueNameinComponent(comp1,"foo",1), comp1);
			org.eclipse.uml2.uml.Property a2 = comp1.createOwnedAttribute(getUniqueNameinComponent(comp1,"bar",1), comp2);
			sourceEnd.setRole(a1);
			targetEnd.setRole(a2);
		} else {
			connector = conn;
		}
		
		EList<Stereotype> stereotypesConn = connector.getApplicableStereotypes();
		for (Stereotype stereotype: stereotypesConn) {
			if(stereotype.getName().equals("Indirection")) {
				connector.applyStereotype(stereotype);
			}
		}
		
		if(clientEnabled) {
			if(client==null) {
				comp3 = (Component) myPackage.createOwnedType(getUniqueName("Client",1), UMLPackage.eINSTANCE.getComponent());
			} else {
				comp3 = (Component) myPackage.getOwnedType(client.getName());
			}
			
			//Create provided/required on component(Client)
			Class class3 = myPackage.createOwnedClass(getUniqueName("Client_clientport",2), true); 
			//Port p3 = (Port) comp3.getOwnedPort("clientport", null);	
			Port p3 = (Port) comp3.createOwnedPort(getUniqueNameinComponent(comp1,"Client_clientport",2), null);
			p3.setType(class3);
			p3.setIsComposite(true);
			Usage newUsage1 = UMLFactory.eINSTANCE.createUsage();
			myPackage.getPackagedElements().add(newUsage1);
			newUsage1.getClients().add(class3);
			newUsage1.getSuppliers().add(myInterface1);
			
			Connector connector1;
			connector1 = comp3.createOwnedConnector(null);
			ConnectorEnd sourceEnd1 = UMLFactory.eINSTANCE.createConnectorEnd();
			ConnectorEnd targetEnd1 = UMLFactory.eINSTANCE.createConnectorEnd();

			//see ConnectorEndsConvention -- source first, than target
			connector1.getEnds().add(sourceEnd1);
			connector1.getEnds().add(targetEnd1);

			sourceEnd1.setPartWithPort(p3);
			targetEnd1.setPartWithPort(p1);
			
			org.eclipse.uml2.uml.Property a3 = comp3.createOwnedAttribute(getUniqueNameinComponent(comp3,"foo",1), comp3);
			org.eclipse.uml2.uml.Property a4 = comp3.createOwnedAttribute(getUniqueNameinComponent(comp3,"bar",1), comp1);
			sourceEnd1.setRole(a3);
			targetEnd1.setRole(a4);
		}
		
	}
	public void appendIndirectionWithClient(boolean newClient, Component client,Component indirector) {
		Component comp1,comp3;
		
		if(newClient==true) {
			comp3 = (Component) myPackage.createOwnedType(getUniqueName("Client",1), UMLPackage.eINSTANCE.getComponent());
		} else {
			comp3 = (Component) myPackage.getOwnedType(client.getName());
		}
		comp1 = (Component) myPackage.getOwnedType(indirector.getName());
		
		Port p1;
		EList<Port> ports = comp1.getOwnedPorts();
		for (Port port: ports) {
			EList<Stereotype> stereotypesPort = port.getApplicableStereotypes();
			for (Stereotype stereotype: stereotypesPort) {
				if(stereotype.getName().equals("IndirectionPort")) {
					p1=port;
					
					Class class1 = (Class) p1.getType();
					//Create provided/required on component(Client)
					Class class3 = myPackage.createOwnedClass(getUniqueName("Client_clientport",2), true); 
					Port p3 = (Port) comp3.getOwnedPort("clientport", null);			
					p3.setType(class3);
					p3.setIsComposite(true);
					
					Interface myInterface1 = (Interface) myPackage.createOwnedType(getUniqueName("IndirectedOp",3), UMLPackage.eINSTANCE.getInterface());
					
					InterfaceRealization newElement = UMLFactory.eINSTANCE.createInterfaceRealization();
					newElement.setImplementingClassifier(class1);
					newElement.setContract(myInterface1);
					class1.getInterfaceRealizations().add(newElement);
					
					Usage newUsage1 = UMLFactory.eINSTANCE.createUsage();
					myPackage.getPackagedElements().add(newUsage1);
					newUsage1.getClients().add(class3);
					newUsage1.getSuppliers().add(myInterface1);
			
					//apply Stereotype interface IndirectedOp
					EList<Stereotype> stereotypesInterface1 = myInterface1.getApplicableStereotypes();
					for (Stereotype stereotype1: stereotypesInterface1) {
						if(stereotype1.getName().equals("Indirector")) {
							myInterface1.applyStereotype(stereotype1);
						}
					}
			
					Connector connector;
					connector = comp3.createOwnedConnector(null);
					ConnectorEnd sourceEnd = UMLFactory.eINSTANCE.createConnectorEnd();
					ConnectorEnd targetEnd = UMLFactory.eINSTANCE.createConnectorEnd();
			
					//see ConnectorEndsConvention -- source first, than target
					connector.getEnds().add(sourceEnd);
					connector.getEnds().add(targetEnd);
			
					sourceEnd.setPartWithPort(p3);
					targetEnd.setPartWithPort(p1);
					
					org.eclipse.uml2.uml.Property a1 = comp3.createOwnedAttribute(getUniqueNameinComponent(comp3,"foo",1), comp3);
					org.eclipse.uml2.uml.Property a2 = comp3.createOwnedAttribute(getUniqueNameinComponent(comp3,"bar",1), comp1);
					sourceEnd.setRole(a1);
					targetEnd.setRole(a2);
					
					EList<Stereotype> stereotypesConn = connector.getApplicableStereotypes();
					for (Stereotype stereotype1: stereotypesConn) {
						if(stereotype1.getName().equals("Indirection")) {
							connector.applyStereotype(stereotype1);
						}
					}
				}
			}
		}			
	}
	public void createGroup(boolean newPackage,org.eclipse.uml2.uml.Package pack) {
		Element element;
		if(newPackage==true) {
			element = (org.eclipse.uml2.uml.Package) myPackage.createNestedPackage(getUniqueName("Group",4));
		} else {
			element = (org.eclipse.uml2.uml.Package) pack;
		}
		EList<Stereotype> stereotypes = element.getApplicableStereotypes();
		for (Stereotype stereotype: stereotypes) {
			
			if(stereotype.getName().equals("Group")) {
				if(!element.getAppliedStereotypes().contains(stereotype)) {
					element.applyStereotype(stereotype);
				}
			}
		}
	}
	public void createLayer(boolean newPackage,org.eclipse.uml2.uml.Package pack,int layerNumber) {
		Element element;
		if(newPackage==true) {
			element = (org.eclipse.uml2.uml.Package) myPackage.createNestedPackage(getUniqueName("Layer",4));
		} else {
			element = (org.eclipse.uml2.uml.Package) pack;
		}
		EList<Stereotype> stereotypes = element.getApplicableStereotypes();
		for (Stereotype stereotype: stereotypes) {
			if(stereotype.getName().equals("Layer")) {
				if(!element.getAppliedStereotypes().contains(stereotype)) {
					element.applyStereotype(stereotype);
					
				}
				element.setValue(stereotype, "LayerNumber", layerNumber);
				if(stereotype.getName().equals("Group")) {
					if(!element.getAppliedStereotypes().contains(stereotype)) {
						element.applyStereotype(stereotype);
					}
				}
			}
		}
	}
	public void createAggregationCascade(boolean mainAggActive,boolean mainAggNew,boolean agg1New,boolean agg2New,Component mainAgg,Component agg1,Component agg2,String aggregationType) {
		Component comp0,comp1,comp2;
		
		if(agg1New==true) {
			comp1 = (Component) myPackage.createOwnedType(getUniqueName("Agg",1), UMLPackage.eINSTANCE.getComponent());
		} else {
			comp1 = (Component) myPackage.getOwnedType(agg1.getName());
		}
		if(agg2New==true) {
			comp2 = (Component) myPackage.createOwnedType(getUniqueName("Agg",1), UMLPackage.eINSTANCE.getComponent());
		} else {
			comp2 = (Component) myPackage.getOwnedType(agg2.getName());
		}
		//create ports
		Port p1 = (Port) comp1.createOwnedPort(getUniqueNameinComponent(comp1,"PortAgg",2), null); 
		Port p2 = (Port) comp2.createOwnedPort(getUniqueNameinComponent(comp2,"PortAgg",2), null); 
		
		Class class1 = myPackage.createOwnedClass(getUniqueName("Agg_PortAgg",2), true);	
		p1.setType(class1);
		p1.setIsComposite(true);
		
		Class class2 = myPackage.createOwnedClass(getUniqueName("Agg_PortAgg",2), true); 		
		p2.setType(class2);
		p2.setIsComposite(true);
		
		Interface myInterface1 = (Interface) myPackage.createOwnedType(getUniqueName("TargetOp",3), UMLPackage.eINSTANCE.getInterface());
		Usage newUsage = UMLFactory.eINSTANCE.createUsage();
		myPackage.getPackagedElements().add(newUsage);
		newUsage.getClients().add(class1);
		newUsage.getSuppliers().add(myInterface1);
		
		InterfaceRealization newElement = UMLFactory.eINSTANCE.createInterfaceRealization();
		newElement.setImplementingClassifier(class2);
		newElement.setContract(myInterface1);
		class2.getInterfaceRealizations().add(newElement);

		Interface myInterface2 = (Interface) myPackage.createOwnedType(getUniqueName("IndirectedOp",3), UMLPackage.eINSTANCE.getInterface());
		InterfaceRealization newElement1 = UMLFactory.eINSTANCE.createInterfaceRealization();
		newElement1.setImplementingClassifier(class1);
		newElement1.setContract(myInterface2);
		class1.getInterfaceRealizations().add(newElement1);
		
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
		
		EList<Stereotype> stereotypes = connector.getApplicableStereotypes();
		for (Stereotype stereotype: stereotypes) {
			if(stereotype.getName().equals("AggregationCascade")) {
				connector.applyStereotype(stereotype);
			}
		}
		if(mainAggActive) {
			if(mainAggNew==true) {
				comp0 = (Component) myPackage.createOwnedType(getUniqueName("MainAgg",1), UMLPackage.eINSTANCE.getComponent());
			} else {
				comp0 = (Component) myPackage.getOwnedType(mainAgg.getName());
			}
			comp1.createGeneralization(comp0);
			comp2.createGeneralization(comp0);
		}
		
		org.eclipse.uml2.uml.Association association = (org.eclipse.uml2.uml.Association) myPackage.createOwnedType(getUniqueName("aggbetweencomp",6),UMLPackage.eINSTANCE.getAssociation());
		org.eclipse.uml2.uml.Property prop1 = comp1.createOwnedAttribute(getUniqueNameinComponent(comp1,"prop",1), comp1);
		org.eclipse.uml2.uml.Property prop2 = comp2.createOwnedAttribute(aggregationType, comp2);
		prop1.setAssociation(association);
		prop1.setAggregation(AggregationKind.SHARED_LITERAL);
		
		prop2.setAssociation(association);
		
		connector.setType(association);
		//public void createIndirection(boolean clientEnabled,Component client,Component indirector,Port indirectorPort, Component target, Port targetPort,Interface op, Interface tar,Connector conn,Class class_ip,Class class_itp) {
		createIndirection(false,false,null,false,comp1,p1,false,comp2,p2,myInterface2, myInterface1,connector,class1,class2);
	}
	public void createCompositionCascade(boolean mainCompNew,boolean leafCompNew,boolean compositeCompNew,Component main,Component leaf,Component composite) {
		Component comp1,comp2,comp3;
		if(mainCompNew==true) {
			comp1 = (Component) myPackage.createOwnedType(getUniqueName("Main",1), UMLPackage.eINSTANCE.getComponent());
		} else {
			comp1 = (Component) myPackage.getOwnedType(main.getName());
		}
		if(leafCompNew==true) {
			comp2 = (Component) myPackage.createOwnedType(getUniqueName("Leaf",1), UMLPackage.eINSTANCE.getComponent());
		} else {
			comp2 = (Component) myPackage.getOwnedType(leaf.getName());
		}
		if(compositeCompNew==true) {
			comp3 = (Component) myPackage.createOwnedType(getUniqueName("Composite",1), UMLPackage.eINSTANCE.getComponent());
		} else {
			comp3 = (Component) myPackage.getOwnedType(composite.getName());
		}
		//create ports
		
		Port p1 = (Port) comp1.createOwnedPort(getUniqueNameinComponent(comp1,"PortComp",2), null); 
		Port p2 = (Port) comp3.createOwnedPort(getUniqueNameinComponent(comp3,"PortComp",2), null);

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

		org.eclipse.uml2.uml.Property a1 = comp3.createOwnedAttribute(getUniqueNameinComponent(comp3,"foo",1), comp3);
		org.eclipse.uml2.uml.Property a2 = comp3.createOwnedAttribute(getUniqueNameinComponent(comp3,"bar",1), comp1);
		sourceEnd.setRole(a1);
		targetEnd.setRole(a2);

		sourceEnd.setPartWithPort(p1);
		targetEnd.setPartWithPort(p2);
		
		EList<Stereotype> stereotypes = connector.getApplicableStereotypes();
		for (Stereotype stereotype: stereotypes) {
			if(stereotype.getName().equals("CompositionCascade")) {
				connector.applyStereotype(stereotype);
			}
			if(stereotype.getName().equals("AggregationCascade")) {
				connector.applyStereotype(stereotype);
			}
		}

		org.eclipse.uml2.uml.Association association = (org.eclipse.uml2.uml.Association)myPackage.createOwnedType(getUniqueName("compositebetweencomp",6),UMLPackage.eINSTANCE.getAssociation());
		org.eclipse.uml2.uml.Property prop3 = comp3.createOwnedAttribute(getUniqueNameinComponent(comp3,"prop",1), comp3);
		org.eclipse.uml2.uml.Property prop1 = comp1.createOwnedAttribute(getUniqueNameinComponent(comp1,"prop",1), comp1);
		prop3.setAssociation(association);
		prop3.setIsComposite(true);

		prop1.setAssociation(association);
		
		connector.setType(association);
		
		createIndirection(false,false,null,false,comp3,p2,false,comp1,p1,null, myInterface1,connector,class2,class1);
	}
	public void createShield(boolean clientShieldNew,boolean accesspointShieldNew,boolean protectedActive,boolean protectedShieldNew,boolean groupShieldNew,Component clientShield,Component accesspointShield,Component protectedShield,org.eclipse.uml2.uml.Package groupShield) {
		Component comp1,comp2,comp3;
		org.eclipse.uml2.uml.Package pack1;
		
		if(clientShieldNew==true) {
			comp1 = (Component) myPackage.createOwnedType(getUniqueName("ClientShield",1), UMLPackage.eINSTANCE.getComponent());
		} else {
			comp1 = (Component) myPackage.getOwnedType(clientShield.getName());
		}
		if(accesspointShieldNew==true) {
			comp2 = (Component) myPackage.createOwnedType(getUniqueName("AccessPointShield",1), UMLPackage.eINSTANCE.getComponent());
		} else {
			comp2 = (Component) myPackage.getOwnedType(accesspointShield.getName());
		}
		
		if(groupShieldNew==true) {
			pack1 = (org.eclipse.uml2.uml.Package) myPackage.createNestedPackage(getUniqueName("ShieldGroup",4));
		} else {
			pack1 = (org.eclipse.uml2.uml.Package) groupShield;
		}	
		Port p1 = (Port) comp1.createOwnedPort(getUniqueNameinComponent(comp1,"PortClient",2), null); 
		Class class1 = myPackage.createOwnedClass(getUniqueName("Client_PortClient",2), true);	
		p1.setType(class1);
		p1.setIsComposite(true);
		
		Interface myInterface1 = (Interface) myPackage.createOwnedType(getUniqueName("ShieldInt",3), UMLPackage.eINSTANCE.getInterface());			
		Usage newUsage = UMLFactory.eINSTANCE.createUsage();
		myPackage.getPackagedElements().add(newUsage);
		newUsage.getClients().add(class1);
		newUsage.getSuppliers().add(myInterface1);
		
		Port p2 = (Port) comp2.createOwnedPort(getUniqueNameinComponent(comp2,"PortAccessPoint",2), null); 
		
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
		
		pack1.createElementImport(comp2);
		
		EList<Stereotype> stereotypesPackage = pack1.getApplicableStereotypes();
		
		for (Stereotype stereotype: stereotypesPackage) {
			if(stereotype.getName().equals("Group")) {
				if(!pack1.getAppliedStereotypes().contains(stereotype)) {
					pack1.applyStereotype(stereotype);
				}
			}
		}
		
		EList<Stereotype> stereotypesPort = p2.getApplicableStereotypes();
	
		for (Stereotype stereotype: stereotypesPort) {
			if(stereotype.getName().equals("ShieldPort")) {
				p2.applyStereotype(stereotype);
				p2.setValue(stereotype, "shieldGroup", pack1.getName());
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
		org.eclipse.uml2.uml.Property a2 = comp1.createOwnedAttribute(getUniqueNameinComponent(comp1,"bar",1), comp2);

		sourceEnd.setRole(a1);
		targetEnd.setRole(a2);
		EList<Stereotype> stereotypesConn = connector.getApplicableStereotypes();
		for (Stereotype stereotype: stereotypesConn) {
			if(stereotype.getName().equals("Shield")) {
				connector.applyStereotype(stereotype);
			}
		}
		org.eclipse.uml2.uml.Association association = (org.eclipse.uml2.uml.Association)myPackage.createOwnedType(getUniqueName("shieldassoc",6),UMLPackage.eINSTANCE.getAssociation());
		org.eclipse.uml2.uml.Property prop2 = comp2.createOwnedAttribute(getUniqueNameinComponent(comp2,"prop",1), comp2);
		org.eclipse.uml2.uml.Property prop1 = comp1.createOwnedAttribute(getUniqueNameinComponent(comp1,"prop",1), comp1);
		prop1.setAssociation(association);
		prop2.setAssociation(association);
		connector.setType(association);
		if(protectedActive==true) {
			if(protectedShieldNew==true) {
				comp3 = (Component) myPackage.createOwnedType(getUniqueName("ProtecedShield",1), UMLPackage.eINSTANCE.getComponent());
			} else {
				comp3 = (Component) myPackage.getOwnedType(protectedShield.getName());
			}
			pack1.createElementImport(comp3);
			//CREATE Connector between accesspoint and protected component
			Interface myInterface2 = (Interface) myPackage.createOwnedType(getUniqueName("InternalShieldComm",3), UMLPackage.eINSTANCE.getInterface());			
			Usage newUsage2 = UMLFactory.eINSTANCE.createUsage();
			
			myPackage.getPackagedElements().add(newUsage2);
			newUsage2.getClients().add(class2);
			newUsage2.getSuppliers().add(myInterface2);
			
			Port p3 = (Port) comp3.createOwnedPort(getUniqueNameinComponent(comp3,"PortProtected",2), null); 
			
			Class class3 = myPackage.createOwnedClass(getUniqueName("Protected_PortProtected",2), true);	
			p3.setType(class3);
			p3.setIsComposite(true);
			
			InterfaceRealization newElement2 = UMLFactory.eINSTANCE.createInterfaceRealization();
			newElement2.setImplementingClassifier(class3);
			newElement2.setContract(myInterface2);
			class3.getInterfaceRealizations().add(newElement2);
			
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
			org.eclipse.uml2.uml.Property a22 = comp2.createOwnedAttribute(getUniqueNameinComponent(comp2,"bar",1), comp3);
			
			sourceEnd1.setRole(a11);
			targetEnd1.setRole(a22);
		}
		System.out.println("7");
	}
	public void createTyping(boolean baseNew,boolean typeNew,boolean supertypeNew,boolean firstActive,boolean thirdActive,Component base,Component type,Component supertype) {
		Component comp1,comp2,comp3;

		if(typeNew==true) {
			comp2 = (Component) myPackage.createOwnedType(getUniqueName("Type",1), UMLPackage.eINSTANCE.getComponent());
		} else {
			comp2 = (Component) myPackage.getOwnedType(type.getName());
		}

		if(firstActive==true) {
			if(baseNew==true) {
				comp1 = (Component) myPackage.createOwnedType(getUniqueName("Base",1), UMLPackage.eINSTANCE.getComponent());
			} else {
				comp1 = (Component) myPackage.getOwnedType(base.getName());
			}
			
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
			
			org.eclipse.uml2.uml.Property a1 = comp1.createOwnedAttribute(getUniqueNameinComponent(comp1,"foo",1), comp1);
			org.eclipse.uml2.uml.Property a2 = comp1.createOwnedAttribute(getUniqueNameinComponent(comp1,"bar",1), comp2);
			sourceEnd.setRole(a1);
			targetEnd.setRole(a2);
	
			EList<Stereotype> stereotypesConn = connector.getApplicableStereotypes();
			for (Stereotype stereotype: stereotypesConn) {
				if(stereotype.getName().equals("TypeConnector")) {
					connector.applyStereotype(stereotype);
				}
			}
		}
		if(thirdActive==true) {

			if(supertypeNew==true) {
				comp3 = (Component) myPackage.createOwnedType(getUniqueName("Supertype",1), UMLPackage.eINSTANCE.getComponent());
			} else {
				comp3 = (Component) myPackage.getOwnedType(supertype.getName());
			}
			
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
		}
	}
	public void createVirtualConnector(boolean callerVCNew,boolean receiverVCNew,Component callerVC,Component receiverVC) {
		Component comp1,comp2;
		
		if(callerVCNew==true) {
			comp1 = (Component) myPackage.createOwnedType(getUniqueName("A",1), UMLPackage.eINSTANCE.getComponent());
		} else {
			comp1 = (Component) myPackage.getOwnedType(callerVC.getName());
		}
		if(receiverVCNew==true) {
			comp2 = (Component) myPackage.createOwnedType(getUniqueName("B",1), UMLPackage.eINSTANCE.getComponent());
		} else {
			comp2 = (Component) myPackage.getOwnedType(receiverVC.getName());
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

		org.eclipse.uml2.uml.Property a1 = comp1.createOwnedAttribute(getUniqueNameinComponent(comp1,"foo",1), comp1);
		org.eclipse.uml2.uml.Property a2 = comp1.createOwnedAttribute(getUniqueNameinComponent(comp1,"bar",1), comp2);
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
	}
	public void createConnector(boolean callerConnectorNew,boolean receiverConnectorNew,Component callerConnector,Component receiverConnector) {
Component comp1,comp2;
		
		if(callerConnectorNew==true) {
			comp1 = (Component) myPackage.createOwnedType(getUniqueName("A",1), UMLPackage.eINSTANCE.getComponent());
		} else {
			comp1 = (Component) myPackage.getOwnedType(callerConnector.getName());
		}
		if(receiverConnectorNew==true) {
			comp2 = (Component) myPackage.createOwnedType(getUniqueName("B",1), UMLPackage.eINSTANCE.getComponent());
		} else {
			comp2 = (Component) myPackage.getOwnedType(receiverConnector.getName());
		}
		Port p1 = (Port) comp1.createOwnedPort("PortA", null); 
		
		Class class1 = myPackage.createOwnedClass(getUniqueName("A_PortA",2), true);	
		p1.setType(class1);
		p1.setIsComposite(true);
		
		Port p2 = (Port) comp2.createOwnedPort("PortB", null); 

		Class class2 = myPackage.createOwnedClass(getUniqueName("B_PortB",2), true);	
		p2.setType(class2);
		p2.setIsComposite(true);
		
		Interface myInterface1 = (Interface) myPackage.createOwnedType(getUniqueName("ConnectorInt",3), UMLPackage.eINSTANCE.getInterface());			
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
	}
}
