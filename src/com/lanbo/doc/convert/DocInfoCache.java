package com.lanbo.doc.convert;

import java.util.HashMap;

/**
 * obtain触发make和made.
 */
public abstract class DocInfoCache {
    void put(Object key, Object value) {
        mCache.put(key, value);
    }

    /**
     * obtain触发make和made.
     */
    public Object obtain(Object o) {
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
