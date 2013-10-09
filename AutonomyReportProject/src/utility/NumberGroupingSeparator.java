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
	
/*	public static void main(String[] args) {
		System.out.println(NumberGroupingSeparator.formatNumber(23400));

	}
 */
}
