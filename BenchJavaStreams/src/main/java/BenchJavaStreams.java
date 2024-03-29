/**
 *
 * @author fmm 2017
 */

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.System.out;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.util.stream.Collectors.*;

public class BenchJavaStreams {

    private static final List<List<TransCaixa>> ltcs = new ArrayList<>();

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

    public static <R> SimpleEntry<Double,R> testeBoxGenW(Supplier<? extends R> supplier)  {
        // com warmup de 5 runs
        for (int i = 1; i <= 10; i++) supplier.get();
        System.gc();
        Crono.start();
        R resultado = supplier.get();
        Double tempo = Crono.stop();
        return new SimpleEntry<Double,R>(tempo, resultado);
    }

    private static void loadFiles(List<String> files) {
        for (String f : files) {
            Crono.start();
            final List<TransCaixa> ltc= setup(f);
            out.println("========================");
            out.println(" Documento " + f.substring(20, f.length()-4));
            out.println("========================");
            out.println("Duração: " + Crono.stop()*1000 + " ms");
            out.println("Transacções lidas: " + ltc.size());
            out.println("\n");
            ltcs.add(ltc);
        }
    }

    /*
     * Função que dada uma lista de transCaixas e uma lista de soluções
     * gera uma lista de suppliers para as respetivas soluções
     */
    private static Function<List<?>, Function<List<SimpleEntry<String, Function>>, List<SimpleEntry<String, Supplier<SimpleEntry>>>>>
            createSuppliers = ltc -> (lsols -> {
        List<SimpleEntry<String, Supplier<SimpleEntry>>> lsups = new ArrayList<>();
        for (SimpleEntry<String, Function> sol : lsols) {
            lsups.add(new SimpleEntry<>(sol.getKey(), (Supplier<SimpleEntry>) sol.getValue().apply(ltc)));
        }
        return lsups;
    });

    /*
     * Corre os testes.
     * Cada teste tem uma ou mais soluções.
     * Para cada ficheiro de dados de teste são executadas todas as soluções.
     * Resultado:
     *  Testes
     *  ├── T1
     *  │    ├── TransCaixa1M.txt
     *  │    │    ├── solucao1
     *  │    │    ├── solucao2
     *  │    │    └── solucao3
     *  │    │
     *  │    ├── (...)
     *  │    │
     *  │    └── TransCaixa6M.txt
     *  │         ├── solucao1
     *  │         ├── solucao2
     *  │         ├── solucao3
     *  │         └── solucao4
     *  │
     *  ├── (...)
     *  │
     *  └── T12
     *       ├── TransCaixa1M.txt
     *       │    ├── solucao1
     *       │    └── solucao2
     *       │
     *       ├── (...)
     *       │
     *       └── TransCaixa6M.txt
     *            ├── solucao1
     *            ├── solucao2
     *            └── solucao3
     */
    private static List<List<List<SimpleEntry<String, SimpleEntry<Double, SimpleEntry>>>>> runTestes(List<List<SimpleEntry<String, Function>>> testes) {
        List<List<List<SimpleEntry<String, SimpleEntry<Double, SimpleEntry>>>>> resultados = new ArrayList<>();

        for (List<SimpleEntry<String, Function>> solucoesTi : testes) {
            List<List<SimpleEntry<String, SimpleEntry<Double, SimpleEntry>>>> resultadosTi = new ArrayList<>();
            if(solucoesTi.get(0).getKey().startsWith("solucao3")){
                for(int i=1; i<= 8 ; i++) {
                    List<SimpleEntry<String, SimpleEntry<Double, SimpleEntry>>> resultadosResourceI = new ArrayList<>();
                    for (SimpleEntry<String, Function> supSolucao : solucoesTi) {
                        System.out.println("T3: A fazer para " + i * 1000000 + "...");
                        SimpleEntry<Double, SimpleEntry> res = testeBoxGenW((Supplier<SimpleEntry>) supSolucao.getValue().apply(Arrays.asList(i * 1000000)));
                        resultadosResourceI.add(new SimpleEntry<>(supSolucao.getKey(), res));
                    }
                    resultadosTi.add(resultadosResourceI);
                }
            }
            else {
                for (List<TransCaixa> ltc : ltcs) {
                    List<SimpleEntry<String, SimpleEntry<Double, SimpleEntry>>> resultadosResourceI = new ArrayList<>();
                    List<SimpleEntry<String, Supplier<SimpleEntry>>> suppsSolucoesTi = createSuppliers.apply(ltc).apply(solucoesTi);
                    for (SimpleEntry<String, Supplier<SimpleEntry>> supSolucao : suppsSolucoesTi) {
                        System.out.println("A fazer para " + supSolucao.getKey() + "...");
                        SimpleEntry<Double, SimpleEntry> res = testeBoxGenW(supSolucao.getValue());
                        resultadosResourceI.add(new SimpleEntry<>(supSolucao.getKey(), res));
                    }
                    resultadosTi.add(resultadosResourceI);
                }
            }
            resultados.add(resultadosTi);
        }
        return resultados;
    }

