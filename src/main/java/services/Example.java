package services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Example {
    // Twilio credentials
    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN = "";

    // Initialize Twilio
    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    // Method to send SMS to a specific phone number
    public static void sendSms(String toPhoneNumber, String messageBody) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(toPhoneNumber),  // To phone number
                    new PhoneNumber("+12256599520"),  // From phone number (Twilio number)
                    messageBody                      // Message body
            ).create();

            // Print the SID of the sent message
            System.out.println("Message SID: " + message.getSid());
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi du SMS : " + e.getMessage());
        }
    }
}


