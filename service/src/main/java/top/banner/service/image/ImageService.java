package top.banner.service.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.banner.common.exception.ValidationException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${image.path}")
    private String imagePath;
    @Value("${image.host}")
    private String imageHost;


    /**
     * 图片上传
     *
     * @param file
     * @return
     */
    public ImgUrlVO upload(MultipartFile file) {

        ImgUrlVO imgUrlVO = new ImgUrlVO();

        File newFile = new File(imagePath);
        if (!newFile.exists()) {
            newFile.mkdirs();
        }

        String imageName = UUID.randomUUID().toString().replaceAll("-", "") + ".png";
        String absolutePath = newFile.getAbsolutePath() + File.separator + imageName;

        try {
            buff2Image(file.getBytes(), absolutePath);
            String url = imageHost + "/" + imageName;
            imgUrlVO.setUrl(url);
        } catch (Exception e) {
            throw new ValidationException("上传失败");
        }
        return imgUrlVO;
    }

    public static void buff2Image(byte[] b, String tagSrc) {
        try (FileOutputStream outputStream = new FileOutputStream(tagSrc)) {
            outputStream.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
