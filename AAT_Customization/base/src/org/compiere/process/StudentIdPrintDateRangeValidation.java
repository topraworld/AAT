package org.compiere.process;
import org.compiere.util.DB;

//org.compiere.process.StudentIdPrintDateRangeValidation
public class StudentIdPrintDateRangeValidation extends SvrProcess{

	String from , to;
	
	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("from"))
				from = para[i].getParameter().toString(); 
			else if (name.equals("to"))
				to = para[i].getParameter().toString();
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected String doIt() throws Exception {
		
		String sql = "update c_bpartner set is_id_printed = 'Y' where " + "registered_date between '"+this.from+"' and '"+this.to+"';";
		DB.executeUpdate(sql);		
		return "";
	}
}
