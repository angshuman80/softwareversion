package com.miradry.softwareversion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.miradry.softwareversion.model.DownloadRequest;
import com.miradry.softwareversion.model.DownloadResponse;
import com.miradry.softwareversion.service.SoftwareVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class SoftwareVersionHandler {
    @Autowired
    private SoftwareVersionService softwareVersionService;

    private static String downloadUrl = "https://miradrystorage.file.core.windows.net/#path#?sv=2018-03-28&ss=bfqt&srt=sco&sp=rwdlacup&se=2040-01-02T00:12:53Z&st=2019-04-05T15:12:53Z&spr=https,http&sig=OHJu%2FOxuQ%2BlNJrO%2Fts%2FLOUeS9gRi5tMKWVrZEFjAgCs%3D";

    private static String rootFolder = "miradry";

    private static String firmwareFolder = "miraDry_Upgrade_Files";


    @FunctionName("softwareversion")
    public HttpResponseMessage execute(
            @HttpTrigger(name = "request", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<DownloadRequest>> request,
            ExecutionContext context) {
        DownloadRequest downloadRequest = request.getBody().get();

        //	DownloadResponse downloadResponse1 = handleRequest(downloadRequest, context);
        context.getLogger().info("Starting SoftwareVersion Invokation ");

        if (!validateDownloadRequest(downloadRequest)) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Bad Request").build();
        }

        try {
            context.getLogger().info("Download Request " + downloadRequest);
            DownloadResponse downloadResponse = new DownloadResponse();
            process(downloadRequest, context, downloadResponse);

            String json = new ObjectMapper().writeValueAsString(downloadResponse);
            context.getLogger().info("The download response = " + json);
            return request.createResponseBuilder(HttpStatus.OK).body(json).build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            context.getLogger().info("Error = " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()).build();
        }
    }

    private boolean validateDownloadRequest(DownloadRequest downloadRequest) {

        if (downloadRequest.getConsoleId() == null || downloadRequest.getHandPieceid() == null
                || downloadRequest.getModemId() == null || downloadRequest.getConsoleId().isEmpty()
                || downloadRequest.getHandPieceid().isEmpty() || downloadRequest.getModemId().isEmpty()) {
            return false;
        }

        if (downloadRequest.getConsoleSV() == null || downloadRequest.getConsoleSV().isEmpty()
                || downloadRequest.getHandPieceSV() == null || downloadRequest.getHandPieceSV().isEmpty()
                || downloadRequest.getModemFV() == null || downloadRequest.getModemFV().isEmpty()
                || downloadRequest.getConsoleCfgVer() == null || downloadRequest.getConsoleCfgVer().isEmpty()
                || downloadRequest.getModemAV() == null || downloadRequest.getModemAV().isEmpty()
                || downloadRequest.getModemRFV() == null || downloadRequest.getModemRFV().isEmpty()) {
            return false;
        }

        return true;
    }

    private void process(DownloadRequest downloadRequest, ExecutionContext context, DownloadResponse downloadResponse)
            throws Exception {
        String urlTemp = System.getenv("downloadUrl");
        String firmwareFolder = System.getenv("firmwareFolder");
        String rootFolder = System.getenv("rootFolder");

        if (urlTemp == null) {
            urlTemp = downloadUrl;
        }

        if (firmwareFolder == null) {
            firmwareFolder = SoftwareVersionHandler.firmwareFolder;
        }

        if (rootFolder == null) {
            rootFolder = SoftwareVersionHandler.rootFolder;
        }

        Map<String, String> responseMap = softwareVersionService.updateSftwVerByConsole(downloadRequest, context,
                urlTemp, downloadResponse, firmwareFolder, rootFolder);
        context.getLogger().info("Response  = " + responseMap);

        if (responseMap != null && !responseMap.isEmpty()) {
            if (responseMap.containsKey(SoftwareVersionService.CONSOLE_ZIP)) {
                downloadResponse.setConsoleZipUrl(responseMap.get(SoftwareVersionService.CONSOLE_ZIP));
            }
            if (responseMap.containsKey(SoftwareVersionService.MODEM_ZIP)) {
                downloadResponse.setModemZipUrl(responseMap.get(SoftwareVersionService.MODEM_ZIP));
            }

            boolean result = softwareVersionService.addEntryToDownloadHistory(downloadRequest.getConsoleId(), context,
                    responseMap.get(SoftwareVersionService.CONSOLE_FILENAME),
                    responseMap.get(SoftwareVersionService.MODEM_FILENAME));
            if (!result) {
                context.getLogger()
                        .info("Entry to Download History failed for console " + downloadRequest.getConsoleId());
            }

        } else {
            responseMap = softwareVersionService.updateSftWVerConsoleByCountry(downloadRequest, context, urlTemp,
                    downloadResponse, firmwareFolder, rootFolder);

            if (responseMap != null && !responseMap.isEmpty()) {
                if (responseMap.containsKey(SoftwareVersionService.CONSOLE_ZIP)) {
                    downloadResponse.setConsoleZipUrl(responseMap.get(SoftwareVersionService.CONSOLE_ZIP));
                }
                if (responseMap.containsKey(SoftwareVersionService.MODEM_ZIP)) {
                    downloadResponse.setModemZipUrl(responseMap.get(SoftwareVersionService.MODEM_ZIP));
                }

                boolean result = softwareVersionService.addEntryToDownloadHistory(downloadRequest.getConsoleId(),
                        context, responseMap.get(SoftwareVersionService.CONSOLE_FILENAME),
                        responseMap.get(SoftwareVersionService.MODEM_FILENAME));
                if (!result) {
                    context.getLogger()
                            .info("Entry to Download History failed for console " + downloadRequest.getConsoleId());
                }
            }

        }

    }

}
