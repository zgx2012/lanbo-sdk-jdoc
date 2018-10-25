package com.lanbo.doc.html;

public class MyHtmlIndexDoc implements IHtmlIndex {
	
	String mNavHtmlFile;
	public MyHtmlIndexDoc(String navHtmlFile) {
		mNavHtmlFile = navHtmlFile;
	}

	/*
	<html>
	  <frameset cols="20%,80%">
	    <frame src="/example/html/frame_a.html">
	    <frame src="/example/html/frame_b.html">
	  </frameset>
	</html>
	 */
	@Override
	public String genIndexHtml() {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<html><frameset cols=\"20%,80%\">");
		htmlBuilder.append("<frame src=\"");
		htmlBuilder.append(mNavHtmlFile);
		htmlBuilder.append("\" name=\"packageFrame\">");
		htmlBuilder.append("<frame src=\"");
		htmlBuilder.append(mNavHtmlFile);
		htmlBuilder.append("\" name=\"classFrame\">");
		htmlBuilder.append("</frameset></html>");
		return htmlBuilder.toString();
	}

}
