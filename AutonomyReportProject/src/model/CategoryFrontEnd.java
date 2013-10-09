package model;

import Autonomy.CategoryTO;


/**
 * @author MassimilianoPolito
 *
 */
public class CategoryFrontEnd extends CategoryTO{
	
/*	private String idCategory;
	private String name;
	private String idParentKey;
*/	private boolean linked = true;
	private String shortName;

	public CategoryFrontEnd() {
		super();
	}
	
	public CategoryFrontEnd(CategoryTO categoryTO) {
		super();
		setId(categoryTO.getId());
		setNome(categoryTO.getNome());
		setParent(categoryTO.getParent());
	}	

	public String getShortName() {
		shortName = getNome();
		if(shortName!=null && shortName.trim().length()>10) shortName = shortName.substring(0,9) + "..."; 
		return shortName;
	}
	public void setShortName(String shortName) {this.shortName = shortName;}

	public boolean isLinked() {return linked;}
	public void setLinked(boolean linked) {this.linked = linked;}
	
}
