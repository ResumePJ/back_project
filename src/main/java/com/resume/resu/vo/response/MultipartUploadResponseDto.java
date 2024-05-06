package com.resume.resu.vo.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MultipartUploadResponseDto {
    int fileId;
    int resumeNo;
    String fileName;
    LocalDateTime uploadDate;
    String message;
}
