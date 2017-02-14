package org.compiere.process;
import java.util.Properties;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

public class MembreshipTypeValidation extends CalloutEngine{
	
	public String setMemType(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{	
		if(value!=null){
			if(value.equals(true)){
				mTab.setValue("member_type" , "lm");
			}
		}
		return "";
	}
}
