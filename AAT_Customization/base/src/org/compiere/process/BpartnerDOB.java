package org.compiere.process;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

public class BpartnerDOB extends CalloutEngine{
	
	public String setAge(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{	
		try{
			if(mTab.getValue("birthday")!=null){
				
				String c_age = mTab.getValue("birthday").toString();
				
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date d1 = sdf.parse(c_age);
				Date d2 = new Date();
				int difInDays = (int) ((d2.getTime() - d1.getTime())/(1000*60*60*24));
				int years = difInDays / 365;
				mTab.setValue("c_age", years);
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return "";
	}
}
