

<h1 align="center">
  <br>
  <a href="http://www.amitmerchant.com/electron-markdownify"><img src="https://github.com/Clorabase/CloremDB/blob/main/image.png?raw=true" alt="Markdownify" width="300"></a>
  <br>
  CloremDB
  <br>
</h1>

<h4 align="center">A child's play NO-SQL database for java aaps</h4>

<p align="center">
  <img src="https://img.shields.io/badge/Version-1.0-green?style=for-the-badge">
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
* Moders query, No query builder. Predicates are used rather.
* Can directly cache objects (POJO's)

## 💉Implimentation 
[![](https://jitpack.io/v/ErrorxCode/CloremDB.svg)](https://jitpack.io/#ErrorxCode/Clorine)
### Gradle :-
Add it in your root build.gradle at the end of repositories:

```css
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

**Step 2.**  Add the dependency

```css
	dependencies {
	        implementation 'com.github.ErrorxCode:CloremDB:Tag'
	}
```

### Maven :-
**Step 1**. Add to project level file
```markup
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

**Step 2.**  Add the dependency

```markup
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
class User implements CloremObject{  
    public String name;  
    public String password;  
    public String id;  
  
    public User(String id,String name, String password) {  
        this.name = name;  
        this.password = password;  
        this.id = id;  
    }  
  
    @Override  
	public String getKey() {  
        return id;  
    }  
  
    @Override  
    public String getVolume() {  
       return "demoVolume";  
    }  
  
    @Override  
    public String toString() {  
        return "User{" +  
                "name='" + name + '\'' +  
                ", password='" + password + '\'' +  
                ", id='" + id + '\'' +  
                '}';  
    }  
}
```
then initialize the database as like this,
```java
Clorem db = Clorem.getInstance(databaseDir);
```
After that, for the first time, you need to create volume. Volume is like SQL table in which your object will be stored.
```java
db.createVolume("demoVolume");
```
Your object class must return the **Volume name** in which the object will be saved.


###  Putting data
Putting data is very simple. You just need to implement `CloremOBject` in you POJO to save it in database.
```java
db.put(new User("xyz","rahil","123"));  
db.put(new User("key","nandan","any"));  
db.put(new User("x0x","tabish","asdf"));
```

### Fetching data
Just use `get()` method passing the key of the object you wish to rertrive. You need to cast it to the desired class manually.
```java
User obj = (User) db.get("xyz","demoVolume");  
System.out.println(obj.name);  // prints 'rahil'
```

### Updating data
There are two ways of updating an object. One for updating/overwriting whole object and another one for updating only required field.

**To overwrite whole object**:
```java
db.update(new User("xyz", "rahil", "khan"));
```
where, "xyz" is the key of the object I want to update. **That must be unchanged**

**To update required fields**:
```java
db.update("xyz", "demoVolume", User.class, user -> {  
    user.id = "abc";  
    user.name = "Rahil";  
});
```
This will only update the changes you have made to `user` object.


### Deleting data/objects
To delete any object, simply call `delete(key,volume)` method. To delete whole volume, use `deleteVolume(name)`
```java
db.delete("xyz", "demoVolume");  
db.deleteVolume("demoVolume");
```

### Querying data
CloremDB has the most easy querying compared to any other database. You just need to pass a `Predicate` to a function which specify the condition you want to query for. The query will inclue the object for every `test(Object)` (your predicate function) returning true.

For example, I want to get users whose name contains 'a'.
```java
List<User> list = db.query("demoVolume", User.class, user -> user.name.contains("a"));
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
- [ClorographDB](https://github.com/amitmerchant1990/correo) - CLorabase offline graph database


## Powered by 💓
#### [Clorabase](https://github.com/clorabase)
> A account less backend for android
