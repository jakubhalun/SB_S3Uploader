package pl.halun.tools.s3upload.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class UploadController {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
    private AmazonS3 s3client;

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload")
    public String fileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:status";
        }
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());

        try {
            s3client.putObject(new PutObjectRequest(bucket, file.getOriginalFilename(),
                    file.getInputStream(), objectMetadata));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/status";
    }

    @GetMapping("/status")
    public String uploadStatus() {
        return "status";
    }

}
