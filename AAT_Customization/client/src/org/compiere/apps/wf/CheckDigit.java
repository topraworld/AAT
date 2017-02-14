package org.compiere.apps.wf;

//Topra customization
//Created by Chathuranga
//To generated the Student and Member Check number 
public class CheckDigit {
	
	public static int getCheckDigit(String currentnext){
		
		 int val = 0;
		 int sum=0;
		 int checkdigit = 0;
		 
		if(currentnext!=null){
			
			for(int i = 0; i < currentnext.length();i++){
				
				val = Character.getNumericValue(currentnext.charAt(i));
				if(i%2 == 0){
					//multiply by one
					sum += val * 1;
				}
				else{
					//multiply by three
					sum += val *3;
				}
			}
			
			if(sum % 10 == 0){
				
				checkdigit = sum - sum;
			}
			else{
				checkdigit = (((sum /10) + 1) * 10) - sum;
			}
		}
		return checkdigit;
	}
	
}
