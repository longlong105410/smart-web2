package cn.com.smart.flow.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.smart.bean.SmartResponse;
import cn.com.smart.dao.impl.OPDao;
import cn.com.smart.exception.DaoException;
import cn.com.smart.flow.bean.QueryFormData;
import cn.com.smart.form.bean.TableFieldMap;
import cn.com.smart.form.enums.FormPluginType;
import cn.com.smart.form.service.FormTableService;
import cn.com.smart.utils.DateUtil;
import cn.com.smart.web.constant.IWebConstant;

import com.mixsmart.utils.ArrayUtils;
import com.mixsmart.utils.CollectionUtils;
import com.mixsmart.utils.StringUtils;

/**
 * 处理表单数据
 * @author lmq <br />
 * 2015年7月7日
 * @version 1.0
 * @since 1.0
 */
@Component
public class FormDataBusi {

	@Autowired
	private OPDao opDao;
	@Autowired
	private FormTableService formTableServ;
	
	/**
	 * 获取表单数据
	 * @param orderId 流程实例ID
	 * @param formId 表单ID
	 * @param userId 用户ID
	 * @return
	 */
	public SmartResponse<QueryFormData> getFormData(String orderId,String formId,String userId) {
		SmartResponse<QueryFormData> smartResp = new SmartResponse<QueryFormData>();
		List<TableFieldMap> tfMaps = formTableServ.tableFieldMap(formId);
		if(null != tfMaps && tfMaps.size()>0) {
			try {
				querySqlAndExe(tfMaps, orderId, userId);//处理tfMaps
				List<QueryFormData> formDatas = classifyComposite(tfMaps);
				if(CollectionUtils.isNotEmpty(formDatas)) {
					smartResp.setResult(IWebConstant.OP_SUCCESS);
					smartResp.setMsg(IWebConstant.OP_SUCCESS_MSG);
					smartResp.setDatas(formDatas);
					smartResp.setSize(formDatas.size());
					smartResp.setTotalNum(formDatas.size());
				}
			} catch(DaoException ex) {
				ex.printStackTrace();
			}
		}
		return smartResp;
	}
	
	
	/**
	 * 获取表单数据
	 * @param formDataId form数据ID
	 * @param formId 表单ID
	 * @param userId 用户ID
	 * @return
	 */
	public SmartResponse<QueryFormData> getFormDataByFormDataId(String formDataId,String formId) {
		SmartResponse<QueryFormData> smartResp = new SmartResponse<QueryFormData>();
		List<TableFieldMap> tfMaps = formTableServ.tableFieldMap(formId);
		if(null != tfMaps && tfMaps.size()>0) {
			try {
				querySqlAndExeByFormDataId(tfMaps, formDataId);//处理tfMaps
				List<QueryFormData> formDatas = classifyComposite(tfMaps);
				if(CollectionUtils.isNotEmpty(formDatas)) {
					smartResp.setResult(IWebConstant.OP_SUCCESS);
					smartResp.setMsg(IWebConstant.OP_SUCCESS_MSG);
					smartResp.setDatas(formDatas);
					smartResp.setSize(formDatas.size());
					smartResp.setTotalNum(formDatas.size());
				}
			} catch(DaoException ex) {
				ex.printStackTrace();
			}
		}
		return smartResp;
	}
	
