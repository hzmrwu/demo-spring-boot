package com.mk.demoonspringboot.java.annotation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Author maokai.wu
 * @Date 2021/8/19
 * @Description
 */
@Slf4j
public class EasyExportExcelUtils {

    public static <T> void export(List<T> list, String fileName, HttpServletResponse response) {
        if(response == null) {
            //TODO throw new BizException(ResponseErrorCodeEnum.PARAMETER_ERR);
        }
        if(list == null) {
            list = Collections.emptyList();
        }
        if(StringUtils.isEmpty(fileName)) {
            fileName = "导出文件.xls";
        }
        List<List<Object>> exportList = new ArrayList<>();
        if(list.size() > 0) {
            T vo0 = list.get(0);
            List<Object> headRow = new ArrayList<>();
            for (Field field : vo0.getClass().getDeclaredFields()) {
                ExportableField anno = field.getAnnotation(ExportableField.class);
                if (anno != null) {
                    field.setAccessible(true);
                    String columnName = anno.value();
                    headRow.add(columnName);
                }
            }
            exportList.add(headRow);
            for (T vo : list) {
                List<Object> dataRow = new ArrayList<>();
                for (Field field : vo.getClass().getDeclaredFields()) {
                    if(field.isAnnotationPresent(ExportableField.class)) {
                        Object rowField = "";
                        field.setAccessible(true);
                        try {
                            Object value = field.get(vo);
                            rowField = value;
                            ExportableField anno = field.getAnnotation(ExportableField.class);
                            if (anno != null) {
                                if (anno.serializer() != ExportableField.None.class) {
                                    ExportableField.Printer printer = anno.serializer().newInstance();
                                    rowField = printer.print(value);
                                } else if(!"".equals(anno.pattern()) && value instanceof Date) {
                                    //TODO rowField = DateTimeUtils.dateFormat((Date) value, anno.pattern());
                                }
                            }
                        } catch (Exception ex) {
                            log.error("解析异常", ex);
                        }
                        dataRow.add(rowField);
                    }
                }
                exportList.add(dataRow);
            }
        }
        /*ExportExcelUtils<Object> exportExcelUtil = new ExportExcelUtils<>();
        try {
            exportExcelUtil.export(fileName + ".xls", fileName, exportList, response);
        } catch (Exception e) {
            log.error("导出异常", e);
        }
        return null;*/
    }
}
