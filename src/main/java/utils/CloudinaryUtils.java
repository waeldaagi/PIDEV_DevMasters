package utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CloudinaryUtils {

    private static final String CLOUD_NAME = "dx4csxrbp";
    private static final String API_KEY = "193184192781862";
    private static final String API_SECRET = "Sp9vbPD5hSuvFlnSHPzI0cBBLGw";

    private static final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", CLOUD_NAME,
            "api_key", API_KEY,
            "api_secret", API_SECRET
    ));

    public static String uploadImageToCloudinary(String imagePath) {
        try {
            File file = new File(imagePath);

            if (!file.exists()) {
                System.err.println("⚠️ File does not exist: " + file.getAbsolutePath());
                return null;
            }

            Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
