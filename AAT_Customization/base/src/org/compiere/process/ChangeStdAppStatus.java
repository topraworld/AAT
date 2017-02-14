package org.compiere.process;

import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.apps.ADialog;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

public class ChangeStdAppStatus extends CalloutEngine{

	private int ad_window_id;
	
	private enum c_app_status{
		
		Send_For_Approval("SA"), Pending_Results("PR"), Not_Approved("NA"),
		Edit_List_Approved("EA"),Approved("AP") , Draft("DR");
		
		private final String value;
	    private c_app_status(String value) {
	        this.value = value;
	    }
	}
	
	private enum ad_window{
		
		Student_Registration_Verifier_Screen(1000026),Student_Registration_Data_Entry(1000000),
		Student_Registration_Pending_Approvals_Approved(1000029);
		
		private final int value;
	    private ad_window(int value) {
	        this.value = value;
	    }
	}
	
	//org.compiere.process.ChangeStdAppStatus.setAppStatus
	public String setAppStatus(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{	
		if(mTab.getValue("name") == null) return "";
		
		this.ad_window_id = mField.getAD_Window_ID();
		if(ad_window.Student_Registration_Data_Entry.value == ad_window_id){	
			
			//popupe the confirm message
			if(this.isConfirm(WindowNo)){
				//set send for approval date time
				this.setSendForAprovalDateTime(mTab, WindowNo);
				//update the aplication_entered and application_enteredby fields
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				mTab.setValue("aplication_entered", ts);
				mTab.setValue("application_enteredby", mTab.getValue("updatedby"));
				//set application status
				mTab.setValue("c_app_status", c_app_status.Send_For_Approval.value);
				
				mTab.dataSave(false);
				mTab.dataRefreshAll();
				//
				//mTab.dataNew(false);
				
			}
		}
		
		return "";
	}
	
	//Send for approval visible false
	private void setGridFieldVisible(GridTab mTab, String gfname, boolean visible){
		
		GridField gridfield = mTab.getField(gfname);
		gridfield.setDisplayed(visible);
	}
	
	//Get conformation  to initiate the workflow
	private boolean isConfirm(int WindowNo){
		
		return ADialog.ask(WindowNo, null,"Are you sure you want to send this for workflow ?");
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
