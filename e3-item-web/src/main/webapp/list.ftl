<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>获取list类型数据</title>
</head>
<body>
<h1>获取list类型数据:</h1>
<table border="1" style="width: 1200px;height: 300px;">
	<tr>
		<td>循环角标</td>
		<td>用户编号</td>
		<td>用户姓名</td>
		<td>用户年龄</td>
		<td>用户邮箱</td>
		<td>用户地址</td>
		<td>用户操作</td>
	</tr>
	<#list pList as p>
	<#if p_index%2==0>
		<tr style="background-color: blue;">
	<#else>
		<tr style="background-color: orange;">
	</#if>
	
		<td>${p_index}</td>
		<td>${p.id!}</td>
		<td>${p.name!}</td>
		<td>${p.age!}</td>
		<td>${p.email!}</td>
		<td>${p.address}</td>
		<td>
			<a href="">删除</a>
			<a href="">修改</a>
		</td>
	</tr>
	</#list>
</table>
</body>
</html>