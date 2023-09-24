package gitlet;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.io.File;

/** Represents a gitlet commit object.
 *
 *
 *  @author Rishikesh S
 */
public class Commit implements Serializable {

    /** Timestamp of the commit */
    private String timestamp;

    /** The message of this Commit. */
    private String message;

    /** Parent of this commit. We store the SHA1 hash of the parent commit here*/
    private String parent;

    /** The blobs referred by this commit */
    private HashMap<String, String> referenced_blobs;


    /** Constructor */
    public Commit (String message, String parent, HashMap<String, String> tracked_files) {

        this.message = message;
        this.parent = parent;

        /* If the parent is null, that is, initial commit */
        if (parent == null) {
            LocalDateTime epoch = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0);
            DateTimeFormatter formattedEpoch = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            this.timestamp = epoch.format(formattedEpoch);
            /* Initial commit does not track any blobs */
            this.referenced_blobs = new HashMap<>();
        }
        else {
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formattedCurrentTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            this.timestamp = currentTime.format(formattedCurrentTime);
            this.referenced_blobs = tracked_files;
        }


    }

    /** Getter methods for the instance variables */
    public String getMessage () {
        return this.message;
    }

    public String getTimestamp () {
        return this.timestamp;
    }

    public String getParent () {
        return this.parent;
    }

    /** Saves the Commit object to disk and returns the Sha1 Hash of the saved commit obj */
    public String saveCommit () {
        String commit_hash = this.getHash();
        File commit_file = Utils.join(Repository.COMMIT_DIR, commit_hash);
        try {
            commit_file.createNewFile();
        } catch (IOException e) {
            System.out.println("Error when creating commit file");
            e.printStackTrace();
        }
        Utils.writeObject(commit_file, this);
        return commit_hash;
    }

    /** Returns the Sha1 hash of the commit object */
    public String getHash () {
        byte[] serialized_obj = Utils.serialize(this);
        return Utils.sha1(serialized_obj);
    }

    /** Returns the hash of the tracked file */
    public String trackedFileHash (String file_name) {
        return this.referenced_blobs.get(file_name);
    }
    /** Returns true if the given file is tracked by this commit */
    public boolean isTracking (String file_name) {
        return this.referenced_blobs.containsKey(file_name);
    }

    /** Returns the tracked files */
    public HashMap<String, String> getReferencedBlobs () {
        return this.referenced_blobs;
    }

    /** Adds a new blob */
    public void addBlob (String key, String value) {
        this.referenced_blobs.put(key, value);
    }

    /** removes a tracked blob */
    public void removeBlob (String key) {
        this.referenced_blobs.remove(key);
    }
}
