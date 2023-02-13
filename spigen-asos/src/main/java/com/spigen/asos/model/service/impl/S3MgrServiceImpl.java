package com.spigen.asos.model.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.spigen.asos.model.DocMgrVO;
import com.spigen.asos.model.FTPMgrVO;
import com.spigen.asos.model.FileMgrVO;
import com.spigen.asos.model.service.S3MgrService;

@Service("s3MgrService")
public class S3MgrServiceImpl implements S3MgrService{

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
	private AmazonS3 s3Client;

    //파일업로드
	public void uploadMultipartFile(MultipartFile[] files,String bucketKey) throws IOException{
		
		ObjectMetadata omd = new ObjectMetadata();
		
		for(int i=0; i<files.length; i++) {
			String fileNm = new String(files[i].getOriginalFilename().getBytes("8859_1"),"UTF-8");
			omd.setContentType(files[i].getContentType());
			omd.setContentLength(files[i].getSize());
			omd.setHeader("filename",fileNm);
			System.out.println("files[i].getContentType():"  + files[i].getContentType());
			
			PutObjectRequest request = new PutObjectRequest(bucket+bucketKey,fileNm,files[i].getInputStream(),omd);

			AccessControlList acl = new AccessControlList();
		    acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
		    request.withAccessControlList(acl);
		    
			s3Client.putObject(request);
		}
	}
	

	public void uploadBufferedImage(ByteArrayOutputStream os,String imgName,String bucketKey) throws IOException{
		byte[] buffer = os.toByteArray();
      		InputStream is = new ByteArrayInputStream(buffer);
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentLength(buffer.length);
		s3Client.putObject(new PutObjectRequest(bucket+bucketKey,imgName,is,meta));
	}

	//파일 삭제 및 이동
	@Override
	public void deleteMultipartFile(DocMgrVO vo, String bucketKey) throws IOException {
		System.out.println(" vo.getFileNm(): "+  vo.getFileNm());
		
		String fileNm = new String(vo.getFileNm().getBytes("8859_1"),"UTF-8");
		
		CopyObjectResult result = s3Client.copyObject(bucket+bucketKey, fileNm, bucket+"/prd/removed", fileNm);
		System.out.println("result: " + result);
		s3Client.deleteObject(bucket+bucketKey, fileNm);
	}
	
	//미리보기
	@Override
	public S3ObjectInputStream getMultipartFile(FileMgrVO vo, String bucketKey) throws IOException {
		System.out.println("bucket+bucketKey: " + bucket+bucketKey);
		
		String fileNm = "";
		
		if("P".equals(vo.getQryType())) {
			fileNm = new String(vo.getFileNm().getBytes("8859_1"),"UTF-8");
		}else {
			fileNm = vo.getFileNm();
		}
		System.out.println("fileNm: " + fileNm);
		System.out.println("bucket+bucketKey: " + bucket+bucketKey);
		S3Object obj = s3Client.getObject(new GetObjectRequest(bucket+bucketKey, fileNm));
		System.out.println("obj: " + obj.getObjectContent());
		S3ObjectInputStream inputStream = obj.getObjectContent();
	
		return inputStream;
	}


}
