package com.lanbo.doc.convert;

import com.lanbo.doc.info.MemberInfo;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MemberDoc;
import com.sun.javadoc.MethodDoc;

public class ConvertMember {

    public static MemberInfo obtainMember(MemberDoc o) {
        return (MemberInfo) mMembers.obtain(o);
    }

    public static DocInfoCache mMembers = new DocInfoCache() {
        @Override
        protected Object make(Object o) {
            if (o instanceof MethodDoc) {
                return Converter.obtainMethod((MethodDoc) o);
            } else if (o instanceof ConstructorDoc) {
                return Converter.obtainMethod((ConstructorDoc) o);
            } else if (o instanceof FieldDoc) {
                return Converter.obtainField((FieldDoc) o);
            } else {
                return null;
            }
        }
    };
}
