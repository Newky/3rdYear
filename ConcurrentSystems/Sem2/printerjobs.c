#include<stdio.h>
#include<stdlib.h>
#include<pthread.h>

#define NO_JOBS 10

//Simulated Printer Queueing System
//Emulate Semaphores by using mutexes and condition variables.
pthread_mutex_t mutex1 = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t  condition_var   = PTHREAD_COND_INITIALIZER;

int printers = 0;

/*
 *Each print job gets a print job requester it gets the  
 */
void * print_job_requester(void * printjobno){
	//get lock 
	pthread_mutex_lock(&mutex1);
	if(printers == 3)
		pthread_cond_wait(&condition_var, &mutex1);
	printers++;
	pthread_mutex_unlock(&mutex1);
	printf("Printing...%d\n", (int) printjobno);
	sleep((rand() % 6));
	printf("Completed %d\n", (int) printjobno);
	pthread_mutex_lock(&mutex1);
	printers--;
	pthread_cond_signal(&condition_var);
	pthread_mutex_unlock(&mutex1);
	return NULL;
}

int main()
{
	pthread_t threads[NO_JOBS];
	srand(time(NULL));
	int rc,i;
	for (i = 0; i < NO_JOBS; i++) {
		rc = pthread_create(&threads[i], NULL, print_job_requester, (void *) i);
		sleep(rand()%3);
	}
	for (i = 0; i < NO_JOBS; i++) {
		rc = pthread_join(threads[i],NULL);
	}
	return 0;
}
