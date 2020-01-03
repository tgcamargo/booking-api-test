import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BookingTest {

    public Faker faker = new Faker();
    public Random rand = new Random();
    public static SimpleDateFormat formatter;
    public static Date checkInInit;
    public static Date checkInFinal;
    public Calendar calendar = Calendar.getInstance();


    @BeforeClass
    public static void setup () throws ParseException {
        RestAssured.baseURI = "https://automationintesting.online";
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        checkInInit = formatter. parse("2001-01-01");
        checkInFinal = formatter.parse("2018-01-01");
    }

    //Test if the
    @Test
    public void getBookings () {
        given()
                .get ("/booking")
                .then ()
                .contentType(ContentType.JSON)
                .statusCode (200)
                .body("bookings.size()", greaterThan(2));
    }

    @Test
    public void getIndividualBooking () {
        given()
                .get ("/booking/1")
                .then ()
                .contentType(ContentType.JSON)
                .statusCode (200)
                .body("bookingid", is(1))
                .body("roomid", is(1))
                .body("firstname", is("James"))
                .body("lastname", is("Dean"))
                .body("depositpaid", is(true))
                .body("bookingdates.checkin", is("2019-01-01"))
                .body("bookingdates.checkout", is("2019-01-05"));
    }

    @Test
    public void createBookingWithDifferentTimeInformation(){
        String jsontest=
                "{ \"bookingdates\": " +
                    "{ \"checkin\": \"2020-01-02T23:20:24.395Z\", " +
                    "\"checkout\": \"2020-01-03\" }" +
                    ", \"bookingid\": 0, " +
                    "\"depositpaid\": true, " +
                    "\"email\": \""+faker.internet().emailAddress()+"\", " +
                    "\"firstname\": \""+faker.name().firstName()+"\"," +
                    " \"lastname\": \""+faker.name().lastName()+"\", " +
                    "\"phone\": \""+faker.phoneNumber().cellPhone()+"\", " +
                    "\"roomid\": "+(rand.nextInt(1000)+4)+"}";

        given()
                .contentType(ContentType.JSON)
                .body(jsontest)
                .post("/booking/")
                .then()
                .statusCode(201);
    }

    @Test
    public void createBookingForSameRoomInDifferentDate(){
        Date bookingDateCheckIn = faker.date().between(checkInInit,checkInFinal);
        calendar.setTime(bookingDateCheckIn);
        calendar.add( Calendar.DAY_OF_MONTH , 1 );
        Date bookingDateCheckOut = calendar.getTime();

        String bookingBody=
                "{ \"bookingdates\": " +
                        "{ \"checkin\": \""+formatter.format(bookingDateCheckIn)+"\", " +
                        "\"checkout\": \""+formatter.format(bookingDateCheckOut)+"\" }" +
                        ", \"bookingid\": 0, " +
                        "\"depositpaid\": true, " +
                        "\"email\": \""+faker.internet().emailAddress()+"\", " +
                        "\"firstname\": \""+faker.name().firstName()+"\"," +
                        " \"lastname\": \""+faker.name().lastName()+"\", " +
                        "\"phone\": \""+faker.phoneNumber().cellPhone()+"\", " +
                        "\"roomid\": 1}";

        given()
                .contentType(ContentType.JSON)
                .body(bookingBody)
                .post("/booking/")
                .then()
                .statusCode(201);
    }

    @Test
    public void createBookingForSameRoomInSameDate(){
        String bookingBody=
                "{ \"bookingdates\": " +
                        "{ \"checkin\": \"2019-01-01\", " +
                        "\"checkout\": \"2019-01-05\" }" +
                        ", \"bookingid\": 0, " +
                        "\"depositpaid\": true, " +
                        "\"email\": \""+faker.internet().emailAddress()+"\", " +
                        "\"firstname\": \""+faker.name().firstName()+"\"," +
                        " \"lastname\": \""+faker.name().lastName()+"\", " +
                        "\"phone\": \""+faker.phoneNumber().cellPhone()+"\", " +
                        "\"roomid\": 1}";

        given()
                .contentType(ContentType.JSON)
                .body(bookingBody)
                .post("/booking/")
                .then()
                .statusCode(409);
    }

    @Test
    public void createBookingWithCheckInDateLaterThanCheckOutDate(){
        String bookingBody=
                "{ \"bookingdates\": " +
                        "{ \"checkin\": \"2020-01-05\", " +
                        "\"checkout\": \"2020-01-01\" }" +
                        ", \"bookingid\": 0, " +
                        "\"depositpaid\": true, " +
                        "\"email\": \""+faker.internet().emailAddress()+"\", " +
                        "\"firstname\": \""+faker.name().firstName()+"\"," +
                        " \"lastname\": \""+faker.name().lastName()+"\", " +
                        "\"phone\": \""+faker.phoneNumber().cellPhone()+"\", " +
                        "\"roomid\": "+(rand.nextInt(1000)+4)+"}";

        given()
                .contentType(ContentType.JSON)
                .body(bookingBody)
                .post("/booking/")
                .then()
                .statusCode(409);
    }
}
