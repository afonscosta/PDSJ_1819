/**
 *
 * @author fmm 2017
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.out;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.util.stream.Collectors.*;


public class BenchJavaStreams {

    public static void memoryUsage() {
        final int mByte = 1024 * 1024;
        // Parâmetros de RunTime
        Runtime runtime = Runtime.getRuntime();
        out.println("== Valores de Utilização da HEAP [MB] ==");
        out.println("Memória Máxima RT:" + runtime.maxMemory() / mByte);
        out.println("Total Memory:" + runtime.totalMemory() / mByte);
        out.println("Memória Livre:" + runtime.freeMemory() / mByte);
        out.println("Memoria Usada:" + (runtime.totalMemory() -
                runtime.freeMemory()) / mByte);
    }

    public static TransCaixa strToTransCaixa(String linha) {
        //
        double preco = 0.0;
        int ano = 0;
        int mes = 0;
        int dia = 0;
        int hora = 0;
        int min = 0;
        int seg = 0;
        String codTrans, codCaixa;
        // split()
        String[] campos = linha.split("/");
        codTrans = campos[0].trim();
        codCaixa = campos[1].trim();
        try {
            preco = Double.parseDouble(campos[2]);
        } catch (InputMismatchException | NumberFormatException e) {
            return null;
        }
        String[] diaMesAnoHMS = campos[3].split("T");
        String[] diaMesAno = diaMesAnoHMS[0].split(":");
        String[] horasMin = diaMesAnoHMS[1].split(":");
        try {
            dia = Integer.parseInt(diaMesAno[0]);
            mes = Integer.parseInt(diaMesAno[1]);
            ano = Integer.parseInt(diaMesAno[2]);
            hora = Integer.parseInt(horasMin[0]);
            min = Integer.parseInt(horasMin[1]);
        } catch (InputMismatchException | NumberFormatException e) {
            return null;
        }
        return TransCaixa.of(codTrans, codCaixa, preco, LocalDateTime.of(ano, mes, dia, hora, min, 0));
    }

    public static List<TransCaixa> setup(String nomeFich) {
        List<TransCaixa> ltc = new ArrayList<>();
        try (Stream<String> sTrans = Files.lines(Paths.get(nomeFich))) {
            ltc = sTrans.map(linha -> strToTransCaixa(linha)).collect(toList());
        } catch (IOException exc) {
            out.println(exc.getMessage());
        }
        return ltc;
    }

    public static <R> SimpleEntry<Double, R> testeBoxGen(Supplier<? extends R> supplier) {
        Crono.start();
        R resultado = supplier.get();
        Double tempo = Crono.stop();
        return new SimpleEntry<Double, R>(tempo, resultado);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        String nomeFich = "TransCaixaResources/transCaixa1M.txt";
        List<TransCaixa> ltc1;

        // LE O FICHEIRO DE TRANSACÇOES PARA List<TransCaixa> com Streams
        Crono.start();
        ltc1 = setup(nomeFich);
        out.println("Setup com Streams: " + Crono.stop() * 1000 + " ms");
        out.println("Transacções lidas - Streams: " + ltc1.size());

        final List<TransCaixa> ltc = new ArrayList<>(ltc1);

        Supplier<String> debug = () -> {
            ltc.stream().limit(10).forEach(tc -> out.println(tc.getData()));
            return null;
        };

        //testeBoxGen(debug);

        //exc1(ltc1);
        //exc5(ltc1);
        //exc9(ltc1);
        exc10(ltc1);
    }

    /*
    Criar um double[], uma DoubleStream e uma Stream<Double> contendo desde 1M
    até 8M dos valores das transacções registadas em List<TransCaixa>. Usando para o array
    um ciclo for() e um forEach() e para as streams as operações respectivas e processamento
    sequencial e paralelo, comparar para cada caso os tempos de cálculo da soma e da média
    desses valores./*
     */
    private static void exc1(List<TransCaixa> ltc) {
        System.out.println("====Exc1 " + ltc.size() + " unidades");
        double[] doubleArr = new double[ltc.size()];
        for (int i = 0; i < ltc.size(); i++) {
            doubleArr[i] = ltc.get(i).getValor();
        }

        Supplier<Double> metodoForArraySum = () -> {
            double sum = 0;
            double count = 0;
            for (int i = 0; i < doubleArr.length; sum += doubleArr[i], i++, count++);
            return sum;
        };

        Supplier<Double> metodoForArrayAvg = () -> {
            double sum = 0;
            double count = 0;
            for (int i = 0; i < doubleArr.length; sum += doubleArr[i], i++, count++);
            return (sum/count);
        };

        Supplier<Double> metodoForEachArraySum = () -> {
            double sum = 0;
            for (double val : doubleArr) {
                sum += val;
            }
            return sum;
        };

        Supplier<Double> metodoForEachArrayAvg = () -> {
            double sum = 0;
            double count = 0;
            for (double val : doubleArr) {
                sum += val;
                count++;
            }
            return (sum/count);
        };


        Supplier<Double> metodoDoubleStreamSum = () -> ltc.stream().mapToDouble(TransCaixa::getValor).sum();
        Supplier<Double> metodoDoubleStreamAvg = () -> ltc.stream().mapToDouble(TransCaixa::getValor).average().getAsDouble();

        Supplier<Double> metodoParallelDoubleStreamSum = () -> ltc.parallelStream().mapToDouble(TransCaixa::getValor).sum();
        Supplier<Double> metodoParallelDoubleStreamAvg = () -> ltc.parallelStream().mapToDouble(TransCaixa::getValor).average().getAsDouble();

        Supplier<Double> metodoStreamSum = () -> ltc.stream().map(TransCaixa::getValor).reduce(0.0, Double::sum);
        Supplier<Double> metodoStreamAvg = () -> {
            AtomicReference<Double> sum = new AtomicReference<>((double) 0);
            AtomicReference<Double> count = new AtomicReference<>((double) 0);

            ltc.stream().forEach(t -> { sum.updateAndGet(v -> new Double((double) (v + t.getValor()))); count.updateAndGet(v -> new Double((double) (v + 1))); });

            return (sum.get() / count.get());
        };

        Supplier<Double> metodoParallelStreamSum = () -> ltc.parallelStream().map(TransCaixa::getValor).reduce(0.0, Double::sum);
        Supplier<Double> metodoParallelStreamAvg = () -> {

            SimpleEntry<Double,Double> countAndSum = ltc.parallelStream().map((TransCaixa t) -> new SimpleEntry<>(1.0, t.getValor())).reduce(new SimpleEntry<>(0.0,0.0),
                    (SimpleEntry<Double,Double> a, SimpleEntry<Double,Double> b) -> new SimpleEntry<>(a.getKey()+b.getKey(), a.getValue() + b.getValue()));

            return countAndSum.getValue() / countAndSum.getKey();

        };


        SimpleEntry<Double,Double> entryForArraySum = testeBoxGen(metodoForArraySum);
        SimpleEntry<Double,Double> entryForArrayAvg = testeBoxGen(metodoForArrayAvg);
        SimpleEntry<Double,Double> entryForEachArraySum = testeBoxGen(metodoForEachArraySum);
        SimpleEntry<Double,Double> entryForEachArrayAvg = testeBoxGen(metodoForEachArrayAvg);
        SimpleEntry<Double,Double> entryDoubleStreamSum = testeBoxGen(metodoDoubleStreamSum);
        SimpleEntry<Double,Double> entryDoubleStreamAvg = testeBoxGen(metodoDoubleStreamAvg);
        SimpleEntry<Double,Double> entryParallelDoubleStreamSum = testeBoxGen(metodoParallelDoubleStreamSum);
        SimpleEntry<Double,Double> entryParallelDoubleStreamAvg = testeBoxGen(metodoParallelDoubleStreamAvg);
        SimpleEntry<Double,Double> entryStreamSum = testeBoxGen(metodoStreamSum);
        SimpleEntry<Double,Double> entryStreamAvg = testeBoxGen(metodoStreamAvg);
        SimpleEntry<Double,Double> entryParallelStreamSum = testeBoxGen(metodoParallelStreamSum);
        SimpleEntry<Double,Double> entryParallelStreamAvg = testeBoxGen(metodoParallelStreamAvg);

        System.out.println(String.format("EntryForArraySum: %f (%f ms)", entryForArraySum.getValue(), entryForArraySum.getKey()*1000));
        System.out.println(String.format("EntryForEachArraySum: %f (%f ms)", entryForEachArraySum.getValue(), entryForEachArraySum.getKey()*1000));
        System.out.println(String.format("EntryDoubleStreamSum: %f (%f ms)", entryDoubleStreamSum.getValue(), entryDoubleStreamSum.getKey()*1000));
        System.out.println(String.format("EntryParallelDoubleStreamSum: %f (%f ms)", entryParallelDoubleStreamSum.getValue(), entryParallelDoubleStreamSum.getKey()*1000));
        System.out.println(String.format("EntryStreamSum: %f (%f ms)", entryStreamSum.getValue(), entryStreamSum.getKey()*1000));
        System.out.println(String.format("EntryParallelStreamSum: %f (%f ms)", entryParallelStreamSum.getValue(), entryParallelStreamSum.getKey()*1000));

        System.out.println(String.format("EntryForArrayAvg: %f (%f ms)", entryForArrayAvg.getValue(), entryForArrayAvg.getKey()*1000));
        System.out.println(String.format("EntryForEachArrayAvg: %f (%f ms)", entryForEachArrayAvg.getValue(), entryForEachArrayAvg.getKey()*1000));
        System.out.println(String.format("EntryDoubleStreamAvg: %f (%f ms)", entryDoubleStreamAvg.getValue(), entryDoubleStreamAvg.getKey()*1000));
        System.out.println(String.format("EntryParallelDoubleStreamAvg: %f (%f ms)", entryParallelDoubleStreamAvg.getValue(), entryParallelDoubleStreamAvg.getKey()*1000));
        System.out.println(String.format("EntryStreamAvg: %f (%f ms)", entryStreamAvg.getValue(), entryStreamAvg.getKey()*1000));
        System.out.println(String.format("EntryParallelStreamAvg: %f (%f ms)", entryParallelStreamAvg.getValue(), entryParallelStreamSum.getKey()*1000));

        System.out.println("!====Exc1 " + ltc.size() + " unidades");
    }

   /*
    * Usando os dados disponíveis crie um teste que permita comparar se dada a
    * List<TransCaixa> e um Comparator<TransCaixa>, que deverá ser definido, é mais
    * eficiente, usando streams, fazer o collect para um TreeSet<TransCaixa> ou usar a
    * operação sorted() e fazer o collect para uma nova List<TransCaixa>.
    */
    private static void exc5(List<TransCaixa> ltc) {
        System.out.println("====Exc5 " + ltc.size() + " unidades");
        Comparator<TransCaixa> comp = Comparator.comparing(TransCaixa::getTrans);
        Supplier<TreeSet<TransCaixa>> supplyTreeSet = () -> new TreeSet<>(comp);

        Supplier<TreeSet<TransCaixa>> metodoTreeSet = () -> ltc.stream().collect(toCollection(supplyTreeSet));

        Supplier<List<TransCaixa>> metodoSortedList = () -> ltc.stream().sorted(comp).collect(toList());

        SimpleEntry<Double, TreeSet<TransCaixa>> entryTreeSet = testeBoxGen(metodoTreeSet);
        SimpleEntry<Double, List<TransCaixa>> entrySortedList = testeBoxGen(metodoSortedList);

        System.out.println(String.format("entryTreeSet %d unidades (%f ms)", entryTreeSet.getValue().size(), entryTreeSet.getKey()*1000));
        System.out.println(String.format("entrySortedList %d unidades (%f ms)", entrySortedList.getValue().size(), entrySortedList.getKey()*1000));

        System.out.println("!====Exc5 " + ltc.size() + " unidades");
    }

   /*
    * Crie uma List<List<TransCaixa>> em que cada lista elemento da lista contém todas
    * as transacções realizadas nos dias de 1 a 7 de uma dada semana do ano (1 a 52/53).
    * Codifique em JAVA 7 e em Java 8 com streams, o problema de, dada tal lista, se apurar ogg
    * total facturado nessa semana.
    */
    private static void exc9(List<TransCaixa> ltc) {
        Map<Long, List<TransCaixa>> grupo = ltc.stream().collect(
                                groupingBy((TransCaixa t1) -> ChronoUnit.WEEKS.between(LocalDateTime.now().with(firstDayOfYear()),t1.getData())));

        System.out.println(String.format("Contadas %d semanas distintas.", grupo.size()));

        Supplier<Map<Long, Double>> metodoSemStreams = () -> {
            Map<Long, Double> totalFaturado = new HashMap<>();
            for (Map.Entry<Long,List<TransCaixa>> entry : grupo.entrySet()) {
                List<TransCaixa> caixasDaSemana = entry.getValue();
                double faturaDaSemana = 0;

                for (TransCaixa caixa : caixasDaSemana) {
                   faturaDaSemana += caixa.getValor();
                }

                totalFaturado.put(entry.getKey(), faturaDaSemana);
            }

            return totalFaturado;
        };

        Supplier<Map<Long,Double>> metodoComUmStream = () -> {
            Map<Long,Double> totalFaturado = new HashMap<>();
            grupo.entrySet().forEach((Map.Entry<Long,List<TransCaixa>> e)
                                                        -> totalFaturado.put(e.getKey(), e.getValue().stream().mapToDouble(TransCaixa::getValor).sum()));

            return totalFaturado;
        };

        Supplier<Map<Long,Double>> metodoComDoisStreams = () -> grupo.entrySet()
                                                .stream()
                                                .map((Map.Entry<Long,List<TransCaixa>> e)
                                                            -> new SimpleEntry<>(e.getKey(), e.getValue().stream().mapToDouble(TransCaixa::getValor).sum()))
                                                .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));


        SimpleEntry<Double,Map<Long, Double>> entryMetodoSemStreams = testeBoxGen(metodoSemStreams);
        SimpleEntry<Double,Map<Long, Double>> entryMetodoComUmStream = testeBoxGen(metodoComUmStream);
        SimpleEntry<Double,Map<Long, Double>> entryMetodoComDoisStreams = testeBoxGen(metodoComDoisStreams);

        System.out.println(String.format("entryMetodoSemStreams %d unidades (%f ms)", entryMetodoSemStreams.getValue().size(), entryMetodoSemStreams.getKey()*1000));
        System.out.println(String.format("entryMetodoComDoisStreams %d unidades (%f ms)", entryMetodoComDoisStreams.getValue().size(), entryMetodoComDoisStreams.getKey()*1000));
        System.out.println(String.format("entryMetodoComUmStream %d unidades (%f ms)", entryMetodoComUmStream.getValue().size(), entryMetodoComUmStream.getKey()*1000));

        System.out.println(entryMetodoSemStreams.getValue());
        System.out.println(entryMetodoComDoisStreams.getValue());
        System.out.println(entryMetodoComUmStream.getValue());
    }

    /*
     * Admitindo que o IVA a entregar por transacção é de 12% para transacções menores
     * que 20 Euros, 20% entre 20 e 29 e 23% para valores superiores, crie uma tabela com o
     * valor de IVA total a pagar por mês. Compare as soluções em JAVA 7 e Java 8.
     */
    private static void exc10(List<TransCaixa> ltc) {


        Supplier<Map<Month,Double>> metodoComStream = () ->
                ltc.stream().collect(groupingBy((TransCaixa t) -> t.getData().getMonth(),
                                     summingDouble((TransCaixa t) -> (t.getValor() < 20)
                                                                      ? t.getValor()*0.12
                                                                      : (t.getValor() >= 20 && t.getValor() <= 29)
                                                                         ? t.getValor() * 0.2
                                                                         : t.getValor() * 0.23)));


        Supplier<Map<Month,Double>> metodoIterativo = () -> {
            HashMap<Month,Double> pagamentosIvasMeses = new HashMap<>();
            pagamentosIvasMeses.put(Month.JANUARY, 0.0);
            pagamentosIvasMeses.put(Month.FEBRUARY, 0.0);
            pagamentosIvasMeses.put(Month.MARCH, 0.0);
            pagamentosIvasMeses.put(Month.APRIL, 0.0);
            pagamentosIvasMeses.put(Month.MAY, 0.0);
            pagamentosIvasMeses.put(Month.JUNE, 0.0);
            pagamentosIvasMeses.put(Month.JULY, 0.0);
            pagamentosIvasMeses.put(Month.AUGUST, 0.0);
            pagamentosIvasMeses.put(Month.SEPTEMBER, 0.0);
            pagamentosIvasMeses.put(Month.OCTOBER, 0.0);
            pagamentosIvasMeses.put(Month.NOVEMBER, 0.0);
            pagamentosIvasMeses.put(Month.DECEMBER, 0.0);

            for (TransCaixa tc : ltc) {
                Month currMont = tc.getData().getMonth();
                Double currValor = tc.getValor();
                Double currTotalIva = pagamentosIvasMeses.get(currMont);
                Double ivaToPay = 0.0;

                if (currValor < 20) {
                    ivaToPay = currValor * 0.12;
                } else if (currValor >= 20 && currValor <= 29) {
                    ivaToPay = currValor * 0.2 ;
                } else {
                    ivaToPay = currValor * 0.23;
                }

                pagamentosIvasMeses.put(currMont,currTotalIva + ivaToPay);
            }

            return pagamentosIvasMeses;
        };

        SimpleEntry<Double, Map<Month,Double>> entryMetodoComStream = testeBoxGen(metodoComStream);
        SimpleEntry<Double, Map<Month,Double>> entryMetodoIterativo = testeBoxGen(metodoIterativo);


        System.out.println("entryMetodoComStream: " + entryMetodoComStream.getValue().toString());
        System.out.println("entryMetodoIterativo" + entryMetodoIterativo.getValue().toString());

    }

}
