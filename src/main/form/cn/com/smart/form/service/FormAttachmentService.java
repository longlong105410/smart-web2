package cn.com.smart.form.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mixsmart.exception.NullArgumentException;
import com.mixsmart.utils.CollectionUtils;
import com.mixsmart.utils.LoggerUtils;
import com.mixsmart.utils.StringUtils;

import cn.com.smart.bean.SmartResponse;
import cn.com.smart.form.bean.entity.TFormAttachment;
import cn.com.smart.res.SQLResUtil;
import cn.com.smart.service.impl.MgrServiceImpl;
import cn.com.smart.web.bean.entity.TNAttachment;
import cn.com.smart.web.service.OPService;

/**
 * 表单附件服务类
 * @author lmq  2017年8月14日
 * @version 1.0
 * @since 1.0
 */
@Service
public class FormAttachmentService extends MgrServiceImpl<TFormAttachment> {

    @Autowired
    private OPService opServ;
    
    /**
     * 保存表单附件
     * @param att 附件实体对象
     * @param formId  表单ID
     * @param formDataId 表单数据ID
     * @param userId 用户ID（上传附件用户ID）
     * @return 返回表单附件保存结果
     */
    public SmartResponse<String> saveAttachment(TNAttachment att, 
            String formId, String formDataId) {
        SmartResponse<String> smartResp = new SmartResponse<String>();
        if(null == att || StringUtils.isEmpty(formId) || StringUtils.isEmpty(formDataId)) {
            throw new NullArgumentException("参数为空");
        }
        TFormAttachment formAtt = new TFormAttachment();
        formAtt.setAttachmentId(att.getId());
        formAtt.setFormDataId(formDataId);
        formAtt.setFormId(formId);
        smartResp = super.save(formAtt);
        return smartResp;
    }
    
    
    /**
     * 更新表单字段为空 
     * @param fieldId
     * @param formDataId
     * @param attId
     */
    public void updateFormField(String fieldId, String formDataId, String attId) {
        if(StringUtils.isEmpty(fieldId) || StringUtils.isEmpty(formDataId) || StringUtils.isEmpty(attId)) {
            return;
        }
        LoggerUtils.debug(logger, "正在将附件字段更新为空");
        Map<String, Object> param = new HashMap<String, Object>(1);
        param.put("fieldId", fieldId);
        SmartResponse<Object> smartResp = opServ.getDatas("get_tablename_fieldname_byfieldid", param);
        if(OP_SUCCESS.equals(smartResp.getResult())) {
            Object[] array = (Object[])smartResp.getDatas().get(0);
            String tableName = StringUtils.handNull(array[0]);
            String fieldName = StringUtils.handNull(array[1]);
            param.clear();
            param.put("formDataId", formDataId);
            
            String querySql = SQLResUtil.getOpSqlMap().getSQL("get_field_value");
            if(StringUtils.isNotEmpty(querySql)) {
                String sourceValue = null;
                String targetValue = null;
                querySql = querySql.replace("${tableName}", tableName).replace("${fieldName}", fieldName);
                List<Object> list = getDao().queryObjSql(querySql, param);
                if(CollectionUtils.isNotEmpty(list)) {
                    sourceValue = StringUtils.handNull(list.get(0));
                }
                if(StringUtils.isNotEmpty(sourceValue)) {
                    String[] sourceValues = sourceValue.split(MULTI_VALUE_SPLIT);
                    sourceValues = ArrayUtils.removeElement(sourceValues, attId);
                    targetValue = com.mixsmart.utils.ArrayUtils.arrayToString(sourceValues, MULTI_VALUE_SPLIT);
                }
                String fieldUpateValue = fieldName+"=";
                if(StringUtils.isEmpty(targetValue)) {
                    fieldUpateValue += "null";
                } else {
                    fieldUpateValue += "'"+targetValue+"'";
                }
                
                String updateSql = SQLResUtil.getOpSqlMap().getSQL("update_field_value");
                if(StringUtils.isNotEmpty(updateSql)) {
                    updateSql = updateSql.replace("${tableName}", tableName).replace("${fieldName}", fieldUpateValue);
                    if(getDao().executeSql(updateSql, param)>0) {
                        LoggerUtils.debug(logger, tableName+"表中的["+fieldName+"]附件字段更新为空[成功]");
                    } else {
                        LoggerUtils.error(logger, tableName+"表中的["+fieldName+"]附件字段更新为空[失败]");
                    }
                } else {
                    LoggerUtils.error(logger, "[update_field_value]对应的SQL语句为空");
                }
            } else {
                LoggerUtils.error(logger, "[get_field_value]对应的SQL语句为空");
            }
        } else {
            LoggerUtils.error(logger, "附件字段更新为空[失败]，原因是：获取表名及字段名称失败");
        }
    }
}
