<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div class="wrap-content">
    <div class="report-designer p-5">
        <div class="panel panel-default">
            <div class="panel-heading">报表设置</div>
            <div class="panel-body">
		        <form method="post" id="report-designer-form">
		          <table class="table table-condensed table-bordered table-sm">
		              <tbody>
		                  <tr>
		                      <th style="width: 80px;">名称</th>
		                      <td>
		                          <input type="text" name="name" class="form-control require" id="report-name" placeholder="请输入报表名称" value="${objBean.name }" />
		                      </td>
		                      <th style="width: 80px;">类型</th>
                              <td>
                                  <select id="report-type" class="form-control cnoj-select select-form-control" name="type" data-uri="dict/item/REPORT_TYPE.json" data-default-value="${objBean.type }" >
                                  </select>
                              </td>
                              <th style="width: 100px;">支持导出</th>
                              <td>
                                  <select class="form-control cnoj-select select-form-control" name="properties.isImport" data-uri="dict/item/YES_OR_NO.json" data-default-value="${objBean.isImport==null?'0':objBean.isImport }" >
                                  </select>
                              </td>
                              <th style="width: 70px;">固定标题</th>
                              <td>
                                  <select class="form-control cnoj-select select-form-control" name="properties.isFixedHeader" data-uri="dict/item/YES_OR_NO.json" data-default-value="${objBean.isFixedHeader==null?'1':objBean.isFixedHeader }" >
                                  </select>
                              </td>
		                  </tr>
		                  <tr>
                              <th>是否有ID</th>
                              <td>
                                  <select class="form-control cnoj-select select-form-control" name="properties.isHasId" data-uri="dict/item/YES_OR_NO.json" data-default-value="${objBean.properties.isHasId==null?'1':objBean.properties.isHasId }" >
                                  </select>
                              </td>
                              <th>是否显示ID</th>
                              <td>
                                  <select id="report-type" class="form-control cnoj-select select-form-control" name="properties.isShowId" data-uri="dict/item/YES_OR_NO.json" data-default-value="${objBean.properties.isShowId==null?'0':objBean.properties.isShowId }" >
                                  </select>
                              </td>
                              <th>是否有复选框</th>
                              <td>
                                  <select class="form-control cnoj-select select-form-control" name="properties.isCheckbox" data-uri="dict/item/YES_OR_NO.json" data-default-value="${objBean.isCheckbox==null?'0':objBean.isImport }" >
                                  </select>
                              </td>
                              <th></th>
                              <td></td>
                          </tr>
                          <tr class="bg-color-pd">
                            <td colspan="8"><div class="p-t-5 p-b-3 color-pd text-bold">自定义SQL语句</div></td>
                          </tr>
                          <tr>
                            <td colspan="8">
                                <textarea class="form-control require" style="width: 99%;" rows="5" placeholder="请输入自定义SQL语句"></textarea>
                            </td>
                          </tr>
                          <tr class="bg-color-pd">
		                    <td colspan="8">
		                       <div class="col-sm-6 p-t-5 p-b-3 p-l-0 p-r-0 color-pd text-bold">字段设置</div>
		                       <div class="col-sm-6 p-t-5 p-b-3 p-r-5 text-right"><button type="button" class="setting-field btn btn-primary btn-xs"><i class="glyphicon glyphicon-plus-sign"></i> 添加</button></div>
		                    </td>
		                  </tr>
		                  <tr>
		                     <td colspan="8" class="seamless-embed-table">
		                        <table class="table table-bordered table-condensed table-sm">
		                           <thead>
		                              <tr class="ui-state-default" style="border: none;">
		                                 <th style="width: 40px;">序号</th>
		                                 <th style="width: 120px;">标题</th>
		                                 <th style="width: 100px;">宽度</th>
		                                 <th style="width: 150px;">超链接</th>
		                                 <th style="width: 120px;">打开方式</th>
		                                 <th style="width: 120px;">链接参数</th>
		                                 <th style="width: 120px;">链接参数值</th>
		                                 <th style="width: 120px;">搜索变量名</th>
		                                 <th>自定义单元格</th>
		                              </tr>
		                           </thead>
		                       </table>
		                       <div class="table-wrap-limit create-table-field">
		                           <table class="table table-condensed table-bordered table-sm">
		                               <tbody>
		                                  <tr>
		                                      <td style="width: 40px;">
		                                          <label>1</label>
		                                      </td>
		                                      <td style="width: 120px;">
                                                  <input class="form-control" placeholder="请填写标题" type="text"  />
                                              </td>
                                              <td style="width: 100px;">
                                                  <input class="form-control" type="text"  />
                                              </td>
                                              <td style="width: 150px;">
                                                  <input class="form-control" type="text"  />
                                              </td>
                                              <td style="width: 120px;">
                                                  <select class="form-control">
                                                  </select>
                                              </td>
                                              <td style="width: 120px;">
                                                  <input class="form-control" title="多个参数用英文逗号分隔，如果没有请为空" placeholder="多个参数用英文逗号分隔，如果没有请为空" type="text"  />
                                              </td>
                                              <td style="width: 120px;">
                                                  <input class="form-control" title="多个参数引用下标用英文逗号分隔，如果没有请为空" placeholder="多个参数引用下标用英文逗号分隔，如果没有请为空" type="text"  />
                                              </td>
                                              <td style="width: 120px;">
                                                  <input class="form-control" title="填写搜索变量，如果该标题不是搜索项，请为空" placeholder="填写搜索变量，如果该标题不是搜索项，请为空" type="text"  />
                                              </td>
                                              <td>
                                                  <input class="form-control" type="text" title="自定义实现类，需要实现ICustomCellCallback接口" placeholder="自定义实现类，需要实现ICustomCellCallback接口" />
                                              </td>
		                                  </tr>
		                               </tbody>
		                            </table>
		                        </div>
		                     </td>
		                  </tr>
		              </tbody>
		           </table>
		        </form>
	        </div>
	    </div>
    </div>
</div>