    /*
     * Gera ficheiros CSV a partir dos resultados dos testes
     */
    private static void genCSVTable(String filename, List<List<SimpleEntry<String, SimpleEntry<Double, SimpleEntry>>>> resultadosTi) throws IOException {
        StringBuilder sb = new StringBuilder();
        FileWriter fileWriter = new FileWriter(filename + ".csv");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        StringBuilder header = new StringBuilder();
        header.append("# Transações");
        header.append(";");
        boolean headerDone = false;
        int resource = 0;
        for (List<SimpleEntry<String, SimpleEntry<Double, SimpleEntry>>> resultadosResourceI : resultadosTi) {
            if(filename.equals("t3")){
                sb.append((resource+1)*1000000);
            }
            else {
                sb.append(ltcs.get(resource).size());
            }
            sb.append(";");
            resource++;
            for (SimpleEntry<String, SimpleEntry<Double, SimpleEntry>> resultado : resultadosResourceI) {
                if (!headerDone) {
                    header.append(resultado.getKey());
                    header.append(";");
                }
                sb.append(resultado.getValue().getKey());
                sb.append(";");
            }
            headerDone = true;
            header.delete(header.length()-1, header.length());
            header.append("\n");
            sb.delete(sb.length()-1, sb.length());
            sb.append("\n");
        }
        printWriter.print(header.toString());
        printWriter.print(sb.toString().replaceAll("\\.", ","));
        printWriter.close();
    }

