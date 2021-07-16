package com.project.entity;

import com.project.support.BooleanToStringConverter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "premium_adverts_details")
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(of = "id")
@ToString
public class AdvertPremium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_active", nullable = false)
    @Convert(converter= BooleanToStringConverter.class)
    private Boolean isActive;

    @Column(name = "premium_started")
    private LocalDateTime premStarted;

    @Column(name = "premium_end")
    private LocalDateTime premEnd;

}
