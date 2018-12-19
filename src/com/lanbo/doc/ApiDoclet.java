package com.lanbo.doc;

import java.util.ArrayList;
import java.util.Comparator;

import com.lanbo.doc.bean.ClazzDetail;
import com.lanbo.doc.convert.Comment;
import com.lanbo.doc.convert.Converter;
import com.lanbo.doc.html.HtmlBody;
import com.lanbo.doc.html.HtmlHeader;
import com.lanbo.doc.html.IHtmlBody;
import com.lanbo.doc.html.IHtmlDoc;
import com.lanbo.doc.html.IHtmlHeader;
import com.lanbo.doc.html.MyAllClassHtmlDoc;
import com.lanbo.doc.html.MyHtmlDoc;
import com.lanbo.doc.html.MyHtmlIndexDoc;
import com.lanbo.doc.info.TagInfo;
import com.lanbo.doc.stub.Stubs;
import com.lanbo.util.DocUtil;
import com.lanbo.util.FileUtil;
import com.lanbo.util.TextUtil;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.formats.html.ConfigurationImpl;

/**
 * @author lanbo
 */
public class ApiDoclet extends Doclet {
    private static String mLang;
    private static String mOutPath;
	protected static boolean startApi(boolean isInternalApi, String outpath, String lang, ClassDoc[] classes) {
		ArrayList<ClazzDetail> allClasses = new ArrayList<>();
		if (TextUtil.isEmpty(mOutPath)) {
		    mOutPath = outpath;
		}
		if (TextUtil.isEmpty(mLang)) {
            mLang = lang;
        }
		
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
			
			IHtmlBody htmlBody = new HtmlBody(cDoc, isInternalApi, mLang);
			IHtmlDoc htmlDoc = new MyHtmlDoc(htmlHeader, htmlBody, mLang);
			
			FileUtil.createAndWriteToFile(mOutPath+"/"+name+".html", htmlDoc.genHtml());
		}
		
		allClasses.sort(new Comparator<ClazzDetail>() {
			@Override
			public int compare(ClazzDetail o1, ClazzDetail o2) {
				int ret =  o1.pack.compareTo(o2.pack);
				if (ret == 0) return o1.name.compareTo(o2.name);
				return ret;
			}
		});
		
		FileUtil.createAndWriteToFile(mOutPath+"/allclasses-frame.html", new MyAllClassHtmlDoc(allClasses).genAllClassHtml());
		FileUtil.createAndWriteToFile(mOutPath+"/index.html", new MyHtmlIndexDoc("allclasses-frame.html").genIndexHtml());
		
		return true;
	}
	
	public static int optionLength(String option) {
	    //System.out.println("optionLength " + option);
	    if ("-lang".equals(option)) {
	        return 2;
	    }
	    if ("-outpath".equals(option)) {
	        return 2;
	    }
		return new ConfigurationImpl().optionLength(option);
    }

	public static boolean validOptions(String options[][], DocErrorReporter reporter) {
	    for (int i = 0; i < options.length; i++) {
            /*for (int j = 0; j < options[i].length; j++) {
                System.out.print(options[i][j] + " ");
            }*/
	        //System.out.println();
            if ("-lang".equals(options[i][0])) {
                mLang = options[i][1];
            }
            if ("-outpath".equals(options[i][0])) {
                mOutPath = options[i][1];
            }
        }
        return new ConfigurationImpl().validOptions(options, reporter);
    }
	
	// 需要重写，可以使用范型等新JDK用到的功能
    public static LanguageVersion languageVersion() {
        return LanguageVersion.JAVA_1_5;
    }
    
    protected static boolean lanboDocStart(RootDoc root) {
        Converter.makeInfo(root);
        if (TextUtil.isEmpty(mOutPath)) {
            mOutPath = "stubs/src";
        }

        //ClassInfo[] all = ConvertClass.allClasses();//allClasses();
        /*for (int i = 0; i < all.length; i++) {
            ClassInfo classInfo = all[i];
            if (classInfo.isApi()) System.out.println(classInfo.toString() + "\t\t=");
            else System.out.println(classInfo.toString());

            if (!classInfo.comment().isApi())
                continue;

            System.out.println("==========================");
            printComment(classInfo.comment());
            System.out.println(classInfo.toString());

            System.out.println("{");

            ArrayList<FieldInfo> fieldInfos = classInfo.fields();
            for (int j = 0; j < fieldInfos.size(); j++) {
                FieldInfo fieldInfo = fieldInfos.get(j);
                if (!fieldInfo.comment().isApi())
                    continue;
                printComment(fieldInfo.comment());
                System.out.println("\t" + fieldInfo.name());
            }

            System.out.println("------------");

            ArrayList<MethodInfo> methodInfos = classInfo.selfMethods();
            for (int j = 0; j < methodInfos.size(); j++) {
                MethodInfo methodInfo = methodInfos.get(j);
                if (!methodInfo.comment().isApi())
                    continue;
                printComment(methodInfo.comment());
                System.out.println("\t" + methodInfo.toString());
            }
            System.out.println("};");
        }*/
        // 文档输出

        // Stubs

//        if (processInternal()) {
//            // 内部SDK
//            if (stubsDir != null || apiFile != null) {
//                InternalStubs.writeStubsAndApi(stubsDir, apiFile, stubPackages);
//            }
//        } else {
            // 外部SDK Stubs
            //if (stubsDir != null || apiFile != null || proguardFile != null
            //        || removedApiFile != null) {
                Stubs.writeStubsAndApi(mOutPath, null, null, null, null);
            //}
        //}

        return true;
    }

    private static void printComment(Comment comment) {
        TagInfo[] tagInfos = comment.zhDescTags();
        for (int j = 0; j < tagInfos.length; j++) {
            TagInfo tagInfo = tagInfos[j];
            System.out.println("\t" + tagInfo.name() + ", " + tagInfo.text());
        }
    }
    
}
