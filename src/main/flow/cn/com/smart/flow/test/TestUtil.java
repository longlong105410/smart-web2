package cn.com.smart.flow.test;

import cn.com.smart.utils.StringUtil;

/**
 * @author lmq
 * @create 2015年6月19日
 * @version 1.0 
 * @since 
 *
 */
public class TestUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String num = "087";
		System.out.println(StringUtil.isInteger(num));
		Integer n = Integer.parseInt(num);
		System.out.println(n);
	}

}
