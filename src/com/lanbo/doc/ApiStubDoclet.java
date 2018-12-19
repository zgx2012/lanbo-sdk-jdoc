package com.lanbo.doc;

import com.sun.javadoc.RootDoc;

/**
 * @author lanbo
 */
public class ApiStubDoclet extends ApiDoclet {
    public static boolean start(RootDoc root) {
        return lanboDocStart(root);
    }
}
