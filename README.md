

<h1 align="center">
  <br>
  <a href="http://www.amitmerchant.com/electron-markdownify"><img src="https://github.com/Clorabase/CloremDB/blob/main/image.png?raw=true" alt="Markdownify" width="300"></a>
  <br>
  CloremDB
  <br>
</h1>

<h4 align="center">A child's play NO-SQL database for java aaps</h4>

<p align="center">
  <img src="https://img.shields.io/badge/Version-4.0-green?style=for-the-badge">
  <img src="https://img.shields.io/github/license/ErrorxCode/CloremDB?style=for-the-badge">
  <img src="https://img.shields.io/github/stars/ErrorxCode/CloremDB?style=for-the-badge">
  <img src="https://img.shields.io/github/issues/ErrorxCode/CloremDB?color=red&style=for-the-badge">
  <img src="https://img.shields.io/github/forks/ErrorxCode/CloremDB?color=teal&style=for-the-badge">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Author-Rahil--Khan-cyan?style=flat-square">
  <img src="https://img.shields.io/badge/Open%20Source-Yes-cyan?style=flat-square">
  <img src="https://img.shields.io/badge/Written%20In-Java-cyan?style=flat-square">
</p>

<p align="center">
  <a href="#key-features">Key Features</a> •
  <a href="#Usage">How To Use</a> •
  <a href="#Implimentation">Implimentation</a> 
</p>

<img src="https://github.com/Clorabase/CloremDB/blob/main/clorem%20banner.png?raw=true" alt="Markdownify" width="1000">

CloremDB is a simple, robust and easy to use embedded NO-SQL database for java applications. The data is directly saved as objects in the database. You can directly put POJO's in the database and can organize them is different volume. Each volume is like a SQL table which contains objects (as rows). You can have unlimited number of volumes and objects in it.

*I bet you will not find database easier and powerful than this*

## 🎯Key Features

* Super simple, made using the most simple design ever
* Moders query, No query builder. Just direct methods
* Can directly store/update/fetch objects (POJO's)

## 💉Implimentation 
[![](https://jitpack.io/v/ErrorxCode/CloremDB.svg)](https://jitpack.io/#Clorabase/CloremDB)
### Gradle :-
Add it in your root build.gradle at the end of repositories:

```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

**Step 2.**  Add the dependency

```groovy
	dependencies {
	        implementation 'com.github.Clorabase:CloremDB:Tag'
	}
```

### Maven :-
**Step 1**. Add to project level file
```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

**Step 2.**  Add the dependency

```xml
	<dependency>
	    <groupId>com.github.ErrorxCode</groupId>
	    <artifactId>CloremDB</artifactId>
	    <version>Tag</version>
	</dependency>
```

## 📃Documentation
After this guide, I guarantee, you will never be required to see the documentation of this database. This guide will explain everything step-by-step with examples. So let's begin with the index.

It would be best if you see [JavaDocs](https://errorxcode.github.io/docs/clorem/index.html) and acknowledgment first before diving into the guide. Anyway, let's get started.

### Initialization
First of all, you need to implement `CloremObject` interface in the every class of which you wanna save data in the database.
**For example**:
```java
public class Book implements CloremObject {
    private int id;
    private String title;
    private String isbn;
    private int authorId;
    private int publisherId;

    // Constructor
    public Book(int id, String title, String isbn, int authorId, int publisherId) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.authorId = authorId;
        this.publisherId = publisherId;
    }

    @Override
    public String toString() {
        return "Book{id=" + id + ", title='" + title + "', isbn='" + isbn + "', authorId=" + authorId + ", publisherId=" + publisherId + "}";
    }

    @Override
    public String getKey() {
        return String.valueOf(id);
    }

    @Override
    public String getVolume() {
        return "Library.Books";
    }

}
```
then initialize the database as like this,
```java
Clorem db = Clorem.getInstance(databaseDir);
```
After that, you can get instance of a volume. If the volume does not already exists, it will be created
```java
Volume vol = db.volume("Library").volume("Books");
```
Your object class must return the **Volume name** in which the object will be saved.


###  Putting data
Putting data is very simple. You just need to implement `CloremOBject` in you POJO to save it in database.
```java
db.insert(new Book(1,"Test book","1234567890",1,1));        // You can also directly insert into the volume from db instance
vol.insert(new Book(2,"Test book 2","1234567891",2,2));
vol.insert(new Book(3,"Test book 3","1234567892",3,3));
vol.insert(new Book(4,"Test book 4","1234567893",4,4));
```

### Fetching data
Just use `fetch()` method of volume class, passing the key of the object you wish to rertrive. You need to cast it to the desired class manually.
```java
System.out.println(vol.fetch("1"));
System.out.println(vol.fetch("2"));
System.out.println(vol.fetch("3"));
System.out.println(vol.fetch("4"));
```

### Updating data
There are two ways of updating an object. One for updating/overwriting whole object and another one for updating only required field.

**Updating the objects** :
```java
vol.update("2",Book.class,book -> book.setTitle("Test book 2 updated"));
vol.update("2",Book.class,book -> book.setTitle("Test book 2 updated"));
```
where, "xyz" is the key of the object I want to update. **That must be unchanged**


### Deleting data/objects
To delete any object, simply call `delete(key)` method. To delete whole volume, use `deleteVolume()`
```java
vol.delete("3");
vol.delete("4");
vol.deleteVolume();
```

### Querying data
CloremDB has the most easy querying compared to any other database. You just need to pass a `Predicate` to a function which specify the condition you want to query for. The query will inclue the object for every `test(Object)` (your predicate function) returning true.

For example, I want to get users whose name contains 'a'.
```java
List<User> list = vol.query(User.class, user -> user.name.contains("a"));
```

Here is how it works,
```java
List<User> list = db.query("demoVolume", User.class, new Predicate<User>() {  
    @Override  
  public boolean test(User user) {  
      // check the object, if it meets your condition  
	 // then return true, otherwise false
	  return user.name.equals("rahil");  
    }  
});
```

***That's all what you need to learn about this database :)***


## 🆘Support

If you like my work then you can suppot me by giving this repo a ⭐. You can check my other repos as well, if you found this library userfull then you will definetly fine more in my profiles.

## You may also like...

- [ClorastoreDB](https://github.com/Clorabase/ClorastoreDB) - A mongoDB/firestore database offline version
- [ClorographDB](https://github.com/Clorabase/ClorographDB) - CLorabase offline graph database


## Powered by 💓
#### [Clorabase](https://github.com/clorabase)
> A account less backend for android
