<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
    String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/jquery-1.10.0.js"></script>
<script type="text/javascript" src="js/jquery.validate-1.13.1.js"></script>
<title>Autocode Factory</title>

<script type="text/javascript">
	$(document).ready(function () {
       	$("#connectionform").validate({
            rules: {
            	url: {
                    required: true
                },
                username: {
                    required: true
                },
                password: {
                    required: true
                }
            },
            messages: {
            	url: {
                    required: '请输入数据库地址'
                },
                username: {
                    required: '请输入用户名'
                },
                password: {
                    required: '请输入密码',
                }
            },
            highlight: function(element, errorClass, validClass) {
                $(element).addClass(errorClass).removeClass(validClass);
                $(element).fadeOut().fadeIn();
            },
            unhighlight: function(element, errorClass, validClass) {
                $(element).removeClass(errorClass).addClass(validClass);
            },
            submitHandler: function (form) {
                //console.log($(form).serialize());
            }
        });
		
		$("#codeform").validate({
            rules: {
            	packagename: {
                    required: true
                },
                modelname: {
                    required: true
                }
            },
            messages: {
            	packagename: {
                    required: '请输入packagename'
                },
                modelname: {
                    required: '请输入modelname'
                },
            },
            highlight: function(element, errorClass, validClass) {
                $(element).addClass(errorClass).removeClass(validClass);
                $(element).fadeOut().fadeIn();
            },
            unhighlight: function(element, errorClass, validClass) {
                $(element).removeClass(errorClass).addClass(validClass);
            },
            submitHandler: function (form) {
            	form.submit();
            }
        });

		$("#getTables").click(function(){
			var url = $("#url").val();
			var pas = $("#password").val();
			var usr = $("#username").val();
			$.post('<%=basePath%>getTargetDatabaseTables.do',{url:url,usr:usr,pas:pas},function(data){
				$("#label_table").html('<tr><td class="width_50">序号</td><td class="width_140">表名</td><td class="width_140">备注</td><td class="width_140">主键</td></tr>');
				if(data.success){
					$.each(data.result,function(i,n){
						$("#label_table").append('<tr><td class="width_50"><input type="radio" name="tableid" style="min-width:50px;" id="'+i +'" value="'+n.tableName+'"/></td><td class="width_140"><label>'
								+ n.tableName+'</label></td><td class="width_140"><label>'
								+ n.tableDesc +'</label></td><td class="width_140"><label>'
								+ n.key +'</label></td></tr>');
					});
				}else{
					alert(data.message);
					return;
				}
				
			});
		});
		
		$("#getcode").click(function(){
			var table= $("input[name=tableid]:checked");
        	var tablename= table.val();
			if(tablename == '' || tablename==null || tablename=='undefined'){
				alert('请选择一张表');
				return;
			}
			var keyname= $(table).parent().siblings();
			$("#c_table").val(tablename);
			$("#c_url").val($("#url").val());
			$("#c_user").val($("#username").val());
			$("#c_pass").val($("#password").val());
			$("#c_style").val($("input[name=codestyle]:checked").val());
			$("#c_id").val($(keyname[2]).children('label').text());
			$("#codeform").submit();
		});

		$("#packagename").keyup(function(){
			getNames($(this).val(), $("#modelname").val());
		});

		$("#modelname").keyup(function(){
			getNames($("#packagename").val(),$(this).val());
		});


    });

	function getNames(packagename,modelname){
		if(packagename!='' && packagename !=null && packagename != 'undefined' &&
			modelname!='' && modelname !=null && modelname != 'undefined'){
			$.post('<%=basePath%>getNames.do', {
				packagename : packagename,
				modelname : modelname
			}, function(data) {
				$("#modelName").text(data.modelName);
				$("#daoName").text(data.daoName);
			});
		}

	}
</script>
<style type="text/css">
body {
	background-color: #ccc;
	line-height: 1.6;
	margin-left: 80px;
}

input {
	font-size: 25px;
	line-height: 35px;
	border: 1px solid #999;
	min-width: 180px;
}

button {
	margin-top: 20px;
	font-size: 20px;
	padding: 5px;
}

label.error {
	margin-left: 10px;
	color: red;
}

.showname {
	font-size: 20px;
	font-weight: bold;
}

.showname span {
	color: red;
}

.tablediv {
	float: left;
	width: 60%;
}

.tablediv table {
	border-collapse: collapse;
	border: 1px solid #000;
	font-size: 13px;
}

.tablediv table tr td {
	border: 1px solid #000;
	margin: 5px;
}

.width_50 {
	width: 50px;
}

.width_140 {
	width: 200px;
}

</style>
</head>
<body>
	<h1>Autocode Factory</h1>
	<form id="connectionform">
		<label>db url:</label> <input type="text" id="url" name="url"
			value="jdbc:mysql://10.168.66.173:3306/erpmall" size="45">
		<input type="submit" value="获取TABLE" id="getTables" />
		<table>
			<tr>
				<td><label>username:</label></td>
				<td><input type="text" id="username" name="username"
					value="sellmall" size="10"></td>

				<td><label>password:</label></td>
				<td><input type="text" id="password" name="password"
					value="sellmall1234" size="10"></td>

			</tr>
		</table>
	</form>
	<form id="codeform" action="<%=basePath%>getCode.do" method="post">
		<table>
			<tr>
				<td><label>package:</label></td>
				<td><input type="text" id="packagename" name="packagename" value="cn.com.autohome"></td>
				<td><label>module:</label></td>
				<td><input type="text" id="modelname" name="modelname"></td>
				<td><input type="submit" value="获取CODE" id="getcode" /></td>
			</tr>
		</table>
		<input type="hidden" id="c_url" name="c_url"> 
		<input type="hidden" id="c_user" name="c_user"> 
		<input type="hidden" id="c_pass" name="c_pass"> 
		<input type="hidden" id="c_table" name="c_table">
		<input type="hidden" id="c_style" name="c_style">
		<input type="hidden" id="c_id" name="c_id">
	</form>

	<div>
		<input type="radio" name="codestyle" value="1" checked="checked" style="min-width: 30px;"><label class="showname">普通方式</label> 
		<br>
		<label class="showname">Model Class：<span id="modelName"></span></label>
		<br> <label class="showname">Dao Class：<span id="daoName"></span></label>
	</div>

	<div class="tablediv">
		<table id="label_table">
			<tr>
				<td class="width_50">序号</td>
				<td class="width_140">表名</td>
				<td class="width_140">备注</td>
				<td class="width_140">主键</td>
			</tr>
		</table>
	</div>

</body>
</html>