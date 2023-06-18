package com.example.demo.Service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;


@Service
@Slf4j
@RequiredArgsConstructor
public class S3UploadFileService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    public static final String FOLDER="img/food/";
    public static final String EXTENSION =".jpg";
    private final AmazonS3Client amazonS3Client;


    public void fileUpload(File imgFile, String fileName){
        try{
            String key=FOLDER + fileName;
            amazonS3Client.putObject(new PutObjectRequest(bucketName, key, imgFile));

        } catch (Exception e){
            log.info("업로드 실패");
        }
        if (imgFile.exists()) {
            imgFile.delete();
        }
    }
    public String getFileUrl(String foodName){
        //메뉴 수정/삭제에 사용
        return amazonS3Client.getUrl(bucketName,FOLDER + foodName+ EXTENSION).toString();
    }
    public void getFileUrlList(){
        //장바구니 , 메뉴 페이지에 사용
    }
    public void delete(String foodName){
        try {
            String key=FOLDER + foodName+ EXTENSION;
            boolean isObjectExist = amazonS3Client.doesObjectExist(bucketName,key);
            if (isObjectExist) {
                amazonS3Client.deleteObject(bucketName, key);
            } else {
                log.info("파일이 없습니다.");
            }
        } catch (Exception e) {
            log.debug("Delete File failed", e);
        }
    }

}
