package org.compiere.process;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

public class TotalCarrerMonth extends CalloutEngine{

	//org.compiere.process.TotalCarrerMonth.setTotalCarrerMonthCurrent
	public String setTotalCarrerMonthCurrent (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{	
		//This is for calculate total months for previous carrer type  
		try{
			if(mTab.getValue("career_date_joining")!=null && mTab.getValue("career_type")!=null){
				
				if(mTab.getValue("career_type").equals("C")){
					
					String joining = mTab.getValue("career_date_joining").toString();
					DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					
					//current date
					Date date = new Date();
					Date d1 = sdf.parse(joining);
					Date d2 =sdf.parse(sdf.format(date));
					
					int difInDays = (int) ((d2.getTime() - d1.getTime())/(1000*60*60*24));
					int months = difInDays / 30;
					mTab.setValue("career_tot_months", months);
				}	
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return "";
	}
}