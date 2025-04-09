package vito.prove.springbootplusdbunit.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class MyOtherTable {
    @Id
    private Long id;
    private String name;
    private Date somethingDate;
}
