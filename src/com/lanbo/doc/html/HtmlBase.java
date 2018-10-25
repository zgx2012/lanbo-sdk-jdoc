package com.lanbo.doc.html;

public class HtmlBase {
	protected void buildCodeType(StringBuilder builder, String typeName) {
		builder.append("<code class=\"type\">").append(typeName).append("</code> ");
	}
	protected void buildCodeModifiers(StringBuilder builder, String modifiers) {
		builder.append("<code class=\"modifiers\">").append(modifiers).append("</code> ");
	}
	protected void buildCodeMethodName(StringBuilder builder, String methodName) {
		builder.append("<code class=\"methodName\">").append(methodName).append("</code>");
	}
	protected void buildCodeMemberName(StringBuilder builder, String memName) {
		builder.append("<code class=\"memberName\">").append(memName).append("</code>");
	}
	
	/*
    <tr>
        <th>head1</th><th>head2</th>
    </tr>
	 */
	protected void buildTableRowHead(StringBuilder builder, String[] headers) {
		builder.append("<tr>");
		for (int i = 0; i < headers.length; i++) {
			builder.append("<th>");
			builder.append(headers[i]);
			builder.append("</th>");
		}
		builder.append("</tr>");
	}
	
	/*
    <tr>
        <td>data1</td><td>data2</td>
    </tr>
	 */
	protected void buildTableRowData(StringBuilder builder, String[] data) {
		builder.append("<tr>");
		for (int i = 0; i < data.length; i++) {
			builder.append("<td>");
			builder.append(data[i]);
			builder.append("</td>");
		}
		builder.append("</tr>");
	}

}
