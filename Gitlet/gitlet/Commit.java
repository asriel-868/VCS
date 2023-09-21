package gitlet;

// TODO: any imports you need here

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.io.File;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** Timestamp of the commit */
    private String timestamp;

    /** The message of this Commit. */
    private String message;

    /** Parent of this commit. We store the SHA1 hash of the parent commit here*/
    private String parent;

    /** The blobs referred by this commit */
    private Set<String> referenced_blobs;

    /** Constructor */
    public Commit (String message, String parent) {

        this.message = message;
        this.parent = parent;

        /* If the parent is null, that is, initial commit */
        if (parent == null) {
            LocalDateTime epoch = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0);
            DateTimeFormatter formattedEpoch = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            this.timestamp = epoch.format(formattedEpoch);
        }
        else {
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formattedCurrentTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            this.timestamp = currentTime.format(formattedCurrentTime);
        }

        /* TODO :  Just for the time being............. */
        this.referenced_blobs = new HashSet<>();
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

    /** Saves the Commit object to disk */
    public void saveCommit () {
        String name = Utils.sha1(this.message, this.timestamp, this.parent, this.referenced_blobs);
        File commit_file = Utils.join(Repository.COMMIT_DIR, name);
        try {
            commit_file.createNewFile();
        } catch (IOException e) {
            System.out.println("Error when creating commit file");
            e.printStackTrace();
        }
        Utils.writeObject(commit_file, this);
    }

}
