package com.homeloan.project.http.controller;

import com.homeloan.project.http.ServiceResult;
import com.homeloan.project.http.requests.PrePaymentRequest;
import com.homeloan.project.model.Repayment;
import com.homeloan.project.service.IRepaymentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.Quota;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

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

    @GetMapping(value="/emi/export/{loan_id}", produces = "text/csv")
    public ResponseEntity<?> exportToCSV( @PathVariable String loan_id) throws IOException {


        List<Repayment> repay_list = repaymentService.getEmiByLoanId(loan_id);
        ByteArrayInputStream byteArrayOutputStream = null;

//        response.setContentType("text/csv");
        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                // defining the CSV printer
                CSVPrinter csvPrinter = new CSVPrinter(
                        new PrintWriter(out),
                        // withHeader is optional
                        CSVFormat.DEFAULT.withHeader("ID", "Date", "EMI","Principal Amount", "Interest Amount", "Outstanding", "Status")
                );
        ) {
            // populating the CSV content
            for (Repayment repay : repay_list) {
                csvPrinter.printRecord(repay.getId(), repay.getDate(), repay.getEmi(), repay.getInterestAmount(), repay.getOutstanding(), repay.getPrincipalAmount(), repay.getStatus());
            }
            // writing the underlying stream
            csvPrinter.flush();

            byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            log.error("Error While writing CSV ", e);
        }
        InputStreamResource fileInputStream = new InputStreamResource(byteArrayOutputStream);

        String csvFileName = "repayment_schedule.csv";
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + csvFileName);
        // defining the custom Content-Type
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");

        return new ResponseEntity<>(
                fileInputStream,
                headers,
                HttpStatus.OK
        );
    }

}
