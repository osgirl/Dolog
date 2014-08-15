package org.dolan.callbacks;

import java.util.List;

/**
 * The Interface ISessionCallback. 
 * This is used to get the session ID.
 */
public interface ISessionCallback {
	
	/**
	 * The callback function.
	 *
	 * @param sessionID the session id
	 */
	void call(List<String> sessionID);
}
