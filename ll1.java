import java.util.*;  

public class ll1 {

	/*
	 * Please update the file/class name, and the following comment
	 */

	// Tutorial_ID_Name

	static class CFG {
		String grammar;
		Map<String ,  String[] > productions,follow_dict;
		String start = "NONE";
		Map<String ,  Map<String,String> > table;


		/**
		 * Creates an instance of the CFG class. This should parse a string
		 * representation of the grammar and set your internal CFG attributes
		 * 
		 * @param grammar A string representation of a CFG
		 */
		public CFG(String grammar) {

			this.grammar = grammar;
			this.productions = new HashMap<String, String[] >();
			this.follow_dict = new HashMap<String, String[] >();

			String[] grams = grammar.split(";");
			for(int i=0;i<grams.length;i++){
				String[] gram = grams[i].split(",");
				if(start.equals("NONE")){
					this.start=gram[0];
				}
				String lhs = gram[0];
				productions.put( lhs , Arrays.copyOfRange(gram, 1, gram.length));
			}
			String[] ss = {"$"};
			// follow_dict.put(this.start,ss);
			for(String lhs : productions.keySet() ){
				follow_dict.put(lhs,new String[0]);
			}

			// System.out.println("Check : " + follow_dict.keySet().contains("SSS"));
			// for( String sss : follow_dict.keySet() ){
			// 	System.out.println(sss);
			// }
			follow_dict.put(this.start,ss);

			for(String lhs : productions.keySet() ){
				// System.out.println("---> : "+lhs);
				follow_dict = follow( lhs , follow_dict );
			}

			for( String s : follow_dict.keySet() ){
				System.out.println(s);
				for( String val : follow_dict.get(s) ){
					System.out.print(val + " ");
				}
				System.out.println(" ");

			}
			// follow("S");
		
			// System.out.println( this.start + " : : " + productions.get("T")[0]  );
		}

		public boolean check_str(String[] a,String trg){
			for(String s : a){
				if( s.equals(trg) ){
					return true;
				}
			}
			return false;
		}

		public Map<String ,  String[] > follow(String s,Map<String ,  String[] > ans){
			if( s.length() != 1 ){
				return new HashMap<String, String[] >();
			}
			// System.out.println("CALL : "+s);
			for( String key : productions.keySet() ){
				// System.out.println(key);
				for( String value : productions.get(key) ){
					 // System.out.println( key + " : " + value );
					int f = value.indexOf(s);
					System.out.println("f::"+f);
					if(f!=-1){
						if( f==value.length()-1 ){
							if( key.equals(s)==false ){
								if( ans.keySet().contains(key)==false){
									
								 	ans = follow(key,ans);
								}
								ans.put(s,union(ans.get(s),ans.get(key)));
							}
						}else{
							String ss = "";
							for(int i=f+1;i<value.length();i++){
								ss+=value.charAt(i);
							}
							String[] first_of_next = first( ss );
							if( check_str(first_of_next,"e")==true ){
								if( key.equals(s)==false ){
									if(ans.keySet().contains(key)==false){
										ans = follow(key,ans);
									}
									ans.put( s , union( ans.get(s) , ans.get(key) ) );
									ArrayList<String> sss = new ArrayList<String>(Arrays.asList(union( ans.get(s) , first_of_next )));
									sss.remove("e");
									ans.put( s , sss.toArray(new String[0]));
									ans.put(s , union(ans.get(s),first_of_next));
								}
							}else{
								ans.put(s , union(ans.get(s),first_of_next));
							}
						}
					}
				}
			}

			return ans;

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
			if(a==null)
				return b;
			if(b==null)
				return a;
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
// 			if value!='@':
// 				for element in first(value, productions):
// 					table[key, element] = value
// 			else:
// 				for element in follow[key]:
// 					table[key, element] = value

// 	for key,val in table.items():
// 		print key,"=>",val

// 	new_table = {}
// 	for pair in table:
// 		new_table[pair[1]] = {}

// 	for pair in table:
// 		new_table[pair[1]][pair[0]] = table[pair]

		public String table() {
			this.table = new HashMap< String , Map < String, String > >();

			for(String key : productions.keySet()){
				for(String value : productions.get(key)){
					if(value.equals("e")==false){
						for(String element : first(value)){
							if( table.keySet().contains(key) )
								table.get(key).put(element,value);
							else{
								table.put(key,new HashMap<String,String>());
								table.get(key).put(element,value);
							}
						}
					}else{
						for(String element : follow_dict.get(key)  ){
							if( table.keySet().contains(key) )
								table.get(key).put(element,value);
							else{
								table.put(key,new HashMap<String,String>());
								table.get(key).put(element,value);
							}
						}
					}
				}
			}
			String ans = "";
			for(String s1 : table.keySet()){
				for(String s2 : table.get(s1).keySet()){
					System.out.println(s1 + " : " + s2 +  " : " + table.get(s1).get(s2)  );
					ans += s1+","+s2+","+table.get(s1).get(s2)+";";
				}
			}
			// System.out.println("--> :"+table.get("T").get("$"));
			return ans;
		}

		/**
		 * Parses the input string using the parsing table
		 * 
		 * @param s The string to parse using the parsing table
		 * @return A string representation of a left most derivation
		 */
		public String parse(String user_input) {

			int flag=0;
			user_input=user_input+"$";
			Stack<String> stack = new Stack<String>();

			stack.push("$");
			stack.push(this.start);

			int input_len = user_input.length();
			int index = 0;



			while( stack.size()>0 ){
				String top = stack.peek();
				System.out.println("TOP : " + top);
				String current_input = user_input.charAt(index)+"";
				System.out.println("CUR : " + current_input );

				if( top.equals(current_input) ){
					stack.pop();
					index+=1;
				}else{
					if( table.get(top)==null || table.get(top).get(current_input)==null ){
						flag=1;
						break;
					}
					String value = table.get(top).get(current_input);
					if( value.equals("e")==false ){
						stack.pop();
						for( int i=value.length()-1;i>=0;i-- ){
							stack.push( ( value.charAt(i)+"" ) );
						}
					}else{
						stack.pop();
					}
				}

			}

			if(flag==0){
				System.out.println("Accepted");
			}else{
				System.out.println("Not Accepted");
			}




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
		String input2 = "iiac";
		CFG g = new CFG(grammar);
		System.out.println(g.table());
		System.out.println(g.parse(input1));
		System.out.println(g.parse(input2));
	}

}
