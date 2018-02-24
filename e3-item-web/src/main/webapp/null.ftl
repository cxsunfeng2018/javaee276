<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>获取基本类型,将会进行null值处理</title>
</head>
<body>
<h1>获取基本类型,将会进行null值处理</h1>
<h1>?处理null值:</h1>
<h2>方式一:${name?default("默认值")}</h2>
<h2>方式二:${name?default("")}</h2>
<hr color="blue" size="2">


<h1>!处理null值:</h1>
<h2>方式一:${name!"默认值"}</h2>
<h2>方式二:${name!}</h2>
<hr color="blue" size="2">

<h1>if处理null值:</h1>
<h2>
	<#if name??>
		${name}
	</#if>
</h2>


</body>
</html>