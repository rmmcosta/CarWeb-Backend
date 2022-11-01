package com.udacity.vehicles.domain.order;

import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@Table(name = "SaleOrder")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    private String buyer;

    private boolean isFinalized;

    @Valid
    @Embedded
    private Location deliveryLocation = new Location(0d, 0d);

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Car> carSet;
}
