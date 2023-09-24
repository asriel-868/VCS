package gitlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;
import java.util.Formatter;
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
            Date unix_epoch = new Date(0);
            TimeZone timeZone = TimeZone.getTimeZone("GMT");
            Formatter formatter = new Formatter();
            formatter.format("%1$ta %1$tb %1$td %1$tT %1$tY %1$tZ", unix_epoch, timeZone);
            String formatted_time = formatter.toString();
            formatter.close();
            this.timestamp = formatted_time;
            /* Initial commit does not track any blobs */
            this.referenced_blobs = new HashMap<>();
        }
        else {
            Date current_date = new Date();
            TimeZone timeZone = TimeZone.getTimeZone("GMT");
            Formatter formatter = new Formatter();
            formatter.format("%1$ta %1$tb %1$td %1$tT %1$tY %1$tZ", current_date, timeZone);
            String formatted_time = formatter.toString();
            formatter.close();
            this.timestamp = formatted_time;
            this.referenced_blobs = new HashMap<>();
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
