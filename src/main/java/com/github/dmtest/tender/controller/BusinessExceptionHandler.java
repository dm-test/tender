package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class BusinessExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(BusinessExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public final ResponseEntity<OperationResultRsDto> handleBusinessException(BusinessException e) {
        OperationResultRsDto operationResultRsDto = new OperationResultRsDto(e.getOperationResult(), e.getDetailMessage());
        LOG.error(e.getDetailMessage(), e);
        return new ResponseEntity<>(operationResultRsDto, HttpStatus.OK);
    }
}
