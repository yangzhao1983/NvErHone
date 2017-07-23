package zy.UI.Logic;


/**
 * This interface will used by Action/Listener to do the tasked invoked by the
 * event.
 * 
 * 
 * @author yangzhao
 * 
 */
public interface ILogic {

	/**
	 * Navigate to next frame
	 * 
	 * @author yangzhao
	 * 
	 */
	public void navigate2Next();
	
	/**
	 * Navigate to before frame
	 * 
	 */
	public void navigate2Before();

	/**
	 * Store the information
	 * 
	 */
	public void storeInfo();
	
	/**
	 * Clear the input info of the UI
	 * 
	 */
	public void clearUIInput();
	
	/**
	 * Set UI Info
	 */
	public void setUIInfo();
}
