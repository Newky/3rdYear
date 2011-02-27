#include<stdio.h>
#include<stdlib.h>

void find_things(char * haystack, char * needle, int * result)
{
	int i,count=0;
	#pragma omp for
	for (i = 0; i <haystack[i]; i++) {
		if(match(haystack[i], str))
		{
			result[count] = i;
			count++;
		}
	}
}

int main()
{
	return 0;
}
