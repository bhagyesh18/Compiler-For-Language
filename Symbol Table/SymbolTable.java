import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;


public class SymbolTable {

	// Read file and store into string 
	public String readFile(String fileName)
	{
		 String readbytes;
		 StringBuilder sbuilder = new StringBuilder();
         try {
			  FileReader fr =  new FileReader(fileName);
			  BufferedReader bufferR =  new BufferedReader(fr);
			  readbytes = bufferR.readLine();
	            while (readbytes != null) {
	                sbuilder.append(readbytes);
	                sbuilder.append("\n");
	                readbytes = bufferR.readLine();
	            }
	            bufferR.close();         
	        }
	        catch(Exception e) {
	            System.out.println(e.getMessage());
	        }
		  return sbuilder.toString();
	}
	
	// Calculate the hashvalue as per the Hash Function
	public Integer calculateHashValue(String operand,int mod){
		int hashValue=0;
		char character[]=operand.toCharArray();
		int len=character.length;
		for(int i=0;i<len;i++){
			hashValue=hashValue+(int)character[i];
		}
		hashValue=hashValue%mod;
		return hashValue;
	}
	
	
	// Create HashTables for number of Blocks
	public  ArrayList<Hashtable<Integer, ArrayList<String>>> addHashKey( ArrayList<Hashtable<Integer, ArrayList<String>>> hashtableList,int divideVal){

		Hashtable<Integer, ArrayList<String>> hashtableBlock=new Hashtable<Integer, ArrayList<String>>();
			
		for(int i=0;i<divideVal;i++){
			ArrayList<String> identifierArrayList=new ArrayList<String>();
			hashtableBlock.put(i, identifierArrayList);
		}
		hashtableList.add(hashtableBlock);
		return hashtableList;
	}
	
	// Find that identifier is in the Current Scope or not?
	public boolean findInCurrentScope(ArrayList<String> identifierList,String identifier){
		boolean isthereFlag=false;
		int i;
		for(i=0;i<identifierList.size();i++){
			if(identifierList.get(i).equals(identifier)){
				isthereFlag=true;
				break;
			}
		}
		return isthereFlag;
	}
	
	// Insert the identifier into hashtable Block  
	public ArrayList<Hashtable<Integer, ArrayList<String>>> insertIntoHashTable(ArrayList<Hashtable<Integer, ArrayList<String>>> hashtableList,int hashVal,Stack<Integer> stack, String identifier){
		boolean isThereFlag=false;
		ArrayList<String> identifierList=new ArrayList<String>();
		identifierList=(ArrayList<String>)hashtableList.get(stack.lastElement()).get(hashVal);
		
		isThereFlag=findInCurrentScope(identifierList, identifier);
		
		if(isThereFlag==false){
			identifierList.add(identifier);
		}
		
		hashtableList.get(stack.lastElement()).put(hashVal, identifierList);
		return hashtableList;    
	}
	
	// Find identifier in all other scope
	public void findInAllScopes(ArrayList<Hashtable<Integer, ArrayList<String>>> hashtableList,int hashVal,Stack<Integer> stack, String identifier){
		
		boolean isThere=false;
		int iterator;
			
		for(int j=0;j<hashtableList.get(stack.lastElement()).size();j++){
			if(j!=hashVal)
			{
				for(iterator=0;iterator<hashtableList.get(stack.lastElement()).get(j).size();iterator++){
					ArrayList<String> identifierList=new ArrayList<String>();
					identifierList=(ArrayList<String>)hashtableList.get(stack.lastElement()).get(j);
								
					for(int i=0;i<identifierList.size();i++){
						if(identifierList.get(i).equals(identifier)){
							System.out.println("Idenfier"+identifier+" is already present in the Key :"+iterator);
							isThere=true;
							break;
					}}
					if(isThere)	break;
				}if(isThere)	break;	
			}}
	}
	
	// Display the Symbol Table
	public void display(ArrayList<Hashtable<Integer, ArrayList<String>>> hashtableList,Stack<Integer> stack){
		boolean active=false;
		for(int i=0;i<hashtableList.size();i++){
				System.out.println("---------------------------------------------------");
				if(!stack.empty()){
					if(stack.lastElement()==i)
						active=true;
					else
						active=false;
				}
				System.out.print("SYMBOL TABLE  [ BLOCK=" +(i+1) + ", ACTIVE="+active+" ]" );
				for(int j=0;j<hashtableList.get(i).size();j++){
					System.out.println();
					System.out.print(" ["+j+"] :: ");
			
								for(int k=0;k<hashtableList.get(i).get(j).size();k++){
									String hashVal;
								
									if(k==0)
										System.out.print(" ["+hashtableList.get(i).get(j).get(k)+"]");
									else
										System.out.print(" -> ["+hashtableList.get(i).get(j).get(k)+"]");
						}
				}
				System.out.println();
		}
	}
	
	
	public static void main(String[] args) {
	    int countStack=0,hashVal,divValue;
	    String OPEN="{",CLOSE="}",fileName,input;
        SymbolTable st=new SymbolTable();
        Scanner readName=new Scanner(System.in);
        ArrayList<String> identifierList=new ArrayList<String>();
        ArrayList<Hashtable<Integer, ArrayList<String>>> hashList = new ArrayList<Hashtable<Integer, ArrayList<String>>>();
        Stack<Integer> stack=new Stack<Integer>();
        
        System.out.print("Enter File name : ");
        fileName=readName.nextLine();
        
        System.out.print("Enter h % Divide Value : ");
        divValue=Integer.parseInt(readName.nextLine());
       
        input=st.readFile(fileName);
        System.out.println("Your Input File ::");
        System.out.println("--------------------------------------------------");
        System.out.println(input);
        
     	StringTokenizer spaceToken = new StringTokenizer(input);
    	while (spaceToken.hasMoreElements()) {
    		identifierList.add(spaceToken.nextToken().toString());
    	}
    	
    	for(int j = 0;j<identifierList.size();j++){
    		
    		if(identifierList.get(j).equals(OPEN)){
	    			stack.push(countStack++);
	    			Hashtable<Integer, ArrayList<String>> hashtable=new Hashtable<Integer, ArrayList<String>>();
	    			hashList=st.addHashKey(hashList, divValue);
	    		
    		}else if(identifierList.get(j).equals(CLOSE)){
    				stack.pop();
    		}
    		else{
	    			hashVal=st.calculateHashValue(identifierList.get(j),divValue);
	    			hashList=st.insertIntoHashTable(hashList, hashVal,stack,identifierList.get(j));
	    			st.findInAllScopes(hashList, hashVal,stack,identifierList.get(j));
       		}
    	}
    	st.display(hashList,stack);
	}
      
}
