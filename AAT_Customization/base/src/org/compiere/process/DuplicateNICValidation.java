package org.compiere.process;
import java.util.Properties;

import org.compiere.apps.ADialog;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.DB;

public class DuplicateNICValidation extends CalloutEngine{
	
	//org.compiere.process.DuplicateNICValidation.validate
	public String validate(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{	
		try{
			if(mTab.getValue("referenceno")!=null){
				
				String sql = " SELECT C_BPARTNER_ID FROM C_BPARTNER WHERE REFERENCENO = '"+ mTab.getValue("referenceno") + "' AND ISACTIVE='Y'"
						+ " AND AD_CLIENT_ID = "+ mTab.getValue("ad_client_id");
				
				String c_bpartner_id = DB.getSQLValueString(null, sql);
				if(!(c_bpartner_id == null || c_bpartner_id.equals(""))){
					mTab.setValue("referenceno", "");
					ADialog.error(WindowNo, null, "Duplicate NIC!");
				}
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return "";
	}
}
