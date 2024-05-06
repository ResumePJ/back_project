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
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

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

        //.png 파일 형태가 아니면 null 반환
        if(uniqueFileName==null){

            //기본값으로 초기화된 객체 생성
            MultipartUploadResponseDto multipartUploadResponseDto = new MultipartUploadResponseDto();
            multipartUploadResponseDto.setMessage("not png type");
            log.info("not .png type image upload");
            return multipartUploadResponseDto;
        }

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

    @Override
    public Resource download(int resumeNo) {
        String filePath = uploadPath+"\\";

        // 해당 이력서의 제일 최근 사진 가져옴
        MultipartUploadResponseDto image = basicInfoMapper.findImageByResumeNo(resumeNo);

        // 이력서에 사진이 아예 없을 경우
        if (image == null){
            log.info("이력서에 사진이 아예 없음!");
            return null;
        }

        String fileName = image.getFileName();
        log.info("fileName : {}",fileName);

        try{
            // 사진의 주소 가져옴
            // 실제 파일 시스템의 경로로 표준화 (normalize())
            Path path = Paths.get(filePath+fileName).normalize();
            log.info("file Path {}",path);
            Resource resource = new UrlResource(path.toUri());

            // 해당 주소에 해당 사진이 있다면?
            if(resource.exists()){
                return resource;
            }

            // DB에 기록은 있으나, 실제로는 원하는 filepath에 파일이 존재하지 않는 경우 등
            else{
                log.info("해당 주소에 실제 사진이 없음!");
                return null;
            }
        }catch (Exception e){
            return null;
        }

    }

    @Override
    public ResumeBasicInfoDTO updateResumeBasicInfo(int resumeNo, ResumeBasicInfoRequestDTO resumeBasicInfoRequestDTO) {
        int updateInfo= basicInfoMapper.updateResumeBasicInfo(resumeNo,resumeBasicInfoRequestDTO);

        // 한행에 대해 업데이트 성공 했다면?
        if(updateInfo==1){
            return basicInfoMapper.getResumeBasicInfo(resumeNo);
        }

        // 한행에 대해 업데이트를 하지 않았다면?
        else{
            return null;
        }
    }

    @Override
    public boolean isResume(int resumeNo) {
        int result = basicInfoMapper.isResume(resumeNo);

        // 존재하는 이력서 번호임
        if(result ==1){
            return true;
        }
        return false;
    }

    private String generateUniqueFileName(String originalFileName){
        //중복 될 가능성이 거의 없는 고유 식별자인 uuid 생성
        UUID uuid = UUID.randomUUID();

        /* TODO : 추후 확장자에 대해 처리 하기 위해, 파일이름/확장자를 나누어 관리함 - png만 업로드 할 수 있도록 수정 할 것*/

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

            // .png가 아니면 null을 리턴
            if(!extension.equals(".png")){
                return null;
            }

            // 0 ~ lastIndex-1까지 자름
            fileName = originalFileName.substring(0,lastIndex);
        }else{
            fileName= originalFileName;
        }

        log.info("generateUniqueFileName의 결과 : {}",uuid+ "_" + fileName+extension);
        return uuid+ "_" + fileName+extension;
    }


}
