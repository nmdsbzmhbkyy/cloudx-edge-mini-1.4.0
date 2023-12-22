package com.aurine.cloudx.open.origin.config;

import com.aurine.cloudx.open.origin.entity.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "szzt")
public class SzztConfigurationProperties {
    private String HUMAN_ID;
    private Map<String, Szzt6193> table_6193;
    private Map<String, Szzt6194> table_6194;
    private Map<String, Szzt6195> table_6195;
    private Map<String, Szzt6196> table_6196;
    private Map<String, Szzt6197> table_6197;
    private Map<String, Szzt6198> table_6198;
    private Map<String, Szzt6199> table_6199;
}