	/**
	 * 分类组合数据
	 * @param tfMaps
	 * @return
	 */
	private List<QueryFormData> classifyComposite(List<TableFieldMap> tfMaps) {
		List<QueryFormData> formDatas = new ArrayList<QueryFormData>();
		QueryFormData formData = null;
		//表单分类
		Map<String,List<TableFieldMap>> formTypeMaps =  formTypelassify(tfMaps);
		for(String key : formTypeMaps.keySet()) {
			tfMaps = formTypeMaps.get(key);
			//针对特殊的表单类型进行处理
			if(key.startsWith(FormPluginType.Listctrl.getValue())) { 
				formData = new QueryFormData();
				formData.setName(tfMaps.get(0).getTableId());
				formData.setValueSize(1);
				List<QueryFormData> subFormDatas = new ArrayList<QueryFormData>();
				QueryFormData subFormData = null;
				for (TableFieldMap tfMap : tfMaps) {
					subFormData = new QueryFormData();
					subFormData.setName(tfMap.getTableFieldId());
					subFormData.setValueSize(1);
					if(null != tfMap.getValues() && tfMap.getValues().size()>0) {
						List<Object> objs = new ArrayList<Object>();
						for (int i = 0; i < tfMap.getValues().size(); i++) {
							if(null != tfMap.getValues().get(i)){ 
								objs.add(StringUtils.repaceSpecialChar(tfMap.getValues().get(i).toString()));
							} else {
								objs.add("");
							}
						}
						subFormData.setValue(objs);
						subFormData.setValueSize(tfMap.getValues().size());
					} else {
						subFormData.setValue(StringUtils.repaceSpecialChar(StringUtils.nullToStr(tfMap.getValue())));
					}
					subFormDatas.add(subFormData);
				}
				formData.setNameMoreValues(subFormDatas);
				formDatas.add(formData);
			} else {
				for (TableFieldMap tfMap : tfMaps) {
					 formData = new QueryFormData();
					 formData.setName(tfMap.getTableFieldId());
					 if(null != tfMap.getValues() && tfMap.getValues().size()>0) {
						 formData.setValue(tfMap.getValues());
					 } else {
						 formData.setValue(StringUtils.nullToStr(tfMap.getValue()));
					 }
					 formData.setValueSize(1);
					 formDatas.add(formData);
				}
			}
		}//for
		formData = null;
		tfMaps = null;
		return formDatas;
	}
	
	
	/**
	 * 保存表单
	 * @param datas
	 * @param formId
	 * @param userId
	 * @param formState 表单状态 <br />
	 * 1--保存(但未提交) <br />
	 * 0-- 保存（并提交）
	 * @return
	 */
	public String saveForm(Map<String,Object> datas,String formId,String userId,Integer formState) {
		String formDataId = StringUtils.createSerialNum();
		List<TableFieldMap> tfMaps = formTableServ.tableFieldMap(formId);
		if(null == tfMaps || tfMaps.size()<1) {
			return null;
		}
		Map<String,List<TableFieldMap>> tableMaps = assignmentFormData(datas, tfMaps);
		tfMaps = null;
		//拼SQL语句
		try {
			for (String key : tableMaps.keySet()) {
				insertData(key, tableMaps.get(key), userId, formState, formDataId);
			}//for
		} catch (DaoException ex) {
			ex.printStackTrace();
			formDataId = null;
		} finally {
			tableMaps = null;
		}
		return formDataId;
	}
	
	/**
	 * 更新表单数据
	 * @param datas
	 * @param formId
	 * @param formDataId
	 * @param userId
	 * @param formState 表单状态 <br />
	 * 1--保存(但未提交) <br />
	 * 0-- 保存（并提交）
	 * @return
	 */
	public boolean updateForm(Map<String,Object> datas,String formId,String formDataId,String userId,Integer formState) {
		boolean is = false;
		List<TableFieldMap> tfMaps = formTableServ.tableFieldMap(formId);
		if(null == tfMaps || tfMaps.size()<1) {
			return is;
		}
		Map<String,List<TableFieldMap>> tableMaps = assignmentFormData(datas, tfMaps);
		tfMaps = null;
		StringBuilder fieldBuild = null;
		StringBuilder sqlBuild = null;
		List<TableFieldMap> tfList = null;
		Map<String, Object> params = null;
		//拼SQL语句
		try {
			for (String key : tableMaps.keySet()) { //key为表名
				params = new HashMap<String, Object>();
				tfList = tableMaps.get(key);
				if(FormPluginType.Listctrl.getValue().equals(tfList.get(0).getPlugin())) {
					//删除以前的数据
					params.put("formDataId", formDataId);
					opDao.executeSql("delete from "+key+" where form_data_id=:formDataId", params);
					//插入新数据
					insertData(key, tfList, userId, formState, formDataId);
				} else if(this.isExistData(key,formDataId)){
					fieldBuild = new StringBuilder();
					sqlBuild = new StringBuilder();
					sqlBuild.append("update "+key+" set ");
					for (TableFieldMap tf : tfList) {
						if(null != tf.getValue()) {
							fieldBuild.append(tf.getTableFieldName()+"=:"+tf.getTableFieldName()+",");
							params.put(tf.getTableFieldName(), tf.getValue());
						}
					}
					params.put("state", formState);
					params.put("formDataId", formDataId);
					sqlBuild.append(fieldBuild.toString()+"state=:state where form_data_id=:formDataId");
					opDao.executeSql(sqlBuild.toString(), params);
				} else {
					insertData(key, tfList, userId, formState, formDataId);
				}
			}//for
			is = true;
		} catch (DaoException ex) {
			ex.printStackTrace();
			formDataId = null;
		} finally {
			fieldBuild = null;
			sqlBuild = null;
			tfList = null;
			params = null;
			tableMaps = null;
		}
		return is;
	}
	
	
	
