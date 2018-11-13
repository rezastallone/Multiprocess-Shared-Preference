# Multiprocess-Shared-Preference
Wrapper around android shared preference to be accessible across multiple process
# Features
  - Insert string, integer, float, long, boolean & string set
  - Remove string, integer, float, long, boolean & string set
  - Get string, integer, float, long, boolean & string set
  - Check if preference contains a key
  - Get all key & value
# Installation

add this in your project level build.gradle at the end of the repositories
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
add the dependency
```
dependencies {
	        implementation 'com.github.rezastallone:Multiprocess-Shared-Preference:1.0'
	}
```
# Examples
```
val sharedPref = SharedPrefProvider(PREF_NAME)
sharedPref.setString(contentResolver,PREF_STRING ,"duck")
val stringVal = sharedPref.getString(contentResolver,PREF_STRING )
```
you can see more example and demonstration in multiprocess in the example project
