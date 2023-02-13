package com.spigen.asos.model.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.spigen.asos.model.DocMgrVO;
import com.spigen.asos.model.FileMgrVO;


public interface S3MgrService {

	public void uploadMultipartFile(MultipartFile[] files,String bucketKey) throws IOException;
	
	public void uploadBufferedImage(ByteArrayOutputStream os,String imgName,String bucketKey) throws Exception ;
	
	public void deleteMultipartFile(DocMgrVO vo,String bucketKey) throws IOException;

	public S3ObjectInputStream getMultipartFile(FileMgrVO vo, String bucketKey) throws IOException;

}
