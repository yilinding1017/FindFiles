import java.io.*;

public class FindFiles {
    public static File[] files;

    public static void getTargetFiles(File directory, String targets, boolean isRegex, boolean isExt, String[] extensions){
        if(directory == null || directory.isDirectory() == false){
            System.out.println("Error: the directory is null/does not exit");
            System.exit(1);
        }


        files = directory.listFiles(new FilenameFilter(){
            @Override
            public boolean accept(File dir, String name) {
                // TODO Auto-generated method stub

                // -reg
                if(isRegex) {
                    if(isExt) {
                        for(String ext : extensions) {
                            if(name.matches(targets) && name.endsWith("."+ext)) {
                                return true;
                            }
                        }
                        return false;
                    } else {
                        return name.matches(targets);
                    }

                } else {
                    if(isExt) {
                        for(String ext : extensions) {
                            if(name.equals(targets+"."+ext)) return true;
                        }
                        return false;
                    } else {
                        return name.equals(targets);
                    }

                }
            }

        });
    }

    // only pass directory as parameter
    public static void recursive(File path, String targets, boolean isRegex, boolean isExt, String[] extensions) {
        if(path == null || path.isDirectory() == false){
            System.out.println("Error: the directory is null/does not exit");
            System.exit(1);
        }

        getTargetFiles(path,targets,isRegex,isExt,extensions);
        for(File file: path.listFiles()){
            if(file.isDirectory()){
                recursive(file,targets,isRegex,isExt,extensions);
            }
        }

    }

    public static void printFiles(File[] files) throws IOException {
        System.out.println("Found matching file(s) at:");
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
            } else {
                System.out.println(" in '" + tarDir.getName() + "'");
                getTargetFiles(tarDir, fileName, regex, extension, extTypes);
            }

            /*if(regex && extension) {
                for(File file : files) {
                    boolean correctExt = false;
                    for(String ext : extTypes) {
                        if(file.getName().endsWith(ext)) {
                            correctExt = true;
                        }
                    }
                    if(correctExt == false) files;
                }
            }*/

            if(files.length == 0) {
                System.out.println("No matching file found!");
            } else {
                printFiles(files);
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


}
