import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DYGUtil_Java {
    public static void main(String[] args) {
        List matcher = getMatcherAll("(1235)", "123456123456");
        matcher.forEach(x->System.out.println(x));
    }

    public static List<String> getMatcherAll(String regex, String source){
        List<String> resultList = new ArrayList<>();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            String result = "";
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result += matcher.group(i) + "-";
            }
            result = result.substring(0,result.length()-1);
            resultList.add(result);
        }
        return resultList;
    }

    public static String splitJoin(){

        return "";
    }

    public static String getMatcher(String regex, String source) {
        String result = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    public static String getStringFromUrl(String urlStr, String regex) {
        StringBuffer resultStr = new StringBuffer();
        try {
            URL url = new URL(urlStr);

            /*使用转换流解决乱码*/
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
            String str;
            while ((str = br.readLine()) != null) {
                resultStr.append(str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return DYGUtil_Java.getMatcher(regex, resultStr.toString());
    }

    public static String trimWithRegex(String regex, String source){
        if(source.endsWith(regex)){
            source = source.substring(0,source.length()-regex.length());
        }
        if(source.startsWith(regex)){
            source = source.substring(regex.length());
        }
        return source;
    }




}
