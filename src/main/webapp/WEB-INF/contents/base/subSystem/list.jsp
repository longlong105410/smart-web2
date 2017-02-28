<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="cnoj" uri="/cnoj-tags" %>
<div class="wrap-content">
	<cnoj:table smartResp="${smartResp }" headers="名称,地址,图标,状态,序号,创建时间" 
	   headerWidths="25%,30%,10%,10%,10%,15%" 
		  isCheckbox="1" isRowSelected="1" currentUri="${currentUri }" 
		  addBtn="${addBtn }" editBtn="${editBtn }" delBtn="${delBtn }" refreshBtn="${refreshBtn }" 
		  page="${pageParam }" />
</div>