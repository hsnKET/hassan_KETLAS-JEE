package net.ketlas.di.xml;

import net.ketlas.di.exceptions.KETDIException;
import net.ketlas.di.xml.bean.Beans;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class XMLResource implements Resource {


    private File xsdFile;
    private File configFile;

    public XMLResource(String xsdFileName, String configFileName) {
        this.init(xsdFileName,configFileName);
    }
    public void init(String xsdFileName, String configFileName){
        try{
            File parentConfig = new File(getClass().getClassLoader().getResource("").toURI());
            this.xsdFile = new File(getClass().getClassLoader().getResource(xsdFileName).toURI());
            this.configFile = new File(parentConfig,configFileName);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void printFile(File file){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = br.readLine()) !=  null){
                System.out.println(line);
            }
        }catch (Exception e){

        }
    }
    @Override
    public Beans getResource() {
        Beans beans = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Beans.class);
            //Create Unmarshaller
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            //Setup schema validato
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema();
            jaxbUnmarshaller.setSchema(schema);
            //Unmarshal xml file
            beans = (Beans) jaxbUnmarshaller.unmarshal(this.configFile);
        } catch (JAXBException | SAXException e) {
            e.printStackTrace();
            throw  new KETDIException("Please Verify Your XML Config File "+configFile.getAbsolutePath());
        }
        return beans;
    }
}
