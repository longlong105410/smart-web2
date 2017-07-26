<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<link href="${ctx}/plugins/form/css/form.css" rel="stylesheet" />
<div class="wrap-content">
   <div class="form-header">
       <form id="create-form-param">
           <input type="hidden" id="form-data-id" name="formDataId" value="${formDataId}" />
       </form>
       <div class="form-header-btn">
			<div class="navbar-nar-right">
			    <button type="button" class="btn btn-primary btn-sm" data-uri="form/save.json"><i class="fa fa-floppy-o" aria-hidden="true"></i> 保存 </button>
			</div>
        </div>
   </div>
   <div class="form-contents p-t-3">
       <div class="panel-tabs-wrap">
			<div class="panel-heading p-0">
				<div class="panel-tabs-tab">
					<ul class="nav nav-tabs ui-state-default" role="tablist">
						<li class="active"><a href="#form-content-tab" role="presentation" data-toggle="tab"> 表单信息</a></li>
						<c:if test="${isAtt==1 }">
						   <li><a href="#form-att-tab" id="form-att-tab-a" role="presentation" data-toggle="tab"> 附件 <span class="badge">1</span></a></li>
						</c:if>
					</ul>
				</div>
			</div>
			<div class="panel-body p-0">
				<div id="form-panel-contents" class="tab-content panel-tab-content bg-color-white cnoj-auto-limit-height">
					<div role="tabpanel" class="tab-pane active" id="form-content-tab">
						<div class="form-prop">
					       <form id="create-form" method="post" data-relate-arg-form="#create-form-param" enctype="multipart/form-data">
					           ${smartResp.data.parseHtml}
					       </form>
                           <iframe class="hidden" id="handle-form-iframe" name="handle-form-iframe" frameborder=0 width=0 height=0></iframe>
					   </div>
					</div>
					<c:if test="${isAtt==1 }">
						<div role="tabpanel" class="tab-pane" id="form-att-tab">
							<div class="cnoj-load-url" data-uri="" ></div>
						</div>
					</c:if>
				</div>
			</div>
	  </div><!-- panel-tabs-wrap -->
   </div>
</div>
<script type="text/javascript">
    if(typeof(UE) == 'undefined') {
        var $wrap = $("#create-form-param").parent();
        $wrap.append('<script type="text/javascript" charset="utf-8" src="${ctx}/plugins/ueditor/ueditor.config.js"><\/script>');
        $wrap.append('<script type="text/javascript" charset="utf-8" src="${ctx}/plugins/ueditor/ueditor.all.js"><\/script>');
        $wrap.append('<script type="text/javascript" charset="utf-8" src="${ctx}/plugins/ueditor/lang/zh-cn/zh-cn.js"><\/script>');
    }
</script>
<script src="${ctx}/plugins/form/js/form.prop.listener.js" type="text/javascript"></script>
<script src="${ctx}/plugins/flow/js/flow.form.js" type="text/javascript"></script>
<script src="${ctx}/js/flow.form.js" type="text/javascript"></script>
<script type="text/javascript">
    /*$("#process-handle-form").flowForm({
    	formFieldNames:'${taskModel.formPropIds}',
    	username:'${userInfo.fullName}',
    	deptName: '${userInfo.deptName}',
    	formData:'${output}'
    });*/
</script>