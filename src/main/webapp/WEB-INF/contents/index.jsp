<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="base/include/menu.jsp" /> 
<div class="content" id="main-content">  
	 <div id="main-tab" class="easyui-tabs" data-options="tools:'#tab-tools'" style="width:100%;">
	     <div id="index-tab" title="首页" href="index/welcome" data-options="iconCls:'glyphicon glyphicon-home'"></div>
	 </div>
</div>
<jsp:include page="base/include/footer.jsp" />