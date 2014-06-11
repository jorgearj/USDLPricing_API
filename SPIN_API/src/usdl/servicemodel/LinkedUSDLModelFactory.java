 /*  ----------------------------------------------------------------------------------------
 *  This file is part of LinkedUSDLPricingAPI.
 *
 *  LinkedUSDLPricingAPI is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  LinkedUSDLPricingAPI is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with LinkedUSDLPricingAPI.  If not, see <http://www.gnu.org/licenses/>.
 *  ---------------------------------------------------------------------------------------
 */

package usdl.servicemodel;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import usdl.constants.properties.PricingAPIProperties;
import usdl.servicemodel.validations.LinkedUSDLValidator;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

import exceptions.ErrorEnum;
import exceptions.InvalidLinkedUSDLModelException;
import exceptions.ReadModelException;

/**
 * The LinkedUSDLModelFactory class is a factory for initializing a LinkedUSDLModel instance. 
 * @author  Daniel Barrigas
 * @author Jorge Araujo
 * @version 1.0, March 12
 */
public class LinkedUSDLModelFactory {
	
	protected LinkedUSDLModelFactory(){
		
	}
	
	/**
	 * Creates an empty LinkedUSDLModel instance. 
	 * @return  An initialized LinkedUSDLModel object.
	 */
	public static LinkedUSDLModel createEmptyModel(){
		return new LinkedUSDLModel(PricingAPIProperties.defaultBaseURI);
	}
	
	/**
	 * Creates an empty LinkedUSDLModel instance with a user defined baseURI. 
	 * @param   baseURI   the base URI to use in the model
	 * @return  An initialized LinkedUSDLModel object.
	 */
	public static LinkedUSDLModel createEmptyModel(String baseURI){
		return new LinkedUSDLModel(baseURI);
	}
	
	
	/**
	 * Creates a LinkedUSDLModel instance based on an already existing Linked USDL model. 
	 * The existing model is read from the file path that can either be to a single file (TTL or RDF) or to a folder containing any number of these file types.
	 * Note that all files with file extensions: ttl and rdf will be imported and validated for Linked USDL compliance.  
	 * @param   path   The file path from where to import the Linked USDL model
	 * @return  An initialized LinkedUSDLModel object already populated with all elements read.
	 * @throws InvalidLinkedUSDLModelException 
	 * @throws IOException 
	 * @throws ReadModelException 
	 */
	public static LinkedUSDLModel createFromModel(String path) throws InvalidLinkedUSDLModelException, IOException, ReadModelException{
		return LinkedUSDLModelFactory.createFromModel(path, PricingAPIProperties.defaultBaseURI, true);
	}
	
	/**
	 * Creates a LinkedUSDLModel instance based on an already existing Linked USDL model. 
	 * The existing model is read from the file path that can either be to a single file (TTL or RDF) or to a folder containing any number of these file types.
	 * Note that all files with file extensions: ttl and rdf will be imported. The imported model validation or not depends on the parameter validation.  
	 * @param   path   The file path from where to import the Linked USDL model
	 * @param   validation  enable/disable linked USDL model validation
	 * @return  An initialized LinkedUSDLModel object already populated with all elements read.
	 * @throws InvalidLinkedUSDLModelException 
	 * @throws IOException 
	 * @throws ReadModelException 
	 */
	public static LinkedUSDLModel createFromModel(String path, boolean validation) throws InvalidLinkedUSDLModelException, IOException, ReadModelException{
		return LinkedUSDLModelFactory.createFromModel(path, PricingAPIProperties.defaultBaseURI, true);
	}
	
