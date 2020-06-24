package vito.prove.springbootplusdbunit;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MyTable {
    @Id
    @GeneratedValue
    public Long id;
    public String name;
    public Date when;
}
