/**
 * @author lmq
 * 监听事件
 * @param $
 */
(function($) {
	/**
	 * 监听增删改按钮组
	 * 标识 
	 *  class="cnoj-op-btn-list"
	 *  会监听class为"cnoj-op-btn-list" 元素下面class为"add","edit","del","refresh","open-pop","open-self","open-new-tab","open-blank"的按钮；
	 *  class="add" 添加按钮
	 *  参数
	 *     必须
	 *         data-uri  点击该按钮时显示的页面（是一个弹出窗口）
	 *         data-title 弹出窗口的标题
	 *      可选
	 *         selected-value 选中表格行时获取的值
	 *         data-busi  指定页面处理类；java dao
	 *         data-width 指定弹出窗口宽度
	 *         data-before-check 检查方法；该方法有两个参数；参数名及参数值；
	 *         如果返回：false则不执行后续操作；返回true；进行执行
	 *         
	 * class="edit" 编辑按钮 
	 *  参数
	 *     必须
	 *         data-uri  点击该按钮时显示的页面（是一个弹出窗口）
	 *         data-title 弹出窗口的标题
	 *         selected-value 选中表格行时获取的值(要修改数据的Id)
	 *         data-busi  指定页面处理类；java dao
	 *      可选
	 *         data-width 指定弹出窗口宽度
	 *         data-before-check 检查方法；该方法有两个参数；参数名及参数值；
	 *         如果返回：false则不执行后续操作；返回true；进行执行
	 *       
	 * class="del"  删除按钮
	 *  参数
	 *     必须
	 *         data-uri  删除uri
	 *         data-msg 删除数据时，提示的信息
	 *         selected-value 选中表格行时获取的值(要删除的数据ID，多个ID直接用英文逗号隔开)
	 *         data-busi  指定页面处理类；java dao
	 *      可选
	 *         data-del-after 删除成功后执行的js函数;它的优先级高于"data-refresh-uri"和"data-target";
	 *         data-refresh-uri 删除成功后要刷新页面的uri；当data-del-after设置了，该参数忽略
	 *         data-target 指定刷新页面显示的地方，默认为"#main-content";改成参数和“data-refresh-uri”成对出现；当data-del-after设置了，该参数忽略
	 * class="refresh"  刷新按钮
	 *  参数
	 *     必须
	 *        data-uri 要刷新uri
	 *     可选
	 *        data-target 刷新显示的地方，默认为"#main-content";
	 *        
	 * class="open-pop" 自定义按钮，打开指定uri页面（弹出窗口）
	 *  参数
	 *     必须
	 *       data-uri  显示uri
	 *       data-title 弹出窗口的标题
	 *     可选
	 *       selected-value 选中表格行时获取的值(要修改数据的Id)
	 *       data-width 指定弹出窗口宽度
	 *       data-param-name 指定节点ID的参数名称，默认名称为："id"
	 *       data-before-check 检查方法；该方法有两个参数；参数名及参数值；
	 *         如果返回：false则不执行后续操作；返回true；进行执行
	 *
	 *class="open-new-tab" 自定义按钮，在Tab中打开指定URL页面
	 *  参数
	 *     必须
	 *       data-uri  显示uri
	 *       data-title Tab窗口的标题
	 *     可选
	 *       selected-value 选中表格行时获取的值(要修改数据的Id)
	 *       data-width 指定弹出窗口宽度
	 *       data-param-name 指定节点ID的参数名称，默认名称为："id"
	 *       data-before-check 检查方法；该方法有两个参数；参数名及参数值；
	 *         如果返回：false则不执行后续操作；返回true；进行执行
	 */
	$.fn.btnListener = function() {
		var $idTag = $(this);
		$idTag.find(".add").each(function(){
			var $this = $(this);
			if(!utils.isContain($this.attr("class"), "add-listener")) {
				$this.addClass("add-listener");
				$this.click(function(){
					openProp($(this),"add");
				});
			}
		});
		$idTag.find(".edit").each(function(){
			var $this = $(this);
			if(!utils.isContain($this.attr("class"), "edit-listener")) {
				$this.addClass("edit-listener");
				$this.click(function(){
					var value = $(this).attr("selected-value");
					if(!utils.isEmpty(value)) {
						if((value).indexOf(',')>0) {
							BootstrapDialogUtil.warningAlert("编辑只能选择一条数据!");
							return;
						} else {
							openProp($(this),"edit");
						} 
					} else 
						BootstrapDialogUtil.warningAlert("请选择一条数据!");
				});
			}
			//$this = null;
		});
		
		$idTag.find(".del").each(function(){
			var $this = $(this);
			if(!utils.isContain($this.attr("class"), "del-listener")) {
				$this.addClass("del-listener");
				$this.click(function(){
					var uri = $(this).data("uri");
					var value = $(this).attr("selected-value");
					var busiName = $(this).data("busi");
					var msg = $(this).data("msg");
					var successFun = $(this).data("del-after");
					var refreshUri = $(this).data("refresh-uri");
					var target = $(this).data("target");
					if(!utils.isEmpty(value)) {
						if(!utils.isEmpty(uri)) {
							if(uri.indexOf("?")>0)
								uri = uri+"&id="+value+"&busiName="+busiName;
							else 
								uri = uri+"?id="+value+"&busiName="+busiName;
							BootstrapDialogUtil.confirmDialog(msg,function(){
								$.post(uri,function(data){
									var output = data;//$.parseJSON(data.output);
									utils.showMsg(output.msg+"！");
									if(output.result=='1') {
										if(!utils.isEmpty(successFun)) {
											setTimeout(successFun, 0);
										} else {
											if(!utils.isEmpty(refreshUri)) {
												if(!utils.isEmpty(target)) {
													loadUri(target, refreshUri, true);
												} else {
													//loadLocation(refreshUri);
													loadActivePanel(refreshUri);
												}
											}
										}
									}
								});
							});
						}
					} else 
						BootstrapDialogUtil.warningAlert("请选择数据!");
				});
				
			}
		});
		
		$idTag.find(".refresh").each(function(){
			var $this = $(this);
			if(!utils.isContain($this.attr("class"), "refresh-listener")) {
				$this.addClass("refresh-listener");
				$this.click(function(){
					var uri = $(this).data("uri");
					var target = $(this).data("target");
					if(!utils.isEmpty(uri)) {
						if(!utils.isEmpty(target)) {
							loadUri(target,uri);
						} else {
							loadLocation(uri);
						}
					}
				});
			}
		});
		
		$idTag.find(".open-pop").each(function(){
			var $this = $(this);
			if(!utils.isContain($this.attr("class"), "open-pop-listener")) {
				$this.addClass("open-pop-listener");
				$this.click(function(){
					openProp($(this),null,'open-pop');
				});
			}
		});
		$idTag.find(".open-self").each(function(){
			var $this = $(this);
			if(!utils.isContain($this.attr("class"), "open-self-listener")) {
				$this.addClass("open-self-listener");
				$this.click(function(){
					openProp($(this),null,'open-self');
				});
			}
		});
		$idTag.find(".open-new-tab").each(function(){
			var $this = $(this);
			if(!utils.isContain($this.attr("class"), "open-new-tab-listener")) {
				$this.addClass("open-new-tab-listener");
				$this.click(function(){
					openProp($(this),null,'open-new-tab');
				});
			}
		});
		$idTag.find(".open-blank").each(function(){
			var $this = $(this);
			if(!utils.isContain($this.attr("class"), "open-blank-listener")) {
				$this.addClass("open-blank-listener");
				$this.click(function(){
					openProp($(this),null,'open-blank');
				});
			}
		});
	}
})(jQuery)


/**
 * 监听单击全选(复选框)
 * 标识
 *  class="cnoj-checkbox-all" 
 *  参数
 *    必须
 *      data-target 指定要选中的复选框
 *  单机该复选框会选中(或取消)data-target指定的所有class为"cnoj-op-checkbox"的复选框
 *  对应的值会赋值到指定 "<div class='btn-list'><div class='cnoj-op-btn-list'></div></div>" 
 *  里面class为"param"元素里面.
 *  
 */
function checkboxAllListener() {
	$(".cnoj-checkbox-all").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-checkbox-all-listener")) {
			$this.addClass("cnoj-checkbox-all-listener");
			$this.click(function(){
				var target = $this.data("target");
				var $panel = $(this).parents(".panel");
				if(!utils.isEmpty(target)) {
					if($(this).prop("checked")) {
						$panel.find(target).each(function(){
							if(!$(this).prop("disabled")) {
								var $tr = $(this).parents("tr.tr-mutil-selected:eq(0)");
								if(!utils.isEmpty($tr.attr("class"))) {
									$tr.addClass("ui-state-focus");
									$tr.find("td").addClass("ui-state-focus");
								}
								$(this).prop("checked",true);
							}
						});
					} else {
						$panel.find(target).each(function(){
							var $tr = $(this).parents("tr.tr-mutil-selected:eq(0)");
							if(!utils.isEmpty($tr.attr("class"))) {
								$tr.removeClass("ui-state-focus");
								$tr.find("td").removeClass("ui-state-focus");
							}
							$(this).prop("checked",false);
						});
					}
				} 
				var classNames = $this.attr("class");
				if(utils.isContain(classNames,"cnoj-op-checkbox")) {
					var ids = "";
					$panel.find(target).each(function(){
						if($(this).prop("checked")) {
							var id = $(this).val();
							if(!utils.isEmpty(id))
							   ids += id+",";
						}
					});
					if(!utils.isEmpty(ids))
						ids = ids.substring(0, ids.length-1);
					var $param = null;
					if(typeof($panel.attr("class")) !== 'undefined') {
						$param = $panel.find(".cnoj-op-btn-list .param");
						if(typeof($param.attr("class")) === 'undefined') {
							$param = null;
						}
					}
					if(null != $param) {
						$param.attr("selected-value",ids);
					}
				}
			});
		}
		//$this = null;
  });
}


/**
 * 监听单击单个复选框
 * class="cnoj-op-checkbox"
 * 点击该复选框时，会把对应的值赋值到指定的地方；"<div class='btn-list'><div class='cnoj-op-btn-list'></div></div>" 
 * 下面class为"param"元素里面.
 * 
 */
