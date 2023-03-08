package com.szymontracz.warehouse.podam;

import uk.co.jemos.podam.common.AttributeStrategy;

import java.lang.annotation.Annotation;
import java.util.List;

public class URLStringStrategy implements AttributeStrategy<String> {
    @Override
    public String getValue(Class<?> aClass, List<Annotation> list) {
        return "/one/two/three";
    }
}
