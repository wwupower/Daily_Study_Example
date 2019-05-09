package demo;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.DateUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author y
 * @create 2018-01-19 0:13
 * @desc
 **/
public class ExcelReaderUtil {
    //excel2003扩展名
    public static final String EXCEL03_EXTENSION = ".xls";
    //excel2007扩展名
    public static final String EXCEL07_EXTENSION = ".xlsx";

    public static String intUperCase(int num){
        if(1 == num){
            return "一";
        }
        if(2 == num){
            return "二";
        }
        if(3 == num){
            return "三";
        }
        if(4 == num){
            return "四";
        }
        if(5 == num){
            return "五";
        }
        if(6 == num){
            return "六";
        }
        if(7 == num){
            return "七";
        }
        return "";
    }
    public static int weekToInt(String num){
        if("星期一".equals(num)){
            return 1;
        }
        if("星期二".equals(num)){
            return 2;
        }
        if("星期三".equals(num)){
            return 3;
        }
        if("星期四".equals(num)){
            return 4;
        }
        if("星期五".equals(num)){
            return 5;
        }
        if("星期六".equals(num)){
            return 6;
        }
        if("星期天".equals(num) || "星期日".equals(num)){
            return 7;
        }
        return 0;
    }

    /**
     * 每获取一条记录，即打印
     * 在flume里每获取一条记录即发送，而不必缓存起来，可以大大减少内存的消耗，这里主要是针对flume读取大数据量excel来说的
     *
     * @param sheetName
     * @param sheetIndex
     * @param curRow
     * @param cellList
     */
    public static void sendRows(String filePath, String sheetName, int sheetIndex, int curRow, List<String> cellList) {
        StringBuffer oneLineSb = new StringBuffer();
        oneLineSb.append(filePath);
        oneLineSb.append("--");
        oneLineSb.append("sheet" + sheetIndex);
        oneLineSb.append("::" + sheetName);//加上sheet名
        oneLineSb.append("--");
        oneLineSb.append("row" + curRow);
        oneLineSb.append("::");
//        String sql = "insert into t_jbxx(XH,SQ,XZ,XZC,ZRC,XM,SBZ) values(";
//        String sql ="insert into `t_studen_parents` " +
//                "(" +
//                "  `XS`," +
//                "  `XB`," +
//                "  `LXFS`," +
//                "  `FQXM`," +
//                "  `FQSFZ`," +
//                "  `FQLXFS`," +
//                "  `MQXM`," +
//                "  `MQSFZ`," +
//                "  `EXFS`" +
//                ") values(";
        String sql = "INSERT INTO `t_teacher_plan` (`subject`,`week`, `plan_time`, `plan_time_info`, `group_teacher`, `groups`,  `teach_type`,`grade`,`lesson`) VALUES (";
        String columns = "";
        int i = 0;
        boolean isBeiKe = false;
        int week = 0;
        for (String cell : cellList) {
            if (i >= 10) {
                break;
            }
            if (cell.length() == 0) {
                continue;
            }
            if(i==1){
                try {
                    if(cell.trim().length()>0 && !cell.trim().contains("星期")){
                        Date date =DateUtils.parseDateStrictly(cell.trim(),"yyyy/MM/dd");
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        sql += "'星期" +intUperCase( (calendar.get(Calendar.DAY_OF_WEEK)-1))+ "',";
                    }else{
                        isBeiKe = true;
                        week = weekToInt(cell.trim());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            sql += "'" + cell.trim() + "',";
            oneLineSb.append(cell.trim());
            oneLineSb.append("|");
            i++;
        }
       // sql += "'教研',";
        sql = sql.substring(0, sql.lastIndexOf(",")) + ")";
        Connection conn = DbUtil.open();
        PreparedStatement pstmt = null;

        try {
            if(sql.contains("2018-10-07 00:00:00")){
                Date date = new Date();
                for(int ii=1;ii<15;ii++){
                    String temp = new String(sql);
                    if(ii==1){
                         date = DateUtils.addDays(date,week);
                    }else{
                         date = DateUtils.addDays(date,7);
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    temp = temp.replace("2018-10-07 00:00:00",sdf.format(date));
                    pstmt = (PreparedStatement) conn.prepareStatement(temp);
                    pstmt.executeUpdate();
                    System.out.println(sql);
                }
            }else{
                System.out.println(sql);
                pstmt = (PreparedStatement) conn.prepareStatement(sql);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            DbUtil.close(conn);
        }
        String oneLine = oneLineSb.toString();
        if (oneLine.endsWith("|")) {
            oneLine = oneLine.substring(0, oneLine.lastIndexOf("|"));
        }// 去除最后一个分隔符

        // System.out.println(oneLine);
    }

    public static void readExcel(String fileName) throws Exception {
        int totalRows = 0;
        if (fileName.endsWith(EXCEL03_EXTENSION)) { //处理excel2003文件
            ExcelXlsReader excelXls = new ExcelXlsReader();
            totalRows = excelXls.process(fileName);
        } else if (fileName.endsWith(EXCEL07_EXTENSION)) {//处理excel2007文件
            ExcelXlsxReader excelXlsxReader = new ExcelXlsxReader();
            totalRows = excelXlsxReader.process(fileName);
        } else {
            throw new Exception("文件格式错误，fileName的扩展名只能是xls或xlsx。");
        }
        System.out.println("发送的总行数：" + totalRows);
    }

    public static void main(String[] args) throws Exception {
        /* String path = "d:/截止2018年8月1日建档立卡贫困户信息表.xlsx";*/

//        String path = "D:/旃倩表格/学校安排/教研组、备课组汇总表/地理教研组、备课组时间登记表.xls";
        String path = "D:/旃倩表格/学校安排/教研组、备课组汇总表/教研组时间.xlsx";
        ExcelReaderUtil.readExcel(path);
    }
}