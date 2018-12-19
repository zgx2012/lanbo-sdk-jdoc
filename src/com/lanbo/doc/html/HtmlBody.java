package com.lanbo.doc.html;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lanbo.doc.convert.Converter;
import com.lanbo.doc.info.TypeInfo;
import com.lanbo.util.DocUtil;
import com.lanbo.util.TextUtil;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Tag;
import com.sun.javadoc.Type;

public class HtmlBody extends HtmlBase implements IHtmlBody {
	String mPackName, mClazzName, mDesc, mNotice;
	String mLang;
	ClassDoc mClazzDoc;
	boolean mIsInternalApi;

	public HtmlBody(ClassDoc cDoc, boolean isInternalApi) {
		this(cDoc, isInternalApi, "en");
	}

	public HtmlBody(ClassDoc cDoc, Boolean isInternalApi, String lang) {
		mClazzDoc = cDoc;
		mIsInternalApi = isInternalApi;
		mLang = lang;

		mPackName = cDoc.containingPackage().name();
		mClazzName = cDoc.name();
		mDesc = genComment(cDoc);
		mNotice = "NOTICE: All  functions and members  not defined as bellow of this class are NOT part of any supported API.</br>"
				+ "If you write code that depends on this, you do so at your own risk.  This code and its internal interfaces are subject to change or deletion without notice.";
	}

	private String genComment(Doc doc) {
		StringBuilder desc = new StringBuilder();
		desc.append(doc.commentText()).append(
				TextUtil.isEmpty(doc.commentText()) ? "" : "</br>");

		Tag[] tags = doc.tags();
		for (int j = 0; j < tags.length; j++) {
			Tag tag = tags[j];
			if (!TextUtil.isEmpty(mLang) && !TextUtil.isEmpty(tag.name())
					&& mLang.equals(tag.name().substring(1))) {
				desc.append(tag.text()).append(
						TextUtil.isEmpty(tag.text()) ? "" : "</br>");
			}
		}

		tags = doc.tags(isZh() ? "param_zh" : "param_en");
		if (tags.length > 0) {
			desc.append("<h3>").append(isZh() ? "参数:" : "Params:")
					.append("</h3>");
			ParamObj paramObj = new ParamObj();
			for (int j = 0; j < tags.length; j++) {
				parseParam(tags[j].text(), paramObj);
				desc.append("<dt><b>" + paramObj.parameterName + "</b>");
				desc.append("<dd style='color:black'>"
						+ paramObj.parameterComment + "</dd>");
			}
		}

		tags = doc.tags(isZh() ? "return_zh" : "return_en");
		if (tags.length > 0) {
			desc.append("<h3>").append(isZh() ? "返回值:" : "Return:")
					.append("</h3>");
			for (int j = 0; j < tags.length; j++) {
				desc.append(tags[j].text()).append("<br/>");
			}
		}

		tags = doc.tags("device");
        if (tags.length > 0) {
            desc.append("<h3>").append(isZh() ? "支持的产品:" : "Supported Products:")
                    .append("</h3><p>");
            for (int j = 0; j < tags.length; j++) {
                desc.append(tags[j].text());
            }
            desc.append("</p>");
        }

		return desc.toString();
	}

	private static final Pattern typeParamRE = Pattern.compile("<([^<>]+)>");

	private static class ParamObj {
		private String parameterName;
		private String parameterComment;
	}

	private void parseParam(String text, ParamObj paramObj) {
		String[] sa = divideAtWhite(text);

		Matcher m = typeParamRE.matcher(sa[0]);
		boolean isTypeParameter = m.matches();
		paramObj.parameterName = isTypeParameter ? m.group(1) : sa[0];
		paramObj.parameterComment = sa[1];
	}

