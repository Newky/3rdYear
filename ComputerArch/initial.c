#include<stdio.h>

int g = 256;

int p(int i, int j){
	int k;
	k = i + j;
	return (k << 2) -1;
}

int q(int i)
{
	return p(g, -i);
}

int f(int n)
{
	if(n > 0)
		return n*f(n-1);
	else
		return 1;
}

int main()
{
	return 0;
}
