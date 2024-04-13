package com.resume.resu.vo.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MultipartUploadRequestDto {
    MultipartFile uploadFile;

    // resumeNo
    int resumeNo;

    int fileId;
}
