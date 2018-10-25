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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TypeInfo {
    public static final Set<String> PRIMITIVE_TYPES = Collections
            .unmodifiableSet(new HashSet<String>(Arrays.asList("boolean", "byte", "char", "double",
                    "float", "int", "long", "short", "void")));

    public TypeInfo(boolean isPrimitive, String dimension, String simpleTypeName,
            String qualifiedTypeName, ClassInfo cl) {
        mIsPrimitive = isPrimitive;
        mDimension = dimension;
        mSimpleTypeName = simpleTypeName;
        mQualifiedTypeName = qualifiedTypeName;
        mClass = cl;
    }

    public TypeInfo(String typeString) {
        System.out.println("typeString: " + typeString);
        // VarArgs
        if (typeString.endsWith("...")) {
            typeString = typeString.substring(0, typeString.length() - 3);
        }

        // Generic parameters
        int paramStartPos = typeString.indexOf('<');
        if (paramStartPos > -1) {
            ArrayList<TypeInfo> generics = new ArrayList<TypeInfo>();
            int paramEndPos = typeString.lastIndexOf('>');

            int entryStartPos = paramStartPos + 1;
            int bracketNesting = 0;
            for (int i = entryStartPos; i < paramEndPos; i++) {
                char c = typeString.charAt(i);
                if (c == ',' && bracketNesting == 0) {
                    String entry = typeString.substring(entryStartPos, i).trim();
                    TypeInfo info = new TypeInfo(entry);
                    generics.add(info);
                    entryStartPos = i + 1;
                } else if (c == '<') {
                    bracketNesting++;
                } else if (c == '>') {
                    bracketNesting--;
                }
            }

            TypeInfo info = new TypeInfo(typeString.substring(entryStartPos, paramEndPos).trim());
            generics.add(info);

            mTypeArguments = generics;

            if (paramEndPos < typeString.length() - 1) {
                typeString = typeString.substring(0, paramStartPos)
                        + typeString.substring(paramEndPos + 1);
            } else {
                typeString = typeString.substring(0, paramStartPos);
            }
        }

        // Dimensions
        int pos = typeString.indexOf('[');
        if (pos > -1) {
            mDimension = typeString.substring(pos);
            typeString = typeString.substring(0, pos);
        } else {
            mDimension = "";
        }

        if (PRIMITIVE_TYPES.contains(typeString)) {
            mIsPrimitive = true;
            mSimpleTypeName = typeString;
            mQualifiedTypeName = typeString;
        } else {
            mQualifiedTypeName = typeString;
            pos = typeString.lastIndexOf('.');
            if (pos > -1) {
                mSimpleTypeName = typeString.substring(pos + 1);
            } else {
                mSimpleTypeName = typeString;
            }
            System.out.println("SimeTypeName = " + mSimpleTypeName);
        }
    }

    /**
     * Copy Constructor.
     */
    private TypeInfo(TypeInfo other) {
        mIsPrimitive = other.isPrimitive();
        mIsTypeVariable = other.isTypeVariable();
        mIsWildcard = other.isWildcard();
        mDimension = other.dimension();
        mSimpleTypeName = other.simpleTypeName();
        mQualifiedTypeName = other.qualifiedTypeName();
        mClass = other.asClassInfo();
        if (other.typeArguments() != null) {
            mTypeArguments = new ArrayList<TypeInfo>(other.typeArguments());
        }
        if (other.superBounds() != null) {
            mSuperBounds = new ArrayList<TypeInfo>(other.superBounds());
        }
        if (other.extendsBounds() != null) {
            mExtendsBounds = new ArrayList<TypeInfo>(other.extendsBounds());
        }
        mFullName = other.fullName();
    }

    public ClassInfo asClassInfo() {
        return mClass;
    }

    public boolean isPrimitive() {
        return mIsPrimitive;
    }

    public String dimension() {
        return mDimension;
    }

    public void setDimension(String dimension) {
        mDimension = dimension;
    }

    public String simpleTypeName() {
        return mSimpleTypeName;
    }

    public String qualifiedTypeName() {
        return mQualifiedTypeName;
    }

    public String fullName() {
        if (mFullName != null) {
            return mFullName;
        } else {
            return fullName(new HashSet<String>());
        }
    }

    public static String typeArgumentsName(ArrayList<TypeInfo> args, HashSet<String> typeVars) {
        String result = "<";

        int i = 0;
        for (TypeInfo arg : args) {
            result += arg.fullName(typeVars);
            if (i != (args.size() - 1)) {
                result += ", ";
            }
            i++;
        }
        result += ">";
        return result;
    }

    public String fullName(HashSet<String> typeVars) {
        mFullName = fullNameNoDimension(typeVars) + mDimension;
        return mFullName;
    }

    public String fullNameNoDimension(HashSet<String> typeVars) {
        String fullName = null;
        if (mIsTypeVariable) {
            if (typeVars.contains(mQualifiedTypeName)) {
                // don't recurse forever with the parameters. This handles
                // Enum<K extends Enum<K>>
                return mQualifiedTypeName;
            }
            typeVars.add(mQualifiedTypeName);
        }
        /*
         * if (fullName != null) { return fullName; }
         */
        fullName = mQualifiedTypeName;
        if (mTypeArguments != null && !mTypeArguments.isEmpty()) {
            fullName += typeArgumentsName(mTypeArguments, typeVars);
        } else if (mSuperBounds != null && !mSuperBounds.isEmpty()) {
            for (TypeInfo superBound : mSuperBounds) {
                if (superBound == mSuperBounds.get(0)) {
                    fullName += " super " + superBound.fullName(typeVars);
                } else {
                    fullName += " & " + superBound.fullName(typeVars);
                }
            }
        } else if (mExtendsBounds != null && !mExtendsBounds.isEmpty()) {
            for (TypeInfo extendsBound : mExtendsBounds) {
                if (extendsBound == mExtendsBounds.get(0)) {
                    fullName += " extends " + extendsBound.fullName(typeVars);
                } else {
                    fullName += " & " + extendsBound.fullName(typeVars);
                }
            }
        }
        return fullName;
    }

    public ArrayList<TypeInfo> typeArguments() {
        return mTypeArguments;
    }

    public void setTypeArguments(ArrayList<TypeInfo> args) {
        mTypeArguments = args;
    }

    public void addTypeArgument(TypeInfo arg) {
        if (mTypeArguments == null) {
            mTypeArguments = new ArrayList<TypeInfo>();
        }

        mTypeArguments.add(arg);
    }

    public void setBounds(ArrayList<TypeInfo> superBounds, ArrayList<TypeInfo> extendsBounds) {
        mSuperBounds = superBounds;
        mExtendsBounds = extendsBounds;
    }

    public ArrayList<TypeInfo> superBounds() {
        return mSuperBounds;
    }

    public ArrayList<TypeInfo> extendsBounds() {
        return mExtendsBounds;
    }

    public void setIsTypeVariable(boolean b) {
        mIsTypeVariable = b;
    }

    public void setIsWildcard(boolean b) {
        mIsWildcard = b;
    }

    public boolean isWildcard() {
        return mIsWildcard;
    }

    static HashSet<String> typeVariables(ArrayList<TypeInfo> params) {
        return typeVariables(params, new HashSet<String>());
    }

    static HashSet<String> typeVariables(ArrayList<TypeInfo> params, HashSet<String> result) {
        if (params != null) {
            for (TypeInfo t : params) {
                if (t.mIsTypeVariable) {
                    result.add(t.mQualifiedTypeName);
                }
            }
        }
        return result;
    }

    public boolean isTypeVariable() {
        return mIsTypeVariable;
    }

    public String defaultValue() {
        if (mIsPrimitive) {
            if ("boolean".equals(mSimpleTypeName)) {
                return "false";
            } else {
                return "0";
            }
        } else {
            return "null";
        }
    }

    @Override
    public String toString() {
        String returnString = "";
        returnString += "Primitive?: " + mIsPrimitive + " TypeVariable?: " + mIsTypeVariable
                + " Wildcard?: " + mIsWildcard + " Dimension: " + mDimension
                + " QualifedTypeName: " + mQualifiedTypeName + " SimpleTypeName: "
                + mSimpleTypeName;

        if (mTypeArguments != null && mTypeArguments.size() > 0) {
            returnString += "\nTypeArguments: ";
            for (TypeInfo tA : mTypeArguments) {
                returnString += tA.qualifiedTypeName() + "(" + tA + ") ";
            }
        }
        if (mSuperBounds != null) {
            returnString += "\nSuperBounds: ";
            for (TypeInfo tA : mSuperBounds) {
                returnString += tA.qualifiedTypeName() + "(" + tA + ") ";
            }
        }
        if (mExtendsBounds != null) {
            returnString += "\nExtendsBounds: ";
            for (TypeInfo tA : mExtendsBounds) {
                returnString += tA.qualifiedTypeName() + "(" + tA + ") ";
            }
        }
        return returnString;
    }

    /**
     * Copy this TypeInfo, but replace type arguments with those defined in the
     * typeArguments mapping.
     * <p>
     * If the current type is one of the base types in the mapping (i.e. a
     * parameter itself) then this returns the mapped type.
     */
    public TypeInfo getTypeWithArguments(Map<String, TypeInfo> typeArguments) {
        if (typeArguments.containsKey(fullName())) {
            return typeArguments.get(fullName());
        }

        TypeInfo ti = new TypeInfo(this);
        if (typeArguments() != null) {
            ArrayList<TypeInfo> newArgs = new ArrayList<TypeInfo>();
            for (TypeInfo t : typeArguments()) {
                newArgs.add(t.getTypeWithArguments(typeArguments));
            }
            ti.setTypeArguments(newArgs);
        }
        return ti;
    }

    /**
     * Given two TypeInfos that reference the same type, take the first one's
     * type parameters and generate a mapping from their names to the type
     * parameters defined in the second.
     */
    public static Map<String, TypeInfo> getTypeArgumentMapping(TypeInfo generic, TypeInfo typed) {
        Map<String, TypeInfo> map = new HashMap<String, TypeInfo>();
        // / M: generic and generic.typeArguments maybe null.
        if (generic != null && generic.typeArguments() != null) {
            for (int i = 0; i < generic.typeArguments().size(); i++) {
                if (typed.typeArguments() != null && typed.typeArguments().size() > i) {
                    map.put(generic.typeArguments().get(i).fullName(), typed.typeArguments().get(i));
                }
            }
        }
        return map;
    }

    /**
     * Given a ClassInfo and a parameterized TypeInfo, take the class's raw
     * type's type parameters and generate a mapping from their names to the
     * type parameters defined in the TypeInfo.
     */
    public static Map<String, TypeInfo> getTypeArgumentMapping(ClassInfo cls, TypeInfo typed) {
        return getTypeArgumentMapping(cls.asTypeInfo(), typed);
    }

    private boolean mIsPrimitive;
    private boolean mIsTypeVariable;
    private boolean mIsWildcard;
    private String mDimension;
    private String mSimpleTypeName;
    private String mQualifiedTypeName;
    private ClassInfo mClass;
    private ArrayList<TypeInfo> mTypeArguments;
    private ArrayList<TypeInfo> mSuperBounds;
    private ArrayList<TypeInfo> mExtendsBounds;
    private String mFullName;
}
