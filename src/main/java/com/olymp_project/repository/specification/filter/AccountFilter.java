package com.olymp_project.repository.specification.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountFilter {

    private String firstName;
    private String lastName;
    private String email;

    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final String ID = "id";
    public static final String FROM = "from";
    public static final String SIZE = "size";
}