function checkboxListener() {
	$(".cnoj-op-checkbox").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-op-checkbox-listener")) {
				classNames = $(this).attr("class");
				$this.addClass("cnoj-op-checkbox-listener");
				if(!utils.isContain(classNames,"cnoj-checkbox-all")) {
					$this.click(function(){
						var ids = "";
						$(this).parents(".cnoj-checkbox-wrap:eq(0)").find(".cnoj-op-checkbox").each(function(){
							if($(this).prop("checked")) {
								var id = $(this).val();
								if(!utils.isEmpty(id))
								   ids += id+",";
							}
						});
						if(!utils.isEmpty(ids))
							ids = ids.substring(0, ids.length-1);
						var $panel = $(this).parents(".panel:eq(0)");
						var $param = null;
						if(typeof($panel.attr("class")) !== 'undefined') {
							$param = $panel.find(".cnoj-op-btn-list .param");
							if(typeof($param.attr("class")) === 'undefined') {
								$param = null;
							}
						}
						if(null != $param) {
							$param.attr("selected-value",ids);
						}
					});
				}
		}
		//$this = null;
	});
}

/**
 * 表单必填监听
 * 标识
 * class="require"
 */
function formRequireListener() {
	$("input[type=text].require,select.require,textarea.require").each(function(){
		if(!$(this).prop("disabled")) {
			var id = $(this).attr("id");
			var newIdTag = id+"-"+"require";
			if(utils.isEmpty($(this).parent().find("#"+newIdTag).attr("class"))) {
				$(this).after("<span id='"+newIdTag+"' class='star-require hidden-print'> * </span>");
			}
		}
	});
}

/**
 * 链接监听,也可以是按钮或其他
 * class="cnoj-change-page" 该标识主要是用来标记分页，点击页面时触发的事件
 *   参数:必须 data-uri 分页uri
 *       可选 data-target 显示地方(一般为一个div层)
 *       data-search-panel-tag 搜索面板标识
 *   
 * class="cnoj-open-self" 点击时，指定的uri显示到当前"#main-content"里面
 *   参数：必须 data-uri 显示uri
 *        可选 data-target 显示地方(一般为一个div层)
 *        
 * class="cnoj-open-blank" 点击时，会弹出一个新窗口（弹出窗口）;  
 *    参数： 必须 data-uri 弹出页面的uri
 *         可选 data-title 弹出窗口的标题;data-width 弹出窗口的宽度
 *         
 */
function hrefListener() {
	//$(".cnoj-change-page").unbind("click")
	$(".cnoj-change-page").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-change-page-listener")) {
			$this.addClass("cnoj-change-page-listener");
			$this.click(function(event) {
				var uri = $(this).data("uri");
				uri = utils.isEmpty(uri)?$(this).attr("href"):uri;
				//获取搜索参数
				var searchPanelTag = $(this).data("search-panel-tag");
				var $searchPanel = null;
				if(utils.isEmpty(searchPanelTag)) {
					$searchPanel = $(this).parents(".panel:eq(0)").find(">.panel-search");
				} else {
					$searchPanel = $(searchPanelTag);
				}
				if(utils.isExist($searchPanel)) {
					var $form = $searchPanel.find("form");
					if(utils.isExist($form)) {
						uri = uri+"&"+$form.serialize();
					}
				}
				var target = $(this).data("target");
				if (!utils.isEmpty(uri)) {
					if(!utils.isEmpty(target))
						loadUri(target,uri,true);
					else
						loadActivePanel(uri);
				}
				event.stopPropagation();
				return false;
			});
		}
		//$this = null;
	});
	
	//$(".cnoj-open-self").unbind("click")
	$(".cnoj-open-self").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-open-self-listener")) {
			$this.addClass("cnoj-open-self-listener");
			$this.click(function(event) {
				var uri = $(this).data("uri");
				var target = $(this).data("target");
				var title = $(this).data("title");
				if(utils.isEmpty(uri)) {
					uri = $(this).attr("href");
				}
				if (!utils.isEmpty(uri)) {
					if(!utils.isEmpty(target) && target != '#main-content')
						loadUri(target, uri, true);
					else 
						openTab(title, uri, true);
						//loadLocation(uri);
				}
				event.stopPropagation();
				return false;
			});
		}
		//$this = null;
	});
	
	//$(".cnoj-open-blank").unbind("click")
	$(".cnoj-open-blank").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-open-blank-listener")) {
			$this.addClass("cnoj-open-blank-listener");
			$this.click(function(event){
		        var uri = $(this).data("uri");
		        var title = $(this).data("title");
		        var w = $this.data("width");
		        if(!utils.isEmpty(uri)) {
		          if(utils.isEmpty(w)) {
		        	  w = $(window).width()-100;
		          }
		          BootstrapDialogUtil.loadUriDialog(title,uri,w,"#fff",false,function(){
						setTimeout(function(){
							initEvent();
						}, 200);
				  });
		        }
		        event.stopPropagation();
		        return false;
		     });
			
		}
		//$this = null;
	});
	
}

/**
 * 加载到当前激活的面板中
 * @param uri
 */
function loadActivePanel(uri) {
	//var $panel = getActiveTabPanel();
	//handleLoading(uri, $panel);
	reloadTab(uri); 
}

/**
 * 单击搜索按钮提交数据
 * 标识
 *   class="cnoj-search-submit" 标记在触发按钮上
 *   参数
 *     必须
 *       action 该参数为form表单的action属性；提交的路径url
 *     可选
 *       target 该参数为form表单的target属性；提交数据请求之后，返回内容显示的位置，默认为"#main-content"
 *       data-loading-target-tag 要获取载入页面指定的内容
 */
function searchSubmitListener(){
	//$(".cnoj-search-submit").unbind("click");
	$(".cnoj-search-submit").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-search-submit-listener")) {
			$this.addClass("cnoj-search-submit-listener");
			$this.click(function() {
			    var $form = $(this).parents("form:eq(0)");
				var param = $form.serialize();
			    var uri = $form.attr("action");
			    var target = $form.attr("target");
			    var loadingTargetTag = $form.data("loading-target-tag");
			    param = encodeURI(param);
			    if(!utils.isEmpty(uri)) {
			    	if(utils.isContain(uri, "?")) {
			    		 uri = uri+"&"+param;
			    	} else {
			    		 uri = uri+"?"+param;
			    	}
			    	uri = utils.isEmpty(loadingTargetTag)?uri:uri+" "+loadingTargetTag;
			 	    if(!utils.isEmpty(target) && mainTag != target) {
			 		    loadUri(target,uri);
			 	    } else {
			 		    //loadLocation(uri);
			 	    	loadActivePanel(uri);
			 	    }
			    }
			    return false;
			});
		}
		//$this = null;
	});
	
}

/**
 * 载入指定的uri到DIV标识为id的层中
 * @param id
 * @param uri
 * @param isLoadProcess 是否有加载等待提示,默认为:true
 * @param isCheckLogin 是否验证用户登录,默认为:true(访问页面时验证用户是否已登录)
 */
function loadUri(id,uri,isLoadProcess,isCheckLogin) {
	if(false != isLoadProcess) {
		isLoadProcess = true;
	}
	if(utils.isEmpty(isCheckLogin)) {
		isCheckLogin = true;
	}
	isCheckLogin = isCheckLogin == false ? false : true;
	if(isCheckLogin) {
		$.get("user/islogin.json",function(data){
			if(data.result=='1') {
				handleLoading(uri,$(id));
			} else {
				location.reload();
			}
		});
	} else {
		handleLoading(uri,$(id));
	}
}

/**
 * 加载处理
 * @param uri
 * @param obj
 */
function handleLoading(uri,obj) {
	var array = utils.parseUri(uri);
	if(null != array) {
		obj.html('<div class="cnoj-loading"><i class="fa fa-spinner fa-spin fa-lg"></i> 正在加载，请稍候...</div><div class="loading-content"></div>');
		var $target = obj.find(".loading-content");
		$target.css("visibility","hidden");
		$("body").css({"overflow":"hidden"});
		$target.load(array[0],array[1],function() {
			setTimeout(function(){
				obj.find(".cnoj-loading").remove();
				initEvent();
				$target.css("visibility","visible");
				$("body").css("overflow","auto");
			}, 200);
		});
	}
}

/**
 * 表格树监听
 * 标识
 * table class为:"cnoj-tree-table"
 * tr class为:"tr-tree"
 * td class为:"op-tree"
 */
function tableTreeListener() {
	//$(".cnoj-tree-table .tr-tree .op-tree").unbind("click");
	$(".cnoj-tree-table .tr-tree .op-tree").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "op-tree-listener")) {
			$this.addClass("op-tree-listener");
			$this.click(function(){
				var classNames = $(this).attr("class");
				if(utils.isContain(classNames," shrink-data")) {
					var $spanIcon = $(this).find("span.ui-icon");
					var trId = $(this).parent().attr("id");
					if(trId !== 'undefined') {
						$("."+trId).show();
						$spanIcon.removeClass("ui-icon-triangle-1-e");
						$spanIcon.addClass("ui-icon-triangle-1-s");
						
						$(this).removeClass("shrink-data");
						$(this).addClass("open-data");
					}
				} else {
					var stackArray = new Array();
					var id = $(this).parent().attr("id");
					if(!utils.isEmpty(id)) {
						$("."+id).hide();
						var $spanIcon = $(this).find("span.ui-icon");
						$spanIcon.removeClass("ui-icon-triangle-1-s");
						$spanIcon.addClass("ui-icon-triangle-1-e");
						
						$(this).removeClass("open-data");
						$(this).addClass("shrink-data");
						stackArray.push(id);
					}
					while(stackArray.length>0) {
						stackArray = stackArray.concat(shrinkTableTree(stackArray.pop()));
					}
					stackArray = null;
				}
			});
		}
	});
}
/**
 * 收缩表格树
 * @param id
 * @returns {Array}
 */
function shrinkTableTree(id) {
	var array = new Array();
	$(".cnoj-tree-table .open-data").each(function(){
		var parentId = $(this).parent().attr("parentid");
		$("."+id).hide();
		var $spanIcon = $("#"+id).find("span.ui-icon");
		$spanIcon.removeClass("ui-icon-triangle-1-s");
		$spanIcon.addClass("ui-icon-triangle-1-e");
		
		$("#"+id+" .op-tree").removeClass("open-data");
		$("#"+id+" .op-tree").addClass("shrink-data");
		if(id == parentId) {
			var trId = $(this).parent().attr("id");
			array.push(trId);
		}
	});
	return array;
}


/**
 * 树形表格行选中监听
 * 标识
 * table为：class="cnoj-tree-table"
 * tr为：class="tr-tree"
 */
