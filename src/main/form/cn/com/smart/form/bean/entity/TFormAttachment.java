package cn.com.smart.form.bean.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.com.smart.bean.BaseBeanImpl;

/**
 * 表单附件实体
 * @author lmq  2017年8月14日
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name="t_form_attachment")
public class TFormAttachment extends BaseBeanImpl {
    
    /**
     * 
     */
    private static final long serialVersionUID = -173765903250814569L;

    private String id;
    
    private String attachmentId;
    
    private String formId;
    
    private String formDataId;

    @Id
    @Column(name="id", length=50)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name="attachment_id", length=50, nullable=false)
    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    @Column(name="form_id", length=50, nullable=false)
    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    @Column(name="form_data_id", length=50, nullable=false)
    public String getFormDataId() {
        return formDataId;
    }

    public void setFormDataId(String formDataId) {
        this.formDataId = formDataId;
    }
    
}
