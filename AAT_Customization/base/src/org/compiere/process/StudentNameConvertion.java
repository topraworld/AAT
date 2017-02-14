package org.compiere.process;

import java.util.Properties;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

public class StudentNameConvertion extends CalloutEngine{

	//Created by chathuranga Topra (Pvt) Ltd
	//Date - 2016/06/15
	//AAT Customization
	
	public String setNames (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		try{
			if(mTab.getValue("name")!=null){
				
				String fullname =mTab.getValue("name").toString();
				fullname = this.getFirstLetterUpper(fullname);
				String initials = "";
				int length  = 0;
				
				String[] splited = fullname.split("\\s+");
				
				if(splited.length > 0){
					
					length = splited.length;
					//set student full name as change first letter upper case
					mTab.setValue("name", fullname);
					//set student last name
					mTab.setValue("c_lastname", splited[(length - 1)]);
					//set initials 
					initials =  this.getInitials(splited);
					mTab.setValue("c_initials",initials);
					//set Initials stand for
					mTab.setValue("c_initials_stand", this.getInitialsStandFor(splited));
					mTab.setValue("name2",initials+ splited[(length - 1)]);
					mTab.dataSave(true);
				}
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return "";
	}
	
	private String getInitials(String [] fullname){
		
		String initials = "";
		if(fullname.length >= 0){
			
			for(int i = 0; i < (fullname.length -1);i++){
				initials += fullname[i].charAt(0)+".";
			}
		}
		return initials.toUpperCase();
	}
	
	private String getInitialsStandFor(String [] fullname){
		
		String iniStandFor = "";
		if(fullname.length >= 0){
			
			for(int i = 0; i < (fullname.length -1);i++){
				iniStandFor += fullname[i] + " ";
			}
		}
		return iniStandFor;
	}
	
	private String getFirstLetterUpper(String source){
		
		String[] strArr = source.split("\\s+");
		
	    StringBuffer res = new StringBuffer();	    
	    for (String str : strArr) {
	        char[] stringArray = str.trim().toCharArray();
	        stringArray[0] = Character.toUpperCase(stringArray[0]);
	        str = new String(stringArray);

	        res.append(str).append(" ");
	    }
		return res.toString().trim();
	}
}