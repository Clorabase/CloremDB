
# CloremDB ~ Firebase as local database

This is implimentation of firebase realtime database as local database for android.
 CloremDB stores data in exact way as firebase does i.e in a JSON tree. 
 Yes, this is again a no-sql database that stores data in key-value paires. 
 The main aim of making this database is to make developer life easier. Programmer
 does not need to know that stupid SQL queries for just simple CRUD operations.
 This will be the easiest database ever made for android.

![image](https://instagram.fudr2-1.fna.fbcdn.net/v/t51.2885-15/e15/s640x640/244681217_584295309581460_6281762861824570294_n.jpg?_nc_ht=instagram.fudr2-1.fna.fbcdn.net&_nc_cat=100&_nc_ohc=i3ZgTESlsGoAX8WZ5cf&edm=AJXOVykBAAAA&ccb=7-4&oh=fb3e20f2e11eaeea0b852f5b9eabb7c8&oe=616F5A1C&_nc_sid=9c1db7&ig_cache_key=MjY3OTMyNjMwMDQ5MjgxNjM1MA%3D%3D.2-ccb7-4)

## Features

- Easy,lightweight and fast
- Data sorting using queries
- Capable of storing almost all primitive datatypes
- Use JSON structure for storing data
- Supports List<Integer> & ArrayList<String>

  
## Acknowledgements
 - [What is No-Sql](https://en.wikipedia.org/wiki/NoSQL)
	
## Documentation
- [Javadocs](https://errorxcode.github.io/docs/clorem/index.html)

  
## Deployment / Installation
 In your project build.gradle
```groovy
 allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
In your app build.gradle
```groovy
dependencies {
	        implementation 'com.github.ErrorxCode:CloremDB:v1.0'
	}
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


***Note** : Please refer to [javadocs](https://errorxcode.github.io/docs/clorem/index.html) for detailed information of each method*
Alternatively, you can also hover or press ctrl + method name to view javadocs.
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
 *This structure will be considered for every example below*.

What we have done is that first we created a new node named 'contributor' 
using `newNode("contributor")` method. Then we created another node
under same node with 3 children [using `put("instagram","null")` ]
. Since we were in node "Anas", we have to go back to 'contributor' node to
make another child. For that, we use `back()` method...... Finally we commited it to
database using `commit()` method.


### CRUD Operations
**Note: First you have to go to the parent node of the child on which you wish to perform operation.*
#### Create
lets add the email of rahil, for that first you have to go to "Rahil" node.
```java
db.node("contributor")
    .node("Rahil")
    .put("email","inbox@xcoder.tk")
    .commit();
```


#### Read
lets read the github username of anas, for that first you have to go to "Anas" node.
```java
String username = db.node("contributor").node("Anas").getString("github");
```
**Note : We only have to commit() when writing to database.*

#### Update
lets change the instagram username of shubam, for that first you have to go to "Shubam" node.
```java
db.node("contributor")
    .node("Shubam")
    .put("instagram","new username")
    .commit();
```

#### Delete
lets delete the website of rahil, for that first you have to go to "Rahil" node.
```java
db.node("contributor")
    .node("Rahil")
    .remove("website")
    .commit()
```
or to delete the whole node,
```java
db.node("contributor")
    .remove("Rahil")
    .commit()
```

### Sorting

You can also sort data from database as you do in sqllite. You can run queries on node with some conditions.
To query, you **first need to go on grandparent node of the child you wish to retrive**.
For example, if I have to get the name of the user whose username is "weshubh" then, I have to call queries on "contributor" node.

Lets get the name of user whose username contains 'e'.
```java
List<String> keys = db.node("contributor").query().whereContains("instagram","e");
// this will return "Rahil" and "Shubam"
```
Similarly you can query for `whereEqual`,`whereSmaller`,`whereGreater` or `whereBoolean`.
