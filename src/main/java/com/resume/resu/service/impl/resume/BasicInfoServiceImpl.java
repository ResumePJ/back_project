package com.resume.resu.service.impl.resume;

import com.resume.resu.repository.resume.BasicInfoMapper;
import com.resume.resu.service.api.resume.BasicInfoService;
import com.resume.resu.vo.request.MultipartUploadRequestDto;
import com.resume.resu.vo.request.ResumeBasicInfoRequestDTO;
import com.resume.resu.vo.response.MemberDTO;
import com.resume.resu.vo.response.MultipartUploadResponseDto;
import com.resume.resu.vo.response.ResumeBasicInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicInfoServiceImpl implements BasicInfoService {

    //TODO :  사원의 정보 받아오는 것 만들 것

    public final BasicInfoMapper basicInfoMapper;


    private String uploadPath = "C:\\Resu\\BackEnd\\upload";

    @Override
    public boolean isMyResume(int memberNo, int resumeNo) {
         int count = basicInfoMapper.isMyResume(memberNo,resumeNo);
         if(count==0){
             return false;
         }
         return true;
    }

    @Override
    public ResumeBasicInfoDTO getResumeBasicInfo(int resumeNo) {
        return basicInfoMapper.getResumeBasicInfo(resumeNo);
    }

    @Override
    public MemberDTO getMemberInfo(int memberNo) {
        return basicInfoMapper.getMemberInfo(memberNo);
    }

    @Override
    public int insertResumeBasicInfo(ResumeBasicInfoRequestDTO resumeBasicInfoRequestDTO, MemberDTO memberInfo) {
        return basicInfoMapper.insertResumeBasicInfo(resumeBasicInfoRequestDTO,memberInfo);
    }

    @Override
    public int findLastResumeNoById(int memberNo) {
        return basicInfoMapper.findLastResumeNoById(memberNo);
    }

    @Override
    public MultipartUploadResponseDto uploadFile(MultipartUploadRequestDto dto) {
        MultipartFile file = dto.getUploadFile();

        // 파일 이름에서 주소로 사용할 수 없는 위험한 문자 제거해 안전한 파일 이름으로 변환 -> 파일 업로드 과정에서 보안 취약점 방지, 파일 이름 안전하게 처리 위함
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

        // 유니크한 파일 이름 생성
        String uniqueFileName = generateUniqueFileName(originalFileName);

        // 문자열 경로로부터 Path 객체 생성 -> Files.exists()의 인자는 Path형이기 때문
        Path directoryPath = Paths.get(uploadPath);

        // 해당 경로에 파일/폴더가 없다면
        if(!Files.exists(directoryPath)){
            try{
                Files.createDirectories(directoryPath);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }

        // Paths.get(uploadPath)의 결과 경로에 uniqueFileName을 결합해 새로운 경로 생성
        Path targetLocation = Paths.get(uploadPath).resolve(uniqueFileName);

        try{
            // 업로드 파일의 입력스트림을 가져와 targetLocation 경로에 복사
            // 업로드 파일을 targetLocation 경로에 저장
            Files.copy(file.getInputStream(),targetLocation);
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        // 파일 복사 후, DB에 기록 저장
        basicInfoMapper.upload(dto,uniqueFileName);
        int generatedFileId=dto.getFileId();
        MultipartUploadResponseDto result = basicInfoMapper.findByFileId(generatedFileId);

        return result;
    }

    private String generateUniqueFileName(String originalFileName){
        //중복 될 가능성이 거의 없는 고유 식별자인 uuid 생성
        UUID uuid = UUID.randomUUID();

        /* TODO : 추후 확장자에 대해 처리 하기 위해, 파일이름/확장자를 나누어 관리함*/

        /* .이 마지막으로 존재하는 곳의 인덱스
        * 찾는 문자가 없는 경우 -1 반환
        * */
        int lastIndex = originalFileName.lastIndexOf('.');

        String extension=""; // 확장자
        String fileName;

        // 찾는 문자가 있는 경우
        if(lastIndex != -1){
            // 인덱스부터 자름 ('.확장자' 부분을 자름)
            extension = originalFileName.substring(lastIndex);

            // 0 ~ lastIndex-1까지 자름
            fileName = originalFileName.substring(0,lastIndex);
        }else{
            fileName= originalFileName;
        }

        log.info("generateUniqueFileName의 결과 : {}",uuid+ "_" + fileName+extension);
        return uuid+ "_" + fileName+extension;
    }


}
