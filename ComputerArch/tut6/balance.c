#include<stdio.h>
#include<stdlib.h>
#include<pthread.h>
#include<time.h>

int balancea = 100;
int balanceb = 100;
int balancec = 100;
int spinlock = 0;
int count = 0;

void lock() { 
	__asm__("jmp	start;"
		"counter:;"
			"addl	$1, count;"
		"start:;"
			"cmpl $0, spinlock;"
			"jne	counter;"
			"movl	$1, spinlock;"
			);
}

void unlock () {
	__asm__(	
		
			"movl	$0,spinlock;"
	);
}

struct thread_args {
	int * srcbalance;
	int * destbalance;
};

void * bank (void * ptr) {
	struct thread_args *args = (struct thread_args *)ptr;
	lock();
	int * srcbalance = args->srcbalance;
	int * destbalance = args->destbalance;
	int i = 0;
	int amount;
	for (i = 0; i < 1000000; i++) {
		amount = (rand() % 21) + 1;

		if(*srcbalance > 0){
			if(*srcbalance < amount)
				amount = *srcbalance;
			if (amount > 0) {
				*srcbalance -= amount;
				*destbalance += amount;
			}
		}
	}
	unlock();
}


int main()
{
	srand(time(NULL));
	int bala, balb, balc;
	pthread_t t1, t2, t3;
	struct thread_args t1args, t2args, t3args;
		
	t1args.srcbalance = &balancea;
	t1args.destbalance = &balanceb;
	t2args.srcbalance = &balanceb;
	t2args.destbalance = &balancec;
	t3args.srcbalance = &balancec;
	t3args.destbalance = &balancea;

	bala = pthread_create(&t1, NULL, bank, (void *) &t1args);
	balb = pthread_create(&t2, NULL, bank, (void *) &t2args);
	balc = pthread_create(&t3, NULL, bank, (void *) &t3args);

	pthread_join(t1, NULL);
	pthread_join(t2, NULL);
	pthread_join(t3, NULL);

	printf("%d %d %d \n", balancea, balanceb, balancec);

	return 0;
}
