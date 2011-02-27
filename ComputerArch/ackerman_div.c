#include <stdio.h>
#include <stdlib.h>
#include <time.h>
/*
Stock code takes ~1700 microseconds

   */
/*
int count= 0;
static int CWP =0;
static int SWP =0;
static int overflow = 0;
static int underflow= 0;
//static int window[6];
static int windowSize = 6;
int ackerman(int x, int y){
   count++;
   changeWindow();
   if(x == 0)
      return y+1;
   else if (y ==0)
      return ackerman(x-1,1);
   else
      return ackerman(x-1, ackerman(x, y -1));
}
void changeWindow(){
;
}*/

int main(){
  /* struct timeval start_time, end_time;
   long long compute_time; */
   int * window = malloc(sizeof(int) * 6);   
   int i;
   printf("hello\n");
   for(i =0; i < 40; i++){ //Why does this run??
      window[i] = 12;
      printf("%d\n", window[i]);
   }
   /*
   printf("hello again\n");
   gettimeofday(&start_time,NULL);
   int r = ackerman(3,6);
   gettimeofday(&end_time, NULL);
   compute_time = (end_time.tv_sec - start_time.tv_sec) * 1000000L +
           (end_time.tv_usec - start_time.tv_usec);
   printf("Called %d times\n Took %lld microseconds\n", count, compute_time);
   */
   return 0;
}

