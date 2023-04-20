package com.custom.http.client;

import java.lang.annotation.Annotation;

public class SkipCallbackExecutorImpl implements SkipCallbackExecutor {

    private static final SkipCallbackExecutor INSTANCE = new SkipCallbackExecutorImpl();

    public static Annotation[] ensurePresent(Annotation[] annotations) {
        if (Utils.Companion.isAnnotationPresent(annotations, SkipCallbackExecutor.class)) {
            return annotations;
        }

        Annotation[] newAnnotations = new Annotation[annotations.length + 1];
        // Place the skip annotation first since we're guaranteed to check for it in the call adapter.
        newAnnotations[0] = SkipCallbackExecutorImpl.INSTANCE;
        System.arraycopy(annotations, 0, newAnnotations, 1, annotations.length);
        return newAnnotations;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return SkipCallbackExecutor.class;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SkipCallbackExecutor;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "@" + SkipCallbackExecutor.class.getName() + "()";
    }

}
