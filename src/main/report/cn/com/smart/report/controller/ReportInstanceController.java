package cn.com.smart.report.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mixsmart.enums.YesNoType;
import com.mixsmart.exception.NullArgumentException;
import com.mixsmart.utils.CollectionUtils;
import com.mixsmart.utils.StringUtils;

import cn.com.smart.bean.SmartResponse;
import cn.com.smart.report.bean.entity.TReport;
import cn.com.smart.report.bean.entity.TReportField;
import cn.com.smart.report.bean.entity.TReportProperties;
import cn.com.smart.report.service.ReportInstanceService;
import cn.com.smart.report.service.ReportService;
import cn.com.smart.web.bean.RequestPage;
import cn.com.smart.web.bean.UserInfo;
import cn.com.smart.web.constant.enums.PageOpenStyle;
import cn.com.smart.web.controller.base.BaseController;
import cn.com.smart.web.helper.HttpRequestHelper;
import cn.com.smart.web.tag.bean.ALink;
import cn.com.smart.web.tag.bean.CustomTableCell;
import cn.com.smart.web.tag.bean.ICustomCellCallback;
import cn.com.smart.web.tag.bean.PageParam;
import cn.com.smart.web.tag.bean.RefreshBtn;

/**
 * 报表实例
 * @author lmq  2017年10月11日
 * @version 1.0
 * @since 1.0
 */
@Controller
@RequestMapping("/report/instance")
public class ReportInstanceController extends BaseController {
    
    private static final String VIEW_DIR = "report/instance";
    
    @Autowired
    private ReportInstanceService reportInstServ;
    @Autowired
    private ReportService reportServ;
    
