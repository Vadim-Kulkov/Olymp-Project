package com.olymp_project.repository.specification.criteria;

import com.olymp_project.service.SearchOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchCriteria {

    private String key;
    private Object value;
    private SearchOperation operation;

    @Override
    public String toString() {
        return "SearchCriteria{" + "key='" + key + '\'' +
                ", value=" + value +
                ", operation=" + operation +
                '}';
    }
}
