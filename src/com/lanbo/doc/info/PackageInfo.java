package com.lanbo.doc.info;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.lanbo.doc.constant.DocConstant;
import com.lanbo.doc.convert.Converter;
import com.sun.javadoc.PackageDoc;

public class PackageInfo extends DocInfo implements ContainerInfo {
    public static final String DEFAULT_PACKAGE = "default package";

    public static final Comparator<PackageInfo> comparator = new Comparator<PackageInfo>() {
        public int compare(PackageInfo a, PackageInfo b) {
            return a.name().compareTo(b.name());
        }
    };

    public PackageInfo(PackageDoc pkg, String name, SourcePositionInfo position) {
        super(pkg.getRawCommentText(), position);
        if (isEmpty(name)) {
            mName = DEFAULT_PACKAGE;
        } else {
            mName = name;
        }

        mPackage = pkg;
        initializeMaps();
    }

    public PackageInfo(String name) {
        super("", null);
        mName = name;
        initializeMaps();
    }

    public PackageInfo(String name, SourcePositionInfo position) {
        super("", position);

        if (isEmpty(name)) {
            mName = "default package";
        } else {
            mName = name;
        }
        initializeMaps();
    }

    private void initializeMaps() {
        mAnnotationsMap = new HashMap<String, ClassInfo>();
        mInterfacesMap = new HashMap<String, ClassInfo>();
        mOrdinaryClassesMap = new HashMap<String, ClassInfo>();
        mEnumsMap = new HashMap<String, ClassInfo>();
        mExceptionsMap = new HashMap<String, ClassInfo>();
        mErrorsMap = new HashMap<String, ClassInfo>();
    }

    public String htmlPage() {
        String s = mName;
        s = s.replace('.', '/');
        s += "/package-summary.html";
        s = DocConstant.javadocDir + s;
        return s;
    }

    @Override
    public ContainerInfo parent() {
        return null;
    }

    @Override
    public boolean isHidden() {
        // / M: Override when processing internal. @{
        /*if (Doclava.processInternal()) {
            // Check ClassInfo type elements
            if (interfaces() != null && interfaces().length > 0) {
                return false;
            }
            if (ordinaryClasses() != null && ordinaryClasses().length > 0) {
                return false;
            }
            if (enums() != null && enums().length > 0) {
                return false;
            }
            if (exceptions() != null && exceptions().length > 0) {
                return false;
            }
            if (errors() != null && errors().length > 0) {
                return false;
            }
            return true;
        }*/
        // / @}
        if (mHidden == null) {
            if (hasHideComment()) {
                // We change the hidden value of the package if a class wants to
                // be not hidden.
                ClassInfo[][] types = new ClassInfo[][] { annotations(), interfaces(),
                        ordinaryClasses(), enums(), exceptions() };
                for (ClassInfo[] type : types) {
                    if (type != null) {
                        for (ClassInfo c : type) {
                            if (c.hasShowAnnotation()) {
                                mHidden = false;
                                return false;
                            }
                        }
                    }
                }
                mHidden = true;
            } else {
                mHidden = false;
            }
        }
        return mHidden;
    }

    @Override
    public boolean isRemoved() {
        if (mRemoved == null) {
            if (hasRemovedComment()) {
                // We change the removed value of the package if a class wants
                // to be not hidden.
                ClassInfo[][] types = new ClassInfo[][] { annotations(), interfaces(),
                        ordinaryClasses(), enums(), exceptions() };
                for (ClassInfo[] type : types) {
                    if (type != null) {
                        for (ClassInfo c : type) {
                            if (c.hasShowAnnotation()) {
                                mRemoved = false;
                                return false;
                            }
                        }
                    }
                }
                mRemoved = true;
            } else {
                mRemoved = false;
            }
        }

        return mRemoved;
    }

    @Override
    public boolean isHiddenOrRemoved() {
        return isHidden() || isRemoved();
    }

    /**
     * Used by ClassInfo to determine packages default visability before
     * annoations.
     */
    public boolean hasHideComment() {
        if (mHiddenByComment == null) {
            //if (Doclava.hiddenPackages.contains(mName)) {
            //    mHiddenByComment = true;
            //} else {
                mHiddenByComment = comment().isHidden();
            //}
        }
        return mHiddenByComment;
    }

    public boolean hasRemovedComment() {
        if (mRemovedByComment == null) {
            mRemovedByComment = comment().isRemoved();
        }

        return mRemovedByComment;
    }

