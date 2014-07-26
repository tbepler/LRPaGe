package language.compiler.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import language.compiler.grammar.Grammar;
import language.compiler.grammar.Production;
import language.compiler.parser.Action;
import language.compiler.parser.Actions;
import language.compiler.parser.Item;
import language.compiler.parser.Parser;
import language.compiler.parser.State;

public class TestParser extends junit.framework.TestCase{
	
	private static final Collection<String> TERMINATORS = new HashSet<String>(Arrays.asList(
			"a",
			"c",
			"d"
			));
	
	private static enum Rules implements Production<String>{
		
		Z_d("Z", "d"),
		Z_XYZ("Z", "X", "Y", "Z"),
		Y_("Y"),
		Y_c("Y", "c"),
		X_Y("X", "Y"),
		X_a("X", "a");
		
		private final String lhs;
		private final String[] rhs;
		
		private Rules(String lhs, String ... rhs){
			this.lhs = lhs;
			this.rhs = rhs;
		}
		
		@Override
		public String leftHandSide() {
			return lhs;
		}

		@Override
		public String[] rightHandSide() {
			return rhs.clone();
		}
		
	}
	
	private static class SimpleGrammar implements Grammar<String>{

		@Override
		public Collection<String> getTerminalSymbols() {
			return TERMINATORS;
		}

		@Override
		public Collection<Production<String>> getProductions(String leftHandSide) {
			Collection<Production<String>> ps = new ArrayList<Production<String>>();
			for(Production<String> p : Rules.values()){
				if(p.leftHandSide() == leftHandSide){
					ps.add(p);
				}
			}
			return ps;
		}

		@Override
		public Collection<Production<String>> getAllProductions() {
			return Arrays.<Production<String>>asList(Rules.values());
		}

		@Override
		public Iterator<Production<String>> iterator() {
			return Arrays.<Production<String>>asList(Rules.values()).iterator();
		}

		@Override
		public boolean isTerminal(String symbol) {
			return symbol.equals("$") || TERMINATORS.contains(symbol);
		}

		@Override
		public Production<String> getStartProduction() {
			//return Rules.Z_XYZ;
			return new Production<String>(){

				@Override
				public String leftHandSide() {
					return "Z'";
				}

				@Override
				public String[] rightHandSide() {
					return new String[]{"Z", "$"};
				}
				
			};
		}

		@Override
		public boolean isEOFSymbol(String symbol) {
			return symbol.equals("$");
		}
		
	}
	
	public void testFirstFollowNullable(){
		Parser<String> parser = new Parser<String>(new SimpleGrammar());
		
		//test nullable
		assertTrue(parser.isNullable("X"));
		assertTrue(parser.isNullable("Y"));
		assertFalse(parser.isNullable("Z"));
		assertFalse(parser.isNullable("a"));
		assertFalse(parser.isNullable("c"));
		assertFalse(parser.isNullable("d"));
		
		//test first sets
		Set<String> xfirst = parser.getFirst("X");
		assertEquals(2, xfirst.size());
		assertTrue(xfirst.contains("a"));
		assertTrue(xfirst.contains("c"));
		
		Set<String> yfirst = parser.getFirst("Y");
		assertEquals(1, yfirst.size());
		assertTrue(yfirst.contains("c"));
		
		Set<String> zfirst = parser.getFirst("Z");
		assertEquals(3, zfirst.size());
		assertTrue(zfirst.contains("a"));
		assertTrue(zfirst.contains("c"));
		assertTrue(zfirst.contains("d"));
		
		Set<String> afirst = parser.getFirst("a");
		assertEquals(1, afirst.size());
		assertTrue(afirst.contains("a"));
		
		Set<String> cfirst = parser.getFirst("c");
		assertEquals(1, cfirst.size());
		assertTrue(cfirst.contains("c"));
		
		Set<String> dfirst = parser.getFirst("d");
		assertEquals(1, dfirst.size());
		assertTrue(dfirst.contains("d"));
		
		//test follow sets
		Set<String> xfollow = parser.getFollow("X");
		assertEquals(3, xfollow.size());
		assertTrue(xfollow.contains("a"));
		assertTrue(xfollow.contains("c"));
		assertTrue(xfollow.contains("d"));
		
		Set<String> yfollow = parser.getFollow("Y");
		assertEquals(3, yfollow.size());
		assertTrue(yfollow.contains("a"));
		assertTrue(yfollow.contains("c"));
		assertTrue(yfollow.contains("d"));
		
		Set<String> zfollow = parser.getFollow("Z");
		assertTrue(zfollow.isEmpty());
		
		Set<String> afollow = parser.getFollow("a");
		assertTrue(afollow.isEmpty());
		
		Set<String> cfollow = parser.getFollow("c");
		assertTrue(cfollow.isEmpty());
		
		Set<String> dfollow = parser.getFollow("d");
		assertTrue(dfollow.isEmpty());
	}
	
