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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParamTagInfo extends ParsedTagInfo {
  public static final ParamTagInfo[] EMPTY_ARRAY = new ParamTagInfo[0];

  public static ParamTagInfo[] getArray(int size) {
      return size == 0 ? EMPTY_ARRAY : new ParamTagInfo[size];
  }

  static final Pattern PATTERN = Pattern.compile("([^ \t\r\n]+)[ \t\r\n]+(.*)", Pattern.DOTALL);

  private boolean mIsTypeParameter;
  private String mParameterComment;
  private String mParameterName;

  public ParamTagInfo(String name, String kind, String text, ContainerInfo base, SourcePositionInfo sp) {
    super(name, kind, text, base, sp);

    Matcher m = PATTERN.matcher(text);
    if (m.matches()) {
      mParameterName = m.group(1);
      mParameterComment = m.group(2);
      int len = mParameterName.length();
      mIsTypeParameter =
          len > 2 && mParameterName.charAt(0) == '<' && mParameterName.charAt(len - 1) == '>';
    } else {
      mParameterName = text.trim();
      mParameterComment = "";
      mIsTypeParameter = false;
    }
    setCommentText(mParameterComment);
  }

  ParamTagInfo(String name, String kind, String text, boolean isTypeParameter,
      String parameterComment, String parameterName, ContainerInfo base, SourcePositionInfo sp) {
    super(name, kind, text, base, sp);
    mIsTypeParameter = isTypeParameter;
    mParameterComment = parameterComment;
    mParameterName = parameterName;
  }

  public boolean isTypeParameter() {
    return mIsTypeParameter;
  }

  public String parameterComment() {
    return mParameterComment;
  }

  public String parameterName() {
    return mParameterName;
  }
}
