import java.util.*;  

public class ll1 {

	/*
	 * Please update the file/class name, and the following comment
	 */

	// Tutorial_ID_Name

	static class CFG {
		String grammar;
		Map<String ,  String[] > productions;
		String start = "NONE";

		/**
		 * Creates an instance of the CFG class. This should parse a string
		 * representation of the grammar and set your internal CFG attributes
		 * 
		 * @param grammar A string representation of a CFG
		 */
		public CFG(String grammar) {

			this.grammar = grammar;
			this.productions = new HashMap<String, String[] >();
			String[] grams = grammar.split(";");
			for(int i=0;i<grams.length;i++){
				String[] gram = grams[i].split(",");
				if(start.equals("NONE")){
					this.start=gram[0];
				}
				String lhs = gram[0];
				productions.put( lhs , Arrays.copyOfRange(gram, 1, gram.length));
			}

		
			// System.out.println( this.start + " : : " + productions.get("T")[0]  );
		}

		public String[] follow(String s){
			if( s.length() != 1 ){
				return new String[0];
			}
				return new String[0];
			
		}


		public String[] first(String s){
			String[] ans = {};
			if( 'A' <= s.charAt(0) && s.charAt(0)<='Z' ){
				for(int i=0;i<productions.get(s).length;i++){
					if(productions.get(s)[i].equals("e")  ){
						if( s.length()!=1 ){
							ans = union(ans , first( (s.charAt(1)+"") ) );
						}else{
							String[] ss = {"e"};
							ans = union( ans , ss );
						}
					}else{
						ans = union( ans , first( productions.get(s)[i] ) );
					}
				}
			}else{
				String[] ss = {s.charAt(0)+""};
				ans = union( ans , ss );
			}
			return ans;
		}

		public String[] union(String[] a, String[] b){
			 HashSet<String> set = new HashSet<>(); 
    			set.addAll(Arrays.asList(a));
    			set.addAll(Arrays.asList(b));
    			String[] uon = {};
    			uon = set.toArray(uon);
    			// System.out.println(Arrays.toString(uon));
    			return uon;
		}
















		/**
		 * Generates the parsing table for this context free grammar. This should set
		 * your internal parsing table attributes
		 * 
		 * @return A string representation of the parsing table
		 */
		public String table() {
			return null;
		}

		/**
		 * Parses the input string using the parsing table
		 * 
		 * @param s The string to parse using the parsing table
		 * @return A string representation of a left most derivation
		 */
		public String parse(String s) {
			return null;
		}
	}

	public static void main(String[] args) {

		/*
		 * Please make sure that this EXACT code works. This means that the method
		 * and class names are case sensitive
		 */

		String grammar = "S,iST,e;T,cS,a";
		String input1 = "iiac";
		String input2 = "iia";
		CFG g = new CFG(grammar);
		System.out.println(g.table());
		System.out.println(g.parse(input1));
		System.out.println(g.parse(input2));
	}

}
