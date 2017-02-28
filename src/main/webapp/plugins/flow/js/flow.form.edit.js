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
		$parent.prepend('<div class="loading"><i class="fa fa-spinner fa-spin fa-lg"></i> 正在加载，请稍候...</div>');
		initFormData(setting.formData);
		listenerProcessBtns();
		if(!utils.isEmpty(setting.callback) && typeof(setting.callback) === 'function') {
			setting.callback();
		}
		if(typeof(handleForm) === 'function') {
			handleForm();
		}
		$parent.find(".loading").remove();
		$this.removeClass("v-hidden");
		
		/**
		 * 监听流程处理表单按钮
		 */
		function listenerProcessBtns() {
			$(".edit-update-form").unbind("click");
			$(".edit-update-form").click(function() {  //保存表单
				//if($this.validateForm()) {
					var formPorcessInnfo = $("#edit-flow-process-form").serialize();//流程信息
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
						   var $tableTag = $("#"+tableTag);
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
						   $this.find("input[name='"+datas[i].name+"'],select[name='"+datas[i].name+"'],textarea[name='"+datas[i].name+"'],#"+datas[i].name).each(function(){
							   var value = datas[i].value;
							   if(utils.isNotEmpty(value) && value != 'null') {
									setFormValue($(this),value);
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
