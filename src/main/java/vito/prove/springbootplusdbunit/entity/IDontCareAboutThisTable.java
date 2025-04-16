package vito.prove.springbootplusdbunit.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@AllArgsConstructor(staticName = "of")
public class IDontCareAboutThisTable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Date somethingDate;
}
