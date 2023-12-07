package edu.ufl.cise.cop4020fa23.ast;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import edu.ufl.cise.cop4020fa23.exceptions.PLCCompilerException;
import edu.ufl.cise.cop4020fa23.exceptions.TypeCheckException;

public class SymbolTable {
	public class Pair<Obj, Int> {
	    public final NameDef first;
	    public final Integer second;

	    public Pair(NameDef first, Integer second) {
	        this.first = first;
	        this.second = second;
	    }
	}
	
	HashMap<String, LinkedList<Pair<NameDef, Integer>>> st = new HashMap<String, LinkedList<Pair<NameDef, Integer>>>();
	Stack<Integer> stack = new Stack<Integer>();
	int curr_num = 0;
	int next_num = 0;
	
	SymbolTable(){
		stack.push(0); // makes sure stack.peek() doesn't throw an exception
	}
	
	void enterScope() {
		curr_num = next_num++;
		stack.push(curr_num);
	}
	
	void leaveScope() {
		curr_num = stack.pop();
		next_num = curr_num+1;
		//deleteOutofScope();
	}
	
	void deleteOutofScope() {
		for (HashMap.Entry<String, LinkedList<Pair<NameDef,Integer>>> entry : st.entrySet()) {
		String key = entry.getKey();
        LinkedList<Pair<NameDef, Integer>> value = entry.getValue();
        
        //System.out.println("Key: " + key);
        for (Pair<NameDef, Integer> pair : value) {
        	System.out.print(pair.second);
            if (pair.second > curr_num) {
            	value.remove(pair);
            }
        }
        
        for (Pair<NameDef, Integer> pair : value) {
        	System.out.print(pair.second);
        }
        
        st.remove(key);
        st.put(key, value);
	}
	}
	
	public static void iterateAndDeleteFromHashMap(HashMap<String, LinkedList<Pair<NameDef, Integer>>> hashMap, String nameToDelete) {
        Iterator<HashMap.Entry<String, LinkedList<Pair<NameDef, Integer>>>> iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            HashMap.Entry<String, LinkedList<Pair<NameDef, Integer>>> entry = iterator.next();
            String key = entry.getKey();
            LinkedList<Pair<NameDef, Integer>> value = entry.getValue();

            // Iterate over the LinkedList to find and remove specific values
            Iterator<Pair<NameDef, Integer> > listIterator = value.iterator();
            while (listIterator.hasNext()) {
                Pair<NameDef, Integer> pair = listIterator.next();
                
               /* if (pair.second. ) {
                    listIterator.remove(); // Remove the pair with the specified name
                }*/
            }
        }
	}
	
	NameDef lookup(String name) throws PLCCompilerException {
		LinkedList<Pair<NameDef, Integer>> list = st.get(name);
		NameDef nameDef = null;
		int maxScope = 0;
		if (list == null) {
			return nameDef;
			//throw new TypeCheckException("Lookup did not find name!");
		}
		for(Pair<NameDef, Integer> pair : list) {
			if (pair.second <= stack.peek() && pair.second >= maxScope) { // if NameDef is in the scope or encompasses it and if it is greater than our maxScope
				nameDef = pair.first;
				maxScope = pair.second;
				System.out.print(maxScope + "\n");
				//return nameDef;
			}
		}
		return nameDef;
	}
	
	void insert(NameDef nameDef) {
		LinkedList<Pair<NameDef, Integer>> list = st.get(nameDef.getName());
		if (list == null) {
			list = new LinkedList<Pair<NameDef, Integer>>();
			st.put(nameDef.getName(), list);
		}
		Pair<NameDef, Integer> pair = new Pair<>(nameDef, stack.peek());
		st.get(nameDef.getName()).push(pair);
	}
	
	void printStack() {
		 System.out.println("Stack elements:");
	        for (Integer element : this.stack) {
	            System.out.println(element);
	        }
	}
	
	HashMap<String, LinkedList<Pair<NameDef, Integer>>> getST() {
		return st;
	}
	
}
