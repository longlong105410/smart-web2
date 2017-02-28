/**
 * 表单加载完之后执行该方法
 */
function handleForm() {
	var value = $("#row-U143736338786971047-1").val();
	if(utils.isNotEmpty(value)) {
		$("#row-U143736338786971047-1").parents("table").hide();
	}
	setTimeout(function() {
		dynamicRequireFormListener();
	}, 200);
	
}

/**
 * 添加动态添加行时触发该方法
 */
function formAddRow(index) {
	
}

/**
 * 动态必填项监听
 */
function dynamicRequireFormListener() {
	isRepeatTender();
	isProxyContractSign();
}
/**
 * 是否重新招标
 */
function isRepeatTender() {
	var $repeatTender = $("select[name=U144836153041379020]");
	//alert($repeatTender.attr("class"));
	if(!$repeatTender.prop("disabled")) {
		 var value = $repeatTender.val();
		 var $no = $("#U144836153041461734");
		 var $reson = $("#U144836153041498030");
		 if(value == '1') {
			 relateField($no,true);
			 relateField($reson,true);
		 }
		 $repeatTender.change(function(){
			 value = $(this).val();
			 $no = $("#U144836153041461734");
			 $reson = $("#U144836153041498030");
			 if(value == '1') {
				 relateField($no,true);
				 relateField($reson,true);
			 } else {
				 relateField($no,false);
				 relateField($reson,false);
			 }
		 });
	 }
}

/**
 * 代理合同是否签订
 */
function isProxyContractSign() {
	var $proxyContractSign = $("select[name=U143735857902360775]");
	if(!$proxyContractSign.prop("disabled")) {
		var value = $proxyContractSign.val();
		var $contractName = $("#U143737872233258977");
		var $contractRemark = $("#U144791993596734146");
		if(value == '1') {
			 relateField($contractName,true);
			 relateField($contractRemark,false);
		} else if(value == '0') {
			relateField($contractName,false);
			relateField($contractRemark,true);
		}
		$proxyContractSign.change(function(){
			 value = $(this).val();
			 $contractName = $("#U143737872233258977");
			 $contractRemark = $("#U144791993596734146");
			 if(value == '1') {
				 relateField($contractName,true);
				 relateField($contractRemark,false);
			 } else {
				 relateField($contractName,false);
				 relateField($contractRemark,true);
			 }
		 });
	}
}

/**
 * 关联字段
 * @param id
 * @param isRequire
 */
function relateField($element,isRequire) {
	if($element.attr("id") && !$element.prop("disabled")) {
		if(isRequire) {
			addRequireForm($element);
		} else {
			removeRequireForm($element);
		}
	}
}

/**
 * 添加必填
 * @param $element
 */
function addRequireForm($element) {
	$element.addClass("require");
	var id = $element.attr("id");
	id = utils.isEmpty(id)?$element.attr("name"):id;
	var newIdTag = id+"-"+"require";
	if(!$("#"+newIdTag).attr("id")) {
		$element.after("<span id='"+newIdTag+"' class='star-require'> * </span>");
	}
}
/**
 * 删除必填
 * @param $element
 */
function removeRequireForm($element) {
	$element.removeClass("require");
	var id = $element.attr("id");
	id = utils.isEmpty(id)?$element.attr("name"):id;
	var newIdTag = id+"-"+"require";
	$("#"+newIdTag).remove();
	//$element.css("border-color","#eee");
}
