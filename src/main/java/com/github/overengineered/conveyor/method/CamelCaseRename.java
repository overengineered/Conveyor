package com.github.overengineered.conveyor.method;

import java.util.Locale;

public class CamelCaseRename extends Rename {
    public CamelCaseRename() {
        super("");
    }

    protected String transform(String value, int index) {
        if (index == 0)
            return value.toLowerCase(Locale.US);
        return org.apache.commons.lang3.text.WordUtils.capitalizeFully(value);
    }
}
