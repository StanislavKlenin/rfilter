package rfilter.core;

// Extractor interface is as simple as Iterable<Report>;
// it is now, basically, a type alias
public interface Extractor extends Iterable<Report> {
    // TODO: expected extension method, to register in a map of extractors
}
