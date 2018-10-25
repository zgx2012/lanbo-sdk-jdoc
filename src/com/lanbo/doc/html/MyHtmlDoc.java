package com.lanbo.doc.html;

public class MyHtmlDoc implements IHtmlDoc {
	IHtmlHeader mHeader;
	IHtmlBody mBody;
	String mLang;
	
	public MyHtmlDoc(IHtmlHeader htmlHeader, IHtmlBody htmlBody) {
		this(htmlHeader, htmlBody, null);
	}
	
	public MyHtmlDoc(IHtmlHeader htmlHeader, IHtmlBody htmlBody, String lang) {
		mHeader = htmlHeader;
		mBody = htmlBody;
		mLang = lang;
	}

	@Override
	public String genHtml() {
		StringBuilder htmlBuilder = new StringBuilder();
		if ("zh".equals(mLang)) {
			htmlBuilder.append("<!DOCTYPE html><html lang=\"zh\">");
		} else {
			htmlBuilder.append("<!DOCTYPE html><html lang=\"en\">");
		}
		
		htmlBuilder.append(mHeader.genHeader()).append(mBody.genBody()).append("</html>");
		return htmlBuilder.toString();
	}

}
