package com.lanbo.doc.convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lanbo.doc.info.AnnotationInstanceInfo;
import com.lanbo.doc.info.ClassInfo;
import com.lanbo.doc.info.MethodInfo;
import com.lanbo.doc.info.ParameterInfo;
import com.lanbo.doc.info.TypeInfo;
import com.sun.javadoc.AnnotationTypeElementDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.MethodDoc;

public class ConvertMethod {
    /**
     * Converts ExecutableMemberDoc[] into List<MethodInfo>. No filtering is
     * done.
     */
    public static List<MethodInfo> convertAllMethods(ExecutableMemberDoc[] methods) {
        if (methods == null)
            return null;
        List<MethodInfo> allMethods = new ArrayList<MethodInfo>();
        for (ExecutableMemberDoc method : methods) {
            MethodInfo methodInfo = obtainMethod(method);
            allMethods.add(methodInfo);
        }
        return allMethods;
    }

    /**
     * Convert MethodDoc[] or ConstructorDoc[] into MethodInfo[]. Also filters
     * according to the -private, -public option, because the filtering doesn't
     * seem to be working in the ClassDoc.constructors(boolean) call.
     */
    public static MethodInfo[] convertMethods(ExecutableMemberDoc[] methods) {
        if (methods == null)
            return null;
        List<MethodInfo> filteredMethods = new ArrayList<MethodInfo>();
        for (ExecutableMemberDoc method : methods) {
            MethodInfo methodInfo = obtainMethod(method);
            if (methodInfo.checkLevel()) {
                filteredMethods.add(methodInfo);
            }
        }

        return filteredMethods.toArray(new MethodInfo[filteredMethods.size()]);
    }

    public static MethodInfo[] convertNonWrittenConstructors(ConstructorDoc[] methods) {
        if (methods == null)
            return null;
        ArrayList<MethodInfo> ctors = new ArrayList<MethodInfo>();
        for (ConstructorDoc method : methods) {
            MethodInfo methodInfo = obtainMethod(method);
            if (!methodInfo.checkLevel()) {
                ctors.add(methodInfo);
            }
        }

        return ctors.toArray(new MethodInfo[ctors.size()]);
    }

    public static <E extends ExecutableMemberDoc> MethodInfo obtainMethod(E o) {
        return (MethodInfo) mMethods.obtain(o);
    }

