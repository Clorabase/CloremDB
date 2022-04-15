
# CloremDB ~ Object-storage database
<p align="left">
  <a href="#"><img alt="Version" src="https://img.shields.io/badge/Language-Java-1DA1F2?style=flat-square&logo=java"></a>
  <a href="#"><img alt="Bot" src="https://img.shields.io/badge/Version-2.5-green"></a>
  <a href="https://www.instagram.com/x__coder__x/"><img alt="Instagram - x__coder__" src="https://img.shields.io/badge/Instagram-x____coder____x-lightgrey"></a>
  <a href="#"><img alt="GitHub Repo stars" src="https://img.shields.io/github/stars/ErrorxCode/OTP-Verification-Api?style=social"></a>
  </p>

 This is implimentation of firebase realtime database as local database for android.
 CloremDB stores data in exact way as firebase does i.e in a JSON tree. 
 Yes, this is again a no-sql database that stores data in key-value paires. 
 The main aim of making this database is to make developer life easier. Programmer
 does not need to know that stupid SQL queries for just simple CRUD operations.
 This will be the easiest database ever made for android.

![image](https://cdn.educba.com/academy/wp-content/uploads/2019/05/what-is-Nosql-database1.png)


## Features

- Easy,lightweight and fast
- Data sorting using queries
- Direct object deserialization
- Capable of storing almost all primitive datatypes
- Use JSON structure for storing data
- Supports List<Integer> & List<String>

  
## Acknowledgements
 - [What is No-Sql](https://en.wikipedia.org/wiki/Key%E2%80%93value_database)
	
## Documentation
- [Javadocs](https://errorxcode.github.io/docs/clorem/index.html)
- [Guide](https://github.com/ErrorxCode/CloremDB/wiki/Guide)

  
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
	        implementation 'com.github.ErrorxCode:CloremDB:v2.5'
	}
```


## It's easy
```
Clorem.getInstance().addMyData().commit();
```
	
	

## Powered by ❤
#### [Clorabase](https://clorabase.netlify.app)
> A account-less platform as a service for android apps. (PaaS)
