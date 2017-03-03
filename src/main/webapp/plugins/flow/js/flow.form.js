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
		var $parent = $this.parent();
		if(!$this.hasClass("v-hidden")) {
			$this.addClass("v-hidden");
			$parent.prepend('<div class="cnoj-loading"><i class="fa fa-spinner fa-spin fa-lg"></i> 正在加载，请稍候...</div>');
		}
		setTimeout(function() {
			controlFormField();
			initFormData(setting.formData);
			listenerProcessBtns();
			if(!utils.isEmpty(setting.callback) && typeof(setting.callback) === 'function') {
				setting.callback();
			}
			if(typeof(handleForm) === 'function') {
				handleForm();
			}
			$parent.find(".cnoj-loading").remove();
			$this.removeClass("v-hidden");
		}, 500);
		
		
		/**
		 * 控制表单字段
		 */
		function controlFormField() {
			$this.find("input,select,textarea").each(function(){
				$(this).prop("disabled",true);
				$(this).attr("title","");
			});
			//所有list-ctrl属性改为只读
			$this.find(".list-ctrl").find("input,select,textarea").each(function(){
				$(this).prop("disabled",false);
				$(this).prop("readonly",true);
			});
			if(!utils.isEmpty(setting.formFieldNames)) {
				var fieldNames = setting.formFieldNames.split(",");
				for(var i=0;i<fieldNames.length;i++) {
					$this.find("input[name='"+fieldNames[i]+"'],select[name='"+fieldNames[i]+"'],textarea[name='"+fieldNames[i]+"'],#"+fieldNames[i]).each(function(){
						$(this).prop("disabled",false);
						$(this).prop("readonly",false);
					});
				}
			}
			$this.find(".list-ctrl").find("input,select,textarea").each(function(){
				if($(this).prop("readonly")) {
					$(this).removeClass("cnoj-input-select");
					$(this).removeClass("cnoj-input-select-relate");
					$(this).removeClass("cnoj-auto-complete");
					$(this).removeClass("cnoj-auto-complete-relate");
					$(this).removeClass("cnoj-input-tree");
					
					$(this).removeClass("cnoj-datetime-listener");
					$(this).removeClass("cnoj-date-listener");
					$(this).removeClass("cnoj-time-listener");
				}
			});
			
		}
		
		/**
		 * 监听流程处理表单按钮
		 */
		function listenerProcessBtns() {
			$("#save-form").click(function() {  //保存表单
				if($this.validateForm()) {
					var formPorcessInnfo = $("#flow-process-form").serialize();//流程信息
					formPorcessInnfo += "&"+$this.serialize();//流程表单信息
					var uri = $(this).data("uri");
					if(!utils.isEmpty(uri)) {
						utils.waitLoading("正在处理表单数据...");
						$.post(uri,formPorcessInnfo,function(data){
							utils.closeWaitLoading();
					    	var output = data;
							utils.showMsg(output.msg+"！");
							if(output.result=='1') 
								$("#form-data-id").val(output.data);
						});
					}
				}
				return false;
			});
			//驳回按钮
			$("#back-process").click(function(){
				var $backLineRow = $("#select-back-line-row");
				$(".task-submit-form,.is-suggest").hide();
				$("#node-decision-back-prop").show();
				var isShow = false;
				if($backLineRow.data("is-show") == 1) {
					isShow = true;
					$backLineRow.removeClass("hide");
				}
				var $isSug = $backLineRow.parent().find(".is-suggest");
				if(!utils.isEmpty($isSug.attr("class"))) {
					isShow = true;
					$isSug.show();
				}
				if(isShow) {
					$('#myModal').modal('show');
				}
				exePorcessBack($(this),isShow);
				return false;
			});
			//提交按钮
			$("#submit-process").click(function(){
				//隐藏任务提交的表单及处理意见所在的元素，
				//原因是：避免于点击驳回按钮时候的冲突(显示驳回时候的处理意见或显示驳回时的表单)
				$(".task-submit-form,.is-suggest").hide();
				
				var $nextLineRow = $("#select-next-line-row");
				//默认选择下一环节
				if($nextLineRow.data("is-concurrent") == 1) {
					$nextLineRow.find("input").prop("checked",true).attr("onclick","return false");
				} else {
					$nextLineRow.find("input:first").prop("checked",true);
				}
				//新增根据配置判断，是否需要验证表单；修改于2016年10月29日
				var isNotCheck = true;
				$nextLineRow.find("input[name=nextLineName]:checked").each(function(){
					var isCheckVal = $(this).data("is-check");
					if(isCheckVal == utils.YES_OR_NO.NO) {
						isNotCheck = isNotCheck && true;
					} else {
						isNotCheck = isNotCheck && false;
					}
				});
				var isHasCheckSuccess = false; //是否已经验证成功
				if(isNotCheck) { //如果不用验证表单，则认为已经验证成功
					isHasCheck = true;
				} else {
					isHasCheck = $this.validateForm();
				}
				if(isHasCheck) {
					//显示下一步处理所在的表单（只显示提交时候的所在的表单）
				    $("#node-decision-next-prop").show();
					var isShow = false;
					//当有多个出口时，需要显示所有的下一环节；提供用户选择
					//只有一个出口时，不显示下一环节
					if($nextLineRow.data("is-show") == 1) {
						isShow = true;
						$nextLineRow.removeClass("hide");
					}
					//获取下一环节处理者所在的元素
					var $nextAssignerRow = $("#select-next-assigner-row");
					//判断处理下一环节是否需要选人
					isHandleNextAssigner($nextLineRow);
					
					//如果下一环节处理者所在元素存在，并且需要显示时，显示下一环节处理者；提供用户选择下一环节处理者
					if(!utils.isEmpty($nextAssignerRow.attr("id")) && $nextAssignerRow.data("is-show") == 1) {
						isShow = true;
						$nextAssignerRow.removeClass("hide");
					}
					var $isSug = $nextLineRow.parent().find(".is-suggest");
					if(!utils.isEmpty($isSug.attr("class"))) {
						isShow = true;
						$isSug.show();
					}
					if(isShow) {
						$('#myModal').modal('show');
					}
					exePorcessNext($(this),isShow);
				}
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
						   $tableTag.find(".delrow").addClass("hide");
						   var isListCtrlAdd = false;
						   var isListCtrlDel = false;
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
							   var fieldNames = setting.formFieldNames.split(",");
							   var isTr = false;
							   for(var k=0;k<fieldNames.length;k++) {
								   if(fieldNames[k] == datas2[j].name) {isTr = true;break;}
							   }
							   if(isTr) {
								   isListCtrlAdd = true;
							   }
							   //isListCtrl = isListCtrl && isTr;
						   }
						   //判断是否可以操作listctrl
						  //if(isListCtrl) {
						      if(isListCtrlAdd) {
						    	  $tableTag.find(".listctrl-add-row").show();
						      }
						      if(isListCtrlDel) {
						    	  $tableTag.find(".delrow").removeClass("hide");
						      }
						 // }
					   } else {
						   var index = 0;
						   $this.find("input[name='"+datas[i].name+"'],select[name='"+datas[i].name+"'],textarea[name='"+datas[i].name+"'],#"+datas[i].name).each(function(){
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
			} else {
				$this.val(value);
			}
		}
	};
})(jQuery)

