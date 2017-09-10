package cn.com.smart.report.bean.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import cn.com.smart.bean.BaseBean;
import cn.com.smart.bean.DateBean;

/**
 * 报表实体类
 * @author lmq  2017年9月10日
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name="t_report")
public class TReport implements BaseBean, DateBean {
    
    /**
     * 
     */
    private static final long serialVersionUID = -4538820744371430653L;

    private String id;
    
    private String name;
    
    /**
     * 报表类型
     */
    private String type;
    
    /**
     * 是否支持导出功能
     */
    private Integer isImport;
    
    private String creator;
    
    private Date createTime;

    @Id
    @Column(name="id", length=50)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name="name", length=255, nullable=false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name="type", length=127, nullable = false)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name="is_import")
    public Integer getIsImport() {
        return isImport;
    }

    public void setIsImport(Integer isImport) {
        this.isImport = isImport;
    }

    @Column(name="creator")
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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
        return "R";
    }
}
