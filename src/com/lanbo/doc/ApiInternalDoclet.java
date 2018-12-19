package com.lanbo.doc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

/**
 * @author lanbo
 */
public class ApiInternalDoclet extends ApiDoclet {
    public static boolean start(RootDoc root) {
        return testClass(root.classes());
    }

    private static boolean testClass(ClassDoc[] classes) {
        return startApi(true, "apiInternal", "zh", classes);
    }
}
