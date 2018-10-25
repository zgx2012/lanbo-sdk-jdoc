package com.lanbo.doc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

/**
 * @author lanbo
 */
public class ApiExternalDoclet extends ApiDoclet {

	public static boolean start(RootDoc root) {
		testClass(root.classes());
		return true;
	}

	private static boolean testClass(ClassDoc[] classes) {
		return startApi(false, "apiExternal", "zh", classes);
	}
}
