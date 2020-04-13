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
			for(String lhs : productions.keySet() ){
				follow_dict.put(lhs,new String[0]);
			}

			follow_dict.put(this.start,ss);

			for(String lhs : productions.keySet() ){
				follow_dict = follow( lhs , follow_dict );
			}
		}

		/**
			this function will check the string in the array

			@param a A array of string
			@param trg Target in the aaray
			@return boolean value for result
		 */
		public boolean check_str(String[] a,String trg){
			for(String s : a){
				if( s.equals(trg) ){
					return true;
				}
			}
			return false;
		}

		/**
			This function will find the follow of s.

			@param s A stirng to find follow
			@param ans A datastructure to store the results for function
			@return map for follow of each s
		 */
		public Map<String ,  String[] > follow(String s,Map<String ,  String[] > ans){
			if( s.length() != 1 ){
				return new HashMap<String, String[] >();
			}

			for( String key : productions.keySet() ){
				for( String value : productions.get(key) ){
					int f = value.indexOf(s);
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

		/**
			This function will find the first of s, and returns all the first for s as array.

			@param s A stirng to find first
			@return An array of first fo s
		 */
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

		/**
			This function performs unioun opperation of 2 string array


			@param a First String array
			@param b Second String array
			@return unioun of a and b
		 */
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
    			return uon;
		}

		/**
		 * Generates the parsing table for this context free grammar. This should set
		 * your internal parsing table attributes
		 * 
		 * @return A string representation of the parsing table
		 */


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
					ans += s1+","+s2+","+table.get(s1).get(s2)+";";
				}
			}
			return ans;
		}

		/**
		 * Parses the input string using the parsing table
		 * 
		 * @param user_input The string to parse using the parsing table
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

			String matched_String = "";

			ArrayList<String> ans =new ArrayList<String>();
			ans.add(stack.peek());


			while( stack.size()>0 ){
				String top = stack.peek();
				String current_input = user_input.charAt(index)+"";
				String unmatched_string = "";

				if( top.equals(current_input) ){
					if(top.equals("$")==false)
						matched_String+=top;
					stack.pop();
					index+=1;
				}else{
					if( table.get(top)==null || table.get(top).get(current_input)==null ){
						flag=1;
						ans.add("ERROR");
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

				for( String aa : stack ){
					if(aa.equals("$")==false)
					unmatched_string =  aa + unmatched_string;
				}

				if( ans.get( ans.size()-1 ).equals( (matched_String+unmatched_string) )==false ){
					ans.add((matched_String+unmatched_string));
				}

			}

			String temp  = "";
			for(String tt : ans){
				temp = temp + "," + tt;
			}

			return temp;
	
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