	String[] divideAtWhite(String text) {
		String[] sa = new String[2];
		int len = text.length();
		// if no white space found
		sa[0] = text;
		sa[1] = "";
		for (int inx = 0; inx < len; ++inx) {
			char ch = text.charAt(inx);
			if (Character.isWhitespace(ch)) {
				sa[0] = text.substring(0, inx);
				for (; inx < len; ++inx) {
					ch = text.charAt(inx);
					if (!Character.isWhitespace(ch)) {
						sa[1] = text.substring(inx, len);
						break;
					}
				}
				break;
			}
		}
		return sa;
	}

	/*
	 * <h2 class="packageName">com.team.bean</h2> <h1
	 * class="className">Test</h1> <p>测试类</p>
	 */
	@Override
	public String genBodyHeadBlock() {
		StringBuilder head = new StringBuilder();
		head.append("<h2 class=\"packageName\">").append(mPackName)
				.append("</h2>").append("<h1 class=\"className\">")
				.append(mClazzName).append("</h1>").append("<p>").append(mDesc)
				// .append(mNotice)
				.append("</p>");
		return head.toString();
	}

	/*
	 * <table frame="box" rules="all" cellpadding="10"> <col
	 * class=\"detailCol1\"/><col class=\"detailCol2\"/> <tr> <th>method</th>
	 * <th>description</th> </tr> </table>
	 */
	@Override
	public String genBodyMethodBlock() {
		StringBuilder tableBuilder = new StringBuilder();
		tableBuilder
				.append("<table class=\"detail\" frame=\"box\" rules=\"all\" cellpadding=\"10\">");
		tableBuilder
				.append("<col class=\"detailCol1\"/><col class=\"detailCol2\"/>");

		// 表头
		if (isZh()) {
			buildTableRowHead(tableBuilder, new String[] { "方法", "描述" });
		} else {
			buildTableRowHead(tableBuilder, new String[] { "method",
					"description" });
		}

		// 构造方法表数据
		ConstructorDoc[] constructorDocs = mClazzDoc.constructors();
		for (int i = 0; i < constructorDocs.length; i++) {
			ConstructorDoc doc = constructorDocs[i];
			if (DocUtil.isHideOrRemoved(doc))
				continue;
			if (!mIsInternalApi && !DocUtil.isApi(doc))
				continue;

			// 方法定义，不用 doc.toString(), 这个方法将会携带包名, 内容过长
			StringBuilder codeBuilder = new StringBuilder();
			buildMethodDefination(codeBuilder, doc, true);

			buildTableRowData(tableBuilder,
					new String[] { codeBuilder.toString(), genComment(doc) });
		}

		// 内部方法表数据
		MethodDoc[] methodDocs = mClazzDoc.methods();
		for (int i = 0; i < methodDocs.length; i++) {
			MethodDoc doc = methodDocs[i];
			if (DocUtil.isHideOrRemoved(doc))
				continue;
			if (!mIsInternalApi && !DocUtil.isApi(doc))
				continue;

			// 方法定义，不用methodDoc.toString(), 这个方法将会携带包名, 内容过长
			StringBuilder codeBuilder = new StringBuilder();
			buildMethodDefination(codeBuilder, doc, false);

			buildTableRowData(tableBuilder,
					new String[] { codeBuilder.toString(), genComment(doc) });
		}

		tableBuilder.append("</table>");
		return tableBuilder.toString();
	}