function tableTreeSelectListener() {
	//$(".cnoj-tree-table .tr-tree").unbind("click");
	$(".cnoj-tree-table .tr-tree").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "tr-tree-listener")) {
			$this.click(function(){
				var classNames = $(this).attr("class");
				$(".cnoj-tree-table .tr-tree").each(function(){
					$(this).removeClass("ui-state-focus");
					$(this).find("td").removeClass("ui-state-focus");
				});
				var $panel = $(this).parents(".panel:eq(0)");
				var $param = null;
				if(typeof($panel.attr("class")) !== 'undefined') {
					$param = $panel.find(".cnoj-op-btn-list .param");
					if(typeof($param.attr("class")) === 'undefined') {
						$param = null;
					}
				}
				if(!utils.isContain(classNames," ui-state-focus")) {
					$(this).addClass("ui-state-focus");
					$(this).find("td").addClass("ui-state-focus");
					var id = $(this).attr("id");
					id = id.substring(2,id.length);
					if(null != $param) {
						$param.attr("selected-value",id);
					}
					
				} else {
					$(this).removeClass("ui-state-focus");
					$(this).find("td").removeClass("ui-state-focus");
					if(null != $param) {
						$param.attr("selected-value","");
					}
				}
			});
			$this.addClass("tr-tree-listener");
		}
		//$this = null;
	});
}


/**
 * 表格行选中监听
 * 表格标识
 *    class="cnoj-table" 如:<table class="cnoj-table"></table>
 * 该表格最好放到一个叫class="panel"的div里面，如:<div class="panel"><table class="cnoj-table"></table></div>
 *    选中行的class必须为"tr-selected";如 <tr class="tr-selected"></tr>
 *    如果只能选中单行，则class中要有"tr-one-selected";可以选中多行，则要有"tr-mutil-selected";如:<tr class="tr-selected tr-one-selected"></tr>
 *    或<tr class="tr-selected tr-mutil-selected"></tr>
 *要获取选中的参数，则，放到<div class="btn-list"><div class="cnoj-op-btn-list"></div></div>里面
 *并且class设置为param
 *    
 */
function tableSelectListener() {
	//$(".cnoj-table .tr-selected").unbind("click");
	$(".cnoj-table .tr-selected").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "tr-selected-listener")) {
			$this.addClass("tr-selected-listener");
			$this.click(function(){
				var classNames = $(this).attr("class");
				var $panel = $(this).parents(".panel:eq(0)");
				var $param = null;
				if(typeof($panel.attr("class")) !== 'undefined') {
					$param = $panel.find(".btn-list .cnoj-op-btn-list .param");
					if(typeof($param.attr("class")) === 'undefined') {
						$param = null;
					}
				}
				
				var ids = null;
				//单选
				if(utils.isContain(classNames, "tr-one-selected")) {
					 $(".cnoj-table .tr-one-selected").each(function(){
						 $(this).removeClass("ui-state-focus");
						 $(this).find("td").removeClass("ui-state-focus");
					});
					if(!utils.isContain(classNames," ui-state-focus")) {
						$(this).addClass("ui-state-focus");
						$(this).find("td").addClass("ui-state-focus");
						ids = $(this).attr("id");
						ids = ids.substring(2,ids.length);
						if(null != $param) {
							$param.attr("selected-value",ids);
						}
					} else {
						$(this).removeClass("ui-state-focus");
						$(this).find("td").removeClass("ui-state-focus");
						if(null != $param) {
							$param.attr("selected-value","");
						}
					}
				} else if(utils.isContain(classNames, "tr-mutil-selected")) {
					//var $checkbox = $(this).find(".cnoj-op-checkbox");
					//alert($checkbox.prop("checked"));
					if(!utils.isContain(classNames," ui-state-focus")) {
						$(this).addClass("ui-state-focus");
						$(this).find("td").addClass("ui-state-focus");
						$(this).find(".cnoj-op-checkbox").prop("checked",true);
					} else {
						$(this).removeClass("ui-state-focus");
						$(this).find("td").removeClass("ui-state-focus");
						$(this).find(".cnoj-op-checkbox").prop("checked",false);
					}
					ids = "";
					$(this).parents(".cnoj-checkbox-wrap:eq(0)").find(".cnoj-op-checkbox").each(function(){
						if($(this).prop("checked")) {
							var id = $(this).val();
						    if(!utils.isEmpty(id))
						        ids += id+",";
						}
					});
					if(!utils.isEmpty(ids))
						ids = ids.substring(0, ids.length-1);
					if(null != $param) {
						$param.attr("selected-value",ids);
					}
				}
				//执行选中触发事件
				var selectedEventType = $this.data("selected-type");
				if(!utils.isEmpty(selectedEventType)) {
					var selectedUri = $this.data("selected-uri");
					var selectedTarget = $this.data("selected-target");
					var selectedVarName = $this.data("selected-varname");
					selectedVarName = utils.isEmpty(selectedVarName)?"id":selectedVarName;
					if(!utils.isEmpty(selectedUri) && !utils.isEmpty(ids)) {
						selectedUri = selectedUri+(selectedUri.indexOf("?")>0?"&":"?")+selectedVarName+"="+ids;
						cnoj.selectedEvent(selectedEventType,selectedUri,selectedTarget);
					}
				}
			});
		}
		//$this = null;
	});
}


/**
 * 监听表单输入框树
 * 标识
 *   class='cnoj-input-tree'
 * 参数
 *   必须
 *     data-uri 指定数据来源uri 
 *   可选
 *     data-is-show-none 是否显示"无"数据节点;默认为:no(不显示) 可选的值为:"yes"或"no"
 *     data-is-ajax-async 加载数据时是ajax否异步加载；默认为:no(不显示) 可选的值为:"yes"或"no"
 *     data-is-async 是否异步加载（即：分步加载）；默认为:no(不显示) 可选的值为:"yes"或"no"
 *     data-async-url 异步加载的话（即：data-is-async="yes"），异步加载URL;
 *     data-on-click 列表单机时除非该事件；传递js方法名称 
 */
function inputTreeListener() {
	//$(".cnoj-input-tree").unbind("click");
	$(".cnoj-input-tree").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-input-tree-listener")) {
			$this.addClass("cnoj-input-tree-listener");
			var uri = $this.data("uri");
			var isShowNode = $this.data("is-show-none");
			isShowNode = (isShowNode == 'yes'?true:false);
			
			var isAjaxAsync = $this.data("is-ajax-async");
			isAjaxAsync = (isAjaxAsync == 'yes'?true:false);
			
			var onClickFun = $this.data("on-click");
			onClickFun = utils.isEmpty(onClickFun)?null:onClickFun;
			
			var isAsync = $this.data("is-async");
			isAsync = (isAsync == 'yes'?true:false);
			var asyncUrl = null;
			if(isAsync) {
				asyncUrl = $this.data("async-url");
				if(utils.isEmpty(asyncUrl)) {
					asyncUrl = uri;
				}
			}
			
			if(!utils.isEmpty(uri)) {
				$(this).zTreeUtil({
					uri:uri,
					isAsync : isAsync,
					isAjaxAsync : isAjaxAsync,
					getAsyncUri: asyncUrl,
					isInput:true,
					isInputTreeShow:false,
					isShowNone:isShowNode,
					onClick:onClickFun
				});
			}
			$this.click(function(event){
				if(!utils.isEmpty(uri)) {
					$(this).zTreeUtil({
						uri:uri,
						isInput:true,
						isShowNone:isShowNode,
						onClick:onClickFun
					});
				}
				event.stopPropagation();
			});
		}
	});
}

/**
 * 监听表单组织机构树
 * 标识
 *   class='cnoj-input-org-tree'
 *   参数
 *   可选
 *     data-is-show-none 是否显示"无"数据节点;默认为:no(不显示) 可选的值为:"yes"或"no"
 */
function inputOrgTreeListener() {
	//$(".cnoj-input-org-tree").unbind("click");
	//var uri = 'op/queryTree?resId=select_org_tree';
	var uri = 'org/tree.json';
	$(".cnoj-input-org-tree").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-input-org-tree-listener")) {
			$this.addClass("cnoj-input-org-tree-listener");
			var isShowNode = $(this).data("is-show-none");
			isShowNode = (isShowNode == 'yes'?true:false);
			var isAjaxAsync = $(this).data("is-ajax-async");
			isAjaxAsync = (isAjaxAsync == 'yes'?true:false);
			
			var isAsync = $(this).data("is-async");
			isAsync = (isAsync == 'yes'?true:false);
			if(!utils.isEmpty(uri)) {
				$(this).zTreeUtil({
					uri:uri,
					isInput:true,
					isAsync : isAsync,
					isAjaxAsync : isAjaxAsync,
					isInputTreeShow:false,
					isShowNone:isShowNode
				});
			}
			$this.click(function(event){
				if(!utils.isEmpty(uri)) {
					$(this).zTreeUtil({
						uri:uri,
						isInput:true,
						isShowNone:isShowNode
					});
				}
				event.stopPropagation();
			});
		}
		//$this = null;
	});
	
}


/**
 * 监听面板树
 * 标识
 *   class='cnoj-panel-tree'
 * 参数
 *   必须
 *     data-uri 指定数据来源uri
 *   可选
 *     data-redirect-uri 点击节点之后触发的uri
 *     data-is-search 是否支持搜索功能;yes--支持；no--不支持;默认为：no
 *     data-is-node-link 节点是否支持超链接;yes--支持;no--不支持；默认为：no
 *     data-is-default-load 是否默认加载data-redirect-uri指定的uri，该选项设置了data-is-node-link为"yes"的时候才生效
 *     data-target 指定uri内容显示的位置；如果该值为空或没设置，则默认显示到"#main-content"层，一般与"data-redirect-uri"成对出现;
 *     data-param-name 指定节点ID的参数名称，默认名称为："id"
 */
