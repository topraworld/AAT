package org.compiere.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.compiere.model.MBPartner;
import org.compiere.model.MImage;
import org.compiere.util.Trx;

//org.compiere.process.ImageUpload
public class ImageUpload extends SvrProcess{
	
	private String from , to;
	private int success , fails;
	
	
	@Override
	protected void prepare() {
		
		for (ProcessInfoParameter para : getParameter())
		{
			String name = para.getParameterName();
			if (name.equalsIgnoreCase("Source"))
				from = para.getParameterAsString();
			else if (name.equalsIgnoreCase("Destination"))
				to =  para.getParameterAsString();
		}
	}

	@Override
	protected String doIt() throws Exception {
		
		File[] files = new File(this.from).listFiles();
		String message = "<h4>Number of Images - "+files.length+"</h4><br>";
		
		try{
			for (File file : files) {
				
			    if (file.isFile()) {
			    	//validate the student only for already there
			    	String where = "c_temp_no = '"+file.getName().substring(0, file.getName().lastIndexOf("."))+"'";
					int bpas [] = MBPartner.getAllIDs(MBPartner.Table_Name, where, get_TrxName());
					
					if(bpas.length > 0){
						message += file.getName()+"<br>";
				    	File source = new File(from + "/" +file.getName());
				    	File dest = new File(to + "/" +file.getName());
				    	
				    	FileUtils.copyFile(source, dest);
				    	this.setImage(file , bpas[0]);
				    	FileUtils.forceDelete(file);
				    	success ++;
					}
			    }
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		message+="</br> Succeeded : " + success;
		message+="</br> Failed : " + (files.length - success);
		
		return message;
	}
	
	private void setImage(File image , int bpartner_id) throws IOException{
		
		MBPartner bpMbPartner = MBPartner.get(getCtx(), bpartner_id);
		Trx trx = trx = Trx.get(Trx.createTrxName("FWFA"), true);
		
		MImage mImage = new MImage(getCtx(), 0, get_TrxName());
		mImage.setBinaryData(getbyte(image));
		
		mImage.setName(to+"\\"+image.getName());
		mImage.setImageURL(to+"\\"+image.getName());
		
		mImage.saveEx(trx.getTrxName());
		trx.commit();
		trx.close();
		
		bpMbPartner.setLogo_ID(mImage.get_ID());
		bpMbPartner.saveEx();
	}
	
	private byte[] getbyte(File image)  throws IOException{
		
		byte[] fileData = new byte[(int) image.length()];
		  
		FileInputStream in = new FileInputStream(image);
		in.read(fileData);
		in.close();
		return fileData;
	}
}
