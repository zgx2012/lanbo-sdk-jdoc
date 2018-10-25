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

import com.lanbo.doc.convert.Comment;
import com.lanbo.doc.convert.LinkReference;

public class AttrTagInfo extends TagInfo {
    public static final AttrTagInfo[] EMPTY_ARRAY = new AttrTagInfo[0];

    public static AttrTagInfo[] getArray(int size) {
        return size == 0 ? EMPTY_ARRAY : new AttrTagInfo[size];
    }

    private static final String REF_COMMAND = "ref";
    private static final String NAME_COMMAND = "name";
    private static final String DESCRIPTION_COMMAND = "description";
    private static final Pattern TEXT = Pattern.compile("(\\S+)\\s*(.*)", Pattern.DOTALL);
    private static final Pattern NAME_TEXT = Pattern.compile("(\\S+)(.*)", Pattern.DOTALL);

    private ContainerInfo mBase;
    private String mCommand;

    // if mCommand == "ref"
    private FieldInfo mRefField;
    private AttributeInfo mAttrInfo;

    // if mCommand == "name"
    private String mAttrName;

    // if mCommand == "description"
    private Comment mDescrComment;

    public AttrTagInfo(String name, String kind, String text, ContainerInfo base,
            SourcePositionInfo position) {
        super(name, kind, text, position);
        mBase = base;

        parse(text, base, position);
    }

    void parse(String text, ContainerInfo base, SourcePositionInfo position) {
        Matcher m;

        m = TEXT.matcher(text);
        if (!m.matches()) {
            return;
        }

        String command = m.group(1);
        String more = m.group(2);

        if (REF_COMMAND.equals(command)) {
            String ref = more.trim();
            LinkReference linkRef = LinkReference.parse(ref, mBase, position, false);
            if (!linkRef.good) {
                return;
            }
            if (!(linkRef.memberInfo instanceof FieldInfo)) {
                return;
            }
            mCommand = command;
            mRefField = (FieldInfo) linkRef.memberInfo;
        } else if (NAME_COMMAND.equals(command)) {
            m = NAME_TEXT.matcher(more);
            if (!m.matches() || m.group(2).trim().length() != 0) {
                return;
            }
            mCommand = command;
            mAttrName = m.group(1);
        } else if (DESCRIPTION_COMMAND.equals(command)) {
            mCommand = command;
            mDescrComment = new Comment(more, base, position);
        } else {
        }
    }

    public FieldInfo reference() {
        return REF_COMMAND.equals(mCommand) ? mRefField : null;
    }

    @Override
    public String name() {
        return NAME_COMMAND.equals(mCommand) ? mAttrName : null;
    }

    public Comment description() {
        return DESCRIPTION_COMMAND.equals(mCommand) ? mDescrComment : null;
    }

    public void setAttribute(AttributeInfo info) {
        mAttrInfo = info;
    }

}