	private void buildMethodDefination(StringBuilder builder,
			ExecutableMemberDoc doc, boolean isConstructor) {
		buildCodeModifiers(builder, doc.modifiers());
		if (!isConstructor && doc instanceof MethodDoc) {
			MethodDoc methodDoc = (MethodDoc) doc;
			if (methodDoc.returnType() != null) {
				Type type = methodDoc.returnType();
				TypeInfo info = Converter.obtainType(type);
				StringBuilder sb = new StringBuilder(info.simpleTypeName());

				if ("List".equals(type.simpleTypeName())) {
					System.out.println("info ==============");
					System.out.println(type.isPrimitive());
					System.out.println(type.dimension());
					System.out.println(type.simpleTypeName());
					System.out.println(type.qualifiedTypeName());

					ClassDoc classDoc = type.asClassDoc();
					System.out.println(classDoc.toString());
					System.out.println(classDoc.simpleTypeName());

					ArrayList<TypeInfo> list = info.typeArguments();
					if (list != null && list.size() > 0) {
						sb.append("&lt;");
						int size = list.size();
						for (int i = 0; i < size; i++) {
							TypeInfo ti = list.get(i);
							sb.append(ti.simpleTypeName());
							if (i < size - 1) {
								sb.append(',');
							}
						}
					    sb.append("&gt;");
					}
					System.out.println(sb.toString());
				}

				/*
				 * if (type.asParameterizedType() != null) {
				 * type.asParameterizedType().typeArguments(); } else if (type
				 * instanceof ClassDoc) { ((ClassDoc) type).typeParameters(); }
				 * else if (type.asTypeVariable() != null) {
				 * type.asTypeVariable().bounds(); } else if
				 * (type.asWildcardType() != null) {
				 * type.asWildcardType().superBounds();
				 * type.asWildcardType().extendsBounds(); }
				 */

				buildCodeType(builder, sb.toString());
			}
		}
		buildCodeMethodName(builder, doc.name());

		builder.append('(');
		Parameter[] params = doc.parameters();
		for (int k = 0; k < params.length; k++) {
			Parameter param = params[k];
			if (k > 0) {
				builder.append(", ");
			}
			buildCodeType(builder, param.typeName());
			builder.append(param.name());
		}
		builder.append(");");
	}

	/*
	 * <table frame="box" rules="all" cellpadding="10"> </table>
	 */
	@Override
	public String genBodyMemberBlock() {
		StringBuilder memBlockBuilder = new StringBuilder();
		memBlockBuilder
				.append("<table class=\"detail\" frame=\"box\" rules=\"all\" cellpadding=\"10\">");
		memBlockBuilder
				.append("<col class=\"detailCol1\"/><col class=\"detailCol2\"/>");
		// 表头
		if (isZh()) {
			buildTableRowHead(memBlockBuilder, new String[] { "成员", "描述" });
		} else {
			buildTableRowHead(memBlockBuilder, new String[] { "Member",
					"description" });
		}

		// 表数据
		FieldDoc[] fieldDocs = mClazzDoc.fields();
		for (int j = 0; j < fieldDocs.length; j++) {
			FieldDoc doc = fieldDocs[j];
			if (DocUtil.isHideOrRemoved(doc))
				continue;
			if (!mIsInternalApi && !DocUtil.isApi(doc))
				continue;

			StringBuilder memBuilder = new StringBuilder();
			buildCodeModifiers(memBuilder, doc.modifiers());
			buildCodeType(memBuilder, doc.type().simpleTypeName());
			buildCodeMemberName(memBuilder, doc.name());

			buildTableRowData(memBlockBuilder,
					new String[] { memBuilder.toString(), genComment(doc) });
		}

		memBlockBuilder.append("</table>");
		return memBlockBuilder.toString();
	}

	@Override
	public String genBody() {
		StringBuilder body = new StringBuilder();
		body.append(genBodyHeadBlock()).append("<hr/>");
		if (isZh()) {
			body.append("<h1>详情</h1>").append("<h2>方法</h2>");
		} else {
			body.append("<h1>Detail</h1>").append("<h2>Method</h2>");
		}
		body.append(genBodyMethodBlock()).append("<br/>");
		if (isZh()) {
			body.append("<h2>成员和常量</h2>");
		} else {
			body.append("<h2>Member & Constant</h2>");
		}
		body.append(genBodyMemberBlock()).append("<br/>");
		return body.toString();
	}

	private boolean isZh() {
		return "zh".equals(mLang);
	}
}