function panelTreeListener() {
	$(".cnoj-panel-tree").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-panel-tree-listener")) {
			$this.addClass("cnoj-panel-tree-listener");
			var uri = $(this).data("uri");
			var redirectUri = $(this).data("redirect-uri");
			var target = $(this).data("target");
			var isSearch = $(this).data("is-search");
			isSearch = utils.isEmpty(isSearch)?false:(isSearch=='yes'?true:false);
			var paramName = $(this).data("param-name");
			var isNodeLink = $(this).data("is-node-link");
			var panelHeight = $(this).data("panel-height");
			panelHeight = utils.regexInteger(panelHeight)?panelHeight:0;
			isNodeLink = utils.isEmpty(isNodeLink)?false:(isNodeLink == 'yes'?true:false);
			var isDefaultLoad = false;
			if(isNodeLink) {
				isDefaultLoad = $(this).data("is-default-load");
				isDefaultLoad = utils.isEmpty(isDefaultLoad)?false:(isDefaultLoad == 'yes'?true:false);
			}
			
			var isAjaxAsync = $(this).data("is-ajax-async");
			isAjaxAsync = (isAjaxAsync == 'yes'?true:false);
			
			var isAsync = $(this).data("is-async");
			isAsync = (isAsync == 'yes'?true:false);
			
			if(!utils.isEmpty(uri)) {
				$(this).zTreeUtil({
					uri:uri,
					isInput:false,
					isAsync : isAsync,
					isAjaxAsync : isAjaxAsync,
					redirectUri:redirectUri,
					target:target,
					isSearch:isSearch,
					panelHeight:panelHeight,
					isNodeLink:isNodeLink,
					isDefaultLoad:isDefaultLoad,
					paramName:paramName
				});
			}
		}
		//$this = null;
	});
}

/**
 * 监听还有复选框面板树
 * 标识
 *   class='cnoj-panel-check-tree'
 * 参数
 *   必须
 *     data-uri 指定数据来源uri
 *   可选
 *     data-is-search 是否支持搜索功能;值为"yes"或"no"
 *     data-param-name 指定节点ID的参数名称，默认名称为："id"
 *     data-check-opt 选项json格式;如:{check:{chkStyle:"radio",radioType: "level/all"}};
 *     data-is-ajax-async 加载数据时是ajax否异步加载；默认为:yes(异步加载) 可选的值为:"yes"或"no"
 *     data-is-async 是否异步加载（即：分步加载）；默认为:no(否) 可选的值为:"yes"或"no"
 *     data-async-url 异步加载的话（即：data-is-async="yes"），异步加载URL
 */
function panelCheckTreeListener() {
	$(".cnoj-panel-check-tree").each(function(){	
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-panel-check-tree-listener")) {
			$this.addClass("cnoj-panel-check-tree-listener");
			var uri = $(this).data("uri");
			var isSearch = $(this).data("is-search");
			    isSearch = utils.isNotEmpty(isSearch)?("yes"==isSearch?true:false):false;
			var checkOpt = $(this).data("check-opt");
			    checkOpt = utils.isNotEmpty(checkOpt)?checkOpt:null;
			var paramName = $(this).data("param-name");
			    paramName = utils.isEmpty(paramName)?"id":paramName;
			var isAjaxAsync = $(this).data("is-ajax-async");
			isAjaxAsync = (isAjaxAsync == 'no'?false:true);
			var isAsync = $(this).data("is-async");
			isAsync = (isAsync == 'yes'?true:false);
			var asyncUrl = null;
			if(isAsync) {
				asyncUrl = $this.data("async-url");
				if(utils.isEmpty(asyncUrl)) {
					asyncUrl = uri;
				}
			}
			if(!utils.isEmpty(uri)) {
				$(this).zTreeUtil({
					uri:uri,
					isInput:false,
					isAsync : isAsync,
					isAjaxAsync : isAjaxAsync,
					getAsyncUri: asyncUrl,
					isCheck:true,
					checkOpt:checkOpt,
					isSearch:isSearch,
					paramName:paramName
				});
			}
		}
		//$this = null;
	});
}

/**
 * 监听面板组织结构树
 * 标识
 *   class='cnoj-panel-org-tree'
 * 参数
 *   data-redirect-uri 添加节点之后触发的uri
 *   data-target 指定uri内容显示的位置；如果该值为空或没设置，则默认显示到"#main-content"层，一般与"data-redirect-uri"成对出现;
 *   data-param-name 指定节点ID的参数名称，默认名称为："id"
 * 
 */
function panelOrgTreeListener() {
	var uri = 'org/tree';
	$(".cnoj-panel-org-tree").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-panel-org-tree-listener")) {
			$this.addClass("cnoj-panel-org-tree-listener");
			var redirectUri = $(this).data("redirect-uri");
			var target = $(this).data("target");
			var isSearch = $(this).data("is-search");
			isSearch = utils.isEmpty(isSearch)?false:(isSearch=='yes'?true:false);
			var paramName = $(this).data("param-name");
			var isNodeLink = $(this).data("is-node-link");
			var panelHeight = $(this).data("panel-height");
			panelHeight = utils.regexInteger(panelHeight)?panelHeight:0;
			isNodeLink = utils.isEmpty(isNodeLink)?false:(isNodeLink == 'yes'?true:false);
			var isDefaultLoad = false;
			if(isNodeLink) {
				isDefaultLoad = $(this).data("is-default-load");
				isDefaultLoad = utils.isEmpty(isDefaultLoad)?false:(isDefaultLoad == 'yes'?true:false);
			}
			
			var isAjaxAsync = $(this).data("is-ajax-async");
			isAjaxAsync = (isAjaxAsync == 'yes'?true:false);
			
			var isAsync = $(this).data("is-async");
			isAsync = (isAsync == 'yes'?true:false);
			
			if(!utils.isEmpty(uri)) {
				$(this).zTreeUtil({
					uri:uri,
					isInput:false,
					isAsync : isAsync,
					isAjaxAsync : isAjaxAsync,
					redirectUri:redirectUri,
					target:target,
					isSearch:isSearch,
					panelHeight:panelHeight,
					isNodeLink:isNodeLink,
					isDefaultLoad:isDefaultLoad,
					paramName:paramName
				});
			}
		}
		//$this = null;
	});
}

/**
 * 提交按钮监听
 * 标识
 * class="cnoj-data-submit"
 * 该标识是提交表单触发的事件
 * 除了action参数在form表单里面填写之外，其他的参数都在该标识元素中填写
 * 参数
 *   必须
 *      action 该参数就是form表单的action属性,在提交的form的action里面填写该值,指定提交的路径(action)
 *   可选
 *      data-target-form 该参数，指定提交哪个form表单的ID（有多个form表单时）;如:target-form="#add-form";
 *      data-fun 该参数是一个回调函数，提交成功后执行的方法;
 *      data-refresh-uri 该参数指定数据指定成功后，刷新的uri;
 *      target 该参数为form表单的target属性；提交数据请求之后，返回的内容显示到哪个里面，默认为"#main-content"
 *      与data-refresh-uri成对出现.
 *      
 */
function submitBtnListener() {
	//$(".cnoj-data-submit").unbind("click");
	$(".cnoj-data-submit").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-data-submit-listener")) {
			$this.addClass("cnoj-data-submit-listener");
			$this.click(function(event){
				var target = $(this).data("target-form");
				var fun = $(this).data("fun");
				var $form = null;
				if(!utils.isEmpty(target)) {
					$form = $("#"+target);
					if(typeof($form.attr("id")) === 'undefined') {
						$form = null;
						//console.error("data-target-form 必须指定form的id");
					}
				} else {
					 $form = $(this).parents("form:eq(0)");
				}
				if($form.validateForm()) {
					var param = $form.serialize();
				    var uri = $form.attr("action");
				    cnoj.submitDialogData(uri,param,fun,$(this),$form);
				}
				event.stopPropagation();
				return false;
			});
		}
		//$this = null;
	});
}

/**
 * 监听表格内容的高度
 * 标识
 * class="cnoj-table-wrap"
 * 该标识是用于包裹表格而设计的，非表格请使用"auto-limit-height"标识
 */
function tableWrapListener() {
	$(".cnoj-table-wrap").each(function(){
		if(!utils.isContain($(this).attr("class"), "cnoj-table-wrap-listener")) {
			$(this).addClass("cnoj-table-wrap-listener");
			var $tableWrap = $(this);
			var h = 0;
			var $autoLimitHeiht = $tableWrap.parents(".cnoj-auto-limit-height:eq(0)");
			if(utils.isExist($autoLimitHeiht)) {
				h = $autoLimitHeiht.height();
			} else {
				h = getMainHeight();
				//去掉tabs高度
				h = h - getTabHeaderHeight();
			}
			var $parentWrap = $tableWrap.parent();
			var subtractHeight = $parentWrap.data("subtract-height");
			subtractHeight = utils.isEmpty(subtractHeight)?0:subtractHeight;
			h = h - subtractHeight;
			
			var panelHeadingHeight = 0;
			var $panelHeading = $parentWrap.find(".panel-heading");
			if(utils.isExist($panelHeading)) {
				panelHeadingHeight = $panelHeading.data("height");
				if(utils.isEmpty(panelHeadingHeight)) {
					panelHeadingHeight = $panelHeading.outerHeight(true);
					panelHeadingHeight = Math.ceil(panelHeadingHeight);
				}
			}
			h = h - panelHeadingHeight;
			var $panelFooter = $parentWrap.find(".panel-footer");
			var panelFooterHeight = 0;
			var panelFooterDataH = $panelFooter.data("height");
			if(utils.isNotEmpty(panelFooterDataH)) {
				panelFooterHeight = panelFooterDataH;
			} else {
				panelFooterHeight = $panelFooter.outerHeight(true);
			}
			panelFooterHeight = utils.isEmpty(panelFooterHeight)?0:panelFooterHeight;
			h = h - panelFooterHeight;
			var $panelSearch = $parentWrap.find(".panel-search");
			if(utils.isExist($panelSearch)) {
				var searchH = $panelSearch.outerHeight(true);
				searchH = Math.ceil(searchH);
				var dataSearchH = $panelSearch.data("height");
				searchH = (dataSearchH>searchH)?dataSearchH:searchH;
				searchH = utils.isEmpty(searchH)?0:searchH;
				h = h - searchH;
			}
			$tableWrap.height(h - 5);
			$tableWrap.css({"overflow":"auto"});
			if(!$tableWrap.hasClass("table-body-scroll")) {
				return;
			}
			//调整表格；使表格头和表格内容分离
			var $table = $tableWrap.find("table");
			var $theadTr = $table.find("thead").clone(true);
			var $tableTheader = null;
			if(!utils.isEmpty($theadTr)) {
				$tableWrap.before("<div class='table-theader-bg "+$table.find("thead").find("tr").attr("class")+"'><div class='table-theader' data-height='"+$table.find("thead").find("tr").data("height")+"'><table class='"+$table.attr("class")+"'></table></div></div>");
				$table.find("thead").remove();
				$tableTheader = $tableWrap.prev().find(".table-theader");
				$tableTheader.find("table").append($theadTr);
				var panelHeadingHeight = 0;
				var panelHeadingDataH = $panelFooter.data("height");
				if(utils.isNotEmpty(panelHeadingDataH)) {
					panelHeadingHeight = panelHeadingDataH;
				} else {
					panelHeadingHeight = $tableTheader.outerHeight(true);
					panelHeadingHeight = Math.ceil(panelHeadingHeight);
				}
				panelHeadingHeight = utils.isEmpty(panelHeadingHeight)?0:panelHeadingHeight;
				h = h - panelHeadingHeight;
				$tableWrap.height(h);
			}
			$tableWrap.css({"overflow":"auto"});
			$parentWrap = null;
			if(utils.isScroll($tableWrap)) {
				$tableTheader.width($tableWrap.width()-utils.getScrollWidth());
			} else {
				$tableTheader.css({"width":"auto"});
			}	
			$tableWrap.click(function(e) {
				if(utils.isScroll($tableWrap)) {
					$tableTheader.width($tableWrap.width()-utils.getScrollWidth());
				} else 
					$tableTheader.css({"width":"auto"});
			});
		}
	});
}

