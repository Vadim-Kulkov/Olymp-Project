package com.olymp_project.controller;

import com.olymp_project.model.Account;
import com.olymp_project.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController()
@RequestMapping("/accounts")
public class AccountController {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping(value = "/{accountId}")
    public Object findById(@PathVariable("accountId") Long accountId) {
        if (Objects.isNull(accountId) || accountId <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Account> result = accountRepository.findById(accountId);
        return result.isPresent() ? result.get() : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/search")
    public Object searchByParameters(@RequestParam Map<String, String> searchParams) {
        String firstName = searchParams.get("firstName");
        String lastName = searchParams.get("lastName");
        String email = searchParams.get("email");

        String receivedFrom = searchParams.get("from");
        String receivedSize = searchParams.get("size");
        int from = Objects.nonNull(receivedFrom) ? Integer.parseInt(receivedFrom) : 0;
        int size = Objects.nonNull(receivedSize) ? Integer.parseInt(receivedSize) : 10;


        return null;
    }
}
