package com.github.overengineered.conveyor.method;

public class TitleCaseRename extends Rename {
    public TitleCaseRename() {
        super("");
    }

    protected String transform(String value, int index) {
        return org.apache.commons.lang3.text.WordUtils.capitalizeFully(value);
    }
}
