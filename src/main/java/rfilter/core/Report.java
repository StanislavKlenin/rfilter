package rfilter.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// datatype to represent a report entry
// (similar to Scala case class but now mutable for simplicity)
public class Report {
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");

    public String clientAddress;
    public String clientGuid;
    public Date   requestTime;
    public String serviceGuid;
    public int    retriesRequest;
    public int    packetsRequested;
    public int    packetsServiced;
    public int    maxHoleSize;

    public Report() {
        this("", "", null, "", 0, 0, 0, 0);
    }
    public Report(String clientAddress,
                  String clientGuid,
                  Date   requestTime,
                  String serviceGuid,
                  int    retriesRequest,
                  int    packetsRequested,
                  int    packetsServiced,
                  int    maxHoleSize)
    {
        this.clientAddress = clientAddress;
        this.clientGuid = clientGuid;
        this.requestTime = requestTime;
        this.serviceGuid = serviceGuid;
        this.retriesRequest = retriesRequest;
        this.packetsRequested = packetsRequested;
        this.packetsServiced = packetsServiced;
        this.maxHoleSize = maxHoleSize;
    }

    // comma-separated, for debug purposes only
    // TODO: string builder
    @Override
    public String toString() {
        Character comma = ',';
        return clientAddress + comma +
               clientGuid + comma +
               requestTime + comma +
               serviceGuid + comma +
               retriesRequest + comma +
               packetsRequested + comma +
               packetsServiced + comma +
               maxHoleSize;
    }
}

