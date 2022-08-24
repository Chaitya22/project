package com.homeloan.project.http.controller;

import com.homeloan.project.http.ServiceResult;
import com.homeloan.project.http.requests.PrePaymentRequest;
import com.homeloan.project.service.IRepaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/repay/")
@Slf4j
public class RepaymentController {
    @Autowired
    IRepaymentService repaymentService;

    @GetMapping(value = "/emi/{emi_id}")
    public ServiceResult<?> findEmiById(@PathVariable int emi_id){
        try {
            return new ServiceResult<>(repaymentService.getEmiById(emi_id));
        }
        catch(Exception e){
            log.error("Error occurred while fetching all EMIs data: " + e.getMessage());
            return new ServiceResult<>("Error occurred while fetching all EMIs data");
        }
    }

    @PostMapping(value = "/emi/pay/{loan_id}" )
    public ServiceResult<?> payEmi(@PathVariable String loan_id){
        try {
            return new ServiceResult<>(repaymentService.payEmi(loan_id));
        }
        catch(Exception e){
            log.error("Error occurred while fetching all EMIs data: " + e.getMessage());
            return new ServiceResult<>("Error occurred while fetching all EMIs data");
        }
    }

    @PostMapping(value = "/emi/forePay/{loan_id}")
    public ServiceResult<?> forPayEmi(@PathVariable String loan_id){
        try {

            return new ServiceResult<>(repaymentService.forPayEmi(loan_id));
        }
        catch(Exception e){
            log.error("Error occurred while fetching all EMIs data: " + e.getMessage());
            return new ServiceResult<>("Error occurred while fetching all EMIs data");
        }
    }

    @PostMapping(value = "/emi/prePay/{loan_id}")
    public ServiceResult<?> prePayEmi(@PathVariable String loan_id,@RequestBody PrePaymentRequest req){
        try {

            return new ServiceResult<>(repaymentService.prePayEmi(loan_id,req.getMonths()));
        }
        catch(Exception e){
            log.error("Error occurred while fetching all EMIs data: " + e.getMessage());
            return new ServiceResult<>("Error occurred while fetching all EMIs data");
        }
    }
}
