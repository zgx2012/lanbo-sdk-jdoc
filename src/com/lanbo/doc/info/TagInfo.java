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

public class TagInfo {
  public static final TagInfo[] EMPTY_ARRAY = new TagInfo[0];

  public static TagInfo[] getArray(int size) {
      return size == 0 ? EMPTY_ARRAY : new TagInfo[size];
  }

  private String mName;
  private String mText;
  private String mKind;
  private SourcePositionInfo mPosition;

  public TagInfo(String n, String k, String t, SourcePositionInfo sp) {
    mName = n;
    mText = t;
    mKind = k;
    mPosition = sp;
  }

  public String name() {
    return mName;
  }

  public String text() {
    return mText;
  }

  public String kind() {
    return mKind;
  }

  public SourcePositionInfo position() {
    return mPosition;
  }

  void setKind(String kind) {
    mKind = kind;
  }
}
