package gitlet;

import java.io.Serializable;
import java.util.HashMap;

/** Represents the branches in a repository
 * Stores all the branches and their related pointers in a HashMap
 * Is implemented as Serializable because we would like to store the information about branches persistently
 */
public class Branch implements Serializable {

    /* HashMap which stores all the branches */
    private HashMap<String, String> all_branches;

    /* Constructor */
    public Branch() {
        this.all_branches = new HashMap<>();
    }

    /* Insert a newly created branch and its pointer OR update the pointer of an existing branch */
    public void insert (String key, String value) {
        this.all_branches.put(key, value);
    }
}
