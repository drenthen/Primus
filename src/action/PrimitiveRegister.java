package action;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ocl.utilities.impl.Bag;
import org.eclipse.ocl.util.Tuple;
import action.ValidatePrimitiveAction.ValidatePrimitiveRequest;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Connector;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Port;

public class PrimitiveRegister {
	public String resultString="";
	public String resultStringNear="";
	public PrimitiveRegister() {
	
	}
	
	public void registerCallback(Set set,Set setNear) {
		//Bag myBag;
		Object obj;
		org.eclipse.ocl.util.Tuple myTuple;
		Component myComp;
		//use vectors to simplify looking for components already found for the 'nearly' search
		Vector<Component> callerSide = new Vector();
		Vector<Component> callbackSide = new Vector();
		
		//this is to register the query to find primitive in it's exact form
		for (Iterator it = set.iterator(); it.hasNext(); ) {
			myTuple=(Tuple)it.next();
			 
			myComp = (Component) myTuple.getValue("c1");
			obj = (Object) myTuple.getValue("s");
			Collection coll = (Collection) obj;
			//resultString=resultString+"\nCaller = "+myComp.getName();
			callerSide.add(myComp);

			for (Iterator it2 = coll.iterator(); it2.hasNext(); ) {
				Component myComp2 = (Component) it2.next();
				System.out.println("\nCaller = "+myComp.getName());
				resultString=resultString+"\nCaller = "+myComp.getName();
				System.out.println("\nCallback = "+myComp2.getName());
				resultString=resultString+"   Callback = "+myComp2.getName();
				callbackSide.add(myComp2);
			}
			resultString=resultString+"\n";
		}
		
		//this is for a nearly primitive
		resultString=resultString+"\nNear Callback List:";
		for (Iterator it = setNear.iterator(); it.hasNext(); ) {
			myTuple=(Tuple)it.next();
			 
			myComp = (Component) myTuple.getValue("c1");
			obj = (Object) myTuple.getValue("s");
			Collection coll = (Collection) obj;
			for (Iterator it2 = coll.iterator(); it2.hasNext(); ) {
				Component myComp2 = (Component) it2.next();
				if(!callbackSide.contains(myComp2)) {
					System.out.println("\nCaller = "+myComp.getName());
					resultStringNear=resultStringNear+"\nNear Caller = "+myComp.getName();
					System.out.println("\nCallback = "+myComp2.getName());
					resultStringNear=resultStringNear+"   Near Callback = "+myComp2.getName();
				}
			}
			resultStringNear=resultStringNear+"\n";
		}
	}
	public void registerIndirection(Set set, Set setNear) {
		//Bag myBag;
		Object obj;
		org.eclipse.ocl.util.Tuple myTuple;
		Component myComp1,myComp2;
		
		//register for exact primitive fit
		for (Iterator it = set.iterator(); it.hasNext(); ) {
			myTuple=(Tuple)it.next();
			 
			myComp1 = (Component) myTuple.getValue("c1");	
			System.out.println("indirector (proxy) = "+myComp1.getName());
			resultString=resultString+"\nIndirector (proxy) = "+myComp1.getName();
			
			obj = (Object) myTuple.getValue("client");
			Collection coll = (Collection) obj;
			for (Iterator it2 = coll.iterator(); it2.hasNext(); ) {
				Component client = (Component) it2.next();
				System.out.println("\nIndirection Client = "+client.getName());
				resultString=resultString+"\nIndirection Client = "+client.getName();
			}
			obj = (Object) myTuple.getValue("target");
			Collection coll2 = (Collection) obj;
			for (Iterator it2 = coll2.iterator(); it2.hasNext(); ) {
				Component target = (Component) it2.next();
				System.out.println("\nIndirection Target = "+target.getName());
				resultString=resultString+"\nIndirection Target = "+target.getName();
			}
			resultString=resultString+"\n";
		}
		resultString=resultString+"\nNear Indirection List: \n";
		//register near primitive fit
		//cannot use vectors to look for nearly primitives
		for (Iterator it = setNear.iterator(); it.hasNext(); ) {
			myTuple=(Tuple)it.next();
			 
			myComp1 = (Component) myTuple.getValue("c1");	
			
			obj = (Object) myTuple.getValue("client");
			Collection coll = (Collection) obj;
			boolean indirectorShown=false;
			for (Iterator it2 = coll.iterator(); it2.hasNext(); ) {
				Component client = (Component) it2.next();
				
				//look for current client/indirector combination in set
				//if that combination is not found then add to 'near' list
				//if found then set boolean to true
				boolean found=false;
				for (Iterator it3 = set.iterator(); it3.hasNext(); ) {
					Tuple myTupleProper=(Tuple)it3.next();
					Component myCompProper = (Component) myTupleProper.getValue("c1");
					
					if(myComp1.equals(myCompProper)) {
						Object objProper = (Object) myTuple.getValue("client");
						Collection collProper = (Collection) objProper;
						for (Iterator it4 = collProper.iterator(); it4.hasNext(); ) {
							Component clientProper= (Component) it4.next();
							if(clientProper.equals(client)) {
								found=true;
							}
						}	
					}
				}
				if(found==false) {
					System.out.println("\nindirector (proxy) = "+myComp1.getName());
					resultString=resultString+"\nIndirector (proxy) = "+myComp1.getName();
					
					System.out.println("\nIndirection Client = "+client.getName());
					resultString=resultString+"\nIndirection Client = "+client.getName();
					indirectorShown=true;
				}
			}
			
			obj = (Object) myTuple.getValue("target");
			Collection coll2 = (Collection) obj;
			for (Iterator it2 = coll2.iterator(); it2.hasNext(); ) {
				Component target = (Component) it2.next();
				//look for current target/indirector combination in set
				//if that combination is not found then add to 'near' list
				//if found then set boolean to true
				boolean found=false;
				for (Iterator it3 = set.iterator(); it3.hasNext(); ) {
					Tuple myTupleProper=(Tuple)it3.next();
					Component myCompProper = (Component) myTupleProper.getValue("c1");
					
					if(myComp1.equals(myCompProper)) {
						Object objProper = (Object) myTuple.getValue("target");
						Collection collProper = (Collection) objProper;
						for (Iterator it4 = collProper.iterator(); it4.hasNext(); ) {
							Component targetProper= (Component) it4.next();
							if(targetProper.equals(target)) {
								found=true;
							}
						}
					}
				}
				if(found==false) {
					if(!indirectorShown){
						System.out.println("\nindirector (proxy) = "+myComp1.getName());
						resultString=resultString+"\nIndirector (proxy) = "+myComp1.getName();
					}
					System.out.println("Indirection Target = "+target.getName());
					resultString=resultString+"\nIndirection Target = "+target.getName();
					resultString=resultString+"\n";
				}
			}
		}
	}
	public void registerGrouping(Set set, Set setNear) {
		//Bag myBag;
		Object obj;
		Component myComp1,myComp2;
		Vector<org.eclipse.uml2.uml.Package> groupvect= new Vector();
		//check for proper primitive
		for (Iterator it = set.iterator(); it.hasNext(); ) {
			org.eclipse.uml2.uml.Package myPackage = (org.eclipse.uml2.uml.Package) it.next();
		
			System.out.println("group="+myPackage.getName());
			resultString=resultString+"\nGroup = "+myPackage.getName();
			groupvect.add(myPackage);
		}
		resultString=resultString+"\nNear Grouping List:\n";
		//check for near primitive
		for (Iterator it2 = setNear.iterator(); it2.hasNext(); ) {
			org.eclipse.uml2.uml.Package myPackage = (org.eclipse.uml2.uml.Package) it2.next();
		
			if(!groupvect.contains(myPackage)) {
				System.out.println("\ngroup="+myPackage.getName());
				resultString=resultString+"\nGroup = "+myPackage.getName();
			}
		}
	}
	public void registerLayers(Set set,Set setNear) {
		//Bag myBag;
		Object obj;
		Component myComp1,myComp2;
		Vector<org.eclipse.uml2.uml.Package> layervect= new Vector();
		//register proper primitives
		for (Iterator it = set.iterator(); it.hasNext(); ) {
			org.eclipse.uml2.uml.Package myPackage = (org.eclipse.uml2.uml.Package) it.next();
		
			System.out.println("layer="+myPackage.getName());
			resultString=resultString+"\nLayer = "+myPackage.getName();
			layervect.add(myPackage);
		}
		
		//register near primitives
		resultString=resultString+"\n\nNear Layers List:\n";
		for (Iterator it2 = setNear.iterator(); it2.hasNext(); ) {
			org.eclipse.uml2.uml.Package myPackage = (org.eclipse.uml2.uml.Package) it2.next();
		
			if(!layervect.contains(myPackage)) {
				System.out.println("\nlayer="+myPackage.getName());
				resultString=resultString+"\nLayer = "+myPackage.getName();
			}
		}
	}
	public void registerAggregationCascade(Set set, Set setNear) {
		//Bag myBag;
		Object obj;
		org.eclipse.ocl.util.Tuple myTuple;
		Component myComp1,myComp2;
		Vector<Component> aggvect= new Vector();
		
		//look for proper primitives
		for (Iterator it = set.iterator(); it.hasNext(); ) {
			myTuple=(Tuple)it.next();
			 
			myComp1 = (Component) myTuple.getValue("c1");
			myComp2 = (Component) myTuple.getValue("c2");
			
			System.out.println("MainAggregation Component="+myComp1.getName());
			resultString=resultString+"\nMainAggregation Component = "+myComp1.getName();
			System.out.println("AggregationTop Component="+myComp2.getName());
			resultString=resultString+"\nAggregationTop Component = "+myComp2.getName();			
			
			obj = (Object) myTuple.getValue("s");
			Collection coll = (Collection) obj;
			for (Iterator it2 = coll.iterator(); it2.hasNext(); ) {
				Component myComp3 = (Component) it2.next();
				System.out.println("Aggregation Component="+myComp3.getName());
				resultString=resultString+"\nAggregation Component = "+myComp3.getName();
				aggvect.add(myComp3);
			}
		}
		resultString=resultString+"\n\nNear Aggregation Cascade:\n";

		System.out.println("checking for near");
		//look for near primitives
		for (Iterator it = setNear.iterator(); it.hasNext(); ) {
			myTuple=(Tuple)it.next();
			 
			//myComp1 = (Component) myTuple.getValue("c1");
			myComp2 = (Component) myTuple.getValue("c2");			
			
			obj = (Object) myTuple.getValue("s");
			Collection coll = (Collection) obj;
			for (Iterator it2 = coll.iterator(); it2.hasNext(); ) {
				Component myComp3 = (Component) it2.next();
				
				if(!aggvect.contains(myComp3)) {
					//System.out.println("MainAggregation Component="+myComp1.getName());
					//resultString=resultString+"\nMainAggregation Component = "+myComp1.getName();
					System.out.println("AggregationTop Component="+myComp2.getName());
					resultString=resultString+"\nAggregationTop Component = "+myComp2.getName();
					
					System.out.println("Aggregation Component="+myComp3.getName());
					resultString=resultString+"\nAggregation Component = "+myComp3.getName();
				}
			}
		}
		
	}
	public void registerCompositionCascade(Set set,Set setNear) {
		//Bag myBag;
		Object obj;
		org.eclipse.ocl.util.Tuple myTuple;
		Component myComp1,myComp2;
		Vector<Component> compvect= new Vector();
		//look for primitives
		for (Iterator it = set.iterator(); it.hasNext(); ) {
			myTuple=(Tuple)it.next();
			 
			
			
			myComp1 = (Component) myTuple.getValue("c2");
			obj = (Object) myTuple.getValue("s");
			Collection coll = (Collection) obj;
			
			System.out.println("Main Component="+myComp1.getName());
			resultString=resultString+"\nMain Component = "+myComp1.getName();
			
			for (Iterator it2 = coll.iterator(); it2.hasNext(); ) {
				Component myComp3 = (Component) it2.next();
				System.out.println("Composition Component="+myComp3.getName());
				resultString=resultString+"\nComposition Component = "+myComp3.getName();
				compvect.add(myComp3);
			}
		}
		
		//look for near primitives
		resultString=resultString+"\n\nNear Composition Cascade:\n";
		for (Iterator it2 = setNear.iterator(); it2.hasNext(); ) {
			myTuple=(Tuple)it2.next();
			 
			myComp1 = (Component) myTuple.getValue("c2");
			obj = (Object) myTuple.getValue("s");
			Collection coll = (Collection) obj;
			boolean show=false;
			for (Iterator it3 = coll.iterator(); it3.hasNext(); ) {
				Component myComp3 = (Component) it3.next();
				
				if(!compvect.contains(myComp3)) {
					show=true;
					System.out.println("Composition Component="+myComp3.getName());
					resultString=resultString+"\nComposition Component = "+myComp3.getName();
				}
			}
			if(show) {
				System.out.println("Main Component="+myComp1.getName());
				resultString=resultString+"\nMain Component = "+myComp1.getName();
				resultString=resultString+"\n";
				
			}
		}
		
	}
	public void registerShield(Set set,Set setNear) {
		//Bag myBag;
		Object obj;
		org.eclipse.ocl.util.Tuple myTuple;
		Component myComp1,myComp2;
		org.eclipse.uml2.uml.Package myPackage;
		Vector<Component> compvect= new Vector();
		
		for (Iterator it = set.iterator(); it.hasNext(); ) {
			myTuple=(Tuple)it.next();
			 
			myComp1 = (Component) myTuple.getValue("c");
			myPackage = (org.eclipse.uml2.uml.Package) myTuple.getValue("group");
			//myComp2 = (Component) myTuple.getValue("client");
			obj = (Object) myTuple.getValue("client");
			Collection coll = (Collection) obj;
			for (Iterator it2 = coll.iterator(); it2.hasNext(); ) {
				Component myComp3 = (Component) it2.next();
				System.out.println("Client="+myComp3.getName());
				resultString=resultString+"\nClient = "+myComp3.getName();
			}
			System.out.println("AccessPoint="+myComp1.getName());
			resultString=resultString+"\nAccessPoint = "+myComp1.getName();
			System.out.println("group="+myPackage.getName());
			resultString=resultString+"\nGroup = "+myPackage.getName();
			resultString=resultString+"\n";
			compvect.add(myComp1);
		}
		
		
		//look for near primitives
		resultString=resultString+"\n\nNear Shield:\n";
		for (Iterator it2 = setNear.iterator(); it2.hasNext(); ) {
			myTuple=(Tuple)it2.next();
			myComp1 = (Component) myTuple.getValue("c");
			myPackage = (org.eclipse.uml2.uml.Package) myTuple.getValue("group");
			obj = (Object) myTuple.getValue("client");
			Collection coll = (Collection) obj;
			boolean show=false;

			if(!compvect.contains(myComp1)) {
				
				for (Iterator it3 = coll.iterator(); it3.hasNext(); ) {
					Component myComp3 = (Component) it3.next();
					System.out.println("Client="+myComp3.getName());
					resultString=resultString+"\nClient = "+myComp3.getName();
				}
				System.out.println("AccessPoint="+myComp1.getName());
				resultString=resultString+"\nAccessPoint = "+myComp1.getName();
				System.out.println("group="+myPackage.getName());
				resultString=resultString+"\nGroup = "+myPackage.getName();
				resultString=resultString+"\n";
			}
		}
	}
	public void registerTyping(Set set, Set setNear) {
		//Bag myBag;
		Object obj;
		org.eclipse.ocl.util.Tuple myTuple;
		Component myComp1;
		//org.eclipse.uml2.uml.Package myPackage;
		Vector<Component> compvect= new Vector();
		for (Iterator it = set.iterator(); it.hasNext(); ) {
			myTuple=(Tuple)it.next();
			 
			myComp1 = (Component) myTuple.getValue("c1");
			compvect.add(myComp1);
			System.out.println("Base Component = " + myComp1.getName());
			resultString=resultString+"\nBase Component = " + myComp1.getName();
			obj = (Object) myTuple.getValue("s");
			Collection coll = (Collection) obj;
			for (Iterator it2 = coll.iterator(); it2.hasNext(); ) {
				Component myComp2= (Component) it2.next();
				System.out.println("Typed Component="+myComp2.getName());
				resultString=resultString+"\nTyped Component = "+myComp2.getName();
			}
			
			obj = (Object) myTuple.getValue("s1");
			coll = (Collection) obj;
			for (Iterator it2 = coll.iterator(); it2.hasNext(); ) {
				Component myComp2= (Component) it2.next();
				System.out.println("SuperTyped Component="+myComp2.getName());
				resultString=resultString+"\nSuperTyped Component = "+myComp2.getName();
			}
			resultString=resultString+"\n";
		}
		
		//register near
		resultString=resultString+"\n\nNear Typing:\n";
		for (Iterator it = setNear.iterator(); it.hasNext(); ) {
			myTuple=(Tuple)it.next();
			 
			myComp1 = (Component) myTuple.getValue("c1");
			if(!compvect.contains(myComp1)) {
				System.out.println("Base Component = " + myComp1.getName());
				resultString=resultString+"\nBase Component = " + myComp1.getName();
				obj = (Object) myTuple.getValue("s");
				Collection coll = (Collection) obj;
				for (Iterator it2 = coll.iterator(); it2.hasNext(); ) {
					Component myComp2= (Component) it2.next();
					System.out.println("Typed Component="+myComp2.getName());
					resultString=resultString+"\nTyped Component = "+myComp2.getName();
				}
				obj = (Object) myTuple.getValue("s1");
				coll = (Collection) obj;
				for (Iterator it2 = coll.iterator(); it2.hasNext(); ) {
					Component myComp2= (Component) it2.next();
					System.out.println("SuperTyped Component="+myComp2.getName());
					resultString=resultString+"\nSuperTyped Component = "+myComp2.getName();
				}
				resultString=resultString+"\n";
			}
		}
	}
	
