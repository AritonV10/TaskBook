package taskbook.v1.platform.utility;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

/**
 * 
 * @author vio
 * Singleton utility class that contains methods to work with files
 */
@ApplicationScoped
public class Files {
	
	public Map<Object, Object> getProperties(final String fileName) throws FileNotFoundException, IOException {
		try(InputStream inputStream = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(fileName))) {
			Properties propertyFile = new Properties();
			propertyFile.load(inputStream);
			return propertyFile
						.entrySet()
						.stream()
						.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
			
		} 
	} 
}
