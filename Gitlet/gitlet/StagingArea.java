package gitlet;

import java.io.Serializable;
import java.util.HashMap;

/** Represents the staging area of the version control system
 *  Stores the staged files and their Sha1 hashes in a Hashmap
 *  Is implemented as Serializable as the staging area should persist between multiple executions
 */
public class StagingArea implements Serializable {
    /* Stores the staged files and their Sha1 hashes */
    private HashMap<String, String> staged_files;

    /* Stores the staged for removal files */
    private HashMap<String, String> removal_staged_files;

    /* Constructor */
    public StagingArea () {
        this.staged_files = new HashMap<>();
        this.removal_staged_files = new HashMap<>();
    }

    /* Getter method that returns all the currently staged files */
    public HashMap<String, String> getStagedFiles() {
        return this.staged_files;
    }

    /* Getter method that returns removal_staged_files */
    public HashMap<String, String> getRemovalStagedFiles () {
        return this.removal_staged_files;
    }

    /* Returns true if the given file is currently in 'staging' */
    public boolean isStaging (String file_name) {
        return this.staged_files.containsValue(file_name);
    }

    /* Returns the hash of given staged file */
    public String stagedFileHash (String file_name) {
        return this.staged_files.get(file_name);
    }

    /* 'Unstages' the given file */
    public void unStageFile (String file_name) {
        this.staged_files.remove(file_name);
    }

    /* Stages the given file */
    public void stageFile (String file_name, String file_hash) {
        this.staged_files.put(file_name, file_hash);
    }

    /* Returns true if the given file is staged for removal */
    public boolean isStagingForRemoval (String file_name) {
        return this.removal_staged_files.containsValue(file_name);
    }

    /* Unstages the file from removal */
    public void unStageFileRemoval (String file_name) {
         this.removal_staged_files.remove(file_name);
    }
}