	private static final String EOF = "$";
	
	private static final Collection<String> TERMINAL = new HashSet<String>(Arrays.asList(
			"x",
			"=",
			"*",
			EOF
			));
	
	private static final String AUXIL_START = "S'";
	private static final Production<String> AUXIL_START_PRODUCTION = new Production<String>(){

		@Override
		public String leftHandSide() {
			return AUXIL_START;
		}

		@Override
		public String[] rightHandSide() {
			return new String[]{"S", EOF};
		}
		
	};
	
	private static enum Rules2 implements Production<String>{
		
		S_VaE("S", "V", "=", "E"),
		S_E("S", "E"),
		E_V("E", "V"),
		V_x("V", "x"),
		V_sE("V", "*", "E");
		
		private final String lhs;
		private final String[] rhs;
		
		private Rules2(String lhs, String ... rhs){
			this.lhs = lhs;
			this.rhs = rhs;
		}
		
		@Override
		public String leftHandSide() {
			return lhs;
		}

		@Override
		public String[] rightHandSide() {
			return rhs.clone();
		}
		
	}
	
	private static final class Grammar2 implements Grammar<String>{

		@Override
		public Iterator<Production<String>> iterator() {
			return Arrays.<Production<String>>asList(Rules2.values()).iterator();
		}

		@Override
		public Collection<String> getTerminalSymbols() {
			return TERMINAL;
		}

		@Override
		public boolean isTerminal(String symbol) {
			return TERMINAL.contains(symbol);
		}

		@Override
		public Collection<Production<String>> getProductions(String leftHandSide) {
			Collection<Production<String>> list = new ArrayList<Production<String>>();
			for(Production<String> prod : this){
				if(prod.leftHandSide().equals(leftHandSide)){
					list.add(prod);
				}
			}
			return list;
		}

		@Override
		public Collection<Production<String>> getAllProductions() {
			return Arrays.<Production<String>>asList(Rules2.values());
		}

		@Override
		public Production<String> getStartProduction() {
			return AUXIL_START_PRODUCTION;
		}

