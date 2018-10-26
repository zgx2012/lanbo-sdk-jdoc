/*
 * Copyright (C) 2014 MediaTek Inc.
 * Modification based on code covered by the mentioned copyright
 * and/or permission notice(s).
 */
/*
 * Copyright (C) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lanbo.doc.info;

import com.lanbo.doc.convert.Comment;

public abstract class DocInfo {
	public DocInfo(String rawCommentText, SourcePositionInfo sp) {
		mRawCommentText = rawCommentText;
		mPosition = sp;
	}

	/**
	 * The relative path to a web page representing this item.
	 */
	public abstract String htmlPage();

	/**
	 * @return true if the element has never been a part of public API
	 */
	public boolean isHidden() {
		return comment().isHidden();
	}

	/**
	 * @return true if the element was once a part of public API, now removed.
	 */
	public boolean isRemoved() {
		return comment().isRemoved();
	}
	
	/**
     * @return true if the element is a part of public API
     */
	public boolean isApi() {
	    return comment().isApi();
	}

	/**
	 * Hidden and removed elements should not be appear in api.txt files, nor
	 * should they appear in the java doc.
	 * 
	 * @return true if the element is either hidden or removed.
	 */
	public boolean isHiddenOrRemoved() {
		return isHidden() || isRemoved();
	}

	public boolean isDocOnly() {
		return comment().isDocOnly();
	}

	public String getRawCommentText() {
		return mRawCommentText;
	}

	// / M: Add internal Javadoc tag handling. @{
	public boolean isInternal() {
		return comment().isInternal();
	}

	// / @}
	public void setRawCommentText(String rawCommentText) {
		mRawCommentText = rawCommentText;

		// so that if we've created one prior to changing, we recreate it
		if (mComment != null) {
			mComment = new Comment(mRawCommentText, parent(), mPosition);
		}

	}

	public Comment comment() {
		if (mComment == null) {
			mComment = new Comment(mRawCommentText, parent(), mPosition);
		}
		return mComment;
	}

	public SourcePositionInfo position() {
		return mPosition;
	}

	public void setPosition(SourcePositionInfo position) {
		mPosition = position;

		// so that if we've created one prior to changing, we recreate it
		if (mComment != null) {
			mComment = new Comment(mRawCommentText, parent(), mPosition);
		}
	}

	public abstract ContainerInfo parent();

	public void setSince(String since) {
		mSince = since;
	}

	public String getSince() {
		return mSince;
	}

	public void setDeprecatedSince(String since) {
		mDeprecatedSince = since;
	}

	public String getDeprecatedSince() {
		return mDeprecatedSince;
	}

	public boolean isDeprecated() {
		return mDeprecatedSince != null ? true : false;
	}

	private String mRawCommentText;
	Comment mComment;
	SourcePositionInfo mPosition;
	private String mSince;
	private String mDeprecatedSince;
}
