package com.github.overengineered.conveyor.method;

import java.util.Locale;

public class LowerCaseRename extends Rename {
    public LowerCaseRename() {
        super("_");
    }

    protected String transform(String value, int index) {
        return value.toLowerCase(Locale.US);
    }
}
