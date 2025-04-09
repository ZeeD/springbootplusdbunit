package vito.prove.springbootplusdbunit.entity;

import static jakarta.persistence.GenerationType.SEQUENCE;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@AllArgsConstructor(staticName = "of")
public class IDontCareAboutThisTable {
    @Id
    @GeneratedValue(strategy = SEQUENCE,
                    generator = "i_dont_care_about_this_table_seq")
    @SequenceGenerator(name = "i_dont_care_about_this_table_seq",
                       sequenceName = "i_dont_care_about_this_table_seq",
                       allocationSize = 1)
    Long id;
    String name;
    Date when;
}
