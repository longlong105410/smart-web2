package cn.com.smart.report.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mixsmart.exception.NullArgumentException;
import com.mixsmart.utils.StringUtils;

import cn.com.smart.bean.SmartResponse;
import cn.com.smart.exception.ServiceException;
import cn.com.smart.report.bean.entity.TReport;
import cn.com.smart.report.bean.entity.TReportField;
import cn.com.smart.report.bean.entity.TReportProperties;
import cn.com.smart.report.bean.entity.TReportSqlResourse;
import cn.com.smart.service.impl.MgrServiceImpl;

@Service
public class ReportService extends MgrServiceImpl<TReport> {

    @Autowired
    private ReportFieldService reportFieldServ;
    @Autowired
    private ReportPropertiesService reportPropServ;
    @Autowired
    private ReportSqlResourceService reportSqlResServ;
    
    /**
     * 保存或更新报表设计
     * @param report 报表实体类
     * @return 返回结果
     */
    public SmartResponse<String> saveOrUpdate(TReport report) {
        SmartResponse<String> smartResp = new SmartResponse<String>();
        checkRequire(report);
        if(StringUtils.isEmpty(report.getId())) {
            smartResp = this.save(report);
        } else {
            smartResp = this.update(report);
        }
        return smartResp;
    }
    
    @Override
    public SmartResponse<String> save(TReport report) throws ServiceException {
        SmartResponse<String> smartResp = super.save(report);
        if(OP_SUCCESS.equals(smartResp.getResult())) {
            TReportProperties reportProp = report.getProperties();
            reportProp.setReportId(report.getId());
            reportPropServ.save(reportProp);
            
            TReportSqlResourse reportSqlRes = report.getSqlResource();
            reportSqlRes.setSql(handleSql(reportSqlRes.getSql()));
            reportSqlRes.setReportId(report.getId());
            reportSqlResServ.save(reportSqlRes);
            
            List<TReportField> reportFields = report.getFields();
            for (TReportField reportField : reportFields) {
                reportField.setReportId(report.getId());
                //标题为空的字段不保存
                if(StringUtils.isNotEmpty(reportField.getTitle())) {
                    reportFieldServ.save(reportField);
                }
            }
        }
        return smartResp;
    }

    @Override
    public SmartResponse<String> update(TReport report) throws ServiceException {
        SmartResponse<String> smartResp = super.update(report);
        TReportProperties reportProp = report.getProperties();
        reportProp.setReportId(report.getId());
        reportPropServ.update(reportProp);
        
        TReportSqlResourse reportSqlRes = report.getSqlResource();
        reportSqlRes.setSql(handleSql(reportSqlRes.getSql()));
        reportSqlRes.setReportId(report.getId());
        reportSqlResServ.update(reportSqlRes);
        reportFieldServ.updateField(report.getId(), report.getFields());
        smartResp.setResult(OP_SW);
        smartResp.setMsg("修改成功");
        return smartResp;
    }
    
    /**
     * 验证必填属性
     * @param report 报表实体类
     */
    private void checkRequire(TReport report) {
        if(StringUtils.isEmpty(report.getName()) 
                || null == report.getProperties() 
                || null == report.getSqlResource() 
                || StringUtils.isEmpty(report.getSqlResource().getName()) 
                || StringUtils.isEmpty(report.getSqlResource().getSql())) {
            throw new NullArgumentException("必填字段为空");
        }
    }
    
    private String handleSql(String sql) {
        if(StringUtils.isNotEmpty(sql)) {
            sql = sql.replaceAll("\n", " ");
            sql = sql.replaceAll("\r", " ");
            sql = sql.replaceAll("\t", " ");
            sql = sql.replaceAll(" +", " ");
        }
        return sql;
    }
}
