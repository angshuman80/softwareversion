package com.miradry.softwareversion.service;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.file.CloudFileClient;
import com.microsoft.azure.storage.file.CloudFileDirectory;
import com.microsoft.azure.storage.file.CloudFileShare;
import com.microsoft.azure.storage.file.ListFileItem;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;



@Service
@Component
public class StorageService implements InitializingBean {
	@Autowired
	private CloudStorageAccount cloudStorageAccount;
	
	private static final String DATEFORMAT = "yyyy-MM-dd";

	@Value("${azure.storage.container-name}")
	private String containerName;
	

	private CloudFileClient cloudFileClient = null;
	private CloudFileShare cloudFileShare = null;

	@Override
	public void afterPropertiesSet() throws Exception {

		initializeFileShareClient();
	}

	private void initializeFileShareClient() throws URISyntaxException, StorageException {
		// TODO Auto-generated method stub
		cloudFileClient = cloudStorageAccount.createCloudFileClient();
		// Get a reference to a container. (Name must be lower case.)
		cloudFileShare = cloudFileClient.getShareReference(containerName);

	}



	public String searchFile(String dir, String path,String pattern) throws StorageException, URISyntaxException {
		CloudFileDirectory rootDir = cloudFileShare.getRootDirectoryReference();
		String[] destinationNameArray = path.split("/");
		CloudFileDirectory subDir = rootDir;
		for (String name : destinationNameArray) {
			if (name.equals("")) {
				continue;
			}
			subDir = subDir.getDirectoryReference(name);
			if (name.equals(dir)) {
				break;
			}
		}
		
		Iterable<ListFileItem> listFileItems = subDir.listFilesAndDirectories();
		if (listFileItems != null && listFileItems.iterator().hasNext()) {
			for (ListFileItem listFileItem : listFileItems) {
				String subDirUri = listFileItem.getUri().toString();
				String subfile = subDirUri.substring(subDirUri.lastIndexOf("/") + 1, subDirUri.length());
				if(subfile.contains(pattern)) {
					return subfile;
				}
				
			}
		}
		
		return null;
	}


	
	public static String GetUTCdatetimeAsString()
	{
	    final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
	    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	    final String utcTime = sdf.format(new Date());

	    return utcTime;
	}

}
