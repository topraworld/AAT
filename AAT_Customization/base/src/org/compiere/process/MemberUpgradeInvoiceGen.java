package org.compiere.process;

import java.sql.Timestamp;
import java.util.Calendar;

import org.compiere.apps.ADialog;
import org.compiere.apps.wf.WFActivity;
import org.compiere.model.MBPGroup;
import org.compiere.model.MBPartner;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.util.Trx;
import org.compiere.wf.MWFActivity;

public class MemberUpgradeInvoiceGen {

	private WFActivity wfActivity;
	private MInvoice mInvoice;
	private MWFActivity m_activity;
	private int m_product_id;
	private MBPartner bpartner;
	private Trx trx;
	
	public MemberUpgradeInvoiceGen(WFActivity wfActivity) {
		
		this.wfActivity = wfActivity;
		this.m_activity = wfActivity.getMActivity();
		this.bpartner = MBPartner.get(m_activity.getCtx(), m_activity.getRecord_ID());
		
		trx = Trx.get(Trx.createTrxName("FWFA"), true);
		this.mInvoice = new MInvoice(m_activity.getCtx(), 0, trx.getTrxName());
		
		mInvoice.set_ValueOfColumn("M_PriceList_ID", 1000012	); 
		mInvoice.set_ValueOfColumn("c_doctypetarget_id", 100000484);
		mInvoice.set_ValueOfColumn("c_bpartner_id", bpartner.get_ID());
		mInvoice.set_ValueOfColumn("c_currency_id", 313);
		mInvoice.set_ValueOfColumn("c_doctype_id", 0);
		mInvoice.set_ValueOfColumn("ad_org_id", 1000018);
	}
	
	private enum c_bp_group{
		
		MAAT(100000492),
		FMAAT(100000490), 
		SAT(100000491);
		
		private final int value;
	    private c_bp_group(int value) {
	        this.value = value;
	    }
	}
	
	private enum m_product{
		
		MAAT_SAT_Life(1000774),
		SAT_FMAAT_Life(1000779),
		SAT_FMAAT_Ord(1000782),
		MAAT_SAT_Ord(1000780),
		MAAT_FMAAT(100002792);
		
		private final int value;
	    private m_product(int value) {
	        this.value = value;
	    }
	}
	
	public void generateInvoice(){
		
		boolean status = false;
		
		try{
			//get current membership category
			int c_bp_group_id =  this.bpartner.get_ValueAsInt("c_bp_group_id");
			boolean is_sat_direct = this.bpartner.get_ValueAsBoolean("is_s_direct_upgrade");
			boolean is_fmaat_direct = this.bpartner.get_ValueAsBoolean("is_f_direct_upgrade");
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			//select the product according to the upgrade type and change application information
			if(c_bp_group.MAAT.value == c_bp_group_id){
				
				if(is_sat_direct){
					
					this.m_product_id = m_product.MAAT_SAT_Life.value;
					this.bpartner.setBPGroup(new MBPGroup(m_activity.getCtx(), c_bp_group.SAT.value, trx.getTrxName()));
					this.bpartner.set_ValueOfColumn("member_type", "lm");
					this.bpartner.set_ValueOfColumn("sat_effective_date", ts);
					this.bpartner.set_ValueOfColumn("c_sat_id", this.bpartner.getValue());
					
				}else if(is_fmaat_direct){
					this.m_product_id = m_product.MAAT_FMAAT.value;
					
					this.bpartner.setBPGroup(new MBPGroup(m_activity.getCtx(), c_bp_group.FMAAT.value, trx.getTrxName()));
					this.bpartner.set_ValueOfColumn("member_type", "lm");
					this.bpartner.set_ValueOfColumn("fmaat_effective_date", ts);
					this.bpartner.set_ValueOfColumn("c_fmaat_id", this.bpartner.getValue());
					
				}else{
					this.m_product_id = m_product.MAAT_SAT_Ord.value;
					
					this.bpartner.setBPGroup(new MBPGroup(m_activity.getCtx(), c_bp_group.SAT.value, trx.getTrxName()));
					this.bpartner.set_ValueOfColumn("member_type", "om");
					this.bpartner.set_ValueOfColumn("sat_effective_date", ts);
					this.bpartner.set_ValueOfColumn("c_sat_id", this.bpartner.getValue());
					this.bpartner.set_ValueOfColumn("mem_last_renew_year", Calendar.getInstance().get(Calendar.YEAR));
				}
				
			}else if(c_bp_group.SAT.value == c_bp_group_id){
				
				if(is_fmaat_direct){
					this.m_product_id = m_product.SAT_FMAAT_Life.value;
					
					this.bpartner.setBPGroup(new MBPGroup(m_activity.getCtx(), c_bp_group.FMAAT.value, trx.getTrxName()));
					this.bpartner.set_ValueOfColumn("member_type", "lm");
					this.bpartner.set_ValueOfColumn("fmaat_effective_date", ts);
					this.bpartner.set_ValueOfColumn("c_fmaat_id", this.bpartner.getValue());
					
				}else{
					this.m_product_id = m_product.SAT_FMAAT_Ord.value;
					
					this.bpartner.setBPGroup(new MBPGroup(m_activity.getCtx(), c_bp_group.FMAAT.value, trx.getTrxName()));
					this.bpartner.set_ValueOfColumn("member_type", "om");
					this.bpartner.set_ValueOfColumn("fmaat_effective_date", ts);
					this.bpartner.set_ValueOfColumn("c_fmaat_id", this.bpartner.getValue());
					this.bpartner.set_ValueOfColumn("mem_last_renew_year", Calendar.getInstance().get(Calendar.YEAR));
				}
			}
			
			this.bpartner.save();
			
			if(m_product_id > 0){
				
				status = mInvoice.save();
				MInvoiceLine line = new MInvoiceLine(mInvoice);
				
				line.setM_Product_ID(m_product_id);
				line.setQty(1);
				status = line.save();
				
				mInvoice.processIt("CO");
				mInvoice.completeIt();
				status = mInvoice.save();
				
				if(status){
					ADialog.error(wfActivity.getWindoseNo(), this.wfActivity, "Member Upgrade Process Success!!");
					trx.commit();
				}
				else
					trx.rollback();
			}
		}catch(Exception ex){
			
		}	
	}
	
	
	public void setApplicationChanges(){
		
	}
}
