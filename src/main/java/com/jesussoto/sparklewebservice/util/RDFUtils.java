package com.jesussoto.sparklewebservice.util;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.util.TextUtils;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class RDFUtils {

    private static long sResultFilenameCode = 1;

    public static String execSparqlQueryToString(Model model, String sparqlQuery) throws QueryException {
        Query query = QueryFactory.create(sparqlQuery);
        QueryExecution qexecution = QueryExecutionFactory.create(query, model);

        ResultSet results = qexecution.execSelect() ;

        /*for ( ; results.hasNext() ; )
          {
            QuerySolution soln = results.nextSolution() ;
            RDFNode x = soln.get("varName") ;       // Get a result variable by name.
            Resource r = soln.getResource("VarR") ; // Get a result variable - must be a resource
            Literal l = soln.getLiteral("VarL") ;   // Get a result variable - must be a literal
          }
        }*/

        // Output as RDF/XML.
        //CharSequence resultXmlString = ResultSetFormatter.asXMLString(results);

        // Output as RDF/JSON.
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ResultSetFormatter.outputAsJSON(b, results);
        String resultXmlString = b.toString( );

        return resultXmlString.toString();
    }

    public static String execSparelQueryDownload(Model model, String sparqlQuery) throws IOException {
        Query query = QueryFactory.create(sparqlQuery);
        QueryExecution qexecution = QueryExecutionFactory.create(query, model);
        ResultSet results = qexecution.execSelect();

        if (results.hasNext()) {
            String filename = "query_result_" + String.format("%3d", sResultFilenameCode++);
            FileWriter out = new FileWriter(filename);
            try {
                model.write( out, "RDF/XML-ABBREV" );
            } finally {
               try {
                   out.close();
               } catch (IOException closeException) {
                   // ignore
               }
            }

            return filename;
        } else {
            return null;
        }
    }

}
