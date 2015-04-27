package com.jesussoto.sparklewebservice.rdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.FileManager;
import com.jesussoto.sparklewebservice.util.Utils;

public class RDFModel {

    private static OntModel defaultModel;

    private static final String RDF_XML_MODEL_FILENAME = "model01.xml";
    private static final String RDF_XML_ONTOLOGY_FILENAME = "ontology.owl";

    private static final String[] LUBM_DATASETS = new String[] {
        "University1_0.nt",
        "University5_0.nt",
        "University10_0.nt",
        "University20_0.nt",
        "University50_0.nt"
    };

    private static final String[] DBPEDIA_DATASETS = new String[] {
        "geocoordinates-fixed.nt",
        "geo_coordinates_en.nt",
        "infoboxes-geocoordinates-homepages.nt"
    };


    public static OntModel getInstance() {
        //Utils.listFilesInPath("src/main/resources/");
        if (defaultModel == null) {
            //configureDefaultModel();
            //configureDefaultOntologyModel();
            configureLubmModel(4);
            //configureDBpediaModel(1);
        }
        return defaultModel;
    }

    private static void configureDefaultModel() {
        defaultModel = ModelFactory.createOntologyModel();

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
        defaultModel = ModelFactory.createOntologyModel();

        InputStream in = FileManager.get().
                open("src/main/resources/" + RDF_XML_ONTOLOGY_FILENAME);

       if (in == null) {
           throw new IllegalArgumentException(
                   "File: " + RDF_XML_MODEL_FILENAME + " not found");
       }

       // read the RDF/XML file
       defaultModel.read(in, null);
    }

    private static void configureLubmModel(int datasetNumber) {
        defaultModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);

        InputStream in = FileManager.get().
                open("resources/datasets/LUBM/" + LUBM_DATASETS[datasetNumber]);

       if (in == null) {
           throw new IllegalArgumentException(
                   "File: " + LUBM_DATASETS[datasetNumber] + " not found");
       }

       // read the RDF/XML file
       defaultModel.read(in, null, "N-TRIPLES");
    }

    private static void configureDBpediaModel(int datasetNumber) {
        defaultModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);

        InputStream in = FileManager.get().
                open("resources/datasets/DBpedia/" + DBPEDIA_DATASETS[datasetNumber]);

       if (in == null) {
           throw new IllegalArgumentException(
                   "File: " + DBPEDIA_DATASETS[datasetNumber] + " not found");
       }

       // read the RDF/XML file
       defaultModel.read(in, "http://default.uri.com", "N-TRIPLES");
    }

}