    public boolean checkLevel() {
        // TODO should return false if all classes are hidden but the package
        // isn't.
        // We don't have this so I'm not doing it now.
        return !isHiddenOrRemoved();
    }

    public String name() {
        return mName;
    }

    public String qualifiedName() {
        return mName;
    }

    public TagInfo[] inlineTags() {
        return comment().tags();
    }

    public TagInfo[] firstSentenceTags() {
        return comment().briefTags();
    }

    /**
     * @param classes
     *            the Array of ClassInfo to be filtered
     * @return an Array of ClassInfo without any hidden or removed classes
     */
    public static ClassInfo[] filterHiddenAndRemoved(ClassInfo[] classes) {
        ArrayList<ClassInfo> out = new ArrayList<ClassInfo>();

        for (ClassInfo cl : classes) {
            if (!cl.isHiddenOrRemoved()) {
                out.add(cl);
            }
        }

        return out.toArray(new ClassInfo[0]);
    }

    public ClassInfo[] annotations() {
        if (mAnnotations == null) {
            mAnnotations = ClassInfo.sortByName(filterHiddenAndRemoved(Converter
                    .convertClasses(mPackage.annotationTypes())));
        }
        return mAnnotations;
    }

    public ClassInfo[] interfaces() {
        if (mInterfaces == null) {
            mInterfaces = ClassInfo.sortByName(filterHiddenAndRemoved(Converter
                    .convertClasses(mPackage.interfaces())));
        }
        return mInterfaces;
    }

    public ClassInfo[] ordinaryClasses() {
        if (mOrdinaryClasses == null) {
            mOrdinaryClasses = ClassInfo.sortByName(filterHiddenAndRemoved(Converter
                    .convertClasses(mPackage.ordinaryClasses())));
        }
        return mOrdinaryClasses;
    }

    public ClassInfo[] enums() {
        if (mEnums == null) {
            mEnums = ClassInfo.sortByName(filterHiddenAndRemoved(Converter.convertClasses(mPackage
                    .enums())));
        }
        return mEnums;
    }

    public ClassInfo[] exceptions() {
        if (mExceptions == null) {
            mExceptions = ClassInfo.sortByName(filterHiddenAndRemoved(Converter
                    .convertClasses(mPackage.exceptions())));
        }
        return mExceptions;
    }

    public ClassInfo[] errors() {
        if (mErrors == null) {
            mErrors = ClassInfo.sortByName(filterHiddenAndRemoved(Converter.convertClasses(mPackage
                    .errors())));
        }
        return mErrors;
    }

    // in hashed containers, treat the name as the key
    @Override
    public int hashCode() {
        return mName.hashCode();
    }

    private Boolean mHidden = null;
    private Boolean mHiddenByComment = null;
    private Boolean mRemoved = null;
    private Boolean mRemovedByComment = null;
    private String mName;
    private PackageDoc mPackage;
    private ClassInfo[] mAnnotations;
    private ClassInfo[] mInterfaces;
    private ClassInfo[] mOrdinaryClasses;
    private ClassInfo[] mEnums;
    private ClassInfo[] mExceptions;
    private ClassInfo[] mErrors;

    private HashMap<String, ClassInfo> mAnnotationsMap;
    private HashMap<String, ClassInfo> mInterfacesMap;
    private HashMap<String, ClassInfo> mOrdinaryClassesMap;
    private HashMap<String, ClassInfo> mEnumsMap;
    private HashMap<String, ClassInfo> mExceptionsMap;
    private HashMap<String, ClassInfo> mErrorsMap;

    public ClassInfo getClass(String className) {
        ClassInfo cls = mInterfacesMap.get(className);

        if (cls != null) {
            return cls;
        }

        cls = mOrdinaryClassesMap.get(className);

        if (cls != null) {
            return cls;
        }

        cls = mEnumsMap.get(className);

        if (cls != null) {
            return cls;
        }

        cls = mEnumsMap.get(className);

        if (cls != null) {
            return cls;
        }
        cls = mAnnotationsMap.get(className);

        if (cls != null) {
            return cls;
        }

        return mErrorsMap.get(className);
    }

    public void addAnnotation(ClassInfo cls) {
        cls.setPackage(this);
        mAnnotationsMap.put(cls.name(), cls);
    }

    public ClassInfo getAnnotation(String annotationName) {
        return mAnnotationsMap.get(annotationName);
    }

    public void addInterface(ClassInfo cls) {
        cls.setPackage(this);
        mInterfacesMap.put(cls.name(), cls);
    }

    public ClassInfo getInterface(String interfaceName) {
        return mInterfacesMap.get(interfaceName);
    }

