package org.compiere.process;
import org.compiere.util.DB;

//org.compiere.process.StudentIdPrintRangeValidation
public class StudentIdPrintRangeValidation extends SvrProcess{

	int from , to;
	
	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("from"))
				from =Integer.parseInt(para[i].getParameter().toString());
			else if (name.equals("to"))
				to =Integer.parseInt(para[i].getParameter().toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected String doIt() throws Exception {
		
		String sql = "update c_bpartner set is_id_printed = 'Y' where " + "c_student_id between '"+this.from+"' and '"+this.to+"';";
		DB.executeUpdate(sql);		
		return "";
	}
}
