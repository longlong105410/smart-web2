<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/plugins/flow/js/flow.simple.tree.js"></script>
<div class="wrap-content">
    <div class="panel panel-default my-hastodo">
      <div class="panel-search" data-height="37">
           <form class="form-inline search-param" id="search-form-order" method="post" role="form" action="process/hasTodo" target="${target }">
               <div class="form-group p-r-10">
				  <label for="search-input01">项目名称：</label>
				  <input type="text" class="form-control input-form-sm-control" style="width: 400px" id="search-input01" name="title" placeholder="请输入项目名称" value="${queryFilter.title }"/>
			  </div>
			  <div class="form-group">
				<span class="btn btn-primary btn-sm cnoj-search-submit">
					<i class="glyphicon glyphicon-search"></i>
					<span>搜索</span>
				</span>
			 </div>
           </form>
        </div>
      <div class="cnoj-auto-limit-height" data-subtract-height="110">
	    <table class="table table-bordered table-condensed font-size-12">
	        <thead>
	        <tr class="ui-state-default">
	           <th>标题</th>
	           <th class="text-center" style="width: 150px;">办理环节</th>
	           <th class="text-center" style="width: 130px;">起草人</th>
	           <th class="text-center" style="width: 130px;">所在部门</th>
	           <th class="text-center" style="width: 130px;">到达时间</th>
	           <th class="text-center" style="width: 130px;">完成时间</th>
	           <th class="text-center" style="width: 80px;">流程图</th>
	        </tr>
	       </thead>
	       <tbody>
	       <c:choose>
		        <c:when test="${smartResp.result != '1'}">
		             <tr>
		                <td colspan="7" class="text-center">没有已办</td>
		             </tr>
		        </c:when>
		        <c:otherwise>
		           <c:forEach var="todoClassify" items="${smartResp.datas }">
		               <tr class="expand-data tr-parent-tree tr-tree" id="${todoClassify.id}"><td colspan="7" class="tr-parent-tree-td"><span class='ui-icon ui-icon-triangle-1-s left'></span> ${todoClassify.name }</td></tr>
		               <c:forEach var="workItem" items="${todoClassify.datas}">
		               <tr class="tr-tree tr-sub-tree ${todoClassify.id}">
		                  <td class="tr-sub-tree-td">
		                  <a href="#" class="cnoj-open-blank"  data-title="【${workItem.orderTitle}】处理信息" data-uri="process/viewForm?processId=${workItem.processId }&orderId=${workItem.orderId }&isAtt=1">
		                  ${workItem.orderTitle}
		                  </a>
		                  </td>
		                  <td class="text-center">${workItem.taskName }</td>
		                  <td class="text-center">${workItem.fullName }</td>
		                  <td class="text-center">${workItem.orgName }</td>
		                  <td class="text-center" title="${workItem.taskCreateTime }">${fn:substring(workItem.taskCreateTime,0,10)}</td>
		                  <td class="text-center" title="${workItem.taskEndTime }">${fn:substring(workItem.taskEndTime,0,10)}</td>
		                  <td class="text-center">
		                    <button type="button" class="btn btn-info btn-xs cnoj-open-blank" data-title="查看流程图" data-uri="process/view?processId=${workItem.processId }&orderId=${workItem.orderId}"><i class="glyphicon glyphicon-picture"></i> 查看</button>
		                  </td>
		               </tr>
		               </c:forEach>
		           </c:forEach>
		           </c:otherwise>
	        </c:choose>
	       </tbody>
	     </table>
	  </div>
	</div>
	<div class="panel-footer ui-state-default panel-footer-page" data-height="34">
		     <c:if test="${smartResp.totalPage>1 }">
		     <div class="btn-page">
		         <div class="page">
		              <ul class="pagination pagination-sm">
		                <li class="${pagedata.page==1?'disabled':'' }">
		                  <c:choose>
		                     <c:when test="${pagedata.page==1}">
		                        <a href="javascript:void(0)" class="pre-page">&laquo;</a>
		                     </c:when>
		                     <c:otherwise>
		                        <a href="#" data-uri="${uri}${(fn:indexOf(uri,'?')>0?'&':'?')}page=${pagedata.page-1}" class="cnoj-change-page pre-page" data-target="${target }">&laquo;</a>
		                     </c:otherwise>
		                  </c:choose> 
		                </li>
		                <c:forEach var="pageNum" items="${pageNums }">
		                    <li class="${pagedata.page==pageNum?'active':'' }"><a class="cnoj-change-page" href="#" data-uri="${uri}${(fn:indexOf(uri,'?')>0?'&':'?')}page=${pageNum}" data-target="${target }">${pageNum}</a></li>
		                </c:forEach>
					    
					    <li class="${pagedata.page>=smartResp.totalPage?'disabled':'' }">
					    <c:choose>
		                  <c:when test="${pagedata.page>=smartResp.totalPage}">
		                     <a href="javascript:void(0)" class="next-page">&raquo;</a>
		                  </c:when>
		                  <c:otherwise>
		                     <a href="#" data-uri="${uri}${(fn:indexOf(uri,'?')>0?'&':'?')}page=${pagedata.page+1}" class="cnoj-change-page pre-page" data-target="${target }">&raquo;</a>
		                  </c:otherwise>
		                 </c:choose>
		                 <li>&nbsp;到<input class="form-control input-sm goto-page-input" name="page" value="" />页
		                 <button data-uri="${uri}${(fn:indexOf(uri,'?')>0?'&':'?')}page=" class="btn btn-default btn-xs cnoj-goto-page" data-target="${target}">确定</button></li>
		              </ul>
		         </div>
		     </div>
		     </c:if>
		     <div class="page-info"><span>${smartResp.totalPage>0?pagedata.page:'0'} - ${smartResp.totalPage}</span><span>&nbsp;&nbsp; 共${smartResp.totalNum}条(每页显示${smartResp.perPageSize}条)</span></div>
		</div>
</div>
<script type="text/javascript">
   setTimeout("loadJs()", 200);
   function loadJs() {
	   simpleTreeListener(".my-hastodo ");
   }
</script>