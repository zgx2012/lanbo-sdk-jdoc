package com.lanbo.doc.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.lanbo.doc.info.AnnotationInstanceInfo;
import com.lanbo.doc.info.AnnotationValueInfo;
import com.lanbo.doc.info.ClassInfo;
import com.lanbo.doc.info.MethodInfo;
import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationValue;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.Type;

public class ConvertAnnotation {

    public static AnnotationInstanceInfo[] convertAnnotationInstances(AnnotationDesc[] orig) {
        int len = orig.length;
        AnnotationInstanceInfo[] out = new AnnotationInstanceInfo[len];
        for (int i = 0; i < len; i++) {
            out[i] = Converter.obtainAnnotationInstance(orig[i]);
        }
        return out;
    }

    public static AnnotationInstanceInfo obtainAnnotationInstance(AnnotationDesc o) {
        return (AnnotationInstanceInfo) mAnnotationInstances.obtain(o);
    }

    private static DocInfoCache mAnnotationInstances = new DocInfoCache() {
        @Override
        protected Object make(Object o) {
            AnnotationDesc a = (AnnotationDesc) o;
            ClassInfo annotationType = Converter.obtainClass(a.annotationType());
            AnnotationDesc.ElementValuePair[] ev = a.elementValues();
            AnnotationValueInfo[] elementValues = new AnnotationValueInfo[ev.length];
            for (int i = 0; i < ev.length; i++) {
                elementValues[i] = obtainAnnotationValue(ev[i].value(),
                        Converter.obtainMethod(ev[i].element()));
            }
            return new AnnotationInstanceInfo(annotationType, elementValues);
        }
    };

    // annotation values
    private static HashMap<AnnotationValue, AnnotationValueInfo> mAnnotationValues = new HashMap<AnnotationValue, AnnotationValueInfo>();
    private static HashSet<AnnotationValue> mAnnotationValuesNeedingInit = new HashSet<AnnotationValue>();

    public static AnnotationValueInfo obtainAnnotationValue(AnnotationValue o, MethodInfo element) {
        if (o == null) {
            return null;
        }
        AnnotationValueInfo v = mAnnotationValues.get(o);
        if (v != null)
            return v;
        v = new AnnotationValueInfo(element);
        mAnnotationValues.put(o, v);
        if (mAnnotationValuesNeedingInit != null) {
            mAnnotationValuesNeedingInit.add(o);
        } else {
            initAnnotationValue(o, v);
        }
        return v;
    }

    private static void initAnnotationValue(AnnotationValue o, AnnotationValueInfo v) {
        Object orig = o.value();
        Object converted;
        if (orig instanceof Type) {
            // class literal
            converted = Converter.obtainType((Type) orig);
        } else if (orig instanceof FieldDoc) {
            // enum constant
            converted = Converter.obtainField((FieldDoc) orig);
        } else if (orig instanceof AnnotationDesc) {
            // annotation instance
            converted = Converter.obtainAnnotationInstance((AnnotationDesc) orig);
        } else if (orig instanceof AnnotationValue[]) {
            AnnotationValue[] old = (AnnotationValue[]) orig;
            ArrayList<AnnotationValueInfo> values = new ArrayList<AnnotationValueInfo>();
            for (int i = 0; i < old.length; i++) {
                values.add(Converter.obtainAnnotationValue(old[i], null));
            }
            converted = values;
        } else {
            converted = orig;
        }
        v.init(converted);
    }

    public static void finishAnnotationValueInit() {
        int depth = 0;
        while (mAnnotationValuesNeedingInit.size() > 0) {
            HashSet<AnnotationValue> set = mAnnotationValuesNeedingInit;
            mAnnotationValuesNeedingInit = new HashSet<AnnotationValue>();
            for (AnnotationValue o : set) {
                AnnotationValueInfo v = mAnnotationValues.get(o);
                initAnnotationValue(o, v);
            }
            depth++;
        }
        mAnnotationValuesNeedingInit = null;
    }
}
