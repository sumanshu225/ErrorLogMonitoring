# Requirements
> DockerFile could not be created because no java version of required version was available    

Java 22.0.1 


## Commands to Run
- Install the JDK from the [link](https://download.oracle.com/java/22/latest/jdk-22_windows-x64_bin.exe)
- Clone the repositorty
- Provide correct path to `INPUT_FILE_NAME` and `OUTPUT_FILE_NAME` inside [App.java](https://github.com/sumanshu225/ErrorLogMonitoring/blob/main/ErrorLogMonitoring/src/App.java)
  
  ![image](https://github.com/sumanshu225/ErrorLogMonitoring/assets/86718491/cf8f77a2-d523-4b83-b0a1-2d153ce8c252)

 - Go inside ErrorLogMonitoring and run the following commands
   
   ```bash
      javac -d bin -cp "lib/*" src/*.java src/mypackage/*.java
      cd bin
      java -cp ".;../lib/*" App 
    ```

- `Sucessful` message will be displayed in cmd
-  Check `output.txt` inside src folder
  
![Screenshot 2024-05-25 192116](https://github.com/sumanshu225/ErrorLogMonitoring/assets/86718491/af9ae4a0-e252-4f9c-9f90-539aaac5e147)

## Approach
- We have a [LogTypeManager](https://github.com/sumanshu225/ErrorLogMonitoring/blob/main/ErrorLogMonitoring/src/mypackage/LogTypeManager.java) for each Log Type and a speacial Type `ALL`. This class is responsible for inserting data and making queries.
- Inside LogTypeManager we have methods for
  - `add()` for queryType 1 which takes O(N) time in worst case.
  - `get()` for queryType 2 which takes O(1) time.
  - `getBefore(long timeStamp)` for queryType 3&4 which takes O(1) time.
  - `getAfter(long timeStamp)` for queryType 3&4 which takes O(1) time.
- Inside LogTypeManager we maintain a `prefixStats`*(responsible for before query)* and `suffixStats`*(responsible for after query)* List
  - When inserting a new element prefix stats enter new node with `min`, `max`, `sum` by looking just previous value takes *O(1)* time
  - When inserting a new element suffix stats enter a new node with `min`, `max`, `sum` equal to severity and we update previous values by going backwards takes *O(N)* time.
