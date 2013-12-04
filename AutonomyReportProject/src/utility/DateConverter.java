package utility;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.autonomy.utilities.DateUtils;


public class DateConverter {
	private Logger logger = ReportLogger.getLog("general");
	public static String PATTERN_DB = "ddMMyyyy_HHmmss";
	public static String PATTERN_DB_TIMESTAMP = "yyyy-MM-dd HH:mm:ss";
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
	
	public static Timestamp getDateRolled(long timeMillis, int amountRoll){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeMillis);
		calendar.add(Calendar.DATE, amountRoll);
		return new Timestamp(calendar.getTimeInMillis());
	}

	public static String getAutonomyDate(String humandate){
		String date = humandate;
		try{
			Date humandateD = new Date(getDate(humandate, PATTERN_VIEW).getTime());
			date = DateUtils.toEpochSeconds(humandateD);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return date;
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

	public static Timestamp getDate(String date, String patternTo, String patternFrom){
		Calendar cal = Calendar.getInstance();
		try{
			SimpleDateFormat dateFormatFrom = new SimpleDateFormat(patternFrom, Locale.ITALY);
			dateFormatFrom.setLenient(false);
			Date dateFrom = dateFormatFrom.parse(date);
			
			SimpleDateFormat dateFormatTo = new SimpleDateFormat(patternTo, Locale.ITALY);
			dateFormatTo.setLenient(false);
			cal.setTime(dateFormatTo.parse(dateFormatTo.format(dateFrom)));;
		}catch(ParseException e){
			e.printStackTrace();
			System.err.println("Index: " + e.getErrorOffset());  
		}
		return new Timestamp(cal.getTimeInMillis());
	}

	public static List<String> getDates(String from, String to, String gap){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		List<String> dates = new ArrayList<String>();
		try{
			Calendar start = Calendar.getInstance();
			Calendar end = Calendar.getInstance();
			
			if(to!=null && !to.isEmpty()){
				end.setTime(sdf.parse(to));
			}

			if(from!=null && !from.isEmpty()){
				start.setTime(sdf.parse(from));
			}else{
				start.setTime(end.getTime());
				if(gap==null || gap.isEmpty()){
					String months = PropertiesManager.getMyProperty("penthao.thread.month");
					int monthInt = 3;
					try{
						monthInt = Integer.parseInt(months);
					}catch(Exception e){
						
					}
					start.add(Calendar.MONTH, -monthInt);
				}else{
					int gapInt = Integer.parseInt(gap);
					start.add(Calendar.DATE, -gapInt);
				}
			}
			
			start.set(Calendar.HOUR_OF_DAY, 0);
			start.set(Calendar.MINUTE, 0);
			start.set(Calendar.SECOND, 0);
			start.set(Calendar.MILLISECOND, 0);
			 
			end.set(Calendar.HOUR_OF_DAY, 0);
			end.set(Calendar.MINUTE, 0);
			end.set(Calendar.SECOND, 0);
			end.set(Calendar.MILLISECOND, 0);
			
			//Splitto l'intervallo di date nei singoli giorni che lo compongono.
			dates.add(sdf.format(start.getTime()));
			while(start.before(end)){
				start.add(Calendar.DATE, 1);
				dates.add(sdf.format(start.getTime()));
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return dates;
	}

	public static void main(String[] args) {
		System.err.println(DateConverter.getDate("1384119900"));
		System.err.println(DateConverter.getAutonomyDate("10/11/2013"));

	}

}
