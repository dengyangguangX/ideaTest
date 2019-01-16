public class RailwayInfo {
    private String railwayNo;
    private String simpleNo;
    private String firstStation;
    private String lastStation;
    private String startStation;
    private String endStation;
    private String date;
    private String startTime;
    private String endTime;
    private String durationTime;
    private String business;
    private String first;
    private String second;
    private String hard;
    private String soft;
    private String hardSleep;
    private String softSleep;
    private String nothing;

    public RailwayInfo(String railwayNo, String simpleNo, String firstStation, String lastStation, String startStation, String endStation, String startTime, String endTime, String durationTime, String business, String first, String second, String hard, String soft, String hardSleep, String softSleep, String nothing) {
        this.railwayNo = railwayNo;
        this.simpleNo = simpleNo;
        this.firstStation = firstStation;
        this.lastStation = lastStation;
        this.startStation = startStation;
        this.endStation = endStation;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationTime = durationTime;
        this.business = business;
        this.first = first;
        this.second = second;
        this.hard = hard;
        this.soft = soft;
        this.hardSleep = hardSleep;
        this.softSleep = softSleep;
        this.nothing = nothing;
    }

    public RailwayInfo(String railwayNo, String simpleNo, String firstStation, String lastStation, String startStation, String endStation, String startTime, String endTime, String durationTime, String business, String first, String second, String hard, String soft, String hardSleep, String softSleep) {
        this.railwayNo = railwayNo;
        this.simpleNo = simpleNo;
        this.firstStation = firstStation;
        this.lastStation = lastStation;
        this.startStation = startStation;
        this.endStation = endStation;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationTime = durationTime;
        this.business = business;
        this.first = first;
        this.second = second;
        this.hard = hard;
        this.soft = soft;
        this.hardSleep = hardSleep;
        this.softSleep = softSleep;
    }

    public RailwayInfo(String railwayNo, String simpleNo, String firstStation, String lastStation, String startStation, String endStation, String startTime, String endTime, String durationTime) {
        this.railwayNo = railwayNo;
        this.simpleNo = simpleNo;
        this.firstStation = firstStation;
        this.lastStation = lastStation;
        this.startStation = startStation;
        this.endStation = endStation;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationTime = durationTime;
    }

    public RailwayInfo(String railwayNo, String simpleNo, String firstStation, String lastStation, String startStation, String endStation, String date, String startTime, String endTime, String durationTime, String business, String first, String second, String hard, String soft, String hardSleep, String softSleep, String nothing) {
        this.railwayNo = railwayNo;
        this.simpleNo = simpleNo;
        this.firstStation = firstStation;
        this.lastStation = lastStation;
        this.startStation = startStation;
        this.endStation = endStation;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationTime = durationTime;
        this.business = business;
        this.first = first;
        this.second = second;
        this.hard = hard;
        this.soft = soft;
        this.hardSleep = hardSleep;
        this.softSleep = softSleep;
        this.nothing = nothing;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNothing() {
        return nothing;
    }

    public void setNothing(String nothing) {
        this.nothing = nothing;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getHard() {
        return hard;
    }

    public void setHard(String hard) {
        this.hard = hard;
    }

    public String getSoft() {
        return soft;
    }

    public void setSoft(String soft) {
        this.soft = soft;
    }

    public String getHardSleep() {
        return hardSleep;
    }

    public void setHardSleep(String hardSleep) {
        this.hardSleep = hardSleep;
    }

    public String getSoftSleep() {
        return softSleep;
    }

    public void setSoftSleep(String softSleep) {
        this.softSleep = softSleep;
    }

    public String getRailwayNo() {
        return railwayNo;
    }

    public void setRailwayNo(String railwayNo) {
        this.railwayNo = railwayNo;
    }

    public String getSimpleNo() {
        return simpleNo;
    }

    public void setSimpleNo(String simpleNo) {
        this.simpleNo = simpleNo;
    }

    public String getFirstStation() {
        return firstStation;
    }

    public void setFirstStation(String firstStation) {
        this.firstStation = firstStation;
    }

    public String getLastStation() {
        return lastStation;
    }

    public void setLastStation(String lastStation) {
        this.lastStation = lastStation;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(String durationTime) {
        this.durationTime = durationTime;
    }

    @Override
    public String toString() {
        return "date='" + date + '\'' +
                "startTime='" + startTime + '\'' +
                "business='" + business + '\'' +
                ", first='" + first + '\'' +
                ", second='" + second + '\'' +
                ", hard='" + hard + '\'' +
                ", soft='" + soft + '\'' +
                ", hardSleep='" + hardSleep + '\'' +
                ", softSleep='" + softSleep + '\'' +
                ", nothing='" + nothing + '\'' +
                '}';
    }
}
