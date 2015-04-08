package com.jesussoto.sparklewebservice.controller;

import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.jena.riot.RDFFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.rdf.model.Model;
import com.jesussoto.sparklewebservice.model.ErrorResponseBody;
import com.jesussoto.sparklewebservice.util.RDFUtils;

@Controller
public class SparkleController {

    public static final String PATH_QUERY = "/query";
    public static final String PATH_QUERY_DOWNLOAD = "/query_download";
    public static final String PATH_DUMP = "/dump";

    public static final String PARAM_FORMAT = "format";

    @Autowired
    private Model model;

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

    @RequestMapping(value=PATH_QUERY, method=RequestMethod.POST)
    public @ResponseBody String query(
            @RequestBody String sparql,
            @RequestParam(value=PARAM_FORMAT, required=false) Integer format,
            HttpServletResponse response) {

        System.out.println("format: " + format);
        //sparql = sparql.replace("\"", "");
        String result = null;

        try {
            result = RDFUtils.execSparqlQueryToString(model, sparql);
        } catch (QueryException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            //response.setContentType("application/json");

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

    @RequestMapping(value=PATH_QUERY_DOWNLOAD, method=RequestMethod.GET)
    public @ResponseBody MultipartFile queryDownload(
            @RequestBody String sparql,
            HttpServletResponse response) {

        /*String result = RDFUtils.execSparqlQueryToString(model, sparql);

        if (result == null) {
            response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            return null;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return result;
        }*/
        return null;
    }
}
