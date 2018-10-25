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

import java.util.ArrayList;
import java.util.Comparator;

import com.lanbo.doc.convert.Comment;

public class AttributeInfo {
  public static final Comparator<AttributeInfo> comparator = new Comparator<AttributeInfo>() {
    public int compare(AttributeInfo a, AttributeInfo b) {
      return a.name().compareTo(b.name());
    }
  };

  public FieldInfo attrField;
  public ArrayList<MethodInfo> methods = new ArrayList<MethodInfo>();

  private ClassInfo mClass;
  private String mName;
  private Comment mComment;

  public AttributeInfo(ClassInfo cl, FieldInfo f) {
    mClass = cl;
    attrField = f;
  }

  public String name() {
    if (mName == null) {
      for (AttrTagInfo comment : attrField.comment().attrTags()) {
        String n = comment.name();
        if (n != null) {
          mName = n;
          return n;
        }
      }
      mName = attrField.name();
    }
    return mName;
  }

  public Comment comment() {
    if (mComment == null) {
      for (AttrTagInfo attr : attrField.comment().attrTags()) {
        Comment c = attr.description();
        if (c != null) {
          mComment = c;
          return c;
        }
      }
    }
    if (mComment == null) {
      return new Comment("", mClass, SourcePositionInfo.UNKNOWN);
    }
    return mComment;
  }

  public String anchor() {
    return "attr_" + name();
  }

  public String htmlPage() {
    return mClass.htmlPage() + "#" + anchor();
  }

  public boolean checkLevel() {
    return attrField.checkLevel();
  }
}
