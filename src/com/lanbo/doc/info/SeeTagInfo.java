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

import com.lanbo.doc.convert.LinkReference;


public class SeeTagInfo extends TagInfo {
  public static final SeeTagInfo[] EMPTY_ARRAY = new SeeTagInfo[0];

  public static SeeTagInfo[] getArray(int size) {
      return size == 0 ? EMPTY_ARRAY : new SeeTagInfo[size];
  }

  private ContainerInfo mBase;
  LinkReference mLink;

  public SeeTagInfo(String name, String kind, String text, ContainerInfo base, SourcePositionInfo position) {
    super(name, kind, text, position);
    mBase = base;
  }

  protected LinkReference linkReference() {
    if (mLink == null) {
      mLink =
          LinkReference.parse(text(), mBase, position(), (!"@see".equals(name()))
              && (mBase != null ? mBase.checkLevel() : true));
    }
    return mLink;
  }

  public String label() {
    return linkReference().label;
  }

  public boolean checkLevel() {
    return linkReference().checkLevel();
  }

}
