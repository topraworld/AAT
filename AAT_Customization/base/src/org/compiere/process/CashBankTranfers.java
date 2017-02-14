package org.compiere.process;

import java.math.BigDecimal;

import org.compiere.apps.ADialog;

//org.compiere.process.CashBankTranfers
public class CashBankTranfers extends SvrProcess{

	private int ad_org_id;
	private int c_bankaccount_id;
	
	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("ad_org_id"))
				ad_org_id = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("c_bankaccount_id"))
				c_bankaccount_id = ((BigDecimal)para[i].getParameter()).intValue();
		}
		
		
	}

	@Override
	protected String doIt() throws Exception {
		
		if(ad_org_id > 0 || c_bankaccount_id < 0){
			
		}else{
			ADialog.error(0, null, "Process Error.Invalid input");
			return "";
		}
		
		return "";
	}

}

