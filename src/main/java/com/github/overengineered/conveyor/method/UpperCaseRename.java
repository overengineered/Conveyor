package com.github.overengineered.conveyor.method;

import java.util.Locale;

public class UpperCaseRename extends Rename {
    public UpperCaseRename() {
        super("_");
    }

    protected String transform(String value, int index) {
        return value.toUpperCase(Locale.US);
    }
}
