package com.aurine.cloudx.estate.excel.converter;


import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.aurine.cloudx.estate.constant.enums.TrueFalseEnum;

public class TrueFalseConverter implements Converter<String> {
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
        if (TrueFalseEnum.TRUE.value.equals(cellData.getStringValue())) {
            return TrueFalseEnum.TRUE.key;
        } else if (TrueFalseEnum.FALSE.value.equals(cellData.getStringValue())) {
            return TrueFalseEnum.FALSE.key;
        }else {
            return "";
        }

    }

    @Override
    public CellData convertToExcelData(String data, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (TrueFalseEnum.TRUE.key.equals(data)) {
            return new CellData(TrueFalseEnum.TRUE.value);
        } else if (TrueFalseEnum.FALSE.key.equals(data)) {
            return new CellData(TrueFalseEnum.FALSE.value);
        }else {
            return CellData.newEmptyInstance();
        }
    }
}