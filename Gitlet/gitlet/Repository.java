package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.HashMap;


/** Represents a gitlet repository.

 *  @author Rishikesh S
 */
public class Repository {
    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = Utils.join(CWD, ".gitlet");

    /** Directory which stores all the commits */
    public static final File COMMIT_DIR = Utils.join(GITLET_DIR, "commits");

    /** Directory which stores all the blobs */
    public static final File BLOBS_DIR = Utils.join(GITLET_DIR, "blobs");

    /** Directory which stores the branch pointers */
    public static final File BRANCH_DIR = Utils.join(GITLET_DIR, "branches");

    /** File which stores the HEAD pointer */
    public static final File HEAD_POINTER_FILE = Utils.join(BRANCH_DIR,"HEAD.txt");

    /** File in which the branch object is stored */
    public static final File BRANCH_OBJECT_FILE = Utils.join(BRANCH_DIR, "branch_obj");


    /** Directory where the staging area is stored */
    public static final File STAGING_AREA = Utils.join(GITLET_DIR, "staging");

    /** File where the staging area is stored --- named as index */
    public static final File STAGING_AREA_FILE = Utils.join(STAGING_AREA, "index");

    /** Stores the branches and their current pointer locations */
    Branch branches;

    /** The head pointer */
    public String HEAD;

    /** The staging area  */
    public StagingArea staging_area;



    public Repository () {
        /* If the current directory already has a Gitlet version-control system, we load the previous state of the repository */
        if (GITLET_DIR.exists()) {
            this.HEAD = Utils.readContentsAsString(HEAD_POINTER_FILE);
            this.staging_area = Utils.readObject(STAGING_AREA_FILE, StagingArea.class);
            this.branches = Utils.readObject(BRANCH_OBJECT_FILE, Branch.class);
        }
        /* Else, we create a new repo and initialize the repo variables to their default values */
        else {
            this.HEAD = null;
            this.staging_area = new StagingArea();
            this.branches = new Branch();
            this.branches.insert("master", null);
        }
    }

    /* Function fot the init command */
    public void init () {
        /* If a Gitlet VCS already exists in this directory, we print an error message and then exit */
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        /* Else, we set up the .gitlet folder and make an initial commit */
        else {
            GITLET_DIR.mkdir();
            COMMIT_DIR.mkdir();
            BLOBS_DIR.mkdir();
            BRANCH_DIR.mkdir();
            STAGING_AREA.mkdir();
            try {
                HEAD_POINTER_FILE.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BRANCH_OBJECT_FILE.createNewFile();
            } catch (IOException e) {
                     e.printStackTrace();
            }
            try {
                STAGING_AREA_FILE.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /* Creating the initial commit */
            Commit initial_commit = new Commit("initial commit", null, this.staging_area.getStagedFiles());
            /* Updates the HEAD and master branch pointer to point to the latest commit */
            this.HEAD = initial_commit.saveCommit();
            this.branches.insert("master", this.HEAD);
            /* Saving the repo state before exiting */
            this.saveRepoState();
        }
    }

    /* Function for the add command. Takes the name of the file to be staged as the argument */
    public void add (String file_name) {
        File given_file = Utils.join(CWD, file_name);
        /* Checking if the given file exists or not */
        if (given_file.exists()) {
            /* Getting the hash of the given file and deserializing the current commit */
            String file_hash = Utils.sha1(Utils.readContentsAsString(given_file));
            File current_commit_file = Utils.join(COMMIT_DIR, this.HEAD);
            Commit current_commit = Utils.readObject(current_commit_file, Commit.class);

            /* If the current version is same as that tracked by current commit, we don't do anything */
            if (current_commit.isTracking(file_name) && Objects.equals(current_commit.trackedFileHash(file_name), file_hash)) {
                /* If the file is currently staged for addition, we unstage it as it is tracked by current commit */
                if (this.staging_area.isStaging(file_name)) {
                    this.staging_area.unStageFile(file_name);
                }
            }
            /* Else, when the current commit is not tracking */
            else {
                this.staging_area.stageFile(file_name, file_hash);
            }
            /* 'Unstages' the file from removal if it was there */
            if (this.staging_area.isStagingForRemoval(file_name)) {
                this.staging_area.unStageFileRemoval(file_name);
            }
            this.saveRepoState();
        }
        /* Exiting from the command as the file does not exist */
        else {
            System.out.println("File does not exist.");
            this.saveRepoState();
        }
    }

    /* Function for the commit command. Creates a new commit. Takes the message given by the user as the argument */
    public void commit (String message) {
        /* Creates a new commit that tracks the same files as that by its parent */
        File current_commit_file = Utils.join(COMMIT_DIR, this.HEAD);
        Commit current_commit = Utils.readObject(current_commit_file, Commit.class);
        Commit new_commit = new Commit(message, this.HEAD, current_commit.getReferencedBlobs());

        /* Adds all the staged files into the current commit */
        HashMap<String, String> current_staged = this.staging_area.getStagedFiles();
    }

    /* Saves the state of the repo */
    public void saveRepoState () {
        Utils.writeContents(HEAD_POINTER_FILE, this.HEAD);
        Utils.writeObject(STAGING_AREA_FILE, this.staging_area);
        Utils.writeObject(BRANCH_OBJECT_FILE, this.branches);
    }

}
