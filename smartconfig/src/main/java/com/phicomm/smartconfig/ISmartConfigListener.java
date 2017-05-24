package com.phicomm.smartconfig;

public interface ISmartConfigListener {
	/**
	 * when new com.phicomm.esptouch result is added, the listener will call
	 * onEsptouchResultAdded callback
	 * 
	 * @param result
	 *            the Esptouch result
	 */
	void onEsptouchResultAdded(ISmartConfigResult result);
}
