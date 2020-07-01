package rw.tajyire.api.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class CloudinaryService {
  @Autowired
  private Cloudinary cloudinaryConfig;

  public String uploadFile(MultipartFile file) {
    try {
      final String filename = file.getOriginalFilename();
      log.info("Start uploading file ==> ::{}:: to cloudinary service", filename);
      File uploadedFile = convertMultiPartToFile(file);
      Map uploadResult = cloudinaryConfig.uploader().upload(uploadedFile, ObjectUtils.emptyMap());
      removeFileLocally(filename);
      log.info("Uploading file ==> ::{}:: succeeded !!!", filename);
      return uploadResult.get("url").toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Uploads multiple files and returns a json of all their url image paths.
   *
   * @param files the list of {@link MultipartFile}s
   * @return a json string
   */
  public List<String> uploadMultipleFiles(List<MultipartFile> files) {
    return files.stream().map(this::uploadFile).collect(Collectors.toList());
  }

  public void removeFile(String fileName) throws IOException {
    cloudinaryConfig.uploader().destroy(fileName, ObjectUtils.emptyMap());
  }

  public void removeFileLocally(String fileName) {
    File f = new File(fileName);
    try {
      Files.deleteIfExists(f.toPath());
    } catch (IOException e) {
      log.error("Detailed error", e);
    }
  }

  public File convertMultiPartToFile(MultipartFile file) throws IOException {
    File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
    try (FileOutputStream fos = new FileOutputStream(convFile)) {
      fos.write(file.getBytes());
    }
    return convFile;
  }
}
