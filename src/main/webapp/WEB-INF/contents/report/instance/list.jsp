<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cnoj" uri="/cnoj-tags" %>
<div class="wrap-content">
	<div class="panel panel-default no-border">
	   <c:if test="${not empty searchFields }">
		   <div class="panel-search">
		       <form class="form-inline cnoj-entry-submit" id="report-search-form" method="post" role="form" action="${uri }">
	              <c:forEach var="searchField" items="${searchFields }">
	                 <div class="form-group p-r-10">
		                 <label>${searchField.title }：</label>
		                 <input type="text" class="form-control input-form-control" name="${searchField.searchName }" placeholder="请输入${searchField.title }" />
	                 </div>
	              </c:forEach>
	              <div class="form-group p-l-10">
					  <span class="btn btn-primary btn-sm cnoj-search-submit">
							<i class="glyphicon glyphicon-search"></i>
							<span>搜索</span>
						  </span>
					  </div>
	           </form>
	          </div>
          </c:if>
		<!-- table -->
	    <cnoj:table smartResp="${smartResp }" headers="${headerTitles }" currentUri="${currentUri }" 
	     isCheckbox="${reportProp.isCheckbox }" isId="${reportProp.isHasId }" isIdShow="${reportProp.isShowId }" 
	     isRowSelected="1" alinks="${alinks }" refreshBtn="${refreshBtn }" isOriginalTable="${isOriginalTable }"
	     page="${pageParam }" customBtns="${customBtns }"
	    />
	</div>
</div>
