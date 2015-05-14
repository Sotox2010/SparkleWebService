package com.jesussoto.sparklewebservice.rdf;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.tdb.TDBFactory;

public class RDFModel {

    private static Dataset defaultDataset;

    private static final String RDF_XML_MODEL_FILENAME = "model01.xml";
    private static final String RDF_XML_ONTOLOGY_FILENAME = "ontology.owl";

    private static final String[] LUBM_DATASETS = new String[] {
        "TDB_Univ1",
        "TDB_Univ5",
        "TDB_Univ10",
        "TDB_Univ20",
        "TDB_Univ50"
    };

    private static final String[] DBPEDIA_DATASETS = new String[] {
        "TDB_Geocoordinates",
        "TDB_InfoGeoHome"
    };

    public static Dataset getInstance() {
        if (defaultDataset == null) {
            //configureDefaultOntologyModel();
            setUpLubmDataset(0);
            //setUpDBpediaDataset(1);
        }
        return defaultDataset;
    }

    /*private static void configureDefaultModel() {
        defaultDataset = ModelFactory.createOntologyModel();

        InputStream in = FileManager.get().
                open("src/main/resources/" + RDF_XML_MODEL_FILENAME);

       if (in == null) {
           throw new IllegalArgumentException(
                   "File: " + RDF_XML_MODEL_FILENAME + " not found");
       }

       // read the RDF/XML file
       defaultModel.read(in, null);
    }*/

    private static void setUpLubmDataset(int datasetNumber) {
        String tdbDirectoryPath = "resources/datasets/LUBM/" + LUBM_DATASETS[datasetNumber];
        defaultDataset =  TDBFactory.createDataset(tdbDirectoryPath);
    }

    private static void setUpDBpediaDataset(int datasetNumber) {
        String tdbDirectoryPath = "resources/datasets/DBpedia/" + DBPEDIA_DATASETS[datasetNumber];
        defaultDataset =  TDBFactory.createDataset(tdbDirectoryPath);
    }


}
