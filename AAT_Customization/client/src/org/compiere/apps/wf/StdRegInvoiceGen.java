package org.compiere.apps.wf;

import org.compiere.apps.ADialog;
import org.compiere.model.MBPartner;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.wf.MWFActivity;

public class StdRegInvoiceGen {

	private WFActivity wfActivity;
	private MInvoice mInvoice;
	private MWFActivity m_activity;
	private int m_product_id;
	private MBPartner bpartner;
	
	protected StdRegInvoiceGen(WFActivity wfActivity) {
		
		this.wfActivity = wfActivity;
		this.m_activity = wfActivity.getMActivity();
		this.mInvoice = new MInvoice(m_activity.getCtx(), 0, m_activity.get_TrxName());
		this.bpartner = MBPartner.get(m_activity.getCtx(), m_activity.getRecord_ID());
		
		//set bpartner location as *
		bpartner.setAD_Org_ID(0);
		bpartner.save();
		
		String pmt_ctgry = bpartner.get_ValueAsString("c_payment_category") == null?"":bpartner.get_ValueAsString("c_payment_category").toString().trim();
		
		mInvoice.set_ValueOfColumn("m_warehouse_id", 1000002);
		mInvoice.set_ValueOfColumn("c_doctype_id", 1000104);
		mInvoice.set_ValueOfColumn("M_PriceList_ID", 100000481); 
		mInvoice.set_ValueOfColumn("c_doctypetarget_id", 1000104);
		
		//select product based on the client
		//select the product based on the student payment category
		if(pmt_ctgry.equals("SL1"))
			m_product_id = 1000778;
		else if(pmt_ctgry.equals("S01"))
			m_product_id = 1000777;  
		else if(pmt_ctgry.equals("NL2"))
			m_product_id = 1000776;
		else if(pmt_ctgry.equals("N02"))
			m_product_id = 1000775;
		else
			m_product_id = 1000777;
	} 
	
	//Student Registration invoice create
	protected boolean stdRegInvoiceGen(){
		
		boolean status = false;
		
		try{
			
			mInvoice.set_ValueOfColumn("c_bpartner_id", bpartner.get_ID());
			mInvoice.set_ValueOfColumn("c_doctype_id", 0);
			mInvoice.set_ValueOfColumn("ad_org_id", 1000016);
			mInvoice.set_ValueOfColumn("c_currency_id", 313);
			
			mInvoice.save();
			MInvoiceLine line = new MInvoiceLine(mInvoice);
			
			line.setM_Product_ID(m_product_id);
			line.setQty(1);
			line.save();
			
			mInvoice.processIt("CO");
			mInvoice.completeIt();
			if(mInvoice.save())
				status = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
			ADialog.error(wfActivity.getWindoseNo(), this.wfActivity, "Student registration invoice generation failed!.Please contact system administrator");
		}
		return status;
	}
}
