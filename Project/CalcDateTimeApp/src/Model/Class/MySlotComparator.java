package Model.Class;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Comparator;

public class MySlotComparator implements Comparator<Slot>, Serializable {

    @Override
    public int compare(Slot s1, Slot s2) {
        Temporal data1 = s1.getData();
        Temporal data2 = s2.getData();

        if(data1.equals(data2)) return 0;
        else if(data1.getClass().getSimpleName().equals("LocalDateTime")){
                if(data2.getClass().getSimpleName().equals("LocalDateTime")){
                    System.out.println("AS DUAS DATAS SÃO LOCALDATETIME");
                    LocalDateTime ldt1 = LocalDateTime.from(data1);
                    LocalDateTime ldt2 = LocalDateTime.from(data2);
                    System.out.println("ldt1->" + ldt1.toString());
                    System.out.println("ldt2->" + ldt2.toString());
                    if(ldt1.getYear()==ldt2.getYear()){
                            if(ldt1.getMonth()==ldt2.getMonth()){
                                System.out.println("ldt1 day of month->" + ldt1.getDayOfMonth());
                                System.out.println("ldt2 day of month->" + ldt2 .getDayOfMonth());
                                if(ldt1.getDayOfMonth()== ldt2.getDayOfMonth()) {
                                    System.out.println("Datas no mesmo dia");
                                    if (ldt1.isBefore(ldt2)) {
                                        Duration d1 = s1.getDuration();
                                        LocalDateTime data1Final = ldt1.plus(d1);
                                        System.out.println("data1Final->" + data1Final);
                                        if (data1Final.isBefore(ldt2))
                                            return -1;
                                        else{
                                            System.out.println("Retornei 0 no mesmo dia!!");
                                            return 0;
                                        }
                                    }
                                    else {
                                        Duration d2 = s2.getDuration();
                                        LocalDateTime data2Final= ldt2.plus(d2);
                                        System.out.println("data2Final->" + data2Final);
                                        if(data2Final.isBefore(ldt1))
                                            return 1;
                                        else{
                                            System.out.println("Retornei 0 no mesmo dia!!");
                                            return 0;
                                        }
                                    }
                                }
                            }
                    }
                    System.out.println("Se estou aqui é porque não acontecem no mesmo dia!!!");
                    if (ldt1.isBefore(ldt2))
                            return -1;
                    else
                        return 1;
                }
        }
        System.out.println("Se estou aqui é porque não são LocalDate Time");
        return 0;

    }
}
