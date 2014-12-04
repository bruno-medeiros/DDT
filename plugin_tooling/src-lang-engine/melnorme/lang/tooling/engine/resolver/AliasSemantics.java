/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.engine.resolver;

import melnorme.lang.tooling.context.ISemanticContext;
import melnorme.lang.tooling.engine.NotAValueErrorElement;
import melnorme.lang.tooling.engine.NotFoundErrorElement;
import melnorme.lang.tooling.engine.PickedElement;
import melnorme.lang.tooling.engine.scoping.CommonScopeLookup;
import melnorme.lang.tooling.symbols.IConcreteNamedElement;
import melnorme.lang.tooling.symbols.INamedElement;

public abstract class AliasSemantics extends NamedElementSemantics {
	
	public AliasSemantics(INamedElement element, PickedElement<?> pickedElement) {
		super(element, pickedElement);
	}
	
	protected INamedElement resolveAliasTarget_nonNull(ISemanticContext context) {
		INamedElement result = resolveAliasTarget(context);
		if(result == null) {
			return new NotFoundErrorElement(element, null);
		}
		return result;
	}
	
	protected abstract INamedElement resolveAliasTarget(ISemanticContext context);
	
	@Override
	protected IConcreteNamedElement doResolveConcreteElement(ISemanticContext context) {
		return resolveAliasTarget_nonNull(context).resolveConcreteElement(context);
	}
	
	@Override
	public void resolveSearchInMembersScope(CommonScopeLookup search) {
		resolveAliasTarget_nonNull(context).resolveSearchInMembersScope(search);
	}
	
	@Override
	public INamedElement resolveTypeForValueContext() {
		return resolveAliasTarget_nonNull(context).resolveTypeForValueContext(context);
	}
	
	/* -----------------  ----------------- */
	
	public abstract static class RefAliasSemantics extends AliasSemantics {

		public RefAliasSemantics(INamedElement element, PickedElement<?> pickedElement) {
			super(element, pickedElement);
		}
		
		@Override
		protected INamedElement resolveAliasTarget(ISemanticContext context) {
			return resolveAliasTarget(context, getAliasTarget());
		}
		
		protected abstract IResolvable getAliasTarget();
		
		protected static INamedElement resolveAliasTarget(ISemanticContext context, IResolvable aliasTarget) {
			if(aliasTarget == null) {
				return null;
			}
			return aliasTarget.getSemantics(context).resolveTargetElement().getSingleResult();
		}
		
	}
	
	public abstract static class TypeAliasSemantics extends RefAliasSemantics {
		
		public TypeAliasSemantics(INamedElement aliasDef, PickedElement<?> pickedElement) {
			super(aliasDef, pickedElement);
		}
		
		@Override
		public INamedElement resolveTypeForValueContext() {
			// TODO fix leak here, this element should be created only once per resolution.
			return new NotAValueErrorElement(element);
		};
		
	}
	
}