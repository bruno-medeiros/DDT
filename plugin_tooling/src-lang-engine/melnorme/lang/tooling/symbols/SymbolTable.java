/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.symbols;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import melnorme.lang.tooling.ast.ILanguageElement;
import melnorme.lang.tooling.engine.OverloadedNamedElement;
import dtool.engine.analysis.ModuleProxy;
import dtool.engine.analysis.PackageNamespace;
import dtool.engine.analysis.PackageNamespaceFragment;

/**
 * A symbol table (map of named elements).
 * 
 * Automatically handles merging of package namespaces.
 * 
 */
public class SymbolTable {
	
	protected final HashMap<String, INamedElement> map = new HashMap<>();
	
	public HashMap<String,INamedElement> getMap() {
		return map;
	}
	
	public Set<Entry<String, INamedElement>> getEntries() {
		return map.entrySet();
	}
	
	public Collection<INamedElement> getElements() {
		return map.values();
	}
	
	public void addSymbols(SymbolTable importedNames) {
		addSymbols(importedNames.map.values());
	}
	
	public void addSymbols(Collection<INamedElement> values) {
		for (INamedElement namedElement : values) {
			addSymbol(namedElement);
		}
	}
	
	public void addSymbol(INamedElement newElement) {
		newElement = convertNameSpace(newElement);
		
		doAddSymbol(newElement);
	}
	
	protected static INamedElement convertNameSpace(INamedElement newElement) {
		if(newElement instanceof PackageNamespaceFragment) {
			PackageNamespaceFragment newNamespace = (PackageNamespaceFragment) newElement;
			// convert to PackageNamespace
			String fqn = newNamespace.getFullyQualifiedName();
			ILanguageElement parent = newNamespace.getParent();
			INamedElement containedElement = convertNameSpace(newNamespace.getContainedElement());
			return new PackageNamespace(fqn, parent, containedElement);
		}
		return newElement;
	}
	
	protected void doAddSymbol(INamedElement newElement) {
		String name = newElement.getNameInRegularNamespace();
		
		INamedElement existingNamedElement = map.get(name);
		
		if(existingNamedElement instanceof PackageNamespace && newElement instanceof PackageNamespace) {
			PackageNamespace existingNamespace = (PackageNamespace) existingNamedElement;
			PackageNamespace newNamespace = (PackageNamespace) newElement;
			existingNamespace.getNamespace().addSymbols(newNamespace.getContainedElements());
		} else {
			addEntryToMap(name, newElement);
		}
	}
	
	protected void addEntryToMap(String name, INamedElement newElement) {
		INamedElement existingEntry = map.get(name);
		if(existingEntry == null) {
			map.put(name, newElement);
		} else {
			if(existingEntry instanceof ModuleProxy && newElement instanceof ModuleProxy) {
				assertTrue(existingEntry.getFullyQualifiedName().equals(newElement.getFullyQualifiedName()));
				// Don't add duplicated element.
				return;
			}
			
			OverloadedNamedElement overloadElement;
			
			if(existingEntry instanceof OverloadedNamedElement) {
				overloadElement = (OverloadedNamedElement) existingEntry;
			} else {
				// Give priority to ModuleProxy element (note: this isn't entirely like DMD behavior
				if(newElement instanceof ModuleProxy && existingEntry instanceof PackageNamespace) {
					map.put(name, newElement);
					return;
				}
				if(newElement instanceof PackageNamespace && existingEntry instanceof ModuleProxy) {
					return;
				}
				
				overloadElement = new OverloadedNamedElement(existingEntry, existingEntry.getParent());
				map.put(name, overloadElement);
			}
			overloadElement.addElement(newElement);
		}
	}
	
	public HashMap<String,INamedElement> addVisibleSymbols(SymbolTable importedNames) {
		
		for (Entry<String, INamedElement> nameEntry : importedNames.getEntries()) {
			String matchedName = nameEntry.getKey();
			INamedElement matchedElement = nameEntry.getValue();
			
			INamedElement existingSymbol = map.get(matchedName);
			if(existingSymbol == null || 
					(existingSymbol instanceof PackageNamespace && matchedElement instanceof PackageNamespace)) {
				
				doAddSymbol(matchedElement);
			}
		}
		return map;
		
	}
	
}