    public ClassInfo getOrdinaryClass(String className) {
        return mOrdinaryClassesMap.get(className);
    }

    public void addOrdinaryClass(ClassInfo cls) {
        cls.setPackage(this);
        mOrdinaryClassesMap.put(cls.name(), cls);
    }

    public ClassInfo getEnum(String enumName) {
        return mEnumsMap.get(enumName);
    }

    public void addEnum(ClassInfo cls) {
        cls.setPackage(this);
        this.mEnumsMap.put(cls.name(), cls);
    }

    public ClassInfo getException(String exceptionName) {
        return mExceptionsMap.get(exceptionName);
    }

    public ClassInfo getError(String errorName) {
        return mErrorsMap.get(errorName);
    }

    // TODO: Leftovers from ApiCheck that should be better merged.
    private HashMap<String, ClassInfo> mClasses = new HashMap<String, ClassInfo>();

    public void addClass(ClassInfo cls) {
        cls.setPackage(this);
        mClasses.put(cls.name(), cls);
    }

    public HashMap<String, ClassInfo> allClasses() {
        return mClasses;
    }

    public boolean isConsistent(PackageInfo pInfo) {
        return isConsistent(pInfo, null);
    }

    /**
     * Creates the delta class by copying class signatures from original, but
     * use provided list of constructors and methods.
     */
    private ClassInfo createDeltaClass(ClassInfo original, ArrayList<MethodInfo> constructors,
            ArrayList<MethodInfo> methods) {
        ArrayList<FieldInfo> emptyFields = new ArrayList<>();
        ArrayList<ClassInfo> emptyClasses = new ArrayList<>();
        ArrayList<TypeInfo> emptyTypes = new ArrayList<>();
        ArrayList<MethodInfo> emptyMethods = new ArrayList<>();
        ClassInfo ret = new ClassInfo(null, original.getRawCommentText(), original.position(),
                original.isPublic(), original.isProtected(), original.isPackagePrivate(),
                original.isPrivate(), original.isStatic(), original.isInterface(),
                original.isAbstract(), original.isOrdinaryClass(), original.isException(),
                original.isError(), original.isEnum(), original.isAnnotation(), original.isFinal(),
                original.isIncluded(), original.name(), original.qualifiedName(),
                original.qualifiedTypeName(), original.isPrimitive());
        ArrayList<ClassInfo> interfaces = original.interfaces();
        // avoid providing null to init method, replace with empty array list
        // when needed
        if (interfaces == null) {
            interfaces = emptyClasses;
        }
        ArrayList<TypeInfo> interfaceTypes = original.interfaceTypes();
        if (interfaceTypes == null) {
            interfaceTypes = emptyTypes;
        }
        ArrayList<ClassInfo> innerClasses = original.innerClasses();
        if (innerClasses == null) {
            innerClasses = emptyClasses;
        }
        ArrayList<MethodInfo> annotationElements = original.annotationElements();
        if (annotationElements == null) {
            annotationElements = emptyMethods;
        }
        ArrayList<AnnotationInstanceInfo> annotations = original.annotations();
        if (annotations == null) {
            annotations = new ArrayList<>();
        }
        ret.init(original.type(), interfaces, interfaceTypes, innerClasses, constructors, methods,
                annotationElements, emptyFields /* fields */, emptyFields /* enum */,
                original.containingPackage(), original.containingClass(), original.superclass(),
                original.superclassType(), annotations);
        return ret;
    }

    /**
     * Check if packages are consistent, also record class deltas.
     * <p>
     * <ul>
     * class deltas are:
     * <li>brand new classes that are not present in current package
     * <li>stripped existing classes stripped where only newly added methods are
     * kept
     * 
     * @param pInfo
     * @param clsInfoDiff
     * @return
     */
    public boolean isConsistent(PackageInfo pInfo, List<ClassInfo> clsInfoDiff) {
        return isConsistent(pInfo, clsInfoDiff, null);
    }

    /**
     * Check if packages are consistent, also record class deltas.
     * <p>
     * <ul>
     * class deltas are:
     * <li>brand new classes that are not present in current package
     * <li>stripped existing classes stripped where only newly added methods are
     * kept
     * 
     * @param pInfo
     * @param clsInfoDiff
     * @param ignoredClasses
     * @return
     */
    public boolean isConsistent(PackageInfo pInfo, List<ClassInfo> clsInfoDiff,
            Collection<String> ignoredClasses) {
        boolean consistent = true;
        return consistent;
    }

    private static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
