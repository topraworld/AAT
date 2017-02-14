package org.compiere.process;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.apps.ADialog;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

//This class is designed for validate each window wise member application status

public class MemAppStatusValidation extends CalloutEngine{
	
	private enum mem_app_status{
		
		Draft("DR"),Edit_List_Appoved("EA") , Interview_Eligibility_Approved("IA"),Interview_Eligibility_Verified("IV"),
		Member_Approved("MA"),Not_Approved("NA") , Send_For_Approval("SA"),Send_For_Edit_List_Approval("SE"),Training_Approved("TA");
		
		private final String value;
	    private mem_app_status(String value) {
	        this.value = value;
	    }
	}
	
	private enum c_bp_group{
		
		Temp_MAAT("100000493"),Temp_FMAAT("100000495") , Temp_SAT("100000494");
		
		private final String value;
	    private c_bp_group(String value) {
	        this.value = value;
	    }
	}
	
	private enum ad_window{
		
		Membership_Data_Entry("1000001"),Membership_Data_Edit_List("1000037") , Membership_Traning_Division("100000490"),Membership_Interview_Eligebility("1000039"),
		Membership_Interview_Approval("1000040"),Membership_Send_For_Approval("1000041") , Membership_Approve_Member("1000042");
		
		private final String value;
	    private ad_window(String value) {
	        this.value = value;
	    }
	}

