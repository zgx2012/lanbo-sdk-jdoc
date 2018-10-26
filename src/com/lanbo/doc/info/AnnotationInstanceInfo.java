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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AnnotationInstanceInfo {
  private ClassInfo mType;
  private String mAnnotationName; // for debugging purposes TODO - remove
  private ArrayList<AnnotationValueInfo> mElementValues;

  public AnnotationInstanceInfo() {
      mType = null;
      mElementValues = new ArrayList<AnnotationValueInfo>();
    }

  public AnnotationInstanceInfo(ClassInfo type, AnnotationValueInfo[] elementValues) {
    mType = type;
    mElementValues = new ArrayList<AnnotationValueInfo>(Arrays.asList(elementValues));
  }

  public ClassInfo type() {
    return mType;
  }

  public void setClass(ClassInfo cl) {
      mType = cl;
  }

  public void setSimpleAnnotationName(String name) {
      mAnnotationName = name;
  }

  ArrayList<AnnotationValueInfo> elementValues() {
    return mElementValues;
  }

  public void addElementValue(AnnotationValueInfo info) {
      mElementValues.add(info);
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    str.append("@");
    if (mType == null) {
        str.append(mAnnotationName);
    } else {
        str.append(mType.qualifiedName());
    }
    str.append("(");

    for (AnnotationValueInfo value : mElementValues) {
      if (value.element() != null) {
          str.append(value.element().name());
          str.append("=");
      }

      str.append(value.valueString());
      if (value != mElementValues.get(mElementValues.size()-1)) {
        str.append(",");
      }
    }
    str.append(")");
    return str.toString();
  }

  /**
   * Get a new list containing the set of annotations that are shared between
   * the input annotations collection and the names of annotations passed in
   * the showAnnotations parameter
   */
  public static ArrayList<AnnotationInstanceInfo> getShowAnnotationsIntersection(
          ArrayList<AnnotationInstanceInfo> annotations) {
    ArrayList<AnnotationInstanceInfo> list = new ArrayList<AnnotationInstanceInfo>();
    if (annotations != null) {
      for (AnnotationInstanceInfo info : annotations) {
        if (showAnnotations.contains(info.type().qualifiedName())) {
          list.add(info);
        }
      }
    }
    return list;
  }
  
  public static Set<String> showAnnotations = new HashSet<String>();
}
