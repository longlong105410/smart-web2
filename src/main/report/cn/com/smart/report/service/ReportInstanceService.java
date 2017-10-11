package cn.com.smart.report.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mixsmart.exception.NullArgumentException;
import com.mixsmart.utils.StringUtils;

import cn.com.smart.bean.SmartResponse;
import cn.com.smart.helper.ObjectHelper;
import cn.com.smart.report.bean.entity.TReportSqlResourse;
import cn.com.smart.report.dao.ReportDao;
import cn.com.smart.service.impl.BaseServiceImpl;
import cn.com.smart.web.helper.PageHelper;

/**
 * 报表实例服务类
 * @author lmq  2017年10月11日
 * @version 1.0
 * @since 1.0
 */
@Service
public class ReportInstanceService extends BaseServiceImpl {

    @Autowired
    private ReportDao reportDao;
    
    /**
     * 查询数据(分页)
     * @param sqlResource 报表SQL资源对象
     * @param params 查询参数
     * @param start 开始
     * @param rows 每次显示数
     * @return 返回SmartResponse对象
     */
    public SmartResponse<Object> getDatas(TReportSqlResourse sqlResource, Map<String,Object> params, int start, int rows) {
        if(null == sqlResource) {
            throw new NullArgumentException("sqlResource对象为空");
        }
        if(StringUtils.isEmpty(sqlResource.getSql())) {
            throw new NullArgumentException("SQL语句为空");
        }
        SmartResponse<Object> smartResp = new SmartResponse<Object>();
        smartResp.setResult(OP_NOT_DATA_SUCCESS);
        smartResp.setMsg(OP_NOT_DATA_SUCCESS_MSG);
        long totalNum = reportDao.countSql(sqlResource.getSql(), params);
        if(start <= totalNum) {
            List<Object> objs = reportDao.queryObjSql(sqlResource.getSql(), params, start, rows);
            if(null != objs && objs.size()>0) {
                objs = ObjectHelper.handleObjDate(objs);
                smartResp.setResult(OP_SUCCESS);
                smartResp.setMsg(OP_SUCCESS_MSG);
                smartResp.setDatas(objs);
                smartResp.setPerPageSize(rows);
                smartResp.setTotalNum(totalNum);
                smartResp.setTotalPage(PageHelper.getTotalPage(totalNum, rows));
                smartResp.setSize(objs.size());
            }
            objs = null;
        }
        return smartResp;
    }
    
}
