package com.spigen.asos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.spigen.asos.model.FTPMgrVO;


public class SFTPMgrUtil{

    private Session session = null;
    private Channel channel = null;
    private ChannelSftp channelSftp = null;

    public void init(String host, String userName, String password, int port) throws SftpException, JSchException {
    	System.out.println("init....");
		 Session session = null; //com.jcraft.jsch.Session 참조함
		 
	     // 1. JSch 객체를 생성한다.
	     JSch jsch = new JSch();
	  
	  
	     // 2. 세션 객체를 생성한다 (사용자 이름, 접속할 호스트, 포트를 인자로 준다.)
	     session = jsch.getSession("root", "13.125.137.202", 22);
	  
	  
	     // 3. 패스워드를 설정한다.
	     session.setPassword("3!wjfG0!");
	  
	          
	     // 4. 세션과 관련된 정보를 설정한다.
	     java.util.Properties config = new java.util.Properties();
	  
	  
	     // 4-1. 호스트 정보를 검사하지 않는다.
	     config.put("StrictHostKeyChecking", "no");
	     session.setConfig(config);

        try {

        	 //5. 접속한다.      
            session.connect();
            
            // 6. sftp 채널을 연다.
            Channel channel = session.openChannel("sftp");
            ChannelSftp channelSftp = (ChannelSftp) channel;
            channelSftp.connect();
            
        } catch (JSchException e) {
            e.printStackTrace();
        }

        channelSftp = (ChannelSftp) channel;

    }


    /**
     * 인자로 받은 경로의 파일 리스트를 리턴한다.
     * @param path
     * @return
     */
    public Vector<ChannelSftp.LsEntry> getFileList(String path) {
    	
    	Vector<ChannelSftp.LsEntry> list = null;
    	try {
    		channelSftp.cd(path);
    		//System.out.println(" pwd : " + channelSftp.pwd());
    		list = channelSftp.ls(".");
		} catch (SftpException e) {
			e.printStackTrace();
			return null;
		}
    	
    	return list;
    }

    /**
     * 하나의 파일을 업로드 한다.
     *
     * @param dir
     *            저장시킬 주소(서버)
     * @param file
     *            저장할 파일
     */

