package rfilter.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    public static void main(String [] args) {
        // initial empty stream
        Stream<Report> stream = Stream.empty();
        for (String filename : args) {
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
        // print header and sorted output
        System.out.println(Report.HEADER);
        stream.sorted((a, b) -> a.requestTime.compareTo(b.requestTime)).forEach(System.out::println);
    }
}

