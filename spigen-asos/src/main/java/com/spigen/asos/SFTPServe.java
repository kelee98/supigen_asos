package com.spigen.asos;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.spigen.asos.model.FTPMgrVO;

public class SFTPServe{

	public static void main(String args[]) throws SftpException, JSchException {


		System.out.println("ddd");
		String host = "172.16.3.61";

		int port = 22;

		String userName = "spigenweb";

		String password = "Spigen1234!";

		String dir = "/Maetel_ovd_f/해외사업팀 Maetel Server/07. 사업기획팀/02. 매출정산/1. Amazon/Amazon FR"; //접근할 폴더가 위치할 경로

		String file = "/home/rocksea/SFTPUtil.java";



		SFTPMgrUtil util = new SFTPMgrUtil();

		util.init(host, userName, password, port);

//		FTPMgrUtil util2 = new FTPMgrUtil("172.16.3.61", 22, "spigenweb", "Spigen1234!");
//		
//		FTPMgrVO vo = new FTPMgrVO();
//		vo.setQryQuarter("4");
//		vo.setQryYear("2020");
//		util2.getFileList(vo);

		//util.disconnection();

		//System.exit(0);



	}

}


