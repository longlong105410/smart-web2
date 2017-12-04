<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<jsp:include page="../../base/include/common-header.jsp" />
	<!-- 封装 bootstrap 弹出对话框 -->  
	<link href="${pageContext.request.contextPath}/css/bootstrap-dialog.css" rel="stylesheet" />
	<script src="${pageContext.request.contextPath}/js/bootstrap-dialog.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap-dialog-util.js" type="text/javascript"></script>

	<!-- 日期插件  -->
    <link href="${pageContext.request.contextPath}/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet"/>
	<script src="${pageContext.request.contextPath}/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js" type="text/javascript" ></script>
	<script src="${pageContext.request.contextPath}/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8" type="text/javascript" ></script>

	<!-- zTree插件 -->
	<link href="${pageContext.request.contextPath}/plugins/zTree/css/zTreeStyle.css" type="text/css" rel="stylesheet"  />
	<script src="${pageContext.request.contextPath}/plugins/zTree/js/jquery.ztree.core-3.5.min.js" type="text/javascript" ></script>
	<script src="${pageContext.request.contextPath}/plugins/zTree/js/jquery.ztree.excheck-3.5.min.js" type="text/javascript" ></script>
	<script src="${pageContext.request.contextPath}/plugins/zTree/js/jquery.ztree.exedit-3.5.min.js" type="text/javascript" ></script>
	<script src="${pageContext.request.contextPath}/plugins/zTree/js/jquery.ztree.exhide-3.5.min.js" type="text/javascript" ></script>
	
	<!-- 上传文件插件 --> 
	<link href="${pageContext.request.contextPath}/plugins/jqueryFileUpload/css/jquery.fileupload.css" rel="stylesheet"/>
	<link href="${pageContext.request.contextPath}/plugins/jqueryFileUpload/css/jquery.fileupload-ui.css" rel="stylesheet"/>
	<script src="${pageContext.request.contextPath}/plugins/jqueryFileUpload/js/vendor/jquery.ui.widget.js"></script>
	<script src="${pageContext.request.contextPath}/plugins/jqueryFileUpload/js/jquery.iframe-transport.js" type="text/javascript" ></script>
	<script src="${pageContext.request.contextPath}/plugins/jqueryFileUpload/js/jquery.fileupload.js" type="text/javascript" ></script>
	
	<!-- 打印 -->
	<link href="${pageContext.request.contextPath}/plugins/printArea/css/jquery.printarea.css" rel="stylesheet"/>
	<script src="${pageContext.request.contextPath}/plugins/printArea/js/jquery.printarea.js" type="text/javascript" ></script>
	<link href="${pageContext.request.contextPath}/css/print.css" rel="stylesheet" />
	
	<!--ueditor编辑器  -->
	<script type="text/javascript" charset="utf-8" src="${ctx}/plugins/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctx}/plugins/ueditor/ueditor.all.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctx}/plugins/ueditor/lang/zh-cn/zh-cn.js"></script>
	
	<!-- 自定义样式 -->
	<link href="${pageContext.request.contextPath}/css/ztree-rewrite.css" type="text/css" rel="stylesheet"  />
	<link href="${pageContext.request.contextPath}/css/bootstrap-extend.css" rel="stylesheet" type="text/css" />
	<link href="${pageContext.request.contextPath}/css/bootstrap-rewrite.css" rel="stylesheet" type="text/css" />
	<link href="${pageContext.request.contextPath}/css/jquery-ui-rewrite.css" rel="stylesheet" type="text/css" />
	<link href="${pageContext.request.contextPath}/css/common.css" rel="stylesheet" type="text/css" />
	<link href="${pageContext.request.contextPath}/css/cnoj-ui.css" rel="stylesheet" type="text/css" />
	<link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet" />
	<link href="${pageContext.request.contextPath}/css/layout.css" rel="stylesheet" />
	<!-- 与流程相关的样式 -->
	<link href="${ctx}/plugins/form/css/form.css" rel="stylesheet" />
	<link href="${ctx}/plugins/flow/css/flow.css" rel="stylesheet" />
	
	<!-- 自定义js -->
	<script src="${pageContext.request.contextPath}/js/check-card-no.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/js/check-form.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/js/ztree-util.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/js/input-select.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/js/auto-complete.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/js/table-async-tree.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/js/jquery-fileupload-util.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/js/cnoj.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/js/cnoj.event.listener.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/js/adjust-ie-height.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/js/jquery.autotextarea.js" type="text/javascript" charset="UTF-8"></script>
	
	<!--[if lt IE 9]>
    <script type="text/javascript" src="${pageContext.request.contextPath}/plugins/bootstrap/js/html5shiv.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/plugins/bootstrap/js/respond.min.js"></script>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/plugins/jquery-ui-bootstrap/css/custom-theme/jquery.ui.1.10.0.ie.css"/>
    <![endif]-->
    
    <!--[if (gte IE 8)&(lt IE 10)]>
	<script type="text/javascript" src="${pageContext.request.contextPath}/plugins/jqueryFileUpload/js/cors/jquery.xdr-transport.js"></script>
	<![endif]-->
	
	<!--[if IE 7]>
       <link rel="stylesheet" href="${pageContext.request.contextPath}/plugins/jquery-ui-bootstrap/assets/css/font-awesome-ie7.min.css">
    <![endif]-->
    
    <!--[if lte IE 6]>
	  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/plugins/bootstrap/css/bootstrap-ie6.css">
	  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/plugins/bootstrap/css/ie.css">
      <script type="text/javascript" src="${pageContext.request.contextPath}/plugins/bootstrap/js/bootstrap-ie.js"></script>
     <![endif]-->
