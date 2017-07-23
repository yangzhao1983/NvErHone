package zy.UI;

import zy.dso.DocObject;

public interface IContentPanelControl {

	public void saveContent(DocObject obj);
	
	public void clearContent();
	
	public void setContent(DocObject obj);
}
