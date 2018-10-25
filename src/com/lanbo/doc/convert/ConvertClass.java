package com.lanbo.doc.convert;

import java.util.ArrayList;
import java.util.Arrays;

import com.lanbo.doc.info.AnnotationInstanceInfo;
import com.lanbo.doc.info.ClassInfo;
import com.lanbo.doc.info.FieldInfo;
import com.lanbo.doc.info.MethodInfo;
import com.lanbo.doc.info.TypeInfo;
import com.sun.javadoc.AnnotationTypeDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;

public class ConvertClass {

    public static void makeInfo(RootDoc r) {
        // create the objects
        ClassDoc[] classes = getClasses(r);
        for (ClassDoc c : classes) {
            Converter.obtainClass(c);
        }

        ArrayList<ClassInfo> classesNeedingInit2 = new ArrayList<ClassInfo>();

        int i;
        // fill in the fields that reference other classes
        while (mClassesNeedingInit.size() > 0) {
            i = mClassesNeedingInit.size() - 1;
            ClassNeedingInit clni = mClassesNeedingInit.get(i);
            mClassesNeedingInit.remove(i);

            ConvertClass.initClass(clni.c, clni.cl);
            classesNeedingInit2.add(clni.cl);
        }
        mClassesNeedingInit = null;
        for (ClassInfo cl : classesNeedingInit2) {
            cl.init2();
        }

        ConvertAnnotation.finishAnnotationValueInit();

        // fill in the "root" stuff
        mRootClasses = Converter.convertClasses(classes);
    }

    private static ClassInfo[] mRootClasses;

    public static ClassInfo[] rootClasses() {
      return mRootClasses;
    }

    public static ClassInfo[] allClasses() {
      return (ClassInfo[]) mClasses.all();
    }
    
    private static ClassDoc[] getClasses(RootDoc r) {
        ClassDoc[] classDocs = r.classes();
        ArrayList<ClassDoc> filtered = new ArrayList<ClassDoc>(classDocs.length);
        for (ClassDoc c : classDocs) {
            if (c.position() != null) {
                // Work around a javadoc bug in Java 7: We sometimes spuriously
                // receive duplicate top level ClassDocs with null positions and
                // no type information. Ignore them, since every ClassDoc must
                // have a non null position.

                filtered.add(c);
            }
        }

        ClassDoc[] filteredArray = new ClassDoc[filtered.size()];
        filtered.toArray(filteredArray);
        return filteredArray;
    }

    
    private static final MethodDoc[] EMPTY_METHOD_DOC = new MethodDoc[0];

    private static void initClass(ClassDoc c, ClassInfo cl) {
        MethodDoc[] annotationElements;
        if (c instanceof AnnotationTypeDoc) {
            annotationElements = ((AnnotationTypeDoc) c).elements();
        } else {
            annotationElements = EMPTY_METHOD_DOC;
        }
        cl.init(Converter.obtainType(c),
                new ArrayList<ClassInfo>(Arrays.asList(Converter.convertClasses(c.interfaces()))),
                new ArrayList<TypeInfo>(Arrays.asList(Converter.convertTypes(c.interfaceTypes()))),
                new ArrayList<ClassInfo>(Arrays.asList(Converter.convertClasses(c.innerClasses()))),
                new ArrayList<MethodInfo>(Arrays.asList(Converter.convertMethods(c
                        .constructors(false)))),
                new ArrayList<MethodInfo>(Arrays.asList(Converter.convertMethods(c.methods(false)))),
                new ArrayList<MethodInfo>(Arrays.asList(Converter
                        .convertMethods(annotationElements))),
                new ArrayList<FieldInfo>(Arrays.asList(Converter.convertFields(c.fields(false)))),
                new ArrayList<FieldInfo>(Arrays.asList(Converter.convertFields(c.enumConstants()))),
                Converter.obtainPackage(c.containingPackage()),
                obtainClass(c.containingClass()),
                obtainClass(c.superclass()),
                Converter.obtainType(c.superclassType()),
                new ArrayList<AnnotationInstanceInfo>(Arrays.asList(Converter
                        .convertAnnotationInstances(c.annotations()))));

        cl.setHiddenMethods(new ArrayList<MethodInfo>(Arrays.asList(Converter.getHiddenMethods(c
                .methods(false)))));
        cl.setRemovedMethods(new ArrayList<MethodInfo>(Arrays.asList(Converter.getRemovedMethods(c
                .methods(false)))));

        cl.setRemovedSelfMethods(new ArrayList<MethodInfo>(Converter.convertAllMethods(c
                .methods(false))));
        cl.setRemovedConstructors(new ArrayList<MethodInfo>(Converter.convertAllMethods(c
                .constructors(false))));
        cl.setRemovedSelfFields(new ArrayList<FieldInfo>(
                Converter.convertAllFields(c.fields(false))));
        cl.setRemovedEnumConstants(new ArrayList<FieldInfo>(Converter.convertAllFields(c
                .enumConstants())));

        cl.setNonWrittenConstructors(new ArrayList<MethodInfo>(Arrays.asList(Converter
                .convertNonWrittenConstructors(c.constructors(false)))));
        cl.init3(
                new ArrayList<TypeInfo>(Arrays.asList(Converter.convertTypes(c.typeParameters()))),
                new ArrayList<ClassInfo>(Arrays.asList(Converter.convertClasses(c
                        .innerClasses(false)))));
    }

    private static class ClassNeedingInit {
        ClassNeedingInit(ClassDoc c, ClassInfo cl) {
            this.c = c;
            this.cl = cl;
        }

        ClassDoc c;
        ClassInfo cl;
    }

    private static ArrayList<ClassNeedingInit> mClassesNeedingInit = new ArrayList<ClassNeedingInit>();

    public static ClassInfo[] convertClasses(ClassDoc[] classes) {
        if (classes == null)
            return null;
        int N = classes.length;
        ClassInfo[] result = new ClassInfo[N];
        for (int i = 0; i < N; i++) {
            result[i] = obtainClass(classes[i]);
        }
        return result;
    }

    public static ClassInfo obtainClass(String className) {
        return obtainClass(Converter.root.classNamed(className));
    }

    static ClassInfo obtainClass(ClassDoc o) {
        return (ClassInfo) mClasses.obtain(o);
    }

    private static DocInfoCache mClasses = new DocInfoCache() {
        @Override
        protected Object make(Object o) {
            ClassDoc c = (ClassDoc) o;
            ClassInfo cl = new ClassInfo(c, c.getRawCommentText(),
                    Converter.convertSourcePosition(c.position()), c.isPublic(), c.isProtected(),
                    c.isPackagePrivate(), c.isPrivate(), c.isStatic(), c.isInterface(),
                    c.isAbstract(), c.isOrdinaryClass(), c.isException(), c.isError(), c.isEnum(),
                    (c instanceof AnnotationTypeDoc), c.isFinal(), c.isIncluded(), c.name(),
                    c.qualifiedName(), c.qualifiedTypeName(), c.isPrimitive());
            if (mClassesNeedingInit != null) {
                mClassesNeedingInit.add(new ClassNeedingInit(c, cl));
            }
            return cl;
        }

        @Override
        protected void made(Object o, Object r) {
            if (mClassesNeedingInit == null) {
                initClass((ClassDoc) o, (ClassInfo) r);
                ((ClassInfo) r).init2();
            }
        }

        @Override
        ClassInfo[] all() {
            return mCache.values().toArray(new ClassInfo[mCache.size()]);
        }
    };
}
