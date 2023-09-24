package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.util.HashSet;

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

    /** Directory which stores the branch information */
    public static final File BRANCH_DIR = Utils.join(GITLET_DIR, "branches");

    /** File which stores the HEAD pointer */
    public static final File HEAD_POINTER_FILE = Utils.join(BRANCH_DIR,"HEAD.txt");

    /** File in which the branch object is stored */
    public static final File BRANCH_OBJECT_FILE = Utils.join(BRANCH_DIR, "branch_obj");


    /** Directory where the staging area is stored */
    public static final File STAGING_AREA_DIR = Utils.join(GITLET_DIR, "staging");

    /** File where the staging area is stored --- named as index */
    public static final File STAGING_AREA_FILE = Utils.join(STAGING_AREA_DIR, "index");

    /** Directory where the staged files are stored */
    public static final File STAGED_FILES_DIR = Utils.join(STAGING_AREA_DIR, "staged_files");

    /** Stores the branches and their current pointer locations */
    Branch branches;

    /** The head pointer */
    public String HEAD;

    /** The staging area  */
    public StagingArea staging_area;


    /** Constructor */
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
            this.branches.addBranch("master", null);
        }
    }

    /** Function fot the init command */
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
            STAGING_AREA_DIR.mkdir();
            STAGED_FILES_DIR.mkdir();
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
            /* Updates the HEAD and master branch pointer to point to the latest commit. Makes master the current branch */
            this.HEAD = initial_commit.saveCommit();
            this.branches.addBranch("master", this.HEAD);
            this.branches.setCurrentBranch("master");
            /* Saving the repo state before exiting */
            this.saveRepoState();
        }
    }

    /** Function for the add command. Takes the name of the file to be staged as the argument */
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

    /** Function for the commit command. Creates a new commit. Takes the message given by the user as the argument */
    public void commit (String message) {
        /* Creates a new commit that tracks the same files as that by its parent */
        File current_commit_file = Utils.join(COMMIT_DIR, this.HEAD);
        Commit current_commit = Utils.readObject(current_commit_file, Commit.class);
        Commit new_commit = new Commit(message, this.HEAD, current_commit.getReferencedBlobs());

        /* Gets the current staged files and current staged for removal files */
        HashMap<String, String> current_staged = this.staging_area.getStagedFiles();
        HashSet<String> current_staged_removal = this.staging_area.getRemovalStagedFiles();

        /* Iterating through staged for removal files */
        for (String entry : current_staged_removal) {
            new_commit.removeBlob(entry);
        }

        /* If no files staged for addition */
        if (current_staged.isEmpty()) {
            System.out.println("No changes added to the commit.");
            this.saveRepoState();
        }
        else {
            /* Iterating through each file staged for addition */
            for (Map.Entry<String, String> entry : current_staged.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                File blob_file = Utils.join(Repository.BLOBS_DIR, value);
                Utils.writeContents(blob_file, Utils.readContentsAsString(Utils.join(Repository.STAGED_FILES_DIR, value)));
                new_commit.addBlob(key, value);
            }
            /* Saving the created commit and updating the HEAD and current branch pointer */
            this.HEAD = new_commit.saveCommit();
            this.branches.addBranch(this.branches.getCurrentBranch(), this.HEAD);

            /* Clearing the staging area */
            this.staging_area.clearStagingArea();
            this.saveRepoState();
        }
    }

    /** Function for the rm command */
    public void rm (String file_name) {
        File f = Utils.join(Repository.CWD, file_name);
        /* Unstaging the file */
        if (this.staging_area.isStaging(file_name)) {
            this.staging_area.unStageFile(file_name);
        }
        Commit current_commit = Utils.readObject(Utils.join(Repository.COMMIT_DIR, this.HEAD), Commit.class);
        /* Checking if the current version of given file is tracked by the current commit.
        *  If so, then remove the file from CWD and stage it for removal */
        if (current_commit.isTracking(file_name)) {
            this.staging_area.stageForRemoval(file_name);
            if (f.exists()) {
                f.delete();
            }
        }
        this.saveRepoState();
    }

    /** Functon for the log command */
    public void log () {
        Commit current_commit = Utils.readObject(Utils.join(Repository.COMMIT_DIR, this.HEAD), Commit.class);
        String current_commit_hash = this.HEAD;
        while (current_commit != null) {
            System.out.println("===");
            System.out.printf("commit %s\n", current_commit_hash);
            System.out.printf("Date: %s\n", current_commit.getTimestamp());
            System.out.println(current_commit.getMessage());

            if (current_commit.getParent() == null) {
                current_commit = null;
            }
            else {
                current_commit_hash = current_commit.getParent();
                current_commit = Utils.readObject(Utils.join(Repository.COMMIT_DIR, current_commit.getParent()), Commit.class);
            }
        }
    }

    /** Saves the state of the repo */
    public void saveRepoState () {
        Utils.writeContents(HEAD_POINTER_FILE, this.HEAD);
        Utils.writeObject(STAGING_AREA_FILE, this.staging_area);
        Utils.writeObject(BRANCH_OBJECT_FILE, this.branches);
    }

    /** returns true if the current working directory is a Gitlet reository */
    public boolean checkInitialized () {
        if (Repository.GITLET_DIR.exists()) {
            return true;
        }
        else {
            System.out.println("Not in an initialized Gitlet directory");
            return false;
        }
    }
}