	public void registerSuperTyping(Set set, Set setNear) {
		//Bag myBag;
		Object obj;
		org.eclipse.ocl.util.Tuple myTuple;
		Component myComp1;
		//org.eclipse.uml2.uml.Package myPackage;
		Vector<Component> compvect= new Vector();
		for (Iterator it = set.iterator(); it.hasNext(); ) {
			myTuple=(Tuple)it.next();
			 
			myComp1 = (Component) myTuple.getValue("c1");
			compvect.add(myComp1);
			System.out.println("Type Component = " + myComp1.getName());
			resultString=resultString+"\nType Component = " + myComp1.getName();
			obj = (Object) myTuple.getValue("s");
			Collection coll = (Collection) obj;
			for (Iterator it2 = coll.iterator(); it2.hasNext(); ) {
				Component myComp2= (Component) it2.next();
				System.out.println("Super Typed Component="+myComp2.getName());
				resultString=resultString+"\nSuper Typed Component = "+myComp2.getName();
			}
			resultString=resultString+"\n";
		}
		
		//register near
		resultString=resultString+"\n\nNear Super Typing:\n";
		for (Iterator it = setNear.iterator(); it.hasNext(); ) {
			myTuple=(Tuple)it.next();
			 
			myComp1 = (Component) myTuple.getValue("c1");
			if(!compvect.contains(myComp1)) {
				System.out.println("Base Component = " + myComp1.getName());
				resultString=resultString+"\nBase Component = " + myComp1.getName();
				obj = (Object) myTuple.getValue("s");
				Collection coll = (Collection) obj;
				for (Iterator it2 = coll.iterator(); it2.hasNext(); ) {
					Component myComp2= (Component) it2.next();
					System.out.println("Typed Component="+myComp2.getName());
					resultString=resultString+"\nTyped Component = "+myComp2.getName();
				}
				resultString=resultString+"\n";
			}
		}
		
	}
	
