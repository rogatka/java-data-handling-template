package com.epam.izh.rd.online.repository;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SimpleFileRepository implements FileRepository {

    /**
     * Метод рекурсивно подсчитывает количество файлов в директории
     *
     * @param path путь до директори
     * @return файлов, в том числе скрытых
     */
    @Override
    public long countFilesInDirectory(String path) {
        if (path == null || path.equals("")) {
            return -1L;
        }
        long numberOfFiles = 0L;
        URL resource = this.getClass().getResource("/" + path);
        File directory = null;
        try {
            directory = Paths.get(resource.toURI()).toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (directory != null) {
            if (directory.isDirectory()) {
                for (File file : directory.listFiles()) {
                    numberOfFiles += countFilesInDirectory(path + "/" + file.getName());
                }
            } else {
                numberOfFiles++;
            }
        }
        return numberOfFiles;
    }

    /**
     * Метод рекурсивно подсчитывает количество папок в директории, считая корень
     *
     * @param path путь до директории
     * @return число папок
     */
    @Override
    public long countDirsInDirectory(String path) {
        if (path == null || path.equals("")) {
            return -1L;
        }
        long numberOfDirectories = 0L;
        URL resource = this.getClass().getResource("/" + path);
        File directory = null;
        try {
            directory = Paths.get(resource.toURI()).toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (directory != null) {
            if (directory.isDirectory()) {
                numberOfDirectories++;
                for (File file : directory.listFiles()) {
                    numberOfDirectories += countDirsInDirectory(path + "/" + file.getName());
                }
            }
        }
        return numberOfDirectories;
    }
    /**
     * Метод копирует все файлы с расширением .txt
     *
     * @param from путь откуда
     * @param to   путь куда
     */
    @Override
    public void copyTXTFiles(String from, String to) {
        if (from == null || to == null || from.equals("") || to.equals("")) {
            System.out.println("Unacceptable path");
            return;
        }
        File sourceDirectory = new File(from);
        File targetDirectory = new File(to);
        if (sourceDirectory.exists()) {
            if (sourceDirectory.isFile() && sourceDirectory.getName().endsWith(".txt")) {
                try {
                    if (!targetDirectory.exists()) {
                        targetDirectory.mkdirs();
                    }
                    Files.copy(sourceDirectory.toPath(), targetDirectory.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (sourceDirectory.isDirectory()) {
                for (File file : sourceDirectory.listFiles()) {
                    copyTXTFiles(file.getAbsolutePath(), targetDirectory + "/" + sourceDirectory.toPath().relativize(file.toPath()));
                }
            }
        }
    }

    /**
     * Метод создает файл на диске с расширением txt
     *
     * @param path путь до нового файла
     * @param name имя файла
     * @return был ли создан файл
     */

    //   тест этого метода не прошел проверку
    @Override
    public boolean createFile(String path, String name) {
        if (path == null || name == null || name.equals("")) {
            return false;
        }
        File file = new File("src/main/resources/" + path + "/" + name);
        File directory = new File("src/main/resources/" + path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Метод считывает тело файла .txt из папки src/main/resources
     *
     * @param fileName имя файла
     * @return контент
     */
    @Override
    public String readFileFromResources(String fileName) {
        if (fileName == null || fileName.equals("")) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        File file = new File("src/main/resources/" + fileName);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
