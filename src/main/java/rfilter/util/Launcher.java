package rfilter.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import rfilter.core.*;

public class Launcher {

    // extension to class name mapping
    // static here but it is possible to set it up dynamically as well
    static HashMap<String, String> typeExtractorMap = new LinkedHashMap<>();
    static {
        typeExtractorMap.put(".xml",  XMLExtractor.class.getCanonicalName());
        typeExtractorMap.put(".json", JSONExtractor.class.getCanonicalName());
        typeExtractorMap.put(".csv",  CSVExtractor.class.getCanonicalName());
    }

    public static Extractor createExtractor(String filename) {
        String extension = ext(filename);
        if (extension == null || extension.length() == 0) {
            return null;
        }
        String className = typeExtractorMap.get(extension);
        try {
            Class<?> c = Class.forName(className);
            java.lang.reflect.Constructor<?> ctor = c.getConstructor(InputStream.class);
            FileInputStream fis = new FileInputStream(filename);
            Object instance = ctor.newInstance(fis);
            if (instance instanceof Extractor) {
                return (Extractor) instance;
            } else {
                return null;
            }
        } catch (ClassNotFoundException |
                 NoSuchMethodException |
                 IllegalAccessException |
                 InstantiationException |
                 InvocationTargetException |
                FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    // old debug method, not used anymore
    public static void iterate(Extractor extractor) {
        for (Report report : extractor) {
            System.out.println(report);
        }
    }

    // pulling external dependency for something that simple is an overkill
    public static String ext(String filename) {
        int idx = filename.lastIndexOf('.');
        return idx >= 0 ? filename.substring(idx) : "";
    }
    public static void usage() {
        System.out.println("usage:");
        System.out.println("appname -b outfile <infiles>");
    }

    public static void main(String [] args) {
        OptionParser parser = new OptionParser("o:");
        OptionSet options = parser.parse(args);
        if (!options.hasArgument("o")) {
            System.out.println("no output file");
            usage();
            return;
        }
        String outfile = (String) options.valueOf("o");
        List<String> remaining = (List<String>) options.nonOptionArguments();
        if (remaining.isEmpty()) {
            System.out.println("no input files");
            usage();
            return;
        }

        // initial empty stream
        Stream<Report> stream = Stream.empty();
        for (String filename : remaining) {
            // create extractor (by file extension)
            Extractor extractor = createExtractor(filename);
            if (extractor != null) {
                Stream<Report> current = StreamSupport.stream(extractor.spliterator(), false);
                stream = Stream.concat(stream,
                                       current.filter(r -> r.packetsServiced != 0));
            } else {
                // any error condition stops the flow
                System.err.println("failed to parse " + filename);
                return;
            }
        }
        Map<String, Long> serviceGuidStats = null;
        PrintWriter out = null;
        try {
            File file = new File(outfile);
            out = new PrintWriter(file);
            // print header
            out.println(Report.HEADER);
            // and sorted output
            // peek() is non-terminal so stream can still be operated upon after we print it
            serviceGuidStats =
                    stream.sorted((a, b) -> a.requestTime.compareTo(b.requestTime))
                            .peek(out::println)
                            .collect(Collectors.groupingBy(r -> r.serviceGuid, Collectors.counting()));

        } catch (FileNotFoundException e) {
            // any error condition stops the flow
            System.err.println("failed to write " + outfile);
            e.printStackTrace();
            return;
        } finally {
            if (out != null) out.close();
        }

        serviceGuidStats.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .forEach(System.out::println);
    }
}

