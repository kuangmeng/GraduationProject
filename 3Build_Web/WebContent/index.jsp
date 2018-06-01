<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  String path = request.getContextPath();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script src="static/jquery-1.7.1.js" type="text/javascript"></script>
<!--   <script src="static/ajax.js" type="text/javascript"></script>
 -->
<base href="<%=basePath%>">
<title>中文分词测试</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link rel="icon" id="myid" href="static/avatar.png">
<link rel="apple-touch-icon" id="myid" href="static/avatar.png">
<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet"
	href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css"
	integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
	crossorigin="anonymous">
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script
	src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"
	integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
	crossorigin="anonymous"></script>


<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$("#temp").click(
										function() {
											$("#sentence").val("");
											$("#sentence")
													.val(
															"工信部女处长每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作。高锰酸钾，强氧化剂，紫红色晶体，可溶于水，遇乙醇即被还原。常用作消毒剂、水净化剂、氧化剂、漂白剂、毒气吸收剂、二氧化碳精制剂等。2018年3月17日，我们一起坐在正心215教室学习。");
										});
						$("#clear").click(
								function() {
									$("#sentence").val("");
									$("#result").empty();
								});
						$("#segment").click(
								function() {
									var k = $("#sentence").val();
									if(k == ''){
										alert("请填写内容！");
									}else{
										$("#result").empty();
										$("#result").append("请耐心等候……");
										htmlobj = $.ajax({
											url : "./rest/seg",
											async : false,
											type : 'post',
											data : $("#sentence").val(),
										});
										$("#result").empty();
										var arr = htmlobj.responseText.split("\n");
										for(var i=0; i<arr.length-1; i++){
											var word = arr[i].split(" ");
											for(var j= 0;j<word.length;j++){
												$("#result").append("<span class='label label-info'>"+word[j]+"</span>&nbsp;");
												if(j % 13 == 0  && j > 0){
													$("#result").append("<br><br>");
												}
											}
											$("#result").append("<hr><br><br>");
										};
									};
						});
						$("#postagging")
								.click(
										function() {
											var k = $("#sentence").val();
											if(k == ''){
												alert("请填写内容！");
											}else{
												$("#result").empty();
												$("#result").append("请耐心等候……");
												htmlobj = $
														.ajax({
															url : "./rest/postagging",
															async : false,
															type : 'post',
															data : $("#sentence")
																	.val(),
														});
												$("#result").empty();
												var arr = htmlobj.responseText.split("\n");
												for(var i=0; i<arr.length-1; i++){
													var word = arr[i].split(" ");
													for(var j= 0;j<word.length;j++){
														var pos = word[j].split("_");
														$("#result").append("<span class='label label-default'>"+pos[0]+"</span>");
														$("#result").append("<span class='label label-info'>"+pos[1]+"</span>&nbsp;");
														if(j % 10 == 0  && j > 0){
															$("#result").append("<br><br>");
														}
													}
													$("#result").append("<hr><br><br>");
												};
												
											};
											
										});
						$("#ner")
						.click(
								function() {
									var k = $("#sentence").val();
									if(k == ''){
										alert("请填写内容！");
									}else{
										$("#result").empty();
										$("#result").append("请耐心等候……");
										htmlobj = $
												.ajax({
													url : "./rest/ner",
													async : false,
													type : 'post',
													data : $("#sentence")
															.val(),
												});
										$("#result").empty();
										var arr = htmlobj.responseText.split(" ");
										if(arr != "" ){
											for(var i=0; i<arr.length; i++){
												var word = arr[i].split("/");
													$("#result").append("<span class='label label-default'>"+word[0]+"</span>");
													$("#result").append("<span class='label label-info'>"+word[1]+"</span>&nbsp;");
													if(i % 10 == 0  && i > 0){
														$("#result").append("<br><br>");
													}
											};
										}else{
											$("#result").append("<span class='label label-warning'>未找到命名实体！</span>");
										};
										
									};
									
								});
					});
	
</script>
  <script>
    var linkEle = document.getElementById("myid");
    var tmplink = linkEle.href;

    var tmptitle = document.title;
    document.addEventListener('visibilitychange', function() {
      var isHidden = document.hidden;
      if (isHidden) {
        document.title = '喔唷，崩溃啦！';
        linkEle.href = 'static/error.png';
      } else {
        document.title = tmptitle;
        linkEle.href = tmplink;

      }
    });
  </script>
</head>

<body>
	<h1 style="text-align: center;">中文分词测试</h1>
	<hr>
	<br>
	<div style="text-align: center;">
		<div class="form-group" style="width: 50%; margin: 0 auto;">
			<textarea class="form-control" rows="6" id="sentence"></textarea>
		</div>
		<br>
		<button type="button" id="temp" class="btn btn-success"
			style="margin-right: 20px;">加载实例</button>
		&nbsp;
		<div class="btn-group">
			<button type="button" id="segment" class="btn btn-primary">分词</button>
			&nbsp;
			<button type="button" id="postagging" class="btn btn-info">词性标注</button>
			&nbsp;
			<button type="button" id="ner" class="btn btn-warning">命名实体识别</button>
		</div>
		&nbsp;
		<button type="button" id="clear" class="btn btn-success"
			style="margin-left: 20px;">清空所有</button>
		<br>
		<hr>
			<h4 id="result" style="line-height:20px;">
			</h4>
	</div>
</body>
</html>
