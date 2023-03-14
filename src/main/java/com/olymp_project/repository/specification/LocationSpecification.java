package com.olymp_project.repository.specification;

import com.olymp_project.model.Location;
import com.olymp_project.repository.specification.criteria.SearchCriteria;
import com.olymp_project.service.CriteriaHandler;
import com.olymp_project.service.SearchOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.olymp_project.service.CriteriaHandler.addSearchCriteria;

@AllArgsConstructor
public class LocationSpecification implements Specification<Location> {

    private static final String ID = "id";
    private static final String START_DATE_TIME_KEY = "startDateTime";
    private static final String END_DATE_TIME_KEY = "endDateTime";
    private static final String VISIT_DATE_TIME_KEY = "visitDateTime";

    private List<SearchCriteria> criteriaList;

    @Override
    public Predicate toPredicate(Root<Location> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        CriteriaHandler.addAllCriteriaToPredicateList(predicates, criteriaList, root, builder);
        return query.where(builder.and(predicates.toArray(new Predicate[0])))
                .orderBy(builder.asc(root.get(VISIT_DATE_TIME_KEY))).getRestriction();
    }

    public static List<SearchCriteria> createCriteriaListForAllParams(Map<String, String> searchParams) {
        List<SearchCriteria> searchCriteria = new ArrayList<>();
        LocalDateTime receivedStartDateTime;
        LocalDateTime receivedEndDateTime;
        if (receivedValueIsNotEmptyAndNotNull(searchParams.get(START_DATE_TIME_KEY))) {
            receivedStartDateTime = LocalDateTime.parse(searchParams.get(START_DATE_TIME_KEY));
            addSearchCriteria(searchCriteria, VISIT_DATE_TIME_KEY, Optional.of(receivedStartDateTime), SearchOperation.GREATER_THAN);
        }
        if (receivedValueIsNotEmptyAndNotNull(searchParams.get(END_DATE_TIME_KEY))) {
            receivedEndDateTime = LocalDateTime.parse(searchParams.get(END_DATE_TIME_KEY));
            addSearchCriteria(searchCriteria, VISIT_DATE_TIME_KEY, Optional.of(receivedEndDateTime), SearchOperation.LESS_THAN);
        }
        return searchCriteria;
    }

    private static boolean receivedValueIsNotEmptyAndNotNull(String value) {
        return value != null && !value.isEmpty();
    }
}
