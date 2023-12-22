package com.aurine.cloudx.estate.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class localTimeConverter implements Converter<LocalTime> {

    @Override
    public Class<LocalDateTime> supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalTime convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
                                           GlobalConfiguration globalConfiguration) {
        return LocalTime.parse(cellData.getStringValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public CellData<String> convertToExcelData(LocalTime value, ExcelContentProperty contentProperty,
                                               GlobalConfiguration globalConfiguration) {
        return new CellData<>(value.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

}
