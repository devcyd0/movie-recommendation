# Big Data Exercises (My solution)

This repo contains several common big data exercises.

* **MovieRecommender** Uses Amazon movie reviews sample data   [stanford.edu/data/web-Movies.html](http://snap.stanford.edu/data/web-Movies.html) for a simple movie recommender

    
 
 
## Setup

1. Install the JDK 7.0
2. [Download & Install Maven](http://maven.apache.org/download.cgi)
3. [Download movie reviews sample data](http://snap.stanford.edu/data/web-Movies.html), uncompress it and paste it into the repository root with the name **movies.txt**.
   
 
## How to run tests

    #from the repository root
    mvn test

    #if you get a 'protocol_version' error you can try
    mvn -Dhttps.protocols=TLSv1.2 test
