package com.lanbo.doc.html;

import java.util.ArrayList;

import com.lanbo.doc.bean.ClazzDetail;

public class MyAllClassHtmlDoc implements IHtmlAllClazz {

	ArrayList<ClazzDetail> mClazzDetails;
	public MyAllClassHtmlDoc(ArrayList<ClazzDetail> details) {
		mClazzDetails = details;
	}
	
	/*
<html lang="zh">
<head>
<meta charset="UTF-8">
<title>所有类</title>
<link rel="stylesheet" type="text/css" href="stylesheet.css" title="Style">
<script type="text/javascript" src="script.js"></script>
</head>
<body>
<h1 class="bar">所有类</h1>
<div class="indexContainer">
<ul>

<li><a href="com/team/bean/LogDO.html" title="com.team.bean中的类" target="classFrame">LogDO</a></li>
<li><a href="com/team/bean/ModuleMemberDO.html" title="com.team.bean中的类" target="classFrame">ModuleMemberDO</a></li>
<li><a href="com/team/bean/ModuleStatusDO.html" title="com.team.bean中的类" target="classFrame">ModuleStatusDO</a></li>
<li><a href="com/team/bean/ProjectModuleDO.html" title="com.team.bean中的类" target="classFrame">ProjectModuleDO</a></li>

</ul>
</div>
</body></html>

	 */
	@Override
	public String genAllClassHtml() {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<html>");
		htmlBuilder.append("<head><meta charset=\"UTF-8\">")
            .append("<meta http-equiv=\"Expires\" content=\"0\">")
            .append("<meta http-equiv=\"Pragma\" content=\"no-cache\">")
            .append("<meta http-equiv=\"Cache-control\" content=\"no-cache\">")
            .append("<meta http-equiv=\"Cache\" content=\"no-cache\">");
		htmlBuilder.append("<title>所有类</title></head>");
		htmlBuilder.append("<script type=\"text/javascript\" src=\"");
		htmlBuilder.append("script.js");
		htmlBuilder.append("\"></script>");
		htmlBuilder.append("<body><h1 class=\"bar\">All Classes</h1><div class=\"indexContainer\"><ul>");
		for (int i = 0; i < mClazzDetails.size(); i++) {
			ClazzDetail detail = mClazzDetails.get(i);
			htmlBuilder.append(genClassItem(detail));
		}
		//htmlBuilder.append("\">");
		htmlBuilder.append("</ul></div></body></html>");
		return htmlBuilder.toString();
	}
	
	/*
	<li><a href="com/team/bean/LogDO.html" title="com.team.bean中的类" target="classFrame">LogDO</a></li>
	 */
	private String genClassItem(ClazzDetail detail) {
		StringBuilder listItemBuilder = new StringBuilder();
		listItemBuilder.append("<li>");
		
		listItemBuilder.append("<a href=\"");
		listItemBuilder.append(detail.path);
		listItemBuilder.append("\" title=\"");
		listItemBuilder.append(detail.pack);
		listItemBuilder.append(" Classes\" target=\"classFrame\">");
		listItemBuilder.append(detail.name);
		listItemBuilder.append("</a>");
		
		listItemBuilder.append("</li>");
		return listItemBuilder.toString();
	}

}