	/**
	 * Creates a LinkedUSDLModel instance based on an already existing Linked USDL model. 
	 * The existing model is read from the file path that can either be to a single file (TTL or RDF) or to a folder containing any number of these file types.
	 * Note that all files with file extensions: ttl and rdf will be imported and validated as a whole for Linked USDL compliance.  
	 * @param   path   The file path from where to import the Linked USDL model
	 * @param   baseURI   the base URI to use in the model
	 * @param   validation  enable/disable linked USDL model validation
	 * @return  An initialized LinkedUSDLModel object already populated with all elements read.
	 * @throws InvalidLinkedUSDLModelException 
	 * @throws IOException 
	 * @throws ReadModelException 
	 */
	public static LinkedUSDLModel createFromModel(String path, String baseURI, boolean validation) throws InvalidLinkedUSDLModelException, IOException, ReadModelException{
		
	
		Model model = new LinkedUSDLModelFactory().importModel(path);
		//checks of the imported model already has a baseURI to use.
	    String uri = model.getNsPrefixURI("");
		if(uri != null){
			System.out.println("HAS BASE URI " + uri);
			baseURI = uri;
		}
		
		LinkedUSDLModel linkedUSDL = new LinkedUSDLModel(baseURI);
//		System.out.println("BASE URI: " + baseURI);
		
		
		//TESTS		
//		System.out.println("Prefixes after importing model");
//		Iterator it = model.getNsPrefixMap().entrySet().iterator();
//	    while (it.hasNext()) {
//	        Map.Entry pairs = (Map.Entry)it.next();
//	        System.out.println("        - "+ pairs.getKey() + " = " + pairs.getValue());
//	    }
		
		
		linkedUSDL.readModel(model);
		if(validation){
			//validates the model against the Linked USDL specification
			LinkedUSDLValidator validator = new LinkedUSDLValidator();
			validator.validateModel(model);
		}
		return linkedUSDL;
	}
	
	
	private Model importModel(String path) throws IOException, ReadModelException{
		Model model = ModelFactory.createDefaultModel();
//		this.setPrefixes(model);
		
		ArrayList<String> fileNames = new ArrayList<String>();
		Map<String, String> prefixes = new HashMap<String, String>(); //inverted map of prefixes KEY = URI, VALUE = name
		
		fileNames = this.getFileNames(path);
		System.out.println(fileNames);
		
		for(String file : fileNames){
			System.out.println("FILE: "+file);
			Model temp;
			//System.out.println(test.getFileExtension(file));
			String ext = this.getFileExtension(file);
			if(ext.equalsIgnoreCase("ttl")){
				temp = this.readFile(file, "TTL" );
				if(temp != null){
					prefixes = this.processPrefixes(temp, prefixes);
//					this.addPrefix(temp, model);
					model.add(temp);
				}
			}else if(ext.equalsIgnoreCase("rdf")){
				temp = this.readFile(file, "RDF/XML" );
				if(temp != null){
					prefixes = this.processPrefixes(temp, prefixes);
//					this.addPrefix(temp, model);
					model.add(temp);
				}
			}
		}
		
		model = this.setPrefixes(model, prefixes);
		
		return model;
	}
	
	private Model readFile(String file, String lang) throws ReadModelException{
		// create an empty model
		Model model = ModelFactory.createDefaultModel();

		// use the FileManager to find the input file
		InputStream in = FileManager.get().open( file );
		if (in == null) {
			throw new ReadModelException(ErrorEnum.FILE_NOT_FOUND.getMessage());
		}
		// read the RDF/XML file
		model.read(in, "", lang);
		
		return model;
	}
	
	private ArrayList<String> getFileNames(String path) throws IOException{
		final ArrayList<String> fileNames = new ArrayList<String>();
		
	    Path startPath = Paths.get(path);
	    Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
	        @Override
	        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
//	            System.out.println("Dir: " + dir.toString());
	            return FileVisitResult.CONTINUE;
	        }

	        @Override
	        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
//	            System.out.println("File: " + file.toString());
	            if (file.getFileName().toString().endsWith(".ttl") || file.getFileName().toString().endsWith(".rdf")){
	            	fileNames.add(file.toString());
	            }		            
	            return FileVisitResult.CONTINUE;
	        }

	        @Override
	        public FileVisitResult visitFileFailed(Path file, IOException e) {
	            return FileVisitResult.CONTINUE;
	        }
	    });
		
		return fileNames;
	}
	
	private String getFileExtension(String file){
		String ext = null;
	    int i = file.lastIndexOf('.');

	    if (i > 0 &&  i < file.length() - 1) {
	        ext = file.substring(i+1).toLowerCase();
	    }
	    return ext;
	}
	
	private String getModelName(Model model){
		String prefix = model.getNsPrefixURI("");
		String[] tokens = prefix.split("/");
		String name = tokens[tokens.length-1];
		name = name.replace("#", "");
//		System.out.println(name);
		
		return name;
	}
	
	private Map<String, String> processPrefixes(Model model, Map<String, String> prefixes) throws ReadModelException{
		Map<String, String> result = prefixes;
		
		
		try {
			String name = this.getModelName(model);
			Iterator<Entry<String,String>> it = model.getNsPrefixMap().entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<String,String> pairs = (Map.Entry<String,String>)it.next();
		        String key = (String)pairs.getKey();
		        if(key.equalsIgnoreCase(""))
		        	key = name;

		        
		        result.put((String)pairs.getValue(), key);
		        
		    }
		} catch (NullPointerException e) {
			throw new ReadModelException(ErrorEnum.NO_BASE_URI.getMessage(), e);
		}
		
		return result;
	}
	
	private Model setPrefixes(Model model, Map<String, String> prefixes){
		
		Iterator<Entry<String,String>> it = prefixes.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,String> pairs = (Map.Entry<String,String>)it.next();
	        String key = (String)pairs.getKey(); //URI
	        String value = (String)pairs.getValue(); //preffix name
	        model.setNsPrefix(value, key);
	    }
		
		return model;
	}

}
