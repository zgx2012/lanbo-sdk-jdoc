package com.lanbo.util;

import com.sun.javadoc.Doc;

public class DocUtil {
	public static boolean isHideOrRemoved(Doc doc) {
		doc.commentText();
		String comment = doc.getRawCommentText();
		return comment.indexOf("@hide") != -1 || comment.indexOf("@pending") != -1 ||
				comment.indexOf("@removed") != -1;
	}
	
	public static boolean isApi(Doc doc) {
		doc.commentText();
		String comment = doc.getRawCommentText();
		return comment.indexOf("@api") != -1;
	}
}