/**
 * 删除附件
 */
function listenerAttDel() {
	$("#process-attachment .att-del").click(function(){
		var $tr = $(this).parent().parent();
		var id = $tr.find(".process-att-id").val();
		if(utils.isNotEmpty(id)) {
			BootstrapDialogUtil.delDialog("附件",'op/del.json?busiName=flowAtt',id,function(){
				$tr.remove();
				if(utils.isNotEmpty(flowAttUri)) {
					loadUri("#process-att-tab",flowAttUri,false);
				}
			});
		}
	});
}

/**
 * 提交任务
 * @param url
 * @param param
 * 
 */
function submitTask(url,param) {
	var formPorcessInfo = $("#flow-process-form").serialize();
	//流程表单信息
	formPorcessInfo += "&"+$("#process-handle-form").serialize();
	if(!utils.isEmpty(param)) {
		formPorcessInfo += "&"+param;
	}
	var refreshUrl = $("#refresh-url").val();
	refreshUrl = utils.isEmpty(refreshUrl)?"process/todo":refreshUrl;
	if(!utils.isEmpty(url)) {
		utils.waitLoading("正在处理表单数据...");
		$.post(url,formPorcessInfo,function(data){
			utils.closeWaitLoading();
			var output = data;
			utils.showMsg(output.msg+"！");
			if(output.result=='1') {
				closeActivedTab();
				var title = "我的待办";
				if($('#main-tab').tabs('exists',"我的待办")) {
					$('#main-tab').tabs('select',title);
					reloadTab(refreshUrl);
				}
				loadingTodoData();
			}
		});
	}
}

