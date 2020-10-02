import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class FindFiles {
    // could have return it
    public static File[] files;
    public static boolean matched = false;

    public static void getTargetFiles(File directory, String targets, boolean isRegex, boolean isExt, String[] extensions){
        if(directory == null || !directory.isDirectory()){
            System.out.println("Error: the directory is null or it does not exit");
            System.exit(1);
        }

        files = directory.listFiles(new FileFilter(){
            @Override
            public boolean accept(File pathname) {
                // TODO Auto-generated method stub

                // -reg
                if(isRegex) {
                    if(isExt) {
                        for(String ext : extensions) {
                            if(!pathname.isDirectory() && pathname.getName().matches(targets) && pathname.getName().endsWith("."+ext)) {
                                return true;
                            }
                        }
                        return false;
                    } else {
                        return !pathname.isDirectory() && pathname.getName().matches(targets);
                    }

                } else {
                    if(isExt) {
                        for(String ext : extensions) {
                            if(!pathname.isDirectory() && pathname.getName().equals(targets+"."+ext)) return true;
                        }
                        return false;
                    } else {
                        return !pathname.isDirectory() && pathname.getName().equals(targets);
                    }

                }
            }

        });

        /*if(targetFiles != null && targetFiles.length != 0) {
            //files.addAll(Arrays.asList(targetFiles));
            for(File file : targetFiles) {
                files.add(file);
            }
        }*/


        /*for(File pathname: directory.listFiles()){
            System.out.println(pathname.getName());
            // -reg
            if(isRegex) {
                if(isExt) {
                    for(String ext : extensions) {
                        if(!pathname.isDirectory() && pathname.getName().matches(targets) && pathname.getName().endsWith("."+ext)) {
                            files.add(pathname);
                        }
                    }
                } else {
                    if(!pathname.isDirectory() && pathname.getName().matches(targets)) {
                        files.add(pathname);
                    }
                }

            } else {
                if(isExt) {
                    for(String ext : extensions) {
                        if(!pathname.isDirectory() && pathname.getName().equals(targets+"."+ext)) files.add(pathname);
                    }
                } else {
                    if(!pathname.isDirectory() && pathname.getName().equals(targets)) {
                        files.add(pathname);
                    }
                }

            }
        }
        System.out.println("out");*/
    }

    // only pass directory as parameter
    public static void recursive(File path, String targets, boolean isRegex, boolean isExt, String[] extensions) throws IOException {
        if(path == null || !path.isDirectory()){
            System.out.println("Error: the directory is null or it does not exit");
            System.exit(1);
        }

        if(path.listFiles() == null) {
            System.out.println("Error: the directory is empty");
            System.exit(1);
        }

        /*try {
            System.out.println(path.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        getTargetFiles(path,targets,isRegex,isExt,extensions);
        if(files.length != 0) {
            printFiles(files);
        }
        for(File file: path.listFiles()){
            if(file.isDirectory()){
                recursive(file,targets,isRegex,isExt,extensions);
            }
        }

    }

    public static void printFiles(File[] files) throws IOException {
        if(!matched) {
            System.out.println("Found matching file(s) at:");
            matched = true;
        }
        for(File file : files) {
            System.out.println(file.getCanonicalPath());
        }
    }

    // if args is "-help" <= 1
    public static void printHelpText() {
        System.out.println("Usage: java FindFiles filetofind [-option arg]\n" +
                "  -help                     :: print out a help page and exit the program.\n" +
                "  -r                        :: execute the command recursively in subfiles.\n" +
                "  -reg                      :: treat `filetofind` as a regular expression when searching.\n" +
                "  -dir [directory]          :: find files starting in the specified directory. \n" +
                "  -ext [ext1,ext2,...]      :: find files matching [filetofind] with extensions [ext1, ext2,...].\n");
    }

    public static void main(String[] args){
        //boolean dir = false;
        File tarDir = new File("").getAbsoluteFile();

        boolean extension = false;
        String extString = "";
        String[] extTypes = new String[10];

        boolean regex = false;
        boolean recurse = false;

        String fileName = "";
        String errorMessage = "";
        int errorNum = 0;

        try {
            // check for help text
            for (String arg : args) {
                if (arg.equals("-help")) {
                    printHelpText();
                    System.exit(1);
                }
            }

            // check argument validity
            int j = 0;
            while (j < args.length) {
                if (args[j].equals("-r")) {
                    recurse = true;
                } else if (args[j].equals("-reg")) {
                    regex = true;
                } else if (args[j].equals("-dir")) {
                    if ((j+1) >= args.length) {
                        if (errorNum == 0) {
                            errorMessage = "Error: no directory argument provided for option -ext";
                        }
                        ++errorNum;
                    } else if (args[j + 1].startsWith("-")) {
                        if (errorNum == 0) {
                            errorMessage = "Error: no directory argument provided for option -dir";
                        }
                        ++errorNum;
                    } else {
                        //dir = true;
                        tarDir = new File(args[j + 1]).getAbsoluteFile();
                        ++j;
                    }
                } else if (args[j].equals("-ext")) {
                    if ((j+1) >= args.length) {
                        if (errorNum == 0) {
                            errorMessage = "Error: no extension argument provided for option -ext";
                        }
                        ++errorNum;
                    } else if (args[j + 1].startsWith("-")) {
                        if (errorNum == 0) {
                            errorMessage = "Error: no extension argument provided for option -ext";
                        }
                        ++errorNum;
                    } else {
                        extension = true;
                        extString = args[j + 1];
                        extTypes = extString.split(",");
                        ++j;
                    }
                } else if (args[j].startsWith("-")) {
                    if (errorNum == 0) {
                        errorMessage = "Error: '" + args[j] + "' is an invalid option. Please supply valid options";
                    }
                    ++errorNum;
                } else {
                    // what if the file name starts with "-"? No way
                    if (fileName.isEmpty()) {
                        fileName = args[j];
                    } else {
                        if (errorNum == 0) {
                            errorMessage = "Error: the argument '" + args[j] + "' is extra or misplaced";
                        }
                        ++errorNum;
                    }
                }
                ++j;
            }

            if(fileName.isEmpty()) {
                System.out.println("Error: no file to find. Need the target file name as minimum argument");
                if(errorNum >= 1) printHelpText();
                System.exit(1);
            } else if (errorNum == 1) {
                System.out.println(errorMessage);
                System.exit(1);
            } else if (errorNum > 1) {
                System.out.println(errorMessage);
                System.out.println("(There are more than one error. Please read the following)");
                printHelpText();
                System.exit(1);
            }

            //ArrayList<String> filesToFind = new ArrayList<>();

            // -ext
            /*if (extension) {
                for (String ext : extTypes) {
                    if(regex == false) {
                        filesToFind.add(fileName.concat("."+ext));
                    }
                }
            } else {
                filesToFind.add(fileName);
            }*/

            System.out.print("Looking for file '" + fileName + "'");
            if(regex) {
                System.out.print(" as Regular expression");
            }
            if(extension) {
                System.out.print(" with extensions " + extString);
            }

            // -r
            if (recurse) {
                System.out.println(" in '" + tarDir.getName() + "' and its subdirectories recursively");
                recursive(tarDir, fileName, regex, extension, extTypes);
                if(!matched) {
                    System.out.println("No matching file found!");
                }
            } else {
                System.out.println(" in '" + tarDir.getName() + "'");
                getTargetFiles(tarDir, fileName, regex, extension, extTypes);
                if(files.length == 0) {
                    System.out.println("No matching file found!");
                } else {
                    printFiles(files);
                }
            }


        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


}
