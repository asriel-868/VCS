package gitlet;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeMap;

/** Represents the branches in a repository
 * Stores all the branches and their related pointers in a TreeMap
 * Is implemented as Serializable because we would like to store the information about branches persistently
 */
public class Branch implements Serializable {

    /** TreeMap which stores all the branches. Key is the branch name and value is the commit hash of its latest commit */
    private TreeMap<String, String> all_branches;
    /** Stores the current branch in the repository */
    private String current_branch;

    /** Constructor */
    public Branch() {
        this.all_branches = new TreeMap<>();
        this.current_branch = null;
    }


    /** Add a newly created branch and its pointer OR update the pointer of an existing branch */
    public void addBranch (String key, String value) {
        this.all_branches.put(key, value);
    }

    /** Getter method for current_branch */
    public String getCurrentBranch () {
        return this.current_branch;
    }

    /** Setter method for current_branch */
    public void setCurrentBranch (String name) {
        this.current_branch = name;
    }

    /** Getter method for all_branches. Returns all the branch names in
     *  lexicographic order.
     */
    public Set<String> getAllBranches() {
        return this.all_branches.keySet();
    }

    /** Returns the commit at the head of the given branch */
    public String branchHead(String branch) {
        return this.all_branches.get(branch);
    }

    /** Checks if a branch with the given name exists */
    public boolean existsBranch (String branch) {
        return this.all_branches.containsKey(branch);
    }

    /** Removes the given branch from the all_branches list */
    public void removeBranch (String branch_name) {
        this.all_branches.remove(branch_name);
    }
}
