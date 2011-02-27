#include<stdio.h>
#include<stdlib.h>
int main()
{
	#define MAX_NUMBER_LENGTH 10 
	char limit_neg[]="2147483648";
	char limit_plus[]="2147483647";
	char in;
	int sign;
	scanf("%c", &in);
	if( in == '-' || in == '+')
	{
		sign = (in == '-') ? -1 : 1;
		scanf("%c", &in);
	}
	else
		sign = 1;
	char * num_str = malloc(sizeof(char) * 1000);
	int count=0;
	while(isdigit(in)){
		/*printf("%c", in);*/
		num_str[count] = in;
		scanf("%c", &in);
		count++;
	}
	num_str[count] = '\0';
	printf("%s", num_str);
	printf("\nThe count is %d\n", count);
	if(count > MAX_NUMBER_LENGTH)
		printf("invalid");
	if(count == MAX_NUMBER_LENGTH)
	{
		if(sign == -1)
		{
			int i;
			for (i = 0; i < MAX_NUMBER_LENGTH; i++) {
				if(limit_neg[i] < num_str[i])
				{
					printf("invalid");
					return 1;
				}
			}		
		}
		else
		{
			int i;
			for (i = 0; i < MAX_NUMBER_LENGTH; i++) {
				if(limit_plus[i] < num_str[i])
				{
					printf("invalid");
					return 1;
				}
			}

		}
	}
	printf("valid");
	return 0;
}
