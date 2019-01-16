import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RailwayPlaceName {


    //szd|宿州东|SRH|suzhoudong|szd|1635
    private static Map<String, String> placeMap = new HashMap<>();

    static {
        String placeStr = DYGUtil_Java.getStringFromUrl(
                "https://kyfw.12306.cn/otn/resources/js/framework/station_name.js?station_version=1.9090",
                "'@(.*)'"
        );

        for (String s : placeStr.split("@")) {
            String s2 = s.split("\\|")[2];
            String s1 = s.split("\\|")[1];
            placeMap.put(s2,s1);
            placeMap.put(s1,s2);
        }


    }


    public static String getPlaceCode(String placeName) {
        return placeMap.get(placeName);
    }

    public static void main(String[] args) {
        RailwayPlaceName railwayPlaceName = new RailwayPlaceName();
        System.out.println(railwayPlaceName.getPlaceCode("NGH"));
    }

}