	public String validateMemAppStatus (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		try{
			if(value!=null){
				
				String ad_window_id = mField.getAD_Window_ID()+"";
				String app_sts = mTab.getValue("c_mem_app_status").toString();
				
				//Data Entry window
				if(ad_window.Membership_Data_Entry.value.equals(ad_window_id)){
					if(mem_app_status.Draft.value.equals(app_sts) || mem_app_status.Send_For_Edit_List_Approval.value.equals(app_sts)){
						if(mem_app_status.Send_For_Edit_List_Approval.value.equals(app_sts)){
							
							//validate correct membership category
							String C_BP_Group_ID = mTab.getValue("C_BP_Group_ID")==null?"":mTab.getValue("C_BP_Group_ID").toString().trim();
							if(C_BP_Group_ID.equals(c_bp_group.Temp_MAAT.value) || C_BP_Group_ID.equals(c_bp_group.Temp_FMAAT.value) || C_BP_Group_ID.equals(c_bp_group.Temp_SAT.value)){
								
								//popupe the confirm message
								if(this.isConfirm(WindowNo)){
									//set visible false of the Member Application status
									Timestamp ts = new Timestamp(System.currentTimeMillis());
									mTab.setValue("mem_aplication_entered", ts);
									mTab.setValue("m_send_for_aproval_date", ts);
									mTab.setValue("mem_aplication_enteredby", mTab.getValue("updatedby").toString());
									mTab.dataSave(true);
									mTab.dataRefreshAll();
									mTab.dataNew(false);
								}
								
							}else{
								
								GridField gridfield = mTab.getField("C_BP_Group_ID");
								gridfield.setError(true);
								mTab.setValue("c_mem_app_status" ,null);
								ADialog.error(WindowNo, null, "Save Failed. Invalid Membership Category!");
								return "";
							}
						}
					}
					else{
						this.getValidateMessage(mTab, WindowNo , mem_app_status.Draft.value);
					}
					
				//Edit List window
				}else if(ad_window.Membership_Data_Edit_List.value.equals(ad_window_id)){
					if(mem_app_status.Draft.value.equals(app_sts) || mem_app_status.Edit_List_Appoved.value.equals(app_sts)){
						if(mem_app_status.Edit_List_Appoved.value.equals(app_sts)){
							//popupe the confirm message
							if(this.isConfirm(WindowNo)){
								//set visible false of the Member Application status
								//this.setGridFieldVisible(mTab , "c_mem_app_status" , false);
								//set send for approval date time
								this.setSendForAprovalDateTime(mTab, WindowNo);
								mTab.dataSave(true);
								mTab.dataRefreshAll();
							}
						}
					}
					else{
						this.getValidateMessage(mTab, WindowNo,mem_app_status.Send_For_Edit_List_Approval.value);
					}
					
				//Membership Training window
				}else if(ad_window.Membership_Traning_Division.value.equals(ad_window_id)){
					if(mem_app_status.Draft.value.equals(app_sts) || mem_app_status.Training_Approved.value.equals(app_sts)){
						if(mem_app_status.Training_Approved.value.equals(app_sts)){
							//popupe the confirm message
							if(this.isConfirm(WindowNo)){
								//set visible false of the Member Application status
								//this.setGridFieldVisible(mTab , "c_mem_app_status" , false);
								//set send for approval date time
								this.setSendForAprovalDateTime(mTab, WindowNo);
								mTab.dataSave(true);
								mTab.dataRefreshAll();
							}
						}
					}
					else{
						this.getValidateMessage(mTab, WindowNo,mem_app_status.Edit_List_Appoved.value);
					}
					
				//Membership Interview Eligebility
				}else if(ad_window.Membership_Interview_Eligebility.value.equals(ad_window_id)){
					if(mem_app_status.Draft.value.equals(app_sts) || mem_app_status.Interview_Eligibility_Verified.value.equals(app_sts)){
						if(mem_app_status.Interview_Eligibility_Verified.value.equals(app_sts)){
							//popupe the confirm message
							if(this.isConfirm(WindowNo)){
								//set visible false of the Member Application status
								//this.setGridFieldVisible(mTab , "c_mem_app_status" , false);
								//set send for approval date time
								this.setSendForAprovalDateTime(mTab, WindowNo);
								mTab.dataSave(true);
								mTab.dataRefreshAll();
							}
						}
					}
					else{
						this.getValidateMessage(mTab, WindowNo,mem_app_status.Training_Approved.value);
					}
				
				//Membership Interview Eligebility Approved
				}else if(ad_window.Membership_Interview_Approval.value.equals(ad_window_id)){
					if(mem_app_status.Draft.value.equals(app_sts) || mem_app_status.Interview_Eligibility_Approved.value.equals(app_sts)){
						if(mem_app_status.Interview_Eligibility_Approved.value.equals(app_sts)){
							//popupe the confirm message
							if(this.isConfirm(WindowNo)){
								//set visible false of the Member Application status
								//this.setGridFieldVisible(mTab , "c_mem_app_status" , false);
								//set send for approval date time
								this.setSendForAprovalDateTime(mTab, WindowNo);
								mTab.dataSave(true);
								mTab.dataRefreshAll();
							}
						}
					}
					else{
						this.getValidateMessage(mTab, WindowNo,mem_app_status.Interview_Eligibility_Verified.value);
					}
					
				//Send for approval
				}else if(ad_window.Membership_Send_For_Approval.value.equals(ad_window_id)){
					
					if(mem_app_status.Draft.value.equals(app_sts) || mem_app_status.Send_For_Approval.value.equals(app_sts)){
						
						if(mem_app_status.Send_For_Approval.value.equals(app_sts)){
							//popupe the confirm message
							if(this.isConfirm(WindowNo)){
								//set visible false of the Member Application status
								//this.setGridFieldVisible(mTab , "c_mem_app_status" , false);
								//set send for approval date time
								this.setSendForAprovalDateTime(mTab, WindowNo);
								mTab.dataSave(true);
								mTab.dataRefreshAll();
							}
						}
					}
					else{
						this.getValidateMessage(mTab, WindowNo,mem_app_status.Interview_Eligibility_Approved.value);
					}
			
				//Member Approvel window	
				}else if(ad_window.Membership_Approve_Member.value.equals(ad_window_id)){
					if(mem_app_status.Draft.value.equals(app_sts) || mem_app_status.Member_Approved.value.equals(app_sts)){
						if(mem_app_status.Member_Approved.value.equals(app_sts)){
							//popupe the confirm message
							if(this.isMemberApproveConfirm(WindowNo)){
								//set visible false of the Member Application status
								//this.setGridFieldVisible(mTab , "c_mem_app_status" , false);
								//set send for approval date time
								this.setSendForAprovalDateTime(mTab, WindowNo);
								mTab.dataSave(true);
								mTab.dataRefreshAll();
							}
						}
					}
					else{
						this.getValidateMessage(mTab, WindowNo,mem_app_status.Send_For_Approval.value);
					}
				}
			}
			else{
				this.setGridFieldVisible(mTab , "c_mem_app_status" , true);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return "";
	}
	
	//validation message
	private void getValidateMessage(GridTab mTab ,int WindowNo , String appstatus){
		ADialog.error(WindowNo, null, "Invalid Selection! Please select the correct Application Status to proceed!");
		mTab.setValue("c_mem_app_status" ,null);
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
	private boolean isMemberApproveConfirm(int WindowNo){
		
		return ADialog.ask(WindowNo, null,"Are you sure to approve this member ?");
	}
	
	//set send for approvel data time
	private void setSendForAprovalDateTime(GridTab mTab ,int WindowNo){
		
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		mTab.setValue("send_for_aproval_date", ts);
	}
}
