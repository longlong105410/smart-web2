/**
 * 处理流程表单
 * @author lmq
 */
(function($){
	$.fn.initForm = function(options) {
		var defaultOptions = {
			formData:null,
			username:null,
			deptName:null,
			isToLabel: false,
			callback:null
	    };
		var setting = $.extend(true,defaultOptions,options);
		var $this = $(this);
		var $parent = $this.parent();
		$this.addClass("v-hidden");
		$parent.prepend('<div class="cnoj-loading"><i class="fa fa-spinner fa-spin fa-lg"></i> 正在加载，请稍候...</div>');
		setTimeout(function() {
			initFormData(setting.formData);
			//listenerProcessBtns();
			if(!utils.isEmpty(setting.callback) && typeof(setting.callback) === 'function') {
				setting.callback();
			}
			if(typeof(handleForm) === 'function') {
				handleForm();
			}
			//如果富文本为disable时，去掉textarea
			$this.find(".cnoj-richtext").each(function(){
				var $self = $(this);
				if($self.prop("disabled")) {
					$self.addClass("hidden");
					$self.prop("disabled", false);
					$self.parent().html("<div>"+$self.val()+"</div>");
				}
			});
			richtextListener();
			$parent.find(">.cnoj-loading").remove();
			$this.removeClass("v-hidden");
			if(setting.isToLabel) {
				formValueToLabel($this);
			}
		}, 100);
		
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
						   var isListCtrlDel = true;
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
								   if(fieldNames[k] == datas2[j].name) {
									   isTr = true;break;
								   }
							   }
							   if(isTr) {
								   isListCtrlAdd = true;
							   }
							   if(datas2[j].name.endWith("_id")) {
								   isTr = true;
							   }
							   isListCtrlDel = isListCtrlDel && isTr;
						   }
						   //判断是否可以操作listctrl
						   //当能填写或修改列表中的值时，则拥有添加行的权限
						  if(isListCtrlAdd) {
							  $tableTag.find(".listctrl-add-row").show();
						  }
						  //当所有字段都有修改权限时，则拥有删除行的权限
						  if(isListCtrlDel) {
							  $tableTag.find(".delrow:gt(0)").removeClass("hide");
						  }
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
									setFormValue($findElement,value, tagName);
							   } else if(tagName == 'div' && $findElement.hasClass("file-upload")) {
								   formAttPluginHandler($findElement, value);
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
		 * @param tagName 
		 */
		function setFormValue($this, value, tagName) {
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
				if(typeof(tagName) != 'undefined' && tagName == 'div' && $this.hasClass("file-upload")) {
					formAttPluginHandler($this, value);
				} else {
					if(utils.isNotEmpty(value)) {
                        if($this.hasClass('cnoj-datetime')) {
                            if(utils.isNotEmpty(value)) {
                                if(value.endWith(".0"))
                                    value = value.substr(0,19);
                            }
                        } else if($this.hasClass('cnoj-date')) {
                            if(utils.isNotEmpty(value)) {
                                if(value.length>10)
                                    value = value.substr(0,11);
                            }
                        } else if($this.hasClass('cnoj-time')) {
                            if(value.length>=19)
                                value = value.substr(11,19);
                        }
					}
					$this.val(value);
				}
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
 * 处理表单附件
 * @param $element
 * @param value
 */
function formAttHandler($element, value) {
	var isDisabled = $element.prop("disabled");
	var name = $element.attr("name");
	var newName = name+"_file";
	var $eleClone = null;
	if(!isDisabled) {
		$eleClone = $element.clone();
		$eleClone.attr("name", newName);
		$eleClone.attr("id", newName);
		$element.removeClass("require");
		$element.after($eleClone);
		$element.attr("type","text");
	}
	if(utils.isNotEmpty(value)) {
		$element.attr("type","text");
		$element.addClass("hidden");
		attachmentListHandler(value, $element, isDisabled);
		if(null != $eleClone) {
			$eleClone.removeClass("require");
		}
	}
}

/**
 * 附件列表处理者
 * @param value
 * @param $element
 */
function attachmentListHandler(value,$element, isDisabled) {
	$.get("process/attachment/info?id="+value, function(output){
		var attInfos = null;
		var elementId = $element.attr("name");
		var attIds = "";
		if(output.result == 1) {
			attInfos = "<ul class='file-list' id='formatt_'"+elementId+">";
			var len = output.datas.length;
			var datas = output.datas;
			var fileType = null;
			for(var i=0; i<len; i++) {
				attInfos += "<li class='att-item'><span class='visible-print-inline'>"+datas[i][2]+"</span><a class='hidden-print' href='download/att?id="+datas[i][0]+"' target='_blank'>"+datas[i][2]+"</a>（"+datas[i][3]+"）";
				attInfos += "<ul class='form-att-op hidden list-inline hidden-print'>操作：";
				fileType = utils.handleNull(datas[i][4]);
				if(utils.isNotEmpty(fileType)) {
					fileType = fileType.toLowerCase();
				}
				if(fileType == 'jpg' || fileType == 'gif' || fileType == 'png' || fileType == 'txt' || fileType == 'pdf') {
					attInfos += "<li><a href='att/view?id="+datas[i][0]+"' target='_blank'>查看</a></li>";
				}
				attInfos += "<li><a href='download/att?id="+datas[i][0]+"' target='_blank'>下载</a></li>";
				if(!isDisabled) {
					attInfos += "<li><a href='javascript:void(0)' data-input-id='"+elementId+"' onclick=deleteFormAtt(this,'"+datas[i][1]+"','"+datas[i][0]+"')><i class='fa fa-trash' aria-hidden='true'></i> 删除</a></li>";
				}
				attInfos +="</ul></li>";
				attIds += datas[i][0]+",";
			}
			attInfos += "</ul>";
			var $ul = $(attInfos);
			$ul.find(".att-item").mouseover(function() {
				var $this = $(this);
				var h = $this.height();
				var $attOp = $this.find(".form-att-op");
				var pos = $this.position();
				$attOp.css({"top":(pos.top+h)+"px","left":pos.left+"px"});
				$attOp.removeClass("hidden");
			}).mouseout(function() {
				$(this).find(".form-att-op").addClass("hidden");
			});
			//判断是否添加过，如果添加过，则删除附件列表元素
			var $parent = $element.parent();
			var $fileList = $parent.find(".file-list");
			if($fileList.length>0) {
				$fileList.remove();
			}
			$parent.prepend($ul);
		}
		if(utils.isNotEmpty(attIds)) {
			attIds = attIds.substring(0, attIds.length-1);
		}
		$element.val(attIds);
	});
}

/**
 * 删除附件
 * @param elementObj 元素对象
 * @param id 流程附件ID
 * @param attId 附件ID
 */
function deleteFormAtt(elementObj, id, attId) {
	if(utils.isNotEmpty(id)) {
		var $li = $(elementObj).parents("li:eq(0)");
		var $ul = $(elementObj).parents("ul:eq(0)");
		var inputEleId = $(elementObj).data("input-id");
		var formDataId = $("#form-data-id").val();
		BootstrapDialogUtil.delDialog("附件",'process/attachment/deleteForm?fieldId='+inputEleId+'&formDataId='+formDataId+"&attId="+attId,id,function(){
			$li.remove();
			//删除隐藏文本框内的对应的附件ID
			var $parent = $(elementObj).parents(".file-list:eq(0)").parent();
			var $inputEle = $parent.find("input[name='"+inputEleId+"']");
			if($inputEle.length>0) {
				var attIds = $inputEle.val();
				if(utils.isNotEmpty(attIds)) {
					attIds = attIds.replace(attId+",","").replace(","+attId,"").replace(attId,"");
				}
				$inputEle.val(attIds);
			}
			//判断是否还有附件
			$li = $ul.find("li");
			if($li.length == 0) {
                var $inputFile = $ul.parent().find("input:eq(0)");
                $inputFile.val("");
				//$ul.parent().find("input").removeClass("hidden");
			}
			if(utils.isNotEmpty(flowAttUri)) {
				loadUri("#process-att-tab",flowAttUri,false);
			}
		});
	}
}

/**
 * 表单附件插件处理者
 * @param $element
 * @param value
 */
function formAttPluginHandler($element, value) {
	//创建一个隐藏的输入框
	var id = $element.attr("id");
	$element.prepend("<input type='hidden' name='"+id+"' />");
	var $inputEle = $element.find("input[name='"+id+"']");
	if(utils.isNotEmpty(value)) {
		var isDisabled = $element.find(".fileinput-button").hasClass("disabled");
		if(isDisabled) {
			$element.find(".fileinput-button").addClass("hidden");
		}
		attachmentListHandler(value, $inputEle, isDisabled);
	}
		
}

/**
 * 显示表单附件列表
 * @param datas
 * @param $element
 */
function showFormAttList(datas, $element) {
	if(utils.isNotEmpty(datas) && datas.length>0 && utils.isNotEmpty($element)) {
		var id = $element.attr("id");
		var inputName = id.replace("-mfile","");
		var $parent = $("#"+inputName);
		var tagName = $parent.prop("tagName");
		console.log($parent.attr("id")+","+tagName);
		var $inputEle = $parent.find("input[name='"+inputName+"']");
		var attId = '';
		for(var i=0;i<datas.length;i++) {
			attId += datas[i].id+',';
		}
		attId = attId.substring(0, attId.length-1);
		var value = $inputEle.val();
		if(utils.isNotEmpty(value)) {
			attId = value+","+attId;
		}
		attachmentListHandler(attId, $inputEle, false);
	}
} 

/**
 * 表单值转换为label
 * @param $element
 * @returns {Boolean}
 */
function formValueToLabel($element) {
	if(utils.isEmpty($element)) {
		return false;
	}
	$element.find("input[type=text],select,textarea").each(function(){
		var $obj = $(this);
        var tagName = $obj.prop("tagName").toLowerCase();
        if(!$obj.hasClass("hidden") && !$obj.hasClass("hide")) {
            var value = $obj.val();
            if(utils.isNotEmpty(value)) {
                value = utils.replaceAll(value,'\n','<br />');
            }
            if(tagName == 'select') {
            	value = $obj.find("option:selected").text();
            } else if(tagName == 'input' && ($obj.attr("type") == 'checkbox' || $obj.attr("type") == 'radio')) {
            	if($obj.prop("checked")) {
            		$obj.addClass("hidden");
            		return;
            	} else {
            		$obj.parent().addClass("hidden");
            		return;
            	}
            }
            var width = $obj.width();
            $obj.addClass("hidden");
            var $td = $obj.parents("td:eq(0)");
            var tbColor = $td.css("border-color");
            if($td.length==0 || utils.isNotEmpty(tbColor) && 
            		(tbColor.toLowerCase() == '#fff' || tbColor.toLowerCase() == '#ffffff' 
            			|| tbColor.toLowerCase() == 'rgb(255, 255, 255)'))
            	$obj.after("<span style='border-bottom:1px solid #ccc;display:inline-block;width:"+width+"px'>"+value+"</span>");
            else {
            	$obj.after("<span>"+value+"</span>");
            }
        }
    });
}