/**
 * 监听限制高度标识
 * 标识
 * class="cnoj-auto-limit-height"
 * 该标识会根据浏览器高度，自适应被该标识标记的DIV层
 */
function limitHeightListener() {
	//var $limitHeight = $(".cnoj-auto-limit-height");
	$(".cnoj-auto-limit-height").each(function(){
		var $limitHeight = $(this);
		if(!utils.isContain($limitHeight.attr("class"), "cnoj-auto-limit-height-listener")) {
			$limitHeight.addClass("cnoj-auto-limit-height-listener");
			var h = getMainHeight();
			var subtractHeight = $limitHeight.data("subtract-height");
			if(!utils.isEmpty(subtractHeight)) {
				h = h - getTabHeaderHeight() - subtractHeight;
			} else {
				var top = $limitHeight.position().top;
				top = Math.ceil(top);
				h = h - getTabHeaderHeight() - top - 8;
			}
			$limitHeight.height(h);
			$limitHeight.css({"overflow":"auto"});
		}
		//$limitHeight = null;
	});
}

/**
 * 监听文件上传标识
 * 标识
 * class="cnoj-upload"
 * 参数
 *   必须
 *     data-uri 上传到服务器上的action路径,如:data-uri="uploadAttr/uploadAttr"
 *   可选
 *     data-form-data 表单参数;填写表单ID；如:data-form-data="#form-id"
 *     data-accept-file-types 支持上传的文件类型;多个类型之间用英文逗号","分开;如:data-accept-file-types="gif,png,jpg"
 *     data-max-file-size 支持上传的文件最大大小;单位为:byte;如:data-max-file-size="1024000000000"
 *     data-pop-width 上传文件时弹出窗口的宽度
 *     data-pop-height 上传文件时弹出窗口的高度
 *     data-progressbar 是否显示上传进度条;1--显示；0--不显示
 *     data-close-after 文件上传结束后，关闭窗口时执行的回调函数;返回一个数组
 *     
 */
function uploadFileListener() {
	$(".cnoj-upload").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-upload-listener")) {
			$this.addClass("cnoj-upload-listener");
			var uri = $this.data("uri");
			var formData = $this.data("form-data");
			var acceptFileTypes = $this.data("accept-file-types");
			var maxFileSize = $this.data("max-file-size");
			var popWidth = $this.data("pop-width");
			var popHeight = $this.data("pop-height");
			var progressBar = $this.data("progressbar");
			var closeAfterFun = $this.data("close-after");
			if(!utils.isEmpty(uri)) {
				formData = utils.isEmpty(formData)?null:$(formData).serializeArray();
				popWidth = utils.isEmpty(popWidth)?450:popWidth;
				popHeight = utils.isEmpty(popHeight)?300:popHeight;
				progressBar = (utils.isEmpty(progressBar) || progressBar=='0')?false:true;
				closeAfterFun = utils.isEmpty(closeAfterFun)?null:eval(closeAfterFun);
				if(null != formData) {
					formData = JSON.stringify(param);
				}
				var param = {
						uri:uri,
						formData:formData,
						popWidth:popWidth,
						popHeight:popHeight,
						progressBar:progressBar,
						closeAfterFun:closeAfterFun
				};
				if(!utils.isEmpty(acceptFileTypes)) {
					acceptFileTypes = acceptFileTypes.replace(/\,/g,'|');
					param = $.extend(param,{acceptFileTypes:'/'+acceptFileTypes+'$/i'});
				}
				if(!utils.isEmpty(maxFileSize) && utils.regexInteger(maxFileSize)) {
					param = $.extend(param,{maxFileSize:maxFileSize});
				}
				$this.jqueryFileUpload(param);
			}
		}
		//$this = null;
	});
}

/**
 * 监听日期时间标识
 * 标识
 * class="cnoj-datetime"(yyyy-mm-dd hh:ii:ss)或
 * class="cnoj-date"(yyyy-mm-dd) 或
 * class="cnoj-time" (hh:ii:ss)
 * 可选参数
 * data-start-date="" 设置时间范围--开始时间
 * data-end-date=""   设置时间范围--结束时间
 * data-date-format="" 日期格式；如：yyyy-mm-dd 或yyyy年mm月dd日
 */
function inputDateListener() {
	$(".cnoj-datetime,.cnoj-date,.cnoj-time").each(function(){
		  var $this = $(this);
		  if(!utils.isContain($this.attr("class"), "cnoj-datetime-listener") && 
				  !utils.isContain($this.attr("class"), "cnoj-date-listener") && 
				  !utils.isContain($this.attr("class"), "cnoj-time-listener") && 
				  !$this.prop("readonly") && !$this.prop("disabled") && !$this.hasClass("hide")) {
			  var $formGroup = $(this).parents(".form-group:eq(0)");
			  var classParentNames = $formGroup.attr("class");
			  var classNames = $this.attr("class");
			  if(!utils.isContain(classParentNames,'has-feedback') && (!utils.isContain(classNames, "cnoj-datetime-listener") 
					  || !utils.isContain(classNames, "cnoj-date-listener") || !utils.isContain(classNames, "cnoj-time-listener"))) {
				  if(utils.isEmpty(classParentNames)) {
					  /*$this.parent().addClass('has-feedback');
					  var top = 0;
					  var left = 0;
					  var pos = $this.position();
					  top = pos.top-3;
					  left = pos.left+($this.width()-18);
					  $this.after("<span class='glyphicon glyphicon-calendar inline-icon form-control-feedback hidden-print' style='top:"+top+"px;left:"+left+"px'></span>");
					  var $icon = $this.find(".inline-icon");*/
					  var $parent = $this.parent();
					  var count = 0;
					  //$parent.find(".cnoj-datetime,.cnoj-date,.cnoj-time").each(function(){
					  $parent.find("input").each(function(){
						  count++;
					  });
					  if(count>1) {
						  if(!$parent.hasClass("form-group")) {
							  var $next = $this.next();
							  if($next.hasClass("star-require")) {
								  $next.remove();
							  }
							  $this.wrap("<div class='text-inline-block' style='width:"+($this.outerWidth(true)+10)+"px'></div>");
						  }
						  $this.parent().addClass('has-feedback');
						  $this.after("<span class='glyphicon glyphicon-calendar inline-icon form-control-feedback hidden-print' style='right:3px;line-height: 30px;'></span>");
					  } else {
						  var $next = $this.next();
						  if($next.hasClass("star-require")) {
							  $next.remove();
						  }
						  var parentW = $parent.outerWidth();
						  var thisW = $this.outerWidth();
						  var w = ((thisW+30)<parentW)?(thisW+10):parentW;
						  $this.wrap("<div class='text-inline-block' style='width:"+w+"px'></div>");
						  $this.parent().addClass('has-feedback');
						  var inputH = $this.outerHeight();
						  var top = inputH>28?0:-2;
						  var style = "top:"+top+"px;";
						  //判断是否在iframe中；如果相等，不是iframe；否则是iframe
						  var notIframe = (top.location == location);
						  if(notIframe) {
							  style = style+"right:5%;";
						  }
						  $this.after("<span class='glyphicon glyphicon-calendar inline-icon form-control-feedback hidden-print' style='"+style+"'></span>");
					  }
					  var $icon = $this.find(".inline-icon");
				  } else {
					  $formGroup.addClass("has-feedback");
					  $this.after("<span class='glyphicon glyphicon-calendar form-control-feedback hidden-print'></span>");
				  }
				  var setting = {};
				  if(utils.isContain(classNames,'cnoj-datetime')) {
					  setting = {format: 'yyyy-mm-dd hh:ii:ss',startView:2};
				  } else if(utils.isContain(classNames,'cnoj-date')) {
					  setting = {format: 'yyyy-mm-dd',minView: 2};
				  } else if(utils.isContain(classNames,'cnoj-time')) {
					  var date = new Date();
					  var month = date.getMonth()+1;
					  month = month<10?('0'+month):month;
					  var day = date.getDate();
					  day = day<10?('0'+day):day;
					 var startDate = date.getFullYear()+"-"+month+"-"+day;
					  setting = {format: 'hh:ii:ss',startDate:startDate,startView: 1,minView: 0,maxView:1};
				  }
				  var startDate = $this.data("start-date");
				  var endDate = $this.data("end-date");
				  if(!utils.isEmpty(startDate)) {
					  setting = $.extend(setting,{startDate:startDate});
				  }
				  if(!utils.isEmpty(endDate)) {
					  setting = $.extend(setting,{endDate:endDate});
				  }
				  //获取日期格式
				  var dateFormat = $this.data("date-format");
				  if(utils.isNotEmpty(dateFormat)) {
					  setting = $.extend(setting,{format:dateFormat});
				  }
				  var pickerHeight = 230;
				  var offset = $(this).offset();
				  var windowHeight = $(window).height();
				  if((offset.top+pickerHeight)>windowHeight) {
					  setting = $.extend(true, setting,{pickerPosition:'top-right'});
				  }
				  setting = $.extend(true, setting,{language:'zh-CN',autoclose:true});
				  $this.datetimepicker(setting).on('show',function(){
					  $this.prop("readonly",true);
				  }).on('hide',function(){
					  $this.prop("readonly",false);
				  });
				  if(utils.isContain($this.attr("class"), "cnoj-datetime")) {
					  $this.addClass("cnoj-datetime-listener");
				  } else if(utils.isContain($this.attr("class"), "cnoj-date")) {
					  $this.addClass("cnoj-date-listener");
				  } else if(utils.isContain($this.attr("class"), "cnoj-time")) {
					  $this.addClass("cnoj-time-listener");
				  }
			  }
		  }
		  //$this = null;
	  });
}

