package com.jesussoto.sparklewebservice.controller;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.jena.riot.RDFFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.rdf.model.Model;
import com.jesussoto.sparklewebservice.model.ErrorResponseBody;
import com.jesussoto.sparklewebservice.model.ResultMetadata;
import com.jesussoto.sparklewebservice.util.RDFUtils;

@Controller
public class SparkleController {

    public static final String PATH_QUERY = "/query";
    public static final String PATH_QUERY_DOWNLOAD = "/query_download";
    public static final String PATH_DOWNLOAD = "/download";
    public static final String PATH_DUMP = "/dump";

    public static final String PARAM_FORMAT = "format";

    @Autowired
    private OntModel model;

    @RequestMapping(value=PATH_DUMP, method=RequestMethod.GET)
    public @ResponseBody String dumpModel(HttpServletResponse response) {
        String format = RDFFormat.RDFXML_PLAIN.toString();
        System.out.println("Format: " + format);
        StringWriter out = new StringWriter();
        //model.write(out, format);
        model.write(out);
        String result = out.toString();

        return result;
    }

    @RequestMapping(value=PATH_QUERY, method=RequestMethod.POST, produces = "text/plain; charset=UTF-8")
    public @ResponseBody String query(
            @RequestBody String sparql,
            @RequestParam(value=PARAM_FORMAT, required=false) Integer format,
            HttpServletResponse response) {

        if (format != null && format >= 0 && format < RDFUtils.FORMATS.length) {
            System.out.println("format: " + RDFUtils.FORMATS[format]);
        } else {
            System.out.println("format: Unknown");
        }

        String result = null;

        try {
            result = RDFUtils.execSparqlQueryToString(model, sparql,
                    format == null ? 0 : format.intValue());
        } catch (QueryException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            ErrorResponseBody errorResponse = new ErrorResponseBody(
                    System.currentTimeMillis(),
                    "Bad Request",
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getClass().getCanonicalName(),
                    e.getMessage());

            try {
                System.out.println(sparql);
                return (new ObjectMapper()).writeValueAsString(errorResponse);
            } catch (JsonProcessingException e1) {
                e1.printStackTrace();
            }
        }

        System.out.println(sparql);

        response.setStatus(HttpServletResponse.SC_OK);
        return result;
    }

    @RequestMapping(value=PATH_QUERY_DOWNLOAD, method=RequestMethod.POST, produces = "text/plain; charset=UTF-8")
    public @ResponseBody String queryDownload(
            @RequestBody String sparql,
            @RequestParam(value=PARAM_FORMAT, required=false) Integer format,
            HttpServletResponse response) {

        System.out.println("Request: queryDownload()");
        System.out.println("REQUEST RECEIVED TIME: " + System.currentTimeMillis());
        System.out.println("sparql: " + sparql);

        if (format != null && format >= 0 && format < RDFUtils.FORMATS.length) {
            System.out.println("format: " + RDFUtils.FORMATS[format]);
        } else {
            System.out.println("format: Unknown (RDF/XML by default)");
        }

        String result = null;

        try {
            ResultMetadata metadata = RDFUtils.execSparqlQueryDownload(model, sparql,
                    format == null ? 0 : format.intValue());
            try {
                return (new ObjectMapper()).writeValueAsString(metadata);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } catch (QueryException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            ErrorResponseBody errorResponse = new ErrorResponseBody(
                    System.currentTimeMillis(),
                    "Bad Request",
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getClass().getCanonicalName(),
                    e.getMessage());

            try {
                System.out.println(sparql);
                return (new ObjectMapper()).writeValueAsString(errorResponse);
            } catch (JsonProcessingException e1) {
                e1.printStackTrace();
            }
        }

        System.out.println(sparql);
        return null;
    }

    @RequestMapping(value=PATH_DOWNLOAD + "/{filename.+}", method=RequestMethod.GET)
    public void getFile(
            @PathVariable("filename.+") String filename, 
            HttpServletResponse response) {
        try {
            System.out.println("Request: getFile()");
            System.out.println("file to download: " + filename);

            File file = new File("resources/results/" + filename);

            if (file.exists() && !file.isDirectory()) {
                response.setContentLength((int) file.length());
                IOUtils.copy(FileUtils.openInputStream(file), response.getOutputStream());
                response.flushBuffer();
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IOException ex) {
            System.out.println("Error writing file to output stream. Filename was '{" + filename + "}'");
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}
