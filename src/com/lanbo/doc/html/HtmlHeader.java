package com.lanbo.doc.html;

import com.lanbo.util.TextUtil;
import com.sun.javadoc.ClassDoc;

public class HtmlHeader implements IHtmlHeader {
	String mTitle;
	String mCss;
	ClassDoc mClazzDoc;
	public HtmlHeader(ClassDoc cDoc) {
		this(cDoc, null);
	}
	
	public HtmlHeader(ClassDoc cDoc, String css) {
		mClazzDoc = cDoc;
		mCss = css;
		mTitle = cDoc.name();
	}

	/*
	   <head>
       <meta charset="UTF-8">
       <meta http-equiv="Expires" content="0">
       <meta http-equiv="Pragma" content="no-cache">
       <meta http-equiv="Cache-control" content="no-cache">
       <meta http-equiv="Cache" content="no-cache">
       <title>Test</title>
       <link rel="stylesheet" type="text/css" href="sdk_sample.css">
       </head>
	 */
	@Override
	public String genHeader() {
		StringBuilder headerBuilder = new StringBuilder();
		headerBuilder.append("<head>")
                     .append("<meta charset=\"UTF-8\">")
                     .append("<title>")
                     .append(mTitle)
                     .append("</title>");
		if (!TextUtil.isEmpty(mCss)) {
			headerBuilder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"");
			headerBuilder.append(mCss);
			headerBuilder.append("\">");
		}
        headerBuilder.append("</head>");
		return headerBuilder.toString();
	}

}
