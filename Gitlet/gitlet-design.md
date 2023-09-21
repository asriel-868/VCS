# MinGit Design Document

**Name**: Rishikesh S

## Classes and Data Structures

### Commit Class
This class serves as a blueprint to represent commits. Each commit is represented as
an object. 

#### Instance Variables

* Message - Contains the commit message
* Timestamp - Time at which the commit was created. Set by the constructor.
              For the initial commit, timestamp is Unix Epoch.
* Parent - The parent of the commit object. We store the SHA1 hash of 
           the parent commit here.
* Blobs - The blobs referenced by this commit. We will store it as a TreeMap with 
          keys being the file names and values being the blobs.

#### Description
Each commit object can be uniquely identified by its name. We get the name of a commit
object by hashing its contents.

### Repository Class
This class serves as a blueprint for a repository. An instance of this class will 
represent a repository.

#### Static Variables
* CWD - Represents the directory from which MinGit is run. 
* MINGIT_DIR - Represents the .mingit directory which stores all the information 
               needed for MinGit.  


#### Instance Variables
* HEAD - Points to the **front of the current branch**.
* Staging area - A structure which stores the key value pairs of files and their
                 blobs, which has been staged for commiting. 
* 

## Algorithms

## Persistence

