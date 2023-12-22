package com.aurine.cloudx.estate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @version 1.0
 * @author： 林功鑫
 * @date： 2022-09-19 17:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingPersonIdDto {
    private List<String> personIdList;
}
