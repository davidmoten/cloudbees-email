package com.github.davidmoten.email;

import static com.github.davidmoten.email.Util.setNoCache;

import java.io.IOException;

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

		SendGrid sendgrid = createSendGrid();

		sendgrid.addTo("davidmoten@gmail.com");
		sendgrid.setFrom("davidmoten@gmail.com");
		sendgrid.setSubject("hello from cloudbees-email app");
		sendgrid.setText(Parameters.toString(req));
		sendgrid.send();

		setNoCache(resp);

		resp.setContentType("text/plain");
		resp.getWriter().write("sid=somebody&status=1");
	}

	private SendGrid createSendGrid() {
		String username = System.getProperty("SENDGRID_USERNAME");
		String password = System.getProperty("SENDGRID_PASSWORD");

		SendGrid sendgrid = new SendGrid(username, password);
		return sendgrid;
	}

}
