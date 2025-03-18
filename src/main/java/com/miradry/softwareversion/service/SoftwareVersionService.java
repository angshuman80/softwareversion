package com.miradry.softwareversion.service;

import com.microsoft.azure.functions.ExecutionContext;
import com.miradry.softwareversion.dao.CruncherDBDao;
import com.miradry.softwareversion.model.DownloadRequest;
import com.miradry.softwareversion.model.DownloadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SoftwareVersionService {

	@Autowired
	private CruncherDBDao cruncherDBDao;

	@Autowired
	private StorageService storageService;
	

	public static String CONSOLE_ZIP="CONSOLE_ZIP";
	public static String CONSOLE_FILENAME="CONSOLE_FILENAME";
	public static String MODEM_FILENAME="MODEM_FILENAME";
	public static String MODEM_ZIP="MODEM_ZIP";
	private static String HP="HP";
	
	private static String FIRMWARE_FOLDER =  "miraDry Upgrade Files";
	private static String ROOT =  "miradry";
	
	/**
	 * Check the entry in SoftwareVerConsole table
	 * 
	 * @param downloadRequest
	 */
	public Map<String,String> updateSftwVerByConsole(DownloadRequest downloadRequest, ExecutionContext context, String urlTemp
			,DownloadResponse downloadResponse,String firmwareFolder,String rootFolder)
			throws Exception {
		
		Map<String, String> sftwerMap = cruncherDBDao.getSftwVerByConsole(downloadRequest.getConsoleId());
		
		Map<String, String> responseMap = new HashMap<String,String>();
		
		if (sftwerMap != null && !sftwerMap.isEmpty()) {
			String console = sftwerMap.get("Console");
			String consoleSW = sftwerMap.get("ConsoleSW");
			String consoleConfig = sftwerMap.get("ConsoleConfig");
			String handpieceSW = sftwerMap.get("HandpieceSW");
			String consoleZipFilename = sftwerMap.get("ConsoleZipFilename");
			String modemFW = sftwerMap.get("ModemFW");
			String modemApp = sftwerMap.get("ModemApp");
			String modemConfig = sftwerMap.get("ModemConfig");
			String modemRadioFW = sftwerMap.get("ModemRadioFW");
			String modemZipFilename = sftwerMap.get("ModemZipFilename");
			
			context.getLogger()
			.info("Able to get record by Console " + console + " "+consoleSW+" "+consoleConfig+ " "+handpieceSW+" "+consoleZipFilename
					+" "+modemFW+" "+modemApp+" "+modemConfig+" "+modemRadioFW+" "+modemZipFilename);
			
			if(!compareConsoleParameter(consoleSW, consoleConfig, handpieceSW, downloadRequest, true)) {
				context.getLogger()
				.info("After console check " + console+ " "+rootFolder+ " "+firmwareFolder+ " "+urlTemp );
				String pattern = consoleZipFilename;
				//String fileName = storageService.searchFile(firmwareFolder, firmwareFolder, pattern);
				//context.getLogger().info("fileName = " + fileName);
			//	if(fileName!=null) {
					responseMap.put(CONSOLE_ZIP, getUrl(pattern, urlTemp, rootFolder+"/"+firmwareFolder));
					responseMap.put(CONSOLE_FILENAME, pattern);
				//}else {
				//	throw new Exception("Unable to find Software Version file for Console");
				//}
				
			}
			
			if(!compareModemParameter(modemFW,modemApp, modemConfig, modemRadioFW , downloadRequest)) {
				String pattern = modemZipFilename;
			/*	String fileName = storageService.searchFile(firmwareFolder, firmwareFolder, pattern);
				context.getLogger().info("fileName = " + fileName);
				if(fileName!=null) {*/
					responseMap.put(MODEM_ZIP, getUrl(pattern, urlTemp, rootFolder+"/"+firmwareFolder));
					responseMap.put(CONSOLE_ZIP, getUrl(consoleZipFilename, urlTemp, rootFolder+"/"+firmwareFolder));
					responseMap.put(CONSOLE_FILENAME, consoleZipFilename);
					responseMap.put(MODEM_FILENAME, pattern);
			/*	}else {
					throw new Exception("Unable to find Software Version file for Console");
				}*/
			}

			
		}
		return responseMap;
	}
	
	
	private boolean compareConsoleParameter(String ConsoleSW,String ConsoleConfig, String HandpieceSW, DownloadRequest downloadRequest,boolean isConsoleCheck) {
		
		if((ConsoleSW!=null && !ConsoleSW.isEmpty() && !downloadRequest.getConsoleSV().equalsIgnoreCase(ConsoleSW)) || 
				(HandpieceSW!=null && !HandpieceSW.isEmpty() && !downloadRequest.getHandPieceSV().equalsIgnoreCase(HandpieceSW)))
				{
			
			return false;
		}
		
		if(isConsoleCheck) {
			if(ConsoleConfig !=null && !ConsoleConfig.isEmpty() && !downloadRequest.getConsoleCfgVer().equalsIgnoreCase(ConsoleConfig)) {
				return false;
			}
		}
		
		return true;
		
	}

	
   private boolean compareModemParameter(String ModemFW,String ModemApp, String ModemConfig, String ModemRadioFW , DownloadRequest downloadRequest) {
		
		if((ModemFW!=null && !ModemFW.isEmpty() && !downloadRequest.getModemFV().equalsIgnoreCase(ModemFW)) || (ModemApp!=null && !ModemApp.isEmpty() 
				&& !downloadRequest.getModemAV().equalsIgnoreCase(ModemApp))
				|| (ModemConfig!=null && !ModemConfig.isEmpty() && !downloadRequest.getModemCfgVer().equalsIgnoreCase(ModemConfig)) || 
				(ModemRadioFW!=null && !ModemRadioFW.isEmpty() && !downloadRequest.getModemRFV().equalsIgnoreCase(ModemRadioFW))) {
			
			return false;
		}
		
		
		return true;
		
	}

	/**
	 * This method updates DownloadReponse by Software version of Console By Country
	 * 
	 * @param downloadRequest
	 * @param context
	 * @param urlTemp
	 * @return
	 */
	public Map<String,String> updateSftWVerConsoleByCountry(DownloadRequest downloadRequest, ExecutionContext context,
			String urlTemp, DownloadResponse downloadResponse,String firmwareFolder,String rootFolder) throws Exception {

		Map<String, String> sftwerMap = cruncherDBDao.getSftwVerByCCountry(downloadRequest.getConsoleId());
		Map<String, String> responseMap = new HashMap<String,String>();

		if (sftwerMap != null && !sftwerMap.isEmpty()) {
			String consoleSW = sftwerMap.get("ConsoleSW");
			String handpieceSW = sftwerMap.get("HandpieceSW");
			String consoleZipFilename = sftwerMap.get("ConsoleZipFilename");
			String modemFW = sftwerMap.get("ModemFW");
			String modemApp = sftwerMap.get("ModemApp");
			String modemConfig = sftwerMap.get("ModemConfig");
			String modemRadioFW = sftwerMap.get("ModemRadioFW");
			String modemZipFilename = sftwerMap.get("ModemZipFilename");
			
			context.getLogger()
			.info("Able to get record by Country for Console " + downloadRequest.getConsoleId() +
					rootFolder+ " "+firmwareFolder+ " "+urlTemp);
			
			if(!compareConsoleParameter(consoleSW, null, handpieceSW, downloadRequest, false)) {
				String pattern = consoleZipFilename;
			/*	String fileName = storageService.searchFile(firmwareFolder, firmwareFolder, pattern);
				context.getLogger().info("fileName = " + fileName);
				if(fileName!=null) {*/
					responseMap.put(CONSOLE_ZIP, getUrl(pattern, urlTemp, rootFolder+"/"+firmwareFolder));
					responseMap.put(CONSOLE_FILENAME, pattern);
					
			/*	}else {
					throw new Exception("Unable to find Software Version file for Console");
				}*/
			}
			
			if(!compareModemParameter(modemFW,modemApp, modemConfig, modemRadioFW , downloadRequest)) {
				String pattern = modemZipFilename;
			/*	String fileName = storageService.searchFile(firmwareFolder, firmwareFolder, pattern);
				context.getLogger().info("fileName = " + fileName);
				if(fileName!=null) {*/
				responseMap.put(MODEM_ZIP, getUrl(pattern, urlTemp, rootFolder+"/"+firmwareFolder));
				responseMap.put(CONSOLE_ZIP, getUrl(consoleZipFilename, urlTemp, rootFolder+"/"+firmwareFolder));
				responseMap.put(MODEM_FILENAME, pattern);
				responseMap.put(CONSOLE_FILENAME, consoleZipFilename);
			/*	}else {
					throw new Exception("Unable to find Software Version file for Console");
				}*/
			}
		
		}

		return responseMap;

	}
	
	public boolean addEntryToDownloadHistory(String console , ExecutionContext context,String consoleZipFileName,String modemzipfileName) {
	
	try {
		if(consoleZipFileName!=null && !consoleZipFileName.isEmpty()) {
			Integer ID = cruncherDBDao.createDownloadHistory(console, consoleZipFileName);
			context.getLogger().info("Entry added to download History for console "+console+" filename "+consoleZipFileName+ " With ID "+ID);
		}
		
		if(modemzipfileName!=null && !modemzipfileName.isEmpty()) {
			Integer ID = cruncherDBDao.createDownloadHistory(console, modemzipfileName);
			context.getLogger().info("Entry added to download History for console "+console+" filename "+modemzipfileName+ " With ID "+ID);
		}
	}catch(Exception e) {
		context.getLogger().info("The Insert failed to download hist for console "+console+ " with error " + e.getMessage() );
		return false;
	}
	
	return true;
		
	}
	

	private static String getUrl(String fileName, String urlTemp, String path) {
		String genPath = path + "/" + fileName;
		String url = urlTemp.replace("#path#", genPath);
		return url;
	}
	

}
