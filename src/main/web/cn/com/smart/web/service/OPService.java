package cn.com.smart.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.smart.bean.SmartResponse;
import cn.com.smart.constant.IConstant;
import cn.com.smart.exception.DaoException;
import cn.com.smart.exception.ServiceException;
import cn.com.smart.filter.bean.FilterParam;
import cn.com.smart.helper.ObjectHelper;
import cn.com.smart.helper.ObjectTreeHelper;
import cn.com.smart.res.SQLResUtil;
import cn.com.smart.service.impl.BaseServiceImpl;
import cn.com.smart.web.bean.AutoComplete;
import cn.com.smart.web.helper.PageHelper;
import cn.com.smart.web.plugins.ZTreeData;
import cn.com.smart.web.plugins.service.ZTreeService;

import com.mixsmart.utils.CollectionUtils;
import com.mixsmart.utils.StringUtils;

/**
 * 
 * @author lmq
 *
 */
@Service("opServ")
public class OPService extends BaseServiceImpl implements IOPService, IConstant {
	
	@Autowired
	private ObjectTreeHelper treeHelper;
	@Autowired
	private ZTreeService zTreeServ;
	
	/**
	 * 查询数据
	 * @param resId
	 * @return
	 * @throws ServiceException
	 */
	public SmartResponse<Object> getDatas(String resId) {
		SmartResponse<Object> smartResp = new SmartResponse<Object>();
		try {
			if(StringUtils.isNotEmpty(resId)) {
				List<Object> objs = getOPDao().queryDatas(resId);
				if(null != objs && objs.size()>0) {
					objs = ObjectHelper.handleObjDate(objs);
					smartResp.setResult(OP_SUCCESS);
					smartResp.setMsg(OP_SUCCESS_MSG);
					smartResp.setDatas(objs);
					smartResp.setSize(objs.size());
					smartResp.setTotalNum(objs.size());
				} else {
					smartResp.setResult(OP_NOT_DATA_SUCCESS);
					smartResp.setMsg(OP_NOT_DATA_SUCCESS_MSG);
				}
				objs = null;
			}
		} catch (DaoException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smartResp;
	}
	
	/**
	 * 查询数据
	 * @param resId
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public SmartResponse<Object> getDatas(String resId,Map<String,Object> params) {
		SmartResponse<Object> smartResp = new SmartResponse<Object>();
		try {
			if(StringUtils.isNotEmpty(resId)) {
				List<Object> objs = getOPDao().queryDatas(resId, params);
				if(null != objs && objs.size()>0) {
					objs = ObjectHelper.handleObjDate(objs);
					smartResp.setResult(OP_SUCCESS);
					smartResp.setMsg(OP_SUCCESS_MSG);
					smartResp.setDatas(objs);
					smartResp.setSize(objs.size());
					smartResp.setTotalNum(objs.size());
				} else {
					smartResp.setResult(OP_NOT_DATA_SUCCESS);
					smartResp.setMsg(OP_NOT_DATA_SUCCESS_MSG);
				}
				objs = null;
			}
		} catch (DaoException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smartResp;
	}
	
	/**
	 * 查询数据
	 * @param resId
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public SmartResponse<Object> getDatas(String resId,FilterParam params) {
		SmartResponse<Object> smartResp = new SmartResponse<Object>();
		try {
			if(StringUtils.isNotEmpty(resId)) {
				List<Object> objs = getOPDao().queryDatas(resId, params);
				if(null != objs && objs.size()>0) {
					objs = ObjectHelper.handleObjDate(objs);
					smartResp.setResult(OP_SUCCESS);
					smartResp.setMsg(OP_SUCCESS_MSG);
					smartResp.setDatas(objs);
					smartResp.setSize(objs.size());
					smartResp.setTotalNum(objs.size());
				} else {
					smartResp.setResult(OP_NOT_DATA_SUCCESS);
					smartResp.setMsg(OP_NOT_DATA_SUCCESS_MSG);
				}
				objs = null;
			}
		} catch (DaoException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smartResp;
	}
	
	/**
	 * 查询数据(分页)
	 * @param resId
	 * @param start
	 * @param rows
	 * @return
	 * @throws ServiceException
	 */
	public SmartResponse<Object> getDatas(String resId,int start,int rows) {
		SmartResponse<Object> smartResp = new SmartResponse<Object>();
		smartResp.setResult(OP_NOT_DATA_SUCCESS);
		smartResp.setMsg(OP_NOT_DATA_SUCCESS_MSG);
		try {
			if(StringUtils.isNotEmpty(resId)) {
				long totalNum = getOPDao().count(resId);
				if(start<=totalNum) {
					List<Object> objs = getOPDao().queryDatas(resId,start,rows);
					if(null != objs && objs.size()>0) {
						objs = ObjectHelper.handleObjDate(objs);
						smartResp.setResult(OP_SUCCESS);
						smartResp.setMsg(OP_SUCCESS_MSG);
						smartResp.setDatas(objs);
						smartResp.setPerPageSize(rows);
						smartResp.setTotalNum(totalNum);
						smartResp.setTotalPage(PageHelper.getTotalPage(totalNum, rows));
						smartResp.setSize(objs.size());
					} 
				   objs = null;
			  }
			}
		} catch (DaoException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smartResp;
	}
	
	/**
	 * 查询数据(分页)
	 * @param resId
	 * @param params
	 * @param start
	 * @param rows
	 * @return
	 * @throws ServiceException
	 */
	public SmartResponse<Object> getDatas(String resId,Map<String,Object> params,int start,int rows) {
		SmartResponse<Object> smartResp = new SmartResponse<Object>();
		smartResp.setResult(OP_NOT_DATA_SUCCESS);
		smartResp.setMsg(OP_NOT_DATA_SUCCESS_MSG);
		try {
			if(StringUtils.isNotEmpty(resId)) {
				long totalNum = getOPDao().count(resId, params);
				if(start<=totalNum) {
					List<Object> objs = getOPDao().queryDatas(resId, params,start,rows);
					if(null != objs && objs.size()>0) {
						objs = ObjectHelper.handleObjDate(objs);
						smartResp.setResult(OP_SUCCESS);
						smartResp.setMsg(OP_SUCCESS_MSG);
						smartResp.setDatas(objs);
						smartResp.setPerPageSize(rows);
						smartResp.setTotalNum(totalNum);
						smartResp.setTotalPage(PageHelper.getTotalPage(totalNum, rows));
						smartResp.setSize(objs.size());
					}
					objs = null;
				}
			}
		} catch (DaoException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smartResp;
	}
	
	
	/**
	 * 查询数据(分页)
	 * @param resId
	 * @param params
	 * @param start
	 * @param rows
	 * @return
	 * @throws ServiceException
	 */
	public SmartResponse<Object> getDatas(String resId,FilterParam params,int start,int rows) {
		SmartResponse<Object> smartResp = new SmartResponse<Object>();
		smartResp.setResult(OP_NOT_DATA_SUCCESS);
		smartResp.setMsg(OP_NOT_DATA_SUCCESS_MSG);
		try {
			if(StringUtils.isNotEmpty(resId)) {
				long totalNum = getOPDao().count(resId, params);
				if(start<=totalNum) {
					List<Object> objs = getOPDao().queryDatas(resId, params,start,rows);
					if(null != objs && objs.size()>0) {
						objs = ObjectHelper.handleObjDate(objs);
						smartResp.setResult(OP_SUCCESS);
						smartResp.setMsg(OP_SUCCESS_MSG);
						smartResp.setDatas(objs);
						smartResp.setPerPageSize(rows);
						smartResp.setTotalNum(totalNum);
						smartResp.setTotalPage(PageHelper.getTotalPage(totalNum, rows));
						smartResp.setSize(objs.size());
					}
					objs = null;
				}
			}
		} catch (DaoException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smartResp;
	}
	
	/**
	 * 查询数据(分页)
	 * @param resId
	 * @param params
	 * @param filterParam
	 * @param start
	 * @param rows
	 * @return
	 * @throws ServiceException
	 */
	public SmartResponse<Object> getDatas(String resId,Map<String,Object> params,FilterParam filterParam,int start,int rows) {
		SmartResponse<Object> smartResp = new SmartResponse<Object>();
		smartResp.setResult(OP_NOT_DATA_SUCCESS);
		smartResp.setMsg(OP_NOT_DATA_SUCCESS_MSG);
		try {
			if(StringUtils.isNotEmpty(resId)) {
				long totalNum = getOPDao().count(resId, params,filterParam);
				if(start<=totalNum) {
					List<Object> objs = getOPDao().queryDatas(resId, params,filterParam,start,rows);
					if(null != objs && objs.size()>0) {
						objs = ObjectHelper.handleObjDate(objs);
						smartResp.setResult(OP_SUCCESS);
						smartResp.setMsg(OP_SUCCESS_MSG);
						smartResp.setDatas(objs);
						smartResp.setPerPageSize(rows);
						smartResp.setTotalNum(totalNum);
						smartResp.setTotalPage(PageHelper.getTotalPage(totalNum, rows));
						smartResp.setSize(objs.size());
					}
					objs = null;
				}
			}
		} catch (DaoException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smartResp;
	}
	
	
	/**
	 * 查询数据，返回树形数据结构
	 * @param resId 资源ID
	 * @param params 参数
	 * @return 返回SmartResponse对象；如果getResult()为“1”表示获取数据成功；否则失败；
	 * 如果成功；通过调用getDatas()获取数据列表
	 * @throws ServiceException
	 */
	public SmartResponse<Object> getTreeDatas(String resId,Map<String,Object> params) {
		SmartResponse<Object> smartResp = new SmartResponse<Object>();
		try {
			if(StringUtils.isNotEmpty(resId)) {
				List<Object> objs = getOPDao().queryDatas(resId, params);
				if(null != objs && objs.size()>0) {
					try {
						objs = treeHelper.outPutTree(objs);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(null != objs && objs.size()>0) {
						smartResp.setResult(OP_SUCCESS);
						smartResp.setMsg(OP_SUCCESS_MSG);
						smartResp.setDatas(objs);
						smartResp.setTotalNum(objs.size());
					}
				}
				objs = null;
			}
		} catch (DaoException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smartResp;
	}
	
	/**
	 * 查询数据,返回树形数据结构
	 * @param resId 资源ID
	 * @return 返回SmartResponse对象；如果getResult()为“1”表示获取数据成功；否则失败；
	 * 如果成功；通过调用getDatas()获取数据列表
	 * @throws ServiceException
	 */
	public SmartResponse<Object> getTreeDatas(String resId) {
		SmartResponse<Object> smartResp = new SmartResponse<Object>();
		smartResp.setResult(OP_NOT_DATA_SUCCESS);
		smartResp.setMsg(OP_NOT_DATA_SUCCESS_MSG);
		try {
			if(StringUtils.isNotEmpty(resId)) {
				List<Object> objs = getOPDao().queryDatas(resId);
				if(null != objs && objs.size()>0) {
					try {
						objs = treeHelper.outPutTree(objs);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(null != objs && objs.size()>0) {
						smartResp.setResult(OP_SUCCESS);
						smartResp.setMsg(OP_SUCCESS_MSG);
						smartResp.setDatas(objs);
						smartResp.setTotalNum(objs.size());
					}
				}
				objs = null;
			}
		} catch (DaoException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smartResp;
	}
	
	
	/**
	 * 查询数据，返回ZTree树形数据结构
	 * @param resId 资源ID
	 * @param params 参数
	 * @return 返回SmartResponse对象；如果getResult()为“1”表示获取数据成功；否则失败；
	 * 如果成功；通过调用getDatas()获取数据列表
	 * @throws ServiceException
	 */
	public SmartResponse<ZTreeData> getZTreeDatas(String resId,Map<String,Object> params) {
		SmartResponse<ZTreeData> smartResp = new SmartResponse<ZTreeData>();
		List<ZTreeData> lists = null;
		try {
			if(StringUtils.isNotEmpty(resId)) {
				Object async = params.get("isAsync");
				params.remove("isAsync");
				Boolean isAsync = false;
				if(null != async) {
					try {
						isAsync = Boolean.parseBoolean(async.toString());
					} catch (Exception e) {
						isAsync = false;
					}
					String key = "id";
					Object obj = params.get(key);
					if(null == obj) {
						key = "parentId";
						obj = params.get(key);
					}
					if(null != obj && obj.getClass().isArray()) {
						Object[] objs = (Object[])obj;
						params.put(key, objs[objs.length-1]);
					}
				}
				
				List<Object> objs = getOPDao().queryDatas(resId, params);
				if(null != objs && objs.size()>0) {
					try {
						objs = treeHelper.outPutTree(objs);
						if(null != objs && objs.size()>0) {
							lists = zTreeServ.convert(objs,isAsync);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						objs = null;
					}
					if(null != lists && lists.size()>0) {
						smartResp.setResult(OP_SUCCESS);
						smartResp.setMsg(OP_SUCCESS_MSG);
						smartResp.setDatas(lists);
						smartResp.setTotalNum(lists.size());
					}
					lists = null;
				}
				objs = null;
			}
		} catch (DaoException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smartResp;
	}
	
	/**
	 * 查询数据,返回ZTree树形数据结构
	 * @param resId 资源ID
	 * @return 返回SmartResponse对象；如果getResult()为“1”表示获取数据成功；否则失败；
	 * 如果成功；通过调用getDatas()获取数据列表
	 * @throws ServiceException
	 */
	public SmartResponse<ZTreeData> getZTreeDatas(String resId) {
		SmartResponse<ZTreeData> smartResp = new SmartResponse<ZTreeData>();
		List<ZTreeData> lists = null;
		try {
			if(StringUtils.isNotEmpty(resId)) {
				List<Object> objs = getOPDao().queryDatas(resId);
				if(null != objs && objs.size()>0) {
					try {
						objs = treeHelper.outPutTree(objs);
						if(null != objs && objs.size()>0) {
							lists = zTreeServ.convert(objs,false);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						objs = null;
					}
					if(null != lists && lists.size()>0) {
						smartResp.setResult(OP_SUCCESS);
						smartResp.setMsg(OP_SUCCESS_MSG);
						smartResp.setDatas(lists);
						smartResp.setTotalNum(lists.size());
					}
					lists = null;
				}
				objs = null;
			}
		} catch (DaoException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smartResp;
	}
	
	
	/**
	 * 自动完成
	 * @param resId 资源ID
	 * @param params 参数
	 * @return 返回SmartResponse对象；如果getResult()为“1”表示获取数据成功；否则失败；
	 * 如果成功；通过调用getDatas()获取数据列表
	 * @throws ServiceException
	 */
	public SmartResponse<AutoComplete> getAutoCompleteDatas(String resId,Map<String,Object> params) {
		SmartResponse<AutoComplete> smartResp = new SmartResponse<AutoComplete>();
		try {
			if(StringUtils.isNotEmpty(resId)) {
				List<Object> objs = getOPDao().queryDatas(resId, params,0,10);
				if(null != objs && objs.size()>0) {
					List<AutoComplete> autoCompleteList = new ArrayList<AutoComplete>();
					AutoComplete autoComplete = null;
					for (Object obj : objs) {
						Object[] objArray = (Object[]) obj;
						autoComplete = new AutoComplete();
						if(objArray.length>=3) {
							autoComplete.setId(StringUtils.handNull(objArray[0]));
							autoComplete.setValue(StringUtils.handNull(objArray[1]));
							autoComplete.setLabel(StringUtils.handNull(objArray[2]));
						} else if(objArray.length==2) {
							autoComplete.setId(StringUtils.handNull(objArray[0]));
							autoComplete.setValue(StringUtils.handNull(objArray[1]));
							autoComplete.setLabel(StringUtils.handNull(objArray[1]));
						} else if(objArray.length==1) {
							autoComplete.setId(StringUtils.handNull(objArray[0]));
							autoComplete.setValue(StringUtils.handNull(objArray[0]));
							autoComplete.setLabel(StringUtils.handNull(objArray[0]));
						} 
						if(objArray.length>3) {
							List<Object> otherValues = new ArrayList<Object>();
							for (int i = 3; i < objArray.length; i++) {
								otherValues.add(objArray[i]);
							}
							autoComplete.setOtherValue(otherValues);
						}
						autoCompleteList.add(autoComplete);
					}
					if(autoCompleteList.size()>0) {
						smartResp.setResult(OP_SUCCESS);
						smartResp.setMsg(OP_SUCCESS_MSG);
						smartResp.setDatas(autoCompleteList);
						smartResp.setSize(autoCompleteList.size());
						smartResp.setTotalNum(autoCompleteList.size());
					}
					autoCompleteList = null;
					autoComplete = null;
				}
				objs = null;
			}
		} catch (DaoException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smartResp;
	}
	
	
	/**
	 * 执行update,delete等的sql
	 * @param resId 资源ID
	 * @param params 参数
	 * @return 返回SmartResponse对象；如果getResult()为“1”表示执行成功；否则失败；
	 * @throws ServiceException
	 */
	public SmartResponse<String> execute(String resId,Map<String,Object> params) {
		SmartResponse<String> smartResp = new SmartResponse<String>();
		try {
			if(StringUtils.isNotEmpty(resId)) {
				//判断处理是否有逗号分割的多条数据组合
				for (String key : params.keySet()) {
					Object objValue = params.get(key);
					if(null != objValue && objValue.getClass().isArray()) {
						String value = StringUtils.handNull(objValue);
						if(StringUtils.isNotEmpty(value) && value.indexOf(",")>-1) {
							String[] values = value.split(",");
							params.put(key, values);
						}
					}
				}
				if(getOPDao().execute(resId, params)) {
					smartResp.setResult(OP_SUCCESS);
					smartResp.setMsg(OP_SUCCESS_MSG);
				}
			}
		} catch (DaoException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smartResp;
	}

	/**
	 * 根据资源ID及提供的参数，获取指定 <code>clazz</code> 类列表的数据
	 * @param resId 资源ID
	 * @param params 参数
	 * @param clazz 需要转换类型的类
	 * @return 返回SmartResponse对象；如果getResult()为“1”表示获取数据成功；否则失败；
	 * 如果成功；通过调用getDatas()获取数据列表
	 */
	public <E> SmartResponse<E> getDatas(String resId, Map<String, Object> params, Class<?> clazz) {
		SmartResponse<E> smartRes = new SmartResponse<E>();
		List<E> lists = getOPDao().queryDatas(resId, params, clazz);
		if(CollectionUtils.isNotEmpty(lists)) {
			smartRes.setResult(OP_SUCCESS);
			smartRes.setDatas(lists);
			smartRes.setMsg(OP_SUCCESS_MSG);
			smartRes.setSize(lists.size());
			smartRes.setTotalNum(lists.size());
		}
		return smartRes;
	}
	
	@Override
	public SmartResponse<Long> count(String resId, Map<String, Object> params) {
		SmartResponse<Long> smartResp = new SmartResponse<Long>();
		if(StringUtils.isEmpty(resId)) {
			return smartResp;
		}
		String sql = SQLResUtil.getOpSqlMap().getSQL(resId);
		if(StringUtils.isNotEmpty(sql)) {
			long num = getOPDao().exeCountSql(sql, params);
			smartResp.setResult(OP_SUCCESS);
			smartResp.setData(num);
			smartResp.setMsg(OP_SUCCESS_MSG);
		}
		return smartResp;
	}
}
