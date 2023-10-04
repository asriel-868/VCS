package gitlet;


/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author Rishikesh S
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        /* When arguments are empty */
        if (args.length == 0) {
            System.out.println("Please Enter a command.");
            System.exit(0);
        }

        Repository repo = new Repository();

        String firstArg = args[0];
        switch (firstArg) {
            /*  Creates a new Gitlet vcs in the current directory. Should abort with an error message
             *  if there is already a Gitlet VCS in the current directory. The newly created system will start with an
             *  initial commit that has the message "initial commit" and a timestamp of Unix Epoch. The system will also
             *  start with a single branch named master.
             */
            case "init": {
                if (validateCommand(1, args)) {
                    repo.init();
                }
                else {
                    System.out.println("Incorrect operands.");
                }
                break;
            }
            /*  Adds the given file to the staging area. Staging an already staged file overwrites the previous entry
             *  in the staging area. If the current version of the file is identical to the version in the current
             *  commit, and remove it from the staging area if it is already there.
             */
            case "add": {
                if (repo.checkInitialized() && validateCommand(2, args)) {
                    repo.add(args[1]);
                }
                else {
                    System.out.println("Incorrect operands.");
                }
                break;
            }
            /* Creates a new commit by saving a snapshot of the tracked files in the current commit and
             * staging area.
             */
            case "commit": {
                if (repo.checkInitialized() && validateCommand(2, args)) {
                    if (args[1].trim().isEmpty()) {
                        System.out.println("Please enter a commit message");
                        break;
                    }
                    repo.commit(args[1]);
                }
                else {
                    System.out.println("Incorrect operands.");
                }
                break;
            }
            /*  Unstages the file if it currently staged. If the file is tracked in the
             *  current commit, stages it for removal and removes the file from working
             *  directory. (DOES NOT REMOVE THE FILE FROM CWD UNLESSS TRACKED BY CURRENT COMMIT)
             */
            case "rm": {
                if (repo.checkInitialized() && validateCommand(2, args)) {
                    repo.rm(args[1]);
                }
                else {
                    System.out.println("Incorrect operands.");
                }
                break;
            }
            /*  Starting at the head commit, displays information about each commit going
             *  backward until the initial commit. Consider the 1st parent in case of
             */
            case "log": {
                if (repo.checkInitialized() && validateCommand(1, args)) {
                    repo.log();
                }
                else {
                    System.out.println("Incorrect operands.");
                }
                break;
            }
            /* Similar to log, but prints information about all the commits ever made. The
             *  order of commits is not fixed
             */
            case "global-log": {
                if (repo.checkInitialized() && validateCommand(1, args)) {
                    repo.globalLog();
                }
                else {
                    System.out.println("Incorrect operands.");
                }
                break;
            }
            /*  Prints out the ids of all commits that have the given commit message. If no
             *  such commit exists, prints out an error.
             */
            case "find": {
                if (repo.checkInitialized() && validateCommand(2, args)) {
                    repo.find(args[1]);
                }
                else {
                    System.out.println("Incorrect operands.");
                }
                break;
            }
            /*  Prints out the current status of the repository. Shows all the branches, currently
             *  staged files, removed files and modified files. Prints out the info in lexicographic
             *  order.
             */
            case "status": {
                if (repo.checkInitialized() && validateCommand(1, args)) {
                    repo.status();
                }
                else {
                    System.out.println("Incorrect operands.");
                }
                break;
            }
            /*    Checkouts the given file/branch. Three possible cases :
             * 1. Filename is given as argument ---- Replaces the file with the one pointed to by HEAD commit
             * 2. Commit id and file name is given as argument ---- Replaces the file with the one pointed to by
             *    given commit.
             * 3. Branch name is given as argument ---- Takes all the files in the given branch and puts them in
             *    the working directory. Clears the staging area unless the given branch is the current branch.
             */
            case "checkout": {
                if (repo.checkInitialized() && ( validateCommand(3, args) || validateCommand(2, args) || validateCommand(4, args))) {
                    repo.checkout(args);
                }
                else {
                    System.out.println("Incorrect operands.");
                }
                break;
            }
            /* To be executed when a wrong command is entered */
            default: {
                System.out.println("No command with that name exists.");
            }
        }
        System.exit(0);
    }

    /**
     * Function that validates the number of arguments passed equals the required number
     */
    public static boolean validateCommand(int length, String[] input) {
        return input.length == length;
    }
}
