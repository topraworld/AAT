package org.compiere.process;
import org.compiere.model.MBPartner;

public class StudentIdPrintValidation extends SvrProcess{

	int C_BPartner_ID;
	@Override
	protected void prepare() {
		
		C_BPartner_ID = this.getRecord_ID();
	}
	
	@Override
	protected String doIt() throws Exception {
		MBPartner mbPartner = MBPartner.get(getCtx(), C_BPartner_ID);
		if(!mbPartner.get_ValueAsBoolean("is_id_printed")){
			mbPartner.set_CustomColumn("is_id_printed", "Y");
			mbPartner.save();
		}
		return "";
	}
}
