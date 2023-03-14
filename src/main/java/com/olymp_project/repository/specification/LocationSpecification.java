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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class LocationSpecification implements Specification<Location> {

    private static final String ID = "id";
    private static final String ""

    private List<SearchCriteria> criteriaList;


    @Override
    public Predicate toPredicate(Root<Location> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        CriteriaHandler.addAllCriteriaToPredicateList(predicates, criteriaList, root, builder);
        return query.where(builder.and(predicates.toArray(new Predicate[0])))
                .orderBy(builder.asc(root.get(ID))).getRestriction();
    }
//?startDateTime=
//            &endDateTime=

    public static List<SearchCriteria> createCriteriaListForAllParams(Map<String, String> searchParams) {
        String receivedStartDateTime;
        String receivedEndDateTime;

    }
}
