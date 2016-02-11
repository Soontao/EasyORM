package org.suntao.easyorm.session.defaults;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import org.suntao.easyorm.executor.defaults.SimpleExecutor;
import org.suntao.easyorm.map.MapStatment;
import org.suntao.easyorm.map.ResultMapConfig;
import org.suntao.easyorm.map.SimpleResultMapping;
import org.suntao.easyorm.proxy.MapperProxyBuilder;
import org.suntao.easyorm.scan.Scanner;
import org.suntao.easyorm.scan.SimpleScanner;
import org.suntao.easyorm.session.SqlSession;
import org.suntao.easyorm.xmlparse.DatabaseConfig;
import org.suntao.easyorm.xmlparse.EasyormConfig;

public class DefaultSqlSession implements SqlSession {
	private DatabaseConfig databaseConfig;
	private EasyormConfig easyormConfig;
	private MapperProxyBuilder mapperProxyBuilder;
	private Map<String, MapStatment> mapStatments;
	private Map<String, ResultMapConfig<?>> resultMaps;
	private Scanner scanner;

	public DefaultSqlSession(EasyormConfig easyormConfig) {
		this.easyormConfig = easyormConfig;
		this.databaseConfig = easyormConfig.getDatabaseConfig();
		if (scanner == null)
			scanner = new SimpleScanner(easyormConfig);
		scanner.scan();
		this.mapStatments = scanner.getScannedMapStatment();
		this.resultMaps = scanner.getScanedResultMap();
	}

	@Override
	public Connection getConnection() {
		try {
			Class.forName(databaseConfig.getDriver());
		} catch (ClassNotFoundException e) {
			// TO DO
			e.printStackTrace();
		}
		Connection result = null;
		try {
			result = DriverManager.getConnection(databaseConfig.getJdbcurl(),
					databaseConfig.getUsername(), databaseConfig.getPassword());
		} catch (SQLException e) {
			// TO DO
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public <T> T getMapper(Class<T> mapperClass) {
		T result = null;
		result = (T) MapperProxyBuilder.getMapperProxy(mapperClass,
				new SimpleExecutor(this, new SimpleResultMapping()),
				mapStatments);

		return result;
	}

	public Scanner getScanner() {
		return scanner;
	}

	public void setScanner(Scanner scanner) {
		this.scanner = scanner;
	}

	public Map<String, MapStatment> getMapStatments() {
		return mapStatments;
	}

	public void setMapStatments(Map<String, MapStatment> mapStatments) {
		this.mapStatments = mapStatments;
	}

	public Map<String, ResultMapConfig<?>> getResultMaps() {
		return resultMaps;
	}

	public void setResultMaps(Map<String, ResultMapConfig<?>> resultMaps) {
		this.resultMaps = resultMaps;
	}

	@Override
	public void returnConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
