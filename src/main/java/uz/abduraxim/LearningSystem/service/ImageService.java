package uz.abduraxim.LearningSystem.service;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${SUPABASE_URL}")
    private String SUPABASE_URL;

    @Value("${SUPABASE_API_KEY}")
    private String SUPABASE_API_KEY;

    private final OkHttpClient client = new OkHttpClient();

    public String uploadImage(MultipartFile image) throws Exception {
        String filePath = UUID.randomUUID() + "_" + System.currentTimeMillis() + ".png";
        String imageUrl = SUPABASE_URL + "/storage/v1/object/learning-system/" + filePath;
        try {
            byte[] fileBytes = image.getBytes();
            RequestBody requestBody = RequestBody.create(
                    fileBytes, MediaType.parse("image/*") // Change to match your image type
            );
            Request request = new Request.Builder()
                    .url(imageUrl)
                    .header("Authorization", "Bearer " + SUPABASE_API_KEY)
                    .header("Content-Type", "image/*") // Change if uploading other image types
                    .put(requestBody)
                    .build();
            try {
                client.newCall(request).execute();
                return imageUrl;
            } catch (Exception e) {

                throw new Exception("Failed to upload image");
            }
        } catch (IOException e) {
            throw new Exception("Problem with image");
        }
    }
}
