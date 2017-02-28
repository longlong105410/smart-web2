package cn.com.smart.form.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.smart.bean.SmartResponse;
import cn.com.smart.form.bean.entity.TForm;
import cn.com.smart.form.parser.factory.FormParserFactory;
import cn.com.smart.form.parser.factory.NotFindParserException;
import cn.com.smart.service.impl.MgrServiceImpl;

import com.mixsmart.utils.StringUtils;

/**
 * 
 * @author lmq
 * @create 2015年7月4日
 * @version 1.0 
 * @since 
 *
 */
@Service
public class FormService extends MgrServiceImpl<TForm> {
	
	@Autowired
	private DynamicFormManager formManager;
	
	/**
	 * 解析表单
	 * @param form
	 * @param dataMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public SmartResponse<String> parseForm(TForm form,Map<String,Object> dataMap) {
		SmartResponse<String> smartResp = new SmartResponse<String>();
		if(null == form || null == dataMap || dataMap.size()<1) {
			return smartResp;
		}
		boolean isUpdate = true;
		if(StringUtils.isEmpty(form.getId())) {
			form.setId(StringUtils.createSerialNum());
			isUpdate = false;
		}
        String parseHtml = (String)dataMap.get("parse");
        List<Map<String, Object>> lists = (List<Map<String, Object>>)dataMap.get("data");
        for (Map<String, Object> mapTmp : lists) {
        	Object plugin = mapTmp.get("leipiplugins");
        	if(null != plugin) {
            	try {
            		//解析表单
            		FormParserFactory parserFactory = FormParserFactory.getInstance();
					String parseContent = parserFactory.parse(plugin.toString(), mapTmp);
					if(StringUtils.isNotEmpty(parseContent)) {
						parseHtml = parseHtml.replace("{"+mapTmp.get("name")+"}", parseContent);
					}
				} catch (NotFindParserException e) {
					e.printStackTrace();
				}
        	}
		}
        Map<String, Object> datas = (Map<String, Object>)dataMap.get("add_fields");
        if(null != datas && datas.size()>0)
        	formManager.process(form, datas);
		String template = (String)dataMap.get("template");
		form.setOriginalHtml(template);
		form.setParseHtml(parseHtml);
		if(isUpdate) {
			smartResp = update(form);
		} else {
			smartResp = save(form);
		}
		return smartResp;
	}
	
}
