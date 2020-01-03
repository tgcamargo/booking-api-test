# Booking API Test

The purpose of this project is to test the API https://automationintesting.online/booking/ in the following requisites:

* **getBookings:** Test that at least 2 existing bookings are returned in the response. 
* **getBooking:** Test that the data returned for an existing booking matches.
* **createBooking:** Test that bookings can be created. Keep in mind these restrictions:
    - A room cannot be booked more than once for a given date.
    - The check-out date must be greater than the check-in date.
    - The dates can optionally include time information (e.g. "2019-01-30" or "2019-01-30T10:09:18.840Z")
    - The response code 409 will be returned if any of the above validations fail. 

## Getting Started

### Prerequisites

* Java IDE
* Maven

Open the pom.xml file and import the settings for your environment.

### Running the tests

The tests can be run using JUnit runner on your IDE or can be run by maven.
If the tests are run in maven they'll run in parallel mode.

Maven command:
```
//First run:
mvn install

//Following runs:
mvn test
```
## Built With

* [IntelliJ IDEA](https://www.jetbrains.com/idea/) - The java IDE used
* [Maven](https://maven.apache.org/) - Dependency Management
* [Rest-assured](http://rest-assured.io/) - Used to test and validate REST services
* [Java Faker](https://github.com/DiUS/java-faker) - Used to generate random data for tests

## Author

**Thiago Camargo** 