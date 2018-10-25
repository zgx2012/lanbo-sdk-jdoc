package com.lanbo.doc.convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lanbo.doc.info.AnnotationInstanceInfo;
import com.lanbo.doc.info.FieldInfo;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.FieldDoc;

public class ConvertField {
    /**
     * Converts FieldDoc[] into List<FieldInfo>. No filtering is done.
     */
    public static List<FieldInfo> convertAllFields(FieldDoc[] fields) {
        if (fields == null)
            return null;
        List<FieldInfo> allFields = new ArrayList<FieldInfo>();

        for (FieldDoc field : fields) {
            FieldInfo fieldInfo = Converter.obtainField(field);
            allFields.add(fieldInfo);
        }

        return allFields;
    }

    public static FieldInfo[] convertFields(FieldDoc[] fields) {
        if (fields == null)
            return null;
        ArrayList<FieldInfo> out = new ArrayList<FieldInfo>();
        int N = fields.length;
        for (int i = 0; i < N; i++) {
            FieldInfo f = Converter.obtainField(fields[i]);
            if (f.checkLevel()) {
                out.add(f);
            }
        }
        return out.toArray(new FieldInfo[out.size()]);
    }

    public static FieldInfo obtainField(FieldDoc o) {
        return (FieldInfo) mFields.obtain(o);
    }

    public static FieldInfo obtainField(ConstructorDoc o) {
        return (FieldInfo) mFields.obtain(o);
    }

    private static DocInfoCache mFields = new DocInfoCache() {
        @Override
        protected Object make(Object o) {
            FieldDoc f = (FieldDoc) o;
            return new FieldInfo(f.name(), Converter.obtainClass(f.containingClass()),
                    Converter.obtainClass(f.containingClass()), f.isPublic(), f.isProtected(),
                    f.isPackagePrivate(), f.isPrivate(), f.isFinal(), f.isStatic(),
                    f.isTransient(), f.isVolatile(), f.isSynthetic(),
                    Converter.obtainType(f.type()), f.getRawCommentText(), f.constantValue(),
                    Converter.convertSourcePosition(f.position()),
                    new ArrayList<AnnotationInstanceInfo>(Arrays.asList(Converter
                            .convertAnnotationInstances(f.annotations()))));
        }
    };
}
