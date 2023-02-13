package com.spigen.asos;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.spigen.asos.model.FileMgrVO;
import com.spigen.asos.model.service.S3MgrService;
import com.spire.xls.*;
import com.spire.xls.Workbook;
@Controller
public class FileMgrUtil {

	@Resource(name ="s3MgrService")
	private S3MgrService s3Service;
	
	//파일목록 추출하는 메서드
	public List<FileMgrVO> fileList() {
		
		File file = new File("C:\\file_test");
		File fileArr[] = file.listFiles(); 
		List<FileMgrVO> ls = new ArrayList<FileMgrVO>();
		
		for (int i = 0; i < fileArr.length; i++) {
			FileMgrVO vo = new FileMgrVO();
			vo.setFileNm(fileArr[i].getName());
			vo.setFileExt(fileArr[i].getName().substring(fileArr[i].getName().lastIndexOf(".")+1));
			vo.setDownFile(fileArr[i].getAbsoluteFile());
			ls.add(vo);
		}
		System.out.println("File List:" + ls);
		
		return ls;
	}
	
	
	//폴더 및 파일 경로 생성
	public static void makeDir(String makePath) {
		
		
		String path = "C:\\file_test\\시방서\\bak";
		
		File Folder = new File(path);
		System.out.println("path: " + path);
		// 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
		if (!Folder.exists()) {
			try{
			    Folder.mkdir(); //폴더 생성합니다.
			    System.out.println("폴더가 생성되었습니다.");
		        } 
		        catch(Exception e){
			    e.getStackTrace();
			}        
	    }else {
			System.out.println("이미 폴더가 생성되어 있습니다.");
		}
		
	}
	
	//파일 복사
	public static void copyFile(File sourceF, File targetF) {
		File temp = new File(targetF.getAbsolutePath() + File.separator);
		System.out.println("temp: " + temp);
		System.out.println("targetF.getAbsolutePath(): " + targetF.getAbsolutePath());
		if(sourceF.isDirectory()){
			//temp.mkdir();
			//copyFile(file, temp);
		} else {
		    FileInputStream fis = null;
			FileOutputStream fos = null;
			try {
				fis = new FileInputStream(sourceF);
				fos = new FileOutputStream(temp) ;
				byte[] b = new byte[4096];
				int cnt = 0;
				while((cnt=fis.read(b)) != -1){
					fos.write(b, 0, cnt);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				try {
					fis.close();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
			}
		}
	}
	
	//기존 파일 삭제
	public static void deleteFile(String path) {
		File folder = new File(path);
		try {
			if(folder.exists()){
			    File[] folder_list = folder.listFiles();
					
			    for (int i = 0; i < folder_list.length; i++) {
				if(folder_list[i].isFile()) {
					folder_list[i].delete();
				}else {
					deleteFile(folder_list[i].getPath());
				}
				folder_list[i].delete();
			    }
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	  

	/**
	 * 파일 저장
	 */
	public void saveUploadFiles(List<MultipartFile> files) throws IOException {
		String UPLOADED_FOLDER = "classes/attach_files/prd/";
//		String UPLOADED_FOLDER = "C:\\spigen-web\\target\\classes\\attach_files\\prd\\";
		
		System.out.println("files:" + files);
		for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename().replace("&", "_"));
            Files.write(path, bytes);
        }
	}
	
	//팩스 PDF파일 병합
	public Map<String,String> mergePDFs(String[] filePathArr, String URL, String bucketKey) throws IOException  {
		List<File> fileList = new ArrayList<File>();
		List<PDDocument> docList = new ArrayList<PDDocument>();
        Map<String,String> returnMap = new HashMap<String,String>();

		for (String filePath : filePathArr) {
			File file = null;
			PDDocument doc = null;
			String fileType = filePath.substring(filePath.lastIndexOf("."));
			String excelPath = "";
			if(".xls".equals(fileType) || ".xlsx".equals(fileType)) {
				System.out.println("type:" + fileType);
				try {
					excelPath = excelToPdf(filePath, bucketKey);
					file = new File(excelPath);
				    doc = PDDocument.load(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}else if(".pdf".equals(fileType)) {
				System.out.println("type:" + fileType);
			   file = new File(filePath);
			   try {
				   doc = PDDocument.load(file);
			   } catch (IOException e) {
				   e.printStackTrace();
			   }
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
			  PDFmerger.setDestinationFileName("classes/attach_files_2/" + mergedFileNm);
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
	
	//엑셀 pdf변환
	public String excelToPdf(String filePath, String bucketKey) throws Exception{
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		S3ObjectInputStream inputStream = null;
		OutputStream output = null; 

		 //Create a Workbook
		System.out.println("excel filePath: " + filePath);
		FileMgrVO vo = new FileMgrVO();
		vo.setFileNm(filePath);
		

		inputStream = s3Service.getMultipartFile(vo, bucketKey);
		
		System.out.println("ddddddddddddddd");
		File tmpFile = File.createTempFile(String.valueOf(inputStream.hashCode()), ".tmp");
		tmpFile.deleteOnExit();
		
		System.out.println("tmp path: " + tmpFile.getAbsolutePath());
		FileOutputStream fos = new FileOutputStream(tmpFile);
		int read;
		byte[] bytes = new byte[1024];
		
		while((read = inputStream.read(bytes)) != -1) {
			fos.write(bytes, 0 , read);
		}
		
	     Workbook workbook = new Workbook();
	
	     workbook.loadFromFile(filePath);
	
	
	     //Fit to page
	     workbook.getConverterSetting().setSheetFitToPage(true);
	     
	     String fullPath = filePath.substring(0,filePath.lastIndexOf("."))+".pdf";
	     System.out.println("=============================================="+fullPath);
	     workbook.saveToFile(fullPath,FileFormat.PDF);
	     
		return fullPath;
	
	}
	
	
}
