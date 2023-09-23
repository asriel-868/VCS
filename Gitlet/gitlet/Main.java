package gitlet;

import java.io.File;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        /* When arguments are empty */
        if (args.length == 0) {
            System.out.println("Please Enter a command.");
            System.exit(0);
        }

        Repository repo = new Repository();

        String firstArg = args[0];
        switch(firstArg) {
            /** Creates a new Gitlet vcs in the current directory. Should abort with an error message
             *  if there is already a Gitlet VCS in the current directory. The newly created system will start with an
             *  initial commit that has the message "initial commit" and a timestamp of Unix Epoch. The system will also
             *  start with a single branch named master.
             */
            case "init": {
                if (validateCommand(1, args)) {
                    repo.init();
                }
                break;
            }
            /** Adds the given file to the staging area. Staging an already staged file overwrites the previous entry
             *  in the staging area. If the current version of the file is identical to the version in the current
             *  commit, and remove it from the staging area if it is already there.
             */
            case "add": {
                if (validateCommand(2, args)) {
                    repo.add(args[1]);
                }
                break;
            }
            /** Creates a new commit by saving a snapshot of the tracked files in the current commit and
             * staging area.
             */
            case "commit": {
                if (validateCommand(2, args)) {
                    repo.commit(args[1]);
                }
                break;
            }
        }
        System.exit(0);
    }
    /** Function that validates the number of arguments passed equals the required number */
    public static boolean validateCommand(int length, String[] input) {
        if (input.length == length) {
            return true;
        }
        System.out.println("Incorrect Operands");
        return false;
    }
}
