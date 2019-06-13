

package demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wwupower
 * @Title: ExcelDemo
 * @history 2018年10月07日
 * @since JDK1.8
 */
public class ExcelDemo {

    static String[] classRoom = {
            "A101", "A308"
    };
    static String[] classRoom2 = {
            "A101", "A308", "B302", "B406"
    };

    public static void main(String[] args) {
        List list = DbUtil.executeQuery("select * from t_teacher_plan ORDER BY plan_time");
        Map<String,String> assignMap = new HashMap<>();
        //分配教室

        for (int i = 0; i < list.size(); i++) {
            Map data = (Map) list.get(i);
            Date time = (Date) data.get("plan_time");
            String plan_time_info = (String) data.get("plan_time_info");
            int id = (Integer) data.get("id");
            String lesson = (String) data.get("lesson");
            String teach_type = (String) data.get("teach_type");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String key = sdf.format(time)+"-"+lesson;
            String sql = "";
            String classroom = classRoom[0];
            if(assignMap.containsKey(key)){
                classroom = getClassRooms(sdf.format(time),lesson,teach_type);
            }else{
                if("教研".equals(teach_type)){
                    classroom = classRoom[0];
                }else{
                    classroom = classRoom2[2];
                }
            }
            assignMap.put(key,classroom);
            sql = "update t_teacher_plan set classroom='" + classroom +"',plan_time=plan_time where id="+id;
            int count = DbUtil.executeUpdate(sql);
            if(count>0){
                System.out.println("更新成功-------");
            }

        }

    }
    public static String getClassRooms(String time, String lesson,String type){
        List isExist = DbUtil.executeQuery("select * from t_teacher_plan where plan_time='"+
                time +"' and lesson='"+lesson+"'");
        if(isExist.size() == 0){
            return "";
        }else if("教研".equals(type)){
            for(String cl:classRoom){
                boolean isUsed = false;
                for(Object obj:isExist){
                    Map map = (Map)obj;
                    String classroom1 = (String) map.get("classroom");
                    if(cl.equals(classroom1)){
                        isUsed = true;
                        break;
                    }
                }
                if(!isUsed){
                    return  cl;
                }
            }
            return "没有教室可用";
        }else{
            for(String cl:classRoom2){
                boolean isUsed = false;
                for(Object obj:isExist){
                    Map map = (Map)obj;
                    String classroom1 = (String) map.get("classroom");
                    if(cl.equals(classroom1)){
                        isUsed = true;
                        break;
                    }
                }
                if(!isUsed){
                    return  cl;
                }
            }
            return "没有教室可用";
        }
    }



}