/**
 * 完成当前任务--驳回
 * @param objTag
 * @param isShow
 */
function exePorcessBack(objTag,isShow) {
	var $backLineRow = $("#select-back-line-row");
	var $backProp = $("#node-decision-back-prop");
	$backLineRow.find("input:first").prop("checked",true);
	var isSugArea = false;  //是否有意见域
	var sugContent = null;
	if(!utils.isEmpty($backProp.find(".is-suggest").attr("class"))) {
		isSugArea = true;
	}
	var isSubmit = true;
	if(isShow) {
		//当点击确定按钮时，处理的事件
		$("#dialog-ok").unbind('click');
		$("#dialog-ok").click(function(){
			var param = '';
			if(isSugArea) {
				sugContent = $backProp.find(".handle-suggest").val();
				if(utils.isEmpty(sugContent)) {
					$backProp.validateForm();
					isSubmit = false;
					return false;
				} else {
					isSubmit = true;
				}
			}
			if(isSubmit) {
				$('#myModal').modal('hide');
				$('#myModal').on('hidden.bs.modal', function (e) {
					handleRequest(objTag,$backLineRow,"isBack=1");
				});
			}
			return false;
		});
	} else {
		handleRequest(objTag,$backLineRow,"isBack=1");
	}
}

/**
 * 处理请求
 * @param objTag
 * @param $lineRowTag
 * @param param
 */
function handleRequest(objTag,$lineRowTag,param) {
	var params = $lineRowTag.parents("form").serialize();
	if(!utils.isEmpty(param)) 
		params = params+"&"+param;
	var url = objTag.data("uri");
	
	//判断是否起草人提交
	if(utils.isEmpty($("#form-data-id").val())) {
		//检测标题
		var titleFieldId = checkInsTitle();
		if(utils.isNotEmpty(titleFieldId)) {
			var name = $("#"+titleFieldId).data("label-name");
			BootstrapDialogUtil.confirmDialog("该"+utils.handleNull(name)+"已经存在，是否继续提交？", function() {
				submitTask(url,params);
			});
		} else {
			submitTask(url,params);
		}
	} else {
		submitTask(url,params);
	}
}

/**
 * 检测标题是否存在
 * @returns 
 * 如果标题存在则返回标题对象的字段ＩＤ
 */
function checkInsTitle() {
	var titleFieldId = null;
	var formPorcessInfo = $("#flow-process-form").serialize();
	//流程表单信息
	formPorcessInfo += "&"+$("#process-handle-form").serialize();
	if(utils.isNotEmpty(formPorcessInfo)) {
		utils.waitLoading("正在验证标题是否重复...")
		$.ajax({
			url: 'process/checkInsTitle.json',
			async: false,
			data: formPorcessInfo,
			success: function(output) {
				utils.closeWaitLoading();
				if(output.result == '1') {
					titleFieldId = output.data;
				}
			}
		})
	}
	return titleFieldId;
}

/**
 * 完成当前任务--继续往下执行
 * @param objTag
 * @param isShow
 */
