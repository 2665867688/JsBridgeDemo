function call(){
		var value = document.getElementById("input").value;
		alert(value);
	}

	function onJsAlert(){
		alert("onJsAlert");
	}

	function onJsConfirm(){
		  var b = confirm("are you sure to login?");
          //alert("your choice is "+b);
	}

	function onJsPrompt(){
		<!--//prompt的执行 会调用webveiwchormclient 的onJsPrompt方法执行，b 是prompt执行后的返回结果，由onJsPrompt的JsPromptResult.confirm方法执行并返回结果-->
		var b = prompt("please input your password","我擦");
		//b:是prompt执行完的返回结果
        //alert("your input is "+b);
        document.getElementById("input").value = b;
	}
	function callJS(){
		alert("android 调用js");
	}