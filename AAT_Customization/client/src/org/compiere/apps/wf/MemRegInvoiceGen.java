package org.compiere.apps.wf;


import org.compiere.apps.ADialog;
import org.compiere.model.MBPartner;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.util.Trx;
import org.compiere.wf.MWFActivity;

public class MemRegInvoiceGen {

	private WFActivity wfActivity;
	private MInvoice mInvoice;
	private MWFActivity m_activity;
	private int m_product_id;
	private MBPartner bpartner;
	private Trx trx;
	
	protected MemRegInvoiceGen(WFActivity wfActivity) {
		
		this.wfActivity = wfActivity;
		this.m_activity = wfActivity.getMActivity();
		this.bpartner = MBPartner.get(m_activity.getCtx(), m_activity.getRecord_ID());
	} 
	
	//Membership Registration invoice create
	protected boolean memRegInvoiceGen(){
		
		boolean status = false;
		
		try{
			
			trx = Trx.get(Trx.createTrxName("FWFA"), true);
			this.mInvoice = new MInvoice(m_activity.getCtx(), 0, trx.getTrxName());
			
			mInvoice.set_ValueOfColumn("M_PriceList_ID", 100000487); 
			mInvoice.set_ValueOfColumn("c_doctypetarget_id", 1000107);
			
			mInvoice.set_ValueOfColumn("c_bpartner_id", bpartner.get_ID());
			mInvoice.set_ValueOfColumn("c_currency_id", 313);
			mInvoice.set_ValueOfColumn("c_doctype_id", 0);
			mInvoice.set_ValueOfColumn("ad_org_id", 1000018);
			
			String member_type = bpartner.get_ValueAsString("member_type");
			int c_bp_group_id = bpartner.getBPGroup().get_ID();
			
			//Ordinary member to Temp MAAT
			//c_bp_group_id 1000016 Temp-Member
			if(member_type.equals("om") && c_bp_group_id == 100000493){//100000493
				//(M_Product - M_Product_ID=1000016) - Ordinary Membership Registration-MAAT
				//Select Product price
				m_product_id = 1000771;
				
			//Life Time member to Temp MAAT
			//c_bp_group_id 1000016 Temp-Member	
			}else if(member_type.equals("lm") && c_bp_group_id == 100000493){//100000493
				//(M_Product - M_Product_ID=1000024) - Life membership - MAAT
				//Select Product price
				m_product_id = 1000766;
			
			//Life Time member to Temp SAT	
			//(C_BP_Group - C_BP_Group_ID=1000017)	
			}else if(member_type.equals("lm") && c_bp_group_id == 100000494){//10000494
				//(M_Product - M_Product_ID=1000025) - Life membership - SAT
				//Select Product price
				m_product_id = 1000767;
			
			//Life Time member to Temp FMAAT	
			//(C_BP_Group - C_BP_Group_ID=1000018)
			}else if(member_type.equals("lm") && c_bp_group_id == 10000495){//10000495
				//(M_Product - M_Product_ID=1000026) - Life membership - FMAAT
				//Select Product price
				m_product_id = 1000765;
			}
			
			if(m_product_id > 0){
			
				status = mInvoice.save();
				MInvoiceLine line = new MInvoiceLine(mInvoice);
				
				line.setM_Product_ID(m_product_id);
				line.setQty(1);
				//line.setTax();
				//C_Tax_ID=1000004
				status = line.save();
				
				mInvoice.processIt("CO");
				mInvoice.completeIt();
				status = mInvoice.save();
				
				if(status)
					trx.commit();
				else
					trx.rollback();
				
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			trx.rollback();
			ADialog.error(wfActivity.getWindoseNo(), this.wfActivity, "Member registration invoice generation failed!.Please contact system administrator");
		}
		return status;
	}
}
