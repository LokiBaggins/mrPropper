package by.baggins;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FileMocker {
    private String resourceDirPath;

    public FileMocker(String resourceDirPath) {
        this.resourceDirPath = resourceDirPath;
    }



    //    correct files, with duplicates
    public File createValidFile1(String fileName) {
        File resultFile = new File(this.resourceDirPath + fileName);

        try {
//            if file not exists trying to create it and throwing exception if failed
            if (!resultFile.exists() && !resultFile.createNewFile()){
                throw new RuntimeException("Error while creating mock file " + fileName);
            }

            PrintWriter writer = new PrintWriter(resultFile, "UTF-8");
            writer.println("p1=one");
            writer.println("p2=two");
            writer.println("p3=three");
            writer.close();

            System.out.println("Mock file created: " + resultFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }


        return resultFile;
    }

    public File createValidFile2(String fileName) {
        File resultFile = new File(this.resourceDirPath + fileName);

        try {
//            if file not exists trying to create it and throwing exception if failed
            if (!resultFile.exists() && !resultFile.createNewFile()){
                throw new RuntimeException("Error while creating mock file " + fileName);
            }

            PrintWriter writer = new PrintWriter(resultFile, "UTF-8");
            writer.println("p1=one");
            writer.println("p2=two");
            writer.println("p4=four");
            writer.close();

            System.out.println("Mock file created: " + resultFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }


        return resultFile;
    }

    public File createValidFile3(String fileName) {
        File resultFile = new File(this.resourceDirPath + fileName);

        try {
//            if file not exists trying to create it and throwing exception if failed
            if (!resultFile.exists() && !resultFile.createNewFile()){
                throw new RuntimeException("Error while creating mock file " + fileName);
            }

            PrintWriter writer = new PrintWriter(resultFile, "UTF-8");
            writer.println("p1=one");
            writer.println("p2=two");
            writer.println("p3=three");
            writer.println("p5=four");
            writer.close();

            System.out.println("Mock file created: " + resultFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }


        return resultFile;
    }

//    files with duplicates
    public File createFileWithDuplicatesMixed(String fileName) {
        File resultFile = new File(resourceDirPath + fileName);

        try {
//            if file not exists trying to create it and throwing exception if failed
            if (!resultFile.exists() && !resultFile.createNewFile()){
                throw new RuntimeException("Error while creating mock file " + fileName);
            }

            PrintWriter writer = new PrintWriter(resultFile, "UTF-8");
            writer.println("p1=one");
            writer.println("p1=раз");
            writer.println("p2=two");
            writer.println("p2=two");
            writer.println("p3=three");
            writer.println("p4=four");
            writer.println("p4=four");
            writer.println("p1=ещё раз");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Mock file created: " + resultFile.getName());

        return resultFile;
    }

    public File createFileWithDuplicatesKeyOnly(String fileName) {
        File resultFile = new File(resourceDirPath + fileName);

        try {
//            if file not exists trying to create it and throwing exception if failed
            if (!resultFile.exists() && !resultFile.createNewFile()){
                throw new RuntimeException("Error while creating mock file " + fileName);
            }

            PrintWriter writer = new PrintWriter(resultFile, "UTF-8");
            writer.println("p1=one");
            writer.println("p1=раз");
            writer.println("p2=two");
            writer.println("p3=three");
            writer.println("p4=four");
            writer.println("p4=четыре");
            writer.println("p4=ещё четыре");
            writer.close();

            System.out.println("Mock file created: " + resultFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }


        return resultFile;
    }

    public File createFileWithDuplicatesFullOnly(String fileName) {
        File resultFile = new File(resourceDirPath + fileName);

        try {
//            if file not exists trying to create it and throwing exception if failed
            if (!resultFile.exists() && !resultFile.createNewFile()){
                throw new RuntimeException("Error while creating mock file " + fileName);
            }

            PrintWriter writer = new PrintWriter(resultFile, "UTF-8");
            writer.println("p1=one");
            writer.println("p1=one");
            writer.println("p2=two");
            writer.println("p3=three");
            writer.println("p4=four");
            writer.println("p4=four");
            writer.println("p1=one");
            writer.close();

            System.out.println("Mock file created: " + resultFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultFile;
    }

    /**
     * Creates few NOT properties files
     * */
    public void createNotPropertiesFiles(String... fileNames) {

        try {
            for (String fileName : fileNames) {
                File file = new File(resourceDirPath + fileName);

//                if file not exists trying to create it and throwing exception if failed
                if (!file.exists() && !file.createNewFile()){
                    throw new RuntimeException("Error while creating mock file " + fileName);
                }

                System.out.println("Mock file created: " + file.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
