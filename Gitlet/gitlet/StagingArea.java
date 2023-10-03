package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.TreeSet;
import java.util.Set;
import java.util.TreeMap;

/** Represents the staging area of the version control system
 *  Stores the staged files and their Sha1 hashes in a Hashmap
 *  Is implemented as Serializable as the staging area should persist between multiple executions
 */
public class StagingArea implements Serializable {
    /** Stores the staged files and their Sha1 hashes. Key is file name and value is their hash*/
    private TreeMap<String, String> staged_files;

    /** Stores the staged for removal files */
    private TreeSet<String> removal_staged_files;

    /** Constructor */
    public StagingArea () {
        this.staged_files = new TreeMap<>();
        this.removal_staged_files = new TreeSet<>();
    }

    /** Getter method that returns all the currently staged (for addition) files */
    public TreeMap<String, String> getStagedFiles() {
        return this.staged_files;
    }

    /** Getter method that returns removal_staged_files */
    public TreeSet <String> getRemovalStagedFiles () {
        return this.removal_staged_files;
    }

    /** Returns true if the given file is currently in 'staging' */
    public boolean isStaging (String file_name) {
        return this.staged_files.containsKey(file_name);
    }

    /** Returns the hash of given staged file */
    public String stagedFileHash (String file_name) {
        return this.staged_files.get(file_name);
    }

    /** 'Unstages' the given file. Removes the file from the STAGED_FILES directory */
    public void unStageFile (String file_name) {
        String file_hash = this.staged_files.get(file_name);
        this.staged_files.remove(file_name);
        File file = Utils.join(Repository.STAGED_FILES_DIR, file_hash);
        if (file.exists()) {
            file.delete();
        }
    }

    /** Stages the given file. Adds the file to the STAGED_FILES directory */
    public void stageFile (String file_name, String file_hash) {
        this.staged_files.put(file_name, file_hash);
        File file = Utils.join(Repository.STAGED_FILES_DIR, file_hash);
        Utils.writeContents(file, Utils.readContentsAsString(Utils.join(Repository.CWD, file_name)));
    }

    /** Returns true if the given file is staged for removal */
    public boolean isStagingForRemoval (String file_name) {
        return this.removal_staged_files.contains(file_name);
    }

    /** Stages the given file for removal */
    public void stageForRemoval (String file_name) {
        this.removal_staged_files.add(file_name);
    }

    /** Unstages the file from removal */
    public void unStageFileRemoval (String file_name) {
         this.removal_staged_files.remove(file_name);
    }

    /** Clears the staging area. Removes the staged for addition files from
     *  STAGED_FILE_DIR and also clears the staged for removal area.
     */
    public void clearStagingArea () {
        this.removal_staged_files.clear();
        /* Iterating through all the staged files */
        for (String file : Utils.plainFilenamesIn(Repository.STAGED_FILES_DIR)) {
            File f = Utils.join(Repository.STAGED_FILES_DIR, file);
            if (f.exists()) {
                f.delete();
            }
        }
        this.staged_files.clear();
    }

    /** Returns the name of all the currently staged (for addition) files. The files are returned in
     *  lexicographic order.
     */
    public Set<String> getStagedFileNames () {
        return this.staged_files.keySet();
    }
}
