package top.xuguoliang.common.utils;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;

import java.io.*;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExcelHelper {

    public static final String UID = "serialVersionUID";
    private static Logger logger = Logger.getLogger(ExcelHelper.class);

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void writeExcel(String file, List list, Class clazz,
                                  List<String> ignorePropertis, List<String> titles) throws Exception {
        Field[] fields = clazz.getDeclaredFields();
        List flist = new ArrayList();
        if (null != ignorePropertis) {
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getName().equals(UID)
                        || ignorePropertis.contains(fields[i].getName())) {
                    continue;
                }
                flist.add(fields[i].getName());
            }
        } else {
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getName().equals(UID)) {
                    continue;
                }
                flist.add(fields[i].getName());
            }
        }
        writeExcel1(file, list, clazz, flist, null == titles ? flist : titles);
    }

    @SuppressWarnings("rawtypes")
    public static void writeExcel1(String file, List list, Class clazz, List<String> fields, List<String> titles) throws Exception {
        OutputStream os = getOutputStream(file);
        jxl.write.WritableWorkbook wwb = jxl.Workbook.createWorkbook(os);
        jxl.write.WritableSheet ws = wwb.createSheet("Sheet1", 0);
        jxl.write.Label label = null;
        int start = 0;
        if (titles != null && titles.size() > 0) {
            for (int j = 0; j < titles.size(); j++) {
                label = new jxl.write.Label(j, 0, titles.get(j));
                ws.addCell(label);
            }
            start++;
        }
        for (int i = start; i < list.size() + start; i++) {
            Object o = list.get(i - start);
            if (o == null) {
                continue;
            }
            for (int j = 0; j < fields.size(); j++) {
                String value = "";
                if ("serialVersionUID".equals(fields)) {
                    continue;
                }
                try {
                    value = ReflectUtils.invokeGetMethod(clazz, o, fields.get(j)).toString();
                } catch (Exception e) {

                }
                if (fields.get(j) != null && isTime(fields.get(j))) {
                    if (value.isEmpty()) {
                        value = "";
                    } else {
						//value=DateUtils.dateStr4(value);
                    }
                }
                label = new jxl.write.Label(j, i, value);
                ws.addCell(label);
            }
        }
        wwb.write();
        wwb.close();
    }

    /**
     * 读取excel文件(需优化)
     *
     * @param file    文件
     * @param hasDate 文件中是否有日期
     * @return
     */
    public static List[] read(File file, boolean hasDate) {
        List[] data = null; //保存工作簿的数据
        Workbook wb;//创建工作簿
        try {
            try {
                wb = new XSSFWorkbook(new FileInputStream(file));//操作Excel2007的版本，扩展名是.xlsx
            } catch (Exception ex) {
                wb = new HSSFWorkbook(new FileInputStream(file));//Excel2003以前（包括2003）的版本，扩展名是.xls
            }
            if (wb != null) {
                Sheet sheet = wb.getSheetAt(0);//第一个表格
                if (sheet != null) {
                    //int rows1 = sheet.getLastRowNum();//得到最后一行的行号，索引从0开始
                    int rows = -1;
                    int rowCount = sheet.getPhysicalNumberOfRows();
                    for (int i = 0; i < rowCount; i++) {
                        Row row = sheet.getRow(i);
                        if (null != row && row.getFirstCellNum() == 0) {
                            rows++;
                        }
                    }
                    data = new List[rows + 1];
                    for (int i = 0; i <= rows; i++) {//遍历所有行
                        Row row = sheet.getRow(i);//得到当前行
                        if (row != null) {
                            List<String> rowData =new ArrayList<>();//用户保存每行的数据
                            for (int j = 0; j < row.getLastCellNum(); j++) {//遍历当前行的所有单元格
                                Cell cell = row.getCell(j);//获取单元格
                                Integer type = cell.getCellType();
                                //判断是否为日期类型
                                if (type == Cell.CELL_TYPE_NUMERIC && hasDate && HSSFDateUtil.isCellDateFormatted(cell)) {
                                    Date d = cell.getDateCellValue();
                                    DateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
                                    String value = simpleDateFormat.format(d);
                                    rowData.add(value);
                                } else {
                                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);//将单元格的数据类型转成string
                                    String value = cell.getStringCellValue();//获取单元格内容
                                    rowData.add(null == value ? "" : value);
                                }
                            }
                            data[i] = rowData;
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
            throw new ValidationException(MessageCodes.EXCEL_ERROR);
        }
        return data;
    }
    @SuppressWarnings("rawtypes")
    public static List[] read(String xls) throws Exception {
        List[] data = null;
        File file = new File(xls);
        if (file.exists()) {
            data = read(file);
        }
        return data;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List[] read(File file) throws Exception {
        List[] data = null; //保存工作簿的数据
        Workbook wb = null;//创建工作簿
        try {
            try {
                wb = new XSSFWorkbook(new FileInputStream(file));//操作Excel2007的版本，扩展名是.xlsx
            } catch (Exception ex) {
                wb = new HSSFWorkbook(new FileInputStream(file));//Excel2003以前（包括2003）的版本，扩展名是.xls
            }
            if (wb != null) {
                Sheet sheet = wb.getSheetAt(0);//第一个表格
                if (sheet != null) {
                    int rows = sheet.getLastRowNum();//得到最后一行的行号，索引从0开始
                    data = new List[rows + 1];
                    for (int i = 0; i <= rows; i++) {//遍历所有行
                        Row row = sheet.getRow(i);//得到当前行
                        if (row != null) {
                            List<String> rowData = new ArrayList();//用户保存每行的数据
                            for (int j = 0; j < row.getLastCellNum(); j++) {//遍历当前行的所有单元格
                                Cell cell = row.getCell(j);//获取单元格
                                cell.setCellType(HSSFCell.CELL_TYPE_STRING);//将单元格的数据类型转成string
                                String value = cell.getStringCellValue();//获取单元格内容
                                rowData.add(null == value ? "" : value);
                            }
                            data[i] = rowData;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return data;
    }


    private static boolean isTime(String field) {
        String[] times = new String[]{"addtime", "repay_time", "verify_time", "repay_yestime"};
        boolean isTime = false;
        for (String s : times) {
            if (s.equals(field)) {
                isTime = true;
                break;
            }
        }
        return isTime;
    }

    public static final String WEBAPP_EXPORT_EXCEL = System.getProperty("java.io.tmpdir") + "sureme" + File.separator
            + "excel" + File.separator;

    public static OutputStream getOutputStream(String file) throws Exception {
        File path = new File(WEBAPP_EXPORT_EXCEL);
        if (!path.exists()) {
            path.mkdirs();
        }
        File f = new File(WEBAPP_EXPORT_EXCEL + file);
        f.createNewFile();
        OutputStream os = new FileOutputStream(f);
        return os;
    }

}
