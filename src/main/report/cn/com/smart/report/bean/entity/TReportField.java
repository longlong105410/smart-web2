package cn.com.smart.report.bean.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.mixsmart.enums.YesNoType;

import cn.com.smart.bean.BaseBean;
import cn.com.smart.bean.DateBean;

/**
 * 报表字段实体类
 * @author lmq  2017年9月10日
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name="t_report_field")
public class TReportField implements BaseBean, DateBean {
    
    /**
     * 
     */
    private static final long serialVersionUID = 4789667416867643754L;

    private String id;
    
    private String reportId;
    
    private String fieldName;
    
    private String fieldText;
    
    /**
     * 是否为ID字段
     * 1 -- 是;
     * 0 -- 否
     */
    private Integer isIdField = YesNoType.NO.getIndex();
    
    /**
     * 如果是ID字段，是否显示ID字段
     * 1 -- 是;
     * 0 -- 否
     */
    private Integer isShowId = YesNoType.NO.getIndex();
    
    private Date createTime;

    @Id
    @Column(name="id", length=50)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name="report_id", length=50, nullable=false)
    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    @Column(name="field_name", length=127)
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Column(name="field_text")
    public String getFieldText() {
        return fieldText;
    }

    public void setFieldText(String fieldText) {
        this.fieldText = fieldText;
    }

    @Column(name="is_id_field")
    public Integer getIsIdField() {
        return isIdField;
    }

    public void setIsIdField(Integer isIdField) {
        this.isIdField = isIdField;
    }

    @Column(name="is_show_id")
    public Integer getIsShowId() {
        return isShowId;
    }

    public void setIsShowId(Integer isShowId) {
        this.isShowId = isShowId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_time", updatable=false)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    @Transient
    public String getPrefix() {
        return "RF";
    }

    
}
