package com.spigen.asos.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.spigen.asos.FileMgrUtil;
import com.spigen.asos.PaginationInfo;
import com.spigen.asos.model.DocMgrVO;
import com.spigen.asos.model.FileMgrVO;
import com.spigen.asos.model.SheetMgrVO;
import com.spigen.asos.model.service.ProdDocMgrService;
import com.spigen.asos.model.service.S3MgrService;

@Controller
public class ProdDocMgrController {
	
	/** prodDocMgrService*/
	@Resource(name ="prodDocMgrService")
	private ProdDocMgrService prodDocMgrService;
	
	
	/** s3MgrService*/
	@Resource(name ="s3MgrService")
	private S3MgrService s3Service;
	
	
	
	//제품서류 마스터 화면 호출
	@RequestMapping(path = "/prodDocMaster", method = RequestMethod.GET)
	public ModelAndView prodDocMasterScreen() throws Exception{
		ModelAndView model = new ModelAndView();
		model.setViewName("prodDocMasterModel");
		model.addObject("liIndex", "nav3");
		
		return model;
	}
	
	//제품 서류 마스터 조회
	@RequestMapping(value = "/prodDocMaster/prodDocMasterList" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap prodDocMasterList(DocMgrVO vo) throws Exception{
		
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(vo.getPageIndex());
        paginationInfo.setRecordCountPerPage(vo.getPageUnit());
        paginationInfo.setPageSize(vo.getPageSize());
        vo.setFirstIndex(paginationInfo.getFirstRecordIndex());
        vo.setLastIndex(paginationInfo.getLastRecordIndex());
        vo.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        
		ModelMap model = new ModelMap();
		
		//SKU 복수 조회
	    List<String> matnrLs = new ArrayList<String>();
	     
	    String[] matnrArr = vo.getMatnr().split(",");
	    
	    for(int i=0; i< matnrArr.length; i++){
	    	matnrLs.add(matnrArr[i].toString());
	    }
	    vo.setQryLs(matnrLs);
	    
		List<DocMgrVO> resultList = prodDocMgrService.selectHeaderList(vo);
		
		if(resultList.size()>0) {
			paginationInfo.setTotalRecordCount(resultList.get(0).getCnt());
		}else {
		    paginationInfo.setTotalRecordCount(0);
		}
	       
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("resultList", resultList);
		
		return model;
	}
	
	//제품서류 마스터 등록
	@RequestMapping(value = "/prodDocMaster/insertRqrdDocs" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap insertRqrdDocs(@RequestBody DocMgrVO vo) throws Exception{
		
		ModelMap model = new ModelMap();
		
		int cnt = prodDocMgrService.selectCntRqrdDocs(vo);
		int result = 0;
		
		if(cnt==0) {
			result = prodDocMgrService.insertRqrdDocs(vo);
		}else {
			result = prodDocMgrService.updateRqrdDocs(vo);
		}
		
		model.addAttribute("result", result);
		
		return model;
	}
	
	//제품서류 마스터(SKU)화면 호출
	@RequestMapping(path = "/prodDocMasterSKU", method = RequestMethod.GET)
	public ModelAndView prodDocMasterSKUScreen() throws Exception{
		ModelAndView model = new ModelAndView();
		model.setViewName("prodDocMasterSKU");
		model.addObject("liIndex", "nav3");
		
		return model;
	}
	
	//제품서류 마스터(SKU) 목록 조회
	@RequestMapping(value = "/prodDocMaster/prodDocMasterSKUList" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap prodDocMasterSKUList(DocMgrVO vo) throws Exception{
		
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(vo.getPageIndex());
        paginationInfo.setRecordCountPerPage(vo.getPageUnit());
        paginationInfo.setPageSize(vo.getPageSize());
        vo.setFirstIndex(paginationInfo.getFirstRecordIndex());
        vo.setLastIndex(paginationInfo.getLastRecordIndex());
        vo.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        
		ModelMap model = new ModelMap();
		
		//SKU 복수 조회
	    List<String> matnrLs = new ArrayList<String>();
	     
	    String[] matnrArr = vo.getMatnr().split(",");
	    
	    for(int i=0; i< matnrArr.length; i++){
	    	matnrLs.add(matnrArr[i].toString());
	    }
	    vo.setQryLs(matnrLs);
	    
		List<DocMgrVO> resultList = prodDocMgrService.selectDetailList(vo);
		
		if(resultList.size()>0) {
			paginationInfo.setTotalRecordCount(resultList.get(0).getCnt());
		}else {
		    paginationInfo.setTotalRecordCount(0);
		}
	       
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("resultList", resultList);
		
		return model;
	}
	
	//시방서 마스터 등록(SKU 개별건)
	@RequestMapping(value = "/prodDocMaster/insertRqrdDocsSKU" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap insertRqrdDocsSKU(@RequestBody DocMgrVO vo) throws Exception{
		
		ModelMap model = new ModelMap();
		
		//SKU 복수 조회
	    List<String> matnrLs = new ArrayList<String>();
	     
	    String[] matnrArr = vo.getMatnr().split(",");
	    
	    for(int i=0; i< matnrArr.length; i++){
	    	matnrLs.add(matnrArr[i].toString());
	    }
	    vo.setQryLs(matnrLs);
	    
		int cnt = prodDocMgrService.selectCntRqrdDocsSKU(vo);
		int result = 0;
		
		if(cnt==0) {
			result = prodDocMgrService.insertRqrdDocsSKU(vo);
		}else {
			result = prodDocMgrService.updateRqrdDocsSKU(vo);
		}
		
		model.addAttribute("result", result);
		
		return model;
	}

	
	//제품 서류 관리 화면 호출
	@RequestMapping(path = "/sDocMgrList", method = RequestMethod.GET)
	public ModelAndView sDocMgrListScreen() throws Exception{
		ModelAndView model = new ModelAndView();
		model.setViewName("sDocMgrList");
		model.addObject("liIndex", "nav4");
		
		return model;
	}
	
	//제품서류 관리 목록 조회
	@RequestMapping(value = "/prodDocMgrList/selectProdDocMgrList" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap selectProdDocMgrList(DocMgrVO vo) throws Exception{
		
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(vo.getPageIndex());
        paginationInfo.setRecordCountPerPage(vo.getPageUnit());
        paginationInfo.setPageSize(vo.getPageSize());
        vo.setFirstIndex(paginationInfo.getFirstRecordIndex());
        vo.setLastIndex(paginationInfo.getLastRecordIndex());
        vo.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        
		ModelMap model = new ModelMap();
		
		//SKU 복수 조회
	    List<String> matnrLs = new ArrayList<String>();
	     
	    String[] matnrArr = vo.getMatnr().split(",");
	    
	    for(int i=0; i< matnrArr.length; i++){
	    	matnrLs.add(matnrArr[i].toString());
	    }
	    vo.setQryLs(matnrLs);
		
		List<DocMgrVO> resultList = prodDocMgrService.selectSDocMgrList(vo);
		
	    paginationInfo.setTotalRecordCount(prodDocMgrService.selectSDocMgrListCnt(vo));
	       
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("resultList", resultList);
		
		return model;
	}
	
	//제품서류 등록 팝업 호출
	@RequestMapping(path = "/prodDocMgrList/prodDocRgtPopupScreen", method = RequestMethod.GET)
	public ModelAndView sDocRgtPopupScreen(DocMgrVO vo) throws Exception{
	
		ModelAndView model = new ModelAndView();

		model.setViewName("popup/prodDocRgtPopup");
		
		//List<DocMgrVO> deviceList = prodDocMgrService.selectCmQryList("deviceList", vo);
		//List<DocMgrVO> modelList = prodDocMgrService.selectCmQryList("modelList", vo);
		
		//model.addObject("deviceList", deviceList);
		//model.addObject("resultList", modelList);
		DocMgrVO docMgrVO = prodDocMgrService.selectMasterInfo(vo);

		
		model.addObject("result", docMgrVO);
		
		return model;
	}
	
	//제품서류 수정 팝업 호출
	@RequestMapping(path = "/prodDocMgrList/prodDocUpdPopupScreen", method = RequestMethod.GET)
	public ModelAndView prodDocUpdPopupScreen(DocMgrVO vo) throws Exception{
		
		ModelAndView model = new ModelAndView();
		
		model.setViewName("popup/prodDocUpdPopup");
		

		DocMgrVO docMgrVO = prodDocMgrService.selectMasterInfo(vo);
		
		List<DocMgrVO> fileList = prodDocMgrService.selectAttachedFileList(vo);
		
		model.addObject("result", docMgrVO);
		model.addObject("resultList", fileList);
		
		return model;
	}
	
	
	//시방서 등록 팝업 SKU 조회
	@RequestMapping(path = "/sDocMgrList/sDocRgtPopupQrySku", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap sDocRgtPopupQrySku(@RequestBody DocMgrVO vo) throws Exception{

		ModelMap model = new ModelMap();

		List<DocMgrVO> skuList = prodDocMgrService.selectCmQryList("skuList", vo);
		
		model.put("skuList", skuList);
		
		return model;
	}
	
	//시방서 등록 유효성 체크
	@RequestMapping(path = "/sDocMgrList/selectCntSkuList", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap selectCntSkuList(@RequestBody DocMgrVO vo) throws Exception{
		
		ModelMap model = new ModelMap();
		List<DocMgrVO> argsList = new ArrayList<DocMgrVO>();
		String[] str = vo.getMatnr().split(",");
		for (int i = 0; i < str.length; i++) {
			DocMgrVO argsVO  = new DocMgrVO();
			argsVO.setMatnr(str[i]);
			argsList.add(argsVO);
		}
		vo.setSubList(argsList);
		
		List<DocMgrVO> resultList = prodDocMgrService.selectCntSkuList(vo);
		
		model.put("skuList", resultList);
		
		return model;
	}
	
 
	//제품 서류 등록
	@RequestMapping("/sDocMgrList/sDocUpload") @ResponseBody
	public ModelMap sDocUpload( @RequestParam("files") MultipartFile[] uploadfiles,  DocMgrVO docVO) throws Exception{
	
		FileMgrUtil util = new FileMgrUtil();
    	System.out.println("=====================================================");

		ModelMap model = new ModelMap();
		
		String s3Path = "/prd";
		
		s3Service.uploadMultipartFile(uploadfiles, s3Path);
		
        // Get file name
        String uploadedFileName = Arrays.stream(uploadfiles)
        								.map(x -> x.getOriginalFilename())
        								.filter(x -> !StringUtils.isEmpty(x))
        								.collect(Collectors.joining(" , "));
        	System.out.println("uploadedFileNames: " + uploadedFileName.replace("&", "_"));
        try {
        	//util.saveUploadFiles(Arrays.asList(uploadfiles));
        	System.out.println("=====================================================");
        	model.put("result", "S");
        	
        	for (int i = 0; i < uploadfiles.length; i++) {
        		docVO.setPath("/prd/");
        		//docVO.setPath("classes/attach_files/prd/");

				docVO.setFileNm(uploadfiles[i].getOriginalFilename().replace("&", "_"));
        		prodDocMgrService.insertSDocFile(docVO);
			}

        	//내역 저장
    		
    		Principal principal = SecurityContextHolder.getContext().getAuthentication();

			docVO.setChgCd("C");
			docVO.setCrtrId(principal.getName());
			prodDocMgrService.insertProdDocFileChgHis(docVO);

        } catch (Exception e) { 
			System.out.println(e.getMessage()); e.printStackTrace(); 
			model.put("result", "F");
		} 

//        if("Y".equals(docVO.getComnYn())) {
//    		docVO.setMatnr("COMMON");
//    		docVO.setFileNm(uploadedFileName);
//    		docVO.setPath("C:\\upload_test");
//    		docVO.setComnYn("Y");
//    		prodDocMgrService.insertSDocFile(docVO);
//        }else if("N".equals(docVO.getComnYn())) {
//        	String[] str = docVO.getMatnr().split(",");
//        	for (int i = 0; i < str.length; i++) {
//        		docVO.setMatnr(str[i]);
//        		docVO.setFileNm(uploadedFileName);
//        		docVO.setPath("C:\\upload_test");
//        		docVO.setComnYn("N");
//        		prodDocMgrService.insertSDocFile(docVO);
//        	}
//        }
        

		
		return model;
	}
	
	//제품 서류 수정
	@RequestMapping("/prodDocMgrList/prodDocUpd") @ResponseBody
	public ModelMap prodDocUpd( @RequestParam("files") MultipartFile[] uploadfiles,  DocMgrVO docVO) throws Exception{
		
		FileMgrUtil util = new FileMgrUtil();
		
		ModelMap model = new ModelMap();
		
		String s3Path = "/prd";
		s3Service.uploadMultipartFile(uploadfiles, s3Path);
		
		// Get file name
		String uploadedFileName = Arrays.stream(uploadfiles)
				.map(x -> x.getOriginalFilename())
				.filter(x -> !StringUtils.isEmpty(x))
				.collect(Collectors.joining(" , "));
		
		System.out.println("uploadedFileNames: " + uploadedFileName.replace("&", "_"));
		try {
			//util.saveUploadFiles(Arrays.asList(uploadfiles));
			
			model.put("result", "S");
			
			for (int i = 0; i < uploadfiles.length; i++) {
				//docVO.setPath("/prd");
        		docVO.setPath("/prd/");

				docVO.setFileNm(uploadfiles[i].getOriginalFilename().replace("&", "_"));
				if("S".equals(docVO.getDocType())||"A".equals(docVO.getDocType())) {
					prodDocMgrService.updateProdDocFile(docVO);
				}
			}
			
		} catch (Exception e) { 
			System.out.println(e.getMessage()); e.printStackTrace(); 
			model.put("result", "F");
		} 
		
		return model;
	}
	
	//제품 서류 삭제 (파일 이동 필요)
	@RequestMapping(value = "/prodDocMgrList/deleteProdDoc" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap deleteProdDoc(@RequestBody DocMgrVO docVO) throws Exception{
		
		ModelMap model = new ModelMap();
		
		String s3Path = "/prd";
		s3Service.deleteMultipartFile(docVO, s3Path);

		int result = prodDocMgrService.deleteProdDoc(docVO);	
		
		model.put("result", result);

		return model;
	}
	
	//파일 뷰어
	@RequestMapping(path = "/sDocMgrList/fileViewer") @ResponseBody
	public void fileViewer(HttpServletResponse response, FileMgrVO vo) throws Exception{
		ModelMap model = new ModelMap();
		
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		S3ObjectInputStream inputStream = null;

		try {
		
		String fileName = vo.getFilePath();
		System.out.println("fileName:" + vo.getFileNm());
		System.out.println("filePath:" + fileName);

		
		if("pdf".equals(vo.getFileType())||"PDF".equals(vo.getFileType())) {
			//클라이언트 브라우져에서 바로 보는 방법(헤더 변경)
			response.setContentType("application/pdf");
		}else {
			//다운로드
			response.addHeader("Content-Disposition", "attachment; filename="+vo.getFileNm());
		}
		
		vo.setQryType("P");
		inputStream = s3Service.getMultipartFile(vo, "/prd");
		
		//OutputStream outputStream = new BufferedOutputStream(new FileOutputStream("/attach_files/tmp.pdf"));
		
		bis = new BufferedInputStream(inputStream);
		
		int size = bis.available(); //지정 파일에서 읽을 수 있는 바이트 수를 반환
		byte[] buf = new byte[size]; //버퍼설정
//		int readCount = bis.read(buf);
	    int readCount = -1;

	    response.flushBuffer();
	    bos = new BufferedOutputStream(response.getOutputStream());

	    while ((readCount = inputStream.read(buf)) != -1) {
	    	bos.write(buf, 0, readCount);
	    	//bos.flush();
	    }
		
		
		
//		 byte[] bytesArray = new byte[4096];
//		    int bytesRead = -1;
//		    while ((bytesRead = inputStream.read(bytesArray)) != -1) {
//		        outputStream.write(bytesArray, 0, bytesRead);
//		    }
//
//		    outputStream.close();
//		    inputStream.close();
		    
//		IOUtils.copy(inputStream, fos);
		
//		byte[] bytes = IOUtils.toByteArray(inputStream);
//		
//		int length = 0;
//		while ((length = inputStream.read(bytes)) > 0) {
//			fos.write(bytes, 0, length);
//		}
//		fos.flush();
		
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
	
	//파일 다운로드
	@RequestMapping(path = "/prodDocMgrList/downloadProdDoc", method = RequestMethod.GET)
	@ResponseBody
	public void downloadProdDoc(HttpServletRequest request,HttpServletResponse response, DocMgrVO docVO) {

		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		S3ObjectInputStream inputStream = null;
		
		try {
			FileMgrVO vo = prodDocMgrService.selectFilePath(docVO);
			String fileName = vo.getFileNm();
			
			
			System.out.println("pdfFileName:" + fileName);
		
			//다운로드
			String encName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			response.addHeader("Content-Disposition", "attachment; filename="+encName);
			
			vo.setQryType("P");
			inputStream = s3Service.getMultipartFile(vo, "/prd");
			
			bis = new BufferedInputStream(inputStream);
			
			int size = bis.available(); //지정 파일에서 읽을 수 있는 바이트 수를 반환
			byte[] buf = new byte[size]; //버퍼설정
//			int readCount = bis.read(buf);
		    int readCount = -1;

		    response.flushBuffer();
		    bos = new BufferedOutputStream(response.getOutputStream());

		    while ((readCount = inputStream.read(buf)) != -1) {
		    	bos.write(buf, 0, readCount);
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
	
	//BOM 서류 관리 화면 호출
	@RequestMapping(path = "/bomMgrList/bomMgrListScreen", method = RequestMethod.GET)
	public ModelAndView bomMgrListScreen(DocMgrVO vo) throws Exception{
		ModelAndView model = new ModelAndView();
		model.setViewName("bomMgrList");
		model.addObject("matnr", vo.getMatnr());
		model.addObject("liIndex", "nav9");
		
		return model;
	}
	
	//제품서류 관리 목록 조회
	@RequestMapping(value = "/bomMgrList/selectBomMgrList" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap selectBomMgrList(DocMgrVO vo) throws Exception{
		
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(vo.getPageIndex());
        paginationInfo.setRecordCountPerPage(vo.getPageUnit());
        paginationInfo.setPageSize(vo.getPageSize());
        vo.setFirstIndex(paginationInfo.getFirstRecordIndex());
        vo.setLastIndex(paginationInfo.getLastRecordIndex());
        vo.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        
		ModelMap model = new ModelMap();
		
		//SKU 복수 조회
	    List<String> matnrLs = new ArrayList<String>();
	     
	    String[] matnrArr = vo.getMatnr().split(",");
	    
	    for(int i=0; i< matnrArr.length; i++){
	    	matnrLs.add(matnrArr[i].toString());
	    }
	    vo.setQryLs(matnrLs);
	    
		List<DocMgrVO> resultList = prodDocMgrService.selectBomMgrList(vo);
		
	    paginationInfo.setTotalRecordCount(prodDocMgrService.selectBomMgrListCnt(vo));
	       
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("resultList", resultList);
		
		return model;
	}
	
	//제품서류 등록(BOM) 팝업 호출
	@RequestMapping(path = "/bomMgrList/bomDocRgtPopupScreen", method = RequestMethod.GET)
	public ModelAndView bomDocRgtPopupScreen(DocMgrVO vo) throws Exception{
	
		ModelAndView model = new ModelAndView();

		model.setViewName("popup/bomDocRgtPopup");
		
		//List<DocMgrVO> deviceList = prodDocMgrService.selectCmQryList("deviceList", vo);
		//List<DocMgrVO> modelList = prodDocMgrService.selectCmQryList("modelList", vo);
		
		//model.addObject("deviceList", deviceList);
		//model.addObject("resultList", modelList);
		DocMgrVO docMgrVO = prodDocMgrService.selectBomMasterInfo(vo);

		
		model.addObject("result", docMgrVO);
		
		return model;
	}
		
	//제품서류 수정(BOM) 팝업 호출
	@RequestMapping(path = "/bomMgrList/bomDocUpdPopupScreen", method = RequestMethod.GET)
	public ModelAndView bomDocUpdPopupScreen(DocMgrVO vo) throws Exception{
		
		ModelAndView model = new ModelAndView();
		
		model.setViewName("popup/bomDocUpdPopup");
		

		DocMgrVO docMgrVO = prodDocMgrService.selectBomMasterInfo(vo);
		
		List<DocMgrVO> fileList = prodDocMgrService.selectAttachedBomFileList(vo);
		
		model.addObject("result", docMgrVO);
		model.addObject("resultList", fileList);
		
		return model;
	}	
	
	//제품 서류 등록(BOM)
	@RequestMapping("/bomMgrList/bomDocRgt") @ResponseBody
	public ModelMap bomDocRgt( @RequestParam("files") MultipartFile[] uploadfiles,  DocMgrVO docVO) throws Exception{
	
		FileMgrUtil util = new FileMgrUtil();
		
		ModelMap model = new ModelMap();
		
        // Get file name
        String uploadedFileName = Arrays.stream(uploadfiles)
        								.map(x -> x.getOriginalFilename())
        								.filter(x -> !StringUtils.isEmpty(x))
        								.collect(Collectors.joining(" , "));
        	System.out.println("uploadedFileNames: " + uploadedFileName.replace("&", "_"));
        try {
        	
        	String s3Path = "/prd";
    		
    		s3Service.uploadMultipartFile(uploadfiles, s3Path);
    		
        	//util.saveUploadFiles(Arrays.asList(uploadfiles));
        	
    		model.put("result", "S");
        	
        	for (int i = 0; i < uploadfiles.length; i++) {
        		docVO.setPath("/prd/");

				docVO.setFileNm(uploadfiles[i].getOriginalFilename().replace("&", "_"));
        		prodDocMgrService.insertBomDocFile(docVO);
			}
    		
        } catch (Exception e) { 
			System.out.println(e.getMessage()); e.printStackTrace(); 
			model.put("result", "F");
		} 
		
		return model;
	}
	
	//제품 서류 수정(BOM)
	@RequestMapping("/bomMgrList/bomDocUpd") @ResponseBody
	public ModelMap bomDocUpd( @RequestParam("files") MultipartFile[] uploadfiles,  DocMgrVO docVO) throws Exception{
		
		FileMgrUtil util = new FileMgrUtil();
		
		ModelMap model = new ModelMap();
		
		// Get file name
		String uploadedFileName = Arrays.stream(uploadfiles)
				.map(x -> x.getOriginalFilename())
				.filter(x -> !StringUtils.isEmpty(x))
				.collect(Collectors.joining(" , "));
		
		System.out.println("uploadedFileNames: " + uploadedFileName.replace("&", "_"));
		try {
			
			//util.saveUploadFiles(Arrays.asList(uploadfiles));
			
        	String s3Path = "/prd";
    		
    		s3Service.uploadMultipartFile(uploadfiles, s3Path);
    		
			for (int i = 0; i < uploadfiles.length; i++) {
        		docVO.setPath("/prd/");
        		
        		System.out.println("uploadfiles: " + uploadfiles[i].getOriginalFilename());
				docVO.setFileNm(uploadfiles[i].getOriginalFilename().replace("&", "_"));
				prodDocMgrService.updateBomDocFile(docVO);
			}
			
			model.put("result", "S");
		} catch (Exception e) { 
			System.out.println(e.getMessage()); e.printStackTrace(); 
			model.put("result", "F");
		} 
		
		return model;
	}
	
	//제품 서류 삭제 (파일 이동 필요)
	@RequestMapping(value = "/bomMgrList/bomDocDelete" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap bomDocDelete(@RequestBody DocMgrVO docVO) throws Exception{
		
		ModelMap model = new ModelMap();
		
		int result = prodDocMgrService.deleteBomDoc(docVO);	
		
		model.put("result", result);

		return model;
	}	
	
	//제품서류 일괄 등록
	@RequestMapping(path = "/prodDocMgrList/prodDocRgtAllPopupScreen", method = RequestMethod.GET)
	public ModelAndView prodDocRgtAllPopupScreen(DocMgrVO vo) throws Exception{
	
		ModelAndView model = new ModelAndView();

		model.setViewName("popup/prodDocRgtAllPopup");
		
		return model;
	}
		
	//제품 서류 일괄 등록
	@RequestMapping("/sDocMgrList/sDocUploadAll") @ResponseBody
	public ModelMap sDocUploadAll( @RequestParam("files") MultipartFile[] uploadfiles,  DocMgrVO docVO) throws Exception{
	
		ModelMap model = new ModelMap();
		
//		String s3Path = "/prd/d/";
//		s3Service.uploadMultipartFile(uploadfiles, s3Path);
		
        // Get file name
        String uploadedFileName = Arrays.stream(uploadfiles)
        								.map(x -> x.getOriginalFilename())
        								.filter(x -> !StringUtils.isEmpty(x))
        								.collect(Collectors.joining(" , "));
        	System.out.println("uploadedFileNames: " + uploadedFileName.replace("&", "_"));
        try {
        	
        	model.put("result", "S");
        	
        	for (int i = 0; i < uploadfiles.length; i++) {
        		docVO.setPath("/prd/");

				docVO.setFileNm(uploadfiles[i].getOriginalFilename().replace("&", "_"));
				//String [] normtArr = docVO.getNormt().split("^");
				String [] normtArr = docVO.getNormtArr();
				for(int j = 0 ; j < normtArr.length ; j++) {
					docVO.setNormt(normtArr[j]);
					prodDocMgrService.insertSDocFile(docVO);
				}
			}
    		
        } catch (Exception e) { 
			System.out.println(e.getMessage()); e.printStackTrace(); 
			model.put("result", "F");
		} 

		
		return model;
	}
	
	
	//자동 일괄 등록 화면 호출
	@RequestMapping(path = "/bomMgrList/bomDocAutoRgtPopupScreen", method = RequestMethod.GET)
	public ModelAndView bomDocAutoRgtPopupScreen(DocMgrVO vo) throws Exception{
	
		ModelAndView model = new ModelAndView();

		model.setViewName("popup/bomDocAutoRgtPopup");
		
		return model;
	}
		
	//제품 서류 등록(자동)
	@RequestMapping("/bomMgrList/bomDocAutoRgt") @ResponseBody
	public ModelMap bomDocAutoRgt( @RequestParam("files") MultipartFile[] uploadfiles,  DocMgrVO docVO) throws Exception{
	
		FileMgrUtil util = new FileMgrUtil();
		
		ModelMap model = new ModelMap();
		
        // Get file name
        String uploadedFileName = Arrays.stream(uploadfiles)
        								.map(x -> x.getOriginalFilename())
        								.filter(x -> !StringUtils.isEmpty(x))
        								.collect(Collectors.joining(" , "));
        	System.out.println("uploadedFileNames: " + uploadedFileName.replace("&", "_"));
        try {

        	String s3Path = "/prd/";
    		s3Service.uploadMultipartFile(uploadfiles, s3Path);
        	
        	
        	for (int i = 0; i < uploadfiles.length; i++) {
        		String parseStr1 = uploadfiles[i].getOriginalFilename().split("_")[0];
        		String parseStr2 = uploadfiles[i].getOriginalFilename().split("_")[1];
        		System.out.println("parseStr1: "+parseStr1 +"/parseStr2:" + parseStr2);
        		
        		docVO.setIdnrk(parseStr1);
        		docVO.setCstlal(parseStr2);
        		docVO.setPath(s3Path);

				docVO.setFileNm(uploadfiles[i].getOriginalFilename().replace("&", "_"));
				
        		prodDocMgrService.insertBomDocFile(docVO);
			}
        	model.put("result", "S");
    		
        } catch (Exception e) { 
			System.out.println(e.getMessage()); e.printStackTrace(); 
			model.put("result", "F");
		} 
		
		return model;
	}
	
	//제품 서류 관리 화면 호출
	@RequestMapping(path = "/sDocMgrList2", method = RequestMethod.GET)
	public ModelAndView sDocMgrListScreen2(DocMgrVO docMgrVO) throws Exception{
		ModelAndView model = new ModelAndView();
		model.setViewName("sDocMgrList2");
		model.addObject("liIndex", "nav12");
		model.addObject("docMgrVO",docMgrVO);
		System.out.println(docMgrVO.toString());
		return model;
	}
	
	//제품서류 관리 목록 조회
	@RequestMapping(value = "/prodDocMgrList/selectProdDocMgrList2" , method = RequestMethod.GET) 
	@ResponseBody
	public ModelMap selectProdDocMgrList2(@RequestParam("mvgr2") String mvgr2, @RequestParam("mvgr3") String mvgr3,  @RequestParam("matnr") String matnr) throws Exception{
		
		DocMgrVO vo = new DocMgrVO();
		vo.setMvgr2(mvgr2);
		vo.setMvgr3(mvgr3);
		vo.setMatnr(matnr);
		
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(vo.getPageIndex());
        paginationInfo.setRecordCountPerPage(vo.getPageUnit());
        paginationInfo.setPageSize(vo.getPageSize());
        vo.setFirstIndex(paginationInfo.getFirstRecordIndex());
        vo.setLastIndex(paginationInfo.getLastRecordIndex());
        vo.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        
		ModelMap model = new ModelMap();
		
		//SKU 복수 조회
	    List<String> matnrLs = new ArrayList<String>();
	     
	    String[] matnrArr = vo.getMatnr().split(",");
	    
	    for(int i=0; i< matnrArr.length; i++){
	    	matnrLs.add(matnrArr[i].toString());
	    }
	    vo.setQryLs(matnrLs);
		
		List<DocMgrVO> resultList = prodDocMgrService.selectSDocMgrList(vo);
		
	    paginationInfo.setTotalRecordCount(prodDocMgrService.selectSDocMgrListCnt(vo));
	       
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("resultList", resultList);
		
		return model;
	}
	
	//BOM 정보 조회 화면 호출
	@RequestMapping(path = "/bomInfoList/bomInfoListScreen", method = RequestMethod.GET)
	public ModelAndView bomInfoListScreen(DocMgrVO vo) throws Exception{
		ModelAndView model = new ModelAndView();
		model.setViewName("bomInfoList");
		model.addObject("liIndex", "nav9");
		
		return model;
	}
	
	//bom 정보 조회
	@RequestMapping(value = "/bomInfoList/selectBomInfoList" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap bomInfoList(DocMgrVO vo) throws Exception{

		PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(vo.getPageIndex());
        paginationInfo.setRecordCountPerPage(vo.getPageUnit());
        paginationInfo.setPageSize(vo.getPageSize());
        vo.setFirstIndex(paginationInfo.getFirstRecordIndex());
        vo.setLastIndex(paginationInfo.getLastRecordIndex());
        vo.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        
		ModelMap model = new ModelMap();
		//SKU 복수 조회
	    List<String> matnrLs = new ArrayList<String>();
	     
	    String[] matnrArr = vo.getMatnr().split(",");
	    
	    for(int i=0; i< matnrArr.length; i++){
	    	matnrLs.add(matnrArr[i].toString());
	    }
	    vo.setQryLs(matnrLs);
		List<DocMgrVO> resultList = prodDocMgrService.selectBomInfoList(vo);
		
	    paginationInfo.setTotalRecordCount(prodDocMgrService.selectBomInfoListCnt(vo));
	       
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("resultList", resultList);
		
		return model;
	}	
	
	//자재 정보 조회 화면 호출
	@RequestMapping(path = "/prodInfoList/prodInfoListScreen", method = RequestMethod.GET)
	public ModelAndView prodInfoListScreen(DocMgrVO vo) throws Exception{
		ModelAndView model = new ModelAndView();
		model.setViewName("prodInfoList");
		model.addObject("liIndex", "nav9");
		
		return model;
	}
	
	//자재 정보 조회
	@RequestMapping(value = "/prodInfoList/selectProdInfoList" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap selectProdInfoList(DocMgrVO vo) throws Exception{
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(vo.getPageIndex());
		paginationInfo.setRecordCountPerPage(vo.getPageUnit());
		paginationInfo.setPageSize(vo.getPageSize());
		vo.setFirstIndex(paginationInfo.getFirstRecordIndex());
		vo.setLastIndex(paginationInfo.getLastRecordIndex());
		vo.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		ModelMap model = new ModelMap();
		
		//SKU 복수 조회
	    List<String> matnrLs = new ArrayList<String>();
	     
	    String[] matnrArr = vo.getMatnr().split(",");
	    
	    for(int i=0; i< matnrArr.length; i++){
	    	matnrLs.add(matnrArr[i].toString());
	    }
	    vo.setQryLs(matnrLs);
	    
		List<DocMgrVO> resultList = prodDocMgrService.selectProdInfoList(vo);
		
		paginationInfo.setTotalRecordCount(prodDocMgrService.selectProdInfoListCnt(vo));
		
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("resultList", resultList);
		
		return model;
	}	
	
	//자재별 제품서류 마스터 화면 호출
	@RequestMapping(path = "/prodDocMasterMtr", method = RequestMethod.GET)
	public ModelAndView prodDocMasterMtrScreen() throws Exception{
		ModelAndView model = new ModelAndView();
		model.setViewName("prodDocMasterMtr");
		model.addObject("liIndex", "nav3");
		
		return model;
	}
	
	//자재별 제품 서류 마스터 조회
	@RequestMapping(value = "/prodDocMasterMtr/prodDocMasterMtrList" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap prodDocMasterMtrList(DocMgrVO vo) throws Exception{
		
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(vo.getPageIndex());
        paginationInfo.setRecordCountPerPage(vo.getPageUnit());
        paginationInfo.setPageSize(vo.getPageSize());
        vo.setFirstIndex(paginationInfo.getFirstRecordIndex());
        vo.setLastIndex(paginationInfo.getLastRecordIndex());
        vo.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        
		ModelMap model = new ModelMap();
		
		//SKU 복수 조회
	    List<String> matnrLs = new ArrayList<String>();
	     
	    String[] matnrArr = vo.getMatnr().split(",");
	    
	    for(int i=0; i< matnrArr.length; i++){
	    	matnrLs.add(matnrArr[i].toString());
	    }
	    vo.setQryLs(matnrLs);
	    
		List<DocMgrVO> resultList = prodDocMgrService.prodDocMasterMtrList(vo);
		
		if(resultList.size()>0) {
			paginationInfo.setTotalRecordCount(resultList.get(0).getCnt());
		}else {
		    paginationInfo.setTotalRecordCount(0);
		}
	       
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("resultList", resultList);
		
		return model;
	}
	
	//자재별 제품서류 마스터 등록
	@RequestMapping(value = "/prodDocMasterMtr/insertRqrdDocsMtr" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap insertRqrdDocsMtr(@RequestBody DocMgrVO vo) throws Exception{
		
		ModelMap model = new ModelMap();
		
		int cnt = prodDocMgrService.selectCntRqrdDocsMtr(vo);
		int result = 0;
		
		if(cnt==0) {
			result = prodDocMgrService.insertRqrdDocsMtr(vo);
		}else {
			result = prodDocMgrService.updateRqrdDocsMtr(vo);
		}
		
		model.addAttribute("result", result);
		
		return model;
	}
	
	//제품 서류 관리(모델) 화면 호출
	@RequestMapping(path = "/prodMgrListModel/prodMgrListModelScreen", method = RequestMethod.GET)
	public ModelAndView prodMgrListModelScreen() throws Exception{
		ModelAndView model = new ModelAndView();
		model.setViewName("prodMgrListModel");
		model.addObject("liIndex", "nav4");
		
		return model;
	}
	
	//제품서류 관리(모델) 목록 조회
	@RequestMapping(value = "/prodMgrListModel/selectProdMgrListModel" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap selectProdMgrListModel(DocMgrVO vo) throws Exception{
		
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(vo.getPageIndex());
        paginationInfo.setRecordCountPerPage(vo.getPageUnit());
        paginationInfo.setPageSize(vo.getPageSize());
        vo.setFirstIndex(paginationInfo.getFirstRecordIndex());
        vo.setLastIndex(paginationInfo.getLastRecordIndex());
        vo.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        
		ModelMap model = new ModelMap();
		
		//SKU 복수 조회
	    List<String> matnrLs = new ArrayList<String>();
	     
	    String[] matnrArr = vo.getMatnr().split(",");
	    
	    for(int i=0; i< matnrArr.length; i++){
	    	matnrLs.add(matnrArr[i].toString());
	    }
	    vo.setQryLs(matnrLs);
		
		List<DocMgrVO> resultList = prodDocMgrService.selectProcDocMgrListModel(vo);
		
	    paginationInfo.setTotalRecordCount(prodDocMgrService.selectSDocMgrListCnt(vo));
	       
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("resultList", resultList);
		
		return model;
	}
	
	//제품 서류 관리(SKU) 화면 호출
	@RequestMapping(path = "/prodMgrListSKU/prodMgrListSKUScreen", method = RequestMethod.GET)
	public ModelAndView prodMgrListSKUScreen() throws Exception{
		ModelAndView model = new ModelAndView();
		model.setViewName("prodMgrListSKU");
		model.addObject("liIndex", "nav4");
		
		return model;
	}
	
	//제품서류 관리(SKU) 목록 조회
	@RequestMapping(value = "/prodMgrListSKU/selectProdMgrListSKU" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap selectProdMgrListSKU(DocMgrVO vo) throws Exception{
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(vo.getPageIndex());
		paginationInfo.setRecordCountPerPage(vo.getPageUnit());
		paginationInfo.setPageSize(vo.getPageSize());
		vo.setFirstIndex(paginationInfo.getFirstRecordIndex());
		vo.setLastIndex(paginationInfo.getLastRecordIndex());
		vo.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		ModelMap model = new ModelMap();
		
		//SKU 복수 조회
		List<String> matnrLs = new ArrayList<String>();
		
		String[] matnrArr = vo.getMatnr().split(",");
		
		for(int i=0; i< matnrArr.length; i++){
			matnrLs.add(matnrArr[i].toString());
		}
		vo.setQryLs(matnrLs);
		
		List<DocMgrVO> resultList = prodDocMgrService.selectProcDocMgrListSKU(vo);
		
		paginationInfo.setTotalRecordCount(prodDocMgrService.selectProcDocMgrListSKUCnt(vo));
		
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("resultList", resultList);
		
		return model;
	}
	
	//BOM 역분개 정보 조회 화면 호출
	@RequestMapping(path = "/bomInfoList/bomInfoListRScreen", method = RequestMethod.GET)
	public ModelAndView bomInfoListRScreen(DocMgrVO vo) throws Exception{
		ModelAndView model = new ModelAndView();
		model.setViewName("bomInfoListR");
		model.addObject("liIndex", "nav30");
		
		return model;
	}
	
	//bom 역분개 정보 조회
	@RequestMapping(value = "/bomInfoList/selectBomInfoListR" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap selectBomInfoListR(DocMgrVO vo) throws Exception{

		PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(vo.getPageIndex());
        paginationInfo.setRecordCountPerPage(vo.getPageUnit());
        paginationInfo.setPageSize(vo.getPageSize());
        vo.setFirstIndex(paginationInfo.getFirstRecordIndex());
        vo.setLastIndex(paginationInfo.getLastRecordIndex());
        vo.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        
		ModelMap model = new ModelMap();
		
		List<DocMgrVO> resultList = prodDocMgrService.selectBomInfoListR(vo);
		
	    paginationInfo.setTotalRecordCount(prodDocMgrService.selectBomInfoListRCnt(vo));
	       
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("resultList", resultList);
		
		return model;
	}	
	
	//구글연동
	@RequestMapping(value = "/importGoogleSheet" , method = RequestMethod.POST) 
	public String importGoogleSheet(DocMgrVO vo) throws Exception{
		
	    List<String> matnrLs = new ArrayList<String>();
	     
	    String[] matnrArr = vo.getMatnr().split(",");
	    
	    for(int i=0; i< matnrArr.length; i++){
	    	matnrLs.add(matnrArr[i].toString());
	    }
	    vo.setQryLs(matnrLs);
		List<SheetMgrVO> resultList = prodDocMgrService.selectBomInfoList2(vo);
		ObjectMapper mapper = new ObjectMapper();

		String jsonStr = mapper.writeValueAsString(resultList);
		System.out.println("json: " + jsonStr);

		String inputLine = null;
		StringBuffer outResult = new StringBuffer();

		String json = "{A:1,B:22}";
		// 연결
		URL url = new URL("https://script.google.com/macros/s/AKfycbxjgbFIBwcPrWyQ2XFGleDvuD86vmukpW26I4nGMLmfAAMgmYRP/exec");
		
		try{
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept-Charset", "UTF-8"); 
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			String encoded = Base64.getEncoder().encodeToString(("hyjeong@spigen.com" + ":" + "Alsqhd88!").getBytes(StandardCharsets.UTF_8));
			conn.setRequestProperty("Authorization", "Basic " + encoded);
		      
			OutputStream os = conn.getOutputStream();
			os.write(json.getBytes("UTF-8"));
			os.flush();
		    
			// 리턴된 결과 읽기
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			while ((inputLine = in.readLine()) != null) {
				outResult.append(inputLine);
			}
		    
			conn.disconnect();
		  }catch(Exception e){
		      e.printStackTrace();
		  }	

		
		
		return "https://docs.google.com/spreadsheets/d/1h46io9-kmPIiVtbYmud7LNSNJKXswkVhuPHwJe76FJM/edit#gid=0";
	}	
	
	
	
	
	//제품서류 등록 팝업 호출
	@RequestMapping(path = "/prodDocMgrList/prodDocRgtPAPopupScreen", method = RequestMethod.GET)
	public ModelAndView sDocRgtPAPopupScreen(DocMgrVO vo) throws Exception{
	
		ModelAndView model = new ModelAndView();

		model.setViewName("popup/prodDocRgtPAPopup");
		
		//List<DocMgrVO> deviceList = prodDocMgrService.selectCmQryList("deviceList", vo);
		//List<DocMgrVO> modelList = prodDocMgrService.selectCmQryList("modelList", vo);
		
		//model.addObject("deviceList", deviceList);
		//model.addObject("resultList", modelList);
		DocMgrVO docMgrVO = prodDocMgrService.selectMasterInfo(vo);

		
		model.addObject("result", docMgrVO);
		
		return model;
	}
}
