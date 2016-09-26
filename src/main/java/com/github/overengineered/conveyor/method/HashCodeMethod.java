package com.github.overengineered.conveyor.method;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import java.util.List;

public class HashCodeMethod implements TemplateMethodModel {

    public TemplateModel exec(List args) throws TemplateModelException {
        if (args.size() != 1) {
            throw new TemplateModelException("Wrong arguments");
        }
        String value = (String) args.get(0);
        return new SimpleScalar(String.valueOf(value.hashCode()));
    }
}
