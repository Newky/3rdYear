#include<stdio.h>
#include<stdlib.h>

double stdev(double * nums, int size)
{
	int i;
	double mean, dev, sum=0, diff;

	#pragma omp parallel for reduction(+:sum)
	{
		for(i=0;i<size;i++){
			sum += nums[i];
		}
		mean = sum/size;
	}
	return mean;
}

int main()
{
	double nums[] = {2.2, 3.5, 6.5, 7.8};
	printf("%lf\n", stdev(nums, 4));
	return 0;
}
