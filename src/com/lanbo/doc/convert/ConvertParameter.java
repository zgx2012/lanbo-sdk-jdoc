package com.lanbo.doc.convert;

import java.util.Arrays;

import com.lanbo.doc.info.ParameterInfo;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.SourcePosition;

public class ConvertParameter {

    public static ParameterInfo convertParameter(Parameter p, SourcePosition pos, boolean isVarArg) {
        if (p == null)
            return null;
        ParameterInfo pi = new ParameterInfo(p.name(), p.typeName(),
                Converter.obtainType(p.type()), isVarArg, Converter.convertSourcePosition(pos),
                Arrays.asList(Converter.convertAnnotationInstances(p.annotations())));
        return pi;
    }

    public static ParameterInfo[] convertParameters(Parameter[] p, ExecutableMemberDoc m) {
        SourcePosition pos = m.position();
        int len = p.length;
        ParameterInfo[] q = new ParameterInfo[len];
        for (int i = 0; i < len; i++) {
            boolean isVarArg = (m.isVarArgs() && i == len - 1);
            q[i] = Converter.convertParameter(p[i], pos, isVarArg);
        }
        return q;
    }
}