/**
 * 下拉列表监听
 * 标识
 *   class="cnoj-select"
 *  参数
 *    必须
 *       data-uri 提供数据uri(数据来源)；数据格式为json数组格式;第一个字段为ID;第二个字段为名称
 *    可选
 *       data-default-value  默认值
 *       data-is-null 是否有空值（如：请选择）默认为:no(没有) 可选的值为:"yes"或"no"
 */
function selectListener() {
	$(".cnoj-select").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-select-listener")) {
			$this.addClass("cnoj-select-listener");
			var uri = $this.data("uri");
			var isNull = $this.data("is-null");
			isNull = (isNull == 'yes'?true:false);
			var defaultValue = $this.data("default-value");
			if(isNull) {
				$this.append("<option value=''>请选择</option>");
			}
			if(!utils.isEmpty(uri)) {
				utils.selectItem(this, uri, defaultValue);
			}
		}
		//$this = null;
	});
}

/**
 * 复选框监听
 * 标识
 *   class="cnoj-checkbox"
 *  参数
 *    必须
 *       data-uri 提供数据uri(数据来源)；数据格式为json数组格式;第一个字段为ID;第二个字段为名称
 *       data-name 复选框名称
 *    可选
 *       data-default-value  默认值
 *       data-is-horizontal 是否横排;默认为：yes；可设置的值为:"yes"或"no" <br />
 *       data-require 是否必填
 */
function inputCheckboxListener() {
	$(".cnoj-checkbox").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-checkbox-listener")) {
			$this.addClass("cnoj-checkbox-listener");
			var uri = $this.data("uri");
			var defaultValue = $this.data("default-value");
			var isH = $this.data("is-horizontal");
			isH = (isH == 'no'?false:true);
			var require = $this.data("require");
			var name = $this.data("name");
			name = utils.handleNull(name);
			if(!utils.isEmpty(uri)) {
				utils.checkboxItem(this, uri, defaultValue, name, isH, require, null);
			}
		}
	});
}

/**
 * 单选框监听
 * 标识
 *   class="cnoj-radio"
 *  参数
 *    必须
 *       data-uri 提供数据uri(数据来源)；数据格式为json数组格式;第一个字段为ID;第二个字段为名称
 *       data-name 单选框名称
 *    可选
 *       data-is-horizontal 是否横排;默认为：yes；可设置的值为:"yes"或"no" <br />
 *       data-default-value  默认值
 *       data-require 是否必填
 */
function inputRadioListener() {
	$(".cnoj-radio").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-radio-listener")) {
			$this.addClass("cnoj-radio-listener");
			var uri = $this.data("uri");
			var defaultValue = $this.data("default-value");
			var isH = $this.data("is-horizontal");
			isH = (isH == 'no'?false:true);
			var require = $this.data("require");
			var name = $this.data("name");
			name = utils.handleNull(name);
			if(!utils.isEmpty(uri)) {
				utils.radioItem(this, uri, defaultValue, name,isH, require, null);
			}
		}
	});
}


/**
 * 级联下拉列表监听
 * 标识
 *   class="cnoj-cascade-select"
 *  参数
 *    必须
 *       data-uri 提供数据uri(数据来源)；数据格式为json数组格式;第一个字段为ID;第二个字段为名称
 *       data-cascade-id 级联的ID值
 *    可选
 *       data-default-value  默认值
 *       data-param-name 参数名称
 *       data-change-id 父级的数据改变时，要获取值的ID；如果data-cascade-id和data-change-id为同一个ID时，data-change-id属性可以不设置
 */
function cascadeSelectListener() {
	$(".cnoj-cascade-select").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-cascade-select-listener")) {
			$this.addClass("cnoj-cascade-select-listener");
			var uri = $this.data("uri");
			var defaultValue = $this.data("default-value");
			var cascadeId = $this.data("cascade-id");
			var paramName = $this.data("param-name");
			var changeId = $this.data("change-id");
			changeId = utils.isEmpty(changeId)?null:changeId;
			if(!utils.isEmpty(uri) && !utils.isEmpty(cascadeId)) {
				utils.selectCascadeItem(this,cascadeId, uri,paramName, defaultValue,changeId);
			}
		}
		//$this = null;
	});
}


/**
 * 输入表单实现下拉列表 <br />
 * 标识 <br />
 *    class="cnoj-input-select" <br />
 *  参数  <br />
 *     必须 <br />
 *       data-uri 提供数据uri(数据来源)；数据格式为json数组格式;第一个字段为ID;第二个字段为名称 <br />
 *     可选 <br />
 *       data-is-show-all 是否显示"全部链接";默认为yes,可设置的值为:"yes"或"no" <br />
 *       data-param-name 搜索的参数名 <br />
 *       data-is-show-none 是否显示“无”;默认为：“no”；可设置的值为:"yes"或"no" <br />
 *       data-default-value 默认值
 */
function inputSelectListener() {
	$(".cnoj-input-select").each(function() {
		var $this= $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-input-select-listener")) {
			$(this).addClass("cnoj-input-select-listener");
			var uri = $this.data("uri");
			if(!utils.isEmpty(uri)) {
				var paramName = $this.data("param-name");
				var defaultValue = $this.data("default-value");
				if(utils.isEmpty(defaultValue))
					defaultValue = $this.val();
				var isShowAll = $(this).data("is-show-all");
				isShowAll = (isShowAll == 'yes'?true:false);
				var isShowNone = $(this).data("is-show-none");
				isShowNone = (isShowNone == 'yes'?true:false);
				var setting = {uri:uri,isShow:false,isShowAll:isShowAll,isShowNone:isShowNone};
				if(!utils.isEmpty(paramName)) 
					setting = $.extend(setting,{paramName:paramName});
				if(!utils.isEmpty(defaultValue)) 
					setting = $.extend(setting,{defaultValue:defaultValue});
				$this.inputSelect(setting);
		   }
			$(this).on("click focus",function(event){
				$(this).prop("readonly",true);
				if(!utils.isEmpty(uri)) {
					var paramName = $(this).data("param-name");
					var defaultValue = $(this).data("default-value");
					var isShowAll = $(this).data("is-show-all");
					isShowAll = (isShowAll == 'yes'?true:false);
					var setting = {uri:uri,isShow:true};
					if(!utils.isEmpty(paramName)) 
						setting = $.extend(setting,{paramName:paramName});
					if(!utils.isEmpty(defaultValue)) 
						setting = $.extend(setting,{defaultValue:defaultValue});
					$(this).inputSelect(setting);
				}
				event.stopPropagation();
			});
		}
		//$this = null;
	});
}

/**
 * 输入表单实现下拉列表 <br />
 * 标识 <br />
 *    class="cnoj-input-select-relate" <br />
 *  参数  <br />
 *     必须 <br />
 *       data-uri 提供数据uri(数据来源)；数据格式为json数组格式;第一个字段为ID;第二个字段为名称 <br />
 *     可选 <br />
 *       data-is-show-all 是否显示"全部链接";默认为yes,可设置的值为:"yes"或"no" <br />
 *       data-param-name 搜索的参数名 <br />
 *       data-is-show-none 是否显示“无”;默认为：“no”；可设置的值为:"yes"或"no" <br />
 *       data-default-value 默认值
 */
function inputSelectRelateListener() {
	$(".cnoj-input-select-relate").each(function() {
		var $this= $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-input-select-relate-listener")) {
			$(this).addClass("cnoj-input-select-relate-listener");
			
			var uri = $this.data("uri");
			if(!utils.isEmpty(uri)) {
				var paramName = $this.data("param-name");
				var defaultValue = $this.data("default-value");
				if(utils.isEmpty(defaultValue))
					defaultValue = $this.val();
				var isShowAll = $this.data("is-show-all");
				isShowAll = (isShowAll == 'yes'?true:false);
				var isShowNone = $this.data("is-show-none");
				isShowNone = (isShowNone == 'yes'?true:false);
				var setting = {uri:uri,isShow:false,isShowAll:isShowAll,isShowNone:isShowNone,
						selectCallback:function(obj,datas){
							selectRelate(datas, $this,2);
				}};
				if(!utils.isEmpty(paramName)) 
					setting = $.extend(setting,{paramName:paramName});
				if(!utils.isEmpty(defaultValue)) 
					setting = $.extend(setting,{defaultValue:defaultValue});
				$this.inputSelect(setting);
		    }
			
			$(this).on("click focus",function(event){
				$(this).prop("readonly",true);
				/*var uri = $this.data("uri");
				if(!utils.isEmpty(uri)) {
					var paramName = $this.data("param-name");
					var defaultValue = $this.data("default-value");
					if(utils.isEmpty(defaultValue))
						defaultValue = $this.val();
					var isShowAll = $(this).data("is-show-all");
					isShowAll = (isShowAll == 'yes'?true:false);
					var isShowNone = $(this).data("is-show-none");
					isShowNone = (isShowNone == 'yes'?true:false);
					var setting = {uri:uri,isShow:true,isShowAll:isShowAll,isShowNone:isShowNone,
							selectCallback:function(obj,datas){
								selectRelate(datas, $this,2);
					}};
					if(!utils.isEmpty(paramName)) 
						setting = $.extend(setting,{paramName:paramName});
					if(!utils.isEmpty(defaultValue)) 
						setting = $.extend(setting,{defaultValue:defaultValue});
					$this.inputSelect(setting);
			   }*/
			   $this.inputSelect({isShow:true});
			   event.stopPropagation();
			});
		}
		//$this = null;
		
	});
}

/**
 * 输入框自动完成监听 <br />
 * 标识 <br /> 
 *   class="cnoj-auto-complete" <br />
 *   参数  <br />
 *     必须 <br />
 *       data-uri 提供数据uri(数据来源)；数据格式为json数组格式;第一个字段为ID;第二个字段为名称 <br />
 *     或data-json-data 提供数据uri(数据来源)；数据格式为json数组格式;第一个字段为ID;第二个字段为名称;第三个为显示内容; <br />
 *       “data-uri”属性的优先级高于“data-json-data”属性; <br />
 *     可选 <br />
 *       data-multiple 是否输入多个输入值;1--是；0--否，默认为：0 <br />
 *       data-multiple-separator 多个值时，多个值之间的分隔符；默认为英文分号:";"；<br />
 *       “data-multiple”与“data-multiple-separator”成对出现 
 */
