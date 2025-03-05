package services.yas;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import models.OffreRecrutement;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class GoogleCalendarService {
    private static final String APPLICATION_NAME = "JavaFX Client";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_EVENTS);
    private static final Path TOKENS_DIRECTORY_PATH = Paths.get("tokens");
    private static final Path CREDENTIALS_PATH = Paths.get("src/main/resources/credentials.json");

    public static Credential getCredentials(final HttpTransport HTTP_TRANSPORT) throws Exception {
        if (!Files.exists(CREDENTIALS_PATH)) {
            throw new Exception("Error: credentials.json file not found at " + CREDENTIALS_PATH.toAbsolutePath());
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY, new InputStreamReader(new FileInputStream(CREDENTIALS_PATH.toFile()))
        );

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(TOKENS_DIRECTORY_PATH.toFile()))
                .setAccessType("offline")
                .build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static Calendar getCalendarService() throws Exception {
        HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = getCredentials(HTTP_TRANSPORT);

        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void createEvent(Calendar service, String summary, String description, Date startDate, Date endDate) throws Exception {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Event event = new Event()
                .setSummary(summary)
                .setDescription(description)
                .setStart(new EventDateTime()
                        .setDateTime(new com.google.api.client.util.DateTime(dateTimeFormat.format(startDate)))
                        .setTimeZone("UTC"))
                .setEnd(new EventDateTime()
                        .setDateTime(new com.google.api.client.util.DateTime(dateTimeFormat.format(endDate)))
                        .setTimeZone("UTC"));

        event = service.events().insert("primary", event).execute();
        System.out.println("Event created: " + event.getHtmlLink());
    }

    public static void addEventToCalendar(OffreRecrutement offre) {
        try {
            Calendar service = getCalendarService();

            // Define event details based on the OffreRecrutement object
            String summary = "Offre de recrutement pour le poste de " + offre.getPoste();
            String description = "Détails de l'offre de recrutement pour le poste de " + offre.getPoste() + ".";
            Date startDate = new Date(offre.getDate_pub().getTime());
            Date endDate = new Date(offre.getDate_limite().getTime());

            // Call the method to create and insert the event
            createEvent(service, summary, description, startDate, endDate);
        } catch (Exception e) {
            System.err.println("Error adding event to Google Calendar: " + e.getMessage());
        }
    }

}