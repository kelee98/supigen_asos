package com.spigen.asos;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import com.spigen.asos.model.FileMgrVO;
import com.spire.xls.Workbook;


public class PrintUtilApplication {

	
	public int printFiles(FileMgrVO vo) {
		//인쇄성공여부 0:성공 1:실패
		int successFlag = 0;
		
		String[] filePathArr = vo.getFilePath().split(",");
		String[] fileTypeArr = vo.getFileType().split(",");
		
		for (int i = 0; i < filePathArr.length; i++) {
			switch (fileTypeArr[i]) {
			case "xlsx":
				printExcel(filePathArr[i]);
				break;
			case "xls":
				printExcel(filePathArr[i]);
				break;
			case "pdf":
				printPdf(filePathArr[i]);
				break;
			default:
				break;
			}
		}
		
		return successFlag;
	}
	
	//프린트를 수행하는 메서드
	public int printPdf(String filePath) {

		 final JFrame f=new JFrame("Printing Test");
		 
		//인쇄성공여부 0:성공 1:실패
		int successFlag = 0;
		
		System.out.println("pdf: " + filePath);

        PDDocument document = null;
		try {
			document = PDDocument.load(new File(filePath));
		} catch (IOException e) {
			successFlag = 1;
			e.printStackTrace();
		}
		System.setProperty("java.awt.headless", "false");
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));
      	        
        if (job.printDialog()) {
	        try {
	        	job.print();
			} catch (PrinterException e) {
				successFlag = 1;
				e.printStackTrace();
			}
        }
		return successFlag;
	}       
	
	//엑셀 프린트를 수행하는 메서드
	public int printExcel(String filePath) {
		
		//인쇄성공여부 0:성공 1:실패
		int successFlag = 0;
		
		System.out.println("excel: " + filePath);
		
		Workbook workbook = new Workbook();
		
		workbook.loadFromFile(filePath);
		
		PrinterJob job = PrinterJob.getPrinterJob();
		
		PageFormat pageFormat  = job.defaultPage();
		Paper paper = pageFormat .getPaper();
		
		paper.setImageableArea(0,0,pageFormat .getWidth(),pageFormat .getHeight());
		pageFormat .setPaper(paper);
		job .setCopies(1);
		 
		job.setPrintable(workbook,pageFormat);
		
		try {
			job.print();
		} catch (PrinterException e) {
			successFlag = 1;
			e.printStackTrace();
		}
		return successFlag;
	}       


	  
}
