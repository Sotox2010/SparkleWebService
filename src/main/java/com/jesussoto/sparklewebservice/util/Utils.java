package com.jesussoto.sparklewebservice.util;

import java.io.File;

public class Utils {

    public static void listFilesInPath(String path) {
        System.out.println("LISTING FILES IN: " + path);

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
            System.out.println("File " + listOfFiles[i].getName());
          } else if (listOfFiles[i].isDirectory()) {
            System.out.println("Directory " + listOfFiles[i].getName());
          }
        }
    }
}
