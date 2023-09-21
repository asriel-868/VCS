package gitlet;

import java.io.File;
import static gitlet.Utils.*;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .mingit directory. */
    public static final File MINGIT_DIR = Utils.join(CWD, ".mingit");

    /** Directory which stores all the commits */
    public static final File COMMIT_DIR = Utils.join(MINGIT_DIR, "commits");

    /** Directory which stores all the blobs */
    public static final File BLOBS_DIR = Utils.join(MINGIT_DIR, "blobs");

    /** Directory which stores all the blobs */

    /** The head pointer */
    public String HEAD;

    /** The staging area  */
    public TreeSet<String> staging_area;

    /* TODO: fill in the rest of this class. */

    public Repository () {
        if (!MINGIT_DIR.exists()) {
            this.staging_area = new TreeSet<>();
            this.HEAD = null;
        }
    }

    public void init () {
        if (MINGIT_DIR.exists()) {
            System.out.println("A MinGit version control system already exists in this directory.");
            System.exit(0);
        }
        if (!(MINGIT_DIR.mkdir() && COMMIT_DIR.mkdir() && BLOBS_DIR.mkdir())) {
            System.out.println("Initialization of repository failed");
        }

        Commit initialCommit = new Commit("Initial Commit", null);
        initialCommit.saveCommit();
    }

}
