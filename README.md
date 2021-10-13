
# CloremDB ~ Firebase as local database

This is implimentation of firebase realtime database as local database for android.
 CloremDB stores data in exact way as firebase does i.e in a JSON tree. 
 Yes, this is again a no-sql database that stores data in key-value paires. 
 The main aim of making this database is to make developer life easier. Programmer
 does not need to know that stupid SQL queries for just simple CRUD operations.
 This will be the easiest database ever made for android.




## Features

- Easy,lightweight and fast
- Data sorting using queries
- Capable of storing almost all primitive datatypes
- Use JSON structure for storing data
- Supports List<Integer> & ArrayList<String>

  
## Acknowledgements

 - [Awesome Readme Templates](https://awesomeopensource.com/project/elangosundar/awesome-README-templates)
 - [Awesome README](https://github.com/matiassingers/awesome-readme)
 - [How to write a Good readme](https://bulldogjob.com/news/449-how-to-write-a-good-readme-for-your-github-project)

  
## Deployment / Installation

To impliment this database

```bash
  npm run deploy
```


## Usage / Examples
### Methods overview

```java
Database db = Clorem.getInstance(this,"demo").getDatabase()
```
This returns the root node of database.


```java
db.newNode("name")
```
 Creates a new node in current node i.e root.


```java
db.node("name") 
```
Moves to the the node "name" of the current node.



```java
db.put("key","valye")
```
Puts a new key-value pair in the current node.


```java
db.back()
```
Moves one node back from the current node.



```java
db.commit()
```
Finally, commits all the changes made to database.


***Note** : Please refer to [javadocs](htt) for detailed information of each method*
.

### Storing data in database.
This code will save information of contributer in database "demo".
```java
Clorem.getInstance(this,"demo").getDatabase()
        .newNode("contributor")
        .newNode("Rahil")
        .put("instagram","@x__coder__x")
        .put("github","@ErrorxCode")
        .put("website","xcoder.tk")
        .back()
        .newNode("Shubam")
        .put("instagram","@weshubh")
        .put("github","@shubhamp98")
        .put("website","shubhamp98.github.io")
        .back()
        .newNode("Anas")
        .put("instagram","null")
        .put("github","@anas43950")
        .put("website","null")
        .commit();
```
This will result in JSON structure like this :
```json
{
  "contributor": {
    "Anas": {
      "github": "@anas43950",
      "website": "null",
      "instagram": "null"
    },
    "Shubam": {
      "github": "@shubhamp98",
      "website": "shubhamp98.github.io",
      "instagram": "@weshubh"
    },
    "Rahil": {
      "github": "@ErrorxCode",
      "website": "xcoder.tk",
      "instagram": "@x__coder__x"
    }
  }
}
```

What we have done is that first we created a new node named 'contributor' 
using `newNode("contributor")` method. Then we created another node
under same node with 3 children [using `put("instagram","null")` ]
. Since we were in node "Anas", we have to go back to 'contributor' node to
make another child. For that, we use `back()` method...... Finally we commited it to
database using `commit()` method.


### CRUD Operations

To update a value
```java

```
