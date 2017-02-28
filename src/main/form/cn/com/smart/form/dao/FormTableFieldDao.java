package cn.com.smart.form.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.smart.form.bean.entity.TCreateTableField;
import cn.com.smart.web.bean.entity.TNDict;
import cn.com.smart.web.dao.impl.RalteDictDaoImpl;

/**
 * @author lmq
 * @create 2015年6月25日
 * @version 1.0 
 * @since 
 *
 */
@Repository("formTableFieldDao")
public class FormTableFieldDao extends RalteDictDaoImpl<TCreateTableField>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8374095278935807907L;

	@Override
	public void asgmtValueByDict(List<TCreateTableField> ts, List<TNDict> dicts) {
		for (TCreateTableField tableField : ts) {
			for (TNDict dict : dicts) {
				if(tableField.getDataFormat().equals(dict.getBusiValue())) {
					tableField.setDataFormat(dict.getBusiName());
					break;
				}
			}
		}
	}

	@Override
	public void asgmtValueByDict(TCreateTableField t, List<TNDict> dicts) {
		for (TNDict dict : dicts) {
			if(t.getDataFormat().equals(dict.getBusiValue())) {
				t.setDataFormat(dict.getBusiName());
				break;
			}
		}
	}

}
