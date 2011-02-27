#include<stdio.h>
#include<stdlib.h>
#include<pthread.h>
//Size of buffer
#define BUFFER_SIZE 5 
//Global mutex
pthread_mutex_t mutex1 = PTHREAD_MUTEX_INITIALIZER;
//Virtual buffer, simply a counter.
int buffer = 0;

/* Producer function 
 * Infinite loop
 * Program terminates with ctrl-c
 * Sleep for random time .
 * Look for mutex lock and increment buffer.
 */
void * producer(){
	while(1){
		sleep((rand() % 3));
		pthread_mutex_lock(&mutex1);
		//Condition variable
		if(buffer != BUFFER_SIZE){
			buffer += 1;
			printf("Added to buffer %d\n", buffer);
		}else{
			printf("OVERFLOW DETECTED!\n");
		}
		pthread_mutex_unlock(&mutex1);
	}
	return NULL;
}
/*Consumer function
 * sleep random time
 * get lock, decrement buffer
 */
void * consumer(){
	while(1){
		sleep((rand() % 5));
		pthread_mutex_lock(&mutex1);
		//Condition variable
		if(buffer != 0){
			buffer -= 1;
			printf("Consumed buffer %d\n", buffer);
		}else{
			printf("UNDERFLOW!\n");
		}
		pthread_mutex_unlock(&mutex1);
	}
	return NULL;
}

int main()
{
	//Seed the random number generator
	srand(time(NULL));
	/* Make two threads*/
	pthread_t threads[2];
	int i,rc;
	// Create and eventually join threads.
	// one for producer and one for consumer
	rc = pthread_create(&threads[0], NULL, producer,NULL);
	rc = pthread_create(&threads[1], NULL, consumer,NULL);
	rc = pthread_join(threads[0],NULL);
	rc = pthread_join(threads[1],NULL);
	return 0;
}
