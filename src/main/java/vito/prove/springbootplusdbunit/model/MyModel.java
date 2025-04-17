package vito.prove.springbootplusdbunit.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MyModel {
    final Long id;
    final String name;
    final LocalDateTime somethingDate;
}
