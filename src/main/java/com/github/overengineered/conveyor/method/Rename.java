package com.github.overengineered.conveyor.method;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import java.util.ArrayList;
import java.util.List;

public abstract class Rename implements TemplateMethodModel {
    private final String mDelimiter;

    protected Rename(String delimiter) {
        mDelimiter = delimiter;
    }

    public TemplateModel exec(List args) throws TemplateModelException {
        if (args.size() < 1 || args.size() > 2) {
            throw new TemplateModelException("Wrong arguments");
        }
        String name = (String) args.get(0);
        String delimiter = args.size() > 1 ? (String) args.get(1) : mDelimiter;
        String[] words = transform(getWords(name));
        return new SimpleScalar(String.join(delimiter, words));
    }

    protected abstract String transform(String value, int index);

    private String[] transform(String[] words) {
        String[] result = new String[words.length];
        for (int i = 0, n = words.length; i < n; i++) {
            result[i] = transform(words[i], i);
        }
        return result;
    }

    private static String[] getWords(String name) {
        // camelCase -> [camel, Case]
        // lower_case -> [lower, case]
        // IOException -> [IOException]
        // Point3d -> [Point, 3d]
        // func2 -> [func2]

        int minimumAcceptableValue = 0;
        // -1 - invalid characters
        //  0 - decimal digits
        //  1 - upper case letters
        //  2 - title case letters
        //  3 - any valid Java identifier part except _
        List<String> words = new ArrayList<>();
        StringBuilder wordBuilder = new StringBuilder();

        for (int i = 0, n = name.length(), codePoint, size; i < n; i += size) {
            codePoint = Character.codePointAt(name, i);
            size = Character.charCount(codePoint);

            int codePointValue;
            if (!Character.isJavaIdentifierPart(codePoint) || codePoint == '_') {
                codePointValue = -1;
            }
            else if (Character.getType(codePoint) == Character.DECIMAL_DIGIT_NUMBER) {
                codePointValue = 0;
            }
            else if (Character.isUpperCase(codePoint)) {
                codePointValue = 1;
            }
            else if (Character.isTitleCase(codePoint)) {
                codePointValue = 2;
            }
            else {
                codePointValue = 3;
            }

            if (codePointValue < minimumAcceptableValue) {
                // cp starts a new word
                if (wordBuilder.length() > 0) {
                    words.add(wordBuilder.toString());
                    wordBuilder.delete(0, wordBuilder.length());
                    minimumAcceptableValue = 0;
                }
            }

            if (codePointValue >= 0) {
                wordBuilder.append(name, i, i + size);
                minimumAcceptableValue = codePointValue;
                if (codePointValue == 2)
                    minimumAcceptableValue = 3;
            }
        }

        if (wordBuilder.length() > 0) {
            if (minimumAcceptableValue == 0 && words.size() > 0) {
                // append number to last word
                int index = words.size() - 1;
                wordBuilder.insert(0, words.get(index));
                words.set(index, wordBuilder.toString());
            }
            else {
                words.add(wordBuilder.toString());
            }
        }

        return words.toArray(new String[words.size()]);
    }
}
