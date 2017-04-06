package cn.com.smart.web.builder;

import com.mixsmart.utils.StringUtils;

public class SQLBuilder {

	/**
	 * 根据提供的查询SQL语句生成统计语句
	 * @param sql SQL语句
	 * @return 返回统计语句
	 */
	public static String countSQL(String sql) {
		if(StringUtils.isEmpty(sql)) {
			return null;
		}
		//去掉最后一出现的order by 语句
		int orderByPos = sql.lastIndexOf(" order by ");
		int bracketsPos = sql.lastIndexOf(")");
		StringBuilder countSqlBuilder = new StringBuilder();
		if(orderByPos > 0 && orderByPos > bracketsPos) {
			sql = org.apache.commons.lang3.StringUtils.substringBeforeLast(sql, " order by ");
		} 
		countSqlBuilder.append("select count(*) from ");
		boolean isLoop = true;
		int selectPos = sql.indexOf("select ", 0);
		int fromPos = 0;
		int keyLen = 6;
		selectPos = selectPos + keyLen;
		while(isLoop) {
			fromPos = sql.indexOf(" from ", selectPos);
			selectPos = sql.indexOf("select ", selectPos);
			if(selectPos >-1 && selectPos < fromPos ) {
				selectPos = fromPos + keyLen;
			} else {
				isLoop = false;
			}
		}
		countSqlBuilder.append(sql.substring(fromPos + keyLen, sql.length()));
		return countSqlBuilder.toString();
	}
	
}
