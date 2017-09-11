<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div class="wrap-content">
    <div class="report-designer p-5">
        <div class="panel panel-default">
            <div class="panel-heading">报表设置</div>
            <div class="panel-body">
		        <form method="post" id="report-designer-form">
		          <table class="table table-condensed">
		              <tbody>
		                  <tr>
		                      <th style="width: 100px;">名称</th>
		                      <td>
		                          <input type="text" name="name" class="form-control" id="report-name" placeholder="请输入报表名称" value="${objBean.name }" />
		                      </td>
		                      <th style="width: 100px;">类型</th>
                              <td>
                                  <select id="report-type" class="form-control cnoj-select select-form-control" name="type" data-uri="dict/item/REPORT_TYPE.json" data-default-value="${objBean.type }" >
                                  </select>
                              </td>
                              <th style="width: 100px;">是否支持导出</th>
                              <td>
                                  <select class="form-control cnoj-select select-form-control" name="properties.isImport" data-uri="dict/item/YES_OR_NO.json" data-default-value="${objBean.isImport }" >
                                  </select>
                              </td>
		                  </tr>
		                  <tr>
                              <th>是否有ID</th>
                              <td>
                                  <select class="form-control cnoj-select select-form-control" name="properties.isHasId" data-uri="dict/item/YES_OR_NO.json" data-default-value="${objBean.properties.isHasId }" >
                                  </select>
                              </td>
                              <th>是否显示ID</th>
                              <td>
                                  <select id="report-type" class="form-control cnoj-select select-form-control" name="properties.isShowId" data-uri="dict/item/YES_OR_NO.json" data-default-value="${objBean.properties.isShowId }" >
                                  </select>
                              </td>
                              <th></th>
                              <td></td>
                          </tr>
		              </tbody>
		           </table>
		        </form>
	        </div>
	    </div>
	    <div class="panel panel-default m-t-20">
            <div class="panel-heading">自定义SQL语句</div>
            <div class="panel-body">
                <textarea class="form-control" style="width: 99%;" rows="5"></textarea>
            </div>
        </div>
            
        <div class="panel panel-default m-t-20">
            <div class="panel-heading">报表字段设置</div>
                <form>
				    <table class="table table-bordered table-condensed">
					    <thead>
					        <tr class="text-center">
					            <th style="width: 50px;">序号</th>
					            <th style="width: 200px;">表名</th>
					            <th style="width: 200px;">字段名</th>
					            <th>显示名称</th>
					        </tr>
					    </thead>
					    <tbody>
					        <tr>
					            <td class="seq-num">1 </td>
					            <td>
					                <select class="cnoj-select form-control input-sm" name="">
					                </select>
					            </td>
					            <td>
					                <select class="cnoj-select form-control input-sm" name="">
	                                </select>
					            </td>
					            <td>
					               <input type="text" id="file-name" class="form-control input-sm require" data-label-name="字段名称" name="fields" placeholder="请输入显示名称" />
					            </td>
					        </tr>
					    </tbody>
				 </table>
			 </form>
	    </div>
    </div>
</div>