	/**
	 * 把字段归类到表
	 * @param tfMaps
	 * @return
	 */
	private Map<String,List<TableFieldMap>> fieldClassifyToTable(List<TableFieldMap> tfMaps) {
		Map<String,List<TableFieldMap>> tableMaps = new HashMap<String, List<TableFieldMap>>();
		List<TableFieldMap> tfMapList = null;
		for (TableFieldMap tfMap : tfMaps) {
			tfMapList = tableMaps.get(tfMap.getTableName());
			if(null == tfMapList) {
				tfMapList = new ArrayList<TableFieldMap>();
				tableMaps.put(tfMap.getTableName(), tfMapList);
			}
			tfMapList.add(tfMap);
		}
		return tableMaps.size()>0?tableMaps:null;
	}
	
	/**
	 * 表单类型分类(如：text,textArea,listctrl等)
	 * @param tfMaps
	 * @return
	 */
	private Map<String,List<TableFieldMap>> formTypelassify(List<TableFieldMap> tfMaps) {
		Map<String,List<TableFieldMap>> tableMaps = new HashMap<String, List<TableFieldMap>>();
		List<TableFieldMap> tfMapList = null;
		for (TableFieldMap tfMap : tfMaps) {
			tfMapList = tableMaps.get(tfMap.getPlugin()+"_"+tfMap.getTableId());
			if(null == tfMapList) {
				tfMapList = new ArrayList<TableFieldMap>();
				tableMaps.put(tfMap.getPlugin()+"_"+tfMap.getTableId(), tfMapList);
			}
			tfMapList.add(tfMap);
		}
		return tableMaps.size()>0?tableMaps:null;
	}
	
	/**
	 * 生成查询SQL语句并执行
	 * @param tfMaps
	 * @param orderId
	 * @param userId
	 */
	private void querySqlAndExe(List<TableFieldMap> tfMaps,String orderId, String userId) throws DaoException {
		//分开表---同一表的字段放在一个List(归类字段)
		Map<String,List<TableFieldMap>> tableMaps = fieldClassifyToTable(tfMaps);
		//生成SQL语句
		List<TableFieldMap> tfMapList = null;
		Map<String, Object> param = new HashMap<String, Object>();
		for (String key : tableMaps.keySet()) {
			StringBuilder sqlBuild = new StringBuilder();
			sqlBuild.append("select ");
			tfMapList = tableMaps.get(key);
			for (TableFieldMap tfMap : tfMapList) {
				sqlBuild.append(tfMap.getTableFieldName()+",");
			}
			//去掉组合语句时多余的那个逗号","
			sqlBuild.delete(sqlBuild.length()-1, sqlBuild.length());
			param.clear();
			if(StringUtils.isEmpty(orderId)) {
				sqlBuild.append(" from "+key+" where state='1' and creator=:userId");
				param.put("userId", userId);
			} else {
				sqlBuild.append(" from "+key+" where form_data_id=(select form_data_id from t_flow_form where order_id=:orderId)");
				param.put("orderId", orderId);
			}
			this.queryAndCompositeData(sqlBuild.toString(), param, tfMapList);
		}//for
		tableMaps = null;
		tfMapList = null;
	}
	
