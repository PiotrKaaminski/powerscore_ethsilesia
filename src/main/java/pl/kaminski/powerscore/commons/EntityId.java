package pl.kaminski.powerscore.commons;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@EqualsAndHashCode
public class EntityId implements Serializable {

    @Column
    private UUID value;

    // used only by persistence to instantiate object
    protected EntityId() {}

    private EntityId(UUID value) {
        this.value = value;
    }

    public UUID value() {
        return value;
    }

    // hibernate metamodel generator needs proper getter to generate metamodel for fields
    private UUID getValue() {return value;}

    public static EntityId newId() {return new EntityId(UUID.randomUUID());}

    public static EntityId from(UUID value) {
        Objects.requireNonNull(value);
        return new EntityId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
