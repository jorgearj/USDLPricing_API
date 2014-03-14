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
import java.util.Iterator;
import java.util.Map;

import usdl.servicemodel.validations.LinkedUSDLValidator;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

import exceptions.ErrorMessagesEnum;
import exceptions.InvalidLinkedUSDLModelException;
import exceptions.ReadModelException;

/**
 * The LinkedUSDLModelFactory class is a factory for initializing a LinkedUSDLModel instance. 
 * @author  Daniel Barrigas
 * @author Jorge Araujo
 * @version 1.0, March 12
 */
public class LinkedUSDLModelFactory {
	
	/**
	 * Creates an empty LinkedUSDLModel instance. 
	 * @return  An initialized LinkedUSDLModel object.
	 */
	public static LinkedUSDLModel createEmptyModel(){
		return new LinkedUSDLModel();
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
		LinkedUSDLModel linkedUSDL = new LinkedUSDLModel();
		Model model = new LinkedUSDLModelFactory().importModel(path);
		//validates the model against the Linked USDL specification
		LinkedUSDLValidator.validateModel(model);
		linkedUSDL.readModel(model);
		return linkedUSDL;
	}
	
	
	private Model importModel(String path) throws IOException, ReadModelException{
		Model model = ModelFactory.createDefaultModel();
		
		ArrayList<String> fileNames = new ArrayList<String>();
		
		fileNames = this.getFileNames(path);
		System.out.println(fileNames);
		
		for(String file : fileNames){
			Model temp;
			//System.out.println(test.getFileExtension(file));
			String ext = this.getFileExtension(file);
			if(ext.equalsIgnoreCase("ttl")){
				temp = this.readFile(file, "TTL" );
				if(temp != null){
					this.addPrefix(temp);
					model.add(temp);
				}
			}else if(ext.equalsIgnoreCase("rdf")){
				temp = this.readFile(file, "RDF/XML" );
				if(temp != null){
					this.addPrefix(temp);
					model.add(temp);
				}
			}
		}
		
		return model;
	}
	
	private Model readFile(String file, String lang){
		// create an empty model
		Model model = ModelFactory.createDefaultModel();

		// use the FileManager to find the input file
		InputStream in = FileManager.get().open( file );
		if (in == null) {
		    throw new IllegalArgumentException("ERROR: File: " + file + " not found");
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
	
	private Model addPrefix(Model service) throws ReadModelException{
		
		try {
			String name = this.getModelName(service);
			service.setNsPrefix(name, service.getNsPrefixURI(""));
		} catch (NullPointerException e) {
			//e.printStackTrace();
			throw new ReadModelException(ErrorMessagesEnum.NO_BASE_URI.getMessage(), e);
		}
		
		return service;
	}
	
	
	private void printPrefixes(Model model){
		Iterator it = model.getNsPrefixMap().entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        System.out.println("        - "+ pairs.getKey() + " = " + pairs.getValue());
	    }
	}
	
	private String getModelName(Model model){
		String prefix = model.getNsPrefixURI("");
		String[] tokens = prefix.split("/");
		String name = tokens[tokens.length-1];
		name = name.replace("#", "");
//		System.out.println(name);
		
		return name;
	}

}
