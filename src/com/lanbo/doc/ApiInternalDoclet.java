package com.lanbo.doc;

import java.util.ArrayList;

import com.lanbo.doc.convert.Comment;
import com.lanbo.doc.convert.ConvertClass;
import com.lanbo.doc.convert.Converter;
import com.lanbo.doc.info.ClassInfo;
import com.lanbo.doc.info.FieldInfo;
import com.lanbo.doc.info.MethodInfo;
import com.lanbo.doc.info.TagInfo;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

/**
 * @author lanbo
 */
public class ApiInternalDoclet extends ApiDoclet {

    public static boolean start(RootDoc root) {
        return lanboDocStart(root);

        // testClass(root.classes());
        // return true;
    }

    private static boolean testClass(ClassDoc[] classes) {
        return startApi(true, "apiInternal", "zh", classes);
    }

    private static boolean lanboDocStart(RootDoc root) {
        Converter.makeInfo(root);

        ClassInfo[] all = ConvertClass.allClasses();
        for (int i = 0; i < all.length; i++) {
            ClassInfo classInfo = all[i];
            
            if (!classInfo.comment().isApi())
               continue;
            
            System.out.println("==========================");
            printComment(classInfo.comment());
            System.out.println(classInfo.toString());
            
            System.out.println("{");
            
            ArrayList<FieldInfo> fieldInfos = classInfo.fields();
            for (int j = 0; j < fieldInfos.size(); j++) {
                FieldInfo fieldInfo = fieldInfos.get(j);
                if (!fieldInfo.comment().isApi()) continue;
                printComment(fieldInfo.comment());
                System.out.println("\t" + fieldInfo.name());
            }

            System.out.println("------------");

            ArrayList<MethodInfo> methodInfos = classInfo.selfMethods();
            for (int j = 0; j < methodInfos.size(); j++) {
                MethodInfo methodInfo = methodInfos.get(j);
                if (!methodInfo.comment().isApi()) continue;
                printComment(methodInfo.comment());
                System.out.println("\t" + methodInfo.toString());
            }
            System.out.println("};");
        }
        // 文档输出

        // Stubs
        /*
         * if (processInternal()) {// 内部SDK if (stubsDir != null || apiFile !=
         * null) { InternalStubs.writeStubsAndApi(stubsDir, apiFile,
         * stubPackages); } } else {// 外部SDK // Stubs if (stubsDir != null ||
         * apiFile != null || proguardFile != null || removedApiFile != null) {
         * ExternalStubs.writeStubsAndApi(stubsDir, apiFile, proguardFile,
         * removedApiFile, stubPackages); } }
         */
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
