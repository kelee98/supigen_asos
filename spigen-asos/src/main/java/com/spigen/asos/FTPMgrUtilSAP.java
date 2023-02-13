package com.spigen.asos;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.spigen.asos.model.FTPMgrVO;

public class FTPMgrUtilSAP {

	FTPClient ftpClient; 
	FTPClientConfig config;
	
	static String server = "13.125.137.202";
	static int port = 21;
	static String id = "root";
	static String password = "3!wjfG0!";
	
//	static String server = "172.16.3.61";
//	static int port = 5757;
//	static String id = "spigenweb";
//	static String password = "Spigen1234!";
	

	public FTPMgrUtilSAP(String server, int port, String id, String password) { 
		this.server = server; 
		this.port = port; 
		ftpClient = new FTPClient(); 
        config = new FTPClientConfig(FTPClientConfig.SYST_UNIX); 
	} 
	
	static int fromDate=0;
	static int toDate=0;
	static String fullPath="";
	static String tmpPath="";
	
	public List<FTPMgrVO> getFileList(FTPMgrVO vo) throws IOException {
		List<FTPMgrVO> resultList = new ArrayList<FTPMgrVO>();
		String subYear = vo.getQryYear().substring(2, 4);
		switch (vo.getQryQuarter()) {
		case "1":
			fromDate = Integer.parseInt(subYear+"01");
			toDate = Integer.parseInt(subYear+"03");
			break;
		case "2":
			fromDate = Integer.parseInt(subYear+"04");
			toDate = Integer.parseInt(subYear+"06");
			break;
		case "3":
			fromDate = Integer.parseInt(subYear+"07");
			toDate = Integer.parseInt(subYear+"09");
			break;
		case "4":
			fromDate = Integer.parseInt(subYear+"10");
			toDate = Integer.parseInt(subYear+"12");
			break;
		}
		
		vo.setFilePath("Amazon CY JP");
		if("A".equals(vo.getDocType())){
			fullPath = "/Maetel_ovd_f/해외사업팀 Maetel Server/07. 사업기획팀/02. 매출정산/1. Amazon";
		}else if("C".equals(vo.getDocType())) {
			fullPath = "/Maetel_ovd_f/해외사업팀 Maetel Server/07. 사업기획팀/02. 매출정산/2. Amazon_CSO";
		}
		System.out.println("fullPath:" + fullPath);
		FTPMgrUtilSAP ftp = new FTPMgrUtilSAP(server, port, id, password); 

		config.setServerLanguageCode("ko");
		config.setDefaultDateFormatStr("d MMM yyyy");
		config.setRecentDateFormatStr("d MMM HH:mm");
		config.setUnparseableEntries(true);
        ftp.ftpClient.configure(config);
        ftp.ftpClient.setControlEncoding("euc-kr");
        
		ftp.login(ftp.id, ftp.password);
		
		ftp.ftpClient.enterLocalPassiveMode();
		
		ftp.cd(fullPath);

		System.out.println("ls: " + ftp.list().toString());
		
		FTPFile[] list = ftp.list();
		
		//System.out.println(list[0]);
		for (int h = 0; h < list.length; h++) {
			System.out.println("호출");
			if(list[h].isDirectory() && !("Amazon AU".equals(list[h].getName())||"과거 작업용 양식".equals(list[h].getName())
					                      ||"작업 종료".equals(list[h].getName())||"CSO 정산용 재고 처리".equals(list[h].getName())||"정산 Issue".equals(list[h].getName()))){
				if("A".equals(vo.getDocType())){
					tmpPath = fullPath + "/" + list[h].getName()+ "/" + vo.getQryYear()+ "년";
				}else if("C".equals(vo.getDocType())) {
					tmpPath = fullPath + "/" + list[h].getName()+ "/" + vo.getQryYear()+ "년도";
				}
				if(ftp.cd(tmpPath)) {
					FTPFile[] files = ftp.list(); 
					FTPFile[] subfiles; 
					String tmpPath2 = tmpPath;
					for (int i = 0; i < files.length ; i++) { 
						int fileName = Integer.parseInt(files[i].getName().substring(0,4)); 
						if(fromDate<=fileName && toDate>=fileName) {
							
							tmpPath2 += "/" + files[i].getName();
	
							if(ftp.cd(tmpPath2)) {
								subfiles = ftp.list();
								for (int j = 0; j < subfiles.length; j++) {
									if("pdf".equals(subfiles[j].getName().substring(subfiles[j].getName().lastIndexOf(".") + 1))) {
										
										FTPMgrVO returnVo = new FTPMgrVO();
										returnVo.setFileNm(subfiles[j].getName());
										returnVo.setFilePath(tmpPath2 + "/" + subfiles[j].getName());
										returnVo.setFileType("pdf");
										resultList.add(returnVo);
									}
								}
								tmpPath2 = tmpPath;
								ftp.cd(tmpPath2);
								subfiles = ftp.list();
							}
						}
					} 
				}
			}
			tmpPath ="";
		}
		
		ftp.logout(); 
		ftp.disconnect(); 
		//System.exit(1);
		
		return resultList; 
	}
	
