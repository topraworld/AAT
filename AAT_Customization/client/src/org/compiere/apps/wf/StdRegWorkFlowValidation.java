package org.compiere.apps.wf;

import org.compiere.model.MBPartner;
import org.compiere.wf.MWFActivity;

public class StdRegWorkFlowValidation {
	
	private MWFActivity m_activity;
	
	public StdRegWorkFlowValidation(MWFActivity m_activity){
		
		this.m_activity = m_activity;
	}
	
	private enum c_app_status{
		
		Send_For_Approval("SA"), Pending_Results("PR"), Not_Approved("NA"),
		Edit_List_Approved("EA"),Approved("AP") , Draft("DR");
		
		private final String value;
	    private c_app_status(String value) {
	        this.value = value;
	    }
	}
	
	private enum AD_WF_Node{
		
		Verify_Student("100000487") , Edit_List_Approval("100000486") , ID_Printing("100000514");
		
		private final String value;
	    private AD_WF_Node(String value) {
	        this.value = value;
	    }

	    @SuppressWarnings("unused")
		public String getValue() {
	        return value;
	    }	
	}
	
	public boolean validateStudentRegWorkFlow(){
		
		boolean status = false;
		try{
			
			String node_id =  m_activity.get_Value("AD_WF_Node_ID").toString();
			MBPartner mBPartner = MBPartner.get(m_activity.getCtx(), m_activity.getRecord_ID());
			String app_status = mBPartner.get_ValueAsString("c_app_status");
			
			if(AD_WF_Node.Edit_List_Approval.value.equals(node_id)){
				
				if(c_app_status.Edit_List_Approved.value.equals(app_status))
					status = true;
			}else if(AD_WF_Node.Verify_Student.value.equals(node_id)){
				
				if(c_app_status.Approved.value.equals(app_status))
					status = true;
			}else if(AD_WF_Node.ID_Printing.value.equals(node_id)){
				
				status = true;
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return status;
	}	
}
