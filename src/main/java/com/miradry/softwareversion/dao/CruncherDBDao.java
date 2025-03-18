package com.miradry.softwareversion.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@Repository
public class CruncherDBDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

  
	
	@Transactional
	public Map<String, String> getSftwVerByConsole(String consoleId){
		List<Map<String, String>> sftwByConsole = jdbcTemplate.query(CruncherDBSql.getSftwVerByConsole,
				new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, consoleId);
					}
				}, new SftwByConsoleMapper());
		
		if(sftwByConsole!=null && !sftwByConsole.isEmpty()) {
			return sftwByConsole.get(0);
		}
		
		return null;
	}
	
	public class SftwByConsoleMapper implements RowMapper<Map<String, String>> {
		public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String, String> rowMap = new HashMap<String, String>();
			rowMap.put("Console", rs.getString("Console"));
			rowMap.put("ConsoleSW", rs.getString("ConsoleSW"));
			rowMap.put("ConsoleConfig", rs.getString("ConsoleConfig"));
			rowMap.put("HandpieceSW", rs.getString("HandpieceSW"));
			rowMap.put("ConsoleZipFilename", rs.getString("ConsoleZipFilename"));
			rowMap.put("ModemFW", rs.getString("ModemFW"));
			rowMap.put("ModemApp", rs.getString("ModemApp"));
			rowMap.put("ModemConfig", rs.getString("ModemConfig"));
			rowMap.put("ModemRadioFW", rs.getString("ModemRadioFW"));
			rowMap.put("ModemZipFilename", rs.getString("ModemZipFilename"));

			return rowMap;
		}
	}
	
	@Transactional
	public Map<String, String> getSftwVerByCCountry(String consoleId){
		List<Map<String, String>> sftwByCConsole = jdbcTemplate.query(CruncherDBSql.getSftwVerByCountryAndConsole,
				new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, consoleId);
					}
				}, new SftwByCountryCMapper());
		
		if(sftwByCConsole!=null && !sftwByCConsole.isEmpty()) {
			return sftwByCConsole.get(0);
		}
		
		return null;
	}
	
	@Transactional
	public Integer createDownloadHistory(String console,String filename) {
		List<Map<String,Integer>> dhList = jdbcTemplate.query(CruncherDBSql.downloadHistory,
				new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, console);
						ps.setString(2, filename);
						
					}
				}, new DownloadHisttMapper());
		
		if(dhList!=null && !dhList.isEmpty()) {
			Map<String,Integer> dhMap = dhList.get(0);
			return dhMap.get("ID");
		}
		
		return null;
	}
	
	public class DownloadHisttMapper implements RowMapper<Map<String, Integer>> {
		public Map<String, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String, Integer> rowMap = new HashMap<String, Integer>();
			rowMap.put("ID", rs.getInt("ID"));
			return rowMap;
		}
	}
	
	public class SftwByCountryCMapper implements RowMapper<Map<String, String>> {
		public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String, String> rowMap = new HashMap<String, String>();
			rowMap.put("ConsoleSW", rs.getString("ConsoleSW"));
			rowMap.put("HandpieceSW", rs.getString("HandpieceSW"));
			rowMap.put("ConsoleZipFilename", rs.getString("ConsoleZipFilename"));
			rowMap.put("ModemFW", rs.getString("ModemFW"));
			rowMap.put("ModemApp", rs.getString("ModemApp"));
			rowMap.put("ModemConfig", rs.getString("ModemConfig"));
			rowMap.put("ModemRadioFW", rs.getString("ModemRadioFW"));
			rowMap.put("ModemZipFilename", rs.getString("ModemZipFilename"));

			return rowMap;
		}
	}
	

	private LocalDateTime getDateFromString(String dateStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
		LocalDateTime date = LocalDateTime.parse(dateStr, formatter);
		return date;
	}

	private String getStrDateFromFileName(String fileName) {

		String date = fileName.substring(0, fileName.indexOf("_"));
		String time = fileName.substring(fileName.indexOf("_") + 1, fileName.lastIndexOf("_"));
		String time_h = time.substring(0, 2);
		String time_m = time.substring(2, 4);
		String time_s = time.substring(4, time.length());

		String dateStr = date + " " + time_h + ":" + time_m + ":" + time_s;

		return dateStr;

	}

}
