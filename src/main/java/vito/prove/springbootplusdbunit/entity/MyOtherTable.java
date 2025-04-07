package vito.prove.springbootplusdbunit.entity;

import static jakarta.persistence.GenerationType.SEQUENCE;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MyOtherTable {
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "my_other_table_seq")
    @SequenceGenerator(name = "my_other_table_seq",
                       sequenceName = "my_other_table_seq",
                       allocationSize = 1)
    public Long id;
    public String name;
    public Date when;
}
