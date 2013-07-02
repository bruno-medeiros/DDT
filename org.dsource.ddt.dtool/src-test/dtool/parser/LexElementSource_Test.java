package dtool.parser;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.junit.Test;

import dtool.ast.SourceRange;
import dtool.tests.DToolBaseTest;

public class LexElementSource_Test extends DToolBaseTest {
	
	protected final class TestsInstrumentedLexer extends AbstractLexer {
		private TestsInstrumentedLexer(String source) {
			super(source);
		}
		
		@Override
		public Token parseToken() {
			if(lookAheadIsEOF()) {
				return createToken(DeeTokens.EOF);
			}
			if(lookAhead() == ' ') {
				return createToken(DeeTokens.WHITESPACE, 1);
			}
			if(lookAhead() == 'X') {
				createToken(DeeTokens.IDENTIFIER, 1);
			}
			return createToken(DeeTokens.IDENTIFIER, 2);
		}
	}
	
	@Test
	public void testInit() throws Exception { testInit$(); }
	public void testInit$() throws Exception {
		LexElementSource lexSource = LexElementProducer.createFromLexer(new TestsInstrumentedLexer("abcdefgh"));
		
		assertEquals(lexSource.lastLexElement.getSourceRange(), new SourceRange(0, 0));
		
		assertTrue(lexSource.lexElementList.size() == 5);
		assertEquals(lexSource.lookAheadElement(4).type, DeeTokens.EOF);
		assertEquals(lexSource.lookAheadElement(5).type, DeeTokens.EOF); // Test index beyond first EOF
	}
	
	@Test
	public void testElementList() throws Exception { testElementList$(); }
	public void testElementList$() throws Exception {
		
		LexElementSource lexSource = LexElementProducer.createFromLexer(new TestsInstrumentedLexer("abcd  efgh"));
		assertTrue(lexSource.lexElementList.size() == 5);
		
		assertEquals(lexSource.lookAheadElement(0).source, "ab");
		assertEquals(lexSource.lookAheadElement(1).source, "cd");
		assertEquals(lexSource.lookAheadElement(3).source, "gh");
		assertEquals(lexSource.lookAheadElement(4).type, DeeTokens.EOF);
		lexSource.consumeInput();
		
		assertEquals(lexSource.lastLexElement().source, "ab");
		assertEquals(lexSource.lookAheadElement(0).source, "cd");
		assertEquals(lexSource.lookAheadElement(1).source, "ef");
		assertEquals(lexSource.lookAheadElement(1).getFullRangeStartPos(), 4);
		assertEquals(lexSource.lookAheadElement(1).getStartPos(), 6);
		assertEquals(lexSource.lookAheadElement(2).source, "gh");
		assertEquals(lexSource.lookAheadElement(3).type, DeeTokens.EOF);

		lexSource.consumeInput();
		assertEquals(lexSource.lastLexElement().source, "cd");
		assertEquals(lexSource.getSourcePosition(), 4);
		assertEquals(lexSource.lookAheadElement(0).source, "ef");
		assertEquals(lexSource.lookAheadElement(1).source, "gh");
		assertEquals(lexSource.lookAheadElement(2).type, DeeTokens.EOF);
		assertEquals(lexSource.lookAheadElement(3).type, DeeTokens.EOF); // Test index beyond first EOF
		
		lexSource.consumeInput();
		lexSource.consumeInput();
		assertEquals(lexSource.lastLexElement().source, "gh");
		assertEquals(lexSource.lookAheadElement(0).type, DeeTokens.EOF);
		assertEquals(lexSource.lookAheadElement(1).type, DeeTokens.EOF);
	}
	
	@Test
	public void testConsumeWhiteSpace() throws Exception { testConsumeWhiteSpace$(); }
	public void testConsumeWhiteSpace$() throws Exception {
		LexElementSource lexSource = LexElementProducer.createFromLexer(new TestsInstrumentedLexer("abcd  efgh")); 
		
		lexSource.advanceSubChannelTokens();
		assertTrue(lexSource.getSourcePosition() == 0);
		
		assertEquals(lexSource.lookAheadElement(2).source, "ef");
		lexSource.consumeInput();
		lexSource.consumeInput();
		assertEquals(lexSource.lookAheadElement(0).source, "ef");
		assertTrue(lexSource.getSourcePosition() == 4);
		lexSource.advanceSubChannelTokens();
		assertTrue(lexSource.getSourcePosition() == 6);
		
		assertEquals(lexSource.lookAheadElement(0).getStartPos(), 6);
		assertEquals(lexSource.lookAheadElement(0).source, "ef");
	}
	
}