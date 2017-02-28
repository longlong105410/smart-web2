package cn.com.smart.web.plugins;

import java.util.ArrayList;
import java.util.List;

import cn.com.smart.bean.TreeProp;
import cn.com.smart.utils.StringUtil;

/**
 * ztree插件助手
 * @author lmq
 *
 * @param <T>
 */
public class ZTreeHelper <T extends ZTreeData>{

	private Object treeProps;
	private Class<T> clazz;
	
	public ZTreeHelper(Class<T> clazz,Object treeProps) {
		this.treeProps = treeProps;
		this.clazz = clazz;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> convert(String checkFlag) {
		List<T> lists = null;
		if(null != treeProps && treeProps instanceof ArrayList<?>) {
			List<Object> objs = (ArrayList<Object>)treeProps;
			treeProps = null;
			lists = new ArrayList<T>();
			Object objTmp = objs.get(0);
			if(objTmp instanceof TreeProp) {
				for (Object obj : objs) {
					T t = null;
					TreeProp tree = (TreeProp)obj;
					try {
						t = this.clazz.newInstance();
						t.setId(tree.getId());
						t.setpId(tree.getParentId());
						t.setName(tree.getName());
						t.setFlag(tree.getFlag());
						t.setCheckFlag(checkFlag);
						t.setIsParent(!isLeaf(objs,tree.getId(),false));
						lists.add(t);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				for (Object obj : objs) {
					Object[] objTmpArray = (Object[]) obj;
					T t = null;
					if(objTmpArray.length>3) {
						try {
							t = this.clazz.newInstance();
							t.setId(StringUtil.handNull(objTmpArray[3]));
							t.setpId(StringUtil.handNull(objTmpArray[4]));
							t.setName(StringUtil.handNull(objTmpArray[5]));
							t.setIsParent(!isLeaf(objs, StringUtil.handNull(objTmpArray[3]),true));
							t.setCheckFlag(checkFlag);
							lists.add(t);
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			objTmp = null;
			objs = null;
		}
		return lists;
	}
	
	
	/**
	 * 判断是否为叶子节点
	 * @param trees
	 * @param id
	 * @param isObjArray
	 * @return
	 */
	public boolean isLeaf(List<Object> trees,String id,boolean isObjArray) {
		boolean is = true;
		if(!StringUtil.isEmpty(id) && trees != null && trees.size()>0 ) {
			if(isObjArray) {
				for (Object objTmp : trees) {
					Object[] objTmpArray = (Object[]) objTmp;
					if(StringUtil.handNull(objTmpArray[4]).equals(id)) {
						is = false;
						break;
					}
				}
			} else {
				for (Object obj : trees) {
					TreeProp treeProp = (TreeProp) obj;
					if(treeProp.getParentId().equals(id)) {
						is = false;
						break;
					}
				}
			}
		}
		return is;
	}
}
