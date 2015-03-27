package com.jesussoto.sparklewebservice.rdf;

import java.io.InputStream;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import com.jesussoto.sparklewebservice.util.Utils;

public class RDFModel {

    private static Model defaultModel;

    private static final String RDF_XML_MODEL_FILENAME = "model01.xml";
    private static final String RDF_XML_ONTOLOGY_FILENAME = "ontology01.owl";

    public static Model getInstance() {
        //Utils.listFilesInPath("src/main/resources/");
        if (defaultModel == null) {
            //configureDefaultModel();
            configureDefaultOntologyModel();
        }
        return defaultModel;
    }

    private static void configureDefaultModel() {
        defaultModel = ModelFactory.createDefaultModel();

        InputStream in = FileManager.get().
                open("src/main/resources/" + RDF_XML_MODEL_FILENAME);

       if (in == null) {
           throw new IllegalArgumentException(
                   "File: " + RDF_XML_MODEL_FILENAME + " not found");
       }

       // read the RDF/XML file
       defaultModel.read(in, null);
    }

    private static void configureDefaultOntologyModel() {
        OntModel model = ModelFactory.createOntologyModel();

        InputStream in = FileManager.get().
                open("src/main/resources/" + RDF_XML_ONTOLOGY_FILENAME);

       if (in == null) {
           throw new IllegalArgumentException(
                   "File: " + RDF_XML_MODEL_FILENAME + " not found");
       }

       // read the RDF/XML file
       model.read(in, null);
       defaultModel = model;
    }

    public static String execSparql(String sparql) {
        if (defaultModel == null) {
            return null;
        }

        return null;
    }
}
