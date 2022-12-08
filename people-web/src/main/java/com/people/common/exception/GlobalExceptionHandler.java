package com.people.common.exception;

import static com.people.common.consts.ErrorCode.INTERNAL_SERVER_ERROR;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.people.common.oldutil.SystemUtil;
import com.people.common.vo.ErrorVO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ CustomException.class })
    protected ResponseEntity<ErrorVO> handleCustomException(CustomException ex) {
        return new ResponseEntity<ErrorVO>(  new ErrorVO(  ex.getErrorCode().getHttpStatus()
        		                                         , ex.getErrorCode().getResultCode()
        		                                         , ex.getErrorCode().getResultMessage()
        		                                        )
        		                           , HttpStatus.valueOf(ex.getErrorCode().getHttpStatus())
        		                          );
    }

    @ExceptionHandler({ Exception.class })
    protected ResponseEntity<ErrorVO> handleServerException(Exception ex) {
        return new ResponseEntity<ErrorVO>(  new ErrorVO(   INTERNAL_SERVER_ERROR.getHttpStatus()
        		                                          , INTERNAL_SERVER_ERROR.getResultCode()
        		                                          , SystemUtil.getExceptionLog(ex)
        		                                        )
        		                           , HttpStatus.INTERNAL_SERVER_ERROR
        		                          );
    }
}