		@Override
		public boolean isEOFSymbol(String symbol) {
			return EOF.equals(symbol);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private static final State<String> STATE1 = new State<String>(new HashSet<Item<String>>(Arrays.asList(
			new Item<String>(AUXIL_START_PRODUCTION, new ArrayList<String>()),
			new Item<String>(Rules2.S_VaE, Arrays.asList(EOF)),
			new Item<String>(Rules2.S_E, Arrays.asList(EOF)),
			new Item<String>(Rules2.E_V, Arrays.asList(EOF)),
			new Item<String>(Rules2.V_x, Arrays.asList(EOF)),
			new Item<String>(Rules2.V_x, Arrays.asList("=")),
			new Item<String>(Rules2.V_sE, Arrays.asList(EOF)),
			new Item<String>(Rules2.V_sE, Arrays.asList("="))
			)));
	
	@SuppressWarnings("unchecked")
	private static final State<String> STATE2 = new State<String>(new HashSet<Item<String>>(Arrays.asList(
			new Item<String>(1,AUXIL_START_PRODUCTION, new ArrayList<String>())
			)));
	
	@SuppressWarnings("unchecked")
	private static final State<String> STATE3 = new State<String>(new HashSet<Item<String>>(Arrays.asList(
			new Item<String>(1, Rules2.S_VaE, Arrays.asList(EOF)),
			new Item<String>(1, Rules2.E_V, Arrays.asList(EOF))
			)));
	
	@SuppressWarnings("unchecked")
	private static final State<String> STATE4 = new State<String>(new HashSet<Item<String>>(Arrays.asList(
			new Item<String>(2, Rules2.S_VaE, Arrays.asList(EOF)),
			new Item<String>(Rules2.E_V, Arrays.asList(EOF)),
			new Item<String>(Rules2.V_x, Arrays.asList(EOF)),
			new Item<String>(Rules2.V_sE, Arrays.asList(EOF))
			)));
	
	@SuppressWarnings("unchecked")
	private static final State<String> STATE5 = new State<String>(new HashSet<Item<String>>(Arrays.asList(
			new Item<String>(1, Rules2.S_E, Arrays.asList(EOF))
			)));
	
	@SuppressWarnings("unchecked")
	private static final State<String> STATE6 = new State<String>(new HashSet<Item<String>>(Arrays.asList(
			new Item<String>(1, Rules2.V_sE, Arrays.asList(EOF)),
			new Item<String>(1, Rules2.V_sE, Arrays.asList("=")),
			new Item<String>(Rules2.E_V, Arrays.asList(EOF)),
			new Item<String>(Rules2.E_V, Arrays.asList("=")),
			new Item<String>(Rules2.V_x, Arrays.asList(EOF)),
			new Item<String>(Rules2.V_x, Arrays.asList("=")),
			new Item<String>(Rules2.V_sE, Arrays.asList(EOF)),
			new Item<String>(Rules2.V_sE, Arrays.asList("="))
			)));
	
	@SuppressWarnings("unchecked")
	private static final State<String> STATE7 = new State<String>(new HashSet<Item<String>>(Arrays.asList(
			new Item<String>(1, Rules2.E_V, Arrays.asList(EOF))
			)));
	
	@SuppressWarnings("unchecked")
	private static final State<String> STATE8 = new State<String>(new HashSet<Item<String>>(Arrays.asList(
			new Item<String>(1, Rules2.V_x, Arrays.asList(EOF)),
			new Item<String>(1, Rules2.V_x, Arrays.asList("="))
			)));
	
	@SuppressWarnings("unchecked")
	private static final State<String> STATE9 = new State<String>(new HashSet<Item<String>>(Arrays.asList(
			new Item<String>(3, Rules2.S_VaE, Arrays.asList(EOF))
			)));
	
	@SuppressWarnings("unchecked")
	private static final State<String> STATE10 = new State<String>(new HashSet<Item<String>>(Arrays.asList(
			new Item<String>(2, Rules2.V_sE, Arrays.asList(EOF)),
			new Item<String>(2, Rules2.V_sE, Arrays.asList("="))
			)));
	
	@SuppressWarnings("unchecked")
	private static final State<String> STATE11 = new State<String>(new HashSet<Item<String>>(Arrays.asList(
			new Item<String>(1, Rules2.V_x, Arrays.asList(EOF))
			)));
	
	@SuppressWarnings("unchecked")
	private static final State<String> STATE12 = new State<String>(new HashSet<Item<String>>(Arrays.asList(
			new Item<String>(1, Rules2.E_V, Arrays.asList(EOF)),
			new Item<String>(1, Rules2.E_V, Arrays.asList("="))
			)));
	
	@SuppressWarnings("unchecked")
	private static final State<String> STATE13 = new State<String>(new HashSet<Item<String>>(Arrays.asList(
			new Item<String>(1, Rules2.V_sE, Arrays.asList(EOF)),
			new Item<String>(Rules2.E_V, Arrays.asList(EOF)),
			new Item<String>(Rules2.V_x, Arrays.asList(EOF)),
			new Item<String>(Rules2.V_sE, Arrays.asList(EOF))
			)));
	
	@SuppressWarnings("unchecked")
	private static final State<String> STATE14 = new State<String>(new HashSet<Item<String>>(Arrays.asList(
			new Item<String>(2, Rules2.V_sE, Arrays.asList(EOF))
			)));
	
	
	
	public void testActionsTable(){
		Parser<String> parse = new Parser<String>(new Grammar2());
		
		//check the states
		Collection<State<String>> states = parse.getStates();
		assertEquals(14, states.size());
		assertTrue(states.contains(STATE1));
		assertTrue(states.contains(STATE2));
		assertTrue(states.contains(STATE3));
		assertTrue(states.contains(STATE4));
		assertTrue(states.contains(STATE5));
		assertTrue(states.contains(STATE6));
		assertTrue(states.contains(STATE7));
		assertTrue(states.contains(STATE8));
		assertTrue(states.contains(STATE9));
		assertTrue(states.contains(STATE10));
		assertTrue(states.contains(STATE11));
		assertTrue(states.contains(STATE12));
		assertTrue(states.contains(STATE13));
		assertTrue(states.contains(STATE14));
		
		//check the actions table
		Action<String> a = parse.getAction(STATE1, "x");
		assertEquals(Actions.SHIFT, a.id());
		assertEquals(STATE8, a.nextState());
		assertNull(a.production());
		
		a = parse.getAction(STATE1, "x");
		assertEquals(Actions.SHIFT, a.id());
		assertEquals(STATE8, a.nextState());
		assertNull(a.production());
		
		a = parse.getAction(STATE1, "*");
		assertEquals(Actions.SHIFT, a.id());
		assertEquals(STATE6, a.nextState());
		assertNull(a.production());
		
		a = parse.getAction(STATE1, "=");
		assertNull(a);
		
		a = parse.getAction(STATE1, EOF);
		assertNull(a);
		
		a = parse.getAction(STATE1, "S");
		assertEquals(Actions.GOTO, a.id());
		assertEquals(STATE2, a.nextState());
		assertNull(a.production());
		
		a = parse.getAction(STATE1, "E");
		assertEquals(Actions.GOTO, a.id());
		assertEquals(STATE5, a.nextState());
		assertNull(a.production());
		
		a = parse.getAction(STATE1, "V");
		assertEquals(Actions.GOTO, a.id());
		assertEquals(STATE3, a.nextState());
		assertNull(a.production());
		
		a = parse.getAction(STATE2, "$");
		assertEquals(Actions.ACCEPT, a.id());
		assertNull(a.nextState());
		assertNull(a.production());
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
