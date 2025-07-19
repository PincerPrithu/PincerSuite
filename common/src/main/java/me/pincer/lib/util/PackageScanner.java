package me.pincer.lib.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PackageScanner {
    public static List<Class<?>> findClasses(String packageName, ClassLoader classLoader) {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');

        try {
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File dir = new File(resource.getFile());
                findClasses(dir, packageName, classes, classLoader);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    private static void findClasses(File dir, String packageName, List<Class<?>> classes, ClassLoader classLoader) {
        if (!dir.exists()) return;

        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                findClasses(file, packageName + "." + file.getName(), classes, classLoader);
            } else if (file.getName().endsWith(".class")) {
                try {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    classes.add(Class.forName(className, false, classLoader));
                } catch (ClassNotFoundException ignored) {}
            }
        }
    }
}