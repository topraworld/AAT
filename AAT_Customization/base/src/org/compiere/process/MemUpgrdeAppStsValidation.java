package org.compiere.process;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.apps.ADialog;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

public class MemUpgrdeAppStsValidation extends CalloutEngine{
	
	enum mem_upgrade_app_sts{
		
		Approved("AP"),CPD_Work_Experience_Approved("CA") , CPD_Work_Experience_Verified("CV"),
		Send_for_Approval("SA"),Send_For_Upgrade("SU");
		
		private final String value;
	    private mem_upgrade_app_sts(String value) {
	        this.value = value;
	    }

	    public String getValue() {
	        return value;
	    }
	}
	
	private enum ad_window{
		
		Member_Upgrade_Aproval(100000498),Interview_Feedback(100000497) , 
		Verifier_Screen(100000495),Verifier_WE_CPD_Aproval(100000496) , Registered_Members(1000036);
		
		private final int value;
	    private ad_window(int value) {
	        this.value = value;
	    }
	}
	
	//org.compiere.process.MemUpgrdeAppStsValidation.validateUpgrdAppStatus
	public String validateUpgrdAppStatus (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{	
		try{
			if(value ==null) return "";
				
			int ad_window_id = mField.getAD_Window_ID();
			String app_sts = mTab.getValue("c_mem_upgrade_status").toString();
			
			if(app_sts.equals("")) return "";
			
			//Data Entry window
			if(ad_window.Registered_Members.value == ad_window_id){
				if(mem_upgrade_app_sts.Send_For_Upgrade.value.equals(app_sts)){
					//popupe the confirm message
					if(this.isConfirm(WindowNo)){
						
						mTab.dataSave(true);
						mTab.dataRefreshAll();
					}
				}
				else this.getValidateMessage(mTab, WindowNo , mem_upgrade_app_sts.Send_For_Upgrade.value);
				
			}else if(ad_window.Verifier_Screen.value == ad_window_id){
				if(mem_upgrade_app_sts.CPD_Work_Experience_Verified.value.equals(app_sts)){
					//popupe the confirm message
					if(this.isConfirm(WindowNo)){
						
						mTab.dataSave(true);
						mTab.dataRefreshAll();
					}
				}
				else this.getValidateMessage(mTab, WindowNo , mem_upgrade_app_sts.Send_For_Upgrade.value);
				
			}else if(ad_window.Verifier_WE_CPD_Aproval.value == ad_window_id){
				if(mem_upgrade_app_sts.CPD_Work_Experience_Approved.value.equals(app_sts)){
					//popupe the confirm message
					if(this.isConfirm(WindowNo)){
						
						mTab.dataSave(true);
						mTab.dataRefreshAll();
					}
				}
				else this.getValidateMessage(mTab, WindowNo , mem_upgrade_app_sts.Send_For_Upgrade.value);
				
			}else if(ad_window.Interview_Feedback.value == ad_window_id){
				if(mem_upgrade_app_sts.Send_for_Approval.value.equals(app_sts)){
					//popupe the confirm message
					if(this.isConfirm(WindowNo)){
						
						mTab.dataSave(true);
						mTab.dataRefreshAll();
					}
				}
				else this.getValidateMessage(mTab, WindowNo , mem_upgrade_app_sts.Send_For_Upgrade.value);	
				
			}else if(ad_window.Member_Upgrade_Aproval.value == ad_window_id){
				if(mem_upgrade_app_sts.Approved.value.equals(app_sts)){
					//popupe the confirm message
					if(this.isConfirm(WindowNo)){
						
						mTab.dataSave(true);
						mTab.dataRefreshAll();
					}
				}
				else this.getValidateMessage(mTab, WindowNo , mem_upgrade_app_sts.Send_For_Upgrade.value);
				
			}else{
				this.setGridFieldVisible(mTab , "c_app_status" , true);
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return "";
	}
	//validation message
	private void getValidateMessage(GridTab mTab ,int WindowNo , String appstatus){
		ADialog.error(WindowNo, null, "Invalid Selection! Please select the correct Application Status to proceed!");
		mTab.setValue("c_app_status" ,null);
	}
	
	//Send for approval visible false
	private void setGridFieldVisible(GridTab mTab, String gfname, boolean visible){
		
		GridField gridfield = mTab.getField(gfname);
		gridfield.setDisplayed(visible);
	}
	
	//Get conformation  to initiate the workflow
	private boolean isConfirm(int WindowNo){
		
		return ADialog.ask(WindowNo, null,"Are you sure you want to send this for upgrade workflow ?");
	}
	
	//Get conformation  to initiate the workflow
	private boolean isStudentApproveConfirm(int WindowNo){
		
		return ADialog.ask(WindowNo, null,"Are you sure to approve this student ?");
	}
	
	//set send for approvel data time
	private void setSendForAprovalDateTime(GridTab mTab ,int WindowNo){
		
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		mTab.setValue("send_for_aproval_date", ts);
	}
}
