# Gitlet Design Document

**Name**: Rishikesh S

## Classes and Data Structures

### 1. Commit Class

This class serves as a blueprint to represent commits. Each commit is represented as
an object.

#### Instance Variables

* Message :  
 Contains the commit message
* Timestamp :  
 Time at which the commit was created. Set by the constructor. For the initial commit, timestamp is 
 Unix Epoch.
* Parent :  
The parent of the commit object. We store the SHA1 hash of the parent commit here.
* Blobs :  
 The blobs referenced by this commit. We will store it as a TreeMap with keys being the file names
 and values being the blobs.

#### Description

Each commit object can be uniquely identified by its name. We get the name of a commit
object by hashing its contents.

### 2. Repository Class

This class serves as a blueprint for a repository. An instance of this class will
represent a repository.

#### Static Variables

* CWD :  
 Represents the directory from which MinGit is run.
* GITLET_DIR :  
Represents the .gitlet directory which stores all the information needed for Gitlet.
* HEAD_PONTER_FILE :  
 Stores the HEAD pointer of the repository when serialied

We also have other static variables which point to relevant directories in the .gitlet folder.

#### Instance Variables

* HEAD :  
 Points to the **front of the current branch**.
* Staging area :  
 An instance of the StagingArea class. It stores the key value pairs of files and their blobs, 
 which has been staged for commiting.
* Branches :  
 An instance of Branch class. Used to store all the branch related information of the repository
 such as no: of branches, current branch, etc.

#### Description

This class provides an interface to  interact with the .gitlet directory in response to different 
commands. It also has a saveRepoState() method which saves the current state of the repository. Note 
that to save the current repo state, we just have to save the HEAD , Staging area and branches 
variables.

### 3. StagingArea class

This class serves as a blueprint for the staging area of the repository. There are two staging areas:
<ol>
<li> Staged for addition</li>
<li> Staged for removal</li>
</ol>

#### Static variables 
There are no static variables for this class

#### Instance variables
* Staged files :  
 Stores the staged for addition files as a TreeMap. Key is the file name and value is the file hash.
* Removal staged files :  
 Stores the staged for removal files. Is stored in a TreeSet. 

#### Description 
This class provides an interface to interact with the staging area of the repository. The reason we 
use a TreeMap and TreeSet is that we would like to store the keys in lexicographic order. This would
help in printing the file names in order while using git status command. 

### 4. Branch Class
This class represents the branches in a repository. 

#### Static variables
There are no static variables for this class. 

#### Instance variables
* all branches :  
Stores all the branches in a repository. We store it as a TreeMap where the key is the branch name
and values is the commit hash of its latest commit. 
* current branch :  
Stores the current branch of the repo. We store it as a String. 

## Algorithms

## Persistence

