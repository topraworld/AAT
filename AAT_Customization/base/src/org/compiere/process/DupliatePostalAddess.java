package org.compiere.process;

import java.util.Properties;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;

public class DupliatePostalAddess extends CalloutEngine{

	//org.compiere.process.DupliatePostalAddess.validate
	public String validate(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value == null) return "";
		
		MBPartnerLocation[] locations = MBPartner.get(ctx, Integer.parseInt(mTab.getValue("c_bpartner_id").toString())).getLocations(true);
		
		if(locations.length > 1){
			for(MBPartnerLocation loc : locations){
				if(loc.get_ID() == mTab.getRecord_ID())
					continue;
				else{
					loc.setIsBillTo(false);
					loc.save();
				}
			}
		}
		return "";
	}
}
