package com.miradry.softwareversion.model;

public class DownloadRequest {

	private String consoleId;
	private String consoleSV;
	private String consoleCfgVer;
	private String handPieceid;
	private String handPieceSV;
	private String modemId;
	private String modemFV;
	private String modemAV;
	private String modemCfgVer;
	private String modemRFV;

	private String ts;

	public String getConsoleId() {
		return consoleId;
	}

	public void setConsoleId(String consoleId) {
		this.consoleId = consoleId;
	}

	public String getHandPieceid() {
		return handPieceid;
	}

	public void setHandPieceid(String handPieceid) {
		this.handPieceid = handPieceid;
	}

	public String getHandPieceSV() {
		return handPieceSV;
	}

	public void setHandPieceSV(String handPieceSV) {
		this.handPieceSV = handPieceSV;
	}

	public String getConsoleSV() {
		return consoleSV;
	}

	public void setConsoleSV(String consoleSV) {
		this.consoleSV = consoleSV;
	}

	public String getConsoleCfgVer() {
		return consoleCfgVer;
	}

	public void setConsoleCfgVer(String consoleCfgVer) {
		this.consoleCfgVer = consoleCfgVer;
	}

	public String getModemId() {
		return modemId;
	}

	public void setModemId(String modemId) {
		this.modemId = modemId;
	}

	public String getModemFV() {
		return modemFV;
	}

	public void setModemFV(String modemFV) {
		this.modemFV = modemFV;
	}

	public String getModemAV() {
		return modemAV;
	}

	public void setModemAV(String modemAV) {
		this.modemAV = modemAV;
	}

	public String getModemCfgVer() {
		return modemCfgVer;
	}

	public void setModemCfgVer(String modemCfgVer) {
		this.modemCfgVer = modemCfgVer;
	}

	public String getModemRFV() {
		return modemRFV;
	}

	public void setModemRFV(String modemRFV) {
		this.modemRFV = modemRFV;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append(consoleId).append(" ").append(consoleSV).append(" ").append(consoleCfgVer).append(" ").append(handPieceid).
		append(" ").append(handPieceSV).append(" ").append(modemId).append(" ").append(modemFV).append(" ").append(modemAV).append(" ")
		.append(modemCfgVer).append(" ").append(modemRFV);

		return builder.toString();
	}
	

}
