package com.github.davidmoten.email;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.sendgrid.SendGrid;

public class EmailServlet extends HttpServlet {

	private static final long serialVersionUID = -4925620975592532511L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String username = System.getProperty("SENDGRID_USERNAME");
		String password = System.getProperty("SENDGRID_PASSWORD");

		StringBuilder s = new StringBuilder();
		@SuppressWarnings("unchecked")
		Enumeration<String> en = req.getParameterNames();
		while (en.hasMoreElements()) {
			String key = en.nextElement();
			String[] values = req.getParameterValues(key);
			s.append(key);
			s.append("=");
			for (String value : values) {
				s.append(value);
				s.append(" ");
			}
			s.append("\n");
		}
		s.append("sent via SendGrid");

		SendGrid sendgrid = new SendGrid(username, password);

		sendgrid.addTo("davidmoten@gmail.com");
		sendgrid.setFrom("davidmoten@gmail.com");
		sendgrid.setSubject("hello from cloudbees-email app");
		sendgrid.setText(s.toString());

		sendgrid.send();

		// Set to expire far in the past.
		resp.setHeader("Expires", "-1");
		// Set standard HTTP/1.1 no-cache headers.
		resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		resp.setHeader("Pragma", "no-cache");

		resp.getWriter().write("sid=somebody&status=1");
	}

}
