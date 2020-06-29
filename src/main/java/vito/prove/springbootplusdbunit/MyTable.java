package vito.prove.springbootplusdbunit;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MyTable {
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "my_table_seq")
    @SequenceGenerator(name = "my_table_seq",
                       sequenceName = "my_table_seq",
                       allocationSize = 1)
    public Long id;
    public String name;
    public Date when;
}