	/**
	 * 生成查询SQL语句并执行
	 * @param tfMaps
	 * @param formDataId
	 */
	private void querySqlAndExeByFormDataId(List<TableFieldMap> tfMaps,String formDataId) throws DaoException {
		//分开表---同一表的字段放在一个List(归类字段)
		Map<String,List<TableFieldMap>> tableMaps = fieldClassifyToTable(tfMaps);
		//生成SQL语句
		List<TableFieldMap> tfMapList = null;
		Map<String, Object> param = new HashMap<String, Object>();
		for (String key : tableMaps.keySet()) {
			StringBuilder sqlBuild = new StringBuilder();
			sqlBuild.append("select ");
			tfMapList = tableMaps.get(key);
			for (TableFieldMap tfMap : tfMapList) {
				sqlBuild.append(tfMap.getTableFieldName()+",");
			}
			//去掉组合语句时多余的那个逗号","
			sqlBuild.delete(sqlBuild.length()-1, sqlBuild.length());
			param.clear();
			sqlBuild.append(" from "+key+" where form_data_id=:formDataId");
			param.put("formDataId", formDataId);
			queryAndCompositeData(sqlBuild.toString(), param, tfMapList);
		}//for
		tableMaps = null;
		tfMapList = null;
	}
	
	/**
	 * 查询并组合数据
	 * @param sql
	 * @param param
	 * @param tfMapList
	 */
	private void queryAndCompositeData(String sql, Map<String, Object> param, List<TableFieldMap> tfMapList) {
		if(StringUtils.isNotEmpty(sql)) {
			List<Object> objs = opDao.queryObjSql(sql, param);
			//对查询出来的值进行处理
			if(null != objs && objs.size()>0) {
				if(objs.size()==1) { //当查询结果只有一条时
					Object[] objArray = null;
					if(objs.get(0).getClass().isArray()) {
						objArray = (Object[])objs.get(0);
					} else {
						objArray = new Object[]{objs.get(0)};
					}
					if(null != objArray)
						for (int i = 0; i < objArray.length; i++) {
							tfMapList.get(i).setValue(objArray[i]);
						}
				} else { //当查询结果有多条时，把对应的值放到一个list里面
					for(Object obj : objs) {
						Object[] objArray = null;
						if(objs.get(0).getClass().isArray()) {
							objArray = (Object[])obj;
						} else {
							objArray = new Object[]{obj};
						}
						if(null != objArray)
							for (int i = 0; i < objArray.length; i++) {
								tfMapList.get(i).getValues().add(objArray[i]);
							}
					}
				}
			}//if
		}
	}
	
	
	/**
	 * 提交表单时，把表单数据赋值到tfMaps对象里面 <br />
	 * 修改时间：2016年08月27日；<br />
	 * 修改内容：支持一个字段多个情况，多值之间用英文逗号分隔
	 * @param datas
	 * @param tfMaps
	 * @return 返回结果为表名对应字段；如：key为表名,value为一个List表示该表中字段信息
	 */
	private Map<String,List<TableFieldMap>> assignmentFormData(Map<String,Object> datas,List<TableFieldMap> tfMaps) {
		Map<String,List<TableFieldMap>> tableMaps = fieldClassifyToTable(tfMaps);
		if(null != tableMaps && tableMaps.size()>0) {
			List<TableFieldMap> tfList = null;
			for(String key : tableMaps.keySet()) {
				tfList = tableMaps.get(key); 
				for (TableFieldMap tf : tfList) {
					if(datas.containsKey(tf.getTableFieldId())) {
						Object value = datas.get(tf.getTableFieldId());
						if(FormPluginType.Listctrl.getValue().equals(tf.getPlugin())) {
							 if(value.getClass().isArray()) {
								 tf.setValues(Arrays.asList((Object[])value));
							 } else {
								 tf.setValue(value);
							 }
						} else if(value.getClass().isArray()) {
							Object[] values = (Object[])value;
							//修改时间：2016年12月07日；同一个表单有相同字段时；只取第一个值，避免出现重复的值
							if(FormPluginType.Text.getValue().equals(tf.getPlugin())) {
								tf.setValue(StringUtils.handNull(values[0]));
							} else {
								tf.setValue(ArrayUtils.arrayToString(values, IWebConstant.MULTI_VALUE_SPLIT));
							}
						} else {
							tf.setValue(value);
						}
						/*
						 * 修改时间：2016年08月27日
						 * if(value.getClass().isArray()) {
							tf.setValues(Arrays.asList((Object[]) value));
						} else if(FORM_TYPE_LISTCTRL.equals(tf.getPlugin())) {
							tf.setValues(Arrays.asList(value));
						} else {
							tf.setValue(value);
						}*/
					}
				}
			}
		}
		return tableMaps;
	}
	