    public void upload(String dir, File file) {


        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            channelSftp.cd(dir);
            channelSftp.put(in, file.getName());
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    /**
     * 하나의 파일을 다운로드 한다.
     *
     * @param dir
     *            저장할 경로(서버)
     * @param downloadFileName
     *            다운로드할 파일
     * @param path
     *            저장될 공간
     */

    public File download(String dir, String downloadFileName, String path) {

        InputStream in = null;
        FileOutputStream out = null;
        try {
            channelSftp.cd(dir);
            in = channelSftp.get(downloadFileName);

        } catch (SftpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            out = new FileOutputStream(new File(path));
            int i;

            while ((i = in.read()) != -1) {
                out.write(i);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		return null;
    }


    /**
     * 서버와의 연결을 끊는다.
     */
    public void disconnection() {
        channelSftp.quit();
    }

	static int fromDate=0;
	static int toDate=0;
	static String fullPath="";
	static String tmpPath="";
	
	public List<FTPMgrVO> getFileList(FTPMgrVO vo) {
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
		
		try {
			channelSftp.cd(fullPath);
			Vector<LsEntry> list = getFileList(fullPath);
			
			
			for (int h = 0; h < list.size(); h++) {
				if(list.get(h).getAttrs().isDir() && !("Amazon AU".equals(list.get(h).getFilename())||"과거 작업용 양식".equals(list.get(h).getFilename())
						                      ||"작업 종료".equals(list.get(h).getFilename())||"CSO 정산용 재고 처리".equals(list.get(h).getFilename())||"정산 Issue".equals(list.get(h).getFilename()))){
					if("A".equals(vo.getDocType())){
						tmpPath = fullPath + "/" + list.get(h).getFilename()+ "/" + vo.getQryYear()+ "년";
					}else if("C".equals(vo.getDocType())) {
						tmpPath = fullPath + "/" + list.get(h).getFilename()+ "/" + vo.getQryYear()+ "년도";
					}
						Vector<LsEntry> files = getFileList(tmpPath); 
						Vector<LsEntry> subfiles; 
						String tmpPath2 = tmpPath;
						for (int i = 0; i < files.size() ; i++) { 
							int fileName = Integer.parseInt(files.get(i).getFilename().substring(0,4)); 
							if(fromDate<=fileName && toDate>=fileName) {
								
								tmpPath2 += "/" + files.get(i).getFilename();
		
								subfiles = getFileList(tmpPath2);
								for (int j = 0; j < subfiles.size(); j++) {
									if("pdf".equals(subfiles.get(j).getFilename().substring(subfiles.get(j).getFilename().lastIndexOf(".") + 1))) {
										
										FTPMgrVO returnVo = new FTPMgrVO();
										returnVo.setFileNm(subfiles.get(j).getFilename());
										returnVo.setFilePath(tmpPath2 + "/" + subfiles.get(j).getFilename());
										returnVo.setFileType("pdf");
										resultList.add(returnVo);
									}
								}
								tmpPath2 = tmpPath;
								
								subfiles = getFileList(tmpPath2);
							}
						} 
				}
				tmpPath ="";
			}
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		disconnection();
		
		return resultList; 
	}
	
//	//파워아크 파일리스트
//	public List<FTPMgrVO> getPFileList(FTPMgrVO vo) {
//		List<FTPMgrVO> resultList = new ArrayList<FTPMgrVO>();
//		String subYear = vo.getQryYear().substring(2, 4);
//		switch (vo.getQryQuarter()) {
//		case "1":
//			fromDate = Integer.parseInt("01");
//			toDate = Integer.parseInt("03");
//			break;
//		case "2":
//			fromDate = Integer.parseInt("04");
//			toDate = Integer.parseInt("06");
//			break;
//		case "3":
//			fromDate = Integer.parseInt("07");
//			toDate = Integer.parseInt("09");
//			break;
//		case "4":
//			fromDate = Integer.parseInt("10");
//			toDate = Integer.parseInt("12");
//			break;
//		}
//		
//		fullPath = "/Maetel_ovd_f/해외사업팀 Maetel Server/07. 사업기획팀/02. 매출정산/3. Amazon_PowerArc";
//		
//		FTPMgrUtil ftp = new FTPMgrUtil(server, port, id, password); 
//		ftp.ftpClient.setControlEncoding("euc-kr");
//		ftp.connect(); 
//		ftp.login(ftp.id, ftp.password);
//		
//		ftp.cd(fullPath);
//		FTPFile[] list = ftp.list();
//		
//		for (int h = 0; h < list.length; h++) {
//			if(list[h].isDirectory()) {
//				System.out.println(list[h].getName().substring(0,2));
//				int fileName = Integer.parseInt(list[h].getName().substring(0,2)); 
//				if(fromDate<=fileName && toDate>=fileName){
//					tmpPath = fullPath + "/" + list[h].getName();
//					if(ftp.cd(tmpPath)) {
//						FTPFile[] files = ftp.list(); 
//						for (int i = 0; i < files.length ; i++) { 
//							if("pdf".equals(files[i].getName().substring(files[i].getName().lastIndexOf(".") + 1))) {
//								
//								FTPMgrVO returnVo = new FTPMgrVO();
//								returnVo.setFileNm(files[i].getName().replaceAll("\\'", "\\^"));
//								returnVo.setFilePath(tmpPath + "/" + files[i].getName().replaceAll("\\'", "\\^"));
//								returnVo.setFileType("pdf");
//								resultList.add(returnVo);
//							}
//						} 
//					}
//				}
//			}
//			tmpPath ="";
//		}
//		
//		ftp.logout(); 
//		ftp.disconnect(); 
//		//System.exit(1);
//		
//		return resultList; 
//	}
//
//	
//	// 서버로부터 로그아웃 
//	private boolean logout() {
//		try { 
//			return ftpClient.logout(); 
//			} catch (IOException ioe) { 
//				ioe.printStackTrace(); 
//			} return false; 
//	} 
//	// 서버로 연결 
//	public void connect() {
//		try { 
//			System.out.println("server: " + server + "/port: " + port );
//			ftpClient.connect(server, port); 
//			int reply; // 연결 시도후, 성공했는지 응답 코드 확인 
//			reply = ftpClient.getReplyCode(); 
//			if(!FTPReply.isPositiveCompletion(reply)) { 
//				ftpClient.disconnect(); 
//				System.err.println("서버로부터 연결을 거부당했습니다"); 
//				//System.exit(1); 
//				}else {
//					System.out.println("연결 성공..."); 
//				}
//			} catch (IOException ioe) { 
//				if(ftpClient.isConnected()) { 
//					try { 
//						ftpClient.disconnect(); 
//						} catch(IOException f) { 
//							// 
//						} 
//				} 
//				System.err.println("서버에 연결할 수 없습니다"); 
//				//System.exit(1); 
//				} 
//		} 
//	
//	// FTP의 ls 명령, 모든 파일 리스트를 가져온다 
//	public FTPFile[] list() { 
//		FTPFile[] files = null; 
//		try { 
//			files = this.ftpClient.listFiles(); 
//			return files; 
//			} catch (IOException ioe) { 
//				ioe.printStackTrace(); 
//			} return null; 
//	} 
//	
//
//	
//	// 파일을 전송 받는다 
//	public File get(String source, String target, String fileName) {
//		System.out.println("target:" + target);
//		FTPMgrUtil ftp = new FTPMgrUtil(server, port, id, password); 
//		ftp.ftpClient.setControlEncoding("euc-kr");
//		ftp.connect(); 
//		ftp.login(ftp.id, ftp.password);
//		OutputStream output = null; 
//		try {
//			File local = new File(source, fileName); 
//			output = new FileOutputStream(local); 
//			} catch (FileNotFoundException fnfe) { 
//				fnfe.printStackTrace(); 
//			} 
//			File file = new File(source,fileName); 
//				try { 
//					if (ftp.ftpClient.retrieveFile(target, output)) { 
//						return file; 
//					} 
//				} catch (IOException ioe) { 
//					ioe.printStackTrace(); 
//				} 
//				ftp.logout(); 
//				ftp.disconnect(); 
//				return null; 
//			} 
//
//	// 서버 디렉토리 이동 
//	public boolean cd(String path) { 
//		boolean flag = false;
//		try { 
//			flag = ftp.changeWorkingDirectory(path); 
//			} catch (IOException ioe) { 
//				flag = false;
//				ioe.printStackTrace(); 
//				} 
//			return flag;
//		} 
//	
//	// 서버로부터 연결을 닫는다
//	private void disconnect() { 
//		try { 
//			ftpClient.disconnect(); 
//			} catch (IOException ioe) { 
//				ioe.printStackTrace(); 
//			} 
//	}


	public Map<String, String> mergePDFsFTP(String[] filePathArr, String[] fileNameArr, String URL) throws IOException {
		List<File> fileList = new ArrayList<File>();
		List<PDDocument> docList = new ArrayList<PDDocument>();
        Map<String,String> returnMap = new HashMap<String,String>();

		for (int i = 0; i < filePathArr.length; i++) {
			
			File file = null;
			PDDocument doc = null;
		    file = download(filePathArr[i].replaceAll( "\\^","\\'"), fileNameArr[i].replaceAll( "\\^","\\'"), "C:\\Temp");
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
		  if("http://127.0.0.1:8808".equals(URL) || "http://localhost:8808".equals(URL)) {
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




