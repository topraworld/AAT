package org.compiere.process;

import java.util.Properties;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

public class GreetingBasedGender extends CalloutEngine{

	//org.compiere.process.GreetingBasedGender.setGender
	public String setGender (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{	
		
		if(value!=null){
			
			if(((int)value) == 1000000 || ((int)value) == 100000482)
				mTab.setValue("c_gender", "Male");
			else if(((int)value) == 1000005 || ((int)value) == 100000483)
				mTab.setValue("c_gender", "Female");
			else
				mTab.setValue("c_gender", "");
		}
		return "";
	}
}
