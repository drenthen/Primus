/*
 * Licensed Material - Property of NICK 
 * (C) Copyright NICK Corp. 2002 - All Rights Reserved. 
 */
package action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Component;

/**
 * Model class containing the data for the primitive wizard
 */
public class PrimitiveModel 
{
	public static final String copyright = "(c) Copyright NICK Corporation 2002.";
		
	public String primitiveChoice;

	//callback data
	public boolean callerNew;
	public boolean callbackNew;
	public Component caller=null;
	public Component callback=null;
	
	public boolean clientActive;
	public boolean clientNew;
	public boolean indirectorNew;
	public boolean targetNew;
	public Component client=null;
	public Component indirector=null;
	public Component target=null;
	ArrayList clients = new ArrayList();
	ArrayList targets = new ArrayList();
	
	public boolean groupPackageNew;
	public org.eclipse.uml2.uml.Package groupPackage;
	
	public boolean layerPackageNew;
	public org.eclipse.uml2.uml.Package layerPackage;
	public int layerNumber;
	public boolean mainAggActive;
	public boolean mainAggNew;
	public boolean agg1New;
	public boolean agg2New;
	public Component mainAgg=null;
	public Component agg1=null;
	public Component agg2=null;
	public String aggregationType="";
	
	public boolean mainCompNew;
	public boolean leafCompNew;
	public boolean compositeCompNew;
	public Component main=null;
	public Component leaf=null;
	public Component composite=null;
	
	public boolean clietShieldNew;
	public boolean accesspointShieldNew;
	public boolean protectedShieldNew;
	public boolean groupShieldNew;
	public boolean protectedActive;
	public Component clientShield=null;
	public Component accessPointShield=null;
	public Component protectedShield=null;
	public org.eclipse.uml2.uml.Package groupShield=null;
	
	public boolean baseNew;
	public boolean typeNew;
	public boolean supertypeNew;
	public Component base;
	public Component type;
	public Component supertype;
	public boolean firstActive;
	public boolean thirdActive;
	
	public boolean callerVCNew;
	public boolean receiverVCNew;
	public Component callerVC;
	public Component receiverVC;
	
	public boolean callerConnectorNew;
	public boolean receiverConnectorNew;
	public Component callerConnector;
	public Component receiverConnector;
	
	public void createPrimitive() {

	}
}