function exePorcessNext(objTag,isShow) {
	var $nextLineRow = $("#select-next-line-row");
	var $nextProp = $("#node-decision-next-prop")
	
	var $nextAssignerRow = $("#select-next-assigner-row");
	var isSelectAssigner = false; //是否需要选择参与者
	var checkedAssigners = null; //选中的参与者
	if($nextAssignerRow.data("is-show") == utils.YES_OR_NO.YES) {
		isSelectAssigner = true;
		listenerOrInitNextAssigners($nextLineRow,$nextAssignerRow);
		var isShow = $nextAssignerRow.data("is-show")==1?true:false;
		if(!isShow) {
			checkedAssigners = getCheckedAssigners($nextLineRow);
		}
	}
	var isSugArea = false;  //是否有意见域
	var sugContent = null;
	if(!utils.isEmpty($nextProp.find(".is-suggest").attr("class"))) {
		isSugArea = true;
	}
	var isSubmit = true;
	var param = '';
	var nextAssigners = '';
	if(isShow) {
		//当点击确定按钮时，处理的事件
		$("#dialog-ok").unbind('click');
		$("#dialog-ok").click(function(){
			if(isSelectAssigner) {
				checkedAssigners = getCheckedAssigners($nextLineRow);
				if(utils.isEmpty(checkedAssigners)) {
					isSubmit = false;
					return false;
				} else 
					isSubmit = true;
				if(utils.isNotEmpty(checkedAssigners) && checkedAssigners != END_NODE_KEY)
					nextAssigners = "nextAssigners="+checkedAssigners;
			}
			if(isSugArea) {
				sugContent = $nextProp.find(".handle-suggest").val();
				if(utils.isEmpty(sugContent)) {
					$nextProp.validateForm();
					isSubmit = false;
					return false;
				} else 
					isSubmit = true
			}
			if(!utils.isEmpty(nextAssigners)) 
				param = param+"&"+nextAssigners;
			if(isSubmit) {
				$('#myModal').modal('hide');
				$('#myModal').on('hidden.bs.modal', function (e) {
					$("#myModal").unbind('hidden.bs.modal');
					handleRequest(objTag, $nextLineRow, param);
				});
			}
			return false;
		});
	} else {
		if(utils.isNotEmpty(checkedAssigners) && checkedAssigners != END_NODE_KEY) {
			param = param+"&nextAssigners="+checkedAssigners;
		}
		handleRequest(objTag, $nextLineRow, param);
	}
}

/**
 * 监听或初始化下一环节参与者
 * @param $nextLineRow
 * @param $nextAssignerRow
 */
function listenerOrInitNextAssigners($nextLineRow,$nextAssignerRow) {
	var selectStyle = $nextAssignerRow.data("select-style");
	var isShow = $nextAssignerRow.data("is-show")==1?true:false;
	var processId = $("#process-id").val();
	var orderId = $("#order-id").val();
	var isSelect = 0;
	$nextLineRow.find("input[name=nextLineName]:checked").each(function(){
		isSelect = $(this).data("is-select");
		selectStyle = $(this).data("select-style");
		if(isSelect == utils.YES_OR_NO.YES) {
			createNextAssignerTree($(this), processId, orderId,selectStyle,isShow);
		}
	});
	var isConcurrent = $nextLineRow.data("is-concurrent");
	if(isConcurrent == 0) {
		$nextLineRow.find("input[type=radio]").click(function(){
			var selectUserTreeId = $(this).attr("id")+"-user-org-tree";
			if(utils.isEmpty($("#"+selectUserTreeId).attr("id"))) {
				//摧毁之前建立的树
				var beforeTreeId = null;
				$nextLineRow.find("input[type=radio]").each(function(){
					beforeTreeId = $(this).attr("id")+"-user-org-tree";
					$("#"+beforeTreeId).zTreeUtil({destory:true});
				});
				$("#select-assigner-content").html("");
				isSelect = $(this).data("is-select");
				selectStyle = $(this).data("select-style");
				if(isSelect == utils.YES_OR_NO.YES) {
					createNextAssignerTree($(this), processId, orderId,selectStyle,isShow);
				}
			}
		});
	}
}

/**
 * 生成选择下一环节参与者树
 * @param tagObj
 * @param processId
 * @param orderId
 * @param selectStyle
 * @param isShow
 */
