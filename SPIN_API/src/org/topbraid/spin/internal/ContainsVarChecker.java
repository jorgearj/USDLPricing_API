/*******************************************************************************
 * Copyright (c) 2009 TopQuadrant, Inc.
 * All rights reserved. 
 *******************************************************************************/
package org.topbraid.spin.internal;

import org.topbraid.spin.model.CommandWithWhere;
import org.topbraid.spin.model.Construct;
import org.topbraid.spin.model.Element;
import org.topbraid.spin.model.ElementList;
import org.topbraid.spin.model.NamedGraph;
import org.topbraid.spin.model.Triple;
import org.topbraid.spin.model.TriplePath;
import org.topbraid.spin.model.TriplePattern;
import org.topbraid.spin.model.TripleTemplate;
import org.topbraid.spin.model.Variable;
import org.topbraid.spin.model.update.Modify;
import org.topbraid.spin.model.visitor.AbstractElementVisitor;
import org.topbraid.spin.model.visitor.AbstractExpressionVisitor;
import org.topbraid.spin.model.visitor.ElementVisitor;
import org.topbraid.spin.model.visitor.ElementWalkerWithDepth;
import org.topbraid.spin.model.visitor.ExpressionVisitor;
import org.topbraid.spin.vocabulary.SP;

import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.RDF;


/**
 * Can be used to check whether a given SPIN Query contains a certain
 * variable, either in the WHERE clause or the CONSTRUCT template(s).
 * 
 * @author Holger Knublauch
 */
public class ContainsVarChecker {
	
	private Integer result;
	
	private Resource var;
	
	private ElementVisitor el = new AbstractElementVisitor() {

		@Override
		public void visit(TriplePath triplePath) {
			if(var.equals(triplePath.getObject()) ||
				var.equals(triplePath.getSubject())) {
				setResult();
			}
		}

		@Override
		public void visit(TriplePattern triplePattern) {
			if(containsVar(triplePattern)) {
				setResult();
			}
		}
	};
	
	private ExpressionVisitor ex = new AbstractExpressionVisitor() {

		@Override
		public void visit(Variable variable) {
			if(var.equals(variable)) {
				setResult();
			}
		}
	};
	
	private ElementWalkerWithDepth walker;
	
	
	public ContainsVarChecker(Resource var) {
		this.var = var;
	}
	

	/**
	 * Tries to find a usage of the variable and returns the maximum depth of the relevant
	 * element inside of the structure.
	 * @param command  the Command to start traversal with
	 * @return an Integer >= 0 or null if not found at all
	 */
	public Integer checkDepth(CommandWithWhere command) {

		if(command instanceof Construct) {
			// Check head of Construct
			for(TripleTemplate template : ((Construct)command).getTemplates()) {
				if(containsVar(template)) {
					result = 0;
				}
			}
		}
		else if(command instanceof Modify) {
			Modify modify = (Modify) command;
			if(templateContainsVar(modify.getProperty(SP.insertPattern))) {
				result = 0;
			}
			if(templateContainsVar(modify.getProperty(SP.deletePattern))) {
				result = 0;
			}
		}
		
		ElementList where = command.getWhere();
		if(where != null) {
			walker = new ElementWalkerWithDepth(el, ex);
			walker.visit(where);
		}
		
		return result;
	}


	private boolean containsVar(Triple triple) {
		return var.equals(triple.getObject()) ||
			var.equals(triple.getPredicate()) ||
			var.equals(triple.getSubject());
	}
	
	
	private void setResult() {
		if(result == null) {
			result = walker.getDepth();
		}
		else {
			result = Math.max(result, walker.getDepth());
		}
	}
	
	
	private boolean templateContainsVar(Statement listS) {
		if(listS != null && listS.getObject().isResource()) {
			ExtendedIterator<RDFNode> nodes = (listS.getObject().as(RDFList.class)).iterator();
			while(nodes.hasNext()) {
				Resource node = (Resource) nodes.next();
				if(node.hasProperty(RDF.type, SP.NamedGraph)) {
					NamedGraph namedGraph = node.as(NamedGraph.class);
					for(Element element : namedGraph.getElements()) {
						if(element instanceof Triple) {
							if(containsVar((Triple)element)) {
								nodes.close();
								return true;
							}
						}
					}
				}
				else {
					if(containsVar(node.as(TripleTemplate.class))) {
						nodes.close();
						return true;
					}
				}
			}
		}
		return false;
	}
}
