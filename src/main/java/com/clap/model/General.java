package com.clap.model;


import org.springframework.data.neo4j.core.schema.Node;

import lombok.Getter;
import lombok.Setter;

/**
 * A General.
 */
@Node
@Getter
@Setter
public class General extends ArtisticContent {

    private static final long serialVersionUID = 1L;


    // prettier-ignore
    @Override
    public String toString() {
        return "General{" +
            "id=" + getId() +
            "}";
    }
}
