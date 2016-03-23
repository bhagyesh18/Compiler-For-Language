%{
#include <string.h>

struct structSymbolTable {
    char *identifier;
    int length;
    int linum;
    int hashVal;
    int scope;
};

struct stack {
   int s[100];
   int topofStack;
} stackforscope;
 
typedef struct structSymbolTable structST;
structST *identifiers[100];
int linum = 1,tableLenght=100,tableTrack = 0,scopeStack=0;
char *yystr;

void push(int item);
void pop();
int getVal();
void addintoST(int tableTrack,char *id,int length,int hashValue,int scopeVal,int linum);
int addSymbolLinkedList(char *identifier,  int linum,int scopeVal,int length);
int checkNotNull(int tableTrack);
int checkDuplicate(int tableTrack,char *id,int length);
void setid(int tableTrack,char *id,int length);

%}
string  \([^\n"]*\)
boolean (T|F){1}
whitespace   [ \t]+
integer  [-]?[\t]?[1-9][0-9]*|0
float  [0-9]+"."[0-9]+
identifier   [a-zA-Z][a-zA-Z0-9]{1,6}
logicalOp [>=|<=|==|!=|<|>]
arithmetic "-"|"*"|"/"|"%"|"+"|"="
%%

{whitespace} {  }
"Program"|"Function"|"then"|"if"|"print"|"else"|"and"|"while"|"do"|"return"|"or"  { printf("%19s %19s %5d\n", yytext,"KeyWord", linum); }
 
\n { linum++; }

{integer} { 	printf("%19s %19s %5d\n", yytext,"Int", linum);}
{float} { 	printf("%19s %19s %5d\n", yytext,"Float", linum);}
{string} { 	printf("%19s %19s %5d\n", yytext, "String",linum);}
{boolean} {	printf("%19s %19s %5d\n", yytext,"Boolean", linum);}
{arithmetic} {  printf("%19s %19s %5d\n", yytext,"Arithmetic", linum); }
{logicalOp} { 	printf("%19s %19s %5d\n", yytext,"Logical operator", linum); }
"("  { 		printf("%20c %19s %5d\n", 40,"Braces open", linum); }
")"  {  	printf("%20c %19s %5d\n", 41,"Braces close", linum); }
"{"  { 
		if(strcmp(yytext,"{")==0){
	       	 	scopeStack++;
			push(scopeStack);
		
		}
		printf("%19s %19s %5d\n", "\{","Curly Brace Open", linum); }

"}"  { 	if(strcmp(yytext,"}")==0){ pop(); } printf("%19c %19s %5d\n", 125,"Curly Brace Close", linum); }
{identifier} {	addSymbolLinkedList(yytext, linum,getVal(),yyleng); 	printf("%19s %19s %5d\n", yytext,"Identifier", linum);}
.	{ 	printf("Invalid %s\n", yytext); }
%%

void push(int item) {
   stackforscope.topofStack++;
   stackforscope.s[stackforscope.topofStack] = item;
}
  
void pop() {
   stackforscope.topofStack--;
}

int getVal(){
  return stackforscope.s[stackforscope.topofStack];	
}


int addSymbolLinkedList(char *id,int linum,int scopeVal,int length)
{
   int iterator, hashFinal = 0,flag=0;
    for (iterator=0; iterator<length; iterator++) {
        hashFinal +=id[iterator] * iterator;
    }
   int hashValue = hashFinal % tableLenght;
   int tableTrack = hashFinal % tableLenght;
   
    if (checkNotNull(tableTrack)==1) {
        while (checkNotNull(tableTrack)==1 && tableTrack < tableLenght) {
            if(checkDuplicate(tableTrack,id,length)==1){ 
			return 1;
	    }
            tableTrack++;
        }
    }

    addintoST(tableTrack,id,length,hashValue,scopeVal,linum);
 
    return 0;
}

int checkNotNull(int tableTrack){
	if (identifiers[tableTrack]){
		return 1;	
	}
return 0;
}

int checkDuplicate(int tableTrack,char *id,int length){
	if (! strncmp(identifiers[tableTrack]->identifier, id, length)){
		return 1;	
	}
	return 0;
}

void addintoST(int tableTrack,char *id,int length,int hashValue,int scopeVal,int linum){
    setid(tableTrack,id,length);
    identifiers[tableTrack]->length = length;
    identifiers[tableTrack]->hashVal = hashValue;
    identifiers[tableTrack]->scope = scopeVal;
    identifiers[tableTrack]->linum = linum;
   
}

void setid(int tableTrack,char *id,int length){
    identifiers[tableTrack] = calloc(1, sizeof(structST));
    identifiers[tableTrack]->identifier = calloc(1, length+1);
    strncpy(identifiers[tableTrack]->identifier, id, length);
}

int main (int NoOfarg, char **argv)
{
    if (NoOfarg < 2) {
        printf("Enter File Name. Ex. Sample.FB \n");
    }
    printf("Enter Hash Symbol Table Size :");
    scanf("%d",&tableLenght);
    
    printf("%19s %19s %6s\n", "Token", "Type", "Line");
    yyin = fopen(argv[1], "r");
    yylex();
    
    printf("\n\n------ SYMBOL TABLE -------- ");
   
    printf("\n%20s %10s  %10s %10s\n ", "Symbol", "Line","hash_Value","Scope");

    int iterator;

    for (iterator=0; iterator<tableLenght; iterator++) {
        if (identifiers[iterator])
        fprintf(stdout,"%20s %10d  %10d  %10d\n", identifiers[iterator]->identifier, identifiers[iterator]->linum,identifiers[iterator]->hashVal,identifiers[iterator]->scope);
    }
    return 0;
}

