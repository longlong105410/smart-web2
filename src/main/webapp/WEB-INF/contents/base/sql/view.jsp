<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="wrap-content">
    <div class="panel panel-default">
		   <table class="table table-bordered">
		       <tbody>
		          <tr>
		              <th style="width: 100px;">名称</th>
		              <td>${objBean.resName }</td>
		              <th style="width: 100px;">是否过滤</th>
                      <td>${objBean.isFilter ? '是' : '否' }</td>
		           </tr>
		           <tr>
                       <th style="width: 100px;">简单描述</th>
                       <td colspan="3">${objBean.descr}</td>
                   </tr>
                   <tr>
                       <th style="width: 100px;">SQL语句</th>
                       <td colspan="3">${objBean.sql} </td>
                   </tr>
                   <tr>
                       <th style="width: 100px;">创建人</th>
                       <td>${objBean.userId } </td>
                       <th style="width: 100px;">创建时间</th>
                       <td>${objBean.createTime } </td>
                    </tr>
                    <tr>
                        <th style="width: 120px;">最近一次修改人</th>
                        <td>${objBean.lastUserId } </td>
                        <th style="width: 130px;">最近一次修改时间</th>
                        <td>${objBean.lastModifyTime } </td>
                     </tr>
		        </tbody>
		    </table>
    </div>
</div>