package com.markava.itcompany.connectionpool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.qa.connectionproject.connection.ConnectionPool;

public class ConnectThread extends Thread {

	private final static Logger LOGGER = LogManager.getLogger(ConnectThread.class);

	private LazyConnectionPool conPool;
	private static Logger logger = LogManager.getLogger();

	public ConnectThread(LazyConnectionPool conPool) {
		super();
		this.conPool = conPool;
	}

	public void run() {
		Connection connection = null;
		Random random = new Random();
		try {
			connection = ConnectionPool.getInstance().getConnection();
			Thread.sleep(1000 + random.nextInt(2000));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Thread error", e);
		} catch (InterruptedException e) {
			logger.log(Level.ERROR, "Thread error", e);
		} finally {
			ConnectionPool.getInstance().releaseConnection(connection);
		}
	}

	public static void main(String[] args) {

		LazyConnectionPool conPool = LazyConnectionPool.getInstance();
		for (int i = 0; i < 10; i++) {
			ConnectThread thread = new ConnectThread(conPool);
			logger.log(Level.INFO, "Thread number "+ i+ " is created");
			thread.start();
		}
	}
}
