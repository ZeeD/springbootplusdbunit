package vito.prove.springbootplusdbunit.model;

import java.util.Date;

import lombok.Data;

@Data
public class MyModel {
    final Long id;
    final String name;
    final Date somethingDate;
}
