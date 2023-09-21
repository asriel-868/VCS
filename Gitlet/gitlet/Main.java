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

        String firstArg = args[0];
        switch(firstArg) {
            /** Creates a new Mingit vcs in the current directory. Should abort with an error message
             *  if there is already a Mingit VCS in the current directory. The newly created system will start with an
             *  initial commit that has the message "initial commit" and a timestamp of Unix Epoch. The system will also
             *  start with a single branch named master.
             */
            case "init":

            case "add":
                // TODO: handle the `add [filename]` command
                break;
            // TODO: FILL THE REST IN
        }
    }
}
