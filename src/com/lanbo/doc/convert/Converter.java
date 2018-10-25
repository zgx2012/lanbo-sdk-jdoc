package com.lanbo.doc.convert;

import java.util.List;

import com.lanbo.doc.info.AnnotationInstanceInfo;
import com.lanbo.doc.info.AnnotationValueInfo;
import com.lanbo.doc.info.ClassInfo;
import com.lanbo.doc.info.FieldInfo;
import com.lanbo.doc.info.MethodInfo;
import com.lanbo.doc.info.PackageInfo;
import com.lanbo.doc.info.ParameterInfo;
import com.lanbo.doc.info.SourcePositionInfo;
import com.lanbo.doc.info.TypeInfo;
import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationValue;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.SourcePosition;
import com.sun.javadoc.Type;

public class Converter {
    public static RootDoc root;
    
    public static void makeInfo(RootDoc r) {
        root = r;

        ConvertClass.makeInfo(r);
    }

    // ///////////////////////////////////////////////////
    // Package
    // ///////////////////////////////////////////////////
    public static PackageInfo obtainPackage(PackageDoc o) {
        return ConvertPackage.obtainPackage(o);
    }

    public static PackageInfo obtainPackage(String packageName) {
        return ConvertPackage.obtainPackage(packageName);
    }

    // ///////////////////////////////////////////////////
    // Class
    // ///////////////////////////////////////////////////
    public static ClassInfo obtainClass(ClassDoc o) {
        return ConvertClass.obtainClass(o);
    }

    public static ClassInfo obtainClass(String className) {
        return ConvertClass.obtainClass(className);
    }

    public static ClassInfo[] convertClasses(ClassDoc[] classes) {
        return ConvertClass.convertClasses(classes);
    }

    // ///////////////////////////////////////////////////
    // Field
    // ///////////////////////////////////////////////////
    /**
     * Converts FieldDoc[] into List<FieldInfo>. No filtering is done.
     */
    public static List<FieldInfo> convertAllFields(FieldDoc[] fields) {
        return ConvertField.convertAllFields(fields);
    }

    public static FieldInfo[] convertFields(FieldDoc[] fields) {
        return ConvertField.convertFields(fields);
    }

    public static FieldInfo obtainField(FieldDoc o) {
        return ConvertField.obtainField(o);
    }

    public static FieldInfo obtainField(ConstructorDoc o) {
        return ConvertField.obtainField(o);
    }

    // ///////////////////////////////////////////////////
    // Method
    // ///////////////////////////////////////////////////
    /**
     * Converts ExecutableMemberDoc[] into List<MethodInfo>. No filtering is
     * done.
     */
    public static List<MethodInfo> convertAllMethods(ExecutableMemberDoc[] methods) {
        return ConvertMethod.convertAllMethods(methods);
    }

    /**
     * Convert MethodDoc[] or ConstructorDoc[] into MethodInfo[]. Also filters
     * according to the -private, -public option, because the filtering doesn't
     * seem to be working in the ClassDoc.constructors(boolean) call.
     */
    public static MethodInfo[] convertMethods(ExecutableMemberDoc[] methods) {
        return ConvertMethod.convertMethods(methods);
    }

    public static MethodInfo[] convertNonWrittenConstructors(ConstructorDoc[] methods) {
        return ConvertMethod.convertNonWrittenConstructors(methods);
    }

    public static <E extends ExecutableMemberDoc> MethodInfo obtainMethod(E o) {
        return ConvertMethod.obtainMethod(o);
    }

    public static MethodInfo[] getHiddenMethods(MethodDoc[] methods) {
        return ConvertMethod.getHiddenMethods(methods);
    }

    // Gets the removed methods regardless of access levels
    public static MethodInfo[] getRemovedMethods(MethodDoc[] methods) {
        return ConvertMethod.getRemovedMethods(methods);
    }

    // ///////////////////////////////////////////////////
    // Parameter
    // ///////////////////////////////////////////////////
    public static ParameterInfo convertParameter(Parameter p, SourcePosition pos, boolean isVarArg) {
        return ConvertParameter.convertParameter(p, pos, isVarArg);
    }

    public static ParameterInfo[] convertParameters(Parameter[] p, ExecutableMemberDoc m) {
        return ConvertParameter.convertParameters(p, m);
    }

    // ///////////////////////////////////////////////////
    // Type
    // ///////////////////////////////////////////////////
    public static TypeInfo[] convertTypes(Type[] p) {
        return ConvertType.convertTypes(p);
    }

    public static TypeInfo obtainType(Type o) {
        return ConvertType.obtainType(o);
    }

    public static TypeInfo obtainTypeFromString(String type) {
        return ConvertType.obtainTypeFromString(type);
    }

    // ///////////////////////////////////////////////////
    // Type
    // ///////////////////////////////////////////////////
    public static AnnotationInstanceInfo[] convertAnnotationInstances(AnnotationDesc[] orig) {
        return ConvertAnnotation.convertAnnotationInstances(orig);
    }

    public static AnnotationInstanceInfo obtainAnnotationInstance(AnnotationDesc o) {
        return ConvertAnnotation.obtainAnnotationInstance(o);
    }

    public static AnnotationValueInfo obtainAnnotationValue(AnnotationValue o, MethodInfo element) {
        return ConvertAnnotation.obtainAnnotationValue(o, element);
    }

    public static SourcePositionInfo convertSourcePosition(SourcePosition sp) {
        if (sp == null) {
            return null;
        }
        return new SourcePositionInfo(sp.file().toString(), sp.line(), sp.column());
    }

}
