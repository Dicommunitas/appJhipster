package com.operacional.controleamostragateway.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.operacional.controleamostragateway.domain.A} entity.
 */
public class ADTO implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ADTO)) {
            return false;
        }

        ADTO aDTO = (ADTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ADTO{" +
            "id=" + getId() +
            "}";
    }
}