    /**
     * 报表列表
     * @param request HttpServletRequest请求对象
     * @param reportId 报表ID
     * @param page 分页对象
     * @return 返回列表视图
     */
    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request, String reportId, RequestPage page) {
        ModelAndView modelView = new ModelAndView();
        if(StringUtils.isEmpty(reportId)) {
            throw new NullArgumentException("reportId参数为空");
        }
        String uri = HttpRequestHelper.getCurrentUri(request);
        uri += "?reportId="+StringUtils.handleNull(reportId);
        TReport report = reportServ.queryAssoc(reportId);
        List<TReportField> fields = report.getFields();
        if(CollectionUtils.isEmpty(fields)) {
            throw new IllegalArgumentException("报表没有字段信息");
        }
        UserInfo userInfo = super.getUserInfoFromSession(request);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orgIds", userInfo.getOrgIds().toArray());
        //设置搜索字段
        List<TReportField> searchFields = getSearchFields(fields);
        if(CollectionUtils.isNotEmpty(searchFields)) {
            for (TReportField reportField : searchFields) {
                params.put(reportField.getSearchName(), request.getParameter(reportField.getSearchName()));
            }
        }
        SmartResponse<Object> smartResp = reportInstServ.getDatas(report.getSqlResource(), params, page.getStartNum(), page.getPageSize());
        //设置链接
        List<TReportField> alinkFields = getALinkFields(fields);
        if(CollectionUtils.isNotEmpty(alinkFields)) {
            alinks = new ArrayList<ALink>();
            for(TReportField alinkField : alinkFields) {
                ALink alink = new ALink();
                alink.setUri(alinkField.getUrl());
                if("_blank".equals(alinkField.getOpenUrlType())) {
                    alink.setaTarget("_blank");
                } else if("popup_win".equals(alinkField.getOpenUrlType())){
                    alink.setClassTarget(PageOpenStyle.OPEN_BLANK);
                } else {
                    alink.setClassTarget(PageOpenStyle.valueOf(alinkField.getOpenUrlType()));
                }
                alink.setDialogTitle("查看"+alinkField.getTitle());
                alink.setParamIndex(alinkField.getParamValue());
                alink.setParamName(alinkField.getParamName());
                alink.setLinkPostion(alinkField.getSortOrder().toString());
                alink.setDialogWidth("");
                alinks.add(alink);
            }
        }
        //设置自定义列
        List<TReportField> customCellFields = getCustomCellFields(fields);
        List<CustomTableCell> customTableCells = null;
        if(CollectionUtils.isNotEmpty(customCellFields)) {
            customTableCells = new ArrayList<CustomTableCell>();
            for(TReportField cellField : customCellFields) {
                ICustomCellCallback callback = getCustomCellCallback(cellField.getCustomClass());
                if(null != callback) {
                    CustomTableCell cell = new CustomTableCell();
                    cell.setCellCallback(callback);
                    customTableCells.add(cell);
                }
            }
            customTableCells = customTableCells.size() > 0 ? customTableCells : null;
        }
        pageParam = new PageParam(uri, null, page.getPage(), page.getPageSize());
        refreshBtn  = new RefreshBtn(uri, null, null);
        refreshBtn.setIsAuth(YesNoType.NO.getValue());
        TReportProperties reportProp = report.getProperties();
        String isOriginalTable = "0";
        if(YesNoType.NO.getIndex() == reportProp.getIsFixedHeader()) {
            isOriginalTable = "1";
        }
        ModelMap modelMap = modelView.getModelMap();
        modelMap.put("uri", uri);
        modelMap.put("headerWidths", getWidths(fields));
        modelMap.put("headerTitles", getHeaderTitles(reportProp, fields));
        modelMap.put("reportProp", reportProp);
        modelMap.put("searchFields", searchFields);
        modelMap.put("smartResp", smartResp);
        modelMap.put("alinks", alinks);
        modelMap.put("customCells", customTableCells);
        modelMap.put("pageParam", pageParam);
        modelMap.put("refreshBtn", refreshBtn);
        modelMap.put("isOriginalTable", isOriginalTable);
        modelView.setViewName(VIEW_DIR+"/list");
        return modelView;
    }
    
    /**
     * 获取搜索字段列表
     * @param fields 字段列表
     * @return 返回搜索字段列表
     */
    private List<TReportField> getSearchFields(List<TReportField> fields) {
        List<TReportField> searchFields = new ArrayList<TReportField>();
        for (TReportField reportField : fields) {
            if(StringUtils.isNotEmpty(reportField.getSearchName())) {
                searchFields.add(reportField);
            }
        }
        return searchFields.size() > 0 ? searchFields : null;
    }
    
    /**
     * 获取链接字段
     * @param fields 字段列表
     * @return 返回链接字段列表
     */
    private List<TReportField> getALinkFields(List<TReportField> fields) {
        List<TReportField> alinkFields = new ArrayList<TReportField>();
        for (TReportField reportField : fields) {
            if(StringUtils.isNotEmpty(reportField.getUrl()) 
                    || StringUtils.isNotEmpty(reportField.getParamName()) 
                    || StringUtils.isNotEmpty(reportField.getParamValue())) {
                alinkFields.add(reportField);
            }
        }
        return alinkFields.size() > 0 ? alinkFields : null;
    }
    
    /**
     * 获取字段宽度
     * @param fields 报表字段列表
     * @return 返回字段宽度；多个宽度之间用英文逗号分隔
     */
    private String getWidths(List<TReportField> fields) {
        StringBuilder widthBuilder = new StringBuilder();
        for (TReportField reportField : fields) {
            widthBuilder.append(StringUtils.handleNull(reportField.getWidth())).append(",");
        }
        widthBuilder.delete(widthBuilder.length()-1, widthBuilder.length());
        if(!widthBuilder.toString().matches(".*\\d+.*")) {
            return null;
        } else {
            return widthBuilder.toString();
        }
    }
    
    /**
     * 获取头标题
     * @param reportProp 报表属性实体类
     * @param fields 字段实体对象列表
     * @return 返回标题，多个之间用英文逗号分隔
     */
    private String getHeaderTitles(TReportProperties reportProp, List<TReportField> fields) {
        StringBuilder titleBuilder = new StringBuilder();
        if(YesNoType.YES.getIndex() == reportProp.getIsHasId() && 
                YesNoType.YES.getIndex() == reportProp.getIsShowId()) {
            titleBuilder.append("ID,");
        }
        for (TReportField reportField : fields) {
            titleBuilder.append(reportField.getTitle()).append(",");
        }
        titleBuilder.delete(titleBuilder.length()-1, titleBuilder.length());
        return titleBuilder.toString();
    }
    
    /**
     * 获取自定义单元格的字段列表
     * @param fields 报表字段列表
     * @return 返回设置了自定义类的字段列表
     */
    private List<TReportField> getCustomCellFields(List<TReportField> fields) {
        List<TReportField> customCellFields = new ArrayList<TReportField>();
        for (TReportField reportField : fields) {
            if(StringUtils.isNotEmpty(reportField.getCustomClass())) {
                customCellFields.add(reportField);
            }
        }
        return customCellFields.size() > 0 ? customCellFields : null;
    }

    /**
     * 
     * @param customClass
     * @return
     */
    private ICustomCellCallback getCustomCellCallback(String customClass) {
        try {
            Class<?> clazz = Class.forName(customClass);
            if(clazz.isAssignableFrom(ICustomCellCallback.class)) {
                return (ICustomCellCallback)clazz.newInstance();
            } else {
                return null;
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        } 
        return null;
    }
}
