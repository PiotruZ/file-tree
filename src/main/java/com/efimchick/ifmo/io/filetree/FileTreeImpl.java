package com.efimchick.ifmo.io.filetree;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FileTreeImpl implements FileTree {
    String bytes=" bytes";

    @Override
    public Optional<String> tree(Path path) {
        File file = new File(String.valueOf(path));
        if (!file.exists()) return Optional.empty();
        if (file.isFile()) {
            return Optional.of(file.getName()+" "+file.length()+bytes); // print file name and size
        }
        if (file.isDirectory()) {
            return Optional.of(directoryTree(file, new ArrayList<>())); // call method to print a tree
        }
        return Optional.empty();
    }

    private String directoryTree(File folder, List<Boolean> lastFolders) {

        StringBuilder dir = new StringBuilder();
        if (lastFolders.size() != 0) //list has size 0 only for when appending name and size of the first folder
            dir.append(!(lastFolders.get(lastFolders.size() - 1)) ? "├─ " : "└─ "); // if the given folder is last one in the previous folder add "└─ " otherwise "├─ "
        dir.append(folder.getName()).append(" ").append(folderSize(folder)); // add folder name and size to dir String

        File[] files = folder.listFiles();
        assert files != null;
        int count = files.length; // number of files in a folder
        files = sortFiles(files); // sort the files
        for (int i =0; i<count; i++) {
            dir.append("\n");
            for (Boolean lastFolder : lastFolders) {
                if (lastFolder) { // checks if the folder is the last file in the folder
                    dir.append("   "); // if yes then there's no line to the next folder
                }
                else {
                    dir.append("│  ");
                }
            }
            if (files[i].isFile()) {
                dir.append(i + 1 == count ? "└" : "├").append("─ ").append(files[i].getName()).append(" ").append(files[i].length()).append(bytes); // if next file is last one in a folder print "└─" if it's not last print "├─" then its name and size
            } else {
                lastFolders.add(i+1==count); // if next folder is last file in the present folder add true to the list
                dir.append(directoryTree(files[i], lastFolders)); // call recursively to add the structure of the folder to dir
                lastFolders.remove(lastFolders.size()-1); // remove boolean previously added for recursive call
            }
        }
        return dir.toString();
    }

    private long getFolderSize(File folder) {
        long size = 0;
        File[] files = folder.listFiles();

        assert files != null;

        for (File file : files) {
            if (file.isFile()) {
                size += file.length(); //get file size if it's a file and add to size variable
            } else {
                size += getFolderSize(file); // call method recursively if it's a folder to get a size of this folder
            }
        }
        return size;
    }
    private String folderSize(File folder) {
        return getFolderSize(folder) + bytes;
    }
    private File[] sortFiles(File[] folder) {

        Arrays.sort(folder);
        List<File> sorted = new ArrayList<>();

        for (File file : folder) {
            if (file.isDirectory()) sorted.add(file);
        }

        for (File file : folder) {
            if (file.isFile()) sorted.add(file);
        }

        return sorted.toArray(new File[0]);
    }
}

