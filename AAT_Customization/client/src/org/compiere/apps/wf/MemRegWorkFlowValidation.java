//This class is designed for validate the work flow nodes against the windows
//of the member registration

package org.compiere.apps.wf;

import org.compiere.model.MBPartner;
import org.compiere.wf.MWFActivity;

public class MemRegWorkFlowValidation {
	
	private MWFActivity m_activity;
	
	public MemRegWorkFlowValidation(MWFActivity m_activity){
	
		this.m_activity = m_activity;
	}
	
	enum mem_app_status{
		
		Draft("DR"),Edit_List_Appoved("EA") , Interview_Eligibility_Approved("IA"),Interview_Eligibility_Verified("IV"),
		Member_Approved("MA"),Not_Approved("NA") , Send_For_Approval("SA"),Send_For_Edit_List_Approval("SE"),Training_Approved("TA");
		
		private final String value;
	    private mem_app_status(String value) {
	        this.value = value;
	    }

	    public String getValue() {
	        return value;
	    }
	}
	
	private enum AD_WF_Node{
		
		Approve_Edit_List(100000488) , Approve_Member(100000493), Interview_Eligibility(100000490),
		Interview_Approve(100000491) , Send_For_Approval(100000492), Training_Approval(100000489);
		
		
		private final int value;
	    private AD_WF_Node(int value) {
	        this.value = value;
	    }
    }
	
	public boolean validateMemberRegWorkFlow(){
		
		int node_id  = (int) m_activity.get_Value("AD_WF_Node_ID");
		
		boolean status = false;
		MBPartner mBPartner = MBPartner.get(m_activity.getCtx(), m_activity.getRecord_ID());
		String c_mem_app_status = mBPartner.get_ValueAsString("c_mem_app_status");
		
		try{
			if(AD_WF_Node.Approve_Edit_List.value == node_id){
				
				if(mem_app_status.Edit_List_Appoved.value.equals(c_mem_app_status))
					status = true;
				
			}else if(AD_WF_Node.Training_Approval.value == node_id){
				
				if(mem_app_status.Training_Approved.value.equals(c_mem_app_status))
					status = true;
				
			}else if(AD_WF_Node.Interview_Eligibility.value == node_id){
				
				if(mem_app_status.Interview_Eligibility_Verified.value.equals(c_mem_app_status))
					status = true;
				
				//Interview_Approve
			}else if(AD_WF_Node.Interview_Approve.value == node_id){
				
				if(mem_app_status.Interview_Eligibility_Approved.value.equals(c_mem_app_status))
					status = true;
				
			}else if(AD_WF_Node.Send_For_Approval.value == node_id){
				
				if(mem_app_status.Send_For_Approval.value.equals(c_mem_app_status))
					status = true;
				
			}else if(AD_WF_Node.Approve_Member.value == node_id){
				
				if(mem_app_status.Member_Approved.value.equals(c_mem_app_status))
					status = true;
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return status;
	}
}
