package utils;

import java.util.logging.Logger;

public class OTPService {
    private static final Logger logger = Logger.getLogger(OTPService.class.getName());

    public String generateOtp() {
        String otp = String.format("%06d", (int) (Math.random() * 1000000));
        logger.info("Generated OTP: " + otp);
        return otp;
    }

    public static void main(String[] args) {
        OTPService otpService = new OTPService();
        System.out.println("Generated OTP: " + otpService.generateOtp());
    }
}