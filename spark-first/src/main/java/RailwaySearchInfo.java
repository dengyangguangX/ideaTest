import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RailwaySearchInfo {
    private Map<String,String> startEndStationList = new HashMap<>();
    private List<RailwayInfo> railwayInfos = new ArrayList<>();

    public Map<String, String> getStartEndStationList() {
        return startEndStationList;
    }

    public void setStartEndStationList(String key, String value) {
        this.startEndStationList.put(key, value);
    }

    public List<RailwayInfo> getRailwayInfos() {
        return railwayInfos;
    }

    public void setRailwayInfos(RailwayInfo railwayInfos) {
        this.railwayInfos.add(railwayInfos);
    }

    @Override
    public String toString() {
        return "RailwaySearchInfo{" +
                "startEndStationList=" + startEndStationList +
                ", railwayInfos=" + railwayInfos +
                '}';
    }
}
