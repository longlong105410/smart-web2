package cn.com.smart.flow.helper;

import java.util.Map;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.com.smart.bean.SmartResponse;
import cn.com.smart.constant.IConstant;
import cn.com.smart.flow.bean.SubmitFormData;
import cn.com.smart.flow.service.FlowService;
import cn.com.smart.service.SmartContextService;
import cn.com.smart.web.bean.entity.TNAttachment;
import cn.com.smart.web.upload.AttachmentUploadHandler;

import com.mixsmart.utils.StringUtils;

/**
 * 表单上传文件助手
 * @author lmq  2017年4月12日
 * @version 1.0
 * @since 1.0
 */
public class FormUploadFileHelper {
	
	/**
	 * 附件字段后缀
	 */
	private static final String ATT_FIELD_SUFFIX = "_file";

	private MultipartHttpServletRequest multiRequest;
	private Map<String, Object> formArgs;
	private SubmitFormData submitFormData;
	private String userId;
	
	private AttachmentUploadHandler uploadHandler;
	private FlowService flowServ;

	public FormUploadFileHelper(MultipartHttpServletRequest multiRequest,
			Map<String, Object> formArgs, SubmitFormData submitFormData, String userId) {
		this.multiRequest = multiRequest;
		this.formArgs = formArgs;
		this.submitFormData = submitFormData;
		this.userId = userId;
		uploadHandler = SmartContextService.find(AttachmentUploadHandler.class);
		flowServ = SmartContextService.find(FlowService.class);
	}
	
	/**
	 * 开始上传文件
	 */
	public void upload() {
		Map<String, MultipartFile> fileMaps = multiRequest.getFileMap();
		if(null == fileMaps || fileMaps.size() == 0) {
			return;
		}
		Set<Map.Entry<String,MultipartFile>> sets = fileMaps.entrySet();
		try {
			for (Map.Entry<String,MultipartFile> set : sets) {
				String id = upload(set.getValue());
				if(StringUtils.isNotEmpty(id)) {
					String key = set.getKey();
					if(key.endsWith(ATT_FIELD_SUFFIX)) {
						String fieldId = key.substring(0, id.length() - (ATT_FIELD_SUFFIX.length()-1));
						String value = StringUtils.handNull(formArgs.get(fieldId));
						if(StringUtils.isNotEmpty(value)) {
							value +=IConstant.MULTI_VALUE_SPLIT + id;
						} else {
							value = id;
						}
						formArgs.put(fieldId, value);
					} else {
						formArgs.put(key, id);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 开始上传文件（保存文件）
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private String upload(MultipartFile file) throws Exception {
		TNAttachment att = uploadHandler.fileUpload(file.getInputStream(), file.getContentType(), file.getOriginalFilename(),file.getSize() ,userId);
		String id = null;
		if(null != att) {
			SmartResponse<String> chRes = flowServ.saveAttachment(att, this.submitFormData);
			if(IConstant.OP_SUCCESS.equals(chRes.getResult())) {
				id = att.getId();
			} else {
				flowServ.deleteAttachment(att.getId());
			}
			chRes = null;
		}
		return id;
	}
}
