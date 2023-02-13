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

import com.spigen.asos.model.FTPMgrVO;

public class FTPMgrUtil {

	FTPClient ftpClient; 
	FTPClientConfig config;

/** 로컬*/	
//	String server = "172.16.3.61";
/** 개발*/	
	String server = "106.242.30.58";
	int port = 5757;
	String id = "spigenweb";
	String password = "Spigen1234!";

	public FTPMgrUtil(String server, int port, String id, String password) { 
		this.server = server; 
		this.port = port; 
		ftpClient = new FTPClient(); 
        config = new FTPClientConfig(FTPClientConfig.SYST_UNIX); 
	} 
	
	static int fromDate=0;
	static int toDate=0;
	static String fullPath="";
	static String tmpPath="";
	FTPMgrUtil ftp=null;
	
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
		ftp = new FTPMgrUtil(server, port, id, password); 

		config.setServerLanguageCode("ko");
		config.setDefaultDateFormatStr("d MMM yyyy");
		config.setRecentDateFormatStr("d MMM HH:mm");
		config.setUnparseableEntries(true);
        ftp.ftpClient.configure(config);
        ftp.ftpClient.setControlEncoding("euc-kr");
        
        
        //ftp.ftpClient.enterLocalPassiveMode();
        ftp.ftpClient.setBufferSize(1000);
		ftp.login(ftp.id, ftp.password);
		System.out.println("ls length: " + ftp.list().length);

		
		ftp.cd(fullPath);

		System.out.println("ls: " + ftp.list().toString());
		
		FTPFile[] list = ftp.list();
		
