#include<stdio.h>
#include<stdlib.h>
#include<string.h>


int check_keyword(char * store, int count, char ** keywords, int keywordcount)
{
	int i=0;
	for (i = 0; i < keywordcount; i++) {
		if(strcmp(store, keywords[i]) == 0)
			return 1;
	}
	return 0;
}

int print_keyword(char * store)
{
	printf("{ReservedWord, %s}", store);
}

int print_identifier(char * store)
{	
	printf("{Identifier, %s}", store);
}
int main()
{
	char in;
	scanf("%c", &in);
	char store[9];
	int count=0;
	/*char * keywords = malloc(sizeof(char) * 9);*/
	/*int i=0;*/
	/*for(;i<9;i++)*/
	/*keywords[i] = malloc(sizeof(char)*9);*/
	char * keywords[10] = {
	"AND",
	"ARRAY", 
	"BEGIN",
	"CASE",
	"CONST",
	"DIV",
	"DO",
	"DOWNTO",
	"ELSE",
	"END"
	};
	while(strcmp(store, "END") != 0)
	{
		while(in == ' ')
			scanf("%c", &in);
		if(isalpha(in))
			store[count++] = in;
		else
			printf("invalid %c", in);
		while(scanf("%c",&in) && (isalpha(in) || isdigit(in)))
		{
			if(count <8)
				store[count++] = in;
		}
		if(in == ' ' || in == 10)
		{
			store[count] = '\0';
			int i=0;
			while(store[i] != '\0' )
				store[i] = (islower(store[i]))? toupper(store[i++]): store[i++];
			if(check_keyword(store, count, keywords,10))
				print_keyword(store);
			else
				print_identifier(store);
		}
		else
		{
			printf("invalid %d", in);
			return 0;
		}
		count=0;
	}

	return 0;
}
