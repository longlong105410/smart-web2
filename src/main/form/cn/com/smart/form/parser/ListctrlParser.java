package cn.com.smart.form.parser;

import java.util.Arrays;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.mixsmart.enums.YesNoType;
import com.mixsmart.utils.StringUtils;

/**
 * 解析控件列表
 * @author lmq
 * @version 1.0 
 * @since 1.0
 * 2015年7月4日
 */
@Component
public class ListctrlParser implements IFormParser {

	
	@Override
	public String getPlugin() {
		return "listctrl";
	}

	@Override
	public String parse(Map<String, Object> dataMap) {
		if(null == dataMap || dataMap.size()<1) {
			return null;
		}
		
		String title = StringUtils.handNull(dataMap.get("orgtitle"));
		String colType = StringUtils.handNull(dataMap.get("orgcoltype"));
		/*String unit = StringUtil.handNull(dataMap.get("orgunit"));
		String sum = StringUtil.handNull(dataMap.get("orgsum"));*/
		String pluginType = StringUtils.handNull(dataMap.get("plugintype"));
		String pluginUri = StringUtils.handNull(dataMap.get("pluginuri"));
		String fieldRequire = StringUtils.handNull(dataMap.get("fieldrequire"));
		
		String colValue = StringUtils.handNull(dataMap.get("orgcolvalue"));
		String fieldName = StringUtils.handNull(dataMap.get("bind_table_field"));
		String name = StringUtils.handNull(dataMap.get("bind_table"));
		String tableWidth = StringUtils.handNull(dataMap.get("tablewidth"));
		if(StringUtils.isEmpty(tableWidth)) {
			tableWidth = "100%";
		}
		
		String[] titles = title.split("`");
		String[] colTypes = colType.split("`");
		//String[] units = unit.split("`");
		//String[] sums = sum.split("`");
		String[] pluginTypes = pluginType.split("`");
		String[] pluginUris = pluginUri.split("`");
		String[] colValues = colValue.split("`");
		String[] fieldNames = fieldName.split("`");
		String[] fieldRequires = fieldRequire.split("`");
		
		if(pluginTypes.length<titles.length) {
			String[] tmps = pluginTypes;
			pluginTypes = Arrays.copyOf(tmps,titles.length);
			for (int i = tmps.length; i < titles.length; i++) {
				pluginTypes[i] = "";
			}
	    }
		if(pluginUris.length<titles.length) {
			String[] tmps = pluginUris;
			pluginUris = Arrays.copyOf(tmps,titles.length);
			for (int i = tmps.length; i < titles.length; i++) {
				pluginUris[i] = "";
			}
	    }
		if(colValues.length<titles.length) {
			String[] tmps = colValues;
			colValues = Arrays.copyOf(tmps,titles.length);
			for (int i = tmps.length; i < titles.length; i++) {
				colValues[i] = "";
			}
		}
		
		StringBuilder strBuild = new StringBuilder();
		
		strBuild.append("<script type=\"text/javascript\">\r\n var addRows=1;\r\n function tbAddRow(dname) {addRows++;\r\n");
		strBuild.append("   var sTbid = dname+\"_table\";\r\n");
		strBuild.append("  var $addTr = $(\"#\"+sTbid+\" .template\") \r\n ");
		strBuild.append("   //连同事件一起复制   \r\n ");
		strBuild.append("   .clone();\r\n");  
		strBuild.append("   //去除模板标记 \r\n   "); 
		strBuild.append("   $addTr.removeClass(\"template\");$addTr.attr(\"id\",\"row\"+addRows);\r\n ");
		strBuild.append("   //修改内部元素 \r\n ");
		strBuild.append("   $addTr.find(\".delrow\").removeClass(\"hide\");\r\n $addTr.find(\"input[type=hidden]\").remove();");
		strBuild.append("   $addTr.find(\"input,textarea\").each(function(){\r\n");
		strBuild.append(" var id = $(this).attr(\"id\");id = id.replace('row-','row'+addRows+'-');$(this).attr(\"id\",id);$(this).val('');\r\n");
		strBuild.append(" $(this).removeClass('cnoj-auto-complete-relate-listener cnoj-input-tree-listener cnoj-input-select-relate-listener cnoj-input-org-tree-listener");
		strBuild.append(" cnoj-auto-complete-listener cnoj-input-select-listener cnoj-datetime-listener cnoj-date-listener cnoj-time-listener');");
		strBuild.append(" $(this).parent().find('.glyphicon-calendar').remove();");
		strBuild.append(" \r\n});\r\n");
        strBuild.append("   //插入表格  \r\n  ");
        strBuild.append("   $addTr.appendTo($(\"#\"+sTbid));inputPluginEvent();if(typeof(formAddRow) !== 'undefined' && !utils.isEmpty(formAddRow) && typeof(formAddRow)==='function'){formAddRow(addRows);}}\r\n ");
        
        strBuild.append("//统计\r\n ");
        strBuild.append("function sum_total(dname,e) {\r\n ");
		strBuild.append(" var tsum = 0; \r\n ");
		strBuild.append(" $(\'input[name=\"\'+dname+\'\"]\').each(function(){\r\n");
		strBuild.append("          var t = parseFloat($(this).val()); \r\n");  
		strBuild.append("          if(!t) t=0;\r\n"); 
		strBuild.append("          if(t) tsum +=t;\r\n");
		strBuild.append("          $(this).val(t);\r\n");
		strBuild.append("      }); \r\n");
        strBuild.append("  $(\'input[name=\"\'+dname+\'[total]\"]\').val(tsum);\r\n}\r\n");
        
        strBuild.append(" /*删除tr*/\r\n");
        strBuild.append("function fnDeleteRow(obj,dname) { \r\n");
        strBuild.append("  var sTbid = dname+\"_table\";\r\n");
        strBuild.append("  var oTable = document.getElementById(sTbid);\r\n");
        strBuild.append("  while(obj.tagName !=\"TR\") {\r\n");
        strBuild.append("     obj = obj.parentNode;\r\n}\r\n");
        strBuild.append("  oTable.deleteRow(obj.rowIndex);\r\n}\r\n");
        strBuild.append("</script>");
        
        StringBuilder thBuild = new StringBuilder(),tbBuild = new StringBuilder(),tfTdBuild = new StringBuilder();
        int isNum = 0,tdNum = 0;
        String require = "";
        for (int i=0;i<titles.length;i++) {
        	tdNum++;
			/*String sumTotalHtml = "";
			sums[i] = StringUtil.isInteger(sums[i])?sums[i]:"0";
			if(Integer.parseInt(sums[i])>0) {
				isNum++;
				colTypes[i] = "int";
				sumTotalHtml = "onblur=\"sum_total('"+fieldNames[i]+"')\"";
			}*/
			//thead
			thBuild.append("<td>"+titles[i]+"</td>");
			/*if("text".equals(colTypes[i])) {
				tbBuild.append("<td><input id=\"row-"+fieldNames[i]+"-"+(i+1)+"\" class=\"form-control input-medium "+fieldNames[i]+" "+pluginTypes[i]+"\" type=\"text\" class=\""+fieldNames[i]+"\" name=\""+fieldNames[i]+"\" value=\""+colValues[i]+"\">"+units[i]+"</td>");
			} else if("textarea".equals(colTypes[i])) {
				tbBuild.append("<td><textarea class=\"form-control input-medium "+fieldNames[i]+"\" type=\"text\" class=\""+fieldNames[i]+"\" name=\""+fieldNames[i]+"\" >"+colValues[i]+"</textarea>"+units[i]+"</td>");
			} else if("int".equals(colTypes[i])) {
				tbBuild.append("<td><input class=\"form-control input-medium "+fieldNames[i]+"\" "+sumTotalHtml+" class=\""+fieldNames[i]+"\" type=\"text\" name=\""+fieldNames[i]+"\" value=\""+colValues[i]+"\">"+units[i]+"</td>");
			}*/
			if(YesNoType.YES.getStrValue().equals(fieldRequires[i])) {
				require = " require";
			} else {
				require = "";
			}
			pluginTypes[i] += pluginTypes[i]+require;
			if("text".equals(colTypes[i])) {
				if("cnoj-datetime".equals(pluginTypes[i]) || "cnoj-date".equals(pluginTypes[i]) || "cnoj-time".equals(pluginTypes[i])) {
					tbBuild.append("<td><input id=\"row-"+fieldNames[i]+"-"+(i+1)+"\" class=\"form-control input-medium "+fieldNames[i]+" "+pluginTypes[i]+"\" type=\"text\" data-label-name=\""+titles[i]+"\" name=\""+fieldNames[i]+"\" value=\""+colValues[i]+"\"></td>");
				} else {
					tbBuild.append("<td><input id=\"row-"+fieldNames[i]+"-"+(i+1)+"\" class=\"form-control input-medium "+fieldNames[i]+" "+pluginTypes[i]+"\" type=\"text\" data-label-name=\""+titles[i]+"\" data-uri=\""+pluginUris[i]+"\" name=\""+fieldNames[i]+"\" value=\""+colValues[i]+"\"></td>");
				}
			} else if("textarea".equals(colTypes[i])) {
				tbBuild.append("<td><textarea id=\"row-"+fieldNames[i]+"-"+(i+1)+"\" class=\"form-control input-medium "+fieldNames[i]+" "+pluginTypes[i]+"\" type=\"text\" data-label-name=\""+titles[i]+"\" data-uri=\""+pluginUris[i]+"\" name=\""+fieldNames[i]+"\" >"+colValues[i]+"</textarea></td>");
			} 
			/*else if("int".equals(colTypes[i])) {
				tbBuild.append("<td><input class=\"form-control input-medium "+fieldNames[i]+"\" "+sumTotalHtml+" class=\""+fieldNames[i]+" "+pluginTypes[i]+"\" type=\"text\" name=\""+fieldNames[i]+"\" value=\""+colValues[i]+"\"></td>");
			}*/
			//tfooter
			/*if(Integer.parseInt(sums[i])>0) {
				tfTdBuild.append("<td>合计：<input type=\"text\" class=\"form-control input-small "+fieldNames[i]+"_total\" name=\""+fieldNames[i]+"[total]\" onblur=\"sum_total('"+fieldNames[i]+"')\" value=\""+colValues[i]+"\"> "+units[i]+"</td>");
			} else {*/
				tfTdBuild.append("<td></td>");
			//}
		}
      //有编辑值时，还原table
       // StringBuilder tbTfTrBuild = new StringBuilder(); //tbody  tfooter
       // if(!StringUtil.isEmpty(StringUtil.handNull(dataMap.get("value")))) {
      //  }
        strBuild.append("<table id=\""+name+"_table\" cellspacing=\"0\" class=\"list-ctrl table table-bordered table-condensed\" style=\"width:"+tableWidth+"\">");
        strBuild.append("<thead><tr><th colspan=\""+(tdNum+1)+"\"><div class=\"col-sm-6 p-l-5 listctrl-title\">"+dataMap.get("title")+"</div> <div class=\"col-sm-6 p-r-5 text-right\">");
        strBuild.append("<button class=\"btn btn-sm btn-success listctrl-add-row hidden-print \" type=\"button\" onclick=\"tbAddRow('"+name+"')\">添加一行</button>");
        strBuild.append("</div></th></tr><tr><tr>"+thBuild.toString()+"<th><span class=\"hidden-print\">操作</span></th></tr></thead>");
		
        strBuild.append("<tr class=\"template\" id='row-1'>"+tbBuild.toString()+"<td><a href=\"javascript:void(0);\" onclick=\"fnDeleteRow(this,'"+name+"')\" class=\"delrow hide hidden-print\">删除</a></td></tr></tbody>");
        
        if(isNum>0) {
        	strBuild.append("<tfooter><tr id='row-1'>"+tfTdBuild.toString()+"<td></td></tr></tfooter>");
        }
        strBuild.append("</table>");
        thBuild = null;tbBuild = null;tfTdBuild = null;
        
		return strBuild.toString();
	}

}
