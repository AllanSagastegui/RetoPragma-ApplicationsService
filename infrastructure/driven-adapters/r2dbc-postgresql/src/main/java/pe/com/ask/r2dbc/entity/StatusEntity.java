package pe.com.ask.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table("status")
public class StatusEntity {
    @Id
    @Column("id_status")
    private UUID idStatus;

    @Column("name")
    private String name;

    @Column("description")
    private String description;
}