function createNextAssignerTree(tagObj,processId,orderId,selectStyle,isShow) {
	var selectUserTreeId = tagObj.attr("id")+"-user-org-tree";
	if(utils.isEmpty($("#"+selectUserTreeId).attr("id"))) {
	    $("#select-assigner-content").append("<div id='"+selectUserTreeId+"' class='select-user-tree-wrap left'><div class='next-title ui-state-default'>"+tagObj.parent().text()+"</div></div>");
	}
	var pathName = tagObj.val();
	var taskKey = pathName.split("_")[1];
	var params = "processId="+processId+"&orderId="+orderId+"&taskKey="+taskKey;
	$("#select-next-assigner-row").hide();
	if(taskKey != END_NODE_KEY) {
		$("#select-next-assigner-row").show();
		var url = "process/selectNextAssigner.json?"+params;
		var checkOpt = null;
		if(selectStyle == 'radio') {
			checkOpt = {check: {
				enable: true,
				chkStyle: "radio",
				radioType: "all"
			}};
		}
		$("#"+selectUserTreeId).zTreeUtil({
			uri:url,
			isCheck:true,
			isAjaxAsync:false,
			isSearch:false,
			isLoading:true,
			checkOpt:checkOpt,
			callback:function(zTreeObj){
				if(selectStyle=='checkbox' && !isShow) 
					zTreeObj.checkAllNodes(true);
			}
		});
	} else {
		$("#select-next-assigner-row").hide();
		$("#select-assigner-content").html("");
	}
}

/**
 * 获取选中的参与者
 * @param $nextLineRow
 */
function getCheckedAssigners($nextLineRow) {
	var checkedAssigners = '';
	var isNext = true;
	$nextLineRow.find("input[name=nextLineName]:checked").each(function(){
		var checkedAssigner = null;
		var selectUserTreeId = $(this).attr("id")+"-user-org-tree";
		var taskKey = $(this).val().split("_")[1];
		$("#"+selectUserTreeId).zTreeUtil({
			getTreeObj:function(zTreeObj){
				if(null != zTreeObj) {
					var nodes = zTreeObj.getCheckedNodes(true);
					if(null != nodes && nodes.length>0) {
						checkedAssigner = "";
						for (var i = 0; i < nodes.length; i++) {
							checkedAssigner += nodes[i].id+",";
						}
						checkedAssigner = checkedAssigner.substring(0,checkedAssigner.length-1);
					}
				}
			}
		});
		if(null != checkedAssigner) 
			checkedAssigners += taskKey+"("+checkedAssigner+");";
		else if(taskKey == END_NODE_KEY) {
			checkedAssigners += taskKey+";";
		} else {
			$("#"+selectUserTreeId).addClass("border-color-red");
			utils.showMsg("请选择下一步处理者！");
			setTimeout(function(){
	    		$("#"+selectUserTreeId).removeClass("border-color-red");
			}, 2000);
			isNext = isNext && false;
			return false;
		}
	});
	if(checkedAssigners != '') {
		checkedAssigners = checkedAssigners.substring(0,checkedAssigners.length-1);
	}
	checkedAssigners = isNext?checkedAssigners:'';
	return checkedAssigners;
}

/**
 * 判断处理一下环节是否需要选人
 * @param $nextLineRow
 */
function isHandleNextAssigner($nextLineRow) {
	var selectedNextTask = '';
	$nextLineRow.find("input[name=nextLineName]").each(function(){
		selectedNextTask = selectedNextTask+$(this).val().split("_")[1]+",";
	});
	selectedNextTask = utils.isNotEmpty(selectedNextTask)?selectedNextTask.substring(0, selectedNextTask.length-1):selectedNextTask;
	var processId = $("#process-id").val();
	$.ajax({
		url:'process/isSelectAssigner.json?nextTaskKeys='+selectedNextTask+"&processId="+processId,
		type:'get',
		async:false,
		success:function(data) {
			if(data.result == utils.YES_OR_NO.YES) {
				$("#select-next-assigner-row").data("is-show",data.data);
				if(data.data == utils.YES_OR_NO.YES) {
					var taskKey = null;
					var resultValues = null;
					$nextLineRow.find("input[name=nextLineName]").each(function(){
						var $this = $(this);
						taskKey = $this.val().split("_")[1];
						for(var i=0;i<data.datas.length;i++) {
							resultValues = data.datas[i].split("_");
							if(taskKey == resultValues[0]) {
								$this.data("is-select",resultValues[1]);
								$this.data("select-style",resultValues[2]);
								break;
							}
						}
					});
				}
			}
		}
	});
}
