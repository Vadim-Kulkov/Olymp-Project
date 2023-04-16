package com.olymp_project.repository.specification;

import com.olymp_project.model.Account;
import com.olymp_project.repository.specification.criteria.SearchCriteria;
import com.olymp_project.service.CriteriaHandler;
import com.olymp_project.service.SearchOperation;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.olymp_project.service.CriteriaHandler.addSearchCriteria;

@AllArgsConstructor
public class AccountSpecification implements Specification<Account> {

    public static final String FIRST_NAME_KEY = "firstName";
    public static final String LAST_NAME_KEY = "lastName";
    public static final String EMAIL_KEY = "email";
    public static final String FROM_KEY = "from";
    public static final String SIZE_KEY = "size";
    private static final String ID = "id";

    private List<SearchCriteria> criteriaList;


    @Override
    public Predicate toPredicate(@NonNull Root<Account> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        CriteriaHandler.addAllCriteriaToPredicateList(predicates, criteriaList, root, builder);
        return query.where(builder.and(predicates.toArray(new Predicate[0])))
                .orderBy(builder.asc(root.get(ID))).getRestriction();
    }

    public static List<SearchCriteria> createCriteriaListForAllParams(Map<String, String> searchParams) {
        String receivedFirstName = searchParams.get(FIRST_NAME_KEY);
        String receivedLastName = searchParams.get(LAST_NAME_KEY);
        String receivedEmail = searchParams.get(EMAIL_KEY);

        List<SearchCriteria> searchCriteria = new ArrayList<>();
        if (receivedValueIsNotEmptyAndNotNull(receivedFirstName)) {
            addSearchCriteria(searchCriteria, FIRST_NAME_KEY, Optional.of(receivedFirstName), SearchOperation.EQUAL);
        }
        if (receivedValueIsNotEmptyAndNotNull(receivedLastName)) {
            addSearchCriteria(searchCriteria, LAST_NAME_KEY, Optional.of(receivedLastName), SearchOperation.EQUAL);
        }
        if (receivedValueIsNotEmptyAndNotNull(receivedEmail)) {
            addSearchCriteria(searchCriteria, EMAIL_KEY, Optional.of(receivedEmail), SearchOperation.EQUAL);
        }
        return searchCriteria;
    }

    private static boolean receivedValueIsNotEmptyAndNotNull(String value) {
        return value != null && !value.isEmpty();
    }
}
