/**
 *
 * @author fmm 2017
 */

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.InputMismatchException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.lang.System.out;
import static java.util.stream.Collectors.toList;


public class BenchJavaStreams {

    public static void memoryUsage() {
        final int mByte = 1024*1024;
        // Parâmetros de RunTime
        Runtime runtime = Runtime.getRuntime();
        out.println("== Valores de Utilização da HEAP [MB] ==");
        out.println("Memória Máxima RT:" + runtime.maxMemory()/mByte);
        out.println("Total Memory:" + runtime.totalMemory()/mByte);
        out.println("Memória Livre:" + runtime.freeMemory()/mByte);
        out.println("Memoria Usada:" + (runtime.totalMemory() -
                runtime.freeMemory())/mByte);
    }

    public static TransCaixa strToTransCaixa(String linha) {
        //
        double preco = 0.0;
        int ano = 0; int mes = 0; int dia = 0;
        int hora = 0; int min = 0; int seg = 0;
        String codTrans, codCaixa;
        // split()
        String[] campos = linha.split("/");
        codTrans = campos[0].trim();
        codCaixa = campos[1].trim();
        try {
            preco = Double.parseDouble(campos[2]);
        }
        catch(InputMismatchException | NumberFormatException e) { return null; }
        String[] diaMesAnoHMS = campos[3].split("T");
        String[] diaMesAno = diaMesAnoHMS[0].split(":");
        String[] horasMin = diaMesAnoHMS[1].split(":");
        try {
            dia = Integer.parseInt(diaMesAno[0]);
            mes = Integer.parseInt(diaMesAno[1]);
            ano = Integer.parseInt(diaMesAno[2]);
            hora = Integer.parseInt(horasMin[0]);
            min = Integer.parseInt(horasMin[1]);
        }
        catch(InputMismatchException | NumberFormatException e) { return null; }
        return TransCaixa.of(codTrans, codCaixa, preco, LocalDateTime.of(ano, mes, dia, hora, min, 0));
    }

    public static List<TransCaixa> setup(String nomeFich) {
        List<TransCaixa> ltc = new ArrayList<>();
        try (Stream<String> sTrans = Files.lines(Paths.get(nomeFich))) {
            ltc = sTrans.map(linha -> strToTransCaixa(linha)).collect(toList());
        }
        catch(IOException exc) { out.println(exc.getMessage()); }
        return ltc;
    }

    public static <R> SimpleEntry<Double,R> testeBoxGen(Supplier<? extends R> supplier) {
        Crono.start();
        R resultado = supplier.get();
        Double tempo = Crono.stop();
        return new SimpleEntry<Double,R>(tempo, resultado);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        String nomeFich = "TransCaixaResources/transCaixa1M.txt";
        List<TransCaixa> ltc1;

        // LE O FICHEIRO DE TRANSACÇOES PARA List<TransCaixa> com Streams
        Crono.start();
        ltc1 = setup(nomeFich);
        out.println("Setup com Streams: " + Crono.stop()*1000 + " ms");
        out.println("Transacções lidas - Streams: " + ltc1.size());

        final List<TransCaixa> ltc = new ArrayList<>(ltc1);

        Supplier<String> debug = () -> {
            ltc.stream().limit(10).forEach(tc -> out.println(tc));
            return null;
        };
        testeBoxGen(debug);
    }
}
