package com.lanbo.doc;

import com.sun.javadoc.*;

/**
 * @author lanbo
 */
public class ApiInternalDoclet extends ApiDoclet {

	public static boolean start(RootDoc root) {
		testClass(root.classes());
		return true;
	}
	
	private static boolean testClass(ClassDoc[] classes) {
		return startApi(true, "apiInternal", "zh", classes);
	}
}
