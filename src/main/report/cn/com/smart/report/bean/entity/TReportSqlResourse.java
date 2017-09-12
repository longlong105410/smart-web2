package cn.com.smart.report.bean.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.mixsmart.enums.YesNoType;

import cn.com.smart.bean.BaseBeanImpl;

/**
 * 报表SQL资源实体类
 * @author lmq  2017年9月10日
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name="t_report_sql_resource")
public class TReportSqlResourse extends BaseBeanImpl {

    /**
     * 
     */
    private static final long serialVersionUID = -8450228591546535944L;

    private String id;
    
    private String name;
    
    private String sql;
    
    /**
     * 是否过滤
     * 1 -- 是；
     * 0 -- 否
     */
    private Integer isFilter = YesNoType.YES.getIndex();
    
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

    @Column(name="name", length=127, nullable=false, unique=true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name="sql_", length=1024, nullable=false)
    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Column(name="is_filter")
    public Integer getIsFilter() {
        return isFilter;
    }

    public void setIsFilter(Integer isFilter) {
        this.isFilter = isFilter;
    }

    @Column(name="creator", length=50, nullable=false)
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_time", updatable = false)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}