		if(vo.getNationA() != null && vo.getNationA() != "") {
			for (int h = 0; h < list.length; h++) {
				if(list[h].getName().equals(vo.getNationA()) && !"Amazon CY JP".equals(list[h].getName())) {
					System.out.println("list[h].getName(): " + list[h].getName());
					tmpPath = fullPath + "/" + list[h].getName()+ "/" + vo.getQryYear()+ "년";
					System.out.println("tmpPath: " + tmpPath);

					if(ftp.cd(tmpPath)) {
						FTPFile[] files = ftp.list(); 
						FTPFile[] subfiles; 
						String tmpPath2 = tmpPath;
						for (int i = 0; i < files.length ; i++) { 
							int fileName = Integer.parseInt(files[i].getName().substring(0,4)); 
							System.out.println("from:" + fromDate+"/to:" + toDate);
							if(fromDate<=fileName && toDate>=fileName) {
								System.out.println("files[i]:" + files[i].getName());
								
								tmpPath2 += "/" + files[i].getName();
		
								if(ftp.cd(tmpPath2)) {
									subfiles = ftp.list();
									for (int j = 0; j < subfiles.length; j++) {
										System.out.println("files[j]:" + subfiles[j].getName());
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
				}else if("Amazon CY JP".equals(list[h].getName())){
	
					int fromDate = 0;
					int toDate = 0;
					
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
					
					String fullPath2 = fullPath + "/" + list[h].getName() + "/" + vo.getQryYear() + "년";
	
					System.out.println("ddd: " + fullPath2);
					ftp.cd(fullPath2);
					
					FTPFile[] Plist = ftp.list();
					
					for (int k = 0; k < Plist.length; k++) {
						System.out.println("Plist[k].getName(): " + Plist[k].getName());
						if(list[k].isDirectory()) {
							int fileName = Integer.parseInt(Plist[k].getName().substring(0,2)); 
							System.out.println("fromDate: " + fromDate + "/toDate:" + toDate);
							System.out.println("fileName:" + fileName);
							
							if(fromDate<=fileName && toDate>=fileName){
								System.out.println("Plist[k].getName(): "+ Plist[k].getName());
								tmpPath = fullPath2 + "/" + Plist[k].getName();
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
					
					
	
				}
			}
		}else {
			//System.out.println(list[0]);
			for (int h = 0; h < list.length; h++) {
				System.out.println("======================================================"+ list[h].getName());
				if(list[h].isDirectory() && !("Amazon AU".equals(list[h].getName())||"과거 작업용 양식".equals(list[h].getName())
						                      ||"작업 종료".equals(list[h].getName())||"CSO 정산용 재고 처리".equals(list[h].getName())||"정산 Issue".equals(list[h].getName())
						                      ||"Amazon CY JP".equals(list[h].getName()))){
					System.out.println("list1:" + list[h].getName());
					if("A".equals(vo.getDocType())){
						tmpPath = fullPath + "/" + list[h].getName()+ "/" + vo.getQryYear()+ "년";
					}else if("C".equals(vo.getDocType())) {
						tmpPath = fullPath + "/" + list[h].getName()+ "/" + vo.getQryYear()+ "년도";
					}
					System.out.println("tmpPath: " + tmpPath);
					//System.out.println("--------tmpPath:" + tmpPath);
					if(ftp.cd(tmpPath)) {
						FTPFile[] files = ftp.list(); 
						FTPFile[] subfiles; 
						String tmpPath2 = tmpPath;
						for (int i = 0; i < files.length ; i++) { 
							int fileName = Integer.parseInt(files[i].getName().substring(0,4)); 
							System.out.println("from:" + fromDate+"/to:" + toDate);
							if(fromDate<=fileName && toDate>=fileName) {
								System.out.println("files[i]:" + files[i].getName());
								
								tmpPath2 += "/" + files[i].getName();
		
								if(ftp.cd(tmpPath2)) {
									subfiles = ftp.list();
									for (int j = 0; j < subfiles.length; j++) {
										System.out.println("files[j]:" + subfiles[j].getName());
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
				}else if("Amazon CY JP".equals(list[h].getName())){
	
					int fromDate = 0;
					int toDate = 0;
					
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
					
					String fullPath2 = fullPath + "/" + list[h].getName() + "/" + vo.getQryYear() + "년";
	
					System.out.println("ddd: " + fullPath2);
					ftp.cd(fullPath2);
					
					FTPFile[] Plist = ftp.list();
					
					for (int k = 0; k < Plist.length; k++) {
						System.out.println("Plist[k].getName(): " + Plist[k].getName());
						if(list[k].isDirectory()) {
							int fileName = Integer.parseInt(Plist[k].getName().substring(0,2)); 
							System.out.println("fromDate: " + fromDate + "/toDate:" + toDate);
							System.out.println("fileName:" + fileName);
							
							if(fromDate<=fileName && toDate>=fileName){
								System.out.println("Plist[k].getName(): "+ Plist[k].getName());
								tmpPath = fullPath2 + "/" + Plist[k].getName();
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
				}
				tmpPath ="";
			} //for문
		}//if문
		
		//ftp.logout(); 
		//ftp.disconnect(); 
		//System.exit(1);
		
		return resultList; 
	}
	
	//파워아크 파일리스트
	public List<FTPMgrVO> getPFileList(FTPMgrVO vo) {
		

		List<FTPMgrVO> resultList = new ArrayList<FTPMgrVO>();
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
		
		ftp = new FTPMgrUtil(server, port, id, password); 

		config.setServerLanguageCode("ko");
		config.setDefaultDateFormatStr("d MMM yyyy");
		config.setRecentDateFormatStr("d MMM HH:mm");
		config.setUnparseableEntries(true);
        ftp.ftpClient.configure(config);
        ftp.ftpClient.setControlEncoding("euc-kr");
        
        
        //ftp.ftpClient.enterLocalPassiveMode();
        ftp.ftpClient.setBufferSize(1000);
		ftp.login(ftp.id, ftp.password);
		
		fullPath += "/" + vo.getQryYear() + "년";

		System.out.println("ddd: " + fullPath);
		ftp.cd(fullPath);
		
		FTPFile[] list = ftp.list();
		
		for (int h = 0; h < list.length; h++) {
			System.out.println(list[h].getName());
			if(list[h].isDirectory()) {
				int fileName = Integer.parseInt(list[h].getName().substring(0,2)); 
				System.out.println("fromDate: " + fromDate + "/toDate:" + toDate);
				System.out.println("fileName:" + fileName);
				
				
				if(fromDate<=fileName && toDate>=fileName){
					System.out.println("list[h].getName(): "+ list[h].getName());
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
		
		//ftp.logout(); 
		//ftp.disconnect(); 
		//System.exit(1);
		
		return resultList; 
	}

	
	// 계정과 패스워드로 로그인 
	public boolean login(String user, String password) { 
		try { 
			this.connect(); 
			ftpClient.enterLocalPassiveMode();
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
			files = this.ftpClient.listFiles(); 
			return files; 
			} catch (IOException ioe) { 
				ioe.printStackTrace(); 
			} return null; 
	} 
	

	
	// 파일을 전송 받는다 
	public File get(String source, String target, String fileName) throws IOException {
		System.out.println("target:" + target);
		//FTPMgrUtil ftp = new FTPMgrUtil(server, port, id, password); 
		config.setServerLanguageCode("ko");
		config.setDefaultDateFormatStr("d MMM yyyy");
		config.setRecentDateFormatStr("d MMM HH:mm");
		config.setUnparseableEntries(true);
//        ftp.ftpClient.configure(config);
//        ftp.ftpClient.setControlEncoding("euc-kr");
        
        //ftp.ftpClient.enterLocalPassiveMode();
        //ftp.login(ftp.id, ftp.password);
		

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
				//ftp.logout(); 
				//ftp.disconnect(); 
				if(output!=null) {
					output.close();
				}
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
			//로컬
			System.out.println("filePathArr: " + filePathArr[i]);
			//file = get("C:\\Temp", filePathArr[i].replaceAll( "\\^","\\'"), fileNameArr[i].replaceAll( "\\^","\\'"));
		    file = get("classes/attach_files/", filePathArr[i].replaceAll( "\\^","\\'"), fileNameArr[i].replaceAll( "\\^","\\'"));
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
			  PDFmerger.setDestinationFileName("C://Temp/" + mergedFileNm);
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
