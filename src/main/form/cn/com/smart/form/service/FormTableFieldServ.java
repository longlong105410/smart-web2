package cn.com.smart.form.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.com.smart.form.bean.entity.TCreateTableField;
import cn.com.smart.form.dao.FormTableFieldDao;
import cn.com.smart.service.impl.MgrServiceImpl;

@Service
public class FormTableFieldServ extends MgrServiceImpl<TCreateTableField> {

	public List<TCreateTableField> getTableFields(String[] fieldIds) {
		return getDao().getTableFields(fieldIds);
	}

	@Override
	public FormTableFieldDao getDao() {
		return (FormTableFieldDao)super.getDao();
	}
	
	
}