</head>
  <body>
<div class="wrap-content">
    <input type="hidden" id="refresh-url" value="${refreshUrl }" />
    <div class="modal fade bootstrap-dialog type-primary size-normal" id="myModal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
			  <div class="modal-header">
			     <div class="bootstrap-dialog-header">
			         <div class="bootstrap-dialog-title" id="dialog-title">处理属性</div>
			         <div class="bootstrap-dialog-close-button" data-dismiss="modal" aria-hidden="true" style="display: block;"></div>
			      </div>
			   </div>
			   <div class="modal-body">
			        <c:if test="${outputClassify.normalLines != null && fn:length(outputClassify.normalLines)>0}">
			             <form class="form-horizontal task-submit-form" id="node-decision-next-prop">
				            <div class="modal-row hide" data-row-title="选择下一环节" data-is-concurrent="${taskModel.isConcurrent}" 
				               data-is-show="${taskModel.isConcurrent=='1'?'0':(fn:length(outputClassify.normalLines)>1?'1':'0')}" 
				               data-size='${fn:length(outputClassify.normalLines)}' id="select-next-line-row">
						       <div class="row">
						            <div class="col-sm-2 p-t-6 text-right p-r-0"><label>下一环节</label></div>
								    <div class="col-sm-10">
								      <c:forEach var="nameVal" items="${outputClassify.normalLines}">
								          <label class="${taskModel.isConcurrent=='1'?'checkbox-inline':'radio-inline'}">
								          <input type="${taskModel.isConcurrent=='1'?'checkbox':'radio'}" id="next-line-${nameVal.value }" data-is-check="${nameVal.other }" value="${nameVal.value }" name="nextLineName"  /> &nbsp;${nameVal.name }</label>
								      </c:forEach>
								    </div>
							    </div>
						    </div>
				            <div class="modal-row hide" data-is-show="0" data-select-style="radio" 
				                 id="select-next-assigner-row">
				            	<div class="row">
						           	<div class="col-sm-2 p-t-6 text-right p-r-0"><label>选择处理人</label></div>
									<div class="col-sm-10">
										<div id="select-next-assigner">
											<!--  <div class="m-t-8 upload-loading">正在加载处理人...</div>-->
											<div id="select-assigner-content"> 
											</div>
										</div>
									</div>
								</div><!-- end row -->
					        </div>
				            <c:if test="${taskModel.isSug == '1' }">
							    <div class="modal-row is-suggest">
							        <div class="row">
							           <div class="col-sm-2 p-t-6 text-right p-r-0"><label>处理意见</label></div>
								       <div class="col-sm-10">
								          <textarea class="form-control require handle-suggest" data-label-name="处理意见" name="handleSuggest" rows="5"></textarea>
								       </div>
							        </div>
							    </div>
						    </c:if>
				        </form>
			        </c:if>
			        <c:if test="${outputClassify.backLines != null && fn:length(outputClassify.backLines)>0}">
						<form class="form-horizontal task-submit-form" id="node-decision-back-prop">
							<div class="modal-row hide" data-row-title="选择驳回环节" data-is-show="${(null != outputClassify.backLines && fn:length(outputClassify.backLines)>1)?'1':'0'}" data-size='${fn:length(outputClassify.backLines)}' id="select-back-line-row">
							       <div class="row">
							            <div class="col-sm-2 p-t-6 text-right p-r-0"><label>驳回环节</label></div>
									    <div class="col-sm-10">
									      <c:forEach var="nameVal" items="${outputClassify.backLines}">
									          <label class="radio-inline"><input type="radio" id="next-line-${nameVal.value }" value="${nameVal.value }" name="nextLineName"  /> &nbsp;${nameVal.name }</label>
									      </c:forEach>
									    </div>
								    </div>
							</div>
							<c:if test="${taskModel.isSug == '1' }">
							    <div class="modal-row is-suggest">
							        <div class="row">
							           <div class="col-sm-2 p-t-6 text-right p-r-0"><label>处理意见</label></div>
								       <div class="col-sm-10">
								          <textarea class="form-control require handle-suggest" data-label-name="处理意见" name="handleSuggest" rows="5"></textarea>
								       </div>
							        </div>
							    </div>
						    </c:if>
						</form>
					</c:if>
			   </div>
			   <div class="modal-footer p-t-5 p-b-5 p-r-5">
			     <button type="button" class="btn btn-primary" id="dialog-ok">
			     <i class="glyphicon glyphicon-ok-sign"></i> 确定</button>
		       </div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal-dialog -->
	</div><!-- /.modal -->

   <div class="flow-process">
       <form id="flow-process-form">
           <input type="hidden" id="process-id" name="processId" value="${taskInfo.processId }" />
           <input type="hidden" id="process-name" name="processName" value="${taskInfo.processName }" />
           <input type="hidden" id="form-id" name="formId" value="${smartResp.data.id}" />
           <input type="hidden" id="form-data-id" name="formDataId" value="${formDataId}" />
           <input type="hidden" id="task-id" name="taskId" value="${taskId }" />
           <input type="hidden" id="task-key" name="taskKey" value="${taskKey }" />
           <input type="hidden" id="order-id" name="orderId" value="${orderId }" />
       </form>
       <div class="container-fluid flow-process-header p-r-0 p-l-0">
           <div class="flow-process-btn">
                 <div class="left process-task-info">
	                    <div class="left p-r-10 task-info-col">
	                       <label>当前环节：</label>
	                       <span id="current-node-name">${taskModel.displayName }</span>
	                    </div>
			            <div class="left p-l-10 p-r-10 task-info-col">
				            <label>标题：</label>
				            <span id="ins-title">${title }</span>
				        </div>
				        <div class="left p-l-10">
			               <label>创建人：</label>
			               <span id="create-user">${creator }</span>
			            </div>
                 </div>
			     <div class="navbar-nar-right">
			        <c:if test="${taskModel.isPrint == '1' }">
			         <button class="btn btn-info btn-sm cnoj-print" data-target="#process-handle-form" id="print-form"><i class="glyphicon glyphicon-print"></i> 打印</button>
			        </c:if>
			        <button class="btn btn-info btn-sm cnoj-open-blank" id="view-pic" data-open-target="parent" data-title="查看流程图" data-uri="process/view?processId=${taskInfo.processId }&orderId=${orderId }"><i class="glyphicon glyphicon-picture"></i> 查看流程图 </button>
			         
			         <c:if test="${firstNode != 1 }">
			           <button type="button" id="save-form" class="btn btn-success btn-sm" data-uri="process/saveForm.json"><i class="glyphicon glyphicon-ok-sign"></i> 保存表单</button> 
			        </c:if>
			         <!-- <button type="button" id="undo-form" class="btn btn-info btn-sm"><i class="glyphicon glyphicon-remove-sign"></i> 撤消</button>  -->
			         <c:if test="${outputClassify.backLines != null }"> 
			         	<c:choose>
			         		<c:when test="${empty backName }">
			         			<button type="button" id="back-process" class="btn btn-info btn-sm" data-uri="process/submitTask.json"><i class="glyphicon glyphicon-repeat"></i> 驳回 </button>
			         		</c:when>
			         		<c:otherwise>
			         			<button type="button" id="back-process" class="btn btn-info btn-sm" data-uri="process/submitTask.json"><i class="glyphicon glyphicon-repeat"></i> ${backName} </button>
			         		</c:otherwise>
			         	</c:choose>
			            
			         </c:if>
			         <button type="button" id="submit-process" class="btn btn-primary btn-sm" data-uri="process/submitTask.json"><i class="glyphicon glyphicon-ok-sign"></i> 提交 </button>
			    </div>
           </div>
       </div>
   </div>
   <div class="flow-form-contents p-t-3">
       <div class="panel-tabs-wrap">
			<div class="panel-heading p-0">
				<div class="panel-tabs-tab">
					<ul class="nav nav-tabs ui-state-default" role="tablist">
						<li class="active"><a href="#process-form-tab" role="presentation" data-toggle="tab"> 表单信息</a></li>
						<c:if test="${isAtt==1 }">
						   <li><a href="#process-att-tab" id="process-att-tab-a" role="presentation" data-toggle="tab"> 已上传附件 <span class="badge">1</span></a></li>
						</c:if>
						<li><a href="#process-record-tab" role="presentation" data-toggle="tab"> 流转记录</a></li>
					</ul>
				</div>
			</div>
			<div class="panel-body p-0">
				<div id="flow-form-panel-contents" class="tab-content panel-tab-content bg-color-white cnoj-auto-limit-height">
					<div role="tabpanel" class="tab-pane active" id="process-form-tab">
						<div class="form-prop">
					       <form id="process-handle-form" data-relate-arg-form="#flow-process-form" method="post" enctype="multipart/form-data">
					           ${smartResp.data.parseHtml}
					       </form>
                           <iframe class="hidden" id="handle-form-iframe" name="handle-form-iframe" frameborder=0 width=0 height=0></iframe>
					   </div>
					</div>
					<c:if test="${isAtt==1 }">
						<div role="tabpanel" class="tab-pane" id="process-att-tab">
							<div class="cnoj-load-url" data-uri="process/attachment/list?processId=${taskInfo.processId}&orderId=${orderId}&taskId=${taskId}&taskKey=${taskKey}&formId=${smartResp.data.id }&isUploadBtn=${taskModel.taskAttachment}" ></div>
						</div>
					</c:if>
					<div role="tabpanel" class="tab-pane" id="process-record-tab">
						<div class="cnoj-load-url" data-uri="process/processHandleInfo?orderId=${orderId }" ></div>
					</div>
				</div>
			</div>
	  </div><!-- panel-tabs-wrap -->
   </div>
</div>
<script src="${ctx}/plugins/form/js/form.prop.listener.js" type="text/javascript"></script>
<script src="${ctx}/plugins/flow/js/flow.form.js" type="text/javascript"></script>
<script src="${ctx}/js/flow.form.js" type="text/javascript"></script>
<script type="text/javascript">
	utils.isIframe = true;
    $("#process-handle-form").flowForm({
    	formFieldNames:'${taskModel.formPropIds}',
    	username:'${userInfo.fullName}',
    	deptName: '${userInfo.deptName}',
    	formData:'${output}',
    	callback: function(){
    	    formRequireListener();
    		printListener();
    		inputPluginEvent();
    		hrefListener();
    		limitHeightListener();
    	    loadUrlListener();
    	}
    });
    $(".flow-process").resize(function(){
 	   setTimeout(function() {
 		  limitHeightListener();
 	   }, 200);
    });
</script>
</body>
</html>