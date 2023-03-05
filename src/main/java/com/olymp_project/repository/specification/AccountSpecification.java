package com.olymp_project.repository.specification;

import com.olymp_project.model.Account;
import com.olymp_project.repository.specification.filter.AccountFilter;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class AccountSpecification implements Specification<Account> {

    private AccountFilter filter;

    @Override
    public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(filter.getFirstName())) {
            predicates.add(cb.equal(root.get(AccountFilter.FIRST_NAME), filter.getFirstName()));
        }
        if (Objects.nonNull(filter.getLastName())) {
            predicates.add(cb.equal(root.get(AccountFilter.LAST_NAME), filter.getLastName()));
        }
        if (Objects.nonNull(filter.getEmail())) {
            predicates.add(cb.equal(root.get(AccountFilter.EMAIL), filter.getEmail()));
        }

        return query.where(cb.and(predicates.toArray(new Predicate[0])))
                .orderBy(cb.asc(root.get(AccountFilter.ID))).getRestriction();
    }
}
