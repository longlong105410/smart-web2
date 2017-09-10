package cn.com.smart.report.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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
    
}