	//파워아크 파일리스트
	public List<FTPMgrVO> getPFileList(FTPMgrVO vo) {
		List<FTPMgrVO> resultList = new ArrayList<FTPMgrVO>();
		String subYear = vo.getQryYear().substring(2, 4);
		switch (vo.getQryQuarter()) {
		case "1":
			fromDate = Integer.parseInt("01");
			toDate = Integer.parseInt("03");
			break;
		case "2":
			fromDate = Integer.parseInt("04");
			toDate = Integer.parseInt("06");
			break;
		case "3":
			fromDate = Integer.parseInt("07");
			toDate = Integer.parseInt("09");
			break;
		case "4":
			fromDate = Integer.parseInt("10");
			toDate = Integer.parseInt("12");
			break;
		}
		
		fullPath = "/Maetel_ovd_f/해외사업팀 Maetel Server/07. 사업기획팀/02. 매출정산/3. Amazon_PowerArc";
		
		FTPMgrUtilSAP ftp = new FTPMgrUtilSAP(server, port, id, password); 
		config.setServerLanguageCode("ko");
		config.setDefaultDateFormatStr("d MMM yyyy");
		config.setRecentDateFormatStr("d MMM HH:mm");
		config.setUnparseableEntries(true);
        ftp.ftpClient.configure(config);
        ftp.ftpClient.setControlEncoding("euc-kr");
        
		ftp.login(ftp.id, ftp.password);
		
		ftp.ftpClient.enterLocalPassiveMode();

		ftp.cd(fullPath);
		FTPFile[] list = ftp.list();
		
		for (int h = 0; h < list.length; h++) {
			if(list[h].isDirectory()) {
				System.out.println(list[h].getName().substring(0,2));
				int fileName = Integer.parseInt(list[h].getName().substring(0,2)); 
				if(fromDate<=fileName && toDate>=fileName){
					tmpPath = fullPath + "/" + list[h].getName();
					if(ftp.cd(tmpPath)) {
						FTPFile[] files = ftp.list(); 
						for (int i = 0; i < files.length ; i++) { 
							if("pdf".equals(files[i].getName().substring(files[i].getName().lastIndexOf(".") + 1))) {
								
								FTPMgrVO returnVo = new FTPMgrVO();
								returnVo.setFileNm(files[i].getName().replaceAll("\\'", "\\^"));
								returnVo.setFilePath(tmpPath + "/" + files[i].getName().replaceAll("\\'", "\\^"));
								returnVo.setFileType("pdf");
								resultList.add(returnVo);
							}
						} 
					}
				}
			}
			tmpPath ="";
		}
		
		ftp.logout(); 
		ftp.disconnect(); 
		//System.exit(1);
		
		return resultList; 
	}

	
	// 계정과 패스워드로 로그인 
	public boolean login(String user, String password) { 
		try { 
			this.connect(); 
			return ftpClient.login(user, password); 
			} catch (IOException ioe) {
				ioe.printStackTrace(); 
				} return false; 
	} 
	// 서버로부터 로그아웃 
	private boolean logout() {
		try { 
			return ftpClient.logout(); 
			} catch (IOException ioe) { 
				ioe.printStackTrace(); 
			} return false; 
	} 
	// 서버로 연결 
	public void connect() {
		try { 
			System.out.println("server: " + server + "/port: " + port );
			ftpClient.connect(server, port); 
			int reply; // 연결 시도후, 성공했는지 응답 코드 확인 
			reply = ftpClient.getReplyCode(); 
			if(!FTPReply.isPositiveCompletion(reply)) { 
				ftpClient.disconnect(); 
				System.err.println("서버로부터 연결을 거부당했습니다"); 
				//System.exit(1); 
				}else {
					System.out.println("연결 성공..."); 
				}
			} catch (IOException ioe) { 
				if(ftpClient.isConnected()) { 
					try { 
						ftpClient.disconnect(); 
						} catch(IOException f) { 
							// 
						} 
				} 
				System.err.println("서버에 연결할 수 없습니다"); 
				//System.exit(1); 
				} 
		} 
	
