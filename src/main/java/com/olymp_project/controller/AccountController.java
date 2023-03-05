package com.olymp_project.controller;

import com.olymp_project.model.Account;
import com.olymp_project.repository.AccountRepository;
import com.olymp_project.repository.specification.AccountSpecification;
import com.olymp_project.repository.specification.filter.AccountFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<Account> findById(@PathVariable("accountId") Long accountId) {
        if (Objects.isNull(accountId) || accountId <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Account> result = accountRepository.findById(accountId);
        return result.map(account -> new ResponseEntity<>(account, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/search")
    public List<Account> searchByParameters(@RequestParam Map<String, String> searchParams) {
        String receivedFrom = searchParams.get(AccountFilter.FROM);
        String receivedSize = searchParams.get(AccountFilter.SIZE);

        int from = Objects.nonNull(receivedFrom) ? Integer.parseInt(receivedFrom) : 0;
        int size = Objects.nonNull(receivedSize) ? Integer.parseInt(receivedSize) : 10;

        AccountFilter accountFilter = new AccountFilter(
                searchParams.get(AccountFilter.FIRST_NAME),
                searchParams.get(AccountFilter.LAST_NAME),
                searchParams.get(AccountFilter.EMAIL)
        );

        AccountSpecification accountSpecification = new AccountSpecification(accountFilter);
        return accountRepository.findAll(accountSpecification, PageRequest.of(from, size)).getContent();
    }
}
