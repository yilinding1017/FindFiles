*Yilin Ding*<br>*20765311 y264ding*<br>*openjdk version "11.0.8" 2020-07-14*<br>*macOS 10.15.6 (MacBook Pro 2019)*<br>

The command-line application "FindFiles" finds files in current directory (by default) and outputs the absolute path of the located files. To use this commandline application, type in the following syntax：

```embeddedjs
java FindFiles filetofind [-option parameter1 parameter2 ...]
```

Multiple options are also supported, including -r, -reg, -dir, -ext, and -help, the usage of each option can be displayed when type in "java FindFiles -help" as the following:

```embeddedjs
 Usage: java FindFiles filetofind [-option arg]
  -help                     :: print out a help page and exit the program.
  -r                        :: execute the command recursively in subfiles.
  -reg                      :: treat `filetofind` as a regular expression when searching.
  -dir [directory]          :: find files starting in the specified directory. 
  -ext [ext1,ext2,...]      :: find files matching [filetofind] with extensions [ext1, ext2,...].
```

Multiple options can be combined in one command. However, if -help is used, all the other auguments will be ignored and the program will display the help message. Error messages will be displayed if there are inccorect options and arguments. The important things that need to be noticed are the followings:<br>

- when there is one error when typing the command, an error message will be displayed. If there are more than one error of the command, the first error message will be displayed along with the help text. 
- if the options "-dir" and "-ext" are combined, the absolute pathes of the files that matches the regular expression as well as ends in one of the given extensions will be displayed, which is different from the case of single -ext (where the filename is appended with extensions and are searched). 
- the found files must be files but not directories.
- the same options may be used/repeated multiple times in the single command and the effect is the same as one single command. When the options with arguments required (i.e. -dir and -ext) are repeated in one single command, the last one will be applied.

