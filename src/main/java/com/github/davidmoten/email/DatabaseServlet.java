package com.github.davidmoten.email;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.github.davidmoten.rx.jdbc.Database;

public class DatabaseServlet extends HttpServlet {

	private static final long serialVersionUID = -4137504003076597022L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String resource = "java:comp/env/jdbc/flashq";
			Connection con = getConnection(resource);
			Database db = Database.from(con);
			String text = Parameters.toString(req);
			int count = db.update("insert into flashq.entry(text) values(?)")
			// set text parameter
					.parameter(text)
					// get num records inserted
					.count()
					// block and return
					.toBlockingObservable().single();
			System.out.println(count + " records inserted");
			con.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		resp.setContentType("text/plain");
		resp.getWriter().write("sid=somebody&status=1");
	}

	private Connection getConnection(String resource) {
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(resource);
			return ds.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

}
