package com.spigen.asos.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.codehaus.groovy.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.popbill.api.FaxService;
import com.spigen.asos.FTPMgrUtil;
import com.spigen.asos.FileMgrUtil;
import com.spigen.asos.PaginationInfo;
import com.spigen.asos.PrintUtilApplication;
import com.spigen.asos.model.CmMgrVO;
import com.spigen.asos.model.DocMgrVO;
import com.spigen.asos.model.FTPMgrVO;
import com.spigen.asos.model.FileMgrVO;
import com.spigen.asos.model.service.CmMgrService;
import com.spigen.asos.model.service.S3MgrService;
import com.spigen.asos.model.service.TradeDocMgrService;
import com.spire.xls.FileFormat;
import com.spire.xls.Workbook;

@Controller
public class TradeDocMgrController {
	
	private static final Logger logger = LoggerFactory.getLogger(TradeDocMgrController.class);

	/** tradeDocMgrService*/
	@Resource(name ="tradeDocMgrService")
	private TradeDocMgrService tradeDocMgrService;
	
	/** s3MgrService*/
	@Resource(name ="s3MgrService")
	private S3MgrService s3Service;
	
	/** cmMgrService*/
	@Resource(name ="cmMgrService")
	private CmMgrService cmMgrService;
	
	//팝빌
	@Autowired
	private FaxService faxService;
	
    FTPMgrUtil util; 
	
	@RequestMapping(path = "/file", method = RequestMethod.GET)
	public ModelAndView fileMgrScreen(FileMgrVO vo) throws Exception{
		
		ModelAndView model = new ModelAndView();
		
		List<FileMgrVO> resultList = cmMgrService.selectClntList(vo);
		
		//@RequestParam String liIndex
		model.setViewName("documentPrint");
		model.addObject("resultList",resultList); 
		model.addObject("liIndex", "nav1");
		
		return model;
	}
	
	@RequestMapping("/file/selectTaxFileMgrListQry") @ResponseBody
	public ModelMap selectTaxFileMgrListQry(FileMgrVO vo, HttpServletRequest req) throws Exception{

		//나중에 제거
		String uri = req.getRequestURI().toString();
		String url = req.getRequestURL().toString();
		
		vo.setReqURL(url.replace(uri, ""));
		//
		
		PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(vo.getPageIndex());
        paginationInfo.setRecordCountPerPage(vo.getPageUnit());
        paginationInfo.setPageSize(vo.getPageSize());
        vo.setFirstIndex(paginationInfo.getFirstRecordIndex());
        vo.setLastIndex(paginationInfo.getLastRecordIndex());
        vo.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
	        
		ModelMap model = new ModelMap();
		
		//SKU 복수 조회
	    List<String> kunnrRLs = new ArrayList<String>();
	     
	    if(vo.getKunnrR()!=null) {
	    	
	    	String[] kunnrRArr = vo.getKunnrR().split(",");
	    	
	    	for(int i=0; i< kunnrRArr.length; i++){
	    		kunnrRLs.add(kunnrRArr[i].toString());
	    	}
	    	vo.setQryLs(kunnrRLs);
	    }
	    
		List<FileMgrVO> resultList = tradeDocMgrService.selectTaxFileMgrListQry(vo);
		
		if(resultList.size()>0) {
			paginationInfo.setTotalRecordCount(resultList.get(0).getCnt());
		}else {
		    paginationInfo.setTotalRecordCount(0);
		}
		model.addAttribute("paginationInfo", paginationInfo);				
		model.addAttribute("resultList", resultList);
		
		return model;
	}
	
	@RequestMapping("/file/fileMgrListQry") @ResponseBody
	public ModelMap fileMgrListQry(FileMgrVO vo, HttpServletRequest req) throws Exception{
		ModelMap model = new ModelMap();
		
		//나중에 제거
		String uri = req.getRequestURI().toString();
		String url = req.getRequestURL().toString();
		
		vo.setReqURL(url.replace(uri, ""));
		//
		
		//재전송일 경우 기존 접수번호 끌고오기?? 아니면 그냥 똑같이 전송? 어떻게 할지 결정하기
		
		PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(vo.getPageIndex());
        paginationInfo.setRecordCountPerPage(vo.getPageUnit());
        paginationInfo.setPageSize(vo.getPageSize());
        vo.setFirstIndex(paginationInfo.getFirstRecordIndex());
        vo.setLastIndex(paginationInfo.getLastRecordIndex());
        vo.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        
		List<FileMgrVO> resultList = tradeDocMgrService.selectFileMgrListQry(vo);
		
		if(resultList.size()>0) {
			paginationInfo.setTotalRecordCount(resultList.get(0).getCnt());
		}else {
		    paginationInfo.setTotalRecordCount(0);
		}
		model.addAttribute("paginationInfo", paginationInfo);		
		model.addAttribute("resultList", resultList);
		
		return model;
	}
	
