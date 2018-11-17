import java.util.List;

public class Test {
    public static void main(String[] args){

        List<List<String>> zones = Utilities.BusinessUtils.getAvailableTimeZoneIdsByPage(10);

        System.out.println(zones.get(0));
        System.out.println(zones.get(1));

    }
}
