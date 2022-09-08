package com.college.os_project.model.filesystem;

import com.college.os_project.model.Bootloader;
import com.college.os_project.model.processor.Process;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileSystem {
    private static File rootFolder;
    private static File currentFolder;
    private TreeItem<File> treeItem;

    public FileSystem(File path) {
        rootFolder = path;
        currentFolder = rootFolder;
        treeItem = new TreeItem<>(rootFolder);
        createTree(treeItem);
    }

    public void createTree(TreeItem<File> rootItem) {
        try (DirectoryStream<Path> directoryStream = Files
                .newDirectoryStream(Paths.get(rootItem.getValue().getAbsolutePath()))) {
            for (Path path : directoryStream) {
                TreeItem<File> newItem = new TreeItem<>(path.toFile());
                newItem.setExpanded(false);
                rootItem.getChildren().add(newItem);
                if (Files.isDirectory(path))
                    createTree(newItem);
                else {
                    byte[] content = Files.readAllBytes(newItem.getValue().toPath());
                    FileInMemory newFile = new FileInMemory(newItem.getValue().getName(), content);
                    if (!Bootloader.getMemory().contains(newItem.getValue().getName()))
                        Bootloader.getMemory().save(newFile);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createFile(Process process) {
        if (process.getOutputFileName() != null) {
            try {
                File newFile = new File(process.getFilePath().getParent() + "/" + process.getOutputFileName() + ".txt");
                FileWriter fw = new FileWriter(newFile);
                fw.write("Rezultat izvrsavanja za " + process.getName() + " : " + process.getProcessOutput());
                fw.close();
            } catch (IOException e) {
                System.out.println("Error while creating file");
            }
        }
    }

    public TreeItem<File> getTreeItem() {
        treeItem = new TreeItem<>(currentFolder);
        createTree(treeItem);
        return treeItem;
    }

    public static void listFiles() {
        System.out.println("Content of: " + currentFolder.getName());
        System.out.println("Type\tName\t\t\tSize");
        for (TreeItem<File> file : Bootloader.getTree().getTreeItem().getChildren()) {
            byte[] fileContent = null;
            try {
                if (!file.getValue().isDirectory())
                    fileContent = Files.readAllBytes(file.getValue().toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file.getValue().isDirectory()) {
                System.out.println("Folder \t" + file.getValue().getName());
            } else {
                System.out.println("File \t" + file.getValue().getName() + "\t\t" + fileContent.length + " B");
            }
        }
    }

    public static void changeDirectory(String directory) {
        if (directory.equals("..") && !currentFolder.equals(rootFolder))
            currentFolder = currentFolder.getParentFile();
        else {
            for (TreeItem<File> file : Bootloader.getTree().getTreeItem().getChildren()) {
                if (file.getValue().getName().equals(directory) && file.getValue().isDirectory())
                    currentFolder = file.getValue();
            }
        }
    }

    public static void makeDirectory(String directory) {
        File folder = new File(currentFolder.getAbsolutePath() + "/" + directory);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    public static void deleteDirectory(String directory) {
        for (TreeItem<File> file : Bootloader.getTree().getTreeItem().getChildren()) {
            if (file.getValue().getName().equals(directory) && file.getValue().isDirectory())
                file.getValue().delete();
        }
    }

    public static void renameDirectory(String newName, String oldName) {
        for (TreeItem<File> file : Bootloader.getTree().getTreeItem().getChildren()) {
            if (file.getValue().getName().equals(oldName) && file.getValue().isDirectory())
                file.getValue().renameTo(new File(currentFolder.getAbsolutePath() + "/" + newName));
        }
    }

    public static void deleteFile(String name) {
        for (TreeItem<File> file : Bootloader.getTree().getTreeItem().getChildren()) {
            if (file.getValue().getName().equals(name) && !file.getValue().isDirectory())
                file.getValue().delete();
            if (Bootloader.getMemory().contains(name)) {
                Bootloader.getMemory().deleteFile(Bootloader.getMemory().getFile(name));
            }
        }
    }

    public File getCurrentFolder() {
        return currentFolder;
    }
}