	@RequestMapping("/file/fileViewer") @ResponseBody
	public void fileViewer(HttpServletResponse response, FileMgrVO vo) throws Exception{
		ModelMap model = new ModelMap();
		
		FileInputStream fis = null;
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;

		try {
		
		String fileName = vo.getFilePath();
		System.out.println("fileName:" + vo.getFileNm());
		System.out.println("filePath:" + fileName);

		File file = new File(fileName);

		
		if("pdf".equals(vo.getFileType())||"PDF".equals(vo.getFileType())) {
			//클라이언트 브라우져에서 바로 보는 방법(헤더 변경)
			response.setContentType("application/pdf");
		}else {
			//다운로드
			response.addHeader("Content-Disposition", "attachment; filename="+vo.getFileNm());
		}


		//파일 읽고 쓰는 건 일반적인 Write방식이랑 동일합니다. 다만 reponse 출력 스트림 객체에 write.
		fis = new FileInputStream(file);
		bis = new BufferedInputStream(fis);

		int size = bis.available(); //지정 파일에서 읽을 수 있는 바이트 수를 반환
		byte[] buf = new byte[size]; //버퍼설정
		int readCount = bis.read(buf);

		response.flushBuffer();
		bos = new BufferedOutputStream(response.getOutputStream());
		bos.write(buf, 0, readCount);
		//bos.flush();

		} catch(Exception e) {
		e.printStackTrace();
		} finally {
		try {
		if (fis != null) fis.close(); //close는 꼭! 반드시!
		if (bis != null) bis.close(); //close는 꼭! 반드시!
		if (bos != null) bos.close();
		} catch (IOException e) {
		e.printStackTrace();
		}
		}

	}
	
