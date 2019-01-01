/**
 *
 * @author fmm 2017
 */

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.lang.System.out;
import static java.util.stream.Collectors.toList;


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

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao2S = ltc -> {
        Supplier<SimpleEntry> t2S = () -> {
            int size = ltc.size();
            List<TransCaixa> sortedList = ltc.stream()
                                             .sorted(transCaixaComparator)
                                             .collect(toList());
            List<TransCaixa> l1 = sortedList.subList(0, size/3);
            List<TransCaixa> l2 = sortedList.subList(size - size/3, size);
            return new SimpleEntry<>(l1, l2);
        };
        return t2S;
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao2PS = ltc -> {
        Supplier<SimpleEntry> t2PS = () -> {
            int size = ltc.size();
            List<TransCaixa> sortedList = ltc.parallelStream()
                                             .sorted(transCaixaComparator)
                                             .collect(toList());
            List<TransCaixa> l1 = sortedList.subList(0, size/3);
            List<TransCaixa> l2 = sortedList.subList(size - size/3, size);
            return new SimpleEntry<>(l1, l2);
        };
        return t2PS;
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao2L = ltc -> {
        Supplier<SimpleEntry> t2L = () -> {
            int size = ltc.size();
            ltc.sort(transCaixaComparator);
            List<TransCaixa> l1 = ltc.subList(0, size/3);
            List<TransCaixa> l2 = ltc.subList(size - size/3, size);
            return new SimpleEntry<>(l1, l2);
        };
        return t2L;
    };

    private static Function<List<TransCaixa>, Supplier<SimpleEntry>> solucao2TS = ltc -> {
        Supplier<SimpleEntry> t2TS = () -> {
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
        return t2TS;
    };

    private static Function<List<TransCaixa>, Function<List<SimpleEntry<String, Function>>, List<SimpleEntry<String, Supplier<SimpleEntry>>>>>
    createSuppliers = ltc -> (lsols -> {
        List<SimpleEntry<String, Supplier<SimpleEntry>>> lsups = new ArrayList<>();
        for (SimpleEntry<String, Function> sol : lsols) {
            lsups.add(new SimpleEntry<>(sol.getKey(), (Supplier<SimpleEntry>) sol.getValue().apply(ltc)));
        }
        return lsups;
    });

    private static List<List<List<SimpleEntry<String, SimpleEntry<Double, SimpleEntry>>>>> runTestes(List<List<SimpleEntry<String, Function>>> respostas) {
        List<List<List<SimpleEntry<String, SimpleEntry<Double, SimpleEntry>>>>> resultados = new ArrayList<>();
        for (List<SimpleEntry<String, Function>> respostasTi : respostas) {
            List<List<SimpleEntry<String, SimpleEntry<Double, SimpleEntry>>>> resultadosTi = new ArrayList<>();
            for (List<TransCaixa> ltc : ltcs) {
                List<SimpleEntry<String, SimpleEntry<Double, SimpleEntry>>> resultadosResourceI = new ArrayList<>();
                List<SimpleEntry<String, Supplier<SimpleEntry>>> suppsRespostasTi = createSuppliers.apply(ltc).apply(respostasTi);
                for (SimpleEntry<String, Supplier<SimpleEntry>> supResposta : suppsRespostasTi) {
                    SimpleEntry<Double, SimpleEntry> res = testeBoxGenW(supResposta.getValue());
                    resultadosResourceI.add(new SimpleEntry<>(supResposta.getKey(), res));
                }
                resultadosTi.add(resultadosResourceI);
            }
            resultados.add(resultadosTi);
        }
        return resultados;
    }

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
            sb.append(ltcs.get(resource).size());
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
        List<SimpleEntry<String, Function>> respostasT2 = Arrays.asList(new SimpleEntry<>("List", solucao2L),
                                                                        new SimpleEntry<>("TreeSet", solucao2TS),
                                                                        new SimpleEntry<>("Streams", solucao2S),
                                                                        new SimpleEntry<>("Parallel Streams", solucao2PS));
        testes.add(respostasT2);

        List<List<List<SimpleEntry<String, SimpleEntry<Double, SimpleEntry>>>>> resultados = runTestes(testes);

        int testeNum = 1;
        for (List<List<SimpleEntry<String, SimpleEntry<Double, SimpleEntry>>>> resultadosTi : resultados) {
            genCSVTable("t" + testeNum, resultadosTi);
        }
    }
}
