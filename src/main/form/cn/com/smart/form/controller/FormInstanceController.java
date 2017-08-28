package cn.com.smart.form.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.snaker.engine.helper.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mixsmart.utils.StringUtils;

import cn.com.smart.bean.SmartResponse;
import cn.com.smart.filter.bean.FilterParam;
import cn.com.smart.flow.bean.QueryFormData;
import cn.com.smart.form.bean.entity.TForm;
import cn.com.smart.form.service.FormInstanceService;
import cn.com.smart.form.service.FormService;
import cn.com.smart.form.service.IFormDataService;
import cn.com.smart.web.bean.RequestPage;
import cn.com.smart.web.helper.HttpRequestHelper;
import cn.com.smart.web.service.OPService;
import cn.com.smart.web.tag.bean.ALink;
import cn.com.smart.web.tag.bean.DelBtn;
import cn.com.smart.web.tag.bean.EditBtn;
import cn.com.smart.web.tag.bean.RefreshBtn;

/**
 * 表单实例 控制器类
 * @author lmq  2017年8月27日
 * @version 1.0
 * @since 1.0
 */
@Controller
@RequestMapping("/form/instance")
public class FormInstanceController extends BaseFormController {

    private static final String VIEW_DIR = BASE_FORM_VIEW_DIR+"instance/";
    @Autowired
    private OPService opServ;
    @Autowired
    private FormService formServ;
    @Autowired
    private IFormDataService formDataServ;
    @Autowired
    private FormInstanceService formInsServ;
    
    /**
     * 表单实例列表
     * @param request
     * @param searchFilter
     * @param page
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request, FilterParam searchFilter, RequestPage page) {
        ModelAndView modelView = new ModelAndView();
        SmartResponse<Object> smartResp = opServ.getDatas("get_form_instance_list", searchFilter, page.getStartNum(), page.getPageSize());
        String uri = HttpRequestHelper.getCurrentUri(request);
        refreshBtn = new RefreshBtn(uri, null, null);
        editBtn = new EditBtn("edit", "form/instance/edit", null, "修改表单数据", null);
        delBtn = new DelBtn("form/instance/delete", "您确定要删除选中的表单数据吗？", uri, null, null);
        ALink alink = new ALink("form/instance/view", null, "查看表单信息");
        alink.setParamIndex("4,5");
        alink.setParamName("formId, formDataId");
        alinks = new ArrayList<ALink>(1);
        alinks.add(alink);
        
        ModelMap modelMap = modelView.getModelMap();
        modelMap.put("smartResp", smartResp);
        modelMap.put("refreshBtn", refreshBtn);
        modelMap.put("delBtn", delBtn);
        modelMap.put("editBtn", editBtn);
        modelMap.put("alinks", alinks);
        modelView.setViewName(VIEW_DIR+"list");
        return modelView;
    }
    
    /**
     * 删除表单实例
     * @param id 实例ID
     * @return 返回删除结果（JSON格式）
     */
    @RequestMapping(value="/delete", produces="application/json;charset=UTF-8")
    @ResponseBody
    public SmartResponse<String> delete(String id) {
        return formInsServ.delete(id);
    }
    
    /**
     * 修改表单数据
     * @param formId 表单ID
     * @param formDataId 表单数据ID
     * @return
     */
    @RequestMapping("/edit")
    public ModelAndView edit(String formId, String formDataId) {
        ModelAndView modelView = new ModelAndView();
        handleView(modelView.getModelMap(), formId, formDataId);
        modelView.setViewName(VIEW_DIR+"edit");
        return modelView;
    }
    
    /**
     * 查看表单实例
     * @param id 表单实例ID
     * @return 
     */
    @RequestMapping("/view")
    public ModelAndView view(String formId, String formDataId) {
        ModelAndView modelView = new ModelAndView();
        handleView(modelView.getModelMap(), formId, formDataId);
        modelView.setViewName(VIEW_DIR+"view");
        return modelView;
    }
    
    /**
     * 处理试图
     * @param modelMap
     * @param formId
     * @param formDataId
     */
    private void handleView(ModelMap modelMap, String formId, String formDataId) {
        if(StringUtils.isNotEmpty(formId) && StringUtils.isNotEmpty(formDataId)) {
            TForm form = formServ.find(formId).getData();
            modelMap.put("objBean", form);
            SmartResponse<QueryFormData> smartResp = formDataServ.getFormDataByFormDataId(formDataId, formId);
            String output = JsonHelper.toJson(smartResp);
            output = StringUtils.repaceSpecialChar(output);
            output = StringUtils.repaceSlash(output);
            modelMap.put("output", output);
        }
    }
}
