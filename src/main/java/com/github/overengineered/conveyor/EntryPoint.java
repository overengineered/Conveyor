package com.github.overengineered.conveyor;

import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

public class EntryPoint {
    public static void main(String[] args) {
        if (args.length < 2) {
            printHelp();
            return;
        }
        else if (!"update".equals(args[0])) {
            printHelp();
            return;
        }

        Engine engine = new Engine();
        for (int i = 1, n = args.length; i < n; i++) {
            try {
                engine.update(new File(args[i]));
            }
            catch (IOException | TemplateException e) {
                System.out.println("Cannot update " + args[i] + ": " + e);
            }
        }
    }

    private static void printHelp() {
        System.out.println("Usage: update <file-list>");
    }
}
