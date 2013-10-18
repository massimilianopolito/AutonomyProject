package utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NavigationMaker {
	private class PageDescriptor{
		private int level = 0;
		private String label = null;
		private String alternativeLink = null;
		
		public PageDescriptor(int level, String label) {
			super();
			this.level = level;
			this.label = label;
		}

		public PageDescriptor(int level, String label, String alternativeLink) {
			super();
			this.level = level;
			this.label = label;
			this.alternativeLink = alternativeLink;
		}

		public int getLevel() {return level;}
		public void setLevel(int level) {this.level = level;}
		
		public String getLabel() {return label;}
		public void setLabel(String label) {this.label = label;}

		public String getAlternativeLink() {return alternativeLink;}
		public void setAlternativeLink(String alternativeLink) {this.alternativeLink = alternativeLink;}
	}

	private static NavigationMaker instance;
	private Map<String, PageDescriptor> pages = new HashMap<String, PageDescriptor>();
	private List<String> cache = new ArrayList<String>();
	private String navigationLabel = "";
	private String context = "";
	private String SEPARATOR = " <span>&gt</span> ";
	private String link = null;
	
	//public void setNavigationLabel(String navigationLabel) {this.navigationLabel = navigationLabel;}

	private NavigationMaker() {
		super();
		//Pagine di I Livello
		pages.put("pages/start.jsp", new PageDescriptor(1, "Home"));

		//Pagine di II Livello
		pages.put("pages/defineReport.jsp", new PageDescriptor(2, "Autonomy"));
		pages.put("pages/realTime/query.jsp", new PageDescriptor(2, "Real Time"));
		pages.put("pages/struttura/chooser.jsp", new PageDescriptor(2, "Report Trainer", "pages/ManageStruttura"));
		pages.put("pages/ontologyTrainer/ontologyTab.jsp", new PageDescriptor(2, "Ontology Trainer"));
		pages.put("pages/social/defineReport.jsp", new PageDescriptor(2, "Social", "pages/defineReport.jsp"));
		
		//Pagine di III Livello
		pages.put("pages/snapShot/view2DMap.jsp", new PageDescriptor(3, "2DMap"));
		pages.put("pages/snapShot/viewSpectro.jsp", new PageDescriptor(3, "Spettrografo"));
		pages.put("pages/tripletta/tripletta.jsp", new PageDescriptor(3, "Tripletta"));
		pages.put("pages/category/categoryTree.jsp", new PageDescriptor(3, "Categorie"));
		pages.put("pages/querySocial/query.jsp", new PageDescriptor(3, "Query"));
		pages.put("pages/struttura/query.jsp", new PageDescriptor(3, "Trainer"));
		pages.put("pages/hotTopics/hotTopicsTree.jsp", new PageDescriptor(3, "Hot Topics"));
		
	}
	
	public static NavigationMaker getInstance(){
		if(instance==null) instance = new NavigationMaker();
		return instance;
	}
	
	private String getPathFromCache(){
		String path = "";
		
		for(int i=0; i<cache.size(); i++){
			String currentPage = cache.get(i);
			PageDescriptor pd = pages.get(currentPage);
			
			if(i==cache.size()-1){
				//path = path + SEPARATOR + pd.getLabel();
				path = path + "<span>" + pd.getLabel() + "</span>";
			}else{
				link = pd.getAlternativeLink()==null?currentPage:pd.getAlternativeLink();
				path = path +  "<a href=\"#\" onclick=\"document.location.href='" + context +  link + "'\">" + pd.getLabel() + "</a>";
			}
		}
		
		if(path.startsWith(SEPARATOR)) path = path.replaceFirst(SEPARATOR, "");
		return path;
	}
	
	public String getPage(){
		String path = "";

		if(cache!=null && !cache.isEmpty()){
			String currentPage = cache.get(cache.size()-1);
			PageDescriptor pd = pages.get(currentPage);
			path=pd.getLabel();
		}
		
		return path;
	}
	
	private int getMaxLevelInCache(){
		int maxLevelInCache = 0;
		if(!cache.isEmpty()){
			String lastPageAdded = cache.get(cache.size()-1);
			PageDescriptor pd = pages.get(lastPageAdded);
			maxLevelInCache = pd.getLevel();
		}
		return maxLevelInCache;
	}
	
	private void addPageToCache(String pageName){
		if(!cache.contains(pageName)) cache.add(pageName);
	}

	private void removePageFromCache(int level){
		if(!cache.isEmpty()){
			Iterator<String> iter = cache.iterator();
			while(iter.hasNext()){
				String currentPage = iter.next();
				PageDescriptor pd = pages.get(currentPage);
				int currentLevel = pd.getLevel();
				if(currentLevel>level){
					cache.remove(currentPage);
					iter = cache.iterator();
				}
			}
		}
	}

	public String getNavigationLabel(String currentPage) {
		if(currentPage.contains("pages")){
			String tokens[] = currentPage.split("pages");
			context = tokens[0];
			String pageName = "pages" + tokens[1];
			PageDescriptor pageDescriptor = pages.get(pageName);

			if(pageDescriptor!=null){
				int currentLevel = pageDescriptor.getLevel();
				int maxLevelInCache = getMaxLevelInCache();
				
				if(currentLevel>=maxLevelInCache){
					addPageToCache(pageName);
				}else if (currentLevel<maxLevelInCache){
					removePageFromCache(currentLevel);
				}
			}
			
			navigationLabel = getPathFromCache();
		}
		return navigationLabel;
	}
	
		
	public String getBack(){
		String back = link;
		if(link == null) back = "pages/start.jsp";
		return "/" + back;
	}

}