	/**
	 * 插入数据
	 * @param tableName
	 * @param tfList
	 * @param userId
	 * @param formState 表单状态 <br />
	 * 1--保存(但未提交) <br />
	 * 0-- 保存（并提交）
	 * @param formDataId
	 * @return
	 * @throws DaoException
	 */
	private boolean insertData(String tableName,List<TableFieldMap> tfList,
			String userId,Integer formState,String formDataId) throws DaoException {
		boolean is = true;
		StringBuilder fieldBuild = new StringBuilder();
		StringBuilder valueBuild = new StringBuilder();
		StringBuilder sqlBuild = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String,List<Object>> valueMaps = new HashMap<String, List<Object>>();
		sqlBuild.append("insert into "+tableName);
		int valueSize = 0;
		for (TableFieldMap tf : tfList) {
			if(null != tf.getValue() || (null != tf.getValues() && tf.getValues().size()>0)) {
				fieldBuild.append(tf.getTableFieldName()+",");
				valueBuild.append(":"+tf.getTableFieldName()+",");
			}
			//有多个值时(分多条数据插入)
			if(null != tf.getValues() && tf.getValues().size()>0) {
				valueMaps.put(tf.getTableFieldName(), tf.getValues());
				valueSize = tf.getValues().size();
			} else if(null != tf.getValue()) {
				valueMaps.put(tf.getTableFieldName(),Arrays.asList(new Object[]{tf.getValue()}));
				valueSize = 1;
			}
		}
		sqlBuild.append("(id,form_data_id,"+fieldBuild.toString()+"state,creator,create_time) ");
		sqlBuild.append("values(:id,:formDataId,"+valueBuild.toString()+" :state,:creator,:createTime)");
		for (int i = 0; i < valueSize; i++) {
			params.put("id", StringUtils.createSerialNum());
			params.put("formDataId", formDataId);
			params.put("state", formState);
			params.put("createTime", DateUtil.dateToStr(new Date(), null));
			params.put("creator", userId);
			for (String fieldNameKey : valueMaps.keySet()) {
				params.put(fieldNameKey, valueMaps.get(fieldNameKey).get(i));
			}
			is = is && opDao.executeSql(sqlBuild.toString(), params)>0?true:false;
			params.clear();
		}
		return is;
	}
	
	
	/**
	 * 判断formDataId对应的数据是否已经存在<br />
	 * 判断有没有与formDataId对应的数据（如果存在返回true；否则返回false）
	 * @param tableName
	 * @param formDataId
	 * @return 如果存在返回:true；否则返回:false
	 */
	private boolean isExistData(String tableName,String formDataId) {
		boolean is = false;
		if(StringUtils.isNotEmpty(formDataId) && StringUtils.isNotEmpty(tableName)) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("formDataId", formDataId);
			String sql = "select count(id) from "+tableName+" where form_data_id=:formDataId";
			if(opDao.exeCountSql(sql, param)>0) {
				is = true;
			}
		} 
		return is;
	}
}
