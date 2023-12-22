package com.aurine.cloudx.estate.excel.converter;


import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.aurine.cloudx.estate.constant.enums.PassTypeEnum;

public class PassConverter implements Converter<String> {
    @Override
    public Class supportJavaTypeKey() {
        return null;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return null;
    }

    @Override
    public String convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (PassTypeEnum.OUT.value.equals(cellData.getStringValue())) {
            return PassTypeEnum.OUT.key;
        } else if (PassTypeEnum.IN.value.equals(cellData.getStringValue())) {
            return PassTypeEnum.IN.key;
        }else {
            return "";
        }

    }

    @Override
    public CellData convertToExcelData(String data, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (PassTypeEnum.IN.key.equals(data)) {
            return new CellData(PassTypeEnum.IN.value);
        } else if (PassTypeEnum.OUT.key.equals(data)) {
            return new CellData(PassTypeEnum.OUT.value);
        }else {
            return CellData.newEmptyInstance();
        }
    }
}