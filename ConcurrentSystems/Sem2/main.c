#include<pthread.h>
#include<stdio.h>
#include<stdlib.h>
#define NUM_THREADS 6

void * PrintHello(void * threadid){
	printf("\n %d Hello World \n", (int)threadid);
	pthread_exit(NULL);
}

int main(int argc, const char * argv[])
{
	pthread_t threads[NUM_THREADS];
	int rc, t;
	for (t = 0; t < NUM_THREADS; t++) {
		printf("Creating Thread %d \n", t);
		rc = pthread_create(&threads[t], NULL, 
						PrintHello, (void *)t);
		if(rc) {
			printf( "ERROR return code from pthread_create(): %d \n", rc);
			exit(-1);
		}
	}

	for (t = 0; t < NUM_THREADS; t++) {
		pthread_join(threads[t], NULL);
	}
	return 0;
}
