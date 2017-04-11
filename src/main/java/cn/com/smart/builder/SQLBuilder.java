package cn.com.smart.builder;

import com.mixsmart.utils.StringUtils;

/**
 * Builder SQL语句
 * @author lmq
 * @version 1.0
 * @since 1.0
 */
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
		String orderBy = "order by";
		int orderByPos = sql.lastIndexOf(" "+orderBy+" ");
		if(orderByPos == -1) {
			orderBy = orderBy.toUpperCase();
			orderByPos = sql.lastIndexOf(" "+orderBy+" ");
		}
		int bracketsPos = sql.lastIndexOf(")");
		StringBuilder countSqlBuilder = new StringBuilder();
		if(orderByPos > 0 && orderByPos > bracketsPos) {
			sql = org.apache.commons.lang3.StringUtils.substringBeforeLast(sql, " "+orderBy+" ");
		}
		countSqlBuilder.append("select count(*) from ");
		boolean isLoop = true;
		String select = "select";
		String from = "from";
		int selectPos = sql.indexOf(select+" ", 0);
		if(selectPos == -1) {
			selectPos = sql.indexOf(select.toUpperCase()+" ", 0);
		}
		int fromPos = 0;
		int keyLen = 6;
		selectPos = selectPos + keyLen;
		while(isLoop) {
			fromPos = sql.indexOf(" "+from+" ", selectPos);
			if(fromPos == -1) {
				fromPos = sql.indexOf(" "+from.toUpperCase()+" ", selectPos);
			}
			int selectPosTmp = sql.indexOf(select+" ", selectPos);
			if(selectPosTmp == -1) {
				selectPos = sql.indexOf(select.toUpperCase()+" ", selectPos);
			} else {
				selectPos = selectPosTmp;
			}
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