    /*
     *  SOLUÇOES PARA O TESTE 1
     */
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao1ForArraySum = ltc -> () -> {
        double[] doubleArr = ltc.stream().mapToDouble(TransCaixa::getValor).toArray();
        double sum = 0;
        for (int i = 0; i < doubleArr.length; sum+= doubleArr[i], i++);

        return new SimpleEntry<>(sum, null);
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao1ForArrayAvg = ltc -> () -> {
        double[] doubleArr = ltc.stream().mapToDouble(TransCaixa::getValor).toArray();
        double sum = 0;
        double count = 0;
        for (int i = 0; i < doubleArr.length; sum += doubleArr[i], i++, count++);
        return new SimpleEntry<>(sum/count, null);
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao1ForEachSum = ltc -> () -> {
        double[] doubleArr = ltc.stream().mapToDouble(TransCaixa::getValor).toArray();
        double sum = 0;
        for (double val : doubleArr) {
            sum += val;
        }
        return new SimpleEntry<>(sum, null);
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao1ForEachAvg = ltc -> () -> {
        double[] doublAarr = ltc.stream().mapToDouble(TransCaixa::getValor).toArray();
        double sum = 0;
        double count = 0;
        for (double val : doublAarr) {
            sum += val;
            count++;
        }
        return new SimpleEntry<>(sum/count, null);
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao1DoubleStreamSum = ltc -> () -> {
        return new SimpleEntry<>(ltc.stream().mapToDouble(TransCaixa::getValor).sum(), null);
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao1DoubleStreamAvg = ltc -> () -> {
        return new SimpleEntry<>(ltc.stream().mapToDouble(TransCaixa::getValor).average().getAsDouble(), null);
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao1ParallelDoubleStreamSum = ltc -> () -> {
        return new SimpleEntry<>(ltc.parallelStream().mapToDouble(TransCaixa::getValor).sum(), null);
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao1ParallelDoubleStreamAvg = ltc -> () -> {
        return new SimpleEntry<>(ltc.parallelStream().mapToDouble(TransCaixa::getValor).average().getAsDouble(), null);
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao1StreamSum = ltc -> () -> {
        return new SimpleEntry<>(ltc.stream().map(TransCaixa::getValor).reduce(0.0, (x1, x2) -> (Double) x1 + (Double) x2), null);
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao1StreamAvg = ltc -> () -> {
        AtomicReference<Double> sum = new AtomicReference<>((double) 0);
        AtomicReference<Double> count = new AtomicReference<>((double) 0);

        ltc.stream().forEach(t -> { sum.updateAndGet(v -> new Double((double) (v + t.getValor()))); count.updateAndGet(v -> new Double((double) (v + 1))); });

        return new SimpleEntry<>(sum.get() / count.get(), null);
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao1ParallelStreamSum = ltc -> () -> {
        return new SimpleEntry<>(ltc.parallelStream().map(TransCaixa::getValor).reduce(0.0, (x1, x2) -> (Double) x1 + (Double) x2), null);
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao1ParallelStreamAvg = ltc -> () -> {
        SimpleEntry<Double,Double> countAndSum = ltc.parallelStream().map((TransCaixa t) -> new SimpleEntry<>(1.0, t.getValor())).reduce(new SimpleEntry<>(0.0,0.0),
                (SimpleEntry<Double,Double> a, SimpleEntry<Double,Double> b) -> new SimpleEntry<>(a.getKey()+b.getKey(), a.getValue() + b.getValue()));

        return new SimpleEntry<>(countAndSum.getValue() / countAndSum.getKey(), null);
    };

    /*
     *  SOLUÇÕES PARA O TESTE 2
     */
    private static Comparator<TransCaixa> transCaixaComparator = (TransCaixa tc1, TransCaixa tc2) -> {
        if (Objects.equals(tc1, tc2)) return 0;
        else if(tc1 == null) return -1;
        else if(tc2 == null) return +1;

        if (tc1.getData().isBefore(tc2.getData())) {
            return -1;
        }
        else if (tc1.getData().isAfter(tc2.getData())) {
            return 1;
        }
        return tc1.getTrans().compareTo(tc2.getTrans());
    };
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao2S = ltc -> () -> {
        int size = ltc.size();
        List<TransCaixa> sortedList = ltc.stream()
                .sorted(transCaixaComparator)
                .collect(toList());
        List<TransCaixa> l1 = sortedList.subList(0, size/3);
        List<TransCaixa> l2 = sortedList.subList(size - size/3, size);
        return new SimpleEntry<>(sortedList, null);//l1, l2);
    };
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao2PS = ltc -> () -> {
        int size = ltc.size();
        List<TransCaixa> sortedList = ltc.parallelStream()
                .sorted(transCaixaComparator)
                .collect(toList());
        List<TransCaixa> l1 = sortedList.subList(0, size/3);
        List<TransCaixa> l2 = sortedList.subList(size - size/3, size);
        return new SimpleEntry<>(l1, l2);
    };
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao2L = ltc -> () -> {
        int size = ltc.size();
        ltc.sort(transCaixaComparator);
        List<TransCaixa> l1 = ltc.subList(0, size/3);
        List<TransCaixa> l2 = ltc.subList(size - size/3, size);
        return new SimpleEntry<>(l1, l2);
    };
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao2TS = ltc -> () -> {
        TreeSet<TransCaixa> ts = new TreeSet<>(transCaixaComparator);
        ts.addAll(ltc);
        List<TransCaixa> l1 = new ArrayList<>();
        List<TransCaixa> l2 = new ArrayList<>();
        int size = ts.size();
        int i = 0;
        for (TransCaixa tc : ts) {
            if (i < size / 3) {
                l1.add(tc);
            } else break;
            i++;
        }
        i = 0;
        for (TransCaixa tc : ts.descendingSet()) {
            if (i < size/3) {
                l2.add(tc);
            } else break;
            i++;
        }
        return new SimpleEntry<>(l1, l2);
    };


    /*
     * SOLUÇÕES PARA O TESTE 3
     */
    private static Function<List<Integer>, Supplier<SimpleEntry>> solucao3intStream = arg -> () -> {
        IntStream array = new Random().ints(arg.get(0), 0, 9999).distinct();
        return new SimpleEntry<>(array, null);
    };

    private static int randomFill(){
        Random rand = new Random();
        return  rand.nextInt(9999);
    }

    private static Function<List<Integer>, Supplier<SimpleEntry>> solucao3intArray = arg -> () -> {
        int[] randomArray = new int[arg.get(0)];

        for(int i=0;i<randomArray.length;i++){
            randomArray[i]= randomFill();
        }

        int finalSize = randomArray.length;
        for(int i=0; i< finalSize; i++){
            for(int j= i+1; j< finalSize; j++){
                if(randomArray[i] == randomArray[j]){
                    randomArray[j] = randomArray[finalSize-1];
                    finalSize --;
                    j--;
                }
            }
        }
        randomArray = Arrays.copyOf(randomArray, finalSize);
        return new SimpleEntry<>(randomArray,null);
    };

    private static Function<List<Integer>, Supplier<SimpleEntry>> solucao3intArrayWithSort = arg -> () -> {
        int[] randomArray = new int[arg.get(0)];

        for(int i=0;i<randomArray.length;i++){
            randomArray[i]= randomFill();
        }

        Arrays.sort(randomArray);
        int target, i;
        for(i=0,target=0;i<randomArray.length-1;i++) {
            if (randomArray[i] != randomArray[i + 1]) {
                randomArray[target++] = randomArray[i];
            }
        }
        randomArray = Arrays.copyOf(randomArray,target);

        return new SimpleEntry<>(randomArray,null);
    };

    private static void testUniqueValuesArray(int [] randomArray, String from){
        for(int i = 0; i< randomArray.length ; i++){
            for(int j= i+1; j< randomArray.length; j++){
                if(randomArray[i]== randomArray[j]){
                    System.out.println("ERRO!!!" + randomArray[i] + "==" + randomArray[j] + "FROM->>>" + from);
                }
            }
        }
    }

    private static void testUniqueValuesList(List<Integer> randomArray, String from){
        for(int i = 0; i< randomArray.size() ; i++){
            for(int j= i+1; j< randomArray.size(); j++){
                if(randomArray.get(i).equals(randomArray.get(j))){
                    System.out.println("ERRO!!!" + randomArray.get(i) + "==" + randomArray.get(j) + "FROM->>>" + from);
                }
            }
        }
    }

    private static Function<List<Integer>, Supplier<SimpleEntry>>  solucao3listIntegerWithSet = arg -> () -> {
        List<Integer> randomList = new ArrayList<>();
        for (int i = 0; i < arg.get(0); i++) {
            randomList.add(randomFill());
        }

        Set<Integer> set = new TreeSet<>(randomList);
        randomList.clear();
        randomList.addAll(set);
        return new SimpleEntry<>(randomList, null);
    };

    private static Function<List<Integer>, Supplier<SimpleEntry>>  solucao3listInteger = arg -> () -> {
        List<Integer> randomList = new ArrayList<>();
        for(int i=0;i<arg.get(0) ; i++){
            randomList.add(randomFill());
        }
        List<Integer> noDuplicates = new ArrayList<>();
        for(Integer i : randomList){
            if(!noDuplicates.contains(i))
                noDuplicates.add(i);
        }

        return new SimpleEntry<>(noDuplicates,null);
    };

    /*
     * SOLUÇÕES PARA O TESTE 4
     */
    private static BiFunction<Double, Double, Double> multBifunction = (i1, i2) -> i1 * i2;

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao4multBiFStreamSeq = ltc -> () -> {
        double[] transcaixa= ltc.stream().mapToDouble(TransCaixa::getValor).toArray();
        double iva = 0.23;
        transcaixa = Arrays.stream(transcaixa).map(a -> multBifunction.apply(a,iva)).toArray();

        return new SimpleEntry<>(transcaixa,null);
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao4multBiFStreamParallel = ltc -> () -> {
        double[] transcaixa= ltc.stream().mapToDouble(TransCaixa::getValor).toArray();
        double iva = 0.23;
        Arrays.parallelSetAll(transcaixa, a -> multBifunction.apply((double) a,iva));
        return new SimpleEntry<>(transcaixa,null);
    };

    private static Double multstatic(Double i1, Double i2){
        return i1*i2;
    }

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao4multMethodStreamSeq = ltc -> () -> {
        double[] transcaixa= ltc.stream().mapToDouble(TransCaixa::getValor).toArray();
        double iva = 0.23;
        transcaixa = Arrays.stream(transcaixa).map(a -> multstatic(a,iva)).toArray();
        return new SimpleEntry<>(transcaixa,null);
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao4multMethodStreamParallel = ltc -> () -> {
        double[] transcaixa= ltc.stream().mapToDouble(TransCaixa::getValor).toArray();
        double iva = 0.23;
        Arrays.parallelSetAll(transcaixa, a -> multstatic((double)a,iva));
        return new SimpleEntry<>(transcaixa,null);
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao4multLambStreamSeq = ltc -> () -> {
        double[] transcaixa= ltc.stream().mapToDouble(TransCaixa::getValor).toArray();
        double iva= 0.23;
        transcaixa = Arrays.stream(transcaixa).map(a -> a * iva).toArray();
        return new SimpleEntry<>(transcaixa,null);
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao4multLambStreamParallel = ltc -> () -> {
        double[] transcaixa= ltc.stream().mapToDouble(TransCaixa::getValor).toArray();
        double iva = 0.23;
        Arrays.parallelSetAll(transcaixa, a -> a * iva);
        return new SimpleEntry<>(transcaixa,null);
    };

    /*
     *  SOLUÇÕES PARA O TESTE 5
     */
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao5TreeSet = ltc -> () -> {
        Comparator<TransCaixa> compexc2 = Comparator.comparing(TransCaixa::getTrans);

        Supplier<TreeSet<TransCaixa>> supplyTreeSet = () -> new TreeSet<>(compexc2);

        return new SimpleEntry<>(ltc.stream().collect(toCollection(supplyTreeSet)), null);
    };
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao5SortedList = ltc -> () -> {
        Comparator<TransCaixa> compexc2 = Comparator.comparing(TransCaixa::getTrans);
        return new SimpleEntry<>(ltc.stream().sorted(compexc2).collect(Collectors.toList()), null);
    };

    /*
     *  SOLUÇÕES PARA O TESTE 6
     */
    // MES -> DIA -> HORA
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao6MDHS = ltc -> () -> {
        Map<Month, Map<Integer, Map<Integer, List<TransCaixa>>>> mapaTxPorMDH =
                ltc.stream()
                        .collect(groupingBy(t -> t.getData().getMonth(),
                                groupingBy(t -> t.getData().getDayOfMonth(),
                                        groupingBy(t -> t.getData().getHour()))));
        return new SimpleEntry<>(mapaTxPorMDH, null);
    };
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao6MDHPS = ltc -> () -> {
        Map<Month, ConcurrentMap<Integer, ConcurrentMap<Integer, List<TransCaixa>>>> mapaTxPorMDH =
                ltc.parallelStream()
                        .collect(groupingByConcurrent(t -> t.getData().getMonth(),
                                groupingByConcurrent(t -> t.getData().getDayOfMonth(),
                                        groupingByConcurrent(t -> t.getData().getHour()))));
        return new SimpleEntry<>(mapaTxPorMDH, null);
    };
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao6MDHL = ltc -> () -> {
        Map<Month, Map<Integer, Map<Integer, List<TransCaixa>>>> mapaTxPorMDH = new TreeMap<>();
        for (TransCaixa tc : ltc) {
            Month mes = tc.getData().getMonth();
            int dia = tc.getData().getDayOfMonth();
            int hora = tc.getData().getHour();
            mapaTxPorMDH.computeIfAbsent(mes, k -> new HashMap<>());
            mapaTxPorMDH.get(mes).computeIfAbsent(dia, k -> new HashMap<>());
            mapaTxPorMDH.get(mes).get(dia).computeIfAbsent(hora, k -> new ArrayList<>());
            mapaTxPorMDH.get(mes).get(dia).get(hora).add(tc);
        }
        return new SimpleEntry<>(mapaTxPorMDH, null);
    };

    // DIA DA SEMANA -> HORA
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao6DHS = ltc -> () -> {
        Map<DayOfWeek, Map<Integer, List<TransCaixa>>> mapaTxPorDH =
                ltc.stream()
                        .collect(groupingBy(t -> t.getData().getDayOfWeek(),
                                groupingBy(t -> t.getData().getHour())));
        return new SimpleEntry<>(mapaTxPorDH, null);
    };
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao6DHPS = ltc -> () -> {
        Map<DayOfWeek, ConcurrentMap<Integer, List<TransCaixa>>> mapaTxPorDH =
                ltc.parallelStream()
                   .collect(groupingByConcurrent(t -> t.getData().getDayOfWeek(),
                                       groupingByConcurrent(t -> t.getData().getHour())));
        return new SimpleEntry<>(mapaTxPorDH, null);
    };
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao6DHL = ltc -> () -> {
        Map<DayOfWeek, Map<Integer, List<TransCaixa>>> mapaTxPorDH = new TreeMap<>();
        for (TransCaixa tc : ltc) {
            DayOfWeek dia = tc.getData().getDayOfWeek();
            int hora = tc.getData().getHour();
            mapaTxPorDH.computeIfAbsent(dia, k -> new HashMap<>());
            mapaTxPorDH.get(dia).computeIfAbsent(hora, k -> new ArrayList<>());
            mapaTxPorDH.get(dia).get(hora).add(tc);
        }
        return new SimpleEntry<>(mapaTxPorDH, null);
    };


    /*
     *  SOLUÇÕES PARA O TESTE 7
     */
    //Full
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao7FullS = ltc -> () -> {
        double total = ltc.stream().mapToDouble(TransCaixa::getValor).sum();
        return new SimpleEntry<>(total, null);
    };
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao7FullPS = ltc -> () -> {
        double total = ltc.parallelStream().mapToDouble(TransCaixa::getValor).sum();
        return new SimpleEntry<>(total, null);
    };
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao7FullL = ltc -> () -> {
        double total = 0;
        for (TransCaixa tc : ltc) {
            total += tc.getValor();
        }
        return new SimpleEntry<>(total, null);
    };
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao7FullLFE = ltc -> () -> {
        final double[] total = {0};
        ltc.forEach(tc -> total[0] += tc.getValor());
        return new SimpleEntry<>(total[0], null);
    };

    //Split with sublist
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao7SplitListS = ltc -> () -> {
        double total = 0;
        int size = ltc.size();
        List<TransCaixa> l1 = ltc.subList(0, size/4);
        List<TransCaixa> l2 = ltc.subList(size/4, size/2);
        List<TransCaixa> l3 = ltc.subList(size/2, (3*size)/4);
        List<TransCaixa> l4 = ltc.subList((3*size)/4, size);

        Callable<Double> t1 = () -> l1.stream().mapToDouble(TransCaixa::getValor).sum();
        Callable<Double> t2 = () -> l2.stream().mapToDouble(TransCaixa::getValor).sum();
        Callable<Double> t3 = () -> l3.stream().mapToDouble(TransCaixa::getValor).sum();
        Callable<Double> t4 = () -> l4.stream().mapToDouble(TransCaixa::getValor).sum();

        ForkJoinPool fjp = ForkJoinPool.commonPool();
        total = fjp.invokeAll(Arrays.asList(t1, t2, t3, t4))
                   .stream()
                   .map(future -> {
                       try {
                           return future.get();
                       }
                       catch (Exception e) {
                           throw new IllegalStateException(e);
                       }
                   })
                   .mapToDouble(d -> d).sum();
        return new SimpleEntry<>(total, null);
    };
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao7SplitListPS = ltc -> () -> {
        double total = 0;
        int size = ltc.size();
        List<TransCaixa> l1 = ltc.subList(0, size/4);
        List<TransCaixa> l2 = ltc.subList(size/4, size/2);
        List<TransCaixa> l3 = ltc.subList(size/2, (3*size)/4);
        List<TransCaixa> l4 = ltc.subList((3*size)/4, size);

        Callable<Double> t1 = () -> l1.parallelStream().mapToDouble(TransCaixa::getValor).sum();
        Callable<Double> t2 = () -> l2.parallelStream().mapToDouble(TransCaixa::getValor).sum();
        Callable<Double> t3 = () -> l3.parallelStream().mapToDouble(TransCaixa::getValor).sum();
        Callable<Double> t4 = () -> l4.parallelStream().mapToDouble(TransCaixa::getValor).sum();

        ForkJoinPool fjp = ForkJoinPool.commonPool();
        total = fjp.invokeAll(Arrays.asList(t1, t2, t3, t4))
                   .parallelStream()
                   .map(future -> {
                       try {
                           return future.get();
                       }
                       catch (Exception e) {
                           throw new IllegalStateException(e);
                       }
                   })
                   .mapToDouble(d -> d).sum();
        return new SimpleEntry<>(total, null);
    };
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao7SplitListL = ltc -> () -> {
        double total = 0;
        int size = ltc.size();
        List<TransCaixa> l1 = ltc.subList(0, size/4);
        List<TransCaixa> l2 = ltc.subList(size/4, size/2);
        List<TransCaixa> l3 = ltc.subList(size/2, (3*size)/4);
        List<TransCaixa> l4 = ltc.subList((3*size)/4, size);

        Callable<Double> t1 = () -> {
            double total1 = 0;
            for (TransCaixa tc : l1) {
                total1 += tc.getValor();
            }
            return total1;
        };
        Callable<Double> t2 = () -> {
            double total2 = 0;
            for (TransCaixa tc : l2) {
                total2 += tc.getValor();
            }
            return total2;
        };
        Callable<Double> t3 = () -> {
            double total3 = 0;
            for (TransCaixa tc : l3) {
                total3 += tc.getValor();
            }
            return total3;
        };
        Callable<Double> t4 = () -> {
            double total4 = 0;
            for (TransCaixa tc : l4) {
                total4 += tc.getValor();
            }
            return total4;
        };

        ForkJoinPool fjp = ForkJoinPool.commonPool();
        total = fjp.invokeAll(Arrays.asList(t1, t2, t3, t4))
                   .stream()
                   .map(future -> {
                       try {
                           return future.get();
                       }
                       catch (Exception e) {
                           throw new IllegalStateException(e);
                       }
                   })
                   .mapToDouble(d -> d).sum();
        return new SimpleEntry<>(total, null);
    };
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao7SplitListLFE = ltc -> () -> {
        double total = 0;
        int size = ltc.size();
        List<TransCaixa> l1 = ltc.subList(0, size/4);
        List<TransCaixa> l2 = ltc.subList(size/4, size/2);
        List<TransCaixa> l3 = ltc.subList(size/2, (3*size)/4);
        List<TransCaixa> l4 = ltc.subList((3*size)/4, size);

        Callable<Double> t1 = () -> {
            final double[] total1 = {0};
            l1.forEach(tc -> total1[0] += tc.getValor());
            return total1[0];
        };
        Callable<Double> t2 = () -> {
            final double[] total2 = {0};
            l2.forEach(tc -> total2[0] += tc.getValor());
            return total2[0];
        };
        Callable<Double> t3 = () -> {
            final double[] total3 = {0};
            l3.forEach(tc -> total3[0] += tc.getValor());
            return total3[0];
        };
        Callable<Double> t4 = () -> {
            final double[] total4 = {0};
            l4.forEach(tc -> total4[0] += tc.getValor());
            return total4[0];
        };

        ForkJoinPool fjp = ForkJoinPool.commonPool();
        total = fjp.invokeAll(Arrays.asList(t1, t2, t3, t4))
                   .stream()
                   .map(future -> {
                       try {
                           return future.get();
                       }
                       catch (Exception e) {
                           throw new IllegalStateException(e);
                       }
                   })
                   .mapToDouble(d -> d).sum();
        return new SimpleEntry<>(total, null);
    };

    //Spliterator
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao7SplitSplit = ltc -> () -> {
        double total = 0;
        Spliterator<TransCaixa> stc1 = ltc.spliterator();
        Spliterator<TransCaixa> stc3 = stc1.trySplit();
        Spliterator<TransCaixa> stc2 = stc1.trySplit();
        Spliterator<TransCaixa> stc4 = stc3.trySplit();

        Callable<Double> t1 = () -> {
            final double[] total1 = {0};
            stc1.forEachRemaining(tc -> total1[0] += tc.getValor());
            return total1[0];
        };
        Callable<Double> t2 = () -> {
            final double[] total2 = {0};
            stc2.forEachRemaining(tc -> total2[0] += tc.getValor());
            return total2[0];
        };
        Callable<Double> t3 = () -> {
            final double[] total3 = {0};
            stc3.forEachRemaining(tc -> total3[0] += tc.getValor());
            return total3[0];
        };
        Callable<Double> t4 = () -> {
            final double[] total4 = {0};
            stc4.forEachRemaining(tc -> total4[0] += tc.getValor());
            return total4[0];
        };

        ForkJoinPool fjp = ForkJoinPool.commonPool();
        total = fjp.invokeAll(Arrays.asList(t1, t2, t3, t4))
                   .stream()
                   .map(future -> {
                       try {
                           return future.get();
                       }
                       catch (Exception e) {
                           throw new IllegalStateException(e);
                       }
                   })
                   .mapToDouble(d -> d).sum();
        return new SimpleEntry<>(total, null);
    };


    /*
     *  SOLUÇÕES PARA O TESTE 8
     */
    private static Comparator<TransCaixa> transCaixaMaxValueComparator = (TransCaixa tc1, TransCaixa tc2) -> {
        if (Objects.equals(tc1, tc2)) return 0;
        else if(tc1 == null) return -1;
        else if(tc2 == null) return +1;

        if (tc1.getValor() > tc2.getValor())
            return 1;
        else if (tc1.getValor() < tc2.getValor())
            return -1;
        return 0;
    };
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao8S = ltc -> () -> {
        Optional<TransCaixa> res = ltc.stream()
                .filter(tc -> tc.getData().getHour() >= 16 &&
                        tc.getData().getHour() < 22)
                .max(transCaixaMaxValueComparator);
        return new SimpleEntry<>(res, null);
    };
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao8PS = ltc -> () -> {
        Optional<TransCaixa> res = ltc.parallelStream()
                                      .filter(tc -> tc.getData().getHour() >= 16 &&
                                                    tc.getData().getHour() < 22)
                                      .max(transCaixaMaxValueComparator);
        return new SimpleEntry<>(res, null);
    };
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao8L = ltc -> () -> {
        TransCaixa max = ltc.get(0);
        for(TransCaixa tc : ltc) {
            if (tc.getData().getHour() >= 16 && tc.getData().getHour() < 22) {
                if (max.getValor() < tc.getValor())
                    max = tc;
            }
        }
        return new SimpleEntry<>(max, null);
    };

    /*
     *  SOLUÇÕES PARA O TESTE 9
     */
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao9SemStreams = ltc -> () -> {
        Map<Long, List<TransCaixa>> grupo = ltc.stream().collect(
                groupingBy((TransCaixa t1) -> ChronoUnit.WEEKS.between(LocalDateTime.now().with(firstDayOfYear()),t1.getData())));

        Map<Long, Double> totalFaturado = new HashMap<>();
        for (Map.Entry<Long,List<TransCaixa>> entry : grupo.entrySet()) {
            List<TransCaixa> caixasDaSemana = entry.getValue();
            double faturaDaSemana = 0;

            for (TransCaixa caixa : caixasDaSemana) {
                faturaDaSemana += caixa.getValor();
            }

            totalFaturado.put(entry.getKey(), faturaDaSemana);
        }

        return new SimpleEntry<>(totalFaturado, null);
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao9ComUmStream = ltc -> () -> {
        Map<Long, List<TransCaixa>> grupo = ltc.stream().collect(
                groupingBy((TransCaixa t1) -> ChronoUnit.WEEKS.between(LocalDateTime.now().with(firstDayOfYear()),t1.getData())));

        Map<Long,Double> totalFaturado = new HashMap<>();
        grupo.entrySet().forEach((Map.Entry<Long,List<TransCaixa>> e) -> totalFaturado.put(e.getKey(), e.getValue().stream().mapToDouble(TransCaixa::getValor).sum()));

        return new SimpleEntry<>(totalFaturado, null);
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao9ComDoisStreams = ltc -> () -> {
        Map<Long, List<TransCaixa>> grupo = ltc.stream().collect(
                groupingBy((TransCaixa t1) -> ChronoUnit.WEEKS.between(LocalDateTime.now().with(firstDayOfYear()),t1.getData())));

        return new SimpleEntry<>(grupo.entrySet()
                .stream()
                .map((Map.Entry<Long,List<TransCaixa>> e)
                        -> new SimpleEntry<>(e.getKey(), e.getValue().stream().mapToDouble(TransCaixa::getValor).sum()))
                .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)), null);
    };

    /*
     *  SOLUÇÕES PARA O TESTE 10
     */
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao10ComStream = ltc -> () -> {
        return new SimpleEntry<>(ltc.stream().collect(groupingBy((TransCaixa t) -> t.getData().getMonth(),
                summingDouble((TransCaixa t) -> {
                    double val = t.getValor();
                    return (val < 20) ? (val * 0.12) : (val >= 20 && val <= 29) ? (val * 0.2) : (val * 0.23);
                }))), null);
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao10Iterativo = ltc -> () -> {
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

        return new SimpleEntry<>(pagamentosIvasMeses, null);
    };


    /*
     * SOLUÇÕES PARA O TESTE 12
     */
    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao12Map = ltc -> () ->{
        //Map<nCaixa, Map<Mes,List<Transcaixa>>
        Map<String, Map<Integer,List<TransCaixa>>> tabelas= ltc.stream().collect(groupingBy(TransCaixa::getCaixa,
                groupingBy(t -> t.getData().getMonth().getValue())));

        Map<String,Double> totalFaturado = tabelas.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                elem -> elem.getValue()
                        .entrySet()
                        .stream()
                        .mapToDouble(list-> list.getValue()
                                .stream()
                                .mapToDouble(TransCaixa::getValor)
                                .sum()
                        ).sum()
                )
        );
        return new SimpleEntry<>(totalFaturado,null);
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao12ConcurrentMap = ltc -> () ->{
        //Map<nCaixa, Map<Mes,List<Transcaixa>>
        ConcurrentMap<String, ConcurrentMap<Integer,List<TransCaixa>>> tabelas= ltc.stream().collect(groupingByConcurrent(TransCaixa::getCaixa,
                groupingByConcurrent(t-> t.getData().getMonth().getValue())));

        ConcurrentMap<String,Double> totalFaturado = tabelas.entrySet().stream()
                .collect(Collectors.toConcurrentMap(Map.Entry::getKey,
                        elem -> elem.getValue()
                                .entrySet()
                                .stream()
                                .mapToDouble(list-> list.getValue()
                                        .stream()
                                        .mapToDouble(TransCaixa::getValor)
                                        .sum()
                                ).sum()
                        )
                );

        return new SimpleEntry<>(totalFaturado,null);
    };



    public static void main(String[] args) throws IOException {

        List<String> file = Arrays.asList("TransCaixaResources/transCaixa1M.txt");

        // Lista dos ficheiros de dados
        List<String> files = Arrays.asList("TransCaixaResources/transCaixa1M.txt",
                "TransCaixaResources/transCaixa2M.txt",
                "TransCaixaResources/transCaixa4M.txt",
                "TransCaixaResources/transCaixa6M.txt");
        // Load dos dados
        loadFiles(files);

        List<List<SimpleEntry<String, Function>>> testes = new ArrayList<>();

        // TESTE 1
        List<SimpleEntry<String, Function>> solucoesT1 = Arrays.asList(
                new SimpleEntry<>("solucao1ForArraySum", solucao1ForArraySum),
                new SimpleEntry<>("solucao1ForArrayAvg", solucao1ForArrayAvg),
                new SimpleEntry<>("solucao1ForEachSum", solucao1ForEachSum),
                new SimpleEntry<>("solucao1ForEachAvg", solucao1ForEachAvg),
                new SimpleEntry<>("solucao1DoubleStreamSum", solucao1DoubleStreamSum),
                new SimpleEntry<>("solucao1DoubleStreamAvg", solucao1DoubleStreamAvg),
                new SimpleEntry<>("solucao1ParallelDoubleStreamSum", solucao1ParallelDoubleStreamSum),
                new SimpleEntry<>("solucao1ParallelDoubleStreamAvg", solucao1ParallelDoubleStreamAvg),
                new SimpleEntry<>("solucao1StreamSum", solucao1StreamSum),
                new SimpleEntry<>("solucao1StreamAvg", solucao1StreamAvg),
                new SimpleEntry<>("solucao1ParallelStreamSum", solucao1ParallelStreamSum),
                new SimpleEntry<>("solucao1ParallelStreamSum", solucao1ParallelStreamAvg)
        );
        testes.add(solucoesT1);

        // TESTE 2
        List<SimpleEntry<String, Function>> solucoesT2 = Arrays.asList(
                new SimpleEntry<>("List", solucao2L),
                new SimpleEntry<>("TreeSet", solucao2TS),
                new SimpleEntry<>("Streams", solucao2S),
                new SimpleEntry<>("Parallel Streams", solucao2PS));
        testes.add(solucoesT2);

        // TESTE 3
        List<SimpleEntry<String, Function>> solucoesT3 = Arrays.asList(
                new SimpleEntry<>("solucao3Array", solucao3intArray),
                new SimpleEntry<>("solucao3ArraySorted",solucao3intArrayWithSort),
                new SimpleEntry<>("solucao3IntStream", solucao3intStream),
                new SimpleEntry<>("solucao3List", solucao3listInteger),
                new SimpleEntry<>("solucao3ListWithSet", solucao3listIntegerWithSet));
        testes.add(solucoesT3);

        //TESTE 4

        List<SimpleEntry<String, Function>> solucoesT4 = Arrays.asList(
                new SimpleEntry<>("Bi-Function seq", solucao4multBiFStreamSeq),
                new SimpleEntry<>("Bi-Function parallel", solucao4multBiFStreamParallel),
                new SimpleEntry<>("Exp-Lambda seq", solucao4multLambStreamSeq),
                new SimpleEntry<>("Exp-Lambda parallel", solucao4multLambStreamParallel),
                new SimpleEntry<>("Metodo Static seq", solucao4multMethodStreamSeq),
                new SimpleEntry<>("Metodo Static parallel", solucao4multMethodStreamParallel));
        testes.add(solucoesT4);

        // TESTE 5
        List<SimpleEntry<String, Function>> solucoesT5 = Arrays.asList(
                new SimpleEntry<>("solucao5TreeSet", solucao5TreeSet),
                new SimpleEntry<>("solucao5SortedList", solucao5SortedList));
        testes.add(solucoesT5);

        // TESTE 6
        List<SimpleEntry<String, Function>> solucoesT6 = Arrays.asList(
                new SimpleEntry<>("List (Mes -> Dia -> Hora)", solucao6MDHL),
                new SimpleEntry<>("Streams (Mes -> Dia -> Hora)", solucao6MDHS),
                new SimpleEntry<>("Parallel Streams (Mes -> Dia -> Hora)", solucao6MDHPS),
                new SimpleEntry<>("List (Dia da semana -> Hora)", solucao6DHL),
                new SimpleEntry<>("Streams (Dia da semana -> Hora)", solucao6DHS),
                new SimpleEntry<>("Parallel Streams (Dia da semana -> Hora)", solucao6DHPS));
        testes.add(solucoesT6);

        // TESTE 7
        List<SimpleEntry<String, Function>> solucoesT7 = Arrays.asList(
                new SimpleEntry<>("List (Completo)", solucao7FullL),
                new SimpleEntry<>("List + ForEach (Completo)", solucao7FullLFE),
                new SimpleEntry<>("Streams (Completo)", solucao7FullS),
                new SimpleEntry<>("Parallel Streams (Completo)", solucao7FullPS),
                new SimpleEntry<>("List (Partições)", solucao7SplitListL),
                new SimpleEntry<>("List + ForEach (Partições)", solucao7SplitListLFE),
                new SimpleEntry<>("Stream (Partições)", solucao7SplitListS),
                new SimpleEntry<>("Parallel Streams (Partições)", solucao7SplitListPS),
                new SimpleEntry<>("Spliterator (Partições)", solucao7SplitSplit));
        testes.add(solucoesT7);

        // TESTE 8
        List<SimpleEntry<String, Function>> solucoesT8 = Arrays.asList(
                new SimpleEntry<>("List", solucao8L),
                new SimpleEntry<>("Streams", solucao8S),
                new SimpleEntry<>("Parallel Streams", solucao8PS));
        testes.add(solucoesT8);

        // TESTE 9
        List<SimpleEntry<String, Function>> solucoesT9 = Arrays.asList(
                new SimpleEntry<>("solucao9SemStreams", solucao9SemStreams),
                new SimpleEntry<>("solucao9ComUmStream", solucao9ComUmStream),
                new SimpleEntry<>("solucao9ComDoisStreams", solucao9ComDoisStreams));
        testes.add(solucoesT9);

        // TESTE 10
        List<SimpleEntry<String, Function>> solucoesT10 = Arrays.asList(
                new SimpleEntry<>("solucao10ComStream", solucao10ComStream),
                new SimpleEntry<>("solucao10Iterativo", solucao10Iterativo));
        testes.add(solucoesT10);

        // TESTE 11
        //Aplicar para T1,T3,T7,T9


        // TESTE 12
        List<SimpleEntry<String, Function>> solucoesT12 = Arrays.asList(
                new SimpleEntry<>("Total faturado->ConcurrentMap", solucao12ConcurrentMap),
                new SimpleEntry<>("Total faturado-> Map", solucao12Map));
        testes.add(solucoesT12);


        List<List<List<SimpleEntry<String, SimpleEntry<Double, SimpleEntry>>>>> resultados = runTestes(testes);

        String[] nomeTestes = {"1","2","3","4","5", "6", "7", "8","9","10","12"};
        int n = 0;
        for (List<List<SimpleEntry<String, SimpleEntry<Double, SimpleEntry>>>> resultadosTi : resultados) {
            genCSVTable("t" + nomeTestes[n], resultadosTi);
            n++;
        }
    }
}