package org.compiere.apps.wf;

import org.compiere.model.MBPartner;
import org.compiere.wf.MWFActivity;

public class MemUpgradeWrkFlwValidation {
	
	private WFActivity wf_activity;
	private MWFActivity m_activity;
	
	static int cpd_Approval;

	static int cpd_Verification;

	static int interview_Remarks;

	static int upgrade_Approval;
	
	public MemUpgradeWrkFlwValidation(WFActivity wf_activity){
		
		this.wf_activity = wf_activity;
		this.m_activity = wf_activity.getMActivity();
		int ad_workflow_id = wf_activity.getMActivity().getAD_Workflow_ID();
		
		if(ad_workflow_id == ad_workflow.MAAT_SAT.value){
			
			cpd_Verification = 100000499;
			cpd_Approval = 100000498;
			interview_Remarks = 100000500;
			upgrade_Approval = 100000501;
			
		}else if(ad_workflow_id == ad_workflow.MAAT_SAT_Direct.value){
			
			cpd_Verification = 100000506;
			cpd_Approval = 100000507;
			interview_Remarks = 100000508;
			upgrade_Approval = 100000509;
			
		}else if(ad_workflow_id == ad_workflow.SAT_FMAAT_Direct.value){
			
			cpd_Verification = 100000510;
			cpd_Approval = 100000511;
			interview_Remarks = 100000512;
			upgrade_Approval = 100000513;
			
		}else if(ad_workflow_id == ad_workflow.SAT_FMAAT.value){
			
			cpd_Verification = 100000505;
			cpd_Approval = 100000502;
			interview_Remarks = 100000503;
			upgrade_Approval = 100000504;
			
		}else if(ad_workflow_id == ad_workflow.MAAT_FMAAT_Direct.value){
			
			cpd_Verification = 100000495;
			cpd_Approval = 100000494;
			interview_Remarks = 100000496;
			upgrade_Approval = 100000497;
		}
	}

	enum ad_workflow{
		
		MAAT_SAT(100000486),MAAT_SAT_Direct(100000488) , SAT_FMAAT(100000487),
		SAT_FMAAT_Direct(100000489), MAAT_FMAAT_Direct(100000487);
		
		private final int value;
	    private ad_workflow(int value) {
	        this.value = value;
	    }

	    public int getValue() {
	        return value;
	    }
	}
	
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
	
	//workflow MAAT to SAT
	private enum ad_wf_node{
		
		CPD_Approval(cpd_Approval) , CPD_Verification(cpd_Verification),
		Interview_Remarks_Send_for_Approval(interview_Remarks),Member_Upgrade_Approval(upgrade_Approval);
		
		private final int value;
	    private ad_wf_node(int value) {
	        this.value = value;
	    }	    
	}
	
	public boolean validateMemUpgrdValidation(){
		
		boolean status = false;
		int node_id =  this.m_activity.get_ValueAsInt("AD_WF_Node_ID");
		MBPartner mBPartner = MBPartner.get(m_activity.getCtx(), m_activity.getRecord_ID());
		String app_status = mBPartner.get_ValueAsString("c_mem_upgrade_status");
		
		try{
			
			if(ad_wf_node.CPD_Verification.value == node_id){
				
				System.out.println(app_status);
				if(mem_upgrade_app_sts.CPD_Work_Experience_Verified.value.equals(app_status)){
					status = true;
				}	
			}else if(ad_wf_node.CPD_Approval.value == node_id){
				
				if(mem_upgrade_app_sts.CPD_Work_Experience_Approved.value.equals(app_status)){
					status = true;
				}
			}else if(ad_wf_node.Interview_Remarks_Send_for_Approval.value == node_id){
				
				if(mem_upgrade_app_sts.Send_for_Approval.value.equals(app_status)){
					status = true;
				}
			}else if(ad_wf_node.Member_Upgrade_Approval.value == node_id){
				
				if(mem_upgrade_app_sts.Approved.value.equals(app_status)){
					status = true;
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return status;
	}
}