function autoCompleteListener() {
	//$(".cnoj-auto-complete").autocomplete("destroy");
	$(".cnoj-auto-complete").each(function() {
		var $this= $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-auto-complete-listener")) {
			$this.addClass("cnoj-auto-complete-listener");
			var uri = $this.data("uri");
			var jsonData = $this.data("json-data");
			var multiple = $this.data("multiple");
			var multipleSeparator = $this.data("multiple-separator");
			var options = {};
			var is = false;
			if(!utils.isEmpty(uri)) {
				options=$.extend(options,{uri:uri});
				is = true;
			}
			if(!is && !utils.isEmpty(jsonData)) {
				options=$.extend(options,{jsonData:jsonData});
				is = true;
			}
			if(is && !utils.isEmpty(multiple) && (multiple=='1' || multiple=='0')) 
				options=$.extend(options,{multiple:(multiple==1?true:false)});
			if(is && !utils.isEmpty(multiple) && multiple=='1' && !utils.isEmpty(multipleSeparator)) 
				options=$.extend(options,{multipleSeparator:multipleSeparator});
			if(is)
				$this.autoComplete(options);
				
		}
		//$this = null;
	});
}

/**
 * 输入框自动完成关联监听 <br />
 * 标识 <br /> 
 *   class="cnoj-auto-complete-relate" <br />
 *   参数  <br />
 *     必须 <br />
 *       data-uri 提供数据uri(数据来源)；数据格式为json数组格式;第一个字段为ID;第二个字段为名称 <br />
 *     或data-json-data 提供数据uri(数据来源)；数据格式为json数组格式;第一个字段为ID;第二个字段为名称;第三个为显示内容; <br />
 *       “data-uri”属性的优先级高于“data-json-data”属性; <br />
 */
function autoCompleteRelateListener() {
	//$(".cnoj-auto-complete").autocomplete("destroy");
	$(".cnoj-auto-complete-relate").each(function() {
		var $this= $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-auto-complete-relate-listener")) {
			$this.addClass("cnoj-auto-complete-relate-listener");
			var uri = $this.data("uri");
			var jsonData = $this.data("json-data");
			var options = {
					selectCallback:function(event,data){
						 if(utils.isNotEmpty(data)) {
							 var otherValue = data.item.otherValue;
							 selectRelate(otherValue, $this, 0);
							 /*
							 if(utils.isNotEmpty(otherValue)) {
								 var len = otherValue.length;
								 var $parentDiv = $this.parents("td:eq(0)");
								 var isTd = true;
								 if(utils.isEmpty($parentDiv.html())) {
									 isTd = false;
									 $parentDiv = $this.parents(".form-group:eq(0)").parent();
								 }
								 for (var i = 0; i < len; i++) {
									 var $input = $parentDiv.next().find(".form-control");
									 if(utils.isNotEmpty($input.attr("class"))) {
										 $input.val(otherValue[i]);
									 } else {
										
										 $input = $parentDiv.next().next().find(".form-control");
										 if(utils.isNotEmpty($input.attr("class"))) { 
											 $input.val(otherValue[i]);
										 } else {
											 break;
										 }
									 }
									 if(isTd) {
										 $parentDiv = $input.parents("td:eq(0)");
									 } else {
										 $parentDiv = $input.parents(".form-group:eq(0)").parent();
									 }
								}
							 }*/
						 }
					 }
			};
			var is = false;
			if(!utils.isEmpty(uri)) {
				options=$.extend(options,{uri:uri});
				is = true;
			}
			if(!is && !utils.isEmpty(jsonData)) {
				options=$.extend(options,{jsonData:jsonData});
				is = true;
			}
			if(is)
				$this.autoComplete(options);
		}
		//$this = null;
	});
}

/**
 * 微调数据 数据类型为number <br />
 * 标识 <br />
 *   class="cnoj-num-spinner" <br />
 *   参数  <br />
 *     可选 <br />
 *       data-min 最小值 <br />
 *       data-max 最大值 <br />
 *       data-step 每一次变化数值
 */
function spinnerNumListener() {
	$(".cnoj-num-spinner").each(function() {
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-num-spinner-listener")) {
			$this.addClass("cnoj-num-spinner-listener");
			var min = $this.data("min");
			min = utils.isEmpty(min)?0:(utils.regexNum(min)?min:0);
			var max = $this.data("max");
			max = utils.isEmpty(max)?0:(utils.regexNum(max)?max:0);
			var step = $this.data("step");
			step = utils.isEmpty(step)?1:(utils.regexNum(step)?step:1);
			var options = {min:min,step:step,numberFormat:"n",change: function(event, ui ) {
				var value = $(event.target).val();
				if(!utils.regexNum(value) || value<min || (max>0 && value>max)) {
					utils.showMsg("输入的数据格式错误！");
					$this.focus();
				}
			}};
			if(max>0) {
				options = $.extend(options,{max:max});
			}
			$(this).spinner(options);
		}
	});
}


/**
 * URL载入标记监听 <br />
 * 标识 <br />
 *   class="cnoj-load-url" <br />
 * 参数  <br />
 *   必须 <br />
 *    data-uri 提供数据uri
 */
function loadUrlListener() {
	$(".cnoj-load-url").each(function() {
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-load-url-listener")) {
			$this.addClass("cnoj-load-url-listener");
			var uri = $this.data("uri");
			if(!utils.isEmpty(uri)) {
				$this.append('<div class="cnoj-loading"><i class="fa fa-spinner fa-spin fa-lg"></i> 正在加载，请稍候...</div>');
				$this.load(uri,function() {
					$this.find(".cnoj-loading").remove();
					initEvent();
					loadUrlListener();
				});
			}
			return false;
		}
	});
}


/**
 * 处理回车提交表单功能
 */
function handleEntrySubmit() {
	$("input").keydown(function(e){
		if(e.keyCode == 13) {
			var $form = $(this).parents("form:eq(0)");
			var $btn = $form.find(".cnoj-search-submit,.cnoj-data-submit,.login-btn");
			if(!utils.isEmpty($btn.attr("class"))) {
				$btn.trigger("click");
				return false;
			}
		}
	});
}

/**
 * 数据提交监听
 * class="cnoj-post-data" 该标识主要是用来标post提交数据，点击标该class的按钮或链接时触发
 *   参数:必须 data-uri 分页uri
 *            data-refresh-uri刷新URI
 *       可选 data-target 显示地方(一般为一个div层)
 *           data-param 参数
 *           data-del-alert 删除后提醒信息
 */
function submitDataListener() {
	$(".cnoj-post-data").click(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-post-data-listener")) {
			$this.addClass("cnoj-post-data-listener");
			var uri = $(this).data("uri");
			var params = $(this).data("param");
			params = utils.isEmpty(params)?"":params;
			var refreshUri = $(this).data("refresh-uri");
			var target = $(this).data("target");
			var delAlertMsg = $(this).data("del-alert");
			if(!utils.isEmpty(uri)) {
				if(!utils.isEmpty(delAlertMsg)) {
					BootstrapDialogUtil.confirmDialog(delAlertMsg,function(){
						cnoj.postData(uri,params,target,refreshUri);
					});
					return false;
				}
				cnoj.postData(uri,params,target,refreshUri);
			}
		}
		return false;
	});
}

/**
 * 提交表单监听（含有附件时使用）
 * 标识
 * class="cnoj-form-submit"
 * 该标识是提交表单触发的事件
 * 除了action参数在form表单里面填写之外，其他的参数都在该标识元素中填写
 * 参数
 *   必须
 *      action 该参数就是form表单的action属性,在提交的form的action里面填写该值,指定提交的路径(action)
 *   可选
 *      data-target-form 该参数，指定提交哪个form表单的ID（有多个form表单时）;如:target-form="#add-form";
 *      data-fun 该参数是一个回调函数，提交成功后执行的方法;
 *      data-refresh-uri 该参数指定数据指定成功后，刷新的uri;
 *      data-show-target 该参数为form表单的data-show-target属性；提交数据请求之后，返回的内容显示到哪个里面，默认为"#main-content"
 *      与data-refresh-uri成对出现.
 *      
 */
function submitFormListener() {
	$(".cnoj-form-submit").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-form-submit-listener")) {
			$this.addClass("cnoj-form-submit-listener");
			$this.click(function(event){
				var target = $(this).data("target-form");
				var fun = $(this).data("fun");
				var $form = null;
				if(!utils.isEmpty(target)) {
					$form = $(target);
					if(typeof($form.attr("id")) === 'undefined') {
						$form = null;
						//console.error("data-target-form 必须指定form的id");
					}
				} else {
					 $form = $(this).parents("form:eq(0)");
				}
				if($form.validateForm()) {
					var param = $form.serialize();
				    var uri = $form.attr("action");
				    cnoj.submitFormDialogData(uri,param,fun,$(this),$form);
				}
				event.stopPropagation();
				return false;
			});
		}
		//$this = null;
	});
}

/**
 * 跳转到指定的页面<br />
 * 标识 <br />
 *   class="cnoj-goto-page" <br />
 *   参数  <br />
 *     必须 <br />
 *        data-uri 跳转到的页面 <br />
 *     可选 <br />
 *       data-target 跳转内容显示位置 <br />
 *       data-search-panel-tag 搜索面板标识
 */
function gotoPageListener() {
	$(".cnoj-goto-page").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-goto-page-listener")) {
			$this.addClass("cnoj-goto-page-listener");
			$this.click(function(event) {
				var uri = $(this).data("uri");
				var page = $(this).prev().val();
				if (!utils.isEmpty(uri) && utils.isNotEmpty(page) && utils.regexInteger(page)) {
					var $btnPage = $(this).parents(".btn-page:eq(0)");
					var $pageInfo = $btnPage.next().find("span:first");
					var pageInfoText = $pageInfo.text();
					if(utils.isNotEmpty(pageInfoText)) {
						var reg = new RegExp("(\\d+) - (\\d+)","gmi")
						pageInfoText = pageInfoText.replace(reg, "$2");
					} else {
						pageInfoText = 1;
					}
					var totalPage = parseInt(pageInfoText);
					page = (page > totalPage)?totalPage:page;
					var target = $(this).data("target");
					uri = uri+page;
					
					//获取搜索参数
					var searchPanelTag = $(this).data("search-panel-tag");
					var $searchPanel = null;
					if(utils.isEmpty(searchPanelTag)) {
						$searchPanel = $(this).parents(".panel:eq(0)").find(">.panel-search");
					} else {
						$searchPanel = $(searchPanelTag);
					}
					if(utils.isExist($searchPanel)) {
						var $form = $searchPanel.find("form");
						if(utils.isExist($form)) {
							uri = uri+"&"+$form.serialize();
						}
					}
					
					if(!utils.isEmpty(target))
						loadUri(target,uri,true);
					else 
						loadActivePanel(uri);
						//loadLocation(uri);
				}
				event.stopPropagation();
				return false;
			});
		}
		//$this = null;
	});
}

