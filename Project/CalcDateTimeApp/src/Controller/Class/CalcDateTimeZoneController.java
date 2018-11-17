package Controller.Class;

import Controller.Interface.InterfCalcDateTimeZoneController;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.Input;
import Utilities.Menu;
import View.Interface.InterfCalcDateTimeZoneView;

import static Utilities.BusinessUtils.zoneDateTimeToString;
import static Utilities.BusinessUtils.getAvailableTimeZoneIdsByPage;
import static Utilities.BusinessUtils.clearConsole;

import java.time.ZonedDateTime;
import java.util.List;

public class CalcDateTimeZoneController implements InterfCalcDateTimeZoneController {
    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeZoneView viewZoneTxt;

    public CalcDateTimeZoneController() {
    }

    @Override
    public void setModel(InterfCalcDateTimeModel model) {
        this.model = model;
    }

    @Override
    public void setView(InterfCalcDateTimeZoneView viewZone) {
        this.viewZoneTxt = viewZone;
    }

    private String buildZoneDateTimeTitle() {
        ZonedDateTime temp = (ZonedDateTime) model.getDateTimeZone();
        String ld = zoneDateTimeToString(temp);
        return ld;
    }

    //------------------------
    // FlowLocal
    //------------------------
    @Override
    public void flowZone() {
        Menu menu = viewZoneTxt.getMenu(0);
        String zld;
        String opcao;
        do {
            zld = buildZoneDateTimeTitle();
            menu.addDateTimeToTitle(zld);
            menu.show();
            opcao = Input.lerString().toUpperCase();
            switch(opcao) {
                case "C": flowConvertZone(); break;
                case "S": break;
                default: System.out.println("Opcao Invalida!"); break;
            }

        } while(!opcao.equals("S"));
    }


    //------------------------
    // FlowConvertZone
    //------------------------
    // Pedir para que zona queremos mudar a data
    private void flowConvertZone() {
        flowShowAllAvailableTimezones();
        System.out.println("Zona para qual converter(S para sair): ");
        String answerZone = Input.lerString();

        if (!answerZone.equals(("S"))) {
            model.convertZoneDateTimeToZone(answerZone);
        }
    }


    //------------------------
    // FlowShowAllAvailableTimeZones
    //------------------------
    // Buscar todos os ZoneIds alfabeticamente e fazer display por páginas
    private void flowShowAllAvailableTimezones() {
        List<List<String>> allZoneidsByPage = getAvailableTimeZoneIdsByPage(50);
        int pageIndex = 0;

        String opcao;
        do {
            clearConsole();
            allZoneidsByPage.get(pageIndex).forEach((String s) -> System.out.println("." + s));
            System.out.println("Pagina (" + (pageIndex+1) + "/" + allZoneidsByPage.size() + ")");
            System.out.println("(+) Proxima pagina (-) Previa pagina (S) Sair da listagem");
            opcao = Input.lerString().toUpperCase();
            switch(opcao) {
                case "+": if ((pageIndex + 1) < allZoneidsByPage.size()) { pageIndex++; } break;
                case "-": if ((pageIndex - 1) >= 0) { pageIndex--; } break;
                case "S": break;
                default: System.out.println("Opcao Invalida!"); break;
            }

        } while(!opcao.equals("S"));
    }
}
