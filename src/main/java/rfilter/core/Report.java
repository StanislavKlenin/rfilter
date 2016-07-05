package rfilter.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// datatype to represent a report entry
// (similar to Scala case class but now mutable for simplicity)
public class Report {
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
    public static final String HEADER =
            "client-address,client-guid,request-time,service-guid,retries-request,packets-requested,packets-serviced,max-hole-size";

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

    // comma-separated, as in input
    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%d,%d,%d,%d",
                             clientAddress,
                             clientAddress,
                             DATE_FORMAT.format(requestTime),
                             serviceGuid,
                             retriesRequest,
                             packetsRequested,
                             packetsServiced,
                             maxHoleSize);
    }
}

