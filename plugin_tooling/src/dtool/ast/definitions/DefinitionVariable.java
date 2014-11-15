package dtool.ast.definitions;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.Iterator;

import melnorme.lang.tooling.ast.IASTVisitor;
import melnorme.lang.tooling.ast.util.ASTCodePrinter;
import melnorme.lang.tooling.ast_actual.ASTNode;
import melnorme.lang.tooling.ast_actual.ASTNodeTypes;
import melnorme.lang.tooling.bundles.IModuleResolver;
import melnorme.lang.tooling.engine.scoping.INonScopedContainer;
import melnorme.lang.tooling.symbols.INamedElement;
import melnorme.utilbox.collections.ArrayView;
import dtool.ast.declarations.IDeclaration;
import dtool.ast.expressions.IInitializer;
import dtool.ast.references.Reference;
import dtool.ast.statements.IStatement;
import dtool.engine.analysis.CommonDefVarSemantics;
import dtool.engine.analysis.IVarDefinitionLike;
import dtool.parser.common.Token;
import dtool.resolver.CommonDefUnitSearch;

/**
 * A variable definition. 
 * Optionally has multiple variables defined with the multi-identifier syntax.
 */
public class DefinitionVariable extends CommonDefinition 
	implements IDeclaration, IStatement, INonScopedContainer, IVarDefinitionLike { 
	
	public static final ArrayView<DefVarFragment> NO_FRAGMENTS = ArrayView.create(new DefVarFragment[0]);
	
	public final Reference type; // Can be null
	public final Reference cstyleSuffix;
	public final IInitializer initializer;
	protected final ArrayView<DefVarFragment> fragments;
	
	public DefinitionVariable(Token[] comments, ProtoDefSymbol defId, Reference type, Reference cstyleSuffix,
		IInitializer initializer, ArrayView<DefVarFragment> fragments)
	{
		super(comments, defId);
		this.type = parentize(type);
		this.cstyleSuffix = parentize(cstyleSuffix);
		this.initializer = parentize(initializer);
		this.fragments = parentize(fragments);
		assertTrue(fragments == null || fragments.size() > 0);
	}
	
	@Override
	public ASTNodeTypes getNodeType() {
		return ASTNodeTypes.DEFINITION_VARIABLE;
	}
	
	@Override
	public void visitChildren(IASTVisitor visitor) {
		acceptVisitor(visitor, type);
		acceptVisitor(visitor, defname);
		acceptVisitor(visitor, cstyleSuffix);
		acceptVisitor(visitor, initializer);
		
		acceptVisitor(visitor, fragments);
	}
	
	@Override
	public void toStringAsCode(ASTCodePrinter cp) {
		cp.append(type, " ");
		cp.append(defname);
		cp.append(cstyleSuffix);
		cp.append(" = ", initializer);
		cp.appendList(", ", fragments, ", ", "");
		cp.append(";");
	}
	
	@Override
	public EArcheType getArcheType() {
		return EArcheType.Variable;
	}
	
	public ArrayView<DefVarFragment> getFragments() {
		return fragments == null ? NO_FRAGMENTS : fragments;
	}
	
	@Override
	public Iterator<? extends ASTNode> getMembersIterator() {
		return getFragments().iterator();
	}
	
	@Override
	public Reference getDeclaredType() {
		return type;
	}
	
	@Override
	public IInitializer getDeclaredInitializer() {
		return initializer;
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public CommonDefVarSemantics getNodeSemantics() {
		return nodeSemantics;
	}
	
	protected final CommonDefVarSemantics nodeSemantics = new CommonDefVarSemantics(this);
	
	/* -----------------  ----------------- */
	
	public static class DefinitionAutoVariable extends DefinitionVariable {
		
		public DefinitionAutoVariable(Token[] comments, ProtoDefSymbol defId, IInitializer initializer,
			ArrayView<DefVarFragment> fragments) {
			super(comments, defId, null, null, initializer, fragments);
		}
		
		@Override
		public ASTNodeTypes getNodeType() {
			return ASTNodeTypes.DEFINITION_AUTO_VARIABLE;
		}
		
	}
	
}