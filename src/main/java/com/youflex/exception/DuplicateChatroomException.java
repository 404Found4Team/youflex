package com.youflex.exception;

// 런타임 시 발생하는 예외를 처리하기 위해 RuntimeException을 상속받습니다.
public class DuplicateChatroomException extends RuntimeException {
    
    public DuplicateChatroomException(String message) {
        super(message);
    }
}