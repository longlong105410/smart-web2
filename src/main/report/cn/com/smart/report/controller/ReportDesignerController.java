package cn.com.smart.report.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.com.smart.bean.SmartResponse;
import cn.com.smart.report.bean.entity.TReport;
import cn.com.smart.report.service.ReportService;
import cn.com.smart.web.bean.UserInfo;

/**
 * 报表设计器 -- 控制类
 * @author lmq  2017年9月10日
 * @version 1.0
 * @since 1.0
 */
@Controller
@RequestMapping("/report/designer")
public class ReportDesignerController extends BaseReportController {

    private static final String VIEW_DIR = BASE_REPORT_VIEW_DIR + "designer";
    @Autowired
    private ReportService reportServ;
    
    /**
     * 设计器默认方法
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request, String id) {
        ModelAndView modelView = new ModelAndView();
        modelView.setViewName(VIEW_DIR+"/index");
        return modelView;
    }
    
    /**
     * 保存信息
     * @param session
     * @param report
     * @return
     */
    @RequestMapping(value="/save", method = RequestMethod.POST, produces="application/json; chartset=UTF-8")
    @ResponseBody
    public SmartResponse<String> save(HttpSession session, TReport report) {
        SmartResponse<String> smartResp = new SmartResponse<String>();
        UserInfo userInfo = super.getUserInfoFromSession(session);
        report.setUserId(userInfo.getId());
        smartResp = reportServ.saveOrUpdate(report);
        return smartResp;
    }
}
