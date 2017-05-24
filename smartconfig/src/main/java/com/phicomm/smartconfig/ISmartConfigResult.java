package com.phicomm.smartconfig;

import java.net.InetAddress;

public interface ISmartConfigResult {
	
	/**
	 * check whether the com.phicomm.esptouch task is executed suc
	 * 
	 * @return whether the com.phicomm.esptouch task is executed suc
	 */
	boolean isSuc();

	/**
	 * get the device's bssid
	 * 
	 * @return the device's bssid
	 */
	String getBssid();

	/**
	 * check whether the com.phicomm.esptouch task is cancelled by user
	 * 
	 * @return whether the com.phicomm.esptouch task is cancelled by user
	 */
	boolean isCancelled();

	/**
	 * get the ip address of the device
	 * 
	 * @return the ip device of the device
	 */
	InetAddress getInetAddress();
}
