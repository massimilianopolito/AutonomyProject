package utility;

import java.util.Collection;

public class AppConstants {

	public static String HT_TABLE_CLUSTER = "HT_Cluster";
	public static String HT_TABLE_DOC = "HT_Document";
	public static String[] ambitoLabel = {"Corporate","Consumer"};
	public static String[] tipoTicketLabel = {"Fisso","Mobile"};
	public static String[] categoriaTicketLabel = {"Case","Interazioni"};
	public static String[] classeReportLabel = {"Real Time","Autonomy", "Struttura", "Social","Ontology Trainer"};
	public static String[] tipoRappresentazionelabel = {"2DMap","Spettrografo", "Categorie", "Tripletta", "Query"};
	public static String[] clusteringRoot = {"Case Fisso", "Interazioni Fisso", "Case Mobile", "Interazioni Mobile", "Case", "Interazioni"};
	//public static String[] amministrazionelabel = {"Gestione triplette"};
	public static String REFERENCE_DOPPIOAPICE= "[D.A.]";
	
	public static boolean isSingleChoice(Collection<String> list){
		return  list!=null && list.size() == 1;
	}

	public static boolean isDisabled(Collection<String> list){
		return  list==null || list.isEmpty();
	}
	
	public static String getLabelFromIndex(String[] labels, String index){
		String lbl = "";
		if(index!=null && index.trim().length()!=0){
			lbl = labels[Integer.parseInt(index)];
		}
		return lbl;
	}
	
	public static class Tripletta{
		public static String TROIKA_FIELD_MANAGE_D = "0";
		public static String TROIKA_FIELD_MANAGE_R = "1";
		public static String TROIKA_INFORMAZIONI = "INFORMAZIONI";
		public static String TROIKA_ASSISTENZA = "ASSISTENZA";
		
	}
	
	public static class Rappresentazione{
		public static String DMAP = "0";
		public static String SPECTRO = "1";
		public static String CATEGORY = "2";
		public static String TRIPLETTA = "3";
		public static String QUERYSOCIAL = "4";
		public static String HOTTOPICS = "5";

		public static String DMAP_DOMANDE = "0.1";
		public static String DMAP_RISPOSTE = "0.2";
		public static String SPECTRO_DOMANDE = "1.1";
		public static String SPECTRO_RISPOSTE = "1.2";
		public static String CATEGORY_DOMANDE = "2.1";
		public static String CATEGORY_RISPOSTE = "2.2";
	}

	public static class NomiCombo{
		public static String AREA = "area";
		public static String REPORT = "report";
		public static String TICKET = "ticket";
		public static String RETE = "rete";
	}

	//real time, autonomy, struttura
	public static class ClasseReport{
		public static String REAL_TIME = "0";
		public static String AUTONOMY = "1";
		public static String STRUTTURA = "2";
		public static String SOCIAL = "3";
		public static String ONTOLOGYTRAINER = "4";
	}

	public static class Ambito{
		public static String CORPORATE = "0";
		public static String CONSUMER = "1";
	}

	public static class TipoTicket{
		public static String FISSO = "0";
		public static String MOBILE = "1";
	}

	public static class categoriaTicket{
		public static String CASE = "0";
		public static String INTERAZIONI = "1";
	}

	public static class Struttura{
		public static String QUERY = "0";
		public static String CONSOLE = "1";
	}

	public static class tipoDiCampo{
		public static String STRING = "String";
		public static String CALENDAR = "Calendar";
		public static String COMBO = "Combo";
		public static String TRIPLETTA = "Tripletta";
	}

	public static class thread{
		public static int HOT_TOPICS = 0;
		public static int STRUTTURA = 1;
		public static int SNAPSHOT =2;
		
		public static int STOPPED = 0;
		public static int RUNNING = 1;
		public static int FAILED = 2;
	}

	public static class Amministrazione{
		public static String GESTIONETRIPLETTE = "0";
	}
	
	public static int getQueryNum(String tipoTicket, String categoriaTicket){
		int val = -1;
		String coppia = tipoTicket+categoriaTicket;
		if("00".equals(coppia)) val=1;
		if("01".equals(coppia)) val=2;
		if("10".equals(coppia)) val=3;
		if("11".equals(coppia)) val=4;
		if(tipoTicket==null){
			coppia = categoriaTicket;
			if(coppia.equals("0")) val = 5 ;
			if(coppia.equals("1")) val = 6 ;
		}
		return val;
	}
	
	public static String getNomiThread(int i){
		String name = null;
		switch (i) {
		case 0:
			name="hotTopicsThread";
			break;
		case 1:
			name="xmlSenderThread";
			break;
		case 2:
			name="snapShotThread";
			break;
		}
		return name;
	}

}
