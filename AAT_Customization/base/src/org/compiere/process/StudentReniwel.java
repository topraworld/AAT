package org.compiere.process;

import java.math.BigDecimal;
import java.util.Properties;

import org.compiere.apps.ADialog;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.GridTable;
import org.compiere.model.MBPartner;
import org.compiere.model.MInvoice;
import org.compiere.model.MProduct;

public class StudentReniwel extends CalloutEngine{

	//Created by chathuranga - Topra (Pvt) Ltd
	//Date - 2016/06/07
	//AAT Customization
	
	public String getLastRenYear_StudentID (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value!=null && mTab.getValue("C_DocTypeTarget_ID") != null){
			try{
				int c_bpartner_id =(mTab.getValue("c_bpartner_id")) == null ? 0:(Integer)mTab.getValue("c_bpartner_id");
				MBPartner bpartner = MBPartner.get(ctx,c_bpartner_id);
				
				//clear existing data
				mTab.setValue("last_renewed_year", "");
				mTab.setValue("c_student_id", "");
				
				//Membership reniewel target document id > C_DocTypeTarget_ID=1000106()
				if(mTab.getValue("C_DocTypeTarget_ID").toString().equals("1000106")){
					if(bpartner.get_Value("mem_last_renew_year")!=null){
						
						//validate for life membership
						if(bpartner.get_Value("member_type").toString().equals("om"))
						{
							//validate for already opened invoices
							if(this.validateStdRegOpenInvoices(ctx, WindowNo, mTab, mField, value))
							{
								mTab.setValue("last_renewed_year", bpartner.get_Value("mem_last_renew_year").toString());
								mTab.setValue("c_student_id", bpartner.get_Value("value").toString());
								
								mTab.setValue("c_member_id", bpartner.get_Value("c_member_id").toString());
								mTab.setValue("M_PriceList_ID", 100000485);
								mTab.setValue("ad_org_id", 1000018);
								mTab.setValue("logo_id", bpartner.getLogo_ID());
								mTab.dataSave(true);
								mTab.dataRefresh();
							}
						}else{
							
							ADialog.error(WindowNo, null, "Reniewals are not allowed to LIFE MEMBERS!");
						}
					}
				//Student reniewel target document id > 	
				}else if(mTab.getValue("C_DocTypeTarget_ID").toString().equals("1000105")){
					
					if(bpartner.get_Value("last_renewed_year")!=null && bpartner.get_Value("c_student_id")!=null){
						//validate for already opened invoices
						if(this.validateStdRegOpenInvoices(ctx, WindowNo, mTab, mField, value))
						{
							//this commented line is substituted two colum sql  
							mTab.setValue("last_renewed_year", bpartner.get_Value("last_renewed_year").toString());
							
							//setting the ad_org_id asStudent registration and price list as student reniewel
							mTab.setValue("c_student_id", bpartner.get_Value("c_student_id").toString());
							mTab.setValue("M_PriceList_ID", 100000483);
							mTab.setValue("ad_org_id", 1000016);
							
							mTab.dataSave(true);
							mTab.dataRefresh();
						}
						
					}else{
						ADialog.error(WindowNo, null, "Problem with Student last renewed year! Your are not allowed to make reniewel for this student.");
					}
				}
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return "";	
	}
	
	//org.compiere.process.StudentReniwel.validateCurrentYearReniewel
	public void validateCurrentYearReniewel(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		try{
			if(value!=null){
				GridTab pmTab = mTab.getParentTab();
				//validate for document type for student renievel
				if(pmTab.getValue("C_DocTypeTarget_ID").toString().equals("1000105")){
					//validate last reniewed not null
					String lry = (pmTab.get_ValueAsString("last_renewed_year")) == null?"":pmTab.get_ValueAsString("last_renewed_year");
					if(!lry.equals("")){
						//get user entered product validate for current year
						int current_year = Integer.parseInt(lry);
						MProduct mProduct= MProduct.get(ctx,Integer.parseInt(mTab.getValue("m_product_id").toString()));
						int search_key = Integer.parseInt(mProduct.getValue().substring(2));//to get the product year from search key
						
						if(search_key > current_year){
							this.validateDuplicateLine(ctx , WindowNo, mTab,mField , mProduct , current_year);
							//this.validateRenewOrder(ctx,WindowNo ,mTab , mField,mProduct);
							return;
						}
						else{
							mTab.dataIgnore();
							ADialog.error(WindowNo, null, "Invalid selection - Renewal year!");
						}
						
					}else{
						mTab.dataIgnore();
						ADialog.error(WindowNo, null, "Problem with Student last Renewad year! Your are not allowed to make reniewel for this student.");
					}
				//validate for document type for membership renievel
				} else if(pmTab.getValue("C_DocTypeTarget_ID").toString().equals("1000106")){
					
					//validate last reniewed not null
					String lry = (pmTab.get_ValueAsString("last_renewed_year")) == null?"":pmTab.get_ValueAsString("last_renewed_year");
					if(!lry.equals("")){
						//get user entered product validate for current year
						int current_year = Integer.parseInt(lry);
						MProduct mProduct= MProduct.get(ctx,Integer.parseInt(mTab.getValue("m_product_id").toString()));
						int search_key = Integer.parseInt(mProduct.getValue().substring(2));//to get the product year from search key
						
						if(search_key > current_year){
							this.validateDuplicateLine(ctx , WindowNo, mTab,mField , mProduct , current_year);
							//this.validateRenewOrder(ctx,WindowNo ,mTab , mField,mProduct);
							return;
						}
						else{
							mTab.dataIgnore();
							ADialog.error(WindowNo, null, "Invalid selection - Renewal year!");
						}
						
					}else{
						mTab.dataIgnore();
						ADialog.error(WindowNo, null, "Problem with Member last Renewad year! Your are not allowed to make reniewel for this member.");
					}
				}
				else{
					return;
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	//validate for invoice line same product
	//only for service category products
	private void validateDuplicateLine(Properties ctx, int WindowNo, GridTab mTab, GridField mField ,MProduct mProduct , int lry){
		
		try{
			//get the order of reniewel ex : 2015,2016,2017
			if(validateRenewOrder(ctx,WindowNo ,mTab , mField, mProduct , lry)){
				
				int rowCount = mTab.getRowCount();
				if(rowCount > 1){
					GridTable gt = mTab.getTableModel();
					String lastAdded = mTab.getValue("m_product_id").toString();
					String existing = "";
					
					for(int i = 0; i < (rowCount-1) ;i++){
						existing = gt.getValueAt(i, 0).toString();
						if(lastAdded.equals(existing)){
							//get order of reniewel
							mTab.dataIgnore();
							ADialog.error(WindowNo, null, "Duplicate Renewal year!.");
						}
						else{
							
						}
					}	
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private boolean validateRenewOrder(Properties ctx, int WindowNo, GridTab mTab, GridField mField , MProduct mProduct , int lry){
		boolean status = false;
		try{
			
			int rowCount = (mTab.getRowCount())-1;
			if(rowCount == 0){
				int lastAdded = Integer.parseInt(mProduct.get_ValueAsString("value").substring(2));
				//first adding product  validation for current reniwel year +1
				lastAdded = lastAdded - 1;
				if(!(lastAdded== lry)){
					mTab.dataIgnore();
					ADialog.error(WindowNo, null, "Invalid selection - Renewal year!");
				}
				else{
					status = true;
					return status; 
				}
			}
			else{
				//adding more than one year renievels
				GridTable gt = mTab.getTableModel();
				int lastAdded =Integer.parseInt(gt.getValueAt(rowCount - 1, 0).toString());
				MProduct last_mProduct= MProduct.get(ctx,lastAdded);
				int last_added_search_key = Integer.parseInt(last_mProduct.getValue().substring(2));//to get the product year from search key
				int newAdded = Integer.parseInt(mProduct.get_ValueAsString("value").substring(2));
				
				newAdded--;
				
				if(!(last_added_search_key == newAdded)){
					mTab.dataIgnore();
					ADialog.error(WindowNo, null, "Invalid selection - Reneval year!");
				}
				else{
					status = true;
					return status; 
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return status;
	}

	//for both student and member reniewel
	//org.compiere.process.StudentReniwel.validateLessExcess
	public String validateLessExcess (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		try{
			if(value!=null){
				
				String c_doctypetarget_id = mTab.getParentTab().getValue("c_doctypetarget_id").toString();
				String str_priceactual = mTab.getValue("priceactual").toString() == ""?"0.00":mTab.getValue("priceactual").toString();
				String str_pricelist = mTab.getValue("pricelist").toString() == ""?"0.00":mTab.getValue("pricelist").toString();
				
				if((c_doctypetarget_id.equals("1000105") || c_doctypetarget_id.equals("1000106")) && !str_priceactual.equals(str_pricelist))
				{
					mTab.setValue("c_price_balance", new BigDecimal(str_priceactual).subtract(new BigDecimal(str_pricelist)));
				}
			}
			
		}catch(Exception ex){}
		
		return "";
	}
	
	private Boolean validateStdRegOpenInvoices(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		
		Boolean status = false;
		
		String c_doctypetarget_id = mTab.getValue("c_doctypetarget_id").toString();;
		String c_bpartner_id = mTab.getValue("c_bpartner_id").toString();
		
		String sqlwhere = " c_doctypetarget_id = "+ c_doctypetarget_id +" AND " + " c_bpartner_id = " + c_bpartner_id;
		sqlwhere += " AND ISACTIVE = 'Y' AND docstatus='DR' AND ad_client_id = " + mTab.getValue("ad_client_id").toString();
		
		//System.out.println(sqlwhere);
		
		int[] i = MInvoice.getAllIDs("c_invoice", sqlwhere, mTab.getTrxInfo());
		
		if(i.length > 0){
			status = false;
			ADialog.error(WindowNo, null, "Can not enter a new renewal.Reniewal Invoice is already in the system with Draft mode!");
			mTab.setValue("c_bpartner_id", null);
		}
		else
			status = true;
		
		return status;
	}
	
	//org.compiere.process.StudentReniwel.concession
	public String concession (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value!=null){
			
			String is_concession =  mTab.getValue("is_concession").toString();
			if(is_concession.equals("true")){
				mTab.setValue("c_price_balance", new BigDecimal("0.00"));
			}
		}
		return "";
	}
}

