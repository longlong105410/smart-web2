package cn.com.smart.web.helper;

import org.apache.log4j.Logger;

import cn.com.smart.init.config.InitSysConfig;
import cn.com.smart.web.constant.IActionConstant;

/**
 * 分页助手类
 * @author lmq
 *
 */
public class PageHelper {

    protected static final Logger log = Logger.getLogger(PageHelper.class);
	
    
    public static int getPerPageSize(int perPageSize) {
		if(perPageSize<1) {
			perPageSize = getPerPageSize();
		}
		return perPageSize;
	}
    
    public static int getPerPageSize() {
    	int perPageSize = 0;
    	try {
			perPageSize = Integer.parseInt(InitSysConfig.getInstance().getValue("page.per.size"));
		} catch (Exception e) {
			perPageSize = IActionConstant.PRE_PAGE_SIZE;
		}
		return perPageSize;
	}
    
    /**
	 * 获取当前页---分页
	 * @return
	 */
	public static int getPage(int page) {
		if(page<1) {
			page = 1;
		}
		return page;
	}
    
    /**
     * 
     * @param total 总数据数
     * @param pageSize 每页显示数
     * @return
     */
	public static int getTotalPage(long total,int pageSize) {
		return (int) Math.ceil((double)total/pageSize);
	}
	
	/**
	 * 获取开始数据---分页
	 * @return
	 */
	public static int getStartNum(int page,int pageSize) {
		int startNum = (getPage(page)-1)*getPerPageSize(pageSize);
		return startNum;
	}
	
	/**
	 * 获取开始数据---分页
	 * @return
	 */
	public static int getStartNum(int page) {
		int startNum = (getPage(page)-1)*getPerPageSize();
		return startNum;
	}
	
	
}
