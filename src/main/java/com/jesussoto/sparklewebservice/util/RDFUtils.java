package com.jesussoto.sparklewebservice.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.sparql.resultset.ResultsFormat;
import com.jesussoto.sparklewebservice.model.ResultMetadata;

public class RDFUtils {

    private static long sResultFilenameCode = 1;

    public static final String ENDPOINT_URL = "http://192.168.0.100:8080";
    //public static final String ENDPOINT_URL = "http://10.0.3.2:8080";
    //public static final String ENDPOINT_URL = "http://localhost:8080";

    public static final int FORMAT_RDF_XML = 0;
    public static final int FORMAT_RDF_XML_ABBREV = 1;
    public static final int FORMAT_RDF_JSON = 2;
    public static final int FORMAT_TURTLE = 3;
    public static final int FORMAT_NTRIPLES = 4;
    public static final int FORMAT_N3 = 5;
    public static final int FORMAT_PLAIN_TEXT = 6;
    public static final int FORMAT_TUPLES = 7;

    public static final String[] FORMATS = {
        "RDF/XML",
        "RDF/XML-ABBREV",
        "RDF/JSON",
        "TURTLE",
        "N-TRIPLES",
        "N3",
        "Plain Text"
    };

    public static final String[] EXTENSIONS = {
        ".xml",
        ".xml",
        ".json",
        ".ttl",
        ".nt",
        ".n3",
        ".txt"
    };

    public static String execSparqlQueryToString(Dataset dataset, String sparqlQuery, int format) throws QueryException {
        dataset.begin(ReadWrite.READ);

        Query query = QueryFactory.create(sparqlQuery);
        QueryExecution qexecution = QueryExecutionFactory.create(query, dataset);
        ResultSet results = qexecution.execSelect();
        String resultStr = formatResult(results, format, false);

        dataset.end();
        return resultStr;
    }

    public static ResultMetadata execSparqlQueryDownload(Dataset dataset, String sparqlQuery, int format) throws QueryException {
        //long beginTime = System.currentTimeMillis();
        //System.out.println("BEGIN QUERY PROCESS: " + System.currentTimeMillis());

        dataset.begin(ReadWrite.READ);

        Query query = QueryFactory.create(sparqlQuery);
        QueryExecution qexecution = QueryExecutionFactory.create(query, dataset);
        ResultSet results = qexecution.execSelect();

        /*int i;
        for (i = 0; results.hasNext(); ++i, results.nextSolution());
        System.out.println("CARDINALITY: " + i);
        System.out.println("QUERY PROCESS TIME: " + (System.currentTimeMillis() - beginTime));*/

        String extension = EXTENSIONS[(format >= 0 && format < EXTENSIONS.length) ? format : 0];
        String filename = formatResult(results, format, true, extension);
        File resultFile = new File("resources/results/" + filename);

        qexecution.close();
        dataset.end();

        return new ResultMetadata(
                System.currentTimeMillis(),
                filename,
                ENDPOINT_URL + "/download/" + filename,
                resultFile.length());
    }

    private static String formatResult(ResultSet results, int format, boolean saveToFile) {
        return formatResult(results, format, saveToFile, "");
    }

    private static String formatResult(ResultSet results, int format, boolean saveToFile, String extension) {
        ResultsFormat resultsFormat;

        switch (format) {
            case FORMAT_RDF_JSON:
                resultsFormat = ResultsFormat.FMT_RS_JSON;
                break;
            case FORMAT_TURTLE:
                resultsFormat = ResultsFormat.FMT_RDF_TURTLE;
                break;
            case FORMAT_NTRIPLES:
                resultsFormat = ResultsFormat.FMT_RDF_NT;
                break;
            case FORMAT_N3:
                resultsFormat = ResultsFormat.FMT_RDF_N3;
                break;
            case FORMAT_PLAIN_TEXT:
                resultsFormat = ResultsFormat.FMT_TEXT;
                break;
            default:
                resultsFormat = ResultsFormat.FMT_RS_XML;
        }

        return saveToFile
                ? writeToFile(results, resultsFormat, extension)
                : writeToString(results, resultsFormat);
    }

    private static String writeToString(ResultSet results, ResultsFormat format) {
        if (format.equals(ResultsFormat.FMT_TEXT)) {
            return ResultSetFormatter.asText(results);
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ResultSetFormatter.output(out, results, format);
            return out.toString();
        }
    }

    private static String writeToFile(ResultSet results, ResultsFormat format, String extension) {
        try {
            String filename = "query_result_" + String.format("%03d", sResultFilenameCode++) + extension;
            File resultFile = new File("resources/results/" + filename);

            if (format.equals(ResultsFormat.FMT_TEXT)) {
                FileUtils.writeStringToFile(resultFile, ResultSetFormatter.asText(results));
            } else {
                resultFile.createNewFile();
                FileOutputStream out = new FileOutputStream(resultFile);
                ResultSetFormatter.output(out, results, format);
                out.close();
            }
            return filename;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
