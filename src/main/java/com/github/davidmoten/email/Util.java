package com.github.davidmoten.email;

import javax.servlet.http.HttpServletResponse;

public class Util {

	public static void setNoCache(HttpServletResponse resp) {
		// Set to expire far in the past.
		resp.setHeader("Expires", "-1");
		// Set standard HTTP/1.1 no-cache headers.
		resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		resp.setHeader("Pragma", "no-cache");
	}

}
