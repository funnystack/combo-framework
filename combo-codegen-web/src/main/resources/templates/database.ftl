<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="shortcut icon" href="favicon.ico">
    <link href="/css/bootstrap.min14ed.css?v=3.3.6" rel="stylesheet">
    <link href="/css/font-awesome.min93e3.css?v=4.4.0" rel="stylesheet">

    <link href="/css/animate.min.css" rel="stylesheet">
    <link href="/css/style.min862f.css?v=4.1.0" rel="stylesheet">
    <title>Combo Code Factory</title>
    <style type="text/css">
        body {
            background-color: #ccc;
            line-height: 1.6;
            margin-left: 80px;
            margin-right: 80px;
        }
    </style>
</head>
<body class="gray-bg">

<div class="row border-bottom white-bg">
    <h1 style="margin-left: 40px">Combo Code Factory</h1>
</div>

<div class="row">
    <div class="ibox float-e-margins">
        <div class="ibox-title">
            <div class="row">
                <form class="form-horizontal" id="codeform" method="post" action="/getCode">
                    <input type="hidden" id="c_url" name="jdbcURL">
                    <input type="hidden" id="c_user" name="jdbcUserId">
                    <input type="hidden" id="c_pass" name="jdbcPassword">
                    <input type="hidden" id="c_table" name="tableNames">

                    <div class="row">
                        <div class="col-md-4 form-group">
                            <label class="col-md-3 control-label">数据库</label>
                            <div class="col-md-9">
                                <select class="form-control" id="data_base_select">
                                </select>
                            </div>
                        </div>

                        <div class="col-md-4 form-group">
                            <label class="col-sm-2 control-label">包名</label>
                            <div class="col-sm-10">
                                <input type="text" id="package_name" name="packageName" value="com.funny.combo"
                                       class="form-control">
                            </div>
                        </div>


                        <div class="col-md-4 form-group">
                            <label class="col-md-2 control-label">模块</label>
                            <div class="col-md-10">
                                <input type="text" id="module_name" name="moduleName" value="user"
                                       class="form-control">
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label class="col-md-2 control-label">Entity</label>
                            <div class="col-md-10">
                                <input type="text" id="entityName" name="entityName" class="form-control" value="com.funny.trade.user.entity.XXXX">
                            </div>
                        </div>
                        <div class="col-md-6 form-group">

                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label class="col-md-2 control-label">mapper</label>
                            <div class="col-md-10">
                                <input type="text" id="daoName" name="daoName" class="form-control" value="com.funny.trade.user.dao.XXXX">
                            </div>
                        </div>
                        <div class="col-md-6 form-group">

                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-1">
                        </div>
                        <div class="col-md-2">
                            <a class="btn btn-primary" id="tableBtn">获取TABLE</a>
                        </div>
                        <div class="col-md-2">
                            <a class="btn btn-info" id="codeBtn">生成代码</a>
                        </div>
                    </div>
                </form>
            </div>

        </div>

        <div class="ibox-content ">
            <div class="row">
                <div class="col-sm-8">
                    <table class="table table-bordered" id="label_table">
                        <tr>
                            <td class="width_50">序号</td>
                            <td class="width_140">表名</td>
                            <td class="width_140">备注</td>
                            <td class="width_140">主键</td>
                        </tr>
                    </table>
                </div>

                <div class="col-sm-4">
                    <a href="/download/BaseMapper.java"
                       title="查看BaseMapper" target="_blank">
                        查看BaseMapper
                    </a>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="/download/BaseEntity.java"
                       title="查看BaseEntity" target="_blank">
                        查看BaseEntity
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>


<script src="/js/jquery.min.js?v=2.1.4"></script>
<script src="/js/bootstrap.min.js?v=3.3.6"></script>
<script src="/js/content.min.js?v=1.0.0"></script>
<script src="/js/jquery.validate-1.13.1.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
        $.ajax({
                type: "get",
                url: "/getDatabaseList",
                dataType: "json",
                success: function(data) {
                    console.log(typeof (data.data));
                    var options = '<option value="0">请选择数据库</option>';
                    $.each(data.data,function(n,db) {
                        options+='<option data-url="'+
                                 db.url+'" data-username="'+
                                 db.username+'" data-password="'+
                                 db.password+'">'+ db.url +'</option>';

                    });
                    $("#data_base_select").append(options);
                    $("#data_base_select").change(function(){
                        var tpMaterial = $('#data_base_select').find("option:selected");
                        $("#c_url").val($(tpMaterial).data('url'));
                        $("#c_user").val($(tpMaterial).data('username'));
                        $("#c_pass").val($(tpMaterial).data('password'));
                    });
                }
        });

    });



    $("#tableBtn").click(function () {
        var url = $("#c_url").val();
        var pas = $("#c_pass").val();
        var usr = $("#c_user").val();
        $.post('/getTargetDatabaseTables', {url: url, usr: usr, pas: pas}, function (data) {
            $("#label_table").html('<tr><td>序号</td><td class="width_140">表名</td><td>备注</td><td>主键</td></tr>');
            if (data.code == 0) {
                $.each(data.data, function (i, n) {
                    $("#label_table").append('<tr><td><input type="checkbox" name="tableid" style="min-width:50px;" id="' + i + '" value="' + n.tableName + '"/></td><td class="width_140"><label>'
                        + n.tableName + '</label></td><td ><label>'
                        + n.tableDesc + '</label></td><td ><label>'
                        + n.key + '</label></td></tr>');
                });
            } else {
                alert(data.message);
                return;
            }

        });
    });

    $("#codeBtn").click(function () {
        var tablename = "";
        var selected_tables = $("input[type=checkbox]:checked");
        $(selected_tables).each(function () {
            if (tablename == "") {
                tablename = $(this).val();
            } else {
                tablename = tablename + "," + $(this).val();
            }
        })

        if (tablename == '' || tablename == null || tablename == 'undefined') {
            alert('请选择一张表');
            return false;
        }
        $("#c_table").val(tablename);
        $("#codeform").submit();
    });

    $("#package_name").keyup(function () {
        getNames($(this).val(), $("#module_name").val());
    });

    $("#module_name").keyup(function () {
        getNames($("#package_name").val(), $(this).val());
    });

    function getNames(package_name, module_name) {
        if (package_name != '' && package_name != null && package_name != 'undefined' &&
            module_name != '' && module_name != null && module_name != 'undefined') {
            $.post('/getNames', {
                packageName: package_name,
                moduleName: module_name
            }, function (data) {
                $("#entityName").val(data.entityName);
                $("#daoName").val(data.daoName);
            });
        }

    }
</script>
</body>
</html>