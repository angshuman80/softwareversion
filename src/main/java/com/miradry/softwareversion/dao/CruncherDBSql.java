package com.miradry.softwareversion.dao;

public class CruncherDBSql {
	

	public static String getProcessInstanceStatus =  
			"SELECT  ID, Status FROM ProcessingInstances " + 
			"WHERE ID = ? ";
	
	public static String getSftwVerByConsole =  
			"select Console,ConsoleSW,ConsoleConfig,HandpieceSW,ConsoleZipFilename,ModemFW,ModemApp,ModemConfig,ModemRadioFW,ModemZipFilename from softwareVersionByConsole where Console= ? ";
	
	public static String getSftwVerByCountryAndConsole="SELECT Sites.Country, ConsoleSW,HandpieceSW,ConsoleZipFilename,ModemFW,ModemApp,ModemConfig,ModemRadioFW,ModemZipFilename " + 
			"	FROM SystemsToSites " + 
			"	INNER JOIN SITES on SiteID=Sites.ID " + 
			"	INNER JOIN SoftwareVersionbyCountry ON SoftwareVersionByCountry.Country=Sites.Country " + 
			"	WHERE Product LIKE 'PN1950%' AND (INDATE>OUTDATE OR OUTDATE IS NULL) AND Discard <1 AND Lot= ?";
	
	public static String downloadHistory = "INSERT INTO DownloadHistory (Console,DownloadTime,Filename)"
			+ " OUTPUT INSERTED.ID  VALUES(?, GETDATE(), ?);";
	
	
	

}
