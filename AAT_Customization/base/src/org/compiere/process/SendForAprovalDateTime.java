package org.compiere.process;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.apps.ADialog;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

public class SendForAprovalDateTime extends CalloutEngine{

	public String setSendForAprovalDateTime (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{	
		//Student registration window validation
		if(value!=null && mField.getAD_Window_ID() == 1000000){
			if(((String)value).equals("SA")){
				if((mTab.getValue("send_for_aproval_date"))!=null){
					ADialog.error(WindowNo, null, "Error - Your have already send this for approval!");
				}
				else
				{
					Boolean i = ADialog.ask(WindowNo, null,"Are you sure you want to send this for approval?");
					if(i){
						Timestamp ts = new Timestamp(System.currentTimeMillis());
						mTab.setValue("send_for_aproval_date", ts);
						//(mTab.getField("c_app_status")).re;
					}
					else
						mTab.setValue("c_app_status", null);
				}	
			}
			else{
				mTab.setValue("c_app_status", null);
				ADialog.error(WindowNo, null, "Error - Your not allowed to change the application status!");
			}
		}
		
		return "";
	}
}
