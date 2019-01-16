import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

public class SearchTicket {



    public static void main(String[] args) {

        String[] dateCondition = new String[]{"2019-02-01"};
        int count = 1;
        while (true){
            System.out.println("第" + count + "次查询");
            bootstrap();
            System.out.println("第" + count + "次查询结束");
            count++;
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    public static void bootstrap(){

//        String fromStation = "北京";
//        String toStation = "宿州";
//        String date = "2019-01-27";
//        String[] condition = new String[]{"hardSleep"};
//        String[] trainNoCondition = new String[]{"T63","1461"};
//        String[] trainTimeCondition = new String[]{"00:00","24:00"};


//        String fromStation = "上海";
//        String toStation = "宿州";
//        String date = "2019-01-31";
//        String[] condition = new String[]{"hardSleep", "hard", "nothing"};
//        String[] trainNoCondition = new String[]{"all","T63","1461"};
//        String[] trainTimeCondition = new String[]{"00:00","24:00"};

//        String fromStation = "金寨";
//        String toStation = "重庆";
//        String date = "2019-02-01";
//        String[] condition = new String[]{"second", "hard", "hardSleep"};
//        String[] trainNoCondition = new String[]{"G","D", "T63", "1461"};
//        String[] trainTimeCondition = new String[]{"00:00","14:00"};

//        String fromStation = "重庆";
//        String toStation = "内江";
//        String date = "2019-02-01";
//        String[] condition = new String[]{"second", "hard", "hardSleep"};
//        String[] trainNoCondition = new String[]{"G","D", "T63", "1461"};
//        String[] trainTimeCondition = new String[]{"16:00","24:00"};



        String fromStation = "北京";
        String toStation = "哈尔滨";
        String date = "2019-01-27";
        String[] condition = new String[]{"second", "hardSleep"};
        String[] trainNoCondition = new String[]{"all","G","D","T","Z"};
        String[] trainTimeCondition = new String[]{"09:00","03:00"};

//        String fromStation = "南京";
//        String toStation = "宿州";
//        String date = "2019-02-02";
//        String[] condition = new String[]{"second","hard", "hardSleep"};
//        String[] trainNoCondition = new String[]{"G","D","T","Z"};
//        String[] trainTimeCondition = new String[]{"09:00","03:00"};


        RailwaySearchInfo ticketInfo = getTicketInfo(fromStation, toStation, date);
        List<RailwayInfo> railwayInfos = ticketInfo.getRailwayInfos();

        railwayInfos = trainWhiteFilter(trainNoCondition,railwayInfos);
        try {
            railwayInfos = trainTimeFilter(trainTimeCondition,railwayInfos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("查询过滤结束，车次总数：" + railwayInfos.size());

        Collection<String> values = ticketInfo.getStartEndStationList().values();
        System.out.println(values);
        for (RailwayInfo railwayInfo : railwayInfos) {
            //RailwayInfo railwayInfo = railwayInfos.get(1);
            List<TrainNoInfo> trainNoInfos = queryByTrainNo(railwayInfo, date);
            int before = 1;
            int after = 1;

            for (int i = 0; i < trainNoInfos.size(); i++) {
                //System.out.println("--------");
                String station_name = trainNoInfos.get(i).getStation_name();
                if (values.contains(station_name)) {
                    if(i-1<0) {
                        before = i -0;
                    }
                    i = i - before--;
                    String station_name2 = trainNoInfos.get(i).getStation_name();
//                    System.out.println(station_name2);
                    boolean flag = false;
                    for (int j = i + 2 + before; j < trainNoInfos.size(); j++) {
                        String station_name1 = trainNoInfos.get(j).getStation_name();

                        List<RailwayInfo> railwayInfos1 = getTicketInfo(station_name2, station_name1, date).getRailwayInfos();
                        railwayInfos1.forEach(x -> {
                            x = trainNoFilter(condition,x);
                            if (x != null && x.getSimpleNo().equals(railwayInfo.getSimpleNo())) {
                                System.out.println(station_name2 + "----->" + station_name1 + " : " + x.getSimpleNo() + "\t" + x);
                            }
                        });
                        if (values.contains(station_name1))
                            flag = true;
                        if(flag && after-- <=0)
                            break;
                    }
                    if(before<0) break;
                }
            }
        }
    }

    private static List<RailwayInfo> trainTimeFilter(String[] trainTimeCondition, List<RailwayInfo> railwayInfos) throws Exception {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        long fromTime = simpleDateFormat.parse(trainTimeCondition[0]).getTime();
        long toTime = simpleDateFormat.parse(trainTimeCondition[1]).getTime();



        //System.out.println(fromTime + " : " + toTime);
        Iterator<RailwayInfo> iterator = railwayInfos.iterator();
        while (iterator.hasNext()){
            RailwayInfo next = iterator.next();
            long time = simpleDateFormat.parse(next.getStartTime()).getTime();
            //System.out.println(time);
            if(toTime<fromTime){
                //[00:00-toTime]  [fromTime- 24:00]
                if(time <= fromTime && time >= toTime){
                    iterator.remove();
                }
            }else {
                //[fromTime-toTime]
                if(time <= fromTime || time >= toTime){
                    iterator.remove();
                }
            }


        }

        return railwayInfos;
    }

    public static List<RailwayInfo> trainWhiteFilter(String[] trainWhiteLists,List<RailwayInfo> railwayInfos ){

        List<String> trainWhites = Arrays.asList(trainWhiteLists);
        if(trainWhites.contains("all")){
            return railwayInfos;
        }

        Iterator<RailwayInfo> iterator = railwayInfos.iterator();

        while (iterator.hasNext()){
            RailwayInfo railwayInfo = iterator.next();
            String simpleNo = railwayInfo.getSimpleNo();
            if(!(trainWhites.contains(simpleNo) || trainWhites.contains(simpleNo.substring(0,1).toUpperCase())))
                iterator.remove();
        }
        return railwayInfos;
    }


    public  static RailwayInfo trainNoFilter(String[] condition,RailwayInfo railwayInfo){
        String regex = "[\\d有]+";
        for (String con : condition) {
            switch (con){
                case "hard" :
                    if(railwayInfo.getHard().matches(regex))
                        return railwayInfo;
                    break;
                case "soft" :
                    if(railwayInfo.getSoft().matches(regex))
                        return railwayInfo;
                    break;
                case "hardSleep" :
                    if(railwayInfo.getHardSleep().matches(regex))
                        return railwayInfo;
                    break;
                case "softSleep" :
                    if(railwayInfo.getSoftSleep().matches(regex))
                        return railwayInfo;
                    break;
                case "business" :
                    if(railwayInfo.getBusiness().matches(regex))
                        return railwayInfo;
                    break;
                case "first" :
                    if(railwayInfo.getFirst().matches(regex))
                        return railwayInfo;
                    break;
                case "second" :
                    if(railwayInfo.getSecond().matches(regex))
                        return railwayInfo;
                    break;
                case "nothing" :
                    if(railwayInfo.getNothing().matches(regex))
                        return railwayInfo;
                    break;
                case "all" :
                    return railwayInfo;

            }
        }
        return null;
    }

    /**
     * @param fromStation
     * @param toStation
     * @param date        yyyy-MM-dd
     * @return
     */
    public static RailwaySearchInfo getTicketInfo(String fromStation, String toStation, String date) {
        RailwayPlaceName railwayPlaceName = new RailwayPlaceName();

        String regex = "\\{\"data\":\\{\"flag\":\"1\",(.*)\\},\"httpstatus\":200,\"messages\":\"\",\"status\":true\\}";

        String url = "https://kyfw.12306.cn/otn/leftTicket/queryZ?leftTicketDTO.train_date=";
        url += date;

        url += "&leftTicketDTO.from_station=" + RailwayPlaceName.getPlaceCode(fromStation);

        url += "&leftTicketDTO.to_station=" + RailwayPlaceName.getPlaceCode(toStation);

        url += "&purpose_codes=ADULT";

        String stringFromUrl = DYGUtil_Java.getStringFromUrl(url, regex);
        List<String> matcherAll = DYGUtil_Java.getMatcherAll("\"map\":\\{(.*)\\},\"result\":\\[(.*)\\]", stringFromUrl);
        //matcherAll.forEach(x -> System.out.println(x));
        RailwaySearchInfo railwaySearchInfo = new RailwaySearchInfo();
        for (String s : matcherAll) {
            String[] searchAndTrainInfo = s.split("-");
            if(searchAndTrainInfo.length<2) continue;
            String[] searchInfos = searchAndTrainInfo[0].split(",");
            String[] trainInfos = searchAndTrainInfo[1].split(",");

            for (String searchInfo : searchInfos) {
                String[] ss1 = searchInfo.split(":");
                railwaySearchInfo.setStartEndStationList(DYGUtil_Java.trimWithRegex("\"", ss1[0]), DYGUtil_Java.trimWithRegex("\"", ss1[1]));
            }

            //  0        1          2      3   4    5   6    7   8    9     10 11   12          13  14     15     16    17     18    19   20     21    22   23    24   25   26   27     28    29    30      | 31     |  32   |33       34      35  36      37      38
            //                   车编号   车次 始发|终点|起点|到达|时间|时间|  用时                时间                                                         软卧    软座     无座       硬卧  硬座     二等座    一等座    商务座
            //"SlCVonTY|预订|2400000T630Y|T63|BJP|HFH|BJP|OXH|19:57|04:21|08:24|Y|1yb4aE%3D|20190115|3  |  P3  |  01  |  05  |  0  |  0  |     |  9  |    |  有  |    |    |    |    |  无  |  无  |         |        |       |    |1040601030|14613|  0  |  0  |  null"
            for (String trainInfo : trainInfos) {
                trainInfo = trainInfo.replace("\"", "");

                //System.out.println(trainInfo);
                String[] ss2 = trainInfo.split("\\|");
                if (ss2.length < 10)
                    continue;
                RailwayInfo railwayInfo = new RailwayInfo(
                        ss2[2],//车编号
                        ss2[3],//车次
                        railwayPlaceName.getPlaceCode(ss2[4]),//始发
                        railwayPlaceName.getPlaceCode(ss2[5]),//终点
                        railwayPlaceName.getPlaceCode(ss2[6]),//起点
                        railwayPlaceName.getPlaceCode(ss2[7]),//到达
                        ss2[13],//date
                        ss2[8],//发车时间
                        ss2[9],//到达时间
                        ss2[10],//用时
                        ss2[32],//商务
                        ss2[31],//一等
                        ss2[30],//二等
                        ss2[29],//硬座
                        ss2[24],//软座
                        ss2[28],//硬卧
                        ss2[23],//软卧
                        ss2[26]//无座
                );
                railwaySearchInfo.setRailwayInfos(railwayInfo);
            }
        }

        //System.out.println(railwaySearchInfo);
        return railwaySearchInfo;
    }

    //https://kyfw.12306.cn/otn/czxx/queryByTrainNo?train_no=330000Z2840E&from_station_telecode=BJP&to_station_telecode=XCH&depart_date=2019-01-16

    public static List<TrainNoInfo> queryByTrainNo(RailwayInfo railwayInfo, String date) {

        String url = "https://kyfw.12306.cn/otn/czxx/queryByTrainNo?train_no=" + railwayInfo.getRailwayNo();

        url += "&from_station_telecode=" + RailwayPlaceName.getPlaceCode(railwayInfo.getStartStation());

        url += "&to_station_telecode=" + RailwayPlaceName.getPlaceCode(railwayInfo.getEndStation());

        url += "&depart_date=" + date;
        //System.out.println(url);

        String trainNoJsonString = DYGUtil_Java.getStringFromUrl(url, "(\\{\"validateMessagesShowId\".*\\})");
        //System.out.println(trainNoJsonString);
        JSONObject trainNoJson = JSON.parseObject(trainNoJsonString);
        String data = trainNoJson.getString("data");
        JSONObject trainNoJson2 = JSON.parseObject(data);
        data = trainNoJson2.getString("data");

        JSONArray trainNoOneJsons = JSON.parseArray(data);
        List<TrainNoInfo> trainNoInfos = JSON.parseArray(data, TrainNoInfo.class);

        //trainNoInfos.forEach(x->System.out.println(x));

        return trainNoInfos;

    }
}

