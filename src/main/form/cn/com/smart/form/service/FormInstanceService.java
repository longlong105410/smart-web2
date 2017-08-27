package cn.com.smart.form.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mixsmart.utils.StringUtils;

import cn.com.smart.bean.SmartResponse;
import cn.com.smart.form.bean.entity.TForm;
import cn.com.smart.form.bean.entity.TFormInstance;
import cn.com.smart.service.impl.MgrServiceImpl;
import cn.com.smart.web.bean.UserInfo;

/**
 * 表单实例 服务类
 * @author lmq  2017年8月28日
 * @version 1.0
 * @since 1.0
 */
@Service
public class FormInstanceService extends MgrServiceImpl<TFormInstance> {

    @Autowired
    private IFormDataService formDataServ;
    @Autowired
    private FormService formServ;
    
    /**
     * 创建表单实例
     * @param datas 表单数据
     * @param formDataId 表单数据ID
     * @param formId 表单ID
     * @param userInfo 用户信息
     * @return 返回创建结果
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public SmartResponse<String> create(Map<String,Object> datas, String formDataId, 
            String formId, UserInfo userInfo) {
        SmartResponse<String> smartResp = new SmartResponse<String>();
        smartResp.setMsg("表单提交失败");
        TForm form = formServ.find(formId).getData();
        if(null == form) {
            return smartResp;
        }
        String insTitle = formServ.getInstanceTitle(datas, formId, userInfo.getId(), form.getName());
        if(StringUtils.isEmpty(insTitle)) {
            insTitle = form.getName() + "("+userInfo.getFullName()+")"; 
        }
        smartResp = formDataServ.saveOrUpdateForm(datas, formDataId, formId,userInfo.getId(), 0);
        if(OP_SUCCESS.equals(smartResp.getResult())) {
            TFormInstance formInstance = new TFormInstance();
            formInstance.setFormDataId(formDataId);
            formInstance.setFormId(formId);
            formInstance.setOrgId(userInfo.getOrgId());
            formInstance.setTitle(insTitle);
            formInstance.setUserId(userInfo.getId());
            super.save(formInstance);
            smartResp.setMsg("表单提交成功");
        }
        return smartResp;
    }
    
}
