package cn.com.smart.form.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mixsmart.utils.LoggerUtils;
import com.mixsmart.utils.StringUtils;

import cn.com.smart.dao.impl.BaseDaoImpl;
import cn.com.smart.exception.DaoException;
import cn.com.smart.form.bean.entity.TFormAttachment;
import cn.com.smart.web.dao.impl.AttachmentDao;

@Repository
public class FormAttachmentDao extends BaseDaoImpl<TFormAttachment> {

    @Autowired
    private AttachmentDao attDao;

    @Override
    public boolean delete(Serializable id) throws DaoException {
        boolean is = false;
        if (null == id || StringUtils.isEmpty(id.toString())) {
            return is;
        }
        String[] ids = id.toString().split(",");
        List<TFormAttachment> lists = find(ids);
        if (null != lists && lists.size() > 0) {
            if (delete(lists)) {
                LoggerUtils.info(log, "表单附件信息删除[成功]");
                StringBuilder idBuilder = new StringBuilder();
                for (TFormAttachment formAtt : lists) {
                    idBuilder.append(formAtt.getAttachmentId() + ",");
                }
                idBuilder = idBuilder.delete(idBuilder.length()-1, idBuilder.length());
                // 删除附件
                if (attDao.delete(idBuilder.toString())) {
                    is = true;
                    LoggerUtils.info(log, "表单附件删除[成功]");
                } else {
                    LoggerUtils.error(log, "表单附件删除[失败]");
                }
            } else {
                LoggerUtils.error(log, "表单附件信息删除[失败]");
            }
        }
        return is;
    }

}
