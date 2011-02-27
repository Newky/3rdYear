#include<stdio.h>
#include "Parser.h"
#include "Scanner.h"
#include<wchar.h>

int main(int argc, char *argv[]){
   map<string, int> * SymTable = new map<string, int>;
   char line[100];
   printf(">>>");
   while(fgets(line, 100, stdin) != NULL)
   {
	   Scanner *scanner = new Scanner((const unsigned char *)line, 100);
	   Parser *parser = new Parser(scanner);
	   parser->tab = SymTable;
	   parser->Parse();
	   delete scanner;
	   delete parser;
	   printf(">>>");
   }	
}
