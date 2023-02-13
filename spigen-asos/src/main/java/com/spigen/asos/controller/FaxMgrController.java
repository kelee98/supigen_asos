package com.spigen.asos.controller;



import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.popbill.api.FaxService;
import com.popbill.api.PopbillException;
import com.popbill.api.fax.FAXSearchResult;
import com.popbill.api.fax.FaxResult;
import com.spigen.asos.FileMgrUtil;
import com.spigen.asos.model.DocMgrVO;
import com.spigen.asos.model.FileMgrVO;
import com.spigen.asos.model.service.S3MgrService;
import com.spigen.asos.model.service.TradeDocMgrService;


@Controller
public class FaxMgrController {
	
	/** tradeDocMgrService*/
	@Resource(name ="tradeDocMgrService")
	private TradeDocMgrService tradeDocMgrService;
	
	//팝빌
	@Autowired
	private FaxService faxService;
	
	/** s3MgrService*/
	@Resource(name ="s3MgrService")
	private S3MgrService s3Service;
	
	//팩스 전송 화면 호출
	@RequestMapping(path = "/fax", method = RequestMethod.GET)
	public ModelAndView fileMgrScreen(DocMgrVO docVO) throws Exception{
		
		ModelAndView model = new ModelAndView();
		//@RequestParam String liIndex
		//System.out.println("liIndex: " + liIndex);
		model.setViewName("documentSend");
		model.addObject("liIndex", "nav2");
		return model;
	}
	
	//팝빌 팩스 전송
	@RequestMapping(path = "/fax/sendFAX", method = RequestMethod.POST)  @ResponseBody
	public ModelMap sendFAX( FileMgrVO vo, HttpServletRequest req) throws Exception {

		ModelMap model = new ModelMap();
		
		//나중에 제거
		String uri = req.getRequestURI().toString();
		String url = req.getRequestURL().toString();
		
		vo.setReqURL(url.replace(uri, ""));
		//
		
		//테이블에 정보 저장하기
	    // 팝빌회원 사업자번호
		String corpNum = "1208736593";

    	// 팝빌회원 아이디
    	String userID = "spigenkorea";

        // 발신번호
        String sendNum = "070";

        // 수신번호
        //String receiveNum = "030334414963";
        String receiveNum = vo.getFaxNum();

        // 수신자명
        //String receiveName = "정하영";
        String receiveName = vo.getBankNm();

        String[] filePathArr = vo.getFilePath().split("\\^");
        
        //String[] fileSeq = vo.getFileSeq().split(",");
       
        FileMgrUtil util = new FileMgrUtil();
        //PDF 병합
       // Map<String,String> returnedMap = util.mergePDFs(filePathArr);
	
        S3ObjectInputStream inputStream = null;

        // 파일객체 배열, 호출당 최대 20개 파일 전송 가능
        File[] files = new File[filePathArr.length];
        for (int i = 0; i < filePathArr.length; i++) {
        	String filePath = filePathArr[i].split(":")[0].substring( 0, filePathArr[i].split(":")[0].lastIndexOf('/'));
        	String fileNm = filePathArr[i].split(":")[0].substring(filePathArr[i].split(":")[0].lastIndexOf("/")+1);
        	vo.setFileNm(fileNm);
			System.out.println("filePath: " + filePath + " / fileNm: " + fileNm);
			
			String path="/trade/unidocu/" + filePath;
			
			inputStream = s3Service.getMultipartFile(vo, path);
			
        	//File oldName = new File(filePathArr[i].split(":")[0]);
        	File tmpFile = File.createTempFile(String.valueOf(inputStream.hashCode()),"."+filePathArr[i].split(":")[1].substring(filePathArr[i].split(":")[1].lastIndexOf(".")+1));
        	//File newName = new File(filePath+"/"+filePathArr[i].split(":")[1]);
        	tmpFile.deleteOnExit();

    		
    		System.out.println("tmp path: " + tmpFile.getAbsolutePath());
    		FileOutputStream fos = new FileOutputStream(tmpFile);
    		int read;
    		byte[] bytes = new byte[1024];
    		
    		while((read = inputStream.read(bytes)) != -1) {
    			fos.write(bytes, 0 , read);
    		}
    		
        	
        	//FileMgrUtil.copyFile(oldName, newName);
        	
        	files[i] = tmpFile;
			
			System.out.println("send files:" + files[i].getAbsolutePath());
		}
		//files[0] = new File("C:\\file_test\\CI1.pdf");

        // 전송 예약일시
        Date reserveDT = null;

        // 광고팩스 전송여부
        Boolean adsYN = false;

        // 팩스제목
        String title = "팩스 제목";

        // 전송요청번호
        // 파트너가 전송 건에 대해 관리번호를 구성하여 관리하는 경우 사용.
        // 1~36자리로 구성. 영문, 숫자, 하이픈(-), 언더바(_)를 조합하여 팝빌 회원별로 중복되지 않도록 할당.
        String requestNum = "";
        FaxResult faxResult[];
        String receiptNum="";
        try {
  		  if("http://localhost:8080".equals(vo.getReqURL()) ) {
  			receiptNum="020082615080100001";
  		  }else {
  			  receiptNum= faxService.sendFAX(corpNum, sendNum, receiveNum,
  					  receiveName, files, reserveDT, userID, adsYN, title, requestNum);
  		  }
            System.out.println("Result: " + receiptNum);
            
            //FAX전송결과 조회
            faxResult = faxService.getFaxResult(corpNum, receiptNum);
            
            
            vo.setFileNm(Arrays.toString(faxResult[0].getFileNames()).replace("[", "").replace("]", ""));
            vo.setReqNum(receiptNum);
            vo.setSendPgs(0); //파트장님 협의 필요
            vo.setSendPee(0); //파트장님 협의 필요
            vo.setSendDt(faxResult[0].getReceiptDT());
            vo.setSendState(faxResult[0].getSendState());
            vo.setSendResult(0);//파트장님 협의 필요
            
            
        } catch (PopbillException e) {
            vo.setSendResult(1);//파트장님 협의 필요

        	model.addAttribute("Exception", e);
            System.out.println("e;" + e);
            //return "exception";
        }

		System.out.println("fileVO.getReqURL())1: " + vo.getReqURL());

        int result = tradeDocMgrService.insertSendHis(vo);
        
        model.addAttribute("result", result);
        
        model.addAttribute("reqNum", receiptNum);
        
        return model;
    }
	


	//팩스 전송 이력 화면 호출 
	@RequestMapping(path = "/fax/faxSendHisScreen", method = RequestMethod.GET)
	public ModelAndView faxSendHisScreen() throws Exception{
		
		ModelAndView model = new ModelAndView();

		model.setViewName("faxSendHis");
		model.addObject("liIndex", "nav6");
		
		return model;
	}
	
	//팩스 전송 이력 조회
	@RequestMapping("/fax/selectfaxSendHisQry") @ResponseBody
	public ModelMap selectfaxSendHisQry(FileMgrVO vo) throws Exception{
		ModelMap model = new ModelMap();
		
		List<FileMgrVO> resultList = tradeDocMgrService.selectfaxSendHisQry(vo);
				
		model.addAttribute("resultList", resultList);
	
		String [] arr = {"1","2","3","4"};
		FAXSearchResult result = faxService.search("1208736593", "20200820", "20200826", arr, false, false , 1, 20, "D", "");
		return model;
	}

}
