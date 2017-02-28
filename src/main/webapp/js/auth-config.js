/**
 * @author lmq
 * 权限配置JS
 */
$(document).ready(function(){
	setTimeout(function(){
		authConfigSubmitBtnListener();
	}, 500);
	
});

function authConfigSubmitBtnListener() {
	$(".cnoj-auth-config-submit").click(function(){
		var alertMsg = $(this).data("alert-msg");
		var refreshUri = $(this).data("refresh-uri");
		var refreshTarget = $(this).data("refresh-target");
		var configId = $(this).data("config-id");
		var submitUri = $(this).data("uri");
		if(!utils.isEmpty(alertMsg) && !utils.isEmpty(refreshUri) && 
				!utils.isEmpty(refreshTarget) && !utils.isEmpty(configId) && 
				!utils.isEmpty(submitUri)) {
			var values = cnoj.getCheckboxValues($(this).parents(".auth-config-dialog-list"),1);
			if(utils.isEmpty(values)) {
				utils.showMsg(alertMsg);
			} else {
				var param = "id="+configId+"&submitDatas="+values;
				cnoj.submitDialogData(submitUri,param,null,refreshUri,refreshTarget,false);
			}
		}
		return false;
	});
}
