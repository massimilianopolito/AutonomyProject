package thread;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import utility.AppConstants;
import utility.HotTopicsManageTable;
import utility.PropertiesManager;
import Autonomy.D2Map;

import com.autonomy.aci.AciResponse;

public class HotTopicsQueryMaker extends AbstractThread {
			
	private AciResponse executeQuery(int i) throws Exception{
		AciResponse aciResponse = null;
		D2Map d2Map = new D2Map();
		if(!"max".equalsIgnoreCase(PropertiesManager.getMyProperty("environment"))){
			switch (i) {
			case 1:
				aciResponse = d2Map.HotTopicsCaseFisso();
				break;
			case 2:
				aciResponse = d2Map.HotTopicsIntFisso();
				break;
			case 3:
				aciResponse = d2Map.HotTopicsCaseMobile();
				break;
			case 4:
				aciResponse = d2Map.HotTopicsIntMobile();
				break;
			}
		}else{
			String nome = AppConstants.getLabelFromIndex(AppConstants.clusteringRoot, (i-1)+"");
			aciResponse = new AciResponse(nome);
		}
		
		return aciResponse;
	}
	
	private void queryMaker() throws Exception{

		HotTopicsManageTable hotTopicsManageTable = new HotTopicsManageTable();
		boolean esito = hotTopicsManageTable.tableMaker();
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MILLISECOND, 0);
		Timestamp dataElaborazione = new Timestamp(c.getTimeInMillis());

		for(int i=1; i<=4; i++){
			System.out.println("esito: " + esito);
			AciResponse currentResponse = executeQuery(i);
			System.out.println("Ok esecuzione query");
			hotTopicsManageTable.saveAciResponse(i, currentResponse, dataElaborazione);
			System.out.println("Ok salvataggio query");
			if(esito) hotTopicsManageTable.makeData(i, currentResponse, dataElaborazione);
			
			//pausa di lancio
			makePausa();
		}
		
		hotTopicsManageTable.deleteOlderAciResponse(dataElaborazione);
		
		hotTopicsManageTable.clear(dataElaborazione);
		System.err.println("-------------- PROCESSO TERMINATO --------------");
	}

	private void makePausa(){
		long pausa = 600*1000L;//Default 10min = 600.000ms
		try{
			String timeForConfig = PropertiesManager.getMyProperty("hotTopics.thread.pausa");
			if(timeForConfig!=null && timeForConfig.trim().length()>0){
				pausa = Long.parseLong(timeForConfig) * 1000L;
			}

			Thread.sleep(pausa);

		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void run() {
		System.out.println("START: " + this.getClass().getName());
		try{
			Calendar currentDate = GregorianCalendar.getInstance();
			Calendar compareDate = GregorianCalendar.getInstance();

			while(true && isAlive()){
				String hours = PropertiesManager.getMyProperty("hotTopics.thread.oraEsecuzioni");
				if("99:99".equalsIgnoreCase(hours)){
					queryMaker();
					break;
				}
				String[] executionHours = hours.split("\\|");
				for(int i=0; i<executionHours.length; i++){
					String currentHour = executionHours[i];
					String[] tokens = currentHour.split("\\:");
					int hour = Integer.parseInt(tokens[0].trim());
					int min = Integer.parseInt(tokens[1].trim());

					currentDate.setTimeInMillis(System.currentTimeMillis());
					compareDate.set(compareDate.get(Calendar.YEAR), compareDate.get(Calendar.MONTH), compareDate.get(Calendar.DATE), hour, min);

					//System.out.println("-------------------------------------------------");
					//System.out.println("HotTopicsQueryMaker currentDate.getTime(): " + currentDate.getTime());
					//System.out.println("HotTopicsQueryMaker compareDate.getTime(): " + compareDate.getTime());
					
					long sleep = compareDate.getTimeInMillis() - currentDate.getTimeInMillis();
					if(sleep>=0){
						try{
							Thread.sleep(sleep);
						}catch (Exception e) {
							// TODO: handle exception
						}
						
						queryMaker();
					}
					
					if(i==executionHours.length-1){
						compareDate.roll(Calendar.DATE, 1);
					}
				}
				
			}

		}catch (Exception e) {
			e.printStackTrace();
			stopOnException(e.getMessage());
		}

	}

	@Override
	protected int getThreadType() {
		return AppConstants.thread.HOT_TOPICS;
	}

}