	public void registerVirtualConnector(Set set,Set setNear) {
		//Bag myBag;
		Object obj;
		org.eclipse.ocl.util.Tuple myTuple;
		Component myComp1,myComp2;
		org.eclipse.uml2.uml.Connector myConn;
		//org.eclipse.uml2.uml.Package myPackage;
		Vector<Component> compvect= new Vector();
		for (Iterator it = set.iterator(); it.hasNext(); ) {
			myTuple=(Tuple)it.next();	
			myComp1 = (Component) myTuple.getValue("c1");
			obj = (Object) myTuple.getValue("s");
			Collection coll = (Collection) obj;
			for (Iterator it2 = coll.iterator(); it2.hasNext(); ) {
				myComp2= (org.eclipse.uml2.uml.Component) it2.next();
				System.out.println("VirtualCaller" + myComp1.getName());
				resultString=resultString+"\nVirtualCaller = " + myComp1.getName();
				System.out.println("Virtual Called="+myComp2.getName());
				resultString=resultString+"\nVirtual Called = "+myComp2.getName();
				compvect.add(myComp2);
			}
		}
		
		//register near
		resultString=resultString+"\n\nNear Virtual Connector:\n";
		for (Iterator it = setNear.iterator(); it.hasNext(); ) {
			myTuple=(Tuple)it.next();	
			myComp1 = (Component) myTuple.getValue("c1");
			obj = (Object) myTuple.getValue("s");
			Collection coll = (Collection) obj;
			for (Iterator it2 = coll.iterator(); it2.hasNext(); ) {
				myComp2= (org.eclipse.uml2.uml.Component) it2.next();
				if(!compvect.contains(myComp2)) {
					System.out.println("VirtualCaller" + myComp1.getName());
					resultString=resultString+"\nVirtualCaller = " + myComp1.getName();
					System.out.println("VirtualConnector="+myComp2.getName());
					resultString=resultString+"\nVirtual Called = "+myComp2.getName();
				}
			}
		}
	}
	public void registerCallbackDetailed(Set set) {
		org.eclipse.ocl.util.Tuple myTuple;
		System.out.println("1");
		for (Iterator it = set.iterator(); it.hasNext(); ) {
			String tempResultString="\nProblem(s):";
			myTuple=(Tuple)it.next();
			int problemCounter=0;
			System.out.println("11");
			//caller comp
			Component c1 = (Component) myTuple.getValue("c1");
			if(c1==null) {
				problemCounter++;
				tempResultString=tempResultString+"\nCaller Component missing";
			}
			System.out.println("12");
			//receiver/callback comp
			Component c2 = (Component) myTuple.getValue("c2");
			if(c2==null) {
				problemCounter++;
				tempResultString=tempResultString+"\nReceiver/Callback Component missing";
			}
			System.out.println("13");
			//connector
			Connector conn = (Connector) myTuple.getValue("callbackConn");
			if(conn==null) {
				problemCounter++;
				tempResultString=tempResultString+"\nCallback stereotype not applied to connector";
			}
			System.out.println("14");
			//caller port
			Port p1 = (Port) myTuple.getValue("callerPort");
			if(p1==null) {
				problemCounter++;
				tempResultString=tempResultString+"\nCaller Port not stereotyped or connector end partWithPort not set";
			}
			System.out.println("15");
			//callback port
			Port p2 = (Port) myTuple.getValue("callbackPort");
			if(p2==null) {
				problemCounter++;
				tempResultString=tempResultString+"\nCallback Port not stereotyped or connector end partWithPort not set";
			}
			
			//caller prov
			Interface callerProv = (Interface) myTuple.getValue("callerProv");
			if(callerProv==null) {
				problemCounter++;
				tempResultString=tempResultString+"\nCaller side Provided not set/stereotyped";
			}
			System.out.println("16");
			//caller req
			Interface callerReq = (Interface) myTuple.getValue("callerReq");
			if(callerReq==null) {
				problemCounter++;
				tempResultString=tempResultString+"\nCaller side Required not set/stereotyped";
			}

			//caller prov
			Interface callbackProv = (Interface) myTuple.getValue("callbackProv");
			if(callbackProv==null) {
				problemCounter++;
				tempResultString=tempResultString+"\nCallback side Provided not set/stereotyped";
			}
			System.out.println("17");
			//caller req
			Interface callbackReq = (Interface) myTuple.getValue("callbackReq");
			if(callbackReq==null) {
				problemCounter++;
				tempResultString=tempResultString+"\nCallback side Required not set/stereotyped";
			}

			//Interface observeEventMatch = (Interface) myTuple.getValue("observeEventMatch");
			Integer observeEventMatch = (Integer)myTuple.getValue("observeEventMatch");
			if(observeEventMatch==0) {
				problemCounter++;
				tempResultString=tempResultString+"\nObserveEvent interface not matched";
			}
			System.out.println("18");
			//Interface updateMatch = (Interface) myTuple.getValue("updateMatch");
			Integer updateMatch = (Integer)myTuple.getValue("updateMatch");
			if(updateMatch==0) {
				problemCounter++;
				tempResultString=tempResultString+"\nupdateMatch interface not matched";
			}
			System.out.println("problemCount="+problemCounter);
			if(problemCounter<6) {
				if(problemCounter==0) {
					tempResultString=tempResultString+"\nNone\n";
				}
				resultString=resultString+"\ncaller="+c1.getName()+"  callback="+c2.getName()+tempResultString+"\n";
				
			}
			System.out.println("19");
		}
		System.out.println("2");
	}
}
