package com.lanbo.doc;

import java.util.ArrayList;
import java.util.Comparator;

import com.lanbo.doc.bean.ClazzDetail;
import com.lanbo.doc.html.HtmlBody;
import com.lanbo.doc.html.HtmlHeader;
import com.lanbo.doc.html.IHtmlBody;
import com.lanbo.doc.html.IHtmlDoc;
import com.lanbo.doc.html.IHtmlHeader;
import com.lanbo.doc.html.MyAllClassHtmlDoc;
import com.lanbo.doc.html.MyHtmlDoc;
import com.lanbo.doc.html.MyHtmlIndexDoc;
import com.lanbo.util.DocUtil;
import com.lanbo.util.FileUtil;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.LanguageVersion;
import com.sun.tools.doclets.formats.html.ConfigurationImpl;

/**
 * @author lanbo
 */
public class ApiDoclet extends Doclet {
	protected static boolean startApi(boolean isInternalApi, String outpath, String lang, ClassDoc[] classes) {
		ArrayList<ClazzDetail> allClasses = new ArrayList<>();
		for(int i=0 ; i < classes.length ; i++) {
			ClassDoc cDoc = classes[i];
			if (DocUtil.isHideOrRemoved(cDoc)) continue;
			
			if (!isInternalApi && !DocUtil.isApi(cDoc)) continue;
			if ("R".equals(cDoc.name())) continue;
			if ("R.string".equals(cDoc.name())) continue;
			if ("BuildConfig".equals(cDoc.name())) continue;
			
			String cName = cDoc.qualifiedName();
			String name = cName.replaceAll("\\.", "\\/");
			System.out.println(name);
			
			allClasses.add(new ClazzDetail(cDoc.containingPackage().name(), cDoc.name(), name+".html"));
			
			StringBuilder relativePath = new StringBuilder("./");
			char[] chars = name.toCharArray();
			for (int j = 0; j < chars.length; j++) {
				if (chars[j] == '/') {
					relativePath.append("../");
				}
			}
			
			IHtmlHeader htmlHeader = new HtmlHeader(cDoc, relativePath.toString()+"sdk_sample.css");
			IHtmlBody htmlBody = new HtmlBody(cDoc, isInternalApi, lang);
			IHtmlDoc htmlDoc = new MyHtmlDoc(htmlHeader, htmlBody, lang);
			
			FileUtil.createAndWriteToFile(outpath+"/"+name+".html", htmlDoc.genHtml());
		}
		
		allClasses.sort(new Comparator<ClazzDetail>() {
			@Override
			public int compare(ClazzDetail o1, ClazzDetail o2) {
				int ret =  o1.pack.compareTo(o2.pack);
				if (ret == 0) return o1.name.compareTo(o2.name);
				return ret;
			}
		});
		
		FileUtil.createAndWriteToFile(outpath+"/allclasses-frame.html", new MyAllClassHtmlDoc(allClasses).genAllClassHtml());
		FileUtil.createAndWriteToFile(outpath+"/index.html", new MyHtmlIndexDoc("allclasses-frame.html").genIndexHtml());
		
		return true;
	}
	
	public static int optionLength(String option) {
		return new ConfigurationImpl().optionLength(option);
    }
 
	public static boolean validOptions(String options[][], DocErrorReporter reporter) {
        return new ConfigurationImpl().validOptions(options, reporter);
    }
	
	// 需要重写，可以使用范型等新JDK用到的功能
    public static LanguageVersion languageVersion() {
        return LanguageVersion.JAVA_1_5;
    }
}
