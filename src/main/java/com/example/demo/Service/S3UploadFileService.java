package com.example.demo.Service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

// asws의 s3 버킷에 파일을 업로드 및 url 획득 그리고 삭제 로직을 담당하는 서비스 계층
@Service
@Slf4j
@RequiredArgsConstructor
public class S3UploadFileService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    public static final String FOLDER="img/food/";
    public static final String EXTENSION =".jpg";
    private final AmazonS3Client amazonS3Client;


    // s3 버킷에 이미지파일을 업로드하는 메소드
    public void fileUpload(File imgFile, String fileName){
        try{
            String key=FOLDER + fileName;
            amazonS3Client.putObject(new PutObjectRequest(bucketName, key, imgFile));

        } catch (Exception e){
            log.info("업로드 실패");
        }

        //로컬에 존재하는 이미지 파일을 삭제

        if (imgFile.exists()) {
            imgFile.delete();
        }
    }

    // 해당 이미지의 url를 얻는 메소드
    public String getFileUrl(String foodName){
        // getUrl 메소드를 통해 특정 이미지 파일의 url를 얻어와 문자열로 변환
        return amazonS3Client.getUrl(bucketName,FOLDER + foodName+ EXTENSION).toString();
    }
    // 버킷에 존재하는 이미지 파일을 삭제하는 메소드

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
