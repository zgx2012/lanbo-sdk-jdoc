package com.lanbo.doc.convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Type;

public class Converter {
	
	
	
	private abstract static class Cache {
		void put(Object key, Object value) {
			mCache.put(key, value);
		}

		Object obtain(Object o) {
			if (o == null) {
				return null;
			}
			Object k = keyFor(o);
			Object r = mCache.get(k);
			if (r == null) {
				r = make(o);
				mCache.put(k, r);
				made(o, r);
			}
			return r;
		}

		protected HashMap<Object, Object> mCache = new HashMap<Object, Object>();

		protected abstract Object make(Object o);

		protected void made(Object o, Object r) {
		}

		protected Object keyFor(Object o) {
			return o;
		}

		Object[] all() {
			return null;
		}
	}

	private static Cache mTypes = new Cache() {
		@Override
		protected Object make(Object o) {
			Type t = (Type) o;
			String simpleTypeName;
			if (t instanceof ClassDoc) {
				simpleTypeName = ((ClassDoc) t).name();
			} else {
				simpleTypeName = t.simpleTypeName();
			}
			TypeInfo ti = new TypeInfo(t.isPrimitive(), t.dimension(),
					simpleTypeName, t.qualifiedTypeName(), null);
			return ti;
		}

		@Override
		protected void made(Object o, Object r) {
			Type t = (Type) o;
			TypeInfo ti = (TypeInfo) r;
			if (t.asParameterizedType() != null) {
				ti.setTypeArguments(new ArrayList<TypeInfo>(Arrays
						.asList(Converter.convertTypes(t.asParameterizedType()
								.typeArguments()))));
			} else if (t instanceof ClassDoc) {
				ti.setTypeArguments(new ArrayList<TypeInfo>(Arrays
						.asList(Converter.convertTypes(((ClassDoc) t)
								.typeParameters()))));
			} else if (t.asTypeVariable() != null) {
				ti.setBounds(
						null,
						new ArrayList<TypeInfo>(Arrays.asList(Converter
								.convertTypes((t.asTypeVariable().bounds())))));
				ti.setIsTypeVariable(true);
			} else if (t.asWildcardType() != null) {
				ti.setIsWildcard(true);
				ti.setBounds(
						new ArrayList<TypeInfo>(
								Arrays.asList(Converter.convertTypes(t
										.asWildcardType().superBounds()))),
						new ArrayList<TypeInfo>(Arrays.asList(Converter
								.convertTypes(t.asWildcardType()
										.extendsBounds()))));
			}
		}

		@Override
		protected Object keyFor(Object o) {
			Type t = (Type) o;
			String keyString = o.getClass().getName() + "/" + o.toString()
					+ "/";
			if (t.asParameterizedType() != null) {
				keyString += t.asParameterizedType().toString() + "/";
				if (t.asParameterizedType().typeArguments() != null) {
					for (Type ty : t.asParameterizedType().typeArguments()) {
						keyString += ty.toString() + "/";
					}
				}
			} else {
				keyString += "NoParameterizedType//";
			}
			if (t.asTypeVariable() != null) {
				keyString += t.asTypeVariable().toString() + "/";
				if (t.asTypeVariable().bounds() != null) {
					for (Type ty : t.asTypeVariable().bounds()) {
						keyString += ty.toString() + "/";
					}
				}
			} else {
				keyString += "NoTypeVariable//";
			}
			if (t.asWildcardType() != null) {
				keyString += t.asWildcardType().toString() + "/";
				if (t.asWildcardType().superBounds() != null) {
					for (Type ty : t.asWildcardType().superBounds()) {
						keyString += ty.toString() + "/";
					}
				}
				if (t.asWildcardType().extendsBounds() != null) {
					for (Type ty : t.asWildcardType().extendsBounds()) {
						keyString += ty.toString() + "/";
					}
				}
			} else {
				keyString += "NoWildCardType//";
			}

			return keyString;
		}
	};

	public static TypeInfo[] convertTypes(Type[] p) {
		if (p == null)
			return null;
		int len = p.length;
		TypeInfo[] q = new TypeInfo[len];
		for (int i = 0; i < len; i++) {
			q[i] = Converter.obtainType(p[i]);
		}
		return q;
	}

	public static TypeInfo obtainType(Type o) {
		return (TypeInfo) mTypes.obtain(o);
	}

	public static TypeInfo obtainTypeFromString(String type) {
		return (TypeInfo) mTypesFromString.obtain(type);
	}

	private static final Cache mTypesFromString = new Cache() {
		@Override
		protected Object make(Object o) {
			String name = (String) o;
			return new TypeInfo(name);
		}

		@Override
		protected void made(Object o, Object r) {

		}

		@Override
		protected Object keyFor(Object o) {
			return o;
		}
	};
	
	/**
	static ClassInfo obtainClass(ClassDoc o) {
		return (ClassInfo) mClasses.obtain(o);
	}

	private static Cache mClasses = new Cache() {
		@Override
		protected Object make(Object o) {
			ClassDoc c = (ClassDoc) o;
			ClassInfo cl = new ClassInfo(c, c.getRawCommentText(),
					Converter.convertSourcePosition(c.position()),
					c.isPublic(), c.isProtected(), c.isPackagePrivate(),
					c.isPrivate(), c.isStatic(), c.isInterface(),
					c.isAbstract(), c.isOrdinaryClass(), c.isException(),
					c.isError(), c.isEnum(), (c instanceof AnnotationTypeDoc),
					c.isFinal(), c.isIncluded(), c.name(), c.qualifiedName(),
					c.qualifiedTypeName(), c.isPrimitive());
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
	};*/
}
