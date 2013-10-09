package utility;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.autonomy.utilities.DateUtils;


public class DateConverter {
	public static String PATTERN_DB = "ddMMyyyy_HHmmss";
	public static String PATTERN_VIEW = "dd/MM/yyyy";
	public static String PATTERN_GENERAL = "dd/MM/yyyy HH:mm:ss";
	public static String PATTERN_ESTESO = "EEE MMM dd yyyy HH:mm:ss z";
	
	public static String getDate(String autonomyDate){
		String humanDate = autonomyDate;
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
			humanDate = DateUtils.formatEpochSeconds(autonomyDate, dateFormat.toPattern());
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return humanDate;
	}
	
	public static String getDate(Timestamp date, String pattern){
		String formattedDate = null;
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.ITALY);
			formattedDate = dateFormat.format(new Date(date.getTime()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return formattedDate;
	}

	public static Timestamp getDate(String date, String pattern){
		Calendar cal = Calendar.getInstance();
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.ITALY);
			cal.setTime(dateFormat.parse(date));;
		}catch(ParseException e){
			e.printStackTrace();
			System.err.println("Index: " + e.getErrorOffset());  
		}
		return new Timestamp(cal.getTimeInMillis());
	}

}
