#include<stdio.h>
int depth = 0;
int reg_size = 12;
int underflow = 0;
int overflow = 0;
int win_count = 0;

int ackerman(int x, int y){
	depth++;
	int ret_value;
	if(win_count == reg_size-1)
		overflow++;
	else
		win_count++;
	if(x == 0)
		ret_value = y +1;
	else if(y == 0)
		ret_value = ackerman(x-1, 1);
	else
		ret_value = ackerman(x-1, ackerman(x, y-1));
	if(win_count <= 0)
		underflow ++;
	else
		win_count --;
	return ret_value;
}


int main()
{
	int r = ackerman(3, 6);
	printf("%d %d %d %d",r, depth, underflow, overflow);
	return 0;
}

