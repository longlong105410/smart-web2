/**
 * 处理流程表单
 * @author lmq
 */
var END_NODE_KEY = "end";
(function($){
	$.fn.flowForm = function(options) {
		var defaultOptions = {
			formData:null,
			formFieldNames:null,
			callback:null
	    };
		var setting = $.extend(true,defaultOptions,options);
		var $this = $(this);
		$this.addClass("v-hidden");
		var $parent = $this.parent();
		$parent.prepend('<div class="cnoj-loading"><i class="fa fa-spinner fa-spin fa-lg"></i> 正在加载，请稍候...</div>');
		initFormData(setting.formData);
		listenerProcessBtns();
		if(!utils.isEmpty(setting.callback) && typeof(setting.callback) === 'function') {
			setting.callback();
		}
		if(typeof(handleForm) === 'function') {
			handleForm();
		}
		$parent.find(">.cnoj-loading").remove();
		$this.removeClass("v-hidden");
		
		/**
		 * 监听流程处理表单按钮
		 */
		function listenerProcessBtns() {
			var $wrap = $this.parents(".wrap-content:eq(0)");
			$wrap.find(".edit-update-form").unbind("click");
			$wrap.find(".edit-update-form").click(function() {  //保存表单
				//if($this.validateForm()) {
					var formPorcessInnfo = $wrap.find("#edit-flow-process-form").serialize();//流程信息
					formPorcessInnfo += "&"+$this.serialize();//流程表单信息
					var uri = $this.attr("action");
					if(!utils.isEmpty(uri)) {
						utils.waitLoading("正在更新表单数据...");
						$.post(uri,formPorcessInnfo,function(data){
							utils.closeWaitLoading();
					    	var output = data;
							utils.showMsg(output.msg+"！");
							if(output.result=='1') 
								BootstrapDialogUtil.close();
						});
					}
				//}
				return false;
			});
		}
		
		/**
		 * 初始化表单数据
		 * @param output
		 */
		function initFormData(output) {
			if(!utils.isEmpty(output)) {
				var output = $.parseJSON(output);
				if(output.result == '1') {
				   var datas = output.datas;
				   for(var i=0;i<datas.length;i++) {
					   if(null != datas[i].nameMoreValues && datas[i].nameMoreValues.length>0) {
						   var tableTag = datas[i].name+"_table";
						   var $tableTag = $this.find("#"+tableTag);
						   $tableTag.find(".listctrl-add-row").hide();
						   var datas2 = datas[i].nameMoreValues;
						   var rows = datas2[0].valueSize;
						   for(var j = 1;j<rows;j++) {
							   tbAddRow(datas[i].name);
						   }
						   //处理控件列表
						   for (var j = 0; j < datas2.length; j++) {
							   var index = 0;
							   $tableTag.find("input[name='"+datas2[j].name+"'],select[name='"+datas2[j].name+"'],textarea[name='"+datas2[j].name+"'],#"+datas2[j].name).each(function(){
									if(datas2[j].valueSize>1) {
										if(datas2[j].value[index] != 'null')
											setFormValue($(this),datas2[j].value[index]);
										index++;
									} else {
										if(datas2[j].value != 'null')
											setFormValue($(this),datas2[j].value);
									}
							   });
						   }
						  $tableTag.find(".listctrl-add-row").show();
						  $tableTag.find(".delrow").removeClass("hide");
					   } else {
						   var index = 0;
						   var name = datas[i].name;
						   $this.find("input[name='"+name+"'],select[name='"+name+"'],textarea[name='"+name+"'],#"+name+",span[data-name='"+name+"']").each(function(){
							   var value = datas[i].value;
							   var $findElement = $(this);
							   var tagName = $findElement.prop("tagName").toLowerCase();
							   if(tagName == 'span') {
								   $findElement.attr("data-default-value", value);
							   } else if(utils.isNotEmpty(value) && value != 'null') {
									setFormValue($findElement,value);
							   }
								index++;
						   });
					   }
				   }//for
				}
			}//if
		}
		
		/**
		 * 设置表单值
		 * @param $this
		 * @param value
		 */
		function setFormValue($this, value) {
			var type = $this.attr("type");
			if(type == 'checkbox' || type == 'radio') {
				if(value.indexOf(",")>-1) {
					var values = value.split(",");
				    for(var i=0; i<values.length;i++) {
				    	if($this.val() == values[i]) {
							$this.prop("checked",true);
						}
				    }
				} else {
					if($this.val() == value) {
						$this.prop("checked",true);
					}
				}
			} else if(type == 'file') {
				formAttHandler($this, value);
			} else {
				$this.val(value);
			}
		}
	};
})(jQuery);

/**
 * 删除附件
 */
function listenerAttDel() {
	$("#edit-process-attachment .att-del").click(function(){
		var $tr = $(this).parent().parent();
		var id = $tr.find(".process-att-id").val();
		if(utils.isNotEmpty(id)) {
			BootstrapDialogUtil.delDialog("附件",'op/del.json?busiName=flowAtt',id,function(){
				$tr.remove();
				if(utils.isNotEmpty(flowAttUri)) {
					loadUri("#edit-process-att-tab",flowAttUri,false);
				}
			});
		}
	});
}


/**
 * 处理表单附件
 * @param $element
 * @param value
 */
function formAttHandler($element, value) {
	var isDisabled = false;
	if(utils.isNotEmpty(value)) {
		$element.addClass("hidden");
		$.get("process/attachment/info?id="+value, function(output){
			var attInfos = null;
			var elementId = $element.attr("id");
			if(output.result == 1) {
				attInfos = "<ul id='formatt_'"+elementId+">";
				var len = output.datas.length;
				var datas = output.datas;
				for(var i=0; i<len; i++) {
					attInfos += "<li class='att-item'><a href='download/att?id="+datas[i][0]+"' target='_blank'>"+datas[i][2]+"</a>（"+datas[i][3]+"）";
					if(!isDisabled) {
						attInfos += "<div class='form-att-op hidden'>操作：<a href='javascript:void(0)' data-input-id='"+elementId+"' onclick=deleteFormAtt(this,'"+datas[i][1]+"')><i class='fa fa-trash' aria-hidden='true'></i> 删除</a></div>";
					}
					attInfos +="</li>";
				}
				attInfos += "</ul>";
				var $ul = $(attInfos);
				$ul.find(".att-item").mouseover(function(){
					$(this).find(".form-att-op").removeClass("hidden").width($(this).find("a").width());
				}).mouseout(function(){
					$(this).find(".form-att-op").addClass("hidden");
				});
				$element.parent().prepend($ul);
			} else {
				$element.removeClass("hidden");
			}
		});
	}
}

/**
 * 删除附件
 */
function deleteFormAtt(elementObj, id) {
	if(utils.isNotEmpty(id)) {
		var $li = $(elementObj).parents("li:eq(0)");
		var $ul = $(elementObj).parents("ul:eq(0)");
		var inputEleId = $(elementObj).data("input-id");
		var formDataId = $("#form-data-id").val();
		BootstrapDialogUtil.delDialog("附件",'flow/attachment/deleteForm?fieldId='+inputEleId+'&formDataId='+formDataId,id,function(){
			$li.remove();
			//判断是否还有附件
			$li = $ul.find("li");
			if($li.length == 0) {
				$ul.parent().find("input").removeClass("hidden");
			}
			if(utils.isNotEmpty(flowAttUri)) {
				loadUri("#edit-process-att-tab",flowAttUri,false);
			}
		});
	}
}
