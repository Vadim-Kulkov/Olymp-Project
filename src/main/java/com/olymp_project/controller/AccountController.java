package com.olymp_project.controller;

import com.olymp_project.model.Account;
import com.olymp_project.repository.AccountRepository;
import com.olymp_project.repository.specification.AccountSpecification;
import com.olymp_project.repository.specification.criteria.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.olymp_project.repository.specification.AccountSpecification.FROM_KEY;
import static com.olymp_project.repository.specification.AccountSpecification.SIZE_KEY;

@RestController()
@RequestMapping("/accounts")
public class AccountController {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping(value = "/{accountId}")
    public ResponseEntity<Account> findById(@PathVariable("accountId") String receivedId) {
        long id;
        try {
            id = Long.parseLong(receivedId);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Account> result = accountRepository.findById(id);
        return result.map(account -> new ResponseEntity<>(account, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<Account>> searchByParameters(@RequestParam Map<String, String> searchParams) {
        String receivedFrom = searchParams.get(FROM_KEY);
        String receivedSize = searchParams.get(SIZE_KEY);
        int from;
        int size;
        try {
            from = Objects.nonNull(receivedFrom) ? Integer.parseInt(receivedFrom) : 0;
            size = Objects.nonNull(receivedSize) ? Integer.parseInt(receivedSize) : 10;
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (from < 0 || size <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<SearchCriteria> searchCriteria = AccountSpecification.createCriteriaListForAllParams(searchParams);
        AccountSpecification specification = new AccountSpecification(searchCriteria);

        return new ResponseEntity<>(accountRepository.findAll(specification, PageRequest.of(from, size)).getContent(), HttpStatus.OK);
    }
}
