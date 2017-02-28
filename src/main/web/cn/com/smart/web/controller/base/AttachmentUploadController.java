package cn.com.smart.web.controller.base;

import java.io.File;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;

import cn.com.smart.bean.SmartResponse;
import cn.com.smart.exception.ServiceException;
import cn.com.smart.web.bean.UploadFileInfo;
import cn.com.smart.web.bean.entity.TNAttachment;
import cn.com.smart.web.service.AttachmentService;

/**
 * 上传附件
 * @author lmq
 * @version 1.0 2015年8月30日
 * @since 1.0
 *
 */
public abstract class AttachmentUploadController extends UploadController {

	@Autowired
	protected AttachmentService attServ;
	
	/**
	 * 上传附件 <br />
	 * 上传附件的信息保存TNAttachment对象的表里面，并返回TNAttachment对象
	 * @param file
	 * @param contentType 文件类型
	 * @param uploadFileName 原文件名
	 * @param destFileName 文件名(不含后缀)
	 * @param destDir  保存目录
	 * @param userId 用户ID
	 * @return 返回附件实体Bean
	 * @exception ServiceException
	 */
	protected TNAttachment fileUpload(File file,String contentType,String uploadFileName,
			String destFileName,String destDir,String userId) throws ServiceException {
		return saveAtt(upload(file,contentType, uploadFileName, destFileName, destDir), userId);
	}
	
	/**
	 * 上传附件 <br />
	 * 上传附件的信息保存TNAttachment对象的表里面，并返回TNAttachment对象
	 * @param file
	 * @param contentType 文件类型
	 * @param uploadFileName 原文件名
	 * @param destFileName 文件名(不含后缀)
	 * @param destDir  保存目录
	 * @param userId 用户ID
	 * @return 返回附件实体Bean
	 * @exception ServiceException
	 */
	protected TNAttachment fileUpload(File file,String contentType,String uploadFileName,String userId) throws ServiceException {
		return saveAtt(upload(file,contentType, uploadFileName), userId);
	}
	
	
	/**
	 * 上传附件 <br />
	 * 上传附件的信息保存TNAttachment对象的表里面，并返回TNAttachment对象
	 * @param inputStream
	 * @param contentType 文件类型
	 * @param uploadFileName 原文件名
	 * @param fileSize 文件大小
	 * @param userId 用户ID
	 * @return 返回附件实体Bean
	 * @exception ServiceException
	 */
	protected TNAttachment fileUpload(InputStream inputStream,String contentType,String uploadFileName,long fileSize,String userId) throws ServiceException {
		return saveAtt(upload(inputStream,contentType, uploadFileName, fileSize), userId);
	}
	
	/**
	 * 上传附件 <br />
	 * 上传附件的信息保存TNAttachment对象的表里面，并返回TNAttachment对象
	 * @param inputStream
	 * @param contentType 文件类型
	 * @param uploadFileName 原文件名
	 * @param fileSize 文件大小
	 * @param destFileName 文件名(不含后缀)
	 * @param destDir  保存目录
	 * @param userId 用户ID
	 * @return 返回附件实体Bean
	 * @exception ServiceException
	 */
	protected TNAttachment fileUpload(InputStream inputStream,String contentType,long fileSize,String uploadFileName,
			String destFileName,String destDir,String userId) throws ServiceException {
		return saveAtt(upload(inputStream,contentType, uploadFileName,fileSize,destFileName,destDir), userId);
	}
	
	
	/**
	 * 保存附件
	 * @param smartResp
	 * @param userId
	 * @return 返回附件实体Bean
	 * @throws ServiceException
	 */
	private TNAttachment saveAtt(SmartResponse<UploadFileInfo> smartResp,String userId) throws ServiceException {
		TNAttachment attachment = null;
		if(OP_SUCCESS.equals(smartResp.getResult())) {
			attachment = new TNAttachment();
			UploadFileInfo uploadFileInfo = smartResp.getData();
			attachment.setFileSuffix(uploadFileInfo.getFileSuffix());
			attachment.setFileName(uploadFileInfo.getFileName());
			attachment.setFileType(uploadFileInfo.getFileType());
			attachment.setFilePath(uploadFileInfo.getFilePath());
			attachment.setFileSize(uploadFileInfo.getFileSize());
			attachment.setUserId(userId);
			SmartResponse<String> chRes = attServ.save(attachment);
			if(OP_SUCCESS.equals(chRes.getResult())) {
				attachment.setId(chRes.getData());
			}
			uploadFileInfo = null;
			chRes = null;
		}
		return attachment;
	}
	
}