	@RequestMapping("/file/fileViewerS3") @ResponseBody
	public void fileViewerS3(HttpServletResponse response, FileMgrVO vo) throws Exception{
		ModelMap model = new ModelMap();
		
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		S3ObjectInputStream inputStream = null;

		try {
		
		String fileName = vo.getFilePath();
		System.out.println("fileName2:" + vo.getFileNm());
		System.out.println("filePath2:" + fileName);

		
		if("pdf".equals(vo.getFileType())||"PDF".equals(vo.getFileType())) {
			//클라이언트 브라우져에서 바로 보는 방법(헤더 변경)
			response.setContentType("application/pdf");
		}else {
			//다운로드
			response.addHeader("Content-Disposition", "attachment; filename="+vo.getFileNm());
		}
		
		String path ="";
		if("P".equals(vo.getDocType())) {
			//path="/trade/sis";
			path="/trade/sis";
		}else {
			String filePath = vo.getFilePath().substring(0,vo.getFilePath().lastIndexOf("/"));
			String fileNm = vo.getFilePath().substring(vo.getFilePath().lastIndexOf("/")+1);
			System.out.println("filePath: " + filePath + " / fileNm: " + fileNm);
			path="/trade/unidocu/" + filePath;
			vo.setFileNm(fileNm);
		}
		
		vo.setQryType("T");
		inputStream = s3Service.getMultipartFile(vo, path);
		
		//OutputStream outputStream = new BufferedOutputStream(new FileOutputStream("/attach_files/tmp.pdf"));
		
		bis = new BufferedInputStream(inputStream);
		
		int size = bis.available(); //지정 파일에서 읽을 수 있는 바이트 수를 반환
		byte[] buf = new byte[size]; //버퍼설정
	    int readCount = -1;

	    response.flushBuffer();
	    bos = new BufferedOutputStream(response.getOutputStream());

	    while ((readCount = inputStream.read(buf)) != -1) {
	    	bos.write(buf, 0, readCount);
	    	//bos.flush();
	    }
		
		
		} catch(Exception e) {
		e.printStackTrace();
		} finally {
			try {
				if (bis != null) bis.close(); //close는 꼭! 반드시!
				if (bos != null) bos.close();
				if (inputStream != null) inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	@RequestMapping("/file/printFile") @ResponseBody
	public ModelMap printFile(FileMgrVO vo, HttpServletRequest req) throws Exception{

//		PrintUtilApplication printApp = new PrintUtilApplication();
//		int successFlag = printApp.printFiles(vo);
		
		ModelMap model = new ModelMap();
		
        String[] filePathArr = vo.getFilePath().split(",");
		//FileMgrUtil util = new FileMgrUtil();
		
		//나중에 제거
		
		String uri = req.getRequestURI().toString();
		String url = req.getRequestURL().toString();
		
		System.out.println("URL:" + url.replace(uri, ""));
		//
		
		//Map<String,String> returnedMap = util.mergePDFs(vo, url.replace(uri, ""), "/trade/sis");
		Map<String,String> returnedMap = mergePDFs(filePathArr, url.replace(uri, ""), "/trade/sis");

		String filePath = returnedMap.get("filePath").replaceAll("\\\\", "\\/");
		
		model.addAttribute("filePath", filePath);
		model.addAttribute("reqURL", url.replace(uri, ""));
		
		return model;
	}
	
	//1페이지 프린트
	@RequestMapping("/file/printFileFstPage") @ResponseBody
	public ModelMap printFileFstPage(FileMgrVO vo, HttpServletRequest req) throws Exception{
		
		ModelMap model = new ModelMap();
		
		String[] filePathArr = vo.getFilePath().split(",");
		
		String uri = req.getRequestURI().toString();
		String url = req.getRequestURL().toString();
		
		System.out.println("URL:" + url.replace(uri, ""));
		
		Map<String,String> returnedMap = mergePDFsFstPage(filePathArr, url.replace(uri, ""), "/trade/sis");
		
		String filePath = returnedMap.get("filePath").replaceAll("\\\\", "\\/");
		
		model.addAttribute("filePath", filePath);
		model.addAttribute("reqURL", url.replace(uri, ""));
		
		return model;
	}
	
	//은행 정보 조회 화면 호출
	@RequestMapping(path = "/bankInfo", method = RequestMethod.GET)
	public ModelAndView bankInfoScreen() throws Exception{
		
		ModelAndView model = new ModelAndView();
		//@RequestParam String liIndex
		//System.out.println("liIndex: " + liIndex);
		model.setViewName("bankInfoList");
		model.addObject("liIndex", "nav5");
		
		return model;
	}
	
	//은행 정보 조회
	@RequestMapping("/bankInfo/bankInfoListQry") @ResponseBody
	public ModelMap bankInfoListQry(FileMgrVO vo) throws Exception{
		ModelMap model = new ModelMap();
		
		List<DocMgrVO> resultList = tradeDocMgrService.selectBankInfoList(vo);
				
		model.addAttribute("resultList", resultList);
		
		return model;
	}

	//은행정보 등록 팝업 호출
	@RequestMapping(path = "/bankInfo/bankInfoRgtScreen", method = RequestMethod.GET)
	public ModelAndView bankInfoRgtScreen() throws Exception{
		
		ModelAndView model = new ModelAndView();
		
		model.setViewName("popup/bankInfoRgtPopup");
		
		
		return model;
	}
	
	//은행 정보 등록
	@RequestMapping("/bankInfo/bankInfoRgt") @ResponseBody
	public ModelMap bankInfoRgt(FileMgrVO vo) throws Exception{
		ModelMap model = new ModelMap();
		
		int result = tradeDocMgrService.insertBankInfo(vo);
				
		model.addAttribute("resultList", result);
		
		return model;
	}
	
	//은행정보 수정 팝업 호출
	@RequestMapping(path = "/bankInfo/bankInfoUpdScreen", method = RequestMethod.GET)
	public ModelAndView bankInfoUpdScreen(FileMgrVO vo) throws Exception{
		
		ModelAndView model = new ModelAndView();
		
		List<DocMgrVO> resultList = tradeDocMgrService.selectBankInfoList(vo);
		
		model.addObject("resultList", resultList);
		
		model.setViewName("popup/bankInfoUpdPopup");
		
		
		return model;
	}
	
	//은행 정보 수정
	@RequestMapping("/bankInfo/bankInfoUpd") @ResponseBody
	public ModelMap bankInfoUpd(FileMgrVO vo) throws Exception{
		ModelMap model = new ModelMap();
		
		int result = tradeDocMgrService.updateBankInfo(vo);
		
		model.addAttribute("resultList", result);
		
		return model;
	}
	
	//수신처 지정 팝업 호출
	@RequestMapping(path = "/bankInfo/destinationSelectPopupScreen", method = RequestMethod.GET)
	public ModelAndView destinationSelectPopupScreen(FileMgrVO vo) throws Exception{
		
		ModelAndView model = new ModelAndView();
		
		vo.setUseYn("Y");
		List<DocMgrVO> resultList = tradeDocMgrService.selectBankInfoList(vo);
		
		Map <String,String> faxMap = new HashMap<String,String>();
		faxMap.put("point", String.valueOf((int)faxService.getPartnerBalance("1208736593")));
		//faxMap.put("pointURL", faxService.getPartnerURL("1208736593", "CHRG"));
		
		model.addObject("resultList", resultList);
		model.addObject("fax", faxMap);
		model.addObject("dracBicNm", vo.getDracBicNm());
		
		model.setViewName("popup/destinationSelectPopup");
		
		return model;
	}
	
	//발신번호 관리 팝업 호출
	@RequestMapping(path = "/bankInfo/getSenderNumberMgtURL") @ResponseBody
	public ModelMap getSenderNumberMgtURL() throws Exception{
		
		ModelMap model = new ModelMap();
		
		String URL = faxService.getSenderNumberMgtURL("1208736593", "spigenkorea");
		
		System.out.println("URL: " + URL);
		
		model.put("numMgrURL", URL);
		
		return model;
	}
	
	//포인트 충전 팝업 호출
	@RequestMapping(path = "/fax/chargePointURL") @ResponseBody
	public ModelMap chargePointURL() throws Exception{
		CmMgrVO cmMgrVO = new CmMgrVO();

		ModelMap model = new ModelMap();
		
		String URL = faxService.getPartnerURL(cmMgrVO.getCorpNum(), "CHRG");
		
		System.out.println("URL: " + URL);
		
		model.put("chargePointURL", URL);
		
		return model;
	}
	
	//해외사업 매출정산 자료 화면
	@RequestMapping(path = "/fileAmazon", method = RequestMethod.GET)
	public ModelAndView fileAmazonMgrScreen() throws Exception{
		
		ModelAndView model = new ModelAndView();
		//@RequestParam String liIndex
		model.setViewName("documentPrintAmazon");
		model.addObject("liIndex", "nav1");
		
		return model;
	}
	
	//해외사업 매출정산 자료 조회
	@RequestMapping("/fileAmazon/selectAmazonRptMgrListQry") @ResponseBody
	public ModelMap selectAmazonRptMgrListQry(FTPMgrVO vo) throws Exception{

		ModelMap model = new ModelMap();
		util = new FTPMgrUtil(vo.getServer(), vo.getPort(), vo.getId(), vo.getPassword());
		
		List<FTPMgrVO> resultList;
		
		if("P".equals(vo.getDocType())){
			resultList = util.getPFileList(vo);
		}else {
			resultList = util.getFileList(vo);
		}
		model.addAttribute("resultList", resultList);
		
		return model;
	}
	
	@RequestMapping("/fileAmazon/ftpFileViewer") @ResponseBody
	public void ftpFileViewer(HttpServletResponse response, HttpServletRequest req,FTPMgrVO vo) throws Exception{
		ModelMap model = new ModelMap();
		
		FileInputStream fis = null;
		BufferedOutputStream bos = null;

		try {
		
		String fileName = vo.getFilePath();
		System.out.println("fileName:" + vo.getFileNm());
		System.out.println("filePath:" + fileName);

		
		//로컬
		//File file =util.get("C:\\Temp", vo.getFilePath().replaceAll( "\\^","\\'"), vo.getFileNm().replaceAll( "\\^","\\'"));
		File file =util.get("classes/attach_files/", vo.getFilePath().replaceAll( "\\^","\\'"), vo.getFileNm().replaceAll( "\\^","\\'"));

		if("pdf".equals(vo.getFileType())) {
			//클라이언트 브라우져에서 바로 보는 방법(헤더 변경)

			response.setContentType("application/pdf;charset=utf-8");
		}else {
			//다운로드
			response.addHeader("Content-Disposition", "attachment; filename="+vo.getFileNm());
		}


		//파일 읽고 쓰는 건 일반적인 Write방식이랑 동일합니다. 다만 reponse 출력 스트림 객체에 write.
		fis = new FileInputStream(file);
		int size = fis.available(); //지정 파일에서 읽을 수 있는 바이트 수를 반환
		byte[] buf = new byte[size]; //버퍼설정
		int readCount = fis.read(buf);

		response.flushBuffer();
		bos = new BufferedOutputStream(response.getOutputStream());
		bos.write(buf, 0, readCount);
		//bos.flush();

		} catch(Exception e) {
		e.printStackTrace();
		} finally {
		try {
		if (fis != null) fis.close(); //close는 꼭! 반드시!
		if (bos != null) bos.close();
		} catch (IOException e) {
		e.printStackTrace();
		}
		}

	}
	
	@RequestMapping("/fileAmazon/ftpPrintFile") @ResponseBody
	public ModelMap ftpPrintFile(FTPMgrVO vo, HttpServletRequest req) throws Exception{

		
		ModelMap model = new ModelMap();
		
        String[] filePathArr = vo.getFilePath().split(",");
        String[] fileNameArr = vo.getFileNm().split(",");
		
		//나중에 제거
		
		String uri = req.getRequestURI().toString();
		String url = req.getRequestURL().toString();
		
		System.out.println("URL:" + url.replace(uri, ""));
		//
		
		Map<String,String> returnedMap = util.mergePDFsFTP(filePathArr, fileNameArr, url.replace(uri, ""));

		String filePath = returnedMap.get("filePath").replaceAll("\\\\", "\\/");
		
		model.addAttribute("filePath", filePath);
		model.addAttribute("reqURL", url.replace(uri, ""));
		
		return model;
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
			String pdfPath = "";
			String pngPath = "";
			if(".xls".equals(fileType) || ".xlsx".equals(fileType)) {
				System.out.println("type1:" + fileType);
				try {
					excelPath = excelToPdf(filePath, bucketKey);
					System.out.println("return1: " + excelPath);
					file = new File(excelPath);
				    doc = PDDocument.load(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}else if(".pdf".equals(fileType) ||".PDF".equals(fileType)) {
				System.out.println("type2:" + fileType);
			   try {
				   pdfPath = getPdfFiles(filePath, bucketKey);
				   System.out.println("return2: " + pdfPath);
				   file = new File(pdfPath);
				   doc = PDDocument.load(file);
			   } catch (Exception e) {
				   e.printStackTrace();
			   }
			}else if(".png".equals(fileType)) {
			   try {
				   pngPath = getPngFiles(filePath, bucketKey);

				   System.out.println("return2: " + pngPath);
				   file = new File(pngPath);
				   doc = PDDocument.load(file);
			   } catch (Exception e) {
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
		  if("http://127.0.0.1:8080".equals(URL) || "http://localhost:8080".equals(URL)) {
			  PDFmerger.setDestinationFileName("C:\\Users\\User\\AppData\\Local\\Temp\\" + mergedFileNm);
		  }else {
			  PDFmerger.setDestinationFileName("classes/attach_files/" + mergedFileNm);
		  }

		  for (File fileObj : fileList) {
			  System.out.println("fileObj: "+ fileObj);
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
	
	//팩스 PDF파일 병합(1페이지)
	public Map<String,String> mergePDFsFstPage(String[] filePathArr, String URL, String bucketKey) throws IOException  {
		List<File> fileList = new ArrayList<File>();
		List<PDDocument> docList = new ArrayList<PDDocument>();
		Map<String,String> returnMap = new HashMap<String,String>();
		
		for (String filePath : filePathArr) {
			File file = null;
			PDDocument doc = null;
			Splitter splitter = new Splitter();
			String fileType = filePath.substring(filePath.lastIndexOf("."));
			String excelPath = "";
			String pdfPath = "";
			String pngPath = "";

			if(".xls".equals(fileType) || ".xlsx".equals(fileType)) {
				System.out.println("type1:" + fileType);
				try {
					excelPath = excelToPdf(filePath, bucketKey);
					System.out.println("return1: " + excelPath);
					file = new File(excelPath);
					doc = PDDocument.load(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}else if(".pdf".equals(fileType)||".PDF".equals(fileType)) {
				System.out.println("type2:" + fileType);
				try {
					pdfPath = getPdfFiles(filePath, bucketKey);
					System.out.println("return2: " + pdfPath);
					file = new File(pdfPath);
					doc = PDDocument.load(file);
					
					List<PDDocument> pages = splitter.split(doc);
					doc = pages.get(0);
					doc.save(new File(pdfPath));
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(".png".equals(fileType)) {
				   try {
					   pngPath = getPngFiles(filePath, bucketKey);

					   System.out.println("return2: " + pngPath);
					   file = new File(pngPath);
					   doc = PDDocument.load(file);
				   } catch (Exception e) {
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
		if("http://127.0.0.1:8080".equals(URL) || "http://localhost:8080".equals(URL)) {
			PDFmerger.setDestinationFileName("C:\\Users\\User\\AppData\\Local\\Temp\\" + mergedFileNm);
		}else {
			PDFmerger.setDestinationFileName("classes/attach_files/" + mergedFileNm);
		}
		
		for (File fileObj : fileList) {
			System.out.println("fileObj: "+ fileObj);
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

		S3ObjectInputStream inputStream = null;

		 //Create a Workbook
		System.out.println("excel filePath: " + filePath);
		FileMgrVO vo = new FileMgrVO();
		vo.setFileNm(filePath);
		
		vo.setQryType("T");
		inputStream = s3Service.getMultipartFile(vo, bucketKey);
		
		File tmpFile = File.createTempFile(String.valueOf(inputStream.hashCode()), ".pdf");
		tmpFile.deleteOnExit();
		
		System.out.println("tmp path: " + tmpFile.getAbsolutePath());
		FileOutputStream fos = new FileOutputStream(tmpFile);
		int read;
		byte[] bytes = new byte[1024];
		
		while((read = inputStream.read(bytes)) != -1) {
			fos.write(bytes, 0 , read);
		}
		
	     Workbook workbook = new Workbook();
	
	     workbook.loadFromFile(tmpFile.getAbsolutePath());
	
	
	     //Fit to page
	     workbook.getConverterSetting().setSheetFitToPage(true);
	     
	     String fullPath = filePath.substring(0,filePath.lastIndexOf("."))+".pdf";
	     System.out.println("=============================================="+fullPath);
	     workbook.saveToFile(tmpFile.getAbsolutePath(),FileFormat.PDF);
	     
		if (fos != null) fos.close(); //close는 꼭! 반드시!

		return tmpFile.getAbsolutePath();
	
	}
	
	//pdf 다운
	public String getPdfFiles(String filePath, String bucketKey) throws Exception{
		
		S3ObjectInputStream inputStream = null;
		
		//Create a Workbook
		System.out.println("pdf filePath: " + filePath);
		FileMgrVO vo = new FileMgrVO();
		vo.setFileNm(filePath);
		
		vo.setQryType("T");
		inputStream = s3Service.getMultipartFile(vo, bucketKey);
		
		File tmpFile = File.createTempFile(String.valueOf(inputStream.hashCode()), ".pdf");
		tmpFile.deleteOnExit();
		
		System.out.println("tmp path: " + tmpFile.getAbsolutePath());
		FileOutputStream fos = new FileOutputStream(tmpFile);
		int read;
		byte[] bytes = new byte[1024];
		
		while((read = inputStream.read(bytes)) != -1) {
			fos.write(bytes, 0 , read);
		}
		
		
		String fullPath = filePath.substring(0,filePath.lastIndexOf("."))+".pdf";
		System.out.println("=============================================="+fullPath);

		if (fos != null) fos.close(); //close는 꼭! 반드시!

		return tmpFile.getAbsolutePath();
		
	}
	
	//png to pdf
   public String getPngFiles(String filePath, String bucketKey) {
        Document document = new Document();
        Image img;
        File tmpFile = null;
        try {
    		S3ObjectInputStream inputStream = null;
    		
    		//Create a Workbook
    		System.out.println("png filePath: " + filePath);
    		FileMgrVO vo = new FileMgrVO();
    		vo.setFileNm(filePath);
    		
    		
    		File tmpPngFile = File.createTempFile(filePath, ".png");
    				
    		vo.setQryType("T");

    		inputStream = s3Service.getMultipartFile(vo, bucketKey);
    		try(FileOutputStream fos = new FileOutputStream(tmpPngFile)) {
    			int read;
    			byte[] bytes = new byte[1024];
    			
    			while((read = inputStream.read(bytes))!= -1) {
    				fos.write(bytes, 0, read);
    			}
    		}
    		
    		
    		
    		tmpFile = File.createTempFile(String.valueOf(inputStream.hashCode()), ".pdf");
    		tmpFile.deleteOnExit();
    		
            if(!tmpFile.exists()) tmpFile.createNewFile();
            
            PdfWriter.getInstance(document, new FileOutputStream(tmpFile));
            document.open();
            
            img = Image.getInstance(tmpPngFile.getCanonicalPath());
            Rectangle one = new Rectangle(img.getScaledWidth()+80,img.getScaledHeight()+80);
            document.setPageSize(one);
            document.newPage();
            document.add(img);
            document.close();
            System.out.println("Img Done");
        } catch (IOException | DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
		return tmpFile.getAbsolutePath();

    }
   
   
}
