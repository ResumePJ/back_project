package com.resume.resu.exception;

import lombok.Getter;

@Getter
public class MissingTokenException extends RuntimeException{
    public MissingTokenException(String message){

        // RuntimeException(부모 클래스)의 예외 메세지 설정하는 생성자 호출
            super(message);

    }
}