/**
 * 打印标识监听<br />
 * 标识 <br />
 *   class="cnoj-print" <br />
 *   参数  <br />
 *     必须 <br />
 *        data-target 打印目标对象 <br />
 */
function printListener() {
	$(".cnoj-print").each(function(){
		var $this = $(this);
		if(!utils.isContain($this.attr("class"), "cnoj-print-listener")) {
			$this.addClass("cnoj-print-listener");
			$this.click(function(event) {
				var target = $(this).data("target");
				if (utils.isNotEmpty(target)) {
					utils.handleFormPrintLabel(target);
					$(target).printArea();
				}
				event.stopPropagation();
				return false;
			});
		}
	});
}

/**
 * popover标识监听<br />
 * 标识 <br />
 *   class="mix-popover" <br />
 *   参数  <br />
 *     必须 <br />
 *        data-uri 打印目标对象 <br />
 */
function popoverListener() {
	$(".mix-popover").each(function(){
		var $this = $(this);
		if(!$this.hasClass("cnoj-popover-listener")) {
			$this.addClass("cnoj-popover-listener");
			var id = $this.attr("id");
			var uri = $this.data("uri");
			var content = $this.data("content");
			$this.click(function(){
				if(utils.isNotEmpty(uri)) {
					$.ajax({type:"get",url:uri,async:false,success:function(data){
						content = data;
					}});
				} 
				if(utils.isNotEmpty(content)){
					$this.popover({
						placement:'auto',
						content:content,
						trigger:"manual",
						html:true
					});
					$this.popover('show');
				}
				$(document).click(function(event){
					if ($(event.target).closest('.popover').length === 0) {
						$this.popover('hide');
					}
				});
				return false;
			});
		}
	});
}

/**
 * 富文本编辑器监听<br />
 * 标识 <br />
 *   class="cnoj-richtext" <br />
 *   参数  <br />
 *     无
 */
function richtextListener() {
	if(typeof(UE) != 'undefined') {
		if(UEDITOR_CONFIG.UEDITOR_HOME_URL.indexOf("/plugins/ueditor/")<0) {
			UEDITOR_CONFIG.UEDITOR_HOME_URL = UEDITOR_CONFIG.UEDITOR_HOME_URL+"plugins/ueditor/";
		}
		$(".cnoj-richtext").each(function(){
			var $this = $(this);
			if(!utils.isContain($this.attr("class"), "cnoj-richtext-listener")) {
				$this.addClass("cnoj-richtext-listener");
				var id = $this.attr("id");
				UE.delEditor(id);
				//$this.width($this.width());
				UE.getEditor(id,{
					toolbars: [[
					            'fullscreen',
					            'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'blockquote', '|', 'forecolor', 'insertorderedlist', 'insertunorderedlist', '|',
					            'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
					            'paragraph', 'fontfamily', 'fontsize', '|', 'indent', '|',
					            'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|',
					            'link', 'unlink', 'anchor', '|','pagebreak','|',
					            'horizontal', 'date', 'time', 'spechars', '|',
					            'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts'
					        ]]
				});
			}
		});
	}
}

/**
 * 初始化
 */
function initEvent() {
	formRequireListener();
	$(".cnoj-op-btn-list").btnListener();
	
	submitDataListener();
	submitFormListener();
	checkboxAllListener();
	checkboxListener();
	tableTreeListener();
	tableTreeSelectListener();
	tableSelectListener();
	
	tableAsyncTreeListener();
	tableAsyncTreeSelectListener();
	
	panelTreeListener();
	panelCheckTreeListener();
	panelOrgTreeListener();
	submitBtnListener();
	searchSubmitListener();
	limitHeightListener();
	tableWrapListener();
	
	hrefListener();
	gotoPageListener();
	uploadFileListener();
	selectListener();
	cascadeSelectListener();
	loadUrlListener();
	//handleEntrySubmit();
	spinnerNumListener();
	inputPluginEvent();
	printListener();
	popoverListener();
}

function inputPluginEvent() {
	inputTreeListener();
	inputOrgTreeListener();
	inputDateListener();
	inputSelectListener();
	inputSelectRelateListener();
	autoCompleteListener();
	autoCompleteRelateListener();
	inputCheckboxListener();
	inputRadioListener();
}

/**
 * 
 * @param obj
 * @param op
 * @param flag
 */
function openProp(obj,op,flag) {
	if(!utils.isEmpty(obj)) {
		var $obj = null;
		if(typeof(obj) === 'string') {
			$obj = $(obj);
		} else {
			$obj = obj;
		}
		var uri = $obj.data("uri");
		var title = $obj.data("title");
		var value = $obj.attr("selected-value");
		var busiName = $obj.data("busi");
		var paramName = $obj.data("param-name");
		var selectedType = $obj.data("selected-type");
		var beforeCheck = $obj.data("before-check");
		selectedType = utils.isEmpty(selectedType)?'none-selected':selectedType;
		var width = $obj.data("width");
		width = utils.regexNum(width)?width:600;
		paramName = utils.isEmpty(paramName)?'id':paramName;
		if(!utils.isEmpty(selectedType)) {
			if(selectedType=='one-selected') {
				if(!utils.isEmpty(value) && value.indexOf(',')>0){
					BootstrapDialogUtil.warningAlert("只能选择一条数据!");
					return;
				} else if(utils.isEmpty(value)) {
					BootstrapDialogUtil.warningAlert("请选择一条数据!");
					return;
				}
			} else if(selectedType== 'multi-selected') {
				if(utils.isEmpty(value)) {
					BootstrapDialogUtil.warningAlert("请选择数据!");
					return;
				}
			} else {
				value = utils.isEmpty(value)?"":value;
			}
			var params = '';
			if(!utils.isEmpty(value)) {
				params = paramName+"="+value;
			}
			if(!utils.isEmpty(busiName)) {
				params += "&busiName="+busiName;
				if(!utils.isEmpty(op)) {
					params = params+"&op="+op;
				}
			}
			var is = true;
			if(utils.isNotEmpty(beforeCheck)) {
				beforeCheck = eval(beforeCheck);
				if(typeof(beforeCheck) == 'function') {
					is = beforeCheck(paramName,value);
				}
			}
			if(!utils.isEmpty(uri) && is) {
				if(!utils.isEmpty(params)) {
					if(utils.isContain(uri, "?")) {
						uri = uri+"&"+params;
					} else {
						uri = uri+"?"+params;
					}
			    }
				if(!utils.isEmpty(flag) && flag == 'open-self') {
					//loadLocation(uri);
					openTab(title, uri,true);
				} else if(!utils.isEmpty(flag) && flag == 'open-new-tab') {
					//loadLocation(uri);
					openTab(title, uri,true);
				} else {
					BootstrapDialogUtil.loadUriDialog(title,uri,width,"#fff",false,function(){
						initEvent();
					});
				}
			}
		}
	}
}

/**
 * 选择关联
 * @param datas
 * @param $this
 * @param start
 */
function selectRelate(datas,$this,start) {
	if(utils.isNotEmpty(datas)) {
		 var otherValue = datas;
		 if(utils.isNotEmpty(otherValue)) {
			 var len = otherValue.length-start;
			 var $parentDiv = $this.parents("td:eq(0)");
			 var isTd = true;
			 if(utils.isEmpty($parentDiv.html())) {
				 isTd = false;
				 $parentDiv = $this.parents(".form-group:eq(0)").parent();
			 }
			 var $currentDiv = $parentDiv;
			 for (var i = 0; i < len; i++) {
				 $parentDiv = $currentDiv.next();
				 var $input = $parentDiv.find(".form-control");
				 if(utils.isNotEmpty($input.attr("class"))) {
					 $input.val(otherValue[i+start]);
					 $currentDiv = $parentDiv;
				 } else {
					 var isBr = false;
					 var $tmp = $parentDiv.next();
					 if(utils.isNotEmpty($tmp.attr("class")) || utils.isNotEmpty($tmp.html())) {
						 $currentDiv = $tmp;
						 $input = $currentDiv.find(".form-control");
						 if(utils.isNotEmpty($input.attr("class"))) { 
							 $input.val(otherValue[i+start]);
						 } else {
							 isBr = true;
						 }
					 } else {
						 isBr = true;
					 }
					 if(isBr) {
						 $currentDiv = findNextInput($currentDiv,otherValue[i+start]);
						 if(null == $currentDiv) {
							 break;
						 } 
					 }
				 }
				 if(isTd && !isBr) {
					 //console.log("执行表格");
					 $currentDiv = $input.parents("td:eq(0)");
				 } else {
					 //$currentDiv = $input.parents(".form-group:eq(0)").parent();
				 }
			}
		 }
	}
}

/**
 * 查询下个INPUT
 * @param $parentDiv
 * @param value
 * @returns
 */
function findNextInput($parentDiv,value){
	$parentDiv = $parentDiv.parent().next();
	var $input = hasInput($parentDiv);
	if(null != $input) {
		var $tmp = $parentDiv.children().eq(0);
		$input = hasInput($tmp);
		if(null != $input) {
			$input.val(value);
			$parentDiv = $tmp;
		} else {
			$parentDiv = $parentDiv.children().eq(1);
			$input = hasInput($parentDiv);
			if(null != $input) {
				$input.val(value);
			} else {
				$parentDiv = null;
			}
		}
	} else {
		$parentDiv = null;
	}
	return $parentDiv;
}
/**
 * 判断是否有input[class=form-control]
 * @param $div
 * @returns
 */
function hasInput($div) {
	var $input = $div.find(".form-control");
	var className = $input.attr("class");
	if(utils.isEmpty(className)) {
		$input = null;
	}
	return $input;
}