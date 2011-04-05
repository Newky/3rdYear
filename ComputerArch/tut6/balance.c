#include<stdio.h>
#include<stdlib.h>
#include<pthread.h>
#include<time.h>

int balancea = 100;
int balanceb = 100;
int balancec = 100;
volatile int spinlock = 0;
//Used in spin lock
int count = 0;
//Used in ticket lock
int queue = 0;
int dequeue = 0;

// fetch and swap (for simple spin locks)
static inline int swap(volatile int *mem, int value)
{
    int result;

    __asm __volatile (
        "xchgl %0, %1\n\t"
        : "=r" (result), "=m" (*mem)
        : "0" (value), "m" (*mem));

    return result;
}

static inline int fetch_inc(int *mem, int inc)
{
    __asm __volatile("lock xadd %0,%1"
         : "=r" (inc), "=m" (*mem)
         : "0" (inc)
         : "memory");
    return inc;
}

struct thread_args {
	int * srcbalance;
	int * destbalance;
};

void * bank (void * ptr) {
	struct thread_args *args = (struct thread_args *)ptr;
	int * srcbalance = args->srcbalance;
	int * destbalance = args->destbalance;
	int i = 0;
	int amount;
	for (i = 0; i < 1000000; i++) {
		amount = (rand() % 21) + 1;
		//Spin lock
		/*while(swap(&spinlock, 1)) {count++;}*/
		//fetch lock
		int got_lock =fetch_inc(&queue, 1);
		fprintf(stderr, "%d\n", got_lock);
		while(fetch_inc(&dequeue,0) < got_lock) {count++;}
		/*while(dequeue < got_lock) {count++;}*/
		if(*srcbalance > 0){
			if(*srcbalance < amount)
				amount = *srcbalance;
			if (amount > 0) {
				*srcbalance -= amount;
				*destbalance += amount;
			}
		}
		//Spin Lock
		/*swap(&spinlock, 0);*/
		//fetch lock
		fprintf(stderr, "Dequeue:%d\n", dequeue);
		fetch_inc(&dequeue, 1);
	}
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

	printf("%d %d %d %d\n", balancea, balanceb, balancec, count);

	return 0;
}
