package com.springdemo.library.controller.admin;

import com.springdemo.library.model.User;
import com.springdemo.library.repositories.UserRepository;
import com.springdemo.library.services.GenerateViewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@Slf4j
@AllArgsConstructor
@RequestMapping("/management/customers")
public class CustomerManagementController {

//Customer accounts_____________________________________________________________________________________________________

    private UserRepository userRepository;
    private GenerateViewService generateViewService;

    @GetMapping
    public ModelAndView viewCustomers(Authentication authentication) {
        List<User> customerList = userRepository.findAll();
        ModelAndView manageCustomerViewModel = generateViewService.generateStaffView("Quản lí Khách", "admin_and_staff/manageCustomer", authentication);
        manageCustomerViewModel.addObject("modelClass", customerList);
        return manageCustomerViewModel;
    }

    @PostMapping("/deactivateCustomer")
    @ResponseBody
    public ResponseEntity<String> deactivateCustomer(
            @RequestParam(name = "id") int id
    ) {
        try {
            User existedCustomer = userRepository.findById(id).orElse(null);
            if(existedCustomer!=null) {
                existedCustomer.setFlagDel(1);
                existedCustomer.setDateUpdated(new Date());
                userRepository.save(existedCustomer);
                return ResponseEntity.ok().build();
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Database error: " + e);
        } catch (NullPointerException e) {
            log.error("System error: " + e);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/activateCustomer")
    @ResponseBody
    public ResponseEntity<String> activateCustomer(
            @RequestParam(name = "id") int id
    ) {
        try {
            User existedCustomer = userRepository.findById(id).orElse(null);
            if(existedCustomer!=null) {
                existedCustomer.setFlagDel(0);
                existedCustomer.setDateUpdated(new Date());
                userRepository.save(existedCustomer);
                return ResponseEntity.ok().build();
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Database error: " + e);
        }
        return ResponseEntity.badRequest().build();
    }
}
