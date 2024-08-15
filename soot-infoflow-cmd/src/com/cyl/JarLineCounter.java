package com.cyl;
import soot.*;
import soot.options.Options;
import soot.util.Chain;

import java.io.File;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import java.util.Enumeration;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JarLineCounter {
    public static void main(String[] args) {
        args= new String[]{"E:\\Jar\\kafka"};
        if (args.length != 1) {
            System.out.println("Usage: java JarLineCounter <directory>");
            System.exit(1);
        }

        String directoryPath = args[0];
        File dir = new File(directoryPath);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("The provided path is not a directory.");
            System.exit(1);
        }

        int totalLines = 0;
        File[] jarFiles = dir.listFiles((dir1, name) -> name.endsWith(".jar"));

        for (File jarFile : jarFiles) {
            totalLines += countLinesInJar(jarFile);
        }

        System.out.println("Total lines in all JAR files: " + totalLines);
    }

    private static int countLinesInJar(File jarFile) {
        int lines = 0;

        try {
            JarFile jar = new JarFile(jarFile);
            Enumeration<JarEntry> entries = jar.entries();

            List<String> classNames = new ArrayList<>();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    System.out.println(entry.getName());
//                    classNames.add(entry.getName().replace("/", ".").replace(".class", ""));
                }
            }

            for (String className : classNames) {
                SootClass sootClass = loadClassFromJar(jarFile, className);
                lines += countLinesInClass(sootClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lines;
    }

    private static SootClass loadClassFromJar(File jarFile, String className) {
        Options.v().set_prepend_classpath(true);
        Options.v().set_process_dir(List.of(jarFile.getAbsolutePath()));
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_output_format(Options.output_format_none);

        Scene.v().loadNecessaryClasses();
        return Scene.v().forceResolve(className, SootClass.BODIES);
    }

    private static int countLinesInClass(SootClass sootClass) {
        int lines = 0;
        for (SootMethod method : sootClass.getMethods()) {
            Body body = method.retrieveActiveBody();
            for (Unit unit : body.getUnits()) {
                lines += countLinesInUnit(unit);
            }
        }
        return lines;
    }

    private static int countLinesInUnit(Unit unit) {
        String[] lines = unit.toString().split("\n");
        return lines.length;
    }
}