    private static DocInfoCache mMethods = new DocInfoCache() {
        @Override
        protected Object make(Object o) {
            if (o instanceof AnnotationTypeElementDoc) {
                AnnotationTypeElementDoc m = (AnnotationTypeElementDoc) o;
                MethodInfo result = new MethodInfo(m.getRawCommentText(), new ArrayList<TypeInfo>(
                        Arrays.asList(Converter.convertTypes(m.typeParameters()))), m.name(),
                        m.signature(), ConvertClass.obtainClass(m.containingClass()),
                        ConvertClass.obtainClass(m.containingClass()), m.isPublic(),
                        m.isProtected(), m.isPackagePrivate(), m.isPrivate(), m.isFinal(),
                        m.isStatic(), m.isSynthetic(), m.isAbstract(), m.isSynchronized(),
                        m.isNative(), m.isDefault(), true, "annotationElement", m.flatSignature(),
                        obtainMethod(m.overriddenMethod()), Converter.obtainType(m.returnType()),
                        new ArrayList<ParameterInfo>(Arrays.asList(Converter.convertParameters(
                                m.parameters(), m))), new ArrayList<ClassInfo>(
                                Arrays.asList(Converter.convertClasses(m.thrownExceptions()))),
                        Converter.convertSourcePosition(m.position()),
                        new ArrayList<AnnotationInstanceInfo>(Arrays.asList(Converter
                                .convertAnnotationInstances(m.annotations()))));
                result.setVarargs(m.isVarArgs());
                result.init(Converter.obtainAnnotationValue(m.defaultValue(), result));
                return result;
            } else if (o instanceof MethodDoc) {
                MethodDoc m = (MethodDoc) o;
                MethodInfo result = new MethodInfo(m.getRawCommentText(), new ArrayList<TypeInfo>(
                        Arrays.asList(Converter.convertTypes(m.typeParameters()))), m.name(),
                        m.signature(), ConvertClass.obtainClass(m.containingClass()),
                        ConvertClass.obtainClass(m.containingClass()), m.isPublic(),
                        m.isProtected(), m.isPackagePrivate(), m.isPrivate(), m.isFinal(),
                        m.isStatic(), m.isSynthetic(), m.isAbstract(), m.isSynchronized(),
                        m.isNative(), m.isDefault(), false, "method", m.flatSignature(),
                        obtainMethod(m.overriddenMethod()), Converter.obtainType(m.returnType()),
                        new ArrayList<ParameterInfo>(Arrays.asList(Converter.convertParameters(
                                m.parameters(), m))), new ArrayList<ClassInfo>(
                                Arrays.asList(Converter.convertClasses(m.thrownExceptions()))),
                        Converter.convertSourcePosition(m.position()),
                        new ArrayList<AnnotationInstanceInfo>(Arrays.asList(Converter
                                .convertAnnotationInstances(m.annotations()))));
                result.setVarargs(m.isVarArgs());
                result.init(null);
                return result;
            } else {
                ConstructorDoc m = (ConstructorDoc) o;
                // Workaround for a JavaDoc behavior change introduced in
                // OpenJDK 8 that breaks
                // links in documentation and the content of API files like
                // current.txt.
                // http://b/18051133.
                String name = m.name();
                ClassDoc containingClass = m.containingClass();
                if (containingClass.containingClass() != null) {
                    // This should detect the new behavior and be bypassed
                    // otherwise.
                    if (!name.contains(".")) {
                        // Constructors of inner classes do not contain the name
                        // of the enclosing class
                        // with OpenJDK 8. This simulates the old behavior:
                        name = containingClass.name();
                    }
                }
                // End of workaround.
                MethodInfo result = new MethodInfo(m.getRawCommentText(), new ArrayList<TypeInfo>(
                        Arrays.asList(Converter.convertTypes(m.typeParameters()))), name,
                        m.signature(), ConvertClass.obtainClass(m.containingClass()),
                        ConvertClass.obtainClass(m.containingClass()), m.isPublic(),
                        m.isProtected(), m.isPackagePrivate(), m.isPrivate(), m.isFinal(),
                        m.isStatic(), m.isSynthetic(), false, m.isSynchronized(), m.isNative(),
                        false/* isDefault */, false, "constructor", m.flatSignature(), null, null,
                        new ArrayList<ParameterInfo>(Arrays.asList(Converter.convertParameters(
                                m.parameters(), m))), new ArrayList<ClassInfo>(
                                Arrays.asList(Converter.convertClasses(m.thrownExceptions()))),
                        Converter.convertSourcePosition(m.position()),
                        new ArrayList<AnnotationInstanceInfo>(Arrays.asList(Converter
                                .convertAnnotationInstances(m.annotations()))));
                result.setVarargs(m.isVarArgs());
                result.init(null);
                return result;
            }
        }
    };

    public static MethodInfo[] getHiddenMethods(MethodDoc[] methods) {
        if (methods == null)
            return null;
        ArrayList<MethodInfo> hiddenMethods = new ArrayList<MethodInfo>();
        for (MethodDoc method : methods) {
            MethodInfo methodInfo = Converter.obtainMethod(method);
            if (methodInfo.isHidden()) {
                hiddenMethods.add(methodInfo);
            }
        }

        return hiddenMethods.toArray(new MethodInfo[hiddenMethods.size()]);
    }

    // Gets the removed methods regardless of access levels
    public static MethodInfo[] getRemovedMethods(MethodDoc[] methods) {
        if (methods == null)
            return null;
        ArrayList<MethodInfo> removedMethods = new ArrayList<MethodInfo>();
        for (MethodDoc method : methods) {
            MethodInfo methodInfo = Converter.obtainMethod(method);
            if (methodInfo.isRemoved()) {
                removedMethods.add(methodInfo);
            }
        }

        return removedMethods.toArray(new MethodInfo[removedMethods.size()]);
    }
}
