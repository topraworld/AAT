package org.compiere.process;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.apps.ADialog;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

public class StdAppStatusValidation extends CalloutEngine{
	
	private enum c_app_status{
			
		Send_For_Approval("SA"), Pending_Results("PR"), Not_Approved("NA"),
		Edit_List_Approved("EA"),Approved("AP") , Draft("DR");
		
		private final String value;
	    private c_app_status(String value) {
	        this.value = value;
	    }
	}
	
	private enum ad_window{
		
		Student_Registration_Verifier_Screen("1000026"),Student_Registration_Data_Entry("1000000"),
		Student_Registration_Pending_Approvals_Approved("1000029");
		
		private final String value;
	    private ad_window(String value) {
	        this.value = value;
	    }
	}
	
	public String validate (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{	
		try{
			//System.out.println(mTab.getValue("c_app_status").toString());
			//value!=null
			if(!(value==null || mTab.getValue("c_app_status").toString().equals("DR"))){
				
				String ad_window_id = mField.getAD_Window_ID()+"";
				String app_sts = mTab.getValue("c_app_status").toString();
				
				//Data Entry window
				if(ad_window.Student_Registration_Data_Entry.value.equals(ad_window_id)){
					if(c_app_status.Draft.value.equals(app_sts) || c_app_status.Send_For_Approval.value.equals(app_sts)){
						if(c_app_status.Send_For_Approval.value.equals(app_sts)){
							//popupe the confirm message
							if(this.isConfirm(WindowNo)){
								//update the aplication_entered and application_enteredby fields
								Timestamp ts = new Timestamp(System.currentTimeMillis());
								mTab.setValue("aplication_entered", ts);
								mTab.setValue("send_for_aproval_date", ts);
								mTab.setValue("application_enteredby", mTab.getValue("updatedby"));
								mTab.dataSave(true);
								mTab.dataRefreshAll();
								mTab.dataNew(false);
							}
						}
					}
					else{
						this.getValidateMessage(mTab, WindowNo , c_app_status.Draft.value);
					}
					
				//	Edit list approve
				}else if(ad_window.Student_Registration_Pending_Approvals_Approved.value.equals(ad_window_id)){
					if(c_app_status.Draft.value.equals(app_sts) || c_app_status.Edit_List_Approved.value.equals(app_sts)){
						if(c_app_status.Edit_List_Approved.value.equals(app_sts)){
							//popupe the confirm message
							if(this.isConfirm(WindowNo)){
								//set visible false of the Member Application status
								//this.setGridFieldVisible(mTab , "c_app_status" , false);
								//set send for approval date time
								this.setSendForAprovalDateTime(mTab, WindowNo);
								mTab.dataSave(true);
								mTab.dataRefreshAll();
							}
						}
					}
					else{
						this.getValidateMessage(mTab, WindowNo , c_app_status.Draft.value);
					}
					
				}else if(ad_window.Student_Registration_Verifier_Screen.value.equals(ad_window_id)){
					
					if(!(c_app_status.Draft.value.equals(app_sts) || c_app_status.Send_For_Approval.value.equals(app_sts) ||c_app_status.Edit_List_Approved.value.equals(app_sts) )){
						if(c_app_status.Approved.value.equals(app_sts)){
							//popupe the confirm message
							if(this.isStudentApproveConfirm(WindowNo)){
								//set visible false of the Member Application status
								//this.setGridFieldVisible(mTab , "c_app_status" , false);
								//set send for approval date time
								this.setSendForAprovalDateTime(mTab, WindowNo);
								mTab.dataSave(true);
								mTab.dataRefreshAll();
							}
						}
					}else{
						this.getValidateMessage(mTab, WindowNo , c_app_status.Draft.value);
					}
				}
			}else{
				//System.out.println("Hello");
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
