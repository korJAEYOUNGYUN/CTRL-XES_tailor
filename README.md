## CTRL-java-XESTailor

Tailoring [XES-based event logs](http://www.xes-standard.org/) with command-line based operations. Operations are designed based on relational algebra.

doi: 10.7472/jksii.2019.20.6.21

**Operations:**

- Select - parses the specific data from the event log.
- Union - integrates two logs which have same process model with different part.
- Slice - divides a log file into several log files by the number of traces.

## Usages

There are some command-line based usages. They are designed based SQL queries to be used easily.

- ```java
  open $(XES file);
  ```

  Get the file handler and do some preprocessing before using.

  

- ```java
  use $(XES file);
  ```

   Check which file to tailor. must be used after open.

  

- ```
  show;
  ```

   Show opened file lists.

  

- ```java
  slice $(num of traces);
  ```

   Divides the file by given num of traces. For example, slice by 500 to a file with 3000 traces, it will generate 6 files with 500 traces each.

  

- ```java
  select $(attributes) from $(element) where $(conditions)
  ```

  Parses specific element's attributes from current using file which meet given conditions.

  

- ```java
  hunion $(XES file);
  ```

  Integrates current using file with given xes file. It's process model must be equal to  the current one's and their contents are horizontally divided.

  

