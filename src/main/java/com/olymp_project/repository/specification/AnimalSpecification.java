package com.olymp_project.repository.specification;

import com.olymp_project.model.Animal;
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
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.olymp_project.service.CriteriaHandler.addSearchCriteria;

@AllArgsConstructor
public class AnimalSpecification implements Specification<Animal> {

    public static final String CHIPPING_DATE_TIME = "chippingDateTime";
    public static final String START_DATE_TIME_KEY = "startDateTime";
    public static final String END_DATE_TIME_KEY = "endDateTime";
    public static final String CHIPPER_ID_KEY = "chipperId";
    public static final String CHIPPING_LOCATION_ID_KEY = "chippingLocationId";
    public static final String LIFE_STATUS_KEY = "lifeStatus";
    public static final String GENDER_KEY = "GENDER";
    public static final String FROM_KEY = "from";
    public static final String SIZE_KEY = "size";

    private List<SearchCriteria> criteriaList;
    private static final String ID = "id";

    @Override
    public Predicate toPredicate(@NonNull Root<Animal> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        CriteriaHandler.addAllCriteriaToPredicateList(predicates, criteriaList, root, builder);
        return query.where(builder.and(predicates.toArray(new Predicate[0])))
                .orderBy(builder.asc(root.get(ID))).getRestriction();
    }

    public static List<SearchCriteria> createCriteriaListForAllParams(Map<String, String> searchParams) {
        String receivedStartDateTime = searchParams.get(CHIPPING_DATE_TIME);
        String receivedEndDateTime = searchParams.get(END_DATE_TIME_KEY);
        String receivedChipperId = searchParams.get(CHIPPER_ID_KEY);
        String receivedLocationId = searchParams.get(CHIPPING_LOCATION_ID_KEY);
        String receivedLifeStatus = searchParams.get(LIFE_STATUS_KEY);
        String receivedGender = searchParams.get(GENDER_KEY);
        List<SearchCriteria> searchCriteria = new ArrayList<>();
        if (receivedValueIsNotEmptyAndNotNull(receivedStartDateTime) && isIsoDate(receivedStartDateTime)) {
            addSearchCriteria(searchCriteria, CHIPPING_DATE_TIME, Optional.of(receivedStartDateTime), SearchOperation.EQUAL);
        }
        if (receivedValueIsNotEmptyAndNotNull(receivedEndDateTime) && isIsoDate(receivedEndDateTime)) {
            addSearchCriteria(searchCriteria, END_DATE_TIME_KEY, Optional.of(receivedEndDateTime), SearchOperation.EQUAL);
        }
        if (receivedValueIsNotEmptyAndNotNull(receivedChipperId)) {
            addSearchCriteria(searchCriteria, CHIPPER_ID_KEY, Optional.of(receivedChipperId), SearchOperation.EQUAL);
        }
        if (receivedValueIsNotEmptyAndNotNull(receivedLocationId)) {
            addSearchCriteria(searchCriteria, CHIPPING_LOCATION_ID_KEY, Optional.of(receivedLocationId), SearchOperation.EQUAL);
        }
        if (receivedValueIsNotEmptyAndNotNull(receivedLifeStatus)) {
            addSearchCriteria(searchCriteria, LIFE_STATUS_KEY, Optional.of(receivedLifeStatus), SearchOperation.EQUAL);
        }
        if (receivedValueIsNotEmptyAndNotNull(receivedGender)) {
            addSearchCriteria(searchCriteria, GENDER_KEY, Optional.of(receivedGender), SearchOperation.EQUAL);
        }
        return searchCriteria;
    }

    private static boolean receivedValueIsNotEmptyAndNotNull(String value) {
        return value != null && !value.isEmpty();
    }

    private static boolean isIsoDate(String date) {
        if (receivedValueIsNotEmptyAndNotNull(date)) {
            return false;
        }
        try {
            Instant.from(DateTimeFormatter.ISO_INSTANT.parse(date));
            return true;
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