	// FTP의 ls 명령, 모든 파일 리스트를 가져온다 
	public FTPFile[] list() { 
		FTPFile[] files = null; 
		try { 
			//System.out.println("list call: " + this.ftpClient.listFiles());
			files = this.ftpClient.listFiles(); 
			System.out.println("files: " + files.length);
			return files; 
			} catch (IOException ioe) { 
				ioe.printStackTrace(); 
			} return null; 
	} 
	

	
	// 파일을 전송 받는다 
	public File get(String source, String target, String fileName) {
		System.out.println("target:" + target);
		FTPMgrUtilSAP ftp = new FTPMgrUtilSAP(server, port, id, password); 
		config.setServerLanguageCode("ko");
		config.setDefaultDateFormatStr("d MMM yyyy");
		config.setRecentDateFormatStr("d MMM HH:mm");
		config.setUnparseableEntries(true);
        ftp.ftpClient.configure(config);
        ftp.ftpClient.setControlEncoding("euc-kr");
        
		ftp.login(ftp.id, ftp.password);
		
		ftp.ftpClient.enterLocalPassiveMode();

		OutputStream output = null; 
		try {
			File local = new File(source, fileName); 
			output = new FileOutputStream(local); 
			} catch (FileNotFoundException fnfe) { 
				fnfe.printStackTrace(); 
			} 
			File file = new File(source,fileName); 
				try { 
					if (ftp.ftpClient.retrieveFile(target, output)) { 
						return file; 
					} 
				} catch (IOException ioe) { 
					ioe.printStackTrace(); 
				} 
				ftp.logout(); 
				ftp.disconnect(); 
				return null; 
			} 

	// 서버 디렉토리 이동 
	public boolean cd(String path) { 
		boolean flag = false;
		try { 
			flag = ftpClient.changeWorkingDirectory(path); 
			} catch (IOException ioe) { 
				flag = false;
				ioe.printStackTrace(); 
				} 
			return flag;
		} 
	
	// 서버로부터 연결을 닫는다
	private void disconnect() { 
		try { 
			ftpClient.disconnect(); 
			} catch (IOException ioe) { 
				ioe.printStackTrace(); 
			} 
	}


	public Map<String, String> mergePDFsFTP(String[] filePathArr, String[] fileNameArr, String URL) throws IOException {
		List<File> fileList = new ArrayList<File>();
		List<PDDocument> docList = new ArrayList<PDDocument>();
        Map<String,String> returnMap = new HashMap<String,String>();

		for (int i = 0; i < filePathArr.length; i++) {
			
			File file = null;
			PDDocument doc = null;
		    file = get("C:\\Temp", filePathArr[i].replaceAll( "\\^","\\'"), fileNameArr[i].replaceAll( "\\^","\\'"));
		    try {
			   doc = PDDocument.load(file);
		    } catch (IOException e) {
			   e.printStackTrace();
		    }
			fileList.add(file);
			docList.add(doc);
		}

		  Date dateNow = new Date(System.currentTimeMillis()); // 현재시간을 가져와 Date형으로 저장한다
		  SimpleDateFormat dateNowFormat  = new SimpleDateFormat("yyyyMMddHHmmss");
		  
		  String timeStamp = dateNowFormat.format(dateNow);
		  
		  System.out.println("timeStamp: " + timeStamp); 

		  
		  String mergedFileNm = "merged_" + timeStamp + ".pdf";
		  // Instantiating PDFMergerUtility class
		  PDFMergerUtility PDFmerger = new PDFMergerUtility();

		  // Setting the destination file
		  if("http://127.0.0.1:8080".equals(URL) || "http://localhost:8080".equals(URL)) {
			  PDFmerger.setDestinationFileName("classes/attach_files/" + mergedFileNm);
		  }else {
			  PDFmerger.setDestinationFileName("classes/attach_files/" + mergedFileNm);
		  }

		  for (File fileObj : fileList) {
		   // adding the source files
		   PDFmerger.addSource(fileObj);
		  }

		  MemoryUsageSetting setting = null;
		  // Merging the two documents
		  PDFmerger.mergeDocuments(setting);

		  
		  System.out.println("Documents merged");
		  // Closing the documents
		  int totalPage = 0;
		  for (PDDocument doc : docList) {
		   doc.close();
		   totalPage += doc.getNumberOfPages();
		  }

		  returnMap.put("fileNm", mergedFileNm);
		  returnMap.put("filePath", PDFmerger.getDestinationFileName());
		  returnMap.put("sendPages", String.valueOf(totalPage));
		  returnMap.put("sendPee", String.valueOf(totalPage*40));
		  
		  System.out.println("sendPages:" + String.valueOf(totalPage));
		  return returnMap;
	}

}
