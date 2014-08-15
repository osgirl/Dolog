package org.dolan.datastructures;

import static org.junit.Assert.assertEquals;

import org.dolan.datastructures.Line;
import org.junit.Test;

public class LineTester {
	@Test
	public void testIfLineCanExtractThreadNo() {
		ILine line = new Line("13:04:00,466 INFO  [com.debs.api.http.WCCookieHelper] (http-executor-threads - 37) [version: 0][name: JSESSIONID][value: 0000deq0cf-Ym2Mv9QzMriJhGmC:17h4h6lv9][domain: www.debenhams.ie][path: /][expiry: null]", 0);
		assertEquals(37, line.getThreadNumber());

		line = new Line("13:04:00,669 INFO  [com.debs.api.rest.commerce.AbstractWCSimpleHandler] (http-executor-threads - 45) response cookie:[version: 0][name: WC_ACTIVEPOINTER][value: %2d1%2c10701][domain: www.debenhams.com][path: /][expiry: null]", 0);
		assertEquals(45, line.getThreadNumber());

		line = new Line("18:46:19,692 INFO  [com.debs.api.cart.mtp.GetCartResponseMTP] (http-executor-threads - 49) default recordSetTotal: 1", 0);
		assertEquals(49, line.getThreadNumber());

		line = new Line("18:46:20,194 INFO  [com.debs.api.http.DebHttpInvoker] (http-executor-threads - 41) response cookie after execution : ----[version: 0][name: WC_SESSION_ESTABLISHED][value: true][domain: www.debenhams.com][path: /][expiry: null]", 0);
		assertEquals(41, line.getThreadNumber());
	}

	@Test
	public void testIfLineCanExtractTimeStamp() {
		ILine line = new Line("13:04:00,466 INFO  [com.debs.api.http.WCCookieHelper] (http-executor-threads - 37) [version: 0][name: JSESSIONID][value: 0000deq0cf-Ym2Mv9QzMriJhGmC:17h4h6lv9][domain: www.debenhams.ie][path: /][expiry: null]", 0);
		assertEquals(13, line.getTimeStamp().hour);
		assertEquals(4, line.getTimeStamp().minute);
		assertEquals(0, line.getTimeStamp().second);
		assertEquals(466, line.getTimeStamp().millisecond);

		assertEquals("13:04:00,466", line.getTimeStamp().toString());
	}
}
