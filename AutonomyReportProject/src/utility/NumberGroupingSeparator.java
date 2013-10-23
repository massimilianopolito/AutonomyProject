package utility;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberGroupingSeparator {

	public static String formatNumber(Object number){
		try{
			if(number!=null){
				NumberFormat nf = NumberFormat.getNumberInstance(Locale.ITALIAN);
				DecimalFormat df = (DecimalFormat)nf;
				number = df.format(Long.valueOf(String.valueOf(number)));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return (number!=null)?number.toString():"";
	}
	
	
	public static String percentValue(long totDoc, long numDoc){
		String percent = "";
		try{
			Double numeratore = new Double(numDoc);
			Double denominatore = new Double(totDoc);
			double value = (100D*numeratore)/denominatore;
			NumberFormat nf = NumberFormat.getNumberInstance(Locale.ITALIAN);
			DecimalFormat df = (DecimalFormat)nf;
			df.setMaximumFractionDigits(2);
			percent = df.format(value);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return percent+"%";
	}
	
/*	public static void main(String[] args) {
		System.out.println(NumberGroupingSeparator.formatNumber(23400));

	}
 */
}
