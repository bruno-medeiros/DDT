package dtool.ast;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.Iterator;
import java.util.List;

import melnorme.utilbox.misc.IteratorUtil;
import dtool.resolver.IScope;
import dtool.resolver.IScopeNode;
import dtool.resolver.api.IModuleResolver;
import dtool.util.ArrayView;

public abstract class NodeList<E extends ASTNode> extends ASTNode implements IScopeNode {
	
	public final ArrayView<E> nodes;
	
	protected NodeList(ArrayView<E> nodes) {
		this.nodes = parentize(assertNotNull(nodes));
	}
	
	@Override
	public void visitChildren(IASTVisitor visitor) {
		acceptVisitor(visitor, nodes);
	}
	
	@Override
	public void toStringAsCode(ASTCodePrinter cp) {
		cp.appendList(nodes, "\n", true);
	}
	
	@Override
	public Iterator<? extends ASTNode> getMembersIterator(IModuleResolver moduleResolver) {
		return nodes.iterator();
	}
	
	@Override
	public List<IScope> getSuperScopes(IModuleResolver moduleResolver) {
		return null;
	}
	
	@Override
	public boolean hasSequentialLookup() {
		return false;
	}
	
	public static Iterator<? extends ASTNode> getBodyMembersIterator(NodeList<? extends ASTNode> body) {
		return body == null ? IteratorUtil.<ASTNode>getEMPTY_ITERATOR() : body.nodes.iterator();
	}
	
}