package com.spigen.asos;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.ResourceUtils;

public class OAuth2{
	static Sheets sheets;
	private static String APPLICATION_NAME = "app test 01";
	private static String SPREADSHEET_ID = "1z7kTDb8KS7M7k6a5KcLmQe2QCCh9YRHSKWmA9ttmDb4";
	private static final File DATA_STORE_DIR =  new File(OAuth2.class.getResource("/").getPath(), "credentials");
    
	public static Credential authorize() throws IOException, GeneralSecurityException {
        	String appName= "[Spigen-web] 회의록22";
            String keyFileName ="client_secret_SpigenWeb.json";
            
            InputStream keyFile = ResourceUtils.getURL("classpath:" + keyFileName).openStream();
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new InputStreamReader(keyFile));
            System.out.println(clientSecrets);
            List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);
            
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            									GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), 
            									clientSecrets, scopes)
            									.setDataStoreFactory(new FileDataStoreFactory(DATA_STORE_DIR))
            									.setAccessType("offline")
            									.build();
            Credential credential = new	AuthorizationCodeInstalledApp(flow, new LocalServerReceiver())
            														.authorize("hyjeong@spigen-web.iam.gserviceaccount.com");
            
            
       //     GoogleCredential credential = GoogleCredential.fromStream(keyFile).createScoped(Arrays.asList(SheetsScopes.SPREADSHEETS));
       //     NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
       //     sheets = new Sheets.Builder(transport, JacksonFactory.getDefaultInstance(), credential).setApplicationName(appName).build();
       //     System.out.println(credential);
            return credential;
        }
        
        public static void main(String[] args) throws IOException, GeneralSecurityException {
        	sheets = getSheetsService();
        	String range = "시트1!A1:B5";
        	
        	ValueRange response = sheets.spreadsheets().values().get(SPREADSHEET_ID, range).execute();
        	
        	List<List<Object>> values = response.getValues();
        	
        	if(values == null || values.isEmpty()) {
        		System.out.println("No data");
        	}else {
        		for(List row : values) {
        			System.out.println(row.get(5));
        		}
        	}
        	
		}
        
        public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        	Credential credential = authorize();
        	
			return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).setApplicationName(APPLICATION_NAME).build();
        }
        
}