# watermark-service

Service to watermark documents from a global publishing company that publishes books and journals. Book publications include topics in business, science and media. Journals don’t include any specific topics. A document (books, journals) has a title, author and a watermark property. An empty watermark property indicates that the document has not been watermarked yet. This service is asynchronous.For a given content document the service should return a ticket, which can be used to poll the status of processing. If the watermarking is finished the document can be retrieved with the ticket. The watermark of a book or a journal is identified by setting the watermark property of the object. For a book the watermark includes the properties content, title, author and topic. The journal watermark includes the content, title and author.

# building and running

-Requirements:

* JDK 1.7
* MAVEN 3

-For building, go into the root directory of the project (watermark-service) and execute: <p>
```
mvn clean install
```

-For run Integration Test:
```
mvn verify -Pintegration-tests
```

-For start the service:
```
java -Xrunjdwp:transport=dt_socket,address=8787,server=y,suspend=n -jar target/watermark-service-0.0.1-SNAPSHOT.jar
```

# how to use

* Watermarking a book:
Do a POST with a book description using JSON format to http://localhost:8080/services/watermark/book. The ticket for getting the watermarked document will be in the body of the response.</br>
Example:</br>
```
curl -vX POST --header "Content-Type: application/json" http://localhost:8080/services/watermark/book   -d "{\"id\":\"10034\",  \"title\":\"Equilibrio Distante\",\"author\":\"Renato Russo\",\"topic\":\"MEDIA\"}"
```

Obs: Valid topics are "MEDIA", "SCIENCE" and "BUSINESS".

* Watermarking a journal:
Do a POST with a journal description using JSON format to http://localhost:8080/services/watermark/journal. The ticket for getting the watermarked document will be in the body of the response.</br>
Example:</br>
```
curl -vX POST --header "Content-Type: application/json" http://localhost:8080/services/watermark/journal   -d "{\"id\":\"20034\", \"title\":\"Como Nossos Pais\",\"author\":\"Elis Regina\"}"
```

* Checking status of a watermarking processing:</br>
Do a GET to ```http://localhost:8080/services/watermark/status/{ticket}``` and the result will be PROCESSING or NOT_PROCESSING

* Retrieve the watermarked document:</br>
Do a GET to ```http://localhost:8080/services/watermark/{ticket}``` and the result is the document in JSON format.
