# prime-numbers-API
Hello! Inside this repository lie two services - one called prime-number-server which calculates 
list of prime numbers up to a specified number and proxy-server which exposes an API endpoint and returns the values. The communication between those two services
is via gRPC. Each of the service contains unit test.

## Implementation choices
### Languague and IDE
I liked the syntax of scala, flexibility, possibility to write in an object oriented as well as functional way. I look forward to get closer to the syntax even more
and especially to leverage all the functional capabilities. As an IDE I chose [Intellij Idea](https://www.jetbrains.com/idea/) from jetbrains which I really liked and for a person without
Scala or Java background the environment was nice and helpful with all the possibilities, shortucts, etc. I also used the recommended sbt tool for creating the
projects which helped a lot with all the settings.


### Modules
I chose gRPC for communication between both sevices. I made this decision as I wanted to used [akka-http](https://doc.akka.io/docs/akka-http/current/index.html) 
for creating the routes and the [akka-grpc](https://doc.akka.io/docs/akka-grpc/current/index.html) was an automatic decision with the thought of working with those two
modules that are from the same company (which is at the same time created by the author of Scala programming langugage). Streaming the values was done by using
[akka-stream](https://doc.akka.io/docs/akka/current/stream/index.html). The streaming class needed some changes as none of the CSV or JSON Entity Streaming Support
 would allow me to stream the results separated by comma and put period at the end. Therefore, I wrote ```CommaSeparatedEntityStreamingSupport``` class where I took
 parts from both CSV and JSON streaming. I used [scalaMock](https://scalamock.org) and [scalaTest](https://www.scalatest.org) for creating basic scenario test cases.


## Running the project
1. Clone the repository to your local device
    ```
    git clone https://github.com/charouzic/prime-numbers-API.git
    ```
    
a) running the prime-number-server

* Navigate to ```prime-number-server``` folder

* open sbt shell by just running 
   ```
   sbt
   ```
* run the service by cmd
   ```
   runMain prime.PrimeServer
   ```
* The gRPC client is now accessible on port http://localhost:8080


b) running the proxy-server

* Navigate to ```proxy-server``` folder

* open sbt shell by just running 
  ```
  sbt
  ```

* run the service by cmd
  ```
  runMain proxy.Main
  ```
  
* The API endpoint is available at http://localhost:9090 -> call e.g.: http://localhost:9090/prime/17 to get the prime number sequence

(Both services are of course possible to run from an IDE like Intellij IDEA)

## TODOS for Future me
* Create a docker file to run this solution in -> making it easier to run both services simultaneusly without starting them one by one

* Adapt the code to run the solution using the most up to date version of akka because now it runs on the older version (2.5.31)

* Create integration tests that would check both services for possible errors with every single push and pull request
