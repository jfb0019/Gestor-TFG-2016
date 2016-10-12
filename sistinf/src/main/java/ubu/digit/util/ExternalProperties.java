package ubu.digit.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.vaadin.server.VaadinService;

/**
 * Clase para la obtención de los valores de las propiedades.
 * 
 * @author Beatriz Zurera Martínez-Acitores.
 * @since 3.0
 */
public class ExternalProperties {

    /**
     * Logger de la clase.
     */
    private static final Logger LOGGER = Logger.getLogger(ExternalProperties.class);

    /**
     * Propiedad.
     */
    private static final Properties PROPERTIES = new Properties();

    /**
     * Fichero del que leeremos las propiedades.
     */
    private static String FILE;

    /**
     * Instancia que tendrá la propiedad.
     */
    private static ExternalProperties instance;

    /**
     * Dirección de los ficheros en la aplicación del servidor.
     */
	private static String basePath = "";

    /**
     * Constructor.
     */
    private ExternalProperties() {

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(FILE);
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            LOGGER.error("", e);
        }
    }

	/**
	 * Método singleton para obtener la instancia del fichero de propiedades.
	 * 
	 * @param propFileName
	 *            nombre del fichero
	 * @param testFlag
	 *            bandera para diferenciar los ficheros de propiedades de test y
	 *            ejecución (true: test, false: no test)
	 * @return
	 */
    public static ExternalProperties getInstance(String propFileName, Boolean testFlag) { 
    	if(!testFlag) {
    		basePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
    	}
    	
        if (instance == null) {
            FILE = basePath + propFileName;
            instance = new ExternalProperties();
        }
        return instance;
    }
    
    // Nomenclatura de la función original para que no de error de compilación
    // TODO Quitar cuando no se necesiten los fuentes de htmlgen y util
    public static ExternalProperties getInstance(String propFileName) {
    	return null;
    }
    
    /**
     * Método que obtiene el valor de la propiedad que se pasa.
     * 
     * @param key
     *            Propiedad de la cual queremos conocer el valor.
     * @return El valor de la propiedad.
     */
    public String getSetting(String key) {
        return PROPERTIES.getProperty(key).trim();
    }

}