package com.lanbo.doc.convert;

import com.lanbo.doc.info.PackageInfo;
import com.sun.javadoc.PackageDoc;

public class ConvertPackage {

    public static PackageInfo obtainPackage(PackageDoc o) {
        return (PackageInfo) mPackagees.obtain(o);
    }

    private static DocInfoCache mPackagees = new DocInfoCache() {
        @Override
        protected Object make(Object o) {
            PackageDoc p = (PackageDoc) o;
            return new PackageInfo(p, p.name(), Converter.convertSourcePosition(p.position()));
        }
    };

    public static PackageInfo obtainPackage(String packageName) {
        return Converter.obtainPackage(Converter.root.packageNamed(packageName));
    }

}
