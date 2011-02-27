#include<stdio.h>
#include<stdlib.h>

int main()
{
	int res[3][5];
	int a[3][4] = {
	{1, 2, 3, 4},
	{1, 2, 3, 4},
	{1, 2, 3, 4}
	};
	int b[4][5] = {
	{1, 2, 3, 4, 5},
	{1, 2, 3, 4, 5},
	{1, 2, 3, 4, 5},
	{1, 2, 3, 4, 5}
	};
	int i,j,k;
	for (i = 0; i < 3; i++) {
		for (j = 0; j < 5; j++) {
			res[i][j] = 0;
			for (k = 0; k < 4; k++) {
				res[i][j] += (a[i][k] * b[k][j]);
			}
			printf("%d ", res[i][j]);
		}
		printf("\n");
	}
	
